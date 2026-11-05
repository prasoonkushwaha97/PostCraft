package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

enum class LanguageMode(val label: String) {
    HINDI("हिंदी"),
    ENGLISH("English")
}

enum class IssueScope(val labelHi: String, val labelEn: String) {
    VILLAGE("गाँव (Rural / Village)", "Village / Rural"),
    CITY("शहर व वार्ड (Urban / Ward)", "City / Urban Ward"),
    DISTRICT("ज़िला व मंडल (District / Regional)", "District / Regional"),
    STATE_NATIONAL("राज्य व राष्ट्रीय (State / National)", "State / National");

    fun label(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) labelHi else labelEn
}

enum class CampaignTheme(
    val icon: String,
    val labelHi: String,
    val labelEn: String,
    val descHi: String,
    val descEn: String
) {
    URBAN_CIVIC(
        "🏙️",
        "शहरी मुद्दे (सड़क, जलभराव, सीवरेज, प्रदूषण)",
        "Urban Civic (Roads, Waterlogging, Pollution, Traffic)",
        "सड़क, जलभराव, सीवरेज, वायु प्रदूषण, कचरा प्रबंधन, ट्रैफिक जाम",
        "Roads, waterlogging, drainage, air pollution, waste management, traffic"
    ),
    RURAL_AGRICULTURE(
        "🌾",
        "ग्रामीण विकास व कृषि (नल जल, सड़क, मंडी, पंचायत)",
        "Rural & Agriculture (Water, PMGSY, Canal, Mandi, Panchayat)",
        "नल जल, PMGSY सड़कें, नहर, खाद-बीज, कृषि मंडी, पंचायत कोष",
        "Tap water, rural roads, canal irrigation, seeds/fertilizers, mandi, panchayat funds"
    ),
    GOVERNANCE_POLICY(
        "🏛️",
        "प्रशासन व नीति (योजनाएँ, RTI, बजट, भ्रष्टाचार, जनसुनवाई)",
        "Governance & Policy (Schemes, RTI, Budget, Anti-Corruption)",
        "सरकारी योजनाएँ, आरटीआई खुलासे, बजट आवंटन, भ्रष्टाचार, जनसुनवाई",
        "Government schemes, RTI revelations, budget allocations, anti-corruption, public hearings"
    ),
    EDUCATION_HEALTHCARE(
        "🎓",
        "शिक्षा व स्वास्थ्य (स्कूल, अस्पताल, भर्ती परीक्षा, छात्रवृत्ति)",
        "Education & Healthcare (Schools, Hospitals, Exams, Scholarship)",
        "सरकारी स्कूल, प्राथमिक स्वास्थ्य केंद्र, भर्ती परीक्षा, छात्रवृत्ति, दवाइयाँ",
        "Government schools, primary health centers, recruitment exams, scholarships, medicines"
    ),
    LEGAL_RIGHTS(
        "⚖️",
        "कानून व जन-अधिकार (FIR, पुलिस, उपभोक्ता, मानवाधिकार)",
        "Legal & Rights (FIR, Police, Consumer Rights, Human Rights)",
        "एफआईआर व पुलिस कार्रवाई, उपभोक्ता शोषण, नागरिक अधिकार, मानवाधिकार",
        "FIR and police accountability, consumer fraud, civil liberties, human rights"
    );

    fun label(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) labelHi else labelEn
    fun desc(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) descHi else descEn
}

enum class ReportTone(val labelHi: String, val labelEn: String) {
    INVESTIGATIVE("खोजी व तार्किक", "Investigative & Direct"),
    CONSTRUCTIVE("सकारात्मक आलोचना", "Constructive Critique"),
    STORYTELLING("ज़मीनी दास्तान", "Storytelling & Field Report");

    fun label(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) labelHi else labelEn
}

enum class SocialPlatform(
    val displayName: String,
    val iconName: String,
    val charLimit: Int?,
    val badgeHi: String,
    val badgeEn: String
) {
    TWITTER("X (Twitter)", "ic_x", 280, "त्वरित प्रभाव", "Quick Impact"),
    INSTAGRAM_FB("Instagram / Facebook", "ic_instagram", 2200, "स्टोरीटेलिंग", "Storytelling"),
    PRESS_RELEASE("Press Release / WhatsApp", "ic_press", 4000, "मीडिया बुलेटिन", "Media Bulletin"),
    LINKEDIN("LinkedIn / Policy", "ic_linkedin", 3000, "नीति विश्लेषण", "Policy Analysis");

    fun badge(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) badgeHi else badgeEn
}

enum class EventType(val labelHi: String, val labelEn: String) {
    NONE("कोई विशेष आयोजन नहीं", "None"),
    JAN_SUNWAI("जनसुनवाई / जन-शिकायत", "Public Hearing (Jan Sunwai)"),
    GRAM_SABHA("ग्राम सभा / मोहल्ला सभा", "Gram Sabha / Ward Sabhas"),
    OFFICIAL_INSPECTION("प्रशासनिक दौरा व जांच", "Administrative Inspection"),
    CITIZEN_PRESS_CONFERENCE("नागरिक प्रेस वार्ता / ब्रीफिंग", "Citizen Press Briefing"),
    PROTEST_DHARNA("शांतिपूर्ण धरना प्रदर्शन", "Peaceful Protest / Dharna"),
    COMMUNITY_MEETING("नागरिक मंच / टाउनहॉल बैठक", "Citizen Forum / Townhall");

    fun label(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) labelHi else labelEn
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

    fun getFormattedBadge(mode: LanguageMode = LanguageMode.HINDI): String {
        if (!hasEvent) return ""
        val parts = mutableListOf<String>()
        if (title.isNotBlank()) parts.add(title) else parts.add(eventType.label(mode))
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
    val instagramFbHindi: String = "",
    val instagramFbEnglish: String = "",
    val pressReleaseHindi: String = "",
    val pressReleaseEnglish: String = "",
    val linkedInHindi: String = "",
    val linkedInEnglish: String = "",
    val suggestedHashtags: List<String> = emptyList(),
    val alternativeTones: List<String> = emptyList(),
    val visualProofSummary: String = "",
    val handlesToTag: List<String> = emptyList(),
    val searchQueriesUsed: List<String> = emptyList(),
    val searchCitations: List<SearchCitation> = emptyList(),
    val eventDetails: EventDetails = EventDetails(),
    val scope: IssueScope = IssueScope.VILLAGE
) {
    fun getPost(platform: SocialPlatform, language: LanguageMode): String {
        return when (platform) {
            SocialPlatform.TWITTER -> if (language == LanguageMode.HINDI) twitterHindi else twitterEnglish
            SocialPlatform.INSTAGRAM_FB -> if (language == LanguageMode.HINDI) instagramFbHindi else instagramFbEnglish
            SocialPlatform.PRESS_RELEASE -> if (language == LanguageMode.HINDI) pressReleaseHindi else pressReleaseEnglish
            SocialPlatform.LINKEDIN -> if (language == LanguageMode.HINDI) linkedInHindi else linkedInEnglish
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

enum class IssueStatus(val labelHi: String, val labelEn: String, val colorHex: String) {
    UNDER_INVESTIGATION("जांच व पड़ताल जारी", "Under Investigation", "#EF4444"),
    IN_REVIEW("विभाग द्वारा संज्ञान", "Department Acknowledged", "#F59E0B"),
    RESOLVED("समाधान व कार्रवाई पूर्ण", "Resolved / Action Taken", "#10B981");

    fun label(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) labelHi else labelEn
}

enum class StudioTab(val labelHi: String, val labelEn: String) {
    INTAKE("ग्राउंड स्टूडियो", "Ground Studio"),
    SOCIAL_DECK("सोशल डेक", "Social Deck"),
    IMPACT_HUB("प्रभाव व आर्काइव", "Impact & Archive");

    fun label(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) labelHi else labelEn
}

@IgnoreExtraProperties
data class CampaignReport(
    @DocumentId val id: String = "",
    val userId: String = "",
    val theme: String = CampaignTheme.URBAN_CIVIC.name,
    val scope: String = IssueScope.VILLAGE.name,
    val localUnit: String = "", // Village or Ward/Locality
    val subDistrict: String = "", // Tehsil/Block or City/Municipality
    val district: String = "",
    val stateRegion: String = "Madhya Pradesh",
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
    val status: String = IssueStatus.UNDER_INVESTIGATION.name,
    val resolutionNotes: String = "",
    val twitterHindi: String = "",
    val twitterEnglish: String = "",
    val instagramFbHindi: String = "",
    val instagramFbEnglish: String = "",
    val pressReleaseHindi: String = "",
    val pressReleaseEnglish: String = "",
    val linkedInHindi: String = "",
    val linkedInEnglish: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
) {
    val village: String get() = localUnit
    val tehsil: String get() = subDistrict
    val state: String get() = stateRegion
    val instagramHindi: String get() = instagramFbHindi
    val instagramEnglish: String get() = instagramFbEnglish
    val facebookHindi: String get() = instagramFbHindi
    val facebookEnglish: String get() = instagramFbEnglish
    val issueStatus: IssueStatus get() = IssueStatus.entries.find { it.name == status } ?: IssueStatus.UNDER_INVESTIGATION

    fun toPostBundle(): GeneratedPostBundle {
        val event = EventDetails(
            eventType = EventType.entries.find { it.name == eventType } ?: EventType.NONE,
            title = eventTitle,
            date = eventDate,
            venue = eventVenue,
            isUpcoming = isUpcomingEvent
        )
        val issueScope = IssueScope.entries.find { it.name == scope } ?: IssueScope.VILLAGE
        return GeneratedPostBundle(
            twitterHindi = twitterHindi,
            twitterEnglish = twitterEnglish,
            instagramFbHindi = instagramFbHindi,
            instagramFbEnglish = instagramFbEnglish,
            pressReleaseHindi = pressReleaseHindi,
            pressReleaseEnglish = pressReleaseEnglish,
            linkedInHindi = linkedInHindi,
            linkedInEnglish = linkedInEnglish,
            suggestedHashtags = suggestedHashtags,
            alternativeTones = alternativeTones,
            visualProofSummary = visualProofSummary ?: "",
            handlesToTag = listOf("@CollectorSingrauli", "@MoHUA_India", "@CMMadhyaPradesh"),
            eventDetails = event,
            scope = issueScope
        )
    }
}
