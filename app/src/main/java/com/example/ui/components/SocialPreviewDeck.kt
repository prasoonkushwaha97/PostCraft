package com.example.ui.components

import android.widget.Toast
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
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.GeneratedPostBundle
import com.example.data.model.LanguageMode
import com.example.data.model.SocialPlatform
import com.example.ui.theme.AccentCyan
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
import com.example.ui.theme.WarningAmber
import com.example.ui.theme.WarningAmberSubtle
import com.example.ui.util.AppStrings
import com.example.util.ShareUtil

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SocialPreviewDeck(
    bundle: GeneratedPostBundle?,
    activePlatform: SocialPlatform,
    activeLanguage: LanguageMode,
    editableDraftText: String,
    onPlatformSelected: (SocialPlatform) -> Unit,
    onLanguageSelected: (LanguageMode) -> Unit,
    onDraftTextChanged: (String) -> Unit,
    onOpenExportCardDialog: () -> Unit,
    onSaveToCloudClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val clipboardManager = LocalClipboardManager.current
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("social_preview_deck"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section Header with Design 2: Speech-bubble dynamic 'P' icon
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, BorderSubtle, RoundedCornerShape(10.dp))
                        .background(CardBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_postcraft_speech_p),
                        contentDescription = "PostCraft Speech Icon",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }

                Column {
                    Text(
                        text = AppStrings.socialDeckTitle(activeLanguage),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )
                    Text(
                        text = AppStrings.socialDeckSubtitle(activeLanguage),
                        style = MaterialTheme.typography.bodySmall.copy(color = SecondaryText)
                    )
                }
            }

            // Language Pill Switcher
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = CardBackgroundMuted,
                border = BorderStroke(1.dp, BorderSubtle)
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
                                .background(if (isSelected) PrimaryNavy else Color.Transparent)
                                .clickable { onLanguageSelected(lang) }
                                .padding(horizontal = 9.dp, vertical = 4.dp)
                                .testTag("lang_toggle_${lang.name.lowercase()}")
                        ) {
                            Text(
                                text = lang.label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else SecondaryText
                            )
                        }
                    }
                }
            }
        }

        // Platform Switcher: 4 Universal Social Media & Media Formats
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = CardBackground,
            border = BorderStroke(1.dp, BorderSubtle),
            shadowElevation = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("platform_tab_row")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                SocialPlatform.entries.forEach { platform ->
                    val isSelected = activePlatform == platform

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) PrimaryNavy else Color.Transparent)
                            .clickable { onPlatformSelected(platform) }
                            .padding(vertical = 9.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = platform.displayName,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else SecondaryText,
                                maxLines = 1
                            )
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(4.dp)
                                        .clip(CircleShape)
                                        .background(AccentCyan)
                                )
                            }
                        }
                    }
                }
            }
        }

        if (bundle == null) {
            // Empty State
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, BorderSubtle)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(CardBackgroundMuted)
                            .border(1.dp, BorderSubtle, RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_postcraft_speech_p),
                            contentDescription = null,
                            modifier = Modifier.size(36.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = AppStrings.noPostsYet(activeLanguage),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = AppStrings.fillDetailsNotice(activeLanguage),
                        style = MaterialTheme.typography.bodySmall.copy(color = SecondaryText),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            // Event badge if present
            if (bundle.eventDetails.hasEvent) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = CardBackgroundMuted,
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Event,
                            contentDescription = null,
                            tint = SecondaryBlue,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            val eventLabel = if (activeLanguage == LanguageMode.HINDI) "संबंधित आयोजन:" else "Associated Event:"
                            val eventTitle = bundle.eventDetails.title.ifBlank { bundle.eventDetails.eventType.label(activeLanguage) }
                            Text(
                                text = "$eventLabel $eventTitle",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryNavy
                                )
                            )
                            val meta = listOf(bundle.eventDetails.date, bundle.eventDetails.venue)
                                .filter { it.isNotBlank() }
                                .joinToString(" | ")
                            if (meta.isNotBlank()) {
                                Text(
                                    text = meta,
                                    style = MaterialTheme.typography.bodySmall.copy(color = SecondaryText),
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }

            // Google Search Grounded citations if present
            if (bundle.searchCitations.isNotEmpty()) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SuccessGreenSubtle,
                    border = BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Link,
                                contentDescription = null,
                                tint = SuccessGreen,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AppStrings.verifiedSchemeTitle(activeLanguage),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = SuccessGreen
                                )
                            )
                        }

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(top = 6.dp)
                        ) {
                            bundle.searchCitations.forEach { citation ->
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

            // Gemini AI Analysis & Targeted Suggestions
            if (bundle.visualProofSummary.isNotBlank() || bundle.suggestedHashtags.isNotEmpty() || bundle.alternativeTones.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackgroundMuted),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    border = BorderStroke(1.dp, BorderSubtle)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = SecondaryBlue,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AppStrings.aiAnalysisTitle(activeLanguage),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryNavy
                                )
                            )
                        }

                        if (bundle.visualProofSummary.isNotBlank()) {
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = SecondaryText,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = bundle.visualProofSummary,
                                    style = MaterialTheme.typography.bodySmall.copy(color = PrimaryText),
                                    lineHeight = 16.sp
                                )
                            }
                        }

                        if (bundle.suggestedHashtags.isNotEmpty()) {
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                bundle.suggestedHashtags.forEach { tag ->
                                    Surface(
                                        onClick = {
                                            val current = editableDraftText
                                            if (!current.contains(tag)) {
                                                onDraftTextChanged("$current\n$tag")
                                            }
                                        },
                                        shape = RoundedCornerShape(50),
                                        color = CardBackground,
                                        border = BorderStroke(1.dp, BorderSubtle)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Tag,
                                                contentDescription = null,
                                                tint = SecondaryBlue,
                                                modifier = Modifier.size(10.dp)
                                            )
                                            Text(
                                                text = tag,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = SecondaryBlue
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Platform Draft Header & Counter
            val charCount = editableDraftText.length
            val charLimit = activePlatform.charLimit

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = AppStrings.draftEditable(activeLanguage, activePlatform.displayName),
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = PrimaryNavy
                    )
                )

                if (charLimit != null) {
                    val isOverLimit = charCount > charLimit
                    val isNearLimit = charCount >= (charLimit - 25) && !isOverLimit

                    Surface(
                        shape = RoundedCornerShape(50),
                        color = when {
                            isOverLimit -> ErrorRedSubtle
                            isNearLimit -> WarningAmberSubtle
                            else -> CardBackgroundMuted
                        },
                        border = BorderStroke(
                            1.dp,
                            when {
                                isOverLimit -> ErrorRed
                                isNearLimit -> WarningAmber
                                else -> BorderSubtle
                            }
                        )
                    ) {
                        Text(
                            text = "$charCount / $charLimit",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = when {
                                isOverLimit -> ErrorRed
                                isNearLimit -> WarningAmber
                                else -> SecondaryText
                            },
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                } else {
                    Text(
                        text = "$charCount characters",
                        style = MaterialTheme.typography.labelSmall.copy(color = SecondaryText)
                    )
                }
            }

            // Editable Draft Text Area (Clean White Newsroom Canvas)
            OutlinedTextField(
                value = editableDraftText,
                onValueChange = onDraftTextChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(290.dp)
                    .testTag("post_draft_editor"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CardBackground,
                    unfocusedContainerColor = CardBackgroundMuted,
                    focusedBorderColor = SecondaryBlue,
                    unfocusedBorderColor = BorderSubtle,
                    focusedTextColor = PrimaryText,
                    unfocusedTextColor = PrimaryText,
                    cursorColor = SecondaryBlue,
                    focusedPlaceholderColor = PlaceholderText,
                    unfocusedPlaceholderColor = PlaceholderText
                )
            )

            // Primary Actions: Copy, Native ShareSheet, Visual Card, Save Profile
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(editableDraftText))
                            Toast.makeText(context, "Draft text copied!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("copy_post_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryNavy,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = null,
                            modifier = Modifier.size(17.dp),
                            tint = AccentCyan
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = AppStrings.copyText(activeLanguage),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Button(
                        onClick = {
                            ShareUtil.shareText(
                                context = context,
                                text = editableDraftText,
                                subject = "${activePlatform.displayName} - PostCraft Report"
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("share_native_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SecondaryBlue,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            modifier = Modifier.size(17.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = AppStrings.shareViaApp(activeLanguage),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onOpenExportCardDialog,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("export_graphic_card_button"),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, BorderSubtle)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = null,
                            modifier = Modifier.size(17.dp),
                            tint = PrimaryNavy
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = AppStrings.graphicCard(activeLanguage),
                            fontSize = 13.sp,
                            color = PrimaryNavy,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    OutlinedButton(
                        onClick = onSaveToCloudClicked,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("save_to_cloud_button"),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, BorderSubtle)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudUpload,
                            contentDescription = null,
                            modifier = Modifier.size(17.dp),
                            tint = PrimaryNavy
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = AppStrings.saveToProfile(activeLanguage),
                            fontSize = 13.sp,
                            color = PrimaryNavy,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
