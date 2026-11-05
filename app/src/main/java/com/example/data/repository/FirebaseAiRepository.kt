package com.example.data.repository

import android.graphics.Bitmap
import android.util.Log
import com.example.data.model.CampaignTheme
import com.example.data.model.EventDetails
import com.example.data.model.GeneratedPostBundle
import com.example.data.model.ReportTone
import com.example.data.model.SearchCitation
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.Tool
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.json.JSONObject

private const val TAG = "FirebaseAiRepository"

class FirebaseAiRepository {

    // 1. Define schema for structured JSON output
    private val postGenerationSchema = Schema.obj(
        properties = mapOf(
            "visualProofSummary" to Schema.string(
                description = "1-2 sentence objective summary of what is seen in the ground reality photo or field context"
            ),
            "suggestedHashtags" to Schema.array(
                items = Schema.string(),
                description = "3 to 6 targeted hashtags for citizen journalism and rural governance"
            ),
            "alternativeTones" to Schema.array(
                items = Schema.string(),
                description = "2 to 3 alternative angles or tone suggestions for this story"
            ),
            "twitterHindi" to Schema.string(
                description = "Twitter / X post in Hindi, punchy hook, contrast (दावा vs हकीकत), event notice if applicable, under 280 characters with tags and hashtags"
            ),
            "twitterEnglish" to Schema.string(
                description = "Twitter / X post in English, punchy hook, contrast (Claim vs Reality), event notice if applicable, under 280 characters with tags and hashtags"
            ),
            "instagramHindi" to Schema.string(
                description = "Instagram caption in Hindi with emotional storytelling hook, bullet points of reality vs claims, event announcement/CTA, and structured hashtags"
            ),
            "instagramEnglish" to Schema.string(
                description = "Instagram caption in English with emotional storytelling hook, bullet points of reality vs claims, event announcement/CTA, and structured hashtags"
            ),
            "linkedInHindi" to Schema.string(
                description = "LinkedIn post in Hindi with public policy implementation analysis, Gram Panchayat accountability, event relevance, and constructive recommendations"
            ),
            "linkedInEnglish" to Schema.string(
                description = "LinkedIn post in English with public policy implementation analysis, Gram Panchayat accountability, event relevance, and constructive recommendations"
            ),
            "facebookHindi" to Schema.string(
                description = "Facebook community post in Hindi in conversational tone inviting locals to attend the event, comment, and tag representatives"
            ),
            "facebookEnglish" to Schema.string(
                description = "Facebook community post in English in conversational tone inviting locals to attend the event, comment, and tag representatives"
            )
        )
    )

    private val config = generationConfig {
        responseMimeType = "application/json"
        responseSchema = postGenerationSchema
    }

    // Lazy initialization of Gemini 3.8 Flash model for structured social post generation
    private val model by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash",
            generationConfig = config
        )
    }

    // Grounded model with Google Search tool enabled
    private val searchGroundedModel by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash",
            tools = listOf(Tool.googleSearch())
        )
    }

    /**
     * Executes Google Search Grounding to verify real-time government scheme guidelines,
     * official portal claims, budget status, or recent district administrative orders.
     */
    suspend fun fetchOfficialSchemeSearchGrounding(
        schemeOrClaim: String,
        location: String
    ): Result<Pair<String, List<SearchCitation>>> = withContext(Dispatchers.IO) {
        try {
            val searchQuery = "Government scheme facts guidelines status: $schemeOrClaim in $location India official portal"
            val prompt = """
                You are a research journalist fact-checking official government data.
                Using Google Search, find the official guidelines, latest news, and verified administrative status for:
                Query: $searchQuery
                Location: $location
                Provide a factual, neutral 3-4 sentence verification of what the government guidelines, timeline, or scheme portal promise for this area.
            """.trimIndent()

            val response = withTimeout(45_000L) {
                searchGroundedModel.generateContent(prompt)
            }

            val text = response.text.orEmpty()
            val candidate = response.candidates.firstOrNull()
            val groundingMetadata = candidate?.groundingMetadata

            val citations = mutableListOf<SearchCitation>()
            groundingMetadata?.groundingChunks?.forEach { chunk ->
                val web = chunk.web
                if (web != null && !web.uri.isNullOrBlank()) {
                    citations.add(
                        SearchCitation(
                            title = web.title?.ifBlank { "Official Scheme Source" } ?: "Official Scheme Source",
                            url = web.uri ?: ""
                        )
                    )
                }
            }

            Result.success(Pair(text, citations.distinctBy { it.url }))
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "Search grounding timed out", e)
            Result.failure(e)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Error in Google Search grounding", e)
            Result.failure(e)
        }
    }

    suspend fun generateSocialPosts(
        theme: CampaignTheme,
        village: String,
        tehsil: String,
        district: String,
        state: String,
        claim: String,
        groundReality: String,
        tone: ReportTone,
        visualBitmap: Bitmap?,
        eventDetails: EventDetails = EventDetails(),
        groundedFactsContext: String? = null
    ): Result<GeneratedPostBundle> = withContext(Dispatchers.IO) {
        try {
            val promptText = buildString {
                appendLine("You are an expert citizen journalist and rural governance communications specialist for Saathi Foundation.")
                appendLine("Generate multi-platform social media posts analyzing ground reality versus official government promises.")
                appendLine()
                appendLine("CAMPAIGN DETAILS:")
                appendLine("- Campaign Theme: ${theme.labelHi} / ${theme.labelEn}")
                appendLine("- Village: $village")
                appendLine("- Tehsil/Block: $tehsil")
                appendLine("- District: $district")
                appendLine("- State: $state")
                appendLine("- Selected Tone: ${tone.labelHi} (${tone.labelEn})")
                appendLine()
                appendLine("FIELD CONTEXT:")
                appendLine("- Official Claim / Paper Scheme: $claim")
                appendLine("- Ground Reality (ज़मीनी हकीकत): $groundReality")

                if (!groundedFactsContext.isNullOrBlank()) {
                    appendLine("- Verified Google Search Grounded Facts: $groundedFactsContext")
                }

                if (eventDetails.hasEvent) {
                    appendLine()
                    appendLine("RELATED EVENT / OCCASION (संबंधित आयोजन):")
                    appendLine("- Event Type: ${eventDetails.eventType.labelHi} (${eventDetails.eventType.labelEn})")
                    if (eventDetails.title.isNotBlank()) appendLine("- Title: ${eventDetails.title}")
                    if (eventDetails.date.isNotBlank()) appendLine("- Date & Time: ${eventDetails.date}")
                    if (eventDetails.venue.isNotBlank()) appendLine("- Venue / Location: ${eventDetails.venue}")
                    appendLine("- Is Upcoming: ${eventDetails.isUpcoming}")
                    appendLine("CRITICAL EVENT INSTRUCTION: Feature this related event prominently in the posts!")
                    if (eventDetails.isUpcoming) {
                        appendLine("Urge local villagers to attend the event (${eventDetails.title.ifBlank { eventDetails.eventType.labelHi }} on ${eventDetails.date} at ${eventDetails.venue}), ask accountability questions, and present this ground reality evidence!")
                    } else {
                        appendLine("Report on what occurred during this event, contrasting official statements with the ground evidence!")
                    }
                }

                if (visualBitmap != null) {
                    appendLine("- Visual Proof Image attached: Analyze this image and provide a concise visual summary reflecting the ground status.")
                }
                appendLine()
                appendLine("PLATFORM INSTRUCTIONS:")
                appendLine("1. Twitter (X): Strict constraint - MUST be under 280 characters! Include hook, concise दावा vs हकीकत contrast, event call if present, question to authorities (@CollectorSingrauli or relevant), and 3-4 hashtags.")
                appendLine("2. Instagram: Storytelling caption, engaging hook, bullet points contrasting Claim vs Ground Reality, clear Call to Action (CTA) mentioning the event date/venue if present, and structured hashtags.")
                appendLine("3. LinkedIn: Professional policy and public administration analysis highlighting scheme delivery gaps, Panchayat accountability, the significance of the forum/event, and constructive civic solutions.")
                appendLine("4. Facebook: Conversational, community-friendly tone inviting local citizens to attend the event, discuss in comments, and tag local representatives.")
                appendLine("5. Hashtags & Tones: Suggest targeted hashtags and alternative tone angles.")
                appendLine("Generate accurate, authentic Hindi in Devanagari and natural, impactful English for each platform.")
            }

            val response = withTimeout(60_000L) {
                if (visualBitmap != null) {
                    model.generateContent(
                        content {
                            image(visualBitmap)
                            text(promptText)
                        }
                    )
                } else {
                    model.generateContent(promptText)
                }
            }

            val jsonText = response.text ?: throw IllegalStateException("Gemini returned empty response text.")
            Log.d(TAG, "Gemini response JSON: $jsonText")

            val json = JSONObject(jsonText)
            val suggestedHashtagsJson = json.optJSONArray("suggestedHashtags")
            val hashtagsList = mutableListOf<String>()
            if (suggestedHashtagsJson != null) {
                for (i in 0 until suggestedHashtagsJson.length()) {
                    hashtagsList.add(suggestedHashtagsJson.getString(i))
                }
            }

            val altTonesJson = json.optJSONArray("alternativeTones")
            val altTonesList = mutableListOf<String>()
            if (altTonesJson != null) {
                for (i in 0 until altTonesJson.length()) {
                    altTonesList.add(altTonesJson.getString(i))
                }
            }

            val bundle = GeneratedPostBundle(
                twitterHindi = json.optString("twitterHindi"),
                twitterEnglish = json.optString("twitterEnglish"),
                instagramHindi = json.optString("instagramHindi"),
                instagramEnglish = json.optString("instagramEnglish"),
                linkedInHindi = json.optString("linkedInHindi"),
                linkedInEnglish = json.optString("linkedInEnglish"),
                facebookHindi = json.optString("facebookHindi"),
                facebookEnglish = json.optString("facebookEnglish"),
                suggestedHashtags = hashtagsList,
                alternativeTones = altTonesList,
                visualProofSummary = json.optString("visualProofSummary"),
                handlesToTag = listOf("@CollectorSingrauli", "@MoJSDoWRRDGR", "@CMMadhyaPradesh"),
                eventDetails = eventDetails
            )

            Result.success(bundle)
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "Generation timed out after 60s", e)
            Result.failure(e)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Error generating posts with Gemini", e)
            Result.failure(e)
        }
    }
}
