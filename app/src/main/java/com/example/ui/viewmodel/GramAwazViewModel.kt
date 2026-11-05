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

private const val TAG = "GramAwazViewModel"

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
    val theme: CampaignTheme = CampaignTheme.GROUND_REALITY,
    val village: String = "Chhamrachh / छमरछ",
    val tehsil: String = "Sarai / सरई",
    val district: String = "Singrauli / सिंगरौली",
    val state: String = "Madhya Pradesh / मध्य प्रदेश",
    val claim: String = "100% tap water connection under Jal Jeevan Mission, potable water supplied to all households",
    val groundReality: String = "Nal sukhe hain, paani nahi aa raha, pipeline tooti hai, gaon wale handpump ke liye 2 ghante line lagate hain",
    val tone: ReportTone = ReportTone.INVESTIGATIVE,
    val photoUri: Uri? = null,
    val photoBitmap: Bitmap? = null,
    val eventDetails: EventDetails = EventDetails()
)

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

    fun updateVillage(village: String) {
        _formState.value = _formState.value.copy(village = village)
    }

    fun updateTehsil(tehsil: String) {
        _formState.value = _formState.value.copy(tehsil = tehsil)
    }

    fun updateDistrict(district: String) {
        _formState.value = _formState.value.copy(district = district)
    }

    fun updateState(state: String) {
        _formState.value = _formState.value.copy(state = state)
    }

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
        val loc = "${form.district}, ${form.state}"
        viewModelScope.launch {
            _searchGroundingState.value = SearchGroundingUiState.Loading("Searching official portal & guidelines for ${form.claim.take(30)}...")
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

    fun loadPreset(presetIndex: Int) {
        when (presetIndex) {
            0 -> { // Jal Jeevan Mission
                _formState.value = _formState.value.copy(
                    theme = CampaignTheme.GROUND_REALITY,
                    village = "Chhamrachh / छमरछ",
                    tehsil = "Sarai / सरई",
                    district = "Singrauli / सिंगरौली",
                    state = "Madhya Pradesh / मध्य प्रदेश",
                    claim = "100% tap water connection under Jal Jeevan Mission (Har Ghar Jal Certified)",
                    groundReality = "Nal bilkul sukhe hain, paani nahi aa raha, pipeline tooti hai, log subah 4 baje se handpump par line lagate hain",
                    tone = ReportTone.INVESTIGATIVE,
                    eventDetails = EventDetails(
                        eventType = EventType.JAN_SUNWAI,
                        title = "कलेक्टर जनसुनवाई (District Public Hearing)",
                        date = "आगामी मंगलवार 11:00 AM",
                        venue = "ज़िला कलेक्ट्रेट सभागार, बैढ़न (सिंगरौली)",
                        isUpcoming = true
                    )
                )
            }
            1 -> { // PMGSY Road
                _formState.value = _formState.value.copy(
                    theme = CampaignTheme.RURAL_DEVELOPMENT,
                    village = "Bargawan / बरगवां",
                    tehsil = "Deosar / देवसर",
                    district = "Singrauli / सिंगरौली",
                    state = "Madhya Pradesh / मध्य प्रदेश",
                    claim = "All-weather pucca road constructed under PMGSY with 5-year maintenance guarantee",
                    groundReality = "Pehli hi baarish me sadak beh gayi, bade-bade gaddhe ho gaye hain, ambulance gaon me nahi aa sakti",
                    tone = ReportTone.CONSTRUCTIVE,
                    eventDetails = EventDetails(
                        eventType = EventType.GRAM_SABHA,
                        title = "विशेष ग्राम सभा बैठक",
                        date = "15 तारीख, सुबह 10:00 बजे",
                        venue = "पंचायत भवन, बरगवां",
                        isUpcoming = true
                    )
                )
            }
            2 -> { // Primary School / Anganwadi
                _formState.value = _formState.value.copy(
                    theme = CampaignTheme.EDUCATION_YOUTH,
                    village = "Katai / कटाई",
                    tehsil = "Sarai / सरई",
                    district = "Singrauli / सिंगरौली",
                    state = "Madhya Pradesh / मध्य प्रदेश",
                    claim = "Model primary school equipped with smart classrooms, functional toilets, and nutritious mid-day meals",
                    groundReality = "School ki chhat tapak rahi hai, shauchalay par tala laga hai, 1 teacher par 80 bachhe hain",
                    tone = ReportTone.STORYTELLING,
                    eventDetails = EventDetails(
                        eventType = EventType.OFFICIAL_INSPECTION,
                        title = "जिला शिक्षा अधिकारी निरीक्षण",
                        date = "गत 2 दिन पूर्व",
                        venue = "शासकीय प्राथमिक विद्यालय, कटाई",
                        isUpcoming = false
                    )
                )
            }
        }
    }

    fun generatePosts() {
        val currentForm = _formState.value
        if (currentForm.claim.isBlank() || currentForm.groundReality.isBlank()) {
            _generationState.value = GenerationUiState.Error("Please enter both the Government Claim and Ground Reality.")
            return
        }

        viewModelScope.launch {
            _generationState.value = GenerationUiState.Loading("Gemini AI is analyzing ground data, event info, and generating 4-platform posts...")

            val groundedFacts = (_searchGroundingState.value as? SearchGroundingUiState.Success)?.factualSummary
            val searchCitations = (_searchGroundingState.value as? SearchGroundingUiState.Success)?.citations ?: emptyList()

            val aiResult = aiRepository.generateSocialPosts(
                theme = currentForm.theme,
                village = currentForm.village,
                tehsil = currentForm.tehsil,
                district = currentForm.district,
                state = currentForm.state,
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
                    village = currentForm.village,
                    tehsil = currentForm.tehsil,
                    district = currentForm.district,
                    state = currentForm.state,
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
        _activeLanguage.value = language
        _currentBundle.value?.let { bundle ->
            updateDraftForSelection(bundle, _activePlatform.value, language)
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
            SocialPlatform.INSTAGRAM -> {
                if (_activeLanguage.value == LanguageMode.HINDI) bundle.copy(instagramHindi = newText)
                else bundle.copy(instagramEnglish = newText)
            }
            SocialPlatform.LINKEDIN -> {
                if (_activeLanguage.value == LanguageMode.HINDI) bundle.copy(linkedInHindi = newText)
                else bundle.copy(linkedInEnglish = newText)
            }
            SocialPlatform.FACEBOOK -> {
                if (_activeLanguage.value == LanguageMode.HINDI) bundle.copy(facebookHindi = newText)
                else bundle.copy(facebookEnglish = newText)
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
                    village = form.village,
                    tehsil = form.tehsil,
                    district = form.district,
                    state = form.state,
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
                    instagramHindi = bundle.instagramHindi,
                    instagramEnglish = bundle.instagramEnglish,
                    linkedInHindi = bundle.linkedInHindi,
                    linkedInEnglish = bundle.linkedInEnglish,
                    facebookHindi = bundle.facebookHindi,
                    facebookEnglish = bundle.facebookEnglish
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
        _formState.value = _formState.value.copy(
            theme = CampaignTheme.entries.find { it.name == report.theme } ?: CampaignTheme.GROUND_REALITY,
            village = report.village,
            tehsil = report.tehsil,
            district = report.district,
            state = report.state,
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
