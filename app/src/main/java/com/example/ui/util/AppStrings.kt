package com.example.ui.util

import com.example.data.model.IssueScope
import com.example.data.model.LanguageMode

object AppStrings {
    // Header & App Bar - PostCraft Rebranded
    fun appTitle(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "पोस्ट क्राफ्ट" else "PostCraft"
    fun appSubtitle(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) 
        "सोशल मीडिया पोस्ट एवं ग्राउंड रिपोर्ट जनरेटर" 
    else 
        "Social Deck & Ground Reality Post Generator"

    // Auth Bar
    fun orgTitle(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "पोस्ट क्राफ्ट | PostCraft" else "PostCraft | Social Deck"
    fun orgSubtitle(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) 
        "सिटिजन जर्नलिज्म एवं जन-जवाबदेही" 
    else 
        "Citizen Journalism & Public Accountability"
    fun defaultReporter(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "नागरिक पत्रकार" else "Citizen Journalist"
    fun verifiedReporter(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "सत्यापित रिपोर्टर" else "Verified Reporter"
    fun signInGoogle(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "Google से साइन इन करें" else "Sign In with Google"
    fun signingIn(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "साइन इन हो रहा है..." else "Signing In..."
    fun logout(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "लॉगआउट" else "Logout"

    // Navigation Tabs
    fun tabGroundForm(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "1. जन-मुद्दा फ़ॉर्म" else "1. Public Issue Form"
    fun tabSocialDeck(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "2. सोशल डेक" else "2. Social Deck"

    // Intake Form Header
    fun intakeTitle(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "जन-मुद्दा एवं ज़मीनी पड़ताल" else "Public Issue & Ground Reality"
    fun intakeSubtitle(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) 
        "शहरी, ग्रामीण या प्रशासनिक मुद्दे दर्ज करें और तुरंत मल्टी-प्लेटफ़ॉर्म पोस्ट तैयार करें।" 
    else 
        "Log urban, rural, policy, or rights issues to auto-generate multi-platform posts."

    // Quick Presets
    fun quickPresets(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "त्वरित उदाहरण (शहरी व ग्रामीण):" else "Quick Presets (Urban & Rural):"
    fun presetUrbanCivic(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "🏙️ इंदौर वार्ड 24 (जलभराव व सीवरेज)" else "🏙️ Ward 24 (Waterlogging & Drainage)"
    fun presetRuralWater(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "🌾 छमरछ (नल जल मिशन)" else "🌾 Chhamrachh (Jal Jeevan)"
    fun presetGovernance(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "🏛️ ज़िला अस्पताल (दवा आपूर्ति व डॉक्टर)" else "🏛️ District Hospital (Medicine Shortage)"
    fun presetRoad(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "🛣️ बरगवां (प्रधानमंत्री सड़क)" else "🛣️ Bargawan (Rural Road)"

    // Issue Scope
    fun issueScopeLabel(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "भौगोलिक दायरा (Issue Scope)" else "Issue Scope"
    fun campaignThemeLabel(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "मुद्दे का प्रकार (Category)" else "Issue Category"

    // Administrative Coordinates based on IssueScope
    fun adminCoordsTitle(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "स्थान व प्रशासनिक विवरण" else "Location & Administrative Coordinates"

    fun localUnitLabel(scope: IssueScope, mode: LanguageMode): String {
        return when (scope) {
            IssueScope.VILLAGE -> if (mode == LanguageMode.HINDI) "गाँव" else "Village"
            IssueScope.CITY -> if (mode == LanguageMode.HINDI) "वार्ड या मोहल्ला" else "Ward / Locality"
            IssueScope.DISTRICT -> if (mode == LanguageMode.HINDI) "क्षेत्रीय प्रभाग" else "Regional Division"
            IssueScope.STATE_NATIONAL -> if (mode == LanguageMode.HINDI) "विभाग या आयोग" else "Department / Agency"
        }
    }

    fun localUnitPlaceholder(scope: IssueScope, mode: LanguageMode): String {
        return when (scope) {
            IssueScope.VILLAGE -> if (mode == LanguageMode.HINDI) "छमरछ" else "Chhamrachh"
            IssueScope.CITY -> if (mode == LanguageMode.HINDI) "वार्ड नं. 24, तिलक नगर" else "Ward 24, Sector 4"
            IssueScope.DISTRICT -> if (mode == LanguageMode.HINDI) "औद्योगिक प्रभाग" else "Industrial Zone"
            IssueScope.STATE_NATIONAL -> if (mode == LanguageMode.HINDI) "लोक स्वास्थ्य यांत्रिकी विभाग" else "Health & Family Welfare"
        }
    }

    fun subDistrictLabel(scope: IssueScope, mode: LanguageMode): String {
        return when (scope) {
            IssueScope.VILLAGE -> if (mode == LanguageMode.HINDI) "तहसील" else "Tehsil"
            IssueScope.CITY -> if (mode == LanguageMode.HINDI) "शहर या नगर निगम" else "City / Municipality"
            IssueScope.DISTRICT -> if (mode == LanguageMode.HINDI) "विकासखंड" else "Block / Subdivision"
            IssueScope.STATE_NATIONAL -> if (mode == LanguageMode.HINDI) "कार्यक्षेत्र" else "Sector / Domain"
        }
    }

    fun subDistrictPlaceholder(scope: IssueScope, mode: LanguageMode): String {
        return when (scope) {
            IssueScope.VILLAGE -> if (mode == LanguageMode.HINDI) "सरई" else "Sarai"
            IssueScope.CITY -> if (mode == LanguageMode.HINDI) "इंदौर नगर निगम" else "Indore Municipal Corp"
            IssueScope.DISTRICT -> if (mode == LanguageMode.HINDI) "देवसर" else "Deosar"
            IssueScope.STATE_NATIONAL -> if (mode == LanguageMode.HINDI) "शहरी विकास" else "Urban Infrastructure"
        }
    }

    fun districtLabel(scope: IssueScope, mode: LanguageMode): String {
        return if (scope == IssueScope.STATE_NATIONAL) {
            if (mode == LanguageMode.HINDI) "राज्य" else "State"
        } else {
            if (mode == LanguageMode.HINDI) "ज़िला" else "District"
        }
    }

    fun districtPlaceholder(scope: IssueScope, mode: LanguageMode): String {
        return if (scope == IssueScope.STATE_NATIONAL) {
            if (mode == LanguageMode.HINDI) "मध्य प्रदेश" else "Madhya Pradesh"
        } else {
            if (mode == LanguageMode.HINDI) "सिंगरौली" else "Singrauli"
        }
    }

    fun stateLabel(scope: IssueScope, mode: LanguageMode): String {
        return if (scope == IssueScope.STATE_NATIONAL) {
            if (mode == LanguageMode.HINDI) "देश" else "Country"
        } else {
            if (mode == LanguageMode.HINDI) "राज्य" else "State"
        }
    }

    fun statePlaceholder(scope: IssueScope, mode: LanguageMode): String {
        return if (scope == IssueScope.STATE_NATIONAL) {
            if (mode == LanguageMode.HINDI) "भारत" else "India"
        } else {
            if (mode == LanguageMode.HINDI) "मध्य प्रदेश" else "Madhya Pradesh"
        }
    }

    // Claim vs Reality
    fun claimBadge(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "सरकारी दावा / योजना वादा" else "Official Promise / Scheme Claim"
    fun claimPlaceholder(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) 
        "योजना, नगर निगम या प्रशासन का क्या दावा है? (जैसे स्मार्ट ड्रेनेज, 100% जल आपूर्ति, 24 घंटे अस्पताल)" 
    else 
        "What is the official promise or scheme claim? (e.g. smart drainage, 100% tap water, 24x7 hospital)"
    fun realityBadge(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "ज़मीनी हक़ीक़त" else "Ground Reality Fact"
    fun realityPlaceholder(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) 
        "धरातल पर वास्तविक स्थिति क्या है? (जैसे नालियां जाम हैं, पानी भरता है, पाइपलाइन टूटी है)" 
    else 
        "What is the actual field status? (e.g. drains choked, waterlogging, pipeline broken, doctors absent)"

    // Google Search Grounding
    fun verifySchemeSearch(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "Google सर्च द्वारा योजना सत्यापित करें" else "Verify Official Scheme via Google Search"
    fun verifyingSchemeSearch(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "Google सर्च से सत्यापन जारी..." else "Verifying via Google Search..."
    fun verifiedSchemeTitle(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "सत्यापित योजना तथ्य (Google ग्राउंडेड)" else "Verified Scheme Facts (Google Grounded)"
    fun officialSources(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "आधिकारिक स्रोत:" else "Official Web Sources:"

    // Related Event
    fun eventSectionTitle(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "संबंधित आयोजन या नागरिक फोरम" else "Related Event / Public Forum"
    fun eventSectionSubtitle(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) 
        "जनसुनवाई, प्रेस वार्ता, वार्ड सभा या धरना जोड़ें ताकि नागरिकों को जागरूक किया जा सके।" 
    else 
        "Associate a public hearing, press conference, ward meeting, or protest to mobilize citizens."
    fun eventTypeLabel(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "आयोजन का प्रकार:" else "Event Type:"
    fun eventTitleLabel(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "आयोजन का शीर्षक" else "Event Title"
    fun eventTitlePlaceholder(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "जैसे: कलेक्टर जनसुनवाई, नागरिक टाउनहॉल, विशेष वार्ड सभा" else "e.g. Public Hearing, Citizen Townhall, Ward Sabha"
    fun eventDateLabel(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "दिनांक व समय" else "Date & Time"
    fun eventDatePlaceholder(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "मंगलवार 11:00 AM" else "Tuesday 11:00 AM"
    fun eventVenueLabel(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "स्थान" else "Venue"
    fun eventVenuePlaceholder(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "कलेक्ट्रेट, नगर निगम या सामुदायिक भवन" else "Collectorate, Municipal Hall, Community Center"
    fun upcomingEvent(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "आगामी आयोजन" else "Upcoming Event"
    fun pastEvent(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "बीता आयोजन" else "Past Event"
    fun upcomingEventDesc(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) 
        "पोस्ट में नागरिकों से आयोजन में पहुँचने की अपील होगी।" 
    else 
        "Posts will urge citizens to attend and demand accountability."
    fun pastEventDesc(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) 
        "पोस्ट में बैठक के निष्कर्ष व अधिकारियों से की गई मांगें होंगी।" 
    else 
        "Posts will summarize findings and formal demands raised."

    // Visual Proof
    fun visualProofTitle(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "तस्वीर या साक्ष्य प्रमाण" else "Visual Proof"
    fun takePhoto(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "फोटो खींचें" else "Take Photo"
    fun retakePhoto(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "पुनः फोटो लें" else "Retake Photo"
    fun pickGallery(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "गैलरी से चुनें" else "Pick Gallery"
    fun visualProofDesc(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) 
        "📸 टूटे पाइप, जलभराव या गड्ढों की तस्वीर लें, ताकि AI साक्ष्य विश्लेषण कर सके।" 
    else 
        "📸 Capture real ground evidence for Gemini multimodal analysis."

    // Tone
    fun toneLabel(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "रिपोर्ट की शैली" else "Tone of Report"

    // Primary CTA
    fun generatePosts(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "सोशल मीडिया व प्रेस पोस्ट तैयार करें" else "Generate Social Posts & Press Release"
    fun generatingPosts(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "पोस्ट तैयार हो रही हैं..." else "Generating 4 Platform Formats..."

    // Social Deck
    fun socialDeckTitle(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "सोशल मीडिया व प्रेस डेक" else "Multi-Platform Social & Press Deck"
    fun socialDeckSubtitle(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) 
        "एक्स (Twitter), इंस्टाग्राम, प्रेस विज्ञप्ति व लिंक्डइन हेतु अलग-अलग प्रारूप।" 
    else 
        "Platform-tailored drafts for X, Instagram, Press Release & LinkedIn."
    fun noPostsYet(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "अभी कोई पोस्ट तैयार नहीं है" else "No Posts Generated Yet"
    fun fillDetailsNotice(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) 
        "बाईं ओर विवरण भरें और 'पोस्ट तैयार करें' पर टैप करें।" 
    else 
        "Fill in public issue details on the left and tap 'Generate'."
    fun aiAnalysisTitle(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "AI विश्लेषण एवं साक्ष्य सारांश" else "Gemini AI Ground Analysis & Summary"
    fun draftEditable(mode: LanguageMode, platformName: String): String = if (mode == LanguageMode.HINDI) 
        "$platformName प्रारूप (संपादन योग्य)" 
    else 
        "$platformName Draft (Editable)"
    fun copyText(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "टेक्स्ट कॉपी करें" else "Copy Text"
    fun shareViaApp(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "शेयर करें" else "Share via App"
    fun graphicCard(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "ग्राफिक कार्ड" else "Graphic Card"
    fun saveToProfile(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "प्रोफ़ाइल में सहेजें" else "Save to Profile"

    // History dialog
    fun historyTitle(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "सहेजी गई रिपोर्ट्स" else "Campaign History"
    fun reportsCount(mode: LanguageMode, count: Int): String = if (mode == LanguageMode.HINDI) "$count रिपोर्ट्स आपकी प्रोफ़ाइल में सहेजी हैं" else "$count reports saved to your profile"
    fun noSavedReports(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "कोई सहेजी गई रिपोर्ट नहीं मिली" else "No saved reports found"
    fun noSavedReportsDesc(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) 
        "पोस्ट तैयार करके 'प्रोफ़ाइल में सहेजें' पर टैप करें।" 
    else 
        "Generate posts and tap 'Save to Profile' to build your campaign portfolio."

    // Visual card modal
    fun visualCardTitle(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "ग्राफिक पोस्टर कार्ड" else "Visual Evidence Poster"
    fun visualCardSubtitle(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) 
        "इंस्टाग्राम स्टोरीज़, एक्स व व्हाट्सएप पर साझा करने हेतु तैयार" 
    else 
        "Ready to publish on Instagram Stories, X, WhatsApp"
    fun saveToGallery(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "गैलरी में सहेजें" else "Save to Gallery"
    fun shareGraphic(mode: LanguageMode): String = if (mode == LanguageMode.HINDI) "ग्राफिक शेयर करें" else "Share Graphic"
}
