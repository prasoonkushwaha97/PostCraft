package com.example.data.repository

import android.graphics.Bitmap
import android.util.Log
import com.example.data.model.CampaignTheme
import com.example.data.model.EventDetails
import com.example.data.model.GeneratedPostBundle
import com.example.data.model.IssueScope
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
                description = "1-2 sentence objective factual summary of what is observed in the ground reality photo or field context"
            ),
            "suggestedHashtags" to Schema.array(
                items = Schema.string(),
                description = "3 to 6 targeted hashtags for citizen journalism, governance, or urban/rural civic awareness"
            ),
            "alternativeTones" to Schema.array(
                items = Schema.string(),
                description = "2 to 3 alternative editorial angles or tone recommendations for this public issue"
            ),
            "twitterHindi" to Schema.string(
                description = "X (Twitter) post in Hindi under 280 characters with punchy hook, crisp दावा vs हक़ीक़त contrast, tagging relevant authorities/ministers, and 3 hashtags"
            ),
            "twitterEnglish" to Schema.string(
                description = "X (Twitter) post in English under 280 characters with punchy hook, crisp Claim vs Ground Reality contrast, tagging authorities, and 3 hashtags"
            ),
            "instagramFbHindi" to Schema.string(
                description = "Instagram / Facebook post in Hindi with engaging storytelling hook, clear bulleted breakdown of ground facts vs official claims, citizen engagement question, and hashtags"
            ),
            "instagramFbEnglish" to Schema.string(
                description = "Instagram / Facebook post in English with engaging storytelling hook, bulleted breakdown of ground facts vs claims, citizen engagement question, and hashtags"
            ),
            "pressReleaseHindi" to Schema.string(
                description = "Press Release & WhatsApp Bulletin in Hindi: Formal 3-paragraph inverted pyramid press note with headline, dateline, factual findings, civic quote, and accountability demand"
            ),
            "pressReleaseEnglish" to Schema.string(
                description = "Press Release & WhatsApp Bulletin in English: Formal 3-paragraph inverted pyramid press note with headline, dateline, factual findings, civic quote, and accountability demand"
            ),
            "linkedInHindi" to Schema.string(
                description = "LinkedIn & Policy Analysis in Hindi examining public governance gaps, systemic delivery failures, administrative audit, and constructive solutions"
            ),
            "linkedInEnglish" to Schema.string(
                description = "LinkedIn & Policy Analysis in English examining public policy implementation, systemic delivery failures, audit mechanisms, and civic recommendations"
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
     * official portal claims, municipal resolutions, budget status, or recent district administrative orders.
     */
    suspend fun fetchOfficialSchemeSearchGrounding(
        schemeOrClaim: String,
        location: String
    ): Result<Pair<String, List<SearchCitation>>> = withContext(Dispatchers.IO) {
        try {
            val searchQuery = "Government official scheme municipal claim status guidelines: $schemeOrClaim in $location official portal"
            val prompt = """
                You are an investigative citizen journalist fact-checking official government, municipal, or scheme claims.
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
        scope: IssueScope,
        localUnit: String,
        subDistrict: String,
        district: String,
        stateRegion: String,
        claim: String,
        groundReality: String,
        tone: ReportTone,
        visualBitmap: Bitmap?,
        eventDetails: EventDetails = EventDetails(),
        groundedFactsContext: String? = null
    ): Result<GeneratedPostBundle> = withContext(Dispatchers.IO) {
        try {
            val locationSummary = listOf(localUnit, subDistrict, district, stateRegion).filter { it.isNotBlank() }.joinToString(", ")

            val promptText = buildString {
                appendLine("You are an expert citizen journalist and public policy communications specialist.")
                appendLine("Generate universal, multi-platform social media posts analyzing ground reality versus official government or municipal promises.")
                appendLine()
                appendLine("ISSUE PARAMETERS:")
                appendLine("- Issue Category: ${theme.labelHi} / ${theme.labelEn}")
                appendLine("- Geographic Scope: ${scope.labelHi} (${scope.labelEn})")
                appendLine("- Location: $locationSummary")
                appendLine("- Selected Tone: ${tone.labelHi} (${tone.labelEn})")
                appendLine()
                appendLine("FIELD CONTEXT:")
                appendLine("- Official Claim / Promised Scheme: $claim")
                appendLine("- Ground Reality (ज़मीनी हक़ीक़त): $groundReality")

                if (!groundedFactsContext.isNullOrBlank()) {
                    appendLine("- Verified Google Search Grounded Facts: $groundedFactsContext")
                }

                if (eventDetails.hasEvent) {
                    appendLine()
                    appendLine("RELATED CIVIC / ADMINISTRATIVE EVENT (संबंधित आयोजन):")
                    appendLine("- Event Type: ${eventDetails.eventType.labelHi} (${eventDetails.eventType.labelEn})")
                    if (eventDetails.title.isNotBlank()) appendLine("- Title: ${eventDetails.title}")
                    if (eventDetails.date.isNotBlank()) appendLine("- Date & Time: ${eventDetails.date}")
                    if (eventDetails.venue.isNotBlank()) appendLine("- Venue: ${eventDetails.venue}")
                    appendLine("- Is Upcoming: ${eventDetails.isUpcoming}")
                    appendLine("CRITICAL INSTRUCTION: Weave this event prominently into the posts as a mobilization point or hearing forum!")
                }

                if (visualBitmap != null) {
                    appendLine("- Visual Proof Image attached: Analyze this image and provide a concise visual summary reflecting the ground status.")
                }
                appendLine()
                appendLine("PLATFORM OUTPUT REQUIREMENTS:")
                appendLine("1. Twitter (X): STRICTLY under 280 characters! Sharp hook, Claim vs Reality contrast, question to authorities, and 3 hashtags.")
                appendLine("2. Instagram / Facebook: Engaging storytelling caption with a bold hook, bulleted ground facts vs claims, public engagement questions, and structured hashtags.")
                appendLine("3. Press Release & WhatsApp Bulletin: Formal 3-paragraph inverted pyramid press note ready for local reporters and citizen WhatsApp groups, with Headline, Dateline, facts, civic quote, and clear accountability demand.")
                appendLine("4. LinkedIn / Policy Analysis: High-level governance analysis highlighting institutional delivery gaps, audit mechanisms, and constructive civic policy recommendations.")
                appendLine("Provide high-quality, authentic Hindi in Devanagari (no English slashes in Hindi text) and natural, impactful English for each platform.")
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
                instagramFbHindi = json.optString("instagramFbHindi"),
                instagramFbEnglish = json.optString("instagramFbEnglish"),
                pressReleaseHindi = json.optString("pressReleaseHindi"),
                pressReleaseEnglish = json.optString("pressReleaseEnglish"),
                linkedInHindi = json.optString("linkedInHindi"),
                linkedInEnglish = json.optString("linkedInEnglish"),
                suggestedHashtags = hashtagsList,
                alternativeTones = altTonesList,
                visualProofSummary = json.optString("visualProofSummary"),
                handlesToTag = listOf("@CollectorSingrauli", "@MoHUA_India", "@CMMadhyaPradesh"),
                eventDetails = eventDetails,
                scope = scope
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
