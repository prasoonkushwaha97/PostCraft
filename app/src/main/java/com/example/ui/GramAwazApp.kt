package com.example.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.auth.AuthManager
import com.example.data.model.LanguageMode
import com.example.ui.components.AuthBar
import com.example.ui.components.CameraCaptureDialog
import com.example.ui.components.ReportFormPane
import com.example.ui.components.SavedReportsDialog
import com.example.ui.components.SocialPreviewDeck
import com.example.ui.components.VisualQuoteCardDialog
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CardBackground
import com.example.ui.theme.MainBackground
import com.example.ui.theme.PrimaryNavy
import com.example.ui.theme.PrimaryText
import com.example.ui.theme.SecondaryBlue
import com.example.ui.theme.SecondaryText
import com.example.ui.util.AppStrings
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Design 1: Modern 3D 'PC' monogram as primary app icon on crisp newsroom badge
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, AccentCyan.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_postcraft_logo),
                                contentDescription = "PostCraft Logo",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = AppStrings.appTitle(activeLanguage),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontSize = 17.sp,
                                        letterSpacing = 0.3.sp
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(AccentCyan)
                                )
                            }
                            Text(
                                text = AppStrings.appSubtitle(activeLanguage),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color(0xFFCBD5E1),
                                    fontSize = 11.sp
                                ),
                                maxLines = 1
                            )
                        }
                    }
                },
                actions = {
                    // Header Language Switcher Pill (Single-Tap Toggle between हिंदी / English)
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF0A1828),
                        border = BorderStroke(1.dp, AccentCyan.copy(alpha = 0.3f)),
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(3.dp),
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            LanguageMode.entries.forEach { lang ->
                                val isSelected = activeLanguage == lang
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) AccentCyan else Color.Transparent)
                                        .clickable { viewModel.selectLanguage(lang) }
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                        .testTag("header_lang_toggle_${lang.name.lowercase()}"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = lang.label,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) PrimaryNavy else Color(0xFF94A3B8)
                                    )
                                }
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryNavy
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MainBackground,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Top Auth / User Profile Newsroom White Card
            AuthBar(
                currentUser = currentUser,
                authManager = authManager,
                activeLanguage = activeLanguage,
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
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
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
                                activeLanguage = activeLanguage,
                                onScopeChanged = viewModel::updateScope,
                                onThemeChanged = viewModel::updateTheme,
                                onLocalUnitChanged = viewModel::updateLocalUnit,
                                onSubDistrictChanged = viewModel::updateSubDistrict,
                                onDistrictChanged = viewModel::updateDistrict,
                                onStateRegionChanged = viewModel::updateStateRegion,
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
                            color = BorderSubtle,
                            thickness = 1.dp
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
                    // Mobile View with Modern Newsroom Segmented Control Switcher
                    Column(modifier = Modifier.fillMaxSize()) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = CardBackground,
                            border = BorderStroke(1.dp, BorderSubtle),
                            shadowElevation = 2.dp,
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
                                        .background(if (mobileSelectedTab == 0) PrimaryNavy else Color.Transparent)
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
                                            tint = if (mobileSelectedTab == 0) AccentCyan else SecondaryText
                                        )
                                        Text(
                                            text = AppStrings.tabGroundForm(activeLanguage),
                                            fontWeight = if (mobileSelectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                                            fontSize = 12.sp,
                                            color = if (mobileSelectedTab == 0) Color.White else SecondaryText
                                        )
                                    }
                                }

                                // Tab 1: Social Deck
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (mobileSelectedTab == 1) PrimaryNavy else Color.Transparent)
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
                                            tint = if (mobileSelectedTab == 1) AccentCyan else SecondaryText
                                        )
                                        Text(
                                            text = AppStrings.tabSocialDeck(activeLanguage),
                                            fontWeight = if (mobileSelectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                                            fontSize = 12.sp,
                                            color = if (mobileSelectedTab == 1) Color.White else SecondaryText
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
                                    activeLanguage = activeLanguage,
                                    onScopeChanged = viewModel::updateScope,
                                    onThemeChanged = viewModel::updateTheme,
                                    onLocalUnitChanged = viewModel::updateLocalUnit,
                                    onSubDistrictChanged = viewModel::updateSubDistrict,
                                    onDistrictChanged = viewModel::updateDistrict,
                                    onStateRegionChanged = viewModel::updateStateRegion,
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
                village = formState.localUnit,
                district = formState.district,
                claim = formState.claim,
                reality = formState.groundReality,
                proofBitmap = formState.photoBitmap,
                captionText = editableDraftText,
                eventBadge = formState.eventDetails.getFormattedBadge(activeLanguage),
                mode = activeLanguage,
                onDismiss = { showVisualCardDialog = false }
            )
        }

        // Saved Reports History Dialog
        if (showHistoryDialog) {
            SavedReportsDialog(
                reports = savedReports,
                mode = activeLanguage,
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
