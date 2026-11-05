package com.example

import com.example.data.generator.TemplatePostGenerator
import com.example.data.model.CampaignTheme
import com.example.data.model.EventDetails
import com.example.data.model.EventType
import com.example.data.model.IssueScope
import com.example.data.model.LanguageMode
import com.example.data.model.ReportTone
import com.example.data.model.SocialPlatform
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TemplatePostGeneratorTest {

    @Test
    fun testGenerateOfflineBundleForAllPlatforms() {
        val bundle = TemplatePostGenerator.generateOfflineBundle(
            theme = CampaignTheme.RURAL_AGRICULTURE,
            scope = IssueScope.VILLAGE,
            localUnit = "Chhamrachh",
            subDistrict = "Sarai",
            district = "Singrauli",
            stateRegion = "Madhya Pradesh",
            claim = "100% tap water connection under Jal Jeevan Mission",
            reality = "Taps dry, pipeline broken, people line up for handpumps",
            tone = ReportTone.INVESTIGATIVE,
            hasImage = true
        )

        assertNotNull(bundle)

        // Verify Twitter character constraints
        val twitterHi = bundle.getPost(SocialPlatform.TWITTER, LanguageMode.HINDI)
        val twitterEn = bundle.getPost(SocialPlatform.TWITTER, LanguageMode.ENGLISH)
        assertTrue(twitterHi.isNotBlank())
        assertTrue(twitterEn.isNotBlank())
        assertTrue("Twitter Hindi should be within 280 chars but was ${twitterHi.length}", twitterHi.length <= 280)
        assertTrue("Twitter English should be within 280 chars but was ${twitterEn.length}", twitterEn.length <= 280)

        // Verify Instagram & Facebook storytelling
        val instaHi = bundle.getPost(SocialPlatform.INSTAGRAM_FB, LanguageMode.HINDI)
        val instaEn = bundle.getPost(SocialPlatform.INSTAGRAM_FB, LanguageMode.ENGLISH)
        assertTrue(instaHi.contains("पोस्ट क्राफ्ट") || instaHi.contains("PostCraft"))
        assertTrue(instaEn.contains("PostCraft"))

        // Verify LinkedIn policy recommendations
        val linkedInHi = bundle.getPost(SocialPlatform.LINKEDIN, LanguageMode.HINDI)
        val linkedInEn = bundle.getPost(SocialPlatform.LINKEDIN, LanguageMode.ENGLISH)
        assertTrue(linkedInHi.contains("नीतिगत") || linkedInHi.contains("PostCraft"))
        assertTrue(linkedInEn.contains("Policy") || linkedInEn.contains("PostCraft"))

        // Verify Press Release / WhatsApp Bulletin format
        val pressHi = bundle.getPost(SocialPlatform.PRESS_RELEASE, LanguageMode.HINDI)
        val pressEn = bundle.getPost(SocialPlatform.PRESS_RELEASE, LanguageMode.ENGLISH)
        assertTrue(pressHi.contains("प्रेस विज्ञप्ति") || pressHi.contains("मीडिया बुलेटिन"))
        assertTrue(pressEn.contains("PRESS RELEASE") || pressEn.contains("MEDIA BULLETIN"))

        // Verify hashtags and proof summary
        assertFalse(bundle.suggestedHashtags.isEmpty())
        assertTrue(bundle.suggestedHashtags.any { it.contains("PostCraft") || it.contains("CitizenJournalism") || it.contains("JalJeevan") })
    }

    @Test
    fun testGenerateOfflineBundleWithEventDetails() {
        val event = EventDetails(
            eventType = EventType.JAN_SUNWAI,
            title = "कलेक्टर जनसुनवाई (District Public Hearing)",
            date = "मंगलवार 11:00 AM",
            venue = "ज़िला कलेक्ट्रेट, सिंगरौली",
            isUpcoming = true
        )

        val bundle = TemplatePostGenerator.generateOfflineBundle(
            theme = CampaignTheme.URBAN_CIVIC,
            scope = IssueScope.CITY,
            localUnit = "Ward 24",
            subDistrict = "Indore Municipal Corp",
            district = "Indore",
            stateRegion = "Madhya Pradesh",
            claim = "Smart drainage completed in 2023",
            reality = "Severe waterlogging during 15 minutes of rain",
            tone = ReportTone.INVESTIGATIVE,
            hasImage = true,
            eventDetails = event
        )

        val twitterHi = bundle.getPost(SocialPlatform.TWITTER, LanguageMode.HINDI)
        val twitterEn = bundle.getPost(SocialPlatform.TWITTER, LanguageMode.ENGLISH)
        assertTrue("Twitter Hindi with event should be within 280 chars but was ${twitterHi.length}", twitterHi.length <= 280)
        assertTrue("Twitter English with event should be within 280 chars but was ${twitterEn.length}", twitterEn.length <= 280)

        val instaHi = bundle.getPost(SocialPlatform.INSTAGRAM_FB, LanguageMode.HINDI)
        assertTrue(instaHi.contains("आयोजन") || instaHi.contains("जनसुनवाई"))

        val pressHi = bundle.getPost(SocialPlatform.PRESS_RELEASE, LanguageMode.HINDI)
        assertTrue(pressHi.contains("जनसुनवाई") || pressHi.contains("प्रेस"))
    }
}
