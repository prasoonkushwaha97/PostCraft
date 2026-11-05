package com.example

import com.example.data.generator.TemplatePostGenerator
import com.example.data.model.CampaignTheme
import com.example.data.model.EventDetails
import com.example.data.model.EventType
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
            theme = CampaignTheme.GROUND_REALITY,
            village = "Chhamrachh",
            tehsil = "Sarai",
            district = "Singrauli",
            state = "Madhya Pradesh",
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

        // Verify Instagram storytelling
        val instaHi = bundle.getPost(SocialPlatform.INSTAGRAM, LanguageMode.HINDI)
        val instaEn = bundle.getPost(SocialPlatform.INSTAGRAM, LanguageMode.ENGLISH)
        assertTrue(instaHi.contains("ग्राम आवाज़"))
        assertTrue(instaEn.contains("GramAwaz"))

        // Verify LinkedIn policy recommendations
        val linkedInHi = bundle.getPost(SocialPlatform.LINKEDIN, LanguageMode.HINDI)
        val linkedInEn = bundle.getPost(SocialPlatform.LINKEDIN, LanguageMode.ENGLISH)
        assertTrue(linkedInHi.contains("लोकनीति"))
        assertTrue(linkedInEn.contains("Public Policy"))

        // Verify Facebook community call
        val fbHi = bundle.getPost(SocialPlatform.FACEBOOK, LanguageMode.HINDI)
        val fbEn = bundle.getPost(SocialPlatform.FACEBOOK, LanguageMode.ENGLISH)
        assertTrue(fbHi.contains("कमेंट"))
        assertTrue(fbEn.contains("comments"))

        // Verify hashtags and proof summary
        assertFalse(bundle.suggestedHashtags.isEmpty())
        assertTrue(bundle.suggestedHashtags.any { it.contains("JalJeevanMission") || it.contains("GramAwaz") })
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
            theme = CampaignTheme.GROUND_REALITY,
            village = "Chhamrachh",
            tehsil = "Sarai",
            district = "Singrauli",
            state = "Madhya Pradesh",
            claim = "100% tap water connection under Jal Jeevan Mission",
            reality = "Taps dry, pipeline broken, people line up for handpumps",
            tone = ReportTone.INVESTIGATIVE,
            hasImage = true,
            eventDetails = event
        )

        val twitterHi = bundle.getPost(SocialPlatform.TWITTER, LanguageMode.HINDI)
        val twitterEn = bundle.getPost(SocialPlatform.TWITTER, LanguageMode.ENGLISH)
        assertTrue("Twitter Hindi with event should be within 280 chars but was ${twitterHi.length}", twitterHi.length <= 280)
        assertTrue("Twitter English with event should be within 280 chars but was ${twitterEn.length}", twitterEn.length <= 280)

        val instaHi = bundle.getPost(SocialPlatform.INSTAGRAM, LanguageMode.HINDI)
        assertTrue(instaHi.contains("संबंधित आयोजन"))
        assertTrue(instaHi.contains("जनसुनवाई"))

        val fbHi = bundle.getPost(SocialPlatform.FACEBOOK, LanguageMode.HINDI)
        assertTrue(fbHi.contains("जनसुनवाई"))
    }
}
