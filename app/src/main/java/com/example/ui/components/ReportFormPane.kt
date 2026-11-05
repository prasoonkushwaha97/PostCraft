package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.CampaignTheme
import com.example.data.model.EventType
import com.example.data.model.ReportTone
import com.example.ui.theme.ClaimBlue
import com.example.ui.theme.ForestTeal
import com.example.ui.theme.RealityRed
import com.example.ui.theme.SafetyOrange
import com.example.ui.theme.SafetyOrangeDark
import com.example.ui.theme.Zinc100
import com.example.ui.theme.Zinc200
import com.example.ui.theme.Zinc300
import com.example.ui.theme.Zinc400
import com.example.ui.theme.Zinc600
import com.example.ui.theme.Zinc700
import com.example.ui.theme.Zinc800
import com.example.ui.theme.Zinc900
import com.example.ui.theme.Zinc950
import com.example.ui.viewmodel.GenerationUiState
import com.example.ui.viewmodel.ReportFormState
import com.example.ui.viewmodel.SearchGroundingUiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ReportFormPane(
    formState: ReportFormState,
    generationState: GenerationUiState,
    searchGroundingState: SearchGroundingUiState = SearchGroundingUiState.Idle,
    onThemeChanged: (CampaignTheme) -> Unit,
    onVillageChanged: (String) -> Unit,
    onTehsilChanged: (String) -> Unit,
    onDistrictChanged: (String) -> Unit,
    onStateChanged: (String) -> Unit,
    onClaimChanged: (String) -> Unit,
    onRealityChanged: (String) -> Unit,
    onToneChanged: (ReportTone) -> Unit,
    onEventTypeChanged: (EventType) -> Unit = {},
    onEventTitleChanged: (String) -> Unit = {},
    onEventDateChanged: (String) -> Unit = {},
    onEventVenueChanged: (String) -> Unit = {},
    onEventIsUpcomingChanged: (Boolean) -> Unit = {},
    onVerifySchemeClicked: () -> Unit = {},
    onPhotoSelected: (Uri?) -> Unit,
    onRemovePhoto: () -> Unit,
    onOpenCameraClicked: () -> Unit,
    onLoadPreset: (Int) -> Unit,
    onGenerateClicked: () -> Unit,
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

    // Consistent input text field colors avoiding cut-through notch issue
    val customTextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Zinc900.copy(alpha = 0.85f),
        unfocusedContainerColor = Zinc900.copy(alpha = 0.85f),
        focusedBorderColor = SafetyOrange,
        unfocusedBorderColor = Zinc800,
        focusedTextColor = Zinc100,
        unfocusedTextColor = Zinc100,
        focusedPlaceholderColor = Zinc600,
        unfocusedPlaceholderColor = Zinc600,
        cursorColor = SafetyOrange
    )

    val claimTextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Zinc900.copy(alpha = 0.85f),
        unfocusedContainerColor = Zinc900.copy(alpha = 0.85f),
        focusedBorderColor = ClaimBlue,
        unfocusedBorderColor = Zinc800,
        focusedTextColor = Zinc100,
        unfocusedTextColor = Zinc100,
        focusedPlaceholderColor = Zinc600,
        unfocusedPlaceholderColor = Zinc600,
        cursorColor = ClaimBlue
    )

    val realityTextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Zinc900.copy(alpha = 0.85f),
        unfocusedContainerColor = Zinc900.copy(alpha = 0.85f),
        focusedBorderColor = RealityRed,
        unfocusedBorderColor = Zinc800,
        focusedTextColor = Zinc100,
        unfocusedTextColor = Zinc100,
        focusedPlaceholderColor = Zinc600,
        unfocusedPlaceholderColor = Zinc600,
        cursorColor = RealityRed
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
                text = "Field Intake & Ground Reality",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = SafetyOrange
                )
            )
            Text(
                text = "Log official promises vs verified grassroots reality to auto-generate platform-tailored posts.",
                style = MaterialTheme.typography.bodySmall.copy(color = Zinc400),
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Preset Chips with high contrast
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.FlashOn,
                    contentDescription = null,
                    modifier = Modifier.size(15.dp),
                    tint = SafetyOrange
                )
                Text(
                    text = "Quick Presets:",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Zinc400,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            // Quick Preset Pills (bg-zinc-900 border border-zinc-700/80 text-zinc-200 text-xs px-3 py-1.5 rounded-full)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 6.dp)
            ) {
                Surface(
                    onClick = { onLoadPreset(0) },
                    shape = RoundedCornerShape(50),
                    color = Zinc900,
                    border = BorderStroke(1.dp, Zinc700.copy(alpha = 0.85f)),
                    modifier = Modifier.testTag("preset_jal_jeevan")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("🚰", fontSize = 12.sp)
                        Text(
                            text = "Chhamrachh (Jal Jeevan)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Zinc200
                        )
                    }
                }

                Surface(
                    onClick = { onLoadPreset(1) },
                    shape = RoundedCornerShape(50),
                    color = Zinc900,
                    border = BorderStroke(1.dp, Zinc700.copy(alpha = 0.85f)),
                    modifier = Modifier.testTag("preset_road")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("🛣️", fontSize = 12.sp)
                        Text(
                            text = "Bargawan (PMGSY Road)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Zinc200
                        )
                    }
                }

                Surface(
                    onClick = { onLoadPreset(2) },
                    shape = RoundedCornerShape(50),
                    color = Zinc900,
                    border = BorderStroke(1.dp, Zinc700.copy(alpha = 0.85f)),
                    modifier = Modifier.testTag("preset_school")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("🏫", fontSize = 12.sp)
                        Text(
                            text = "Katai (Primary School)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Zinc200
                        )
                    }
                }
            }
        }

        // 1. Campaign Theme Dropdown Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Zinc900),
            border = BorderStroke(1.dp, Zinc800)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Campaign Theme (अभियान का विषय)",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Zinc400,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Zinc950)
                        .border(1.dp, Zinc800, RoundedCornerShape(12.dp))
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
                            text = formState.theme.labelHi,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = SafetyOrange
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Expand theme dropdown",
                            tint = Zinc400
                        )
                    }

                    DropdownMenu(
                        expanded = isThemeDropdownOpen,
                        onDismissRequest = { isThemeDropdownOpen = false },
                        modifier = Modifier.background(Zinc900)
                    ) {
                        CampaignTheme.entries.forEach { theme ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(text = theme.labelHi, fontWeight = FontWeight.SemiBold, color = Zinc100)
                                        Text(
                                            text = theme.labelEn,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Zinc400
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

        // 2. Village / Administrative Coordinates (Clean 2-Column Grid)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Zinc900),
            border = BorderStroke(1.dp, Zinc800)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = SafetyOrange,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Administrative Coordinates (स्थान विवरण)",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Zinc100
                        )
                    )
                }

                // Row 1: Village & Tehsil
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Village / गांव",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Zinc400,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = formState.village,
                            onValueChange = onVillageChanged,
                            placeholder = { Text("Chhamrachh / छमरछ") },
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
                            text = "Tehsil / ब्लॉक",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Zinc400,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = formState.tehsil,
                            onValueChange = onTehsilChanged,
                            placeholder = { Text("Sarai / सरई") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_tehsil"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = customTextFieldColors
                        )
                    }
                }

                // Row 2: District & State
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "District / ज़िला",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Zinc400,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = formState.district,
                            onValueChange = onDistrictChanged,
                            placeholder = { Text("Singrauli / सिंगरौली") },
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
                            text = "State / राज्य",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Zinc400,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = formState.state,
                            onValueChange = onStateChanged,
                            placeholder = { Text("Madhya Pradesh") },
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

        // 3. Official Claim vs Ground Reality (Clean inputs without broken fieldset notch)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Zinc900),
            border = BorderStroke(1.dp, Zinc800)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Official Claim Section
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(bottom = 6.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = ClaimBlue
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
                                    text = "सरकारी दावा (Official Claim / Paper Scheme)",
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
                        placeholder = { Text("योजना या प्रशासन का क्या दावा है? (जैसे नल से 100% जल आपूर्ति)") },
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
                            color = RealityRed
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
                                    text = "ज़मीनी हकीकत (Ground Reality Fact)",
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
                        placeholder = { Text("धरातल पर वास्तविक स्थिति क्या है? (जैसे नल सूखे हैं, pipeline टूटी है, पानी नहीं आ रहा)") },
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
                OutlinedButton(
                    onClick = onVerifySchemeClicked,
                    enabled = !isSearching && formState.claim.isNotBlank(),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, ForestTeal),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = ForestTeal
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("verify_scheme_search_button")
                ) {
                    if (isSearching) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = ForestTeal,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Verifying via Google Search...", fontSize = 12.sp, color = ForestTeal)
                    } else {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = ForestTeal
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Verify Official Scheme via Google Search",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Display Google Search Grounded facts and clickable source links
                if (searchGroundingState is SearchGroundingUiState.Success) {
                    val searchResult = searchGroundingState as SearchGroundingUiState.Success
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Zinc950,
                        border = BorderStroke(1.dp, ForestTeal.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = ForestTeal,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Verified Scheme Facts (Google Grounded)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ForestTeal
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = searchResult.factualSummary,
                                fontSize = 12.sp,
                                color = Zinc200,
                                lineHeight = 16.sp
                            )

                            if (searchResult.citations.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Official Web Sources:",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Zinc400
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
                                            color = Zinc900,
                                            border = BorderStroke(1.dp, Zinc700)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Link,
                                                    contentDescription = null,
                                                    tint = ForestTeal,
                                                    modifier = Modifier.size(12.dp)
                                                )
                                                Text(
                                                    text = citation.title.take(28),
                                                    fontSize = 10.sp,
                                                    color = Zinc300
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
        }

        // 4. Event Related to Post (संबंधित आयोजन / अवसर)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Zinc900),
            border = BorderStroke(1.dp, Zinc800)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Event,
                        contentDescription = null,
                        tint = SafetyOrange,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Related Event / Occasion (संबंधित आयोजन)",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Zinc100
                        )
                    )
                }

                Text(
                    text = "Associate a Gram Sabha, Jan Sunwai, or official meeting to mobilize citizens and demand direct accountability.",
                    style = MaterialTheme.typography.bodySmall.copy(color = Zinc400)
                )

                // Event Type Chips
                Text(
                    text = "Event Type (आयोजन का प्रकार):",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Zinc400,
                        fontWeight = FontWeight.Medium
                    )
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    EventType.entries.forEach { type ->
                        val isSelected = formState.eventDetails.eventType == type
                        Surface(
                            onClick = { onEventTypeChanged(type) },
                            shape = RoundedCornerShape(50),
                            color = if (isSelected) SafetyOrangeDark else Zinc950,
                            border = BorderStroke(1.dp, if (isSelected) SafetyOrange else Zinc800),
                            modifier = Modifier.testTag("event_type_chip_${type.name.lowercase()}")
                        ) {
                            Text(
                                text = type.labelHi,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else Zinc300,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                // If an event type is chosen or title entered
                if (formState.eventDetails.eventType != EventType.NONE) {
                    Column {
                        Text(
                            text = "Event Title (आयोजन का शीर्षक / विषय)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Zinc400,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = formState.eventDetails.title,
                            onValueChange = onEventTitleChanged,
                            placeholder = { Text("जैसे: कलेक्टर जनसुनवाई या विशेष ग्राम सभा बैठक") },
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
                                text = "Date & Time (दिनांक व समय)",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Zinc400,
                                    fontWeight = FontWeight.Medium
                                ),
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                            OutlinedTextField(
                                value = formState.eventDetails.date,
                                onValueChange = onEventDateChanged,
                                placeholder = { Text("मंगलवार 11:00 AM") },
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
                                text = "Venue (स्थान / सभागार)",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Zinc400,
                                    fontWeight = FontWeight.Medium
                                ),
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                            OutlinedTextField(
                                value = formState.eventDetails.venue,
                                onValueChange = onEventVenueChanged,
                                placeholder = { Text("कलेक्ट्रेट / पंचायत भवन") },
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
                                text = if (formState.eventDetails.isUpcoming) "Upcoming Event (आगामी आयोजन)" else "Past Event (बीता आयोजन / निरीक्षण)",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = Zinc100
                                )
                            )
                            Text(
                                text = if (formState.eventDetails.isUpcoming) "Posts will urge villagers to attend & demand accountability." else "Posts will summarize inspection facts & demands raised.",
                                style = MaterialTheme.typography.bodySmall.copy(color = Zinc400)
                            )
                        }

                        Switch(
                            checked = formState.eventDetails.isUpcoming,
                            onCheckedChange = onEventIsUpcomingChanged,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = SafetyOrange,
                                uncheckedThumbColor = Zinc400,
                                uncheckedTrackColor = Zinc800
                            ),
                            modifier = Modifier.testTag("switch_event_upcoming")
                        )
                    }
                }
            }
        }

        // 5. Visual Proof Upload (Photo Picker & CameraX)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Zinc900),
            border = BorderStroke(1.dp, Zinc800)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Visual Proof (तस्वीर / साक्ष्य प्रमाण)",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Zinc400,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (formState.photoUri != null) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, Zinc800, RoundedCornerShape(12.dp))
                        ) {
                            AsyncImage(
                                model = formState.photoUri,
                                contentDescription = "Visual Proof Thumbnail",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.matchParentSize()
                            )

                            IconButton(
                                onClick = onRemovePhoto,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                                    .background(Color.Black.copy(alpha = 0.7f), shape = CircleShape)
                                    .size(32.dp)
                                    .testTag("remove_photo_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove photo",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = onOpenCameraClicked,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("retake_camera_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = SafetyOrange),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PhotoCamera,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Retake Photo", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            }

                            OutlinedButton(
                                onClick = {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("change_gallery_button"),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Zinc700)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PhotoLibrary,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = Zinc200
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Pick Gallery", fontSize = 12.sp, color = Zinc200, fontWeight = FontWeight.Medium)
                            }
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
                                colors = ButtonDefaults.buttonColors(containerColor = SafetyOrange),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PhotoCamera,
                                    contentDescription = null,
                                    modifier = Modifier.size(17.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Take Photo", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
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
                                border = BorderStroke(1.dp, Zinc700)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PhotoLibrary,
                                    contentDescription = null,
                                    modifier = Modifier.size(17.dp),
                                    tint = Zinc200
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Pick Gallery", fontSize = 13.sp, color = Zinc200, fontWeight = FontWeight.SemiBold)
                            }
                        }

                        Text(
                            text = "📸 Capture real ground evidence (broken pipes, potholed roads) for Gemini multimodal proof analysis.",
                            style = MaterialTheme.typography.bodySmall.copy(color = Zinc400),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // 6. Tone Selector
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Zinc900),
            border = BorderStroke(1.dp, Zinc800)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Tone of Report (रिपोर्ट की शैली)",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Zinc400,
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
                            color = if (isSelected) SafetyOrangeDark else Zinc950,
                            border = BorderStroke(1.dp, if (isSelected) SafetyOrange else Zinc800),
                            modifier = Modifier.testTag("tone_chip_${tone.name.lowercase()}")
                        ) {
                            Text(
                                text = tone.labelHi,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else Zinc300,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        // 7. Generate All Posts Action Button
        val isLoading = generationState is GenerationUiState.Loading

        Button(
            onClick = onGenerateClicked,
            enabled = !isLoading && formState.claim.isNotBlank() && formState.groundReality.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("generate_posts_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = SafetyOrange,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = Color.White,
                    strokeWidth = 2.5.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Generating 4 Platform Posts...",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(19.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Generate Multi-Platform Posts (पोस्ट तैयार करें)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        AnimatedVisibility(visible = generationState is GenerationUiState.Error) {
            val errorMsg = (generationState as? GenerationUiState.Error)?.message.orEmpty()
            Text(
                text = errorMsg,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}
