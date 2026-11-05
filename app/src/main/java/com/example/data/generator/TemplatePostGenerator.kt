package com.example.data.generator

import com.example.data.model.CampaignTheme
import com.example.data.model.EventDetails
import com.example.data.model.GeneratedPostBundle
import com.example.data.model.IssueScope
import com.example.data.model.ReportTone

object TemplatePostGenerator {
    fun generateOfflineBundle(
        theme: CampaignTheme,
        scope: IssueScope,
        localUnit: String,
        subDistrict: String,
        district: String,
        stateRegion: String,
        claim: String,
        reality: String,
        tone: ReportTone,
        hasImage: Boolean,
        eventDetails: EventDetails = EventDetails()
    ): GeneratedPostBundle {
        val loc = listOf(localUnit, subDistrict, district, stateRegion).filter { it.isNotBlank() }.joinToString(", ")
        val shortLoc = if (localUnit.isNotBlank()) "$localUnit ($district)" else district.ifBlank { "क्षेत्रीय स्थल" }

        val hashtags = mutableListOf("#PostCraft", "#CitizenJournalism", "#PublicAccountability", "#GroundReality")
        when (theme) {
            CampaignTheme.URBAN_CIVIC -> hashtags.addAll(listOf("#UrbanCivic", "#SmartCityReality"))
            CampaignTheme.RURAL_AGRICULTURE -> hashtags.addAll(listOf("#RuralIndia", "#PanchayatRaj"))
            CampaignTheme.GOVERNANCE_POLICY -> hashtags.addAll(listOf("#PublicPolicy", "#RTIIndia"))
            CampaignTheme.EDUCATION_HEALTHCARE -> hashtags.addAll(listOf("#HealthForAll", "#RightToEducation"))
            CampaignTheme.LEGAL_RIGHTS -> hashtags.addAll(listOf("#CitizenRights", "#RuleOfLaw"))
        }

        val hashtagStr = hashtags.distinct().take(4).joinToString(" ")
        val twitterHashtags = hashtags.distinct().take(3).joinToString(" ")

        // 1. Twitter (X) - strictly under 280 chars
        val twitterEventNoticeHi = if (eventDetails.hasEvent) {
            val evTitle = eventDetails.title.ifBlank { eventDetails.eventType.labelHi }.take(22)
            "\n📅 आयोजन: $evTitle"
        } else ""

        val rawTwitterHi = "📍 $shortLoc | #ज़मीनी_हक़ीक़त\n" +
                "📋 दावा: ${claim.take(38)}...\n" +
                "❌ हक़ीक़त: ${reality.take(48)}..." +
                twitterEventNoticeHi +
                "\nसंज्ञान लें: @CollectorSingrauli @MoHUA_India\n$twitterHashtags"
        val twitterHi = if (rawTwitterHi.length > 275) rawTwitterHi.take(272) + "..." else rawTwitterHi

        val twitterEventNoticeEn = if (eventDetails.hasEvent) {
            val evTitle = eventDetails.title.ifBlank { eventDetails.eventType.labelEn }.take(22)
            "\n📅 Event: $evTitle"
        } else ""

        val rawTwitterEn = "📍 $shortLoc | #GroundReality\n" +
                "📋 Claim: ${claim.take(38)}...\n" +
                "❌ Reality: ${reality.take(48)}..." +
                twitterEventNoticeEn +
                "\nAction needed: @CollectorSingrauli @MoHUA_India\n$twitterHashtags"
        val twitterEn = if (rawTwitterEn.length > 275) rawTwitterEn.take(272) + "..." else rawTwitterEn

        // 2. Instagram & Facebook - Storytelling Hook & Public Engagement
        val instaEventSectionHi = if (eventDetails.hasEvent) {
            """
            
            🗓 संबंधित नागरिक आयोजन:
            📌 ${eventDetails.title.ifBlank { eventDetails.eventType.labelHi }}
            ${if (eventDetails.date.isNotBlank()) "⏰ दिनांक व समय: ${eventDetails.date}" else ""}
            ${if (eventDetails.venue.isNotBlank()) "📍 स्थान: ${eventDetails.venue}" else ""}
            ${if (eventDetails.isUpcoming) "👉 नागरिक इस बैठक में शामिल होकर ठोस साक्ष्यों के साथ जवाबदेही तय करें!" else "👉 इस बैठक में अधिकारियों के समक्ष यह मामला प्रमुखता से दर्ज कराया गया।"}
            """.trimIndent()
        } else ""

        val instaEventSectionEn = if (eventDetails.hasEvent) {
            """
            
            🗓 Related Public Forum:
            📌 ${eventDetails.title.ifBlank { eventDetails.eventType.labelEn }}
            ${if (eventDetails.date.isNotBlank()) "⏰ Schedule: ${eventDetails.date}" else ""}
            ${if (eventDetails.venue.isNotBlank()) "📍 Venue: ${eventDetails.venue}" else ""}
            ${if (eventDetails.isUpcoming) "👉 Citizens are urged to attend and question officials with factual evidence!" else "👉 Ground evidence was formally presented to authorities at this forum."}
            """.trimIndent()
        } else ""

        val instaFbHi = """
            📢 पोस्ट क्राफ्ट | जन-मुद्दा ग्राउंड रिपोर्ट 🔍
            
            📍 स्थान: $loc
            📂 विषय: ${theme.labelHi}
            ⚖️ टोन: ${tone.labelHi}
            
            📋 आधिकारिक दावा / योजना वादा:
            "$claim"
            
            ⚠️ ज़मीनी हक़ीक़त (धरातल पर):
            "$reality"
            $instaEventSectionHi
            
            ${if (hasImage) "📸 संलग्न तस्वीर मौके की वास्तविक स्थिति का प्रमाण है।" else ""}
            
            💬 अपनी राय दें: क्या आपके वार्ड या इलाके में भी ऐसी ही स्थिति है? नीचे कमेंट में लिखें और स्थानीय जनप्रतिनिधियों व अधिकारियों को टैग करें!
            
            $hashtagStr #PostCraft #CitizenJournalism #VoiceOfPeople #GoodGovernance
        """.trimIndent()

        val instaFbEn = """
            📢 PostCraft | Ground Reality Investigation 🔍
            
            📍 Location: $loc
            📂 Category: ${theme.labelEn}
            ⚖️ Tone: ${tone.labelEn}
            
            📋 Official Promise / Scheme Claim:
            "$claim"
            
            ⚠️ Ground Reality on the Field:
            "$reality"
            $instaEventSectionEn
            
            ${if (hasImage) "📸 The attached photo is photographic proof of the ground situation." else ""}
            
            💬 Speak Up: Is your locality facing a similar issue? Comment below and tag your local ward councillor, MLA, or administrative officers!
            
            $hashtagStr #PostCraft #CitizenJournalism #PublicVoice #GoodGovernance #Accountability
        """.trimIndent()

        // 3. Press Release & WhatsApp Media Bulletin (Inverted Pyramid)
        val pressReleaseHi = """
            📰 प्रेस विज्ञप्ति | नागरिक पत्रकारिता मंच (पोस्ट क्राफ्ट)
            तत्काल प्रसारण हेतु जारी
            
            शीर्षक: $shortLoc में सरकारी दावों के विपरीत जनता परेशान, $claim की खुली पोल
            
            स्थान: $loc
            
            $loc: नागरिक पड़ताल में $loc अंतर्गत एक गंभीर जन-मुद्दा सामने आया है। एक तरफ जहां प्रशासनिक स्तर पर "$claim" का दावा किया जा रहा है, वहीं धरातल पर वास्तविकता इसके बिल्कुल उलट है।
            
            जांच में पाया गया कि "$reality"। स्थानीय निवासियों का कहना है कि बुनियादी सुविधाओं और वादों की इस अनदेखी से जनजीवन बुरी तरह प्रभावित हो रहा है। ${if (eventDetails.hasEvent) "इस विषय को लेकर ${eventDetails.title.ifBlank { eventDetails.eventType.labelHi }} का आयोजन ${eventDetails.date.ifBlank { "नियत तिथि" }} को ${eventDetails.venue.ifBlank { shortLoc }} में निर्धारित है।" else ""}
            
            नागरिकों ने ज़िला प्रशासन व संबंधित विभाग से मांग की है कि तत्काल प्रभाव से निरीक्षण दल भेजकर समस्या का समयबद्ध समाधान किया जाए, अन्यथा स्थानीय स्तर पर व्यापक जन-संवाद आयोजित किया जाएगा।
            
            सम्पर्क व स्रोत: पोस्ट क्राफ्ट सिटीजन नेटवर्क | #PostCraft
        """.trimIndent()

        val pressReleaseEn = """
            📰 PRESS RELEASE | CITIZEN JOURNALISM DESK (POSTCRAFT)
            FOR IMMEDIATE RELEASE
            
            HEADLINE: Field Audit in $shortLoc Exposes Gaps in Public Delivery; Citizens Demand Urgent Redressal
            
            DATELINE: $loc
            
            $loc — A comprehensive citizen field audit conducted in $loc has brought forward stark discrepancies between official policy claims and grassroots ground reality. While administrative records promise "$claim", empirical field observations reveal that "$reality".
            
            Local residents have expressed deep concern regarding the administrative inertia. ${if (eventDetails.hasEvent) "To demand formal accountability, a ${eventDetails.title.ifBlank { eventDetails.eventType.labelEn }} is convened on ${eventDetails.date.ifBlank { "scheduled date" }} at ${eventDetails.venue.ifBlank { shortLoc }}." else ""}
            
            Civil society observers and citizens have formally urged district authorities and department heads to initiate an immediate on-site inspection and publish a time-bound action plan.
            
            Media Desk: PostCraft Citizen Network | #PostCraft
        """.trimIndent()

        // 4. LinkedIn & Public Policy Analysis
        val linkedInHi = """
            📊 लोकनीति कार्यान्वयन एवं प्रशासनिक जवाबदेही: केस स्टडी
            
            क्षेत्र: $loc | विषय: ${theme.labelHi}
            मंच: पोस्ट क्राफ्ट - जन-अधिकार एवं नीतिगत विश्लेषण
            
            सुशासन का मूल्यांकन बजट आवंटन से नहीं, बल्कि अंतिम पंक्ति के नागरिक तक सेवा की गुणवत्ता पहुँचने से होता है।
            
            1️⃣ नीतिगत वादा / प्रशासनिक लक्ष्य:
            $claim
            
            2️⃣ क्रियान्वयन अंतराल (Implementation Gap):
            $reality
            
            💡 नीतिगत सुधार अनुशंसाएँ:
            • नागरिक-केंद्रित रियल-टाइम डैशबोर्ड व स्वतंत्र सामाजिक अंकेक्षण (Social Audit)।
            • शिकायत निवारण हेतु जवाबदेह अधिकारियों की समय-सीमा तय करना।
            • प्रशासनिक निर्णयों में स्थानीय नागरिकों व विशेषज्ञों की पारदर्शी सहभागिता।
            
            $hashtagStr #PublicPolicy #GoodGovernance #UrbanGovernance #RuralDevelopment #Transparency #PostCraft
        """.trimIndent()

        val linkedInEn = """
            📊 Public Policy Implementation & Governance Gap: Field Analysis
            
            Location: $loc | Domain: ${theme.labelEn}
            Platform: PostCraft - Citizen Journalism & Policy Accountability
            
            The efficacy of public administration is defined not by policy announcements or capital allocation, but by verified outcomes at the citizen touchpoint.
            
            1️⃣ Policy Commitment / Official Benchmark:
            $claim
            
            2️⃣ Ground Level Execution Deficit:
            $reality
            
            💡 Policy Recommendations & Institutional Reforms:
            • Instituting mandatory citizen-led third-party Social Audits.
            • Real-time digital monitoring capturing operational status rather than mere completion certificates.
            • Fixed escalation matrix and time-bound grievance resolution for public grievances.
            
            Bridging the delivery deficit requires active civic engagement and transparent administrative scrutiny.
            
            $hashtagStr #PublicPolicy #GoodGovernance #InstitutionalReform #CivicAction #SocialAudit #PostCraft
        """.trimIndent()

        val proofSummary = if (hasImage) {
            "मौके की तस्वीर में बुनियादी सुविधाओं के वादों के विपरीत वास्तविक स्थिति दिखाई दे रही है।"
        } else {
            "नागरिक अवलोकन व मौखिक साक्ष्यों पर आधारित रिपोर्ट।"
        }

        return GeneratedPostBundle(
            twitterHindi = twitterHi,
            twitterEnglish = twitterEn,
            instagramFbHindi = instaFbHi,
            instagramFbEnglish = instaFbEn,
            pressReleaseHindi = pressReleaseHi,
            pressReleaseEnglish = pressReleaseEn,
            linkedInHindi = linkedInHi,
            linkedInEnglish = linkedInEn,
            suggestedHashtags = hashtags,
            alternativeTones = listOf("खोजी व तार्किक", "सकारात्मक आलोचना", "ज़मीनी दास्तान"),
            visualProofSummary = proofSummary,
            handlesToTag = listOf("@CollectorSingrauli", "@MoHUA_India", "@CMMadhyaPradesh"),
            eventDetails = eventDetails,
            scope = scope
        )
    }
}
