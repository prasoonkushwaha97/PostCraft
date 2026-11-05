package com.example.ui.components

import android.widget.Toast
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GeneratedPostBundle
import com.example.data.model.LanguageMode
import com.example.data.model.SocialPlatform
import com.example.ui.theme.FacebookBlue
import com.example.ui.theme.ForestTeal
import com.example.ui.theme.InstagramPurple
import com.example.ui.theme.LinkedInBlue
import com.example.ui.theme.RealityRed
import com.example.ui.theme.SafetyOrange
import com.example.ui.theme.SafetyOrangeDark
import com.example.ui.theme.TwitterBlue
import com.example.ui.theme.Zinc100
import com.example.ui.theme.Zinc200
import com.example.ui.theme.Zinc300
import com.example.ui.theme.Zinc400
import com.example.ui.theme.Zinc600
import com.example.ui.theme.Zinc700
import com.example.ui.theme.Zinc800
import com.example.ui.theme.Zinc900
import com.example.ui.theme.Zinc950
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
        // Section Header & Language Switcher
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Multi-Platform Social Deck",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = SafetyOrange
                    )
                )
                Text(
                    text = "Tailored for platform character bounds and audiences.",
                    style = MaterialTheme.typography.bodySmall.copy(color = Zinc400)
                )
            }

            // Language Switcher (Segmented pill container)
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Zinc900,
                border = BorderStroke(1.dp, Zinc800)
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
                                .background(if (isSelected) SafetyOrangeDark else Color.Transparent)
                                .clickable { onLanguageSelected(lang) }
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                                .testTag("lang_toggle_${lang.name.lowercase()}")
                        ) {
                            Text(
                                text = lang.label,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else Zinc400
                            )
                        }
                    }
                }
            }
        }

        // Platform Switcher (Modern Segmented Control / Pill Switcher container)
        // bg-zinc-900 border border-zinc-800 rounded-xl p-1 flex gap-1
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = Zinc900,
            border = BorderStroke(1.dp, Zinc800),
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
                    val brandColor = when (platform) {
                        SocialPlatform.TWITTER -> TwitterBlue
                        SocialPlatform.INSTAGRAM -> InstagramPurple
                        SocialPlatform.LINKEDIN -> LinkedInBlue
                        SocialPlatform.FACEBOOK -> FacebookBlue
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) brandColor.copy(alpha = 0.25f) else Color.Transparent)
                            .border(
                                width = if (isSelected) 1.dp else 0.dp,
                                color = if (isSelected) brandColor else Color.Transparent,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { onPlatformSelected(platform) }
                            .padding(vertical = 9.dp)
                            .testTag("tab_${platform.name.lowercase()}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = platform.displayName,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else Zinc400,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        if (bundle == null) {
            // Empty State
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Zinc900),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Zinc800)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = SafetyOrange,
                        modifier = Modifier.size(44.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No Posts Generated Yet",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Zinc100
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Fill in the village issue details on the left and tap 'Generate Multi-Platform Posts'.",
                        style = MaterialTheme.typography.bodySmall.copy(color = Zinc400),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            // Event badge if present
            if (bundle.eventDetails.hasEvent) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Zinc900,
                    border = BorderStroke(1.dp, SafetyOrange.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Event,
                            contentDescription = null,
                            tint = SafetyOrange,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "संबंधित आयोजन: ${bundle.eventDetails.title.ifBlank { bundle.eventDetails.eventType.labelHi }}",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = SafetyOrange
                                )
                            )
                            val meta = listOf(bundle.eventDetails.date, bundle.eventDetails.venue)
                                .filter { it.isNotBlank() }
                                .joinToString(" | ")
                            if (meta.isNotBlank()) {
                                Text(
                                    text = meta,
                                    style = MaterialTheme.typography.bodySmall.copy(color = Zinc400),
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }

            // Grounded citations if present
            if (bundle.searchCitations.isNotEmpty()) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Zinc900,
                    border = BorderStroke(1.dp, ForestTeal.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Link,
                                contentDescription = null,
                                tint = ForestTeal,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Grounded Scheme Sources (Google Search)",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = ForestTeal
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
                                    color = Zinc950,
                                    border = BorderStroke(1.dp, Zinc700)
                                ) {
                                    Text(
                                        text = citation.title.take(30),
                                        fontSize = 10.sp,
                                        color = Zinc300,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // AI Analysis & Targeted Suggestions
            if (bundle.visualProofSummary.isNotBlank() || bundle.suggestedHashtags.isNotEmpty() || bundle.alternativeTones.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Zinc900),
                    border = BorderStroke(1.dp, Zinc800)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = SafetyOrange,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Gemini AI Ground Analysis & Suggestions",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = SafetyOrange
                                )
                            )
                        }

                        if (bundle.visualProofSummary.isNotBlank()) {
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = Zinc400,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = bundle.visualProofSummary,
                                    style = MaterialTheme.typography.bodySmall.copy(color = Zinc300),
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
                                        color = Zinc950,
                                        border = BorderStroke(1.dp, Zinc700)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Tag,
                                                contentDescription = null,
                                                tint = SafetyOrange,
                                                modifier = Modifier.size(10.dp)
                                            )
                                            Text(
                                                text = tag,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = Zinc200
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
                    text = "${activePlatform.displayName} Draft (Editable)",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Zinc100
                    )
                )

                if (charLimit != null) {
                    val isOverLimit = charCount > charLimit
                    val isNearLimit = charCount >= (charLimit - 20) && !isOverLimit

                    Surface(
                        shape = RoundedCornerShape(50),
                        color = when {
                            isOverLimit -> RealityRed.copy(alpha = 0.2f)
                            isNearLimit -> SafetyOrange.copy(alpha = 0.2f)
                            else -> Zinc800
                        },
                        border = BorderStroke(
                            1.dp,
                            when {
                                isOverLimit -> RealityRed
                                isNearLimit -> SafetyOrange
                                else -> Zinc700
                            }
                        )
                    ) {
                        Text(
                            text = "$charCount / $charLimit chars",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = when {
                                isOverLimit -> RealityRed
                                isNearLimit -> SafetyOrange
                                else -> Zinc300
                            },
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                } else {
                    Text(
                        text = "$charCount characters",
                        style = MaterialTheme.typography.labelSmall.copy(color = Zinc400)
                    )
                }
            }

            // Editable Draft Text Area
            OutlinedTextField(
                value = editableDraftText,
                onValueChange = onDraftTextChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .testTag("post_draft_editor"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Zinc900,
                    unfocusedContainerColor = Zinc900,
                    focusedBorderColor = SafetyOrange,
                    unfocusedBorderColor = Zinc800,
                    focusedTextColor = Zinc100,
                    unfocusedTextColor = Zinc100,
                    cursorColor = SafetyOrange
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
                            Toast.makeText(context, "Post text copied to clipboard!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("copy_post_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SafetyOrangeDark,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = null,
                            modifier = Modifier.size(17.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Copy Text", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Button(
                        onClick = {
                            ShareUtil.shareText(
                                context = context,
                                text = editableDraftText,
                                subject = "${activePlatform.displayName} - GramAwaz Report"
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("share_native_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ForestTeal,
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
                        Text("Share via App", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
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
                        border = BorderStroke(1.dp, Zinc700)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = null,
                            modifier = Modifier.size(17.dp),
                            tint = Zinc200
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Graphic Card", fontSize = 13.sp, color = Zinc200, fontWeight = FontWeight.SemiBold)
                    }

                    OutlinedButton(
                        onClick = onSaveToCloudClicked,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("save_to_cloud_button"),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Zinc700)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudUpload,
                            contentDescription = null,
                            modifier = Modifier.size(17.dp),
                            tint = Zinc200
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Save to Profile", fontSize = 13.sp, color = Zinc200, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}
