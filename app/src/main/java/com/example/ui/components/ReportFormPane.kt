package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FactCheck
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.model.CampaignTheme
import com.example.data.model.EventType
import com.example.data.model.IssueScope
import com.example.data.model.LanguageMode
import com.example.data.model.ReportTone
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.BorderActive
import com.example.ui.theme.BorderHover
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CardBackground
import com.example.ui.theme.CardBackgroundMuted
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.ErrorRedSubtle
import com.example.ui.theme.PlaceholderText
import com.example.ui.theme.PrimaryNavy
import com.example.ui.theme.PrimaryText
import com.example.ui.theme.SecondaryBlue
import com.example.ui.theme.SecondaryText
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SuccessGreenSubtle
import com.example.ui.util.AppStrings
import com.example.ui.viewmodel.GenerationUiState
import com.example.ui.viewmodel.ReportFormState
import com.example.ui.viewmodel.SearchGroundingUiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ReportFormPane(
    formState: ReportFormState,
    generationState: GenerationUiState,
    searchGroundingState: SearchGroundingUiState = SearchGroundingUiState.Idle,
    activeLanguage: LanguageMode = LanguageMode.HINDI,
    onScopeChanged: (IssueScope) -> Unit = {},
    onThemeChanged: (CampaignTheme) -> Unit,
    onLocalUnitChanged: (String) -> Unit,
    onSubDistrictChanged: (String) -> Unit,
    onDistrictChanged: (String) -> Unit,
    onStateRegionChanged: (String) -> Unit,
    onClaimChanged: (String) -> Unit,
    onRealityChanged: (String) -> Unit,
    onToneChanged: (ReportTone) -> Unit,
    onEventTypeChanged: (EventType) -> Unit,
    onEventTitleChanged: (String) -> Unit,
    onEventDateChanged: (String) -> Unit,
    onEventVenueChanged: (String) -> Unit,
    onEventIsUpcomingChanged: (Boolean) -> Unit,
    onVerifySchemeClicked: () -> Unit = {},
    onPhotoSelected: (Uri?) -> Unit,
    onRemovePhoto: () -> Unit,
    onOpenCameraClicked: () -> Unit,
    onLoadPreset: (Int) -> Unit,
    onGenerateClicked: () -> Unit,
    // Deprecated compatibility parameters
    onVillageChanged: (String) -> Unit = onLocalUnitChanged,
    onTehsilChanged: (String) -> Unit = onSubDistrictChanged,
    onStateChanged: (String) -> Unit = onStateRegionChanged,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    var isThemeDropdownOpen by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        onPhotoSelected(uri)
    }

    val scrollState = rememberScrollState()

    // Newsroom standard text field styling
    val customTextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = CardBackground,
        unfocusedContainerColor = CardBackgroundMuted,
        focusedBorderColor = SecondaryBlue,
        unfocusedBorderColor = BorderSubtle,
        focusedTextColor = PrimaryText,
        unfocusedTextColor = PrimaryText,
        focusedPlaceholderColor = PlaceholderText,
        unfocusedPlaceholderColor = PlaceholderText,
        cursorColor = SecondaryBlue
    )

    val claimTextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = CardBackground,
        unfocusedContainerColor = CardBackgroundMuted,
        focusedBorderColor = SecondaryBlue,
        unfocusedBorderColor = BorderSubtle,
        focusedTextColor = PrimaryText,
        unfocusedTextColor = PrimaryText,
        focusedPlaceholderColor = PlaceholderText,
        unfocusedPlaceholderColor = PlaceholderText,
        cursorColor = SecondaryBlue
    )

    val realityTextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = CardBackground,
        unfocusedContainerColor = CardBackgroundMuted,
        focusedBorderColor = ErrorRed,
        unfocusedBorderColor = BorderSubtle,
        focusedTextColor = PrimaryText,
        unfocusedTextColor = PrimaryText,
        focusedPlaceholderColor = PlaceholderText,
        unfocusedPlaceholderColor = PlaceholderText,
        cursorColor = ErrorRed
    )

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("report_form_pane"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section Header with Quick Presets
        Column {
            Text(
                text = AppStrings.intakeTitle(activeLanguage),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryNavy
                )
            )
            Text(
                text = AppStrings.intakeSubtitle(activeLanguage),
                style = MaterialTheme.typography.bodySmall.copy(color = SecondaryText),
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Preset Chips Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.FlashOn,
                    contentDescription = null,
                    modifier = Modifier.size(15.dp),
                    tint = SecondaryBlue
                )
                Text(
                    text = AppStrings.quickPresets(activeLanguage),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = SecondaryText,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            // Quick Preset Pills (Diverse categories: Urban, Rural, Governance, Healthcare)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 6.dp)
            ) {
                Surface(
                    onClick = { onLoadPreset(0) },
                    shape = RoundedCornerShape(50),
                    color = CardBackgroundMuted,
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.testTag("preset_urban_civic")
                ) {
                    Text(
                        text = AppStrings.presetUrbanCivic(activeLanguage),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = PrimaryText,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                Surface(
                    onClick = { onLoadPreset(1) },
                    shape = RoundedCornerShape(50),
                    color = CardBackgroundMuted,
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.testTag("preset_rural_water")
                ) {
                    Text(
                        text = AppStrings.presetRuralWater(activeLanguage),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = PrimaryText,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                Surface(
                    onClick = { onLoadPreset(2) },
                    shape = RoundedCornerShape(50),
                    color = CardBackgroundMuted,
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.testTag("preset_governance")
                ) {
                    Text(
                        text = AppStrings.presetRoad(activeLanguage),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = PrimaryText,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                Surface(
                    onClick = { onLoadPreset(3) },
                    shape = RoundedCornerShape(50),
                    color = CardBackgroundMuted,
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.testTag("preset_hospital")
                ) {
                    Text(
                        text = AppStrings.presetGovernance(activeLanguage),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = PrimaryText,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // 1. Issue Category Dropdown Card (Urban, Rural, Policy, Education, Legal)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            border = BorderStroke(1.dp, BorderSubtle)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = AppStrings.campaignThemeLabel(activeLanguage),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = SecondaryText,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardBackgroundMuted)
                        .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
                        .clickable { isThemeDropdownOpen = true }
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                        .testTag("theme_selector_dropdown")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${formState.theme.icon} ${formState.theme.label(activeLanguage)}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = PrimaryNavy
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Expand theme dropdown",
                            tint = SecondaryText
                        )
                    }

                    DropdownMenu(
                        expanded = isThemeDropdownOpen,
                        onDismissRequest = { isThemeDropdownOpen = false },
                        modifier = Modifier.background(CardBackground)
                    ) {
                        CampaignTheme.entries.forEach { theme ->
                            DropdownMenuItem(
                                text = {
                                    Column(modifier = Modifier.padding(vertical = 2.dp)) {
                                        Text(
                                            text = "${theme.icon} ${theme.label(activeLanguage)}",
                                            fontWeight = if (formState.theme == theme) FontWeight.Bold else FontWeight.Normal,
                                            color = if (formState.theme == theme) PrimaryNavy else PrimaryText,
                                            fontSize = 13.sp
                                        )
                                        Text(
                                            text = theme.desc(activeLanguage),
                                            style = MaterialTheme.typography.bodySmall.copy(color = SecondaryText),
                                            fontSize = 11.sp
                                        )
                                    }
                                },
                                onClick = {
                                    onThemeChanged(theme)
                                    isThemeDropdownOpen = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // 2. Geographic Issue Scope Selector Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            border = BorderStroke(1.dp, BorderSubtle)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Public,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = SecondaryBlue
                    )
                    Text(
                        text = AppStrings.issueScopeLabel(activeLanguage),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = SecondaryText,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IssueScope.entries.forEach { scope ->
                        val isSelected = formState.scope == scope
                        Surface(
                            onClick = { onScopeChanged(scope) },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) PrimaryNavy else CardBackgroundMuted,
                            border = BorderStroke(1.dp, if (isSelected) PrimaryNavy else BorderSubtle),
                            modifier = Modifier.testTag("scope_chip_${scope.name.lowercase()}")
                        ) {
                            Text(
                                text = scope.label(activeLanguage),
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else SecondaryText,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                            )
                        }
                    }
                }
            }
        }

        // 3. Administrative Coordinates (Clean 2-Column Grid with Scope-Specific Labels)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            border = BorderStroke(1.dp, BorderSubtle)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = SecondaryBlue
                    )
                    Text(
                        text = AppStrings.adminCoordsTitle(activeLanguage),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = SecondaryText,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                // Row 1: Local Unit & Sub-District
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = AppStrings.localUnitLabel(formState.scope, activeLanguage),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = PrimaryText,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = formState.localUnit,
                            onValueChange = onLocalUnitChanged,
                            placeholder = { Text(AppStrings.localUnitPlaceholder(formState.scope, activeLanguage)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_village"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = customTextFieldColors
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = AppStrings.subDistrictLabel(formState.scope, activeLanguage),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = PrimaryText,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = formState.subDistrict,
                            onValueChange = onSubDistrictChanged,
                            placeholder = { Text(AppStrings.subDistrictPlaceholder(formState.scope, activeLanguage)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_tehsil"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = customTextFieldColors
                        )
                    }
                }

                // Row 2: District & State/Region
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = AppStrings.districtLabel(formState.scope, activeLanguage),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = PrimaryText,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = formState.district,
                            onValueChange = onDistrictChanged,
                            placeholder = { Text(AppStrings.districtPlaceholder(formState.scope, activeLanguage)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_district"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = customTextFieldColors
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = AppStrings.stateLabel(formState.scope, activeLanguage),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = PrimaryText,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = formState.stateRegion,
                            onValueChange = onStateRegionChanged,
                            placeholder = { Text(AppStrings.statePlaceholder(formState.scope, activeLanguage)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_state"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = customTextFieldColors
                        )
                    }
                }
            }
        }

        // 4. Official Claim vs Ground Reality
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            border = BorderStroke(1.dp, BorderSubtle)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Official Promise / Claim Section
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(bottom = 6.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = SecondaryBlue
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.FactCheck,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = AppStrings.claimBadge(activeLanguage),
                                    fontSize = 11.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = formState.claim,
                        onValueChange = onClaimChanged,
                        placeholder = { Text(AppStrings.claimPlaceholder(activeLanguage)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(95.dp)
                            .testTag("input_claim"),
                        shape = RoundedCornerShape(12.dp),
                        colors = claimTextFieldColors
                    )
                }

                // Ground Reality Section
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(bottom = 6.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = ErrorRed
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ReportProblem,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = AppStrings.realityBadge(activeLanguage),
                                    fontSize = 11.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = formState.groundReality,
                        onValueChange = onRealityChanged,
                        placeholder = { Text(AppStrings.realityPlaceholder(activeLanguage)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .testTag("input_reality"),
                        shape = RoundedCornerShape(12.dp),
                        colors = realityTextFieldColors
                    )
                }

                // Google Search Grounding Trigger Button
                val isSearching = searchGroundingState is SearchGroundingUiState.Loading
                Button(
                    onClick = onVerifySchemeClicked,
                    enabled = !isSearching && formState.claim.isNotBlank(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SecondaryBlue,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("verify_scheme_search_button")
                ) {
                    if (isSearching) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = AppStrings.verifyingSchemeSearch(activeLanguage),
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = AccentCyan
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = AppStrings.verifySchemeSearch(activeLanguage),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Display Google Search Grounded facts and clickable source links
                if (searchGroundingState is SearchGroundingUiState.Success) {
                    val searchResult = searchGroundingState as SearchGroundingUiState.Success
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SuccessGreenSubtle,
                        border = BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = SuccessGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = AppStrings.verifiedSchemeTitle(activeLanguage),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SuccessGreen
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = searchResult.factualSummary,
                                fontSize = 12.sp,
                                color = PrimaryText,
                                lineHeight = 16.sp
                            )

                            if (searchResult.citations.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = AppStrings.officialSources(activeLanguage),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SecondaryText
                                )
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.padding(top = 4.dp)
                                ) {
                                    searchResult.citations.forEach { citation ->
                                        Surface(
                                            onClick = {
                                                try {
                                                    uriHandler.openUri(citation.url)
                                                } catch (_: Exception) {}
                                            },
                                            shape = RoundedCornerShape(50),
                                            color = CardBackground,
                                            border = BorderStroke(1.dp, BorderSubtle)
                                        ) {
                                            Text(
                                                text = citation.title.take(30),
                                                fontSize = 10.sp,
                                                color = SecondaryBlue,
                                                fontWeight = FontWeight.Medium,
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. Related Civic Event (Jan Sunwai, Ward Sabha, Press Conference)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            border = BorderStroke(1.dp, BorderSubtle)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Event,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = SecondaryBlue
                    )
                    Text(
                        text = AppStrings.eventSectionTitle(activeLanguage),
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )
                }

                Text(
                    text = AppStrings.eventSectionSubtitle(activeLanguage),
                    style = MaterialTheme.typography.bodySmall.copy(color = SecondaryText)
                )

                // Event Type Chips
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EventType.entries.forEach { type ->
                        val isSelected = formState.eventDetails.eventType == type
                        Surface(
                            onClick = { onEventTypeChanged(type) },
                            shape = RoundedCornerShape(50),
                            color = if (isSelected) PrimaryNavy else CardBackgroundMuted,
                            border = BorderStroke(1.dp, if (isSelected) PrimaryNavy else BorderSubtle),
                            modifier = Modifier.testTag("event_type_chip_${type.name.lowercase()}")
                        ) {
                            Text(
                                text = type.label(activeLanguage),
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else SecondaryText,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                // If an event type is chosen or title entered
                if (formState.eventDetails.eventType != EventType.NONE) {
                    Column {
                        Text(
                            text = AppStrings.eventTitleLabel(activeLanguage),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = PrimaryText,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = formState.eventDetails.title,
                            onValueChange = onEventTitleChanged,
                            placeholder = { Text(AppStrings.eventTitlePlaceholder(activeLanguage)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_event_title"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = customTextFieldColors
                        )
                    }

                    // 2-Column Grid: Date and Venue
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = AppStrings.eventDateLabel(activeLanguage),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = PrimaryText,
                                    fontWeight = FontWeight.Medium
                                ),
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                            OutlinedTextField(
                                value = formState.eventDetails.date,
                                onValueChange = onEventDateChanged,
                                placeholder = { Text(AppStrings.eventDatePlaceholder(activeLanguage)) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_event_date"),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                colors = customTextFieldColors
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = AppStrings.eventVenueLabel(activeLanguage),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = PrimaryText,
                                    fontWeight = FontWeight.Medium
                                ),
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                            OutlinedTextField(
                                value = formState.eventDetails.venue,
                                onValueChange = onEventVenueChanged,
                                placeholder = { Text(AppStrings.eventVenuePlaceholder(activeLanguage)) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_event_venue"),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                colors = customTextFieldColors
                            )
                        }
                    }

                    // Upcoming Event Switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (formState.eventDetails.isUpcoming) AppStrings.upcomingEvent(activeLanguage) else AppStrings.pastEvent(activeLanguage),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = PrimaryText
                                )
                            )
                            Text(
                                text = if (formState.eventDetails.isUpcoming) AppStrings.upcomingEventDesc(activeLanguage) else AppStrings.pastEventDesc(activeLanguage),
                                style = MaterialTheme.typography.bodySmall.copy(color = SecondaryText),
                                fontSize = 11.sp
                            )
                        }

                        Switch(
                            checked = formState.eventDetails.isUpcoming,
                            onCheckedChange = onEventIsUpcomingChanged,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = SecondaryBlue,
                                uncheckedThumbColor = SecondaryText,
                                uncheckedTrackColor = BorderSubtle
                            )
                        )
                    }
                }
            }
        }

        // 6. Visual Proof Section (CameraX and Photo Picker)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            border = BorderStroke(1.dp, BorderSubtle)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = AppStrings.visualProofTitle(activeLanguage),
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )

                    if (formState.photoUri != null) {
                        IconButton(
                            onClick = onRemovePhoto,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove photo",
                                tint = ErrorRed,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                if (formState.photoUri != null) {
                    // Preview captured image
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(CardBackgroundMuted)
                            .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
                    ) {
                        AsyncImage(
                            model = formState.photoUri,
                            contentDescription = "Visual proof preview",
                            modifier = Modifier.matchParentSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onOpenCameraClicked,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryNavy,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhotoCamera,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = AccentCyan
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AppStrings.retakePhoto(activeLanguage),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, BorderSubtle)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhotoLibrary,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = SecondaryBlue
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AppStrings.pickGallery(activeLanguage),
                                fontSize = 12.sp,
                                color = PrimaryText,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = onOpenCameraClicked,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                                    .testTag("open_camera_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PrimaryNavy,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PhotoCamera,
                                    contentDescription = null,
                                    modifier = Modifier.size(17.dp),
                                    tint = AccentCyan
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = AppStrings.takePhoto(activeLanguage),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            OutlinedButton(
                                onClick = {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                                    .testTag("open_gallery_button"),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, BorderSubtle)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PhotoLibrary,
                                    contentDescription = null,
                                    modifier = Modifier.size(17.dp),
                                    tint = SecondaryBlue
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = AppStrings.pickGallery(activeLanguage),
                                    fontSize = 13.sp,
                                    color = PrimaryText,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Text(
                            text = AppStrings.visualProofDesc(activeLanguage),
                            style = MaterialTheme.typography.bodySmall.copy(color = SecondaryText),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // 7. Tone Selector
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            border = BorderStroke(1.dp, BorderSubtle)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = AppStrings.toneLabel(activeLanguage),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = SecondaryText,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ReportTone.entries.forEach { tone ->
                        val isSelected = formState.tone == tone
                        Surface(
                            onClick = { onToneChanged(tone) },
                            shape = RoundedCornerShape(50),
                            color = if (isSelected) PrimaryNavy else CardBackgroundMuted,
                            border = BorderStroke(1.dp, if (isSelected) PrimaryNavy else BorderSubtle),
                            modifier = Modifier.testTag("tone_chip_${tone.name.lowercase()}")
                        ) {
                            Text(
                                text = tone.label(activeLanguage),
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else SecondaryText,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        // Primary Action: Generate Multi-Platform Posts
        val isLoading = generationState is GenerationUiState.Loading

        Button(
            onClick = onGenerateClicked,
            enabled = !isLoading && formState.claim.isNotBlank() && formState.groundReality.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("generate_posts_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryNavy,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = AccentCyan,
                    strokeWidth = 2.5.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = AppStrings.generatingPosts(activeLanguage),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_postcraft_speech_p),
                    contentDescription = "PostCraft",
                    modifier = Modifier
                        .size(22.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = AppStrings.generatePosts(activeLanguage),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        AnimatedVisibility(visible = generationState is GenerationUiState.Error) {
            val errorMsg = (generationState as? GenerationUiState.Error)?.message.orEmpty()
            Text(
                text = errorMsg,
                color = ErrorRed,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}
