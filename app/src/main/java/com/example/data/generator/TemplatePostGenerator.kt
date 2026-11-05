package com.example.data.generator

import com.example.data.model.CampaignTheme
import com.example.data.model.EventDetails
import com.example.data.model.GeneratedPostBundle
import com.example.data.model.ReportTone

object TemplatePostGenerator {
    fun generateOfflineBundle(
        theme: CampaignTheme,
        village: String,
        tehsil: String,
        district: String,
        state: String,
        claim: String,
        reality: String,
        tone: ReportTone,
        hasImage: Boolean,
        eventDetails: EventDetails = EventDetails()
    ): GeneratedPostBundle {
        val loc = listOf(village, tehsil, district, state).filter { it.isNotBlank() }.joinToString(", ")
        val shortLoc = if (village.isNotBlank()) "$village ($district)" else district.ifBlank { "ग्रामीण क्षेत्र" }

        val hashtags = mutableListOf("#GramAwaz", "#CitizenJournalism", "#PanchayatRaj", "#GroundReality")
        if (claim.contains("Jal", ignoreCase = true) || reality.contains("पानी", ignoreCase = true) || reality.contains("water", ignoreCase = true)) {
            hashtags.add("#JalJeevanMission")
        } else if (claim.contains("Road", ignoreCase = true) || reality.contains("सड़क", ignoreCase = true)) {
            hashtags.add("#PMGSY")
        } else {
            hashtags.add("#RuralGovernance")
        }

        val hashtagStr = hashtags.take(4).joinToString(" ")
        val twitterHashtags = hashtags.take(3).joinToString(" ")

        // Twitter (X) strictly under 280 chars
        val twitterEventNoticeHi = if (eventDetails.hasEvent) {
            val evTitle = eventDetails.title.ifBlank { eventDetails.eventType.labelHi }.take(22)
            "\n📅 आयोजन: $evTitle"
        } else ""

        val rawTwitterHi = "📍 $shortLoc | #ज़मीनी_हकीकत\n" +
                "📋 दावा: ${claim.take(40)}...\n" +
                "❌ हकीकत: ${reality.take(50)}..." +
                twitterEventNoticeHi +
                "\nसंज्ञान लें: @CollectorSingrauli\n$twitterHashtags"
        val twitterHi = if (rawTwitterHi.length > 275) rawTwitterHi.take(272) + "..." else rawTwitterHi

        val twitterEventNoticeEn = if (eventDetails.hasEvent) {
            val evTitle = eventDetails.title.ifBlank { eventDetails.eventType.labelEn }.take(22)
            "\n📅 Event: $evTitle"
        } else ""

        val rawTwitterEn = "📍 $shortLoc | #GroundReality\n" +
                "📋 Claim: ${claim.take(40)}...\n" +
                "❌ Reality: ${reality.take(50)}..." +
                twitterEventNoticeEn +
                "\nAction needed: @CollectorSingrauli\n$twitterHashtags"
        val twitterEn = if (rawTwitterEn.length > 275) rawTwitterEn.take(272) + "..." else rawTwitterEn

        // Instagram Feed
        val instaEventSectionHi = if (eventDetails.hasEvent) {
            """
            
            🗓 संबंधित आयोजन / अवसर:
            📌 ${eventDetails.title.ifBlank { eventDetails.eventType.labelHi }}
            ${if (eventDetails.date.isNotBlank()) "⏰ दिनांक व समय: ${eventDetails.date}" else ""}
            ${if (eventDetails.venue.isNotBlank()) "📍 स्थान: ${eventDetails.venue}" else ""}
            ${if (eventDetails.isUpcoming) "👉 गांववासी इस आयोजन में भारी संख्या में पहुंचें और साक्ष्यों के साथ जवाबदेही तय करें!" else "👉 इस आयोजन में अधिकारियों के समक्ष यह मामला प्रमुखता से दर्ज कराया गया।"}
            """.trimIndent()
        } else ""

        val instaEventSectionEn = if (eventDetails.hasEvent) {
            """
            
            🗓 Related Forum / Event:
            📌 ${eventDetails.title.ifBlank { eventDetails.eventType.labelEn }}
            ${if (eventDetails.date.isNotBlank()) "⏰ Date & Time: ${eventDetails.date}" else ""}
            ${if (eventDetails.venue.isNotBlank()) "📍 Venue: ${eventDetails.venue}" else ""}
            ${if (eventDetails.isUpcoming) "👉 Villagers are urged to attend in large numbers and demand accountability with proof!" else "👉 This evidence was formally submitted before officials during this forum."}
            """.trimIndent()
        } else ""

        val instaHi = """
            📢 ग्राम आवाज़ | साथी फाउंडेशन ग्राउंड रिपोर्ट 🌾
            
            स्थान: $loc
            विषय: ${theme.labelHi}
            शैली: ${tone.labelHi}
            
            🔍 सरकारी दावा (कागज़ों में):
            "$claim"
            
            ⚠️ ज़मीनी हकीकत (धरातल पर):
            "$reality"
            $instaEventSectionHi
            
            ${if (hasImage) "📸 संलग्न तस्वीर ग्राउंड रियलिटी का प्रत्यक्ष प्रमाण है।" else ""}
            
            👉 आवाज़ उठाएं: यदि आपके गांव में भी ऐसा ही हाल है, तो कमेंट में लिखें और अपने स्थानीय प्रतिनिधियों को टैग करें। लोकतंत्र में नागरिक जागरूकता ही समाधान है!
            
            $hashtagStr #SaathiFoundation #VillageVoice #RuralIndia #Accountability
        """.trimIndent()

        val instaEn = """
            📢 GramAwaz | Saathi Foundation Ground Report 🌾
            
            Location: $loc
            Campaign Theme: ${theme.labelEn}
            Tone: ${tone.labelEn}
            
            🔍 Official Claim (On Paper):
            "$claim"
            
            ⚠️ Ground Reality (On the Field):
            "$reality"
            $instaEventSectionEn
            
            ${if (hasImage) "📸 The attached photograph is direct proof of the ground situation." else ""}
            
            👉 Speak Up: If your village faces a similar situation, share your thoughts below and tag your local representatives. Citizen awareness drives real governance reform!
            
            $hashtagStr #SaathiFoundation #VillageVoice #RuralIndia #CivicAction
        """.trimIndent()

        // LinkedIn
        val linkedInEventHi = if (eventDetails.hasEvent) {
            """
            
            🏛️ संबंधित नागरिक / प्रशासनिक मंच:
            • विषय: ${eventDetails.title.ifBlank { eventDetails.eventType.labelHi }}
            • समय व स्थल: ${eventDetails.date.ifBlank { "नियत तिथि" }} | ${eventDetails.venue.ifBlank { loc }}
            • लोकतांत्रिक सहभागिता एवं निगरानी के लिए इस मंच पर नागरिक उपस्थिति अनिवार्य है।
            """.trimIndent()
        } else ""

        val linkedInEventEn = if (eventDetails.hasEvent) {
            """
            
            🏛️ Related Civic / Administrative Forum:
            • Platform: ${eventDetails.title.ifBlank { eventDetails.eventType.labelEn }}
            • Schedule & Venue: ${eventDetails.date.ifBlank { "Scheduled" }} | ${eventDetails.venue.ifBlank { loc }}
            • Grassroots participation at this forum is pivotal for ensuring transparent social audit.
            """.trimIndent()
        } else ""

        val linkedInHi = """
            📊 लोकनीति कार्यान्वयन एवं ग्रामीण सुशासन: ज़मीनी पड़ताल
            
            ग्राम: $village | ब्लॉक: $tehsil | ज़िला: $district ($state)
            पहल: साथी फाउंडेशन - नागरिक पत्रकारिता मंच
            
            सार्वजनिक नीतियों की सफलता केवल आवंटन में नहीं, बल्कि अंतिम व्यक्ति तक पहुंचने में है।
            
            1️⃣ नीतिगत दावा:
            $claim
            
            2️⃣ ज़मीनी क्रियान्वयन की स्थिति:
            $reality
            $linkedInEventHi
            
            💡 प्रशासनिक अनुशंसाएं:
            • ग्राम पंचायत स्तर पर नियमित सामाजिक अंकेक्षण (Social Audit) की अनिवार्यता।
            • शिकायत निवारण तंत्र की समयबद्ध निगरानी।
            • नागरिकों व प्रशासन के बीच पारदर्शी संवाद।
            
            सुशासन का अर्थ केवल कागजी लक्ष्य नहीं, बल्कि प्रत्येक नागरिक के जीवन में वास्तविक सुधार है।
            
            $hashtagStr #PublicPolicy #Governance #RuralDevelopment #India #Transparency
        """.trimIndent()

        val linkedInEn = """
            📊 Public Policy Implementation & Rural Governance: Field Case Study
            
            Village: $village | Block: $tehsil | District: $district ($state)
            Initiative: Saathi Foundation - Citizen Journalism & Public Accountability
            
            True governance effectiveness is measured not by budgetary allocations, but by the tangible delivery of services to the grassroots citizen.
            
            1️⃣ Policy Claim / Government Target:
            $claim
            
            2️⃣ Field Reality & Implementation Gap:
            $reality
            $linkedInEventEn
            
            💡 Constructive Recommendations:
            • Mandatory citizen-led Social Audits at the Gram Panchayat level.
            • Real-time digital dashboard reflecting verified ground functionality, not just installations.
            • Time-bound accountability for local administrative officers.
            
            Good governance demands bridging the divide between policy vision and grassroots reality.
            
            $hashtagStr #PublicPolicy #GoodGovernance #RuralDevelopment #SocialAudit #GrassrootsAccountability
        """.trimIndent()

        // Facebook
        val fbEventHi = if (eventDetails.hasEvent) {
            """
            
            📢 आवश्यक सूचना:
            इस संबंध में ${eventDetails.title.ifBlank { eventDetails.eventType.labelHi }} का आयोजन ${if (eventDetails.date.isNotBlank()) "तारीख ${eventDetails.date} को " else ""}${if (eventDetails.venue.isNotBlank()) "${eventDetails.venue} में " else ""}${if (eventDetails.isUpcoming) "होने जा रहा है। सभी साथी ज़रूर पहुंचें!" else "हुआ था जिसमें यह आवाज़ उठाई गई।"}
            """.trimIndent()
        } else ""

        val fbEventEn = if (eventDetails.hasEvent) {
            """
            
            📢 Community Notice:
            Regarding this matter, a ${eventDetails.title.ifBlank { eventDetails.eventType.labelEn }} is ${if (eventDetails.isUpcoming) "scheduled for ${eventDetails.date} at ${eventDetails.venue}. Please attend and make your voice heard!" else "convened on ${eventDetails.date} at ${eventDetails.venue} where these issues were raised."}
            """.trimIndent()
        } else ""

        val facebookHi = """
            🙏 नमस्कार साथियों! 
            
            हमारे गांव $shortLoc से आज की ज़मीनी रिपोर्ट:
            
            कागजों पर कहा गया था:
            "$claim"
            
            लेकिन जब हम मौके पर पहुंचे, तो हकीकत कुछ और ही निकली:
            "$reality"
            $fbEventHi
            
            क्या आपके गांव में भी ऐसा ही हाल है? नीचे कमेंट में बताएं और अपने सरपंच और स्थानीय जनप्रतिनिधियों को टैग करके सवाल पूछें। जब तक हम सब मिलकर आवाज़ नहीं उठाएंगे, तब तक बदलाव कैसे आएगा?
            
            साथी फाउंडेशन - गांव की आवाज़, देश की आवाज़।
            $hashtagStr
        """.trimIndent()

        val facebookEn = """
            🙏 Greetings friends and community members!
            
            Today's ground report from $shortLoc:
            
            On paper, the promise was:
            "$claim"
            
            However, our ground inspection revealed the reality:
            "$reality"
            $fbEventEn
            
            Is this the situation in your village as well? Let us know in the comments below and tag your local representatives to demand accountability. Meaningful change happens when citizens speak up together!
            
            Saathi Foundation - Voice of the Village, Voice of the Nation.
            $hashtagStr
        """.trimIndent()

        val proofSummary = if (hasImage) {
            "फोटो में ग्रामीण क्षेत्र की बुनियादी सुविधाओं और दावों के विपरीत वास्तविक स्थिति दिखाई दे रही है।"
        } else {
            "नागरिक अवलोकन व मौखिक साक्ष्यों पर आधारित रिपोर्ट।"
        }

        return GeneratedPostBundle(
            twitterHindi = twitterHi,
            twitterEnglish = twitterEn,
            instagramHindi = instaHi,
            instagramEnglish = instaEn,
            linkedInHindi = linkedInHi,
            linkedInEnglish = linkedInEn,
            facebookHindi = facebookHi,
            facebookEnglish = facebookEn,
            suggestedHashtags = hashtags,
            alternativeTones = listOf("खोजी व तार्किक", "सकारात्मक आलोचना", "ज़मीनी दास्तान"),
            visualProofSummary = proofSummary,
            handlesToTag = listOf("@CollectorSingrauli", "@MoJSDoWRRDGR", "@CMMadhyaPradesh"),
            eventDetails = eventDetails
        )
    }
}
