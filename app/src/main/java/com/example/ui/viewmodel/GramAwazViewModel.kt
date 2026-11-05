package com.example.ui.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.generator.TemplatePostGenerator
import com.example.data.model.CampaignReport
import com.example.data.model.CampaignTheme
import com.example.data.model.EventDetails
import com.example.data.model.EventType
import com.example.data.model.GeneratedPostBundle
import com.example.data.model.IssueScope
import com.example.data.model.LanguageMode
import com.example.data.model.ReportTone
import com.example.data.model.SearchCitation
import com.example.data.model.SocialPlatform
import com.example.data.repository.CampaignReportRepository
import com.example.data.repository.FirebaseAiRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val TAG = "PostCraftViewModel"

sealed interface GenerationUiState {
    data object Idle : GenerationUiState
    data class Loading(val message: String = "AI is analyzing ground reality and official claims...") : GenerationUiState
    data class Success(val bundle: GeneratedPostBundle, val isAiGenerated: Boolean) : GenerationUiState
    data class Error(val message: String) : GenerationUiState
}

sealed interface SearchGroundingUiState {
    data object Idle : SearchGroundingUiState
    data class Loading(val query: String) : SearchGroundingUiState
    data class Success(val factualSummary: String, val citations: List<SearchCitation>) : SearchGroundingUiState
    data class Error(val message: String) : SearchGroundingUiState
}

data class ReportFormState(
    val theme: CampaignTheme = CampaignTheme.URBAN_CIVIC,
    val scope: IssueScope = IssueScope.CITY,
    val localUnit: String = "वार्ड नं. 24",
    val subDistrict: String = "इंदौर नगर निगम",
    val district: String = "इंदौर",
    val stateRegion: String = "मध्य प्रदेश",
    val claim: String = "स्मार्ट सिटी प्रोजेक्ट के तहत 100% अंडरग्राउंड ड्रेनेज व सीवरेज निर्माण पूर्ण",
    val groundReality: String = "हल्की बारिश में भी मुख्य मार्ग पर 2 फीट तक जलभराव, सीवरेज लाइन चोक, घरों में गंदा पानी घुस रहा है",
    val tone: ReportTone = ReportTone.INVESTIGATIVE,
    val photoUri: Uri? = null,
    val photoBitmap: Bitmap? = null,
    val eventDetails: EventDetails = EventDetails(
        eventType = EventType.JAN_SUNWAI,
        title = "नगर निगम जनसुनवाई व आयुक्त जन-संवाद",
        date = "आगामी मंगलवार 11:00 AM",
        venue = "नगर निगम मुख्यालय, इंदौर",
        isUpcoming = true
    )
) {
    // Compatibility helpers
    val village: String get() = localUnit
    val tehsil: String get() = subDistrict
    val state: String get() = stateRegion
}

class GramAwazViewModel(
    private val aiRepository: FirebaseAiRepository = FirebaseAiRepository(),
    private val firestoreRepository: CampaignReportRepository
) : ViewModel() {

    private val _formState = MutableStateFlow(ReportFormState())
    val formState: StateFlow<ReportFormState> = _formState.asStateFlow()

    private val _generationState = MutableStateFlow<GenerationUiState>(GenerationUiState.Idle)
    val generationState: StateFlow<GenerationUiState> = _generationState.asStateFlow()

    private val _searchGroundingState = MutableStateFlow<SearchGroundingUiState>(SearchGroundingUiState.Idle)
    val searchGroundingState: StateFlow<SearchGroundingUiState> = _searchGroundingState.asStateFlow()

    private val _activePlatform = MutableStateFlow(SocialPlatform.TWITTER)
    val activePlatform: StateFlow<SocialPlatform> = _activePlatform.asStateFlow()

    private val _activeLanguage = MutableStateFlow(LanguageMode.HINDI)
    val activeLanguage: StateFlow<LanguageMode> = _activeLanguage.asStateFlow()

    private val _editableDraftText = MutableStateFlow("")
    val editableDraftText: StateFlow<String> = _editableDraftText.asStateFlow()

    private val _currentBundle = MutableStateFlow<GeneratedPostBundle?>(null)
    val currentBundle: StateFlow<GeneratedPostBundle?> = _currentBundle.asStateFlow()

    private val _saveStatusMessage = MutableStateFlow<String?>(null)
    val saveStatusMessage: StateFlow<String?> = _saveStatusMessage.asStateFlow()

    // Real-time observation of user saved reports
    val userSavedReports: StateFlow<List<CampaignReport>> = firestoreRepository.observeUserReports()
        .catch { e ->
            Log.e(TAG, "Error observing saved reports", e)
            emit(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    fun updateTheme(theme: CampaignTheme) {
        _formState.value = _formState.value.copy(theme = theme)
    }

    fun updateScope(scope: IssueScope) {
        _formState.value = _formState.value.copy(scope = scope)
    }

    fun updateLocalUnit(unit: String) {
        _formState.value = _formState.value.copy(localUnit = unit)
    }

    fun updateSubDistrict(sub: String) {
        _formState.value = _formState.value.copy(subDistrict = sub)
    }

    fun updateDistrict(district: String) {
        _formState.value = _formState.value.copy(district = district)
    }

    fun updateStateRegion(state: String) {
        _formState.value = _formState.value.copy(stateRegion = state)
    }

    // Compatibility aliases
    fun updateVillage(v: String) = updateLocalUnit(v)
    fun updateTehsil(t: String) = updateSubDistrict(t)
    fun updateState(s: String) = updateStateRegion(s)

    fun updateClaim(claim: String) {
        _formState.value = _formState.value.copy(claim = claim)
    }

    fun updateGroundReality(reality: String) {
        _formState.value = _formState.value.copy(groundReality = reality)
    }

    fun updateTone(tone: ReportTone) {
        _formState.value = _formState.value.copy(tone = tone)
    }

    // Event updates
    fun updateEventType(type: EventType) {
        _formState.value = _formState.value.copy(
            eventDetails = _formState.value.eventDetails.copy(eventType = type)
        )
    }

    fun updateEventTitle(title: String) {
        _formState.value = _formState.value.copy(
            eventDetails = _formState.value.eventDetails.copy(title = title)
        )
    }

    fun updateEventDate(date: String) {
        _formState.value = _formState.value.copy(
            eventDetails = _formState.value.eventDetails.copy(date = date)
        )
    }

    fun updateEventVenue(venue: String) {
        _formState.value = _formState.value.copy(
            eventDetails = _formState.value.eventDetails.copy(venue = venue)
        )
    }

    fun updateEventIsUpcoming(isUpcoming: Boolean) {
        _formState.value = _formState.value.copy(
            eventDetails = _formState.value.eventDetails.copy(isUpcoming = isUpcoming)
        )
    }

    fun setPhoto(context: Context, uri: Uri?) {
        if (uri == null) {
            _formState.value = _formState.value.copy(photoUri = null, photoBitmap = null)
            return
        }
        val bitmap = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri)) { decoder, _, _ ->
                    decoder.isMutableRequired = true
                }
            } else {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to decode photo bitmap", e)
            null
        }

        _formState.value = _formState.value.copy(photoUri = uri, photoBitmap = bitmap)
    }

    fun removePhoto() {
        _formState.value = _formState.value.copy(photoUri = null, photoBitmap = null)
    }

    fun verifyOfficialSchemeViaSearch() {
        val form = _formState.value
        val loc = "${form.district}, ${form.stateRegion}"
        viewModelScope.launch {
            _searchGroundingState.value = SearchGroundingUiState.Loading("Searching official guidelines for ${form.claim.take(30)}...")
            val result = aiRepository.fetchOfficialSchemeSearchGrounding(
                schemeOrClaim = form.claim,
                location = loc
            )
            result.onSuccess { (facts, citations) ->
                _searchGroundingState.value = SearchGroundingUiState.Success(facts, citations)
            }.onFailure { err ->
                Log.w(TAG, "Search grounding error", err)
                _searchGroundingState.value = SearchGroundingUiState.Error(err.localizedMessage ?: "Could not fetch search facts")
            }
        }
    }

    fun loadPreset(presetIndex: Int, mode: LanguageMode = _activeLanguage.value) {
        val isHindi = mode == LanguageMode.HINDI
        when (presetIndex) {
            0 -> { // Urban Civic (Indore Ward 24 - Waterlogging & Drainage)
                _formState.value = _formState.value.copy(
                    theme = CampaignTheme.URBAN_CIVIC,
                    scope = IssueScope.CITY,
                    localUnit = if (isHindi) "वार्ड नं. 24" else "Ward 24",
                    subDistrict = if (isHindi) "इंदौर नगर निगम" else "Indore Municipal Corporation",
                    district = if (isHindi) "इंदौर" else "Indore",
                    stateRegion = if (isHindi) "मध्य प्रदेश" else "Madhya Pradesh",
                    claim = if (isHindi) "स्मार्ट सिटी प्रोजेक्ट के तहत 100% अंडरग्राउंड ड्रेनेज व सीवरेज निर्माण पूर्ण"
                            else "100% smart underground drainage and flood prevention system completed",
                    groundReality = if (isHindi) "हल्की बारिश में भी मुख्य मार्ग पर 2 फीट तक जलभराव, सीवरेज लाइन चोक, घरों में गंदा पानी घुस रहा है"
                                   else "Just 30 minutes of rain leads to 2-foot waterlogging, choked drains overflow into residential houses",
                    tone = ReportTone.INVESTIGATIVE,
                    eventDetails = EventDetails(
                        eventType = EventType.JAN_SUNWAI,
                        title = if (isHindi) "नगर निगम जनसुनवाई व आयुक्त जन-संवाद" else "Municipal Commissioner Public Hearing",
                        date = if (isHindi) "आगामी मंगलवार 11:00 AM" else "Upcoming Tuesday 11:00 AM",
                        venue = if (isHindi) "नगर निगम मुख्यालय, इंदौर" else "Municipal HQ Auditorium, Indore",
                        isUpcoming = true
                    )
                )
            }
            1 -> { // Rural & Agriculture (Jal Jeevan Mission)
                _formState.value = _formState.value.copy(
                    theme = CampaignTheme.RURAL_AGRICULTURE,
                    scope = IssueScope.VILLAGE,
                    localUnit = if (isHindi) "छमरछ" else "Chhamrachh",
                    subDistrict = if (isHindi) "सरई" else "Sarai",
                    district = if (isHindi) "सिंगरौली" else "Singrauli",
                    stateRegion = if (isHindi) "मध्य प्रदेश" else "Madhya Pradesh",
                    claim = if (isHindi) "जल जीवन मिशन के तहत हर घर नल से जल कनेक्शन पूर्ण (हर घर जल प्रमाणित)"
                            else "100% functional tap water connection delivered under Jal Jeevan Mission",
                    groundReality = if (isHindi) "नल पूरी तरह सूखे हैं, पानी नहीं आ रहा, पाइपलाइन टूटी है, लोग हैंडपंप पर 2 घंटे लाइन लगाते हैं"
                                   else "Taps are dry, distribution pipeline broken, villagers wait 2 hours in line at handpumps",
                    tone = ReportTone.INVESTIGATIVE,
                    eventDetails = EventDetails(
                        eventType = EventType.JAN_SUNWAI,
                        title = if (isHindi) "कलेक्टर जनसुनवाई" else "District Collector Public Hearing",
                        date = if (isHindi) "आगामी मंगलवार 11:00 AM" else "Upcoming Tuesday 11:00 AM",
                        venue = if (isHindi) "ज़िला कलेक्ट्रेट सभागार, बैढ़न" else "District Collectorate Auditorium, Waidhan",
                        isUpcoming = true
                    )
                )
            }
            2 -> { // Governance & Policy (Bargawan Road / Quality Audit)
                _formState.value = _formState.value.copy(
                    theme = CampaignTheme.GOVERNANCE_POLICY,
                    scope = IssueScope.VILLAGE,
                    localUnit = if (isHindi) "बरगवां" else "Bargawan",
                    subDistrict = if (isHindi) "देवसर" else "Deosar",
                    district = if (isHindi) "सिंगरौली" else "Singrauli",
                    stateRegion = if (isHindi) "मध्य प्रदेश" else "Madhya Pradesh",
                    claim = if (isHindi) "प्रधानमंत्री ग्राम सड़क योजना के तहत पक्की डामर सड़क निर्माण एवं 5 वर्ष रखरखाव"
                            else "All-weather pucca road constructed under PMGSY with 5-year maintenance guarantee",
                    groundReality = if (isHindi) "पहली ही बारिश में सड़क बह गई, गहरे गड्ढे बन गए हैं, गांव में एम्बुलेंस नहीं आ सकती"
                                   else "Road washed away in first rain, deep dangerous potholes, ambulances cannot enter the village",
                    tone = ReportTone.CONSTRUCTIVE,
                    eventDetails = EventDetails(
                        eventType = EventType.GRAM_SABHA,
                        title = if (isHindi) "विशेष ग्राम सभा बैठक" else "Special Gram Sabha Social Audit",
                        date = if (isHindi) "15 तारीख, सुबह 10:00 बजे" else "15th of month, 10:00 AM",
                        venue = if (isHindi) "पंचायत भवन, बरगवां" else "Panchayat Bhavan, Bargawan",
                        isUpcoming = true
                    )
                )
            }
            3 -> { // Education & Healthcare (District Hospital)
                _formState.value = _formState.value.copy(
                    theme = CampaignTheme.EDUCATION_HEALTHCARE,
                    scope = IssueScope.DISTRICT,
                    localUnit = if (isHindi) "ज़िला सिविल अस्पताल" else "District Civil Hospital",
                    subDistrict = if (isHindi) "बैढ़न" else "Waidhan",
                    district = if (isHindi) "सिंगरौली" else "Singrauli",
                    stateRegion = if (isHindi) "मध्य प्रदेश" else "Madhya Pradesh",
                    claim = if (isHindi) "200 बिस्तरों वाला सर्वसुविधायुक्त अस्पताल, निःशुल्क 24 घंटे दवाइयां व सोनोग्राफी उपलब्ध"
                            else "200-bed multi-specialty facility with free round-the-clock medicines and diagnostic services",
                    groundReality = if (isHindi) "सोनोग्राफी मशीन 6 माह से खराब, मरीजों को निजी केंद्रों पर भेजा जा रहा है, जीवनरक्षक दवाइयां नदारद"
                                   else "Sonography machine broken for 6 months, patients forced to costly private labs, essential medicines out of stock",
                    tone = ReportTone.INVESTIGATIVE,
                    eventDetails = EventDetails(
                        eventType = EventType.CITIZEN_PRESS_CONFERENCE,
                        title = if (isHindi) "नागरिक स्वास्थ्य अधिकार प्रेस वार्ता" else "Citizen Healthcare Rights Press Briefing",
                        date = if (isHindi) "शुक्रवार दोपहर 1:00 बजे" else "Friday 1:00 PM",
                        venue = if (isHindi) "प्रेस क्लब सभागार, सिंगरौली" else "Press Club Auditorium, Singrauli",
                        isUpcoming = true
                    )
                )
            }
        }
    }

    fun generatePosts() {
        val currentForm = _formState.value
        if (currentForm.claim.isBlank() || currentForm.groundReality.isBlank()) {
            _generationState.value = GenerationUiState.Error("Please enter both the Official Promise/Claim and Ground Reality.")
            return
        }

        viewModelScope.launch {
            _generationState.value = GenerationUiState.Loading("PostCraft AI is analyzing ground data and generating multi-platform posts...")

            val groundedFacts = (_searchGroundingState.value as? SearchGroundingUiState.Success)?.factualSummary
            val searchCitations = (_searchGroundingState.value as? SearchGroundingUiState.Success)?.citations ?: emptyList()

            val aiResult = aiRepository.generateSocialPosts(
                theme = currentForm.theme,
                scope = currentForm.scope,
                localUnit = currentForm.localUnit,
                subDistrict = currentForm.subDistrict,
                district = currentForm.district,
                stateRegion = currentForm.stateRegion,
                claim = currentForm.claim,
                groundReality = currentForm.groundReality,
                tone = currentForm.tone,
                visualBitmap = currentForm.photoBitmap,
                eventDetails = currentForm.eventDetails,
                groundedFactsContext = groundedFacts
            )

            val bundle = aiResult.getOrElse {
                Log.w(TAG, "Gemini failed, using offline template generator fallback", it)
                TemplatePostGenerator.generateOfflineBundle(
                    theme = currentForm.theme,
                    scope = currentForm.scope,
                    localUnit = currentForm.localUnit,
                    subDistrict = currentForm.subDistrict,
                    district = currentForm.district,
                    stateRegion = currentForm.stateRegion,
                    claim = currentForm.claim,
                    reality = currentForm.groundReality,
                    tone = currentForm.tone,
                    hasImage = currentForm.photoBitmap != null,
                    eventDetails = currentForm.eventDetails
                )
            }.let { base ->
                if (searchCitations.isNotEmpty() && base.searchCitations.isEmpty()) {
                    base.copy(searchCitations = searchCitations)
                } else {
                    base
                }
            }

            _currentBundle.value = bundle
            _generationState.value = GenerationUiState.Success(bundle, isAiGenerated = aiResult.isSuccess)
            updateDraftForSelection(bundle, _activePlatform.value, _activeLanguage.value)
        }
    }

    fun selectPlatform(platform: SocialPlatform) {
        _activePlatform.value = platform
        _currentBundle.value?.let { bundle ->
            updateDraftForSelection(bundle, platform, _activeLanguage.value)
        }
    }

    fun selectLanguage(language: LanguageMode) {
        val previous = _activeLanguage.value
        _activeLanguage.value = language
        _currentBundle.value?.let { bundle ->
            updateDraftForSelection(bundle, _activePlatform.value, language)
        }
        if (previous != language) {
            val form = _formState.value
            // If currently on one of the presets, switch its text to match the new language
            if (form.localUnit == "वार्ड नं. 24" || form.localUnit == "Ward 24") {
                loadPreset(0, language)
            } else if (form.localUnit == "छमरछ" || form.localUnit == "Chhamrachh") {
                loadPreset(1, language)
            } else if (form.localUnit == "बरगवां" || form.localUnit == "Bargawan") {
                loadPreset(2, language)
            } else if (form.localUnit == "ज़िला सिविल अस्पताल" || form.localUnit == "District Civil Hospital") {
                loadPreset(3, language)
            }
        }
    }

    fun updateDraftText(newText: String) {
        _editableDraftText.value = newText
        val bundle = _currentBundle.value ?: return
        val updated = when (_activePlatform.value) {
            SocialPlatform.TWITTER -> {
                if (_activeLanguage.value == LanguageMode.HINDI) bundle.copy(twitterHindi = newText)
                else bundle.copy(twitterEnglish = newText)
            }
            SocialPlatform.INSTAGRAM_FB -> {
                if (_activeLanguage.value == LanguageMode.HINDI) bundle.copy(instagramFbHindi = newText)
                else bundle.copy(instagramFbEnglish = newText)
            }
            SocialPlatform.PRESS_RELEASE -> {
                if (_activeLanguage.value == LanguageMode.HINDI) bundle.copy(pressReleaseHindi = newText)
                else bundle.copy(pressReleaseEnglish = newText)
            }
            SocialPlatform.LINKEDIN -> {
                if (_activeLanguage.value == LanguageMode.HINDI) bundle.copy(linkedInHindi = newText)
                else bundle.copy(linkedInEnglish = newText)
            }
        }
        _currentBundle.value = updated
    }

    private fun updateDraftForSelection(bundle: GeneratedPostBundle, platform: SocialPlatform, language: LanguageMode) {
        _editableDraftText.value = bundle.getPost(platform, language)
    }

    fun saveCurrentReportToCloud(onComplete: (Boolean) -> Unit) {
        val user = Firebase.auth.currentUser
        if (user == null) {
            _saveStatusMessage.value = "Sign in required to save report to profile"
            onComplete(false)
            return
        }

        val bundle = _currentBundle.value
        val form = _formState.value
        if (bundle == null) {
            _saveStatusMessage.value = "Generate posts first before saving"
            onComplete(false)
            return
        }

        viewModelScope.launch {
            try {
                val report = CampaignReport(
                    userId = user.uid,
                    theme = form.theme.name,
                    scope = form.scope.name,
                    localUnit = form.localUnit,
                    subDistrict = form.subDistrict,
                    district = form.district,
                    stateRegion = form.stateRegion,
                    claim = form.claim,
                    groundReality = form.groundReality,
                    tone = form.tone.name,
                    visualProofSummary = bundle.visualProofSummary,
                    suggestedHashtags = bundle.suggestedHashtags,
                    alternativeTones = bundle.alternativeTones,
                    eventType = form.eventDetails.eventType.name,
                    eventTitle = form.eventDetails.title,
                    eventDate = form.eventDetails.date,
                    eventVenue = form.eventDetails.venue,
                    isUpcomingEvent = form.eventDetails.isUpcoming,
                    twitterHindi = bundle.twitterHindi,
                    twitterEnglish = bundle.twitterEnglish,
                    instagramFbHindi = bundle.instagramFbHindi,
                    instagramFbEnglish = bundle.instagramFbEnglish,
                    pressReleaseHindi = bundle.pressReleaseHindi,
                    pressReleaseEnglish = bundle.pressReleaseEnglish,
                    linkedInHindi = bundle.linkedInHindi,
                    linkedInEnglish = bundle.linkedInEnglish
                )

                firestoreRepository.saveReport(report)
                _saveStatusMessage.value = "Report saved to your profile history!"
                onComplete(true)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save report to Firestore", e)
                _saveStatusMessage.value = "Save failed: ${e.localizedMessage}"
                onComplete(false)
            }
        }
    }

    fun deleteReport(reportId: String) {
        viewModelScope.launch {
            try {
                firestoreRepository.deleteReport(reportId)
                _saveStatusMessage.value = "Report deleted"
            } catch (e: Exception) {
                Log.e(TAG, "Failed to delete report", e)
            }
        }
    }

    fun loadSavedReport(report: CampaignReport) {
        val event = EventDetails(
            eventType = EventType.entries.find { it.name == report.eventType } ?: EventType.NONE,
            title = report.eventTitle,
            date = report.eventDate,
            venue = report.eventVenue,
            isUpcoming = report.isUpcomingEvent
        )
        val loadedScope = IssueScope.entries.find { it.name == report.scope } ?: IssueScope.VILLAGE
        _formState.value = _formState.value.copy(
            theme = CampaignTheme.entries.find { it.name == report.theme } ?: CampaignTheme.URBAN_CIVIC,
            scope = loadedScope,
            localUnit = report.localUnit,
            subDistrict = report.subDistrict,
            district = report.district,
            stateRegion = report.stateRegion,
            claim = report.claim,
            groundReality = report.groundReality,
            tone = ReportTone.entries.find { it.name == report.tone } ?: ReportTone.INVESTIGATIVE,
            eventDetails = event
        )
        val bundle = report.toPostBundle()
        _currentBundle.value = bundle
        _generationState.value = GenerationUiState.Success(bundle, isAiGenerated = true)
        updateDraftForSelection(bundle, _activePlatform.value, _activeLanguage.value)
    }

    fun clearStatusMessage() {
        _saveStatusMessage.value = null
    }
}
