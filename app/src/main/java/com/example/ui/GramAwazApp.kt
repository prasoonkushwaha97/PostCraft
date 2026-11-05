package com.example.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.auth.AuthManager
import com.example.ui.components.AuthBar
import com.example.ui.components.CameraCaptureDialog
import com.example.ui.components.ReportFormPane
import com.example.ui.components.SavedReportsDialog
import com.example.ui.components.SocialPreviewDeck
import com.example.ui.components.VisualQuoteCardDialog
import com.example.ui.theme.SafetyOrange
import com.example.ui.theme.Zinc100
import com.example.ui.theme.Zinc400
import com.example.ui.theme.Zinc800
import com.example.ui.theme.Zinc900
import com.example.ui.theme.Zinc950
import com.example.ui.viewmodel.GenerationUiState
import com.example.ui.viewmodel.GramAwazViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GramAwazApp(
    viewModel: GramAwazViewModel,
    authManager: AuthManager
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var currentUser by remember { mutableStateOf<FirebaseUser?>(Firebase.auth.currentUser) }

    LaunchedEffect(Unit) {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            currentUser = auth.currentUser
        }
        Firebase.auth.addAuthStateListener(listener)
    }

    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val generationState by viewModel.generationState.collectAsStateWithLifecycle()
    val searchGroundingState by viewModel.searchGroundingState.collectAsStateWithLifecycle()
    val currentBundle by viewModel.currentBundle.collectAsStateWithLifecycle()
    val activePlatform by viewModel.activePlatform.collectAsStateWithLifecycle()
    val activeLanguage by viewModel.activeLanguage.collectAsStateWithLifecycle()
    val editableDraftText by viewModel.editableDraftText.collectAsStateWithLifecycle()
    val savedReports by viewModel.userSavedReports.collectAsStateWithLifecycle()
    val saveStatusMessage by viewModel.saveStatusMessage.collectAsStateWithLifecycle()

    var showVisualCardDialog by remember { mutableStateOf(false) }
    var showHistoryDialog by remember { mutableStateOf(false) }
    var showCameraCaptureDialog by remember { mutableStateOf(false) }
    var mobileSelectedTab by remember { mutableIntStateOf(0) }

    // When posts are generated on mobile, automatically switch to the preview tab
    LaunchedEffect(generationState) {
        if (generationState is GenerationUiState.Success) {
            mobileSelectedTab = 1
        }
    }

    LaunchedEffect(saveStatusMessage) {
        saveStatusMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearStatusMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "GramAwaz | ग्राम आवाज़",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = SafetyOrange
                            )
                        )
                        Text(
                            text = "Citizen Journalism & Rural Governance | साथी फाउंडेशन",
                            style = MaterialTheme.typography.labelSmall.copy(color = Zinc400)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Zinc950
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Zinc950,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Top Auth / User Profile Glassmorphism Bar
            AuthBar(
                currentUser = currentUser,
                authManager = authManager,
                onAuthSuccess = { user ->
                    currentUser = user
                    Toast.makeText(context, "Welcome ${user.displayName.orEmpty()}", Toast.LENGTH_SHORT).show()
                },
                onSignOut = {
                    currentUser = null
                    Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
                },
                onOpenHistory = {
                    showHistoryDialog = true
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )

            // Responsive Dual-Pane vs Mobile Segmented Tabs
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val isWideScreen = maxWidth >= 768.dp

                if (isWideScreen) {
                    // Desktop / Tablet Dual-Pane Side-by-Side
                    Row(modifier = Modifier.fillMaxSize()) {
                        // Left Pane: Intake Form
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            ReportFormPane(
                                formState = formState,
                                generationState = generationState,
                                searchGroundingState = searchGroundingState,
                                onThemeChanged = viewModel::updateTheme,
                                onVillageChanged = viewModel::updateVillage,
                                onTehsilChanged = viewModel::updateTehsil,
                                onDistrictChanged = viewModel::updateDistrict,
                                onStateChanged = viewModel::updateState,
                                onClaimChanged = viewModel::updateClaim,
                                onRealityChanged = viewModel::updateGroundReality,
                                onToneChanged = viewModel::updateTone,
                                onEventTypeChanged = viewModel::updateEventType,
                                onEventTitleChanged = viewModel::updateEventTitle,
                                onEventDateChanged = viewModel::updateEventDate,
                                onEventVenueChanged = viewModel::updateEventVenue,
                                onEventIsUpcomingChanged = viewModel::updateEventIsUpcoming,
                                onVerifySchemeClicked = viewModel::verifyOfficialSchemeViaSearch,
                                onPhotoSelected = { uri -> viewModel.setPhoto(context, uri) },
                                onRemovePhoto = viewModel::removePhoto,
                                onOpenCameraClicked = { showCameraCaptureDialog = true },
                                onLoadPreset = viewModel::loadPreset,
                                onGenerateClicked = viewModel::generatePosts
                            )
                        }

                        VerticalDivider(
                            modifier = Modifier.fillMaxHeight(),
                            color = Zinc800
                        )

                        // Right Pane: Multi-Platform Social Deck
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            SocialPreviewDeck(
                                bundle = currentBundle,
                                activePlatform = activePlatform,
                                activeLanguage = activeLanguage,
                                editableDraftText = editableDraftText,
                                onPlatformSelected = viewModel::selectPlatform,
                                onLanguageSelected = viewModel::selectLanguage,
                                onDraftTextChanged = viewModel::updateDraftText,
                                onOpenExportCardDialog = { showVisualCardDialog = true },
                                onSaveToCloudClicked = {
                                    viewModel.saveCurrentReportToCloud { success ->
                                        if (!success && currentUser == null) {
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Please sign in with Google to save reports.")
                                            }
                                        }
                                    }
                                }
                            )
                        }
                    }
                } else {
                    // Mobile View with Modern Segmented Control / Pill Switcher
                    // bg-zinc-900 border border-zinc-800 rounded-xl p-1 flex gap-1
                    Column(modifier = Modifier.fillMaxSize()) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Zinc900,
                            border = BorderStroke(1.dp, Zinc800),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                                .testTag("mobile_navigation_tabs")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                // Tab 0: Ground Form
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (mobileSelectedTab == 0) SafetyOrange else Color.Transparent)
                                        .clickable { mobileSelectedTab = 0 }
                                        .padding(vertical = 10.dp)
                                        .testTag("mobile_tab_form"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Description,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp),
                                            tint = if (mobileSelectedTab == 0) Color.White else Zinc400
                                        )
                                        Text(
                                            text = "1. Ground Form",
                                            fontWeight = if (mobileSelectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                                            fontSize = 13.sp,
                                            color = if (mobileSelectedTab == 0) Color.White else Zinc400
                                        )
                                    }
                                }

                                // Tab 1: Social Deck
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (mobileSelectedTab == 1) SafetyOrange else Color.Transparent)
                                        .clickable { mobileSelectedTab = 1 }
                                        .padding(vertical = 10.dp)
                                        .testTag("mobile_tab_social"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Share,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp),
                                            tint = if (mobileSelectedTab == 1) Color.White else Zinc400
                                        )
                                        Text(
                                            text = "2. Social Deck",
                                            fontWeight = if (mobileSelectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                                            fontSize = 13.sp,
                                            color = if (mobileSelectedTab == 1) Color.White else Zinc400
                                        )
                                    }
                                }
                            }
                        }

                        Box(modifier = Modifier.fillMaxSize()) {
                            if (mobileSelectedTab == 0) {
                                ReportFormPane(
                                    formState = formState,
                                    generationState = generationState,
                                    searchGroundingState = searchGroundingState,
                                    onThemeChanged = viewModel::updateTheme,
                                    onVillageChanged = viewModel::updateVillage,
                                    onTehsilChanged = viewModel::updateTehsil,
                                    onDistrictChanged = viewModel::updateDistrict,
                                    onStateChanged = viewModel::updateState,
                                    onClaimChanged = viewModel::updateClaim,
                                    onRealityChanged = viewModel::updateGroundReality,
                                    onToneChanged = viewModel::updateTone,
                                    onEventTypeChanged = viewModel::updateEventType,
                                    onEventTitleChanged = viewModel::updateEventTitle,
                                    onEventDateChanged = viewModel::updateEventDate,
                                    onEventVenueChanged = viewModel::updateEventVenue,
                                    onEventIsUpcomingChanged = viewModel::updateEventIsUpcoming,
                                    onVerifySchemeClicked = viewModel::verifyOfficialSchemeViaSearch,
                                    onPhotoSelected = { uri -> viewModel.setPhoto(context, uri) },
                                    onRemovePhoto = viewModel::removePhoto,
                                    onOpenCameraClicked = { showCameraCaptureDialog = true },
                                    onLoadPreset = viewModel::loadPreset,
                                    onGenerateClicked = viewModel::generatePosts
                                )
                            } else {
                                SocialPreviewDeck(
                                    bundle = currentBundle,
                                    activePlatform = activePlatform,
                                    activeLanguage = activeLanguage,
                                    editableDraftText = editableDraftText,
                                    onPlatformSelected = viewModel::selectPlatform,
                                    onLanguageSelected = viewModel::selectLanguage,
                                    onDraftTextChanged = viewModel::updateDraftText,
                                    onOpenExportCardDialog = { showVisualCardDialog = true },
                                    onSaveToCloudClicked = {
                                        viewModel.saveCurrentReportToCloud { success ->
                                            if (!success && currentUser == null) {
                                                coroutineScope.launch {
                                                    snackbarHostState.showSnackbar("Please sign in with Google to save reports.")
                                                }
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // CameraX Live Capture Dialog
        if (showCameraCaptureDialog) {
            CameraCaptureDialog(
                onPhotoCaptured = { uri ->
                    viewModel.setPhoto(context, uri)
                },
                onDismiss = { showCameraCaptureDialog = false }
            )
        }

        // Visual Quote Card Modal Dialog
        if (showVisualCardDialog) {
            VisualQuoteCardDialog(
                village = formState.village,
                district = formState.district,
                claim = formState.claim,
                reality = formState.groundReality,
                proofBitmap = formState.photoBitmap,
                captionText = editableDraftText,
                eventBadge = formState.eventDetails.getFormattedBadge(),
                onDismiss = { showVisualCardDialog = false }
            )
        }

        // Saved Reports History Dialog
        if (showHistoryDialog) {
            SavedReportsDialog(
                reports = savedReports,
                onSelectReport = { report ->
                    viewModel.loadSavedReport(report)
                    mobileSelectedTab = 1
                },
                onDeleteReport = viewModel::deleteReport,
                onDismiss = { showHistoryDialog = false }
            )
        }
    }
}
