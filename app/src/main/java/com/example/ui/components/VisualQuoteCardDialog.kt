package com.example.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.ForestTeal
import com.example.ui.theme.SafetyOrange
import com.example.ui.theme.Zinc100
import com.example.ui.theme.Zinc200
import com.example.ui.theme.Zinc400
import com.example.ui.theme.Zinc700
import com.example.ui.theme.Zinc800
import com.example.ui.theme.Zinc900
import com.example.ui.theme.Zinc950
import com.example.util.ShareUtil

@Composable
fun VisualQuoteCardDialog(
    village: String,
    district: String,
    claim: String,
    reality: String,
    proofBitmap: Bitmap?,
    captionText: String,
    eventBadge: String = "",
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    // Generate high resolution branded bitmap
    val quoteCardBitmap = remember(village, district, claim, reality, proofBitmap, eventBadge) {
        ShareUtil.createVisualQuoteCard(
            context = context,
            village = village,
            district = district,
            claim = claim,
            reality = reality,
            proofBitmap = proofBitmap,
            eventBadge = eventBadge
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(20.dp))
                .testTag("visual_quote_card_dialog"),
            colors = CardDefaults.cardColors(containerColor = Zinc900),
            border = BorderStroke(1.dp, Zinc800)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Visual Evidence Poster (ग्राफिक कार्ड)",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = SafetyOrange
                            )
                        )
                        Text(
                            text = "Ready to publish on Instagram Stories, X, WhatsApp",
                            style = MaterialTheme.typography.bodySmall.copy(color = Zinc400)
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close dialog",
                            tint = Zinc400
                        )
                    }
                }

                // Card Preview
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Zinc950),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        bitmap = quoteCardBitmap.asImageBitmap(),
                        contentDescription = "Generated Quote Card Preview",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.matchParentSize()
                    )
                }

                // Actions: Download & Share
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            ShareUtil.saveBitmapToDevice(context, quoteCardBitmap)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("download_card_button"),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Zinc700)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = null,
                            modifier = Modifier.size(17.dp),
                            tint = Zinc200
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Save to Gallery", fontSize = 13.sp, color = Zinc200, fontWeight = FontWeight.SemiBold)
                    }

                    Button(
                        onClick = {
                            ShareUtil.shareBitmapWithText(
                                context = context,
                                bitmap = quoteCardBitmap,
                                captionText = captionText,
                                title = "Share Visual Evidence Poster"
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ForestTeal,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("share_card_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            modifier = Modifier.size(17.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Share Graphic", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}
