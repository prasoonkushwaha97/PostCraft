package com.example.ui.components

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import coil.compose.AsyncImage
import com.example.auth.AuthManager
import com.example.ui.theme.ForestTeal
import com.example.ui.theme.SafetyOrange
import com.example.ui.theme.SafetyOrangeDark
import com.example.ui.theme.Zinc100
import com.example.ui.theme.Zinc200
import com.example.ui.theme.Zinc400
import com.example.ui.theme.Zinc700
import com.example.ui.theme.Zinc800
import com.example.ui.theme.Zinc900
import com.google.firebase.auth.FirebaseUser

@Composable
fun AuthBar(
    currentUser: FirebaseUser?,
    authManager: AuthManager,
    onAuthSuccess: (FirebaseUser) -> Unit,
    onSignOut: () -> Unit,
    onOpenHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Subtle dark glassmorphism card (bg-zinc-900/90 border border-zinc-800 rounded-2xl p-4)
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("auth_bar_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Zinc900.copy(alpha = 0.92f)
        ),
        border = BorderStroke(1.dp, Zinc800)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Organization Branding & User Info
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    if (currentUser?.photoUrl != null) {
                        AsyncImage(
                            model = currentUser.photoUrl,
                            contentDescription = "User Avatar",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(if (currentUser != null) ForestTeal else SafetyOrangeDark),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Profile Icon",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        if (currentUser != null) {
                            Text(
                                text = currentUser.displayName?.ifBlank { "Citizen Journalist" } ?: "Citizen Journalist",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Zinc100
                                ),
                                maxLines = 1
                            )
                            Text(
                                text = currentUser.email.orEmpty().ifBlank { "Verified Reporter" },
                                style = MaterialTheme.typography.bodySmall.copy(color = Zinc400),
                                maxLines = 1
                            )
                        } else {
                            Text(
                                text = "साथी फाउंडेशन | ग्राम आवाज़",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = SafetyOrange
                                )
                            )
                            Text(
                                text = "Citizen Journalism & Ground Reality",
                                style = MaterialTheme.typography.bodySmall.copy(color = Zinc400)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Right: Actions (Sleek Horizontal Sign-In Button or History + Sign-out)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (currentUser != null) {
                        IconButton(
                            onClick = onOpenHistory,
                            modifier = Modifier.testTag("history_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = "View Saved Reports",
                                tint = ForestTeal
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                authManager.signOut(coroutineScope, onSignOut)
                            },
                            modifier = Modifier.testTag("sign_out_button"),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Zinc700),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = "Sign out",
                                modifier = Modifier.size(15.dp),
                                tint = Zinc400
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Logout",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Zinc200
                            )
                        }
                    } else {
                        // Sleek horizontal button: px-4 py-2 rounded-xl bg-orange-600 hover:bg-orange-500 text-white font-medium text-xs flex items-center gap-2, whitespace-nowrap
                        Button(
                            onClick = {
                                isLoading = true
                                errorMessage = null
                                (context as? Activity)?.let { activity ->
                                    authManager.signInWithGoogle(
                                        activity = activity,
                                        scope = coroutineScope,
                                        onAuthSuccess = { user ->
                                            isLoading = false
                                            onAuthSuccess(user)
                                        },
                                        onAuthError = { err ->
                                            isLoading = false
                                            errorMessage = err
                                        },
                                        onCancelled = {
                                            isLoading = false
                                        }
                                    )
                                }
                            },
                            enabled = !isLoading,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SafetyOrangeDark,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                            modifier = Modifier.testTag("google_sign_in_button")
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Signing In...",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Login,
                                    contentDescription = "Sign in",
                                    modifier = Modifier.size(16.dp),
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Sign In with Google",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(visible = errorMessage != null) {
                Text(
                    text = errorMessage.orEmpty(),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
