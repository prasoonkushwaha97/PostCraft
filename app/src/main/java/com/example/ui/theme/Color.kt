package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// ==============================================================================
// Modern Newsroom Colour System - PostCraft
// ==============================================================================
val PrimaryNavy = Color(0xFF10243A)        // Sidebar, navigation, primary headings, key CTAs
val SecondaryBlue = Color(0xFF176B87)      // Secondary actions, sub-headings, department badges
val AccentCyan = Color(0xFF20C5D8)         // Active states, AI indicators, selective highlights
val AccentCyanDark = Color(0xFF0EA5B6)     // High-contrast cyan on light backgrounds
val AccentCyanSubtle = Color(0xFFE0F7FA)   // Very light cyan tint for AI analysis backgrounds

val MainBackground = Color(0xFFF4F7FA)     // Light newsroom canvas background
val CardBackground = Color(0xFFFFFFFF)     // Clean white card background
val CardBackgroundMuted = Color(0xFFF8FAFC)// Subtle slate tint for nested containers
val PrimaryText = Color(0xFF172B3A)        // High-contrast primary dark typography
val SecondaryText = Color(0xFF64748B)      // Secondary supporting metadata and subtitles
val PlaceholderText = Color(0xFF94A3B8)    // Placeholder text in input fields

val BorderSubtle = Color(0xFFE2E8F0)       // Standard crisp card and component borders
val BorderHover = Color(0xFFCBD5E1)        // Interactive border state
val BorderActive = Color(0xFF176B87)       // Focused / selected element border

val SuccessGreen = Color(0xFF16845B)       // Verified sources, resolved states, confirmations
val SuccessGreenSubtle = Color(0xFFECFDF5) // Soft green container background
val WarningAmber = Color(0xFFD99A24)       // Pending review, near character limits
val WarningAmberSubtle = Color(0xFFFFFBEB) // Soft amber container background
val ErrorRed = Color(0xFFD64545)           // Field reality alerts, errors, deletion actions
val ErrorRedSubtle = Color(0xFFFEF2F2)     // Soft red container background

// Platform Specific Social Brand Tokens (Newsroom adjusted)
val TwitterBlue = Color(0xFF1D9BF0)
val InstagramPurple = Color(0xFFE1306C)
val LinkedInBlue = Color(0xFF0A66C2)
val FacebookBlue = Color(0xFF1877F2)

// ==============================================================================
// Compatibility Mappings for Existing Components
// ==============================================================================
val Zinc950 = MainBackground
val Zinc900 = CardBackground
val Zinc850 = CardBackgroundMuted
val Zinc800 = BorderSubtle
val Zinc700 = BorderHover
val Zinc600 = SecondaryText
val Zinc400 = SecondaryText
val Zinc300 = SecondaryText
val Zinc200 = PrimaryText
val Zinc100 = PrimaryText
val Zinc50 = CardBackground

val SafetyOrange = PrimaryNavy
val SafetyOrangeDark = SecondaryBlue
val SafetyOrangeLight = AccentCyan
val SafetyOrangeContainer = AccentCyanSubtle

val ForestTeal = SuccessGreen
val ForestTealDark = Color(0xFF0F766E)
val ForestTealLight = AccentCyan
val ForestTealContainer = SuccessGreenSubtle

val ClaimBlue = SecondaryBlue
val ClaimBlueDark = PrimaryNavy
val ClaimBlueContainer = Color(0xFFE0F2FE)

val RealityRed = ErrorRed
val RealityRedDark = Color(0xFFB91C1C)
val RealityRedContainer = ErrorRedSubtle

val CivicSaffron = PrimaryNavy
val CivicSaffronDark = SecondaryBlue
val CivicSaffronLight = AccentCyan
val CivicSaffronContainer = AccentCyanSubtle
val OnCivicSaffronContainer = PrimaryNavy
val ForestTealContainerOld = SuccessGreenSubtle
val OnForestTealContainer = SuccessGreen
val ClaimBlueContainerOld = ClaimBlueContainer
val RealityRedContainerOld = RealityRedContainer
