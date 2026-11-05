package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

enum class CampaignTheme(val labelHi: String, val labelEn: String) {
    GROUND_REALITY("ज़मीनी हकीकत (Ground Reality vs Official Claims)", "Ground Reality vs Official Claims"),
    RURAL_DEVELOPMENT("ग्रामीण विकास (Rural Development)", "Rural Development"),
    COMMUNITY_EMPOWERMENT("सामुदायिक सशक्तिकरण (Community Empowerment)", "Community Empowerment"),
    EDUCATION_YOUTH("शिक्षा एवं युवा (Education & Youth)", "Education & Youth"),
    LOCAL_ACHIEVEMENTS("स्थानीय उपलब्धियाँ (Local Achievements)", "Local Achievements")
}

enum class ReportTone(val labelHi: String, val labelEn: String) {
    INVESTIGATIVE("खोजी व तार्किक (Investigative & Direct)", "Investigative & Direct"),
    CONSTRUCTIVE("सकारात्मक आलोचना (Constructive Critique)", "Constructive Critique"),
    STORYTELLING("ज़मीनी दास्तान (Storytelling / Ground Report)", "Storytelling / Ground Report")
}

enum class SocialPlatform(val displayName: String, val iconName: String, val charLimit: Int?) {
    TWITTER("Twitter (X)", "ic_x", 280),
    INSTAGRAM("Instagram", "ic_instagram", 2200),
    LINKEDIN("LinkedIn", "ic_linkedin", 3000),
    FACEBOOK("Facebook", "ic_facebook", null)
}

enum class LanguageMode(val label: String) {
    HINDI("हिन्दी"),
    ENGLISH("English")
}

enum class EventType(val labelHi: String, val labelEn: String) {
    NONE("कोई विशेष आयोजन नहीं", "None"),
    GRAM_SABHA("ग्राम सभा बैठक (Gram Sabha)", "Gram Sabha Meeting"),
    JAN_SUNWAI("जनसुनवाई (Jan Sunwai)", "Public Hearing (Jan Sunwai)"),
    OFFICIAL_INSPECTION("प्रशासनिक दौरा / जांच (Official Inspection)", "Administrative Inspection"),
    PANCHAYAT_MEETING("पंचायत बैठक (Panchayat Meeting)", "Panchayat Meeting"),
    PROTEST_DHARNA("धरना / शांतिपूर्ण प्रदर्शन (Protest / Dharna)", "Protest / Dharna"),
    COMMUNITY_ACTION("सामुदायिक शिविर / बैठक (Community Camp)", "Community Camp / Meeting")
}

data class EventDetails(
    val eventType: EventType = EventType.NONE,
    val title: String = "",
    val date: String = "",
    val venue: String = "",
    val isUpcoming: Boolean = true
) {
    val hasEvent: Boolean
        get() = eventType != EventType.NONE || title.isNotBlank()

    fun getFormattedBadge(): String {
        if (!hasEvent) return ""
        val parts = mutableListOf<String>()
        if (title.isNotBlank()) parts.add(title) else parts.add(eventType.labelHi)
        if (date.isNotBlank()) parts.add(date)
        if (venue.isNotBlank()) parts.add(venue)
        return parts.joinToString(" | ")
    }
}

data class SearchCitation(
    val title: String,
    val url: String
)

data class GeneratedPostBundle(
    val twitterHindi: String = "",
    val twitterEnglish: String = "",
    val instagramHindi: String = "",
    val instagramEnglish: String = "",
    val linkedInHindi: String = "",
    val linkedInEnglish: String = "",
    val facebookHindi: String = "",
    val facebookEnglish: String = "",
    val suggestedHashtags: List<String> = emptyList(),
    val alternativeTones: List<String> = emptyList(),
    val visualProofSummary: String = "",
    val handlesToTag: List<String> = emptyList(),
    val searchQueriesUsed: List<String> = emptyList(),
    val searchCitations: List<SearchCitation> = emptyList(),
    val eventDetails: EventDetails = EventDetails()
) {
    fun getPost(platform: SocialPlatform, language: LanguageMode): String {
        return when (platform) {
            SocialPlatform.TWITTER -> if (language == LanguageMode.HINDI) twitterHindi else twitterEnglish
            SocialPlatform.INSTAGRAM -> if (language == LanguageMode.HINDI) instagramHindi else instagramEnglish
            SocialPlatform.LINKEDIN -> if (language == LanguageMode.HINDI) linkedInHindi else linkedInEnglish
            SocialPlatform.FACEBOOK -> if (language == LanguageMode.HINDI) facebookHindi else facebookEnglish
        }
    }
}

@IgnoreExtraProperties
data class UserProfile(
    @DocumentId val userId: String = "",
    val email: String = "",
    val displayName: String = "",
    val photoUrl: String? = null,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class CampaignReport(
    @DocumentId val id: String = "",
    val userId: String = "",
    val theme: String = CampaignTheme.GROUND_REALITY.name,
    val village: String = "",
    val tehsil: String = "",
    val district: String = "",
    val state: String = "Madhya Pradesh",
    val claim: String = "",
    val groundReality: String = "",
    val tone: String = ReportTone.INVESTIGATIVE.name,
    val visualProofSummary: String? = null,
    val suggestedHashtags: List<String> = emptyList(),
    val alternativeTones: List<String> = emptyList(),
    val eventType: String = EventType.NONE.name,
    val eventTitle: String = "",
    val eventDate: String = "",
    val eventVenue: String = "",
    val isUpcomingEvent: Boolean = true,
    val twitterHindi: String = "",
    val twitterEnglish: String = "",
    val instagramHindi: String = "",
    val instagramEnglish: String = "",
    val linkedInHindi: String = "",
    val linkedInEnglish: String = "",
    val facebookHindi: String = "",
    val facebookEnglish: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
) {
    fun toPostBundle(): GeneratedPostBundle {
        val event = EventDetails(
            eventType = EventType.entries.find { it.name == eventType } ?: EventType.NONE,
            title = eventTitle,
            date = eventDate,
            venue = eventVenue,
            isUpcoming = isUpcomingEvent
        )
        return GeneratedPostBundle(
            twitterHindi = twitterHindi,
            twitterEnglish = twitterEnglish,
            instagramHindi = instagramHindi,
            instagramEnglish = instagramEnglish,
            linkedInHindi = linkedInHindi,
            linkedInEnglish = linkedInEnglish,
            facebookHindi = facebookHindi,
            facebookEnglish = facebookEnglish,
            suggestedHashtags = suggestedHashtags,
            alternativeTones = alternativeTones,
            visualProofSummary = visualProofSummary ?: "",
            handlesToTag = listOf("@CollectorSingrauli", "@MoJSDoWRRDGR", "@CMMadhyaPradesh"),
            eventDetails = event
        )
    }
}
