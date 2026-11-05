# GramAwaz - Saathi Citizen Journalism & Governance Post Generator

GramAwaz is a citizen journalism and rural governance social media post generator Android application built for Saathi Foundation and grassroots field reporters. It empowers community reporters and rural citizens to document ground realities, contrast official government claims with facts, attach photographic proof, and instantly generate platform-tailored social media posts for Twitter/X, Instagram, LinkedIn, and Facebook with dual Hindi/English capabilities, visual quote card generation, and direct Android sharing.

---

### User Review & Critical Decisions

> [!IMPORTANT]
> The following architectural decisions have been aligned based on your selections and will govern the build:

- **Confirmed Decision 1 (AI Generation Engine):** Powered by **Gemini 3.8 Flash** via **Firebase AI Logic** with structured JSON output (`responseSchema`) and resilient timeout handling. It also provides an offline instant-template fallback if network or credentials are unavailable.
- **Confirmed Decision 2 (Language & Tone):** Full **bilingual support** generating both authentic Hindi (Devanagari) and English versions for all 4 platforms simultaneously, with a fluid one-tap language switch (`हिन्दी` / `English`).
- **Confirmed Decision 3 (Sharing & Export):** Full multi-channel export suite including:
  1. Direct native **Android ShareSheet** (`Intent.ACTION_SEND`) to share text and images directly to Twitter, Instagram, WhatsApp, Facebook, or LinkedIn.
  2. High-resolution **Visual Quote & Fact Card Export** (rendering the village photo, "दावा vs हकीकत" badge, location header, and quote into a downloadable/shareable bitmap image).
  3. One-tap **Copy with Character Counters** and platform limit badges (e.g., 280-char limit for X/Twitter).
- **Confirmed Decision 4 (Form & Adaptive UI):** Adaptive two-pane layout for tablets/foldables/landscape (Form on left, live Social Previews on right) and an ergonomic bottom-bar/tabbed navigation on handheld smartphones with seamless Photo Picker (`ActivityResultContracts.PickVisualMedia`).

---

### 1. Overview & Core Concept

- **What It Does:** Enables community workers, citizens, and activists to log administrative details (Village, Tehsil, District, State), select campaign themes (prominently featuring *"ज़मीनी हकीकत (Ground Reality vs Official Claims)"*), input official scheme promises against verified field realities, attach ground photos, and generate publication-ready posts across Twitter/X, Instagram, LinkedIn, and Facebook in one tap.
- **Target Audience:** Grassroots volunteers, Saathi Foundation coordinators, rural journalists, Panchayat members, and citizens advocating for public accountability in water (Jal Jeevan Mission), roads (PMGSY), schools, clinics, and ration distribution.
- **Key Value:** Bridges the gap between village reality and public policy dialogue by converting raw ground notes into compelling, platform-optimized journalism in seconds.

---

### 2. User Experience & Visual Design

#### Visual Identity & Theme
- **Theme Name:** *Gramin Patrakarita (Rural Civic Editorial)*
- **Color Tokens:**
  - *Primary (Civic Ochre/Terracotta):* Deep warm earthen saffron (`#C05621` / `#DD6B20`) symbolizing rural earth, warmth, and grassroots energy.
  - *Secondary (Integrity Emerald/Teal):* Rich forest teal (`#234E52` / `#2C7A7B`) representing public transparency, water, and accountability.
  - *Ground Reality Alert:* Crimson rose accent (`#E53E3E`) for "हकीकत" (Ground Reality) contrasted with Crisp Blue (`#3182CE`) for "दावा" (Official Claim).
  - *Surfaces:* Warm tinted neutral background (`#FDFBF7`) with soft elevated cards (`#FFFFFF`), crisp borders, and subtle elevation shadow.
- **Typography:** Deep Devanagari and Latin legibility using Material 3 dynamic typography, bold display headings, clear contrasting badges, and monospace character counters.

#### Key User Flows & Screens
1. **Reporting & Intake Form (Input Pane):**
   - **Campaign Theme Selector:** Dropdown with *"ज़मीनी हकीकत (Ground Reality vs Official Claims)"* (pre-selected by default), *Rural Development*, *Community Empowerment*, *Education & Youth*, and *Local Achievements*.
   - **Administrative Details:** Village Name (e.g., *Chhamrachh / छमरछ*), Tehsil/Block (*Sarai / सरई*), District/State (*Singrauli / Madhya Pradesh*). Includes quick-fill sample buttons for instant testing.
   - **Field Context & Notes:**
     - *Government Claim / Paper Scheme:* Promises, budget, or scheme target.
     - *Ground Reality (ज़मीनी हकीकत):* Actual ground truth, delays, broken infrastructure.
   - **Visual Proof Upload:** Android Photo Picker button, thumbnail preview with remove/replace action.
   - **Tone Selector Chips:** *Investigative & Direct (खोजी व तार्किक)*, *Constructive Critique (सकारात्मक आलोचना)*, *Storytelling / Ground Report (ज़मीनी दास्तान)*.
   - **"Generate All Posts" Button:** Dynamic floating action bar with loading state and progress feedback.

2. **Multi-Platform Preview & Editorial Deck (Output Pane):**
   - **Platform Switcher Tabs:** Interactive tabs for **Twitter (X)**, **Instagram**, **LinkedIn**, and **Facebook** with official platform brand badges and icons.
   - **Language Toggle:** One-tap switch between **हिन्दी** and **English**.
   - **Inline Post Editor:** Live editable text field allowing field reporters to tweak hooks, handles, or hashtags before posting.
   - **Platform-Specific Metrics:**
     - *Twitter / X:* Real-time character counter showing remaining characters out of 280 with color warnings. Includes targeted authority handle tags (e.g., `@CollectorSingrauli`, `@MoJSDoWRRDGR`).
     - *Instagram:* Carousel/single caption format with bold hook, bulleted contrast, CTA, and structured hashtag group.
     - *LinkedIn:* Policy impact analysis, governance framework, and constructive recommendations.
     - *Facebook:* Community-friendly tone inviting local villagers to comment and tag Sarpanch/representatives.
   - **Interactive Actions on Each Card:**
     - `Copy Post Text` with snackbar feedback.
     - `Share via App` triggering native Android ShareSheet (`Intent.ACTION_SEND`).
     - `Generate Graphic Card` rendering a polished visual quote card with ground photo and facts.

3. **Visual Quote Card Export Modal:**
   - Previews a graphic poster (aspect 1:1 or 4:5) featuring the village photo, Saathi Foundation / GramAwaz badge, village/district tag, "सरकारी दावा" vs "ज़मीनी हकीकत" highlight box, and inquiry quote.
   - Actions to save directly to device or share to WhatsApp/Instagram Stories.

---

### 3. Key Product Decisions & Trade-Offs

- **Decision 1: Dual-Engine Architecture (Gemini AI + Rule-Based Fallback)**
  - *Approach:* Primary generation via Gemini 3.8 Flash using a structured JSON schema. If the device is offline or API fails, an algorithmic template generator immediately synthesizes high-quality bilingual posts so the user is never blocked in the field.
  - *Why:* Field reporters often operate in low-connectivity rural areas; the app must never fail or crash due to network drops.
- **Decision 2: Zero-Permission Photo Picking**
  - *Approach:* Android standard Photo Picker (`PickVisualMedia`).
  - *Why:* Fully compliant with Android privacy and Google Play policy; no broad storage permissions needed.
- **Decision 3: In-Memory / SharedPrefs Persistence for Drafts**
  - *Approach:* Local state preservation of form fields, previous reports, and drafts so reporters don't lose data if the app pauses or switches apps.
- **Decision 4: Compose Adaptive Layout**
  - *Approach:* Uses `BoxWithConstraints`. On screens >= 600dp width (tablets, Chromebooks, foldables), displays dual-pane side-by-side view. On phones, uses a fluid bottom-nav or pager to slide between Report Form and Social Deck.

---

### 4. Technical Architecture & Data Strategy

```
┌────────────────────────────────────────────────────────────────────────┐
│                          MainActivity                                  │
│   (App Check Configuration, Launch Intent Wiring & Edge-to-Edge)       │
└──────────────────────────────────┬─────────────────────────────────────┘
                                   │
                                   ▼
┌────────────────────────────────────────────────────────────────────────┐
│                        GramAwazViewModel                               │
│  - ReportFormState (Theme, Village, Tehsil, Claim, Reality, PhotoUri)  │
│  - GeneratedPostsState (Twitter, Insta, LinkedIn, FB in HI & EN)       │
│  - GenerationStatus (Idle, Generating, Success, Error)                 │
│  - ActivePlatform & ActiveLanguage & EditMode                          │
└───────────────┬────────────────────────────────────────┬───────────────┘
                │                                        │
                ▼                                        ▼
┌──────────────────────────────┐        ┌────────────────────────────────┐
│   FirebaseAiRepository       │        │   TemplatePostGenerator        │
│   (Gemini 3.8 Flash +        │◄──────►│   (Deterministic Offline       │
│    Structured JSON Schema)   │        │    Bilingual Generator)        │
└──────────────────────────────┘        └────────────────────────────────┘
                │
                ▼
┌────────────────────────────────────────────────────────────────────────┐
│                             UI Deck                                    │
│  ┌──────────────────────────────┐    ┌──────────────────────────────┐  │
│  │     ReportFormPane           │    │     SocialPreviewDeck        │  │
│  │  - Theme & Administrative    │    │  - Platform Tabs (X/IG/LI/FB)│  │
│  │  - Claim vs Reality Inputs   │    │  - Hindi / English Switch    │  │
│  │  - Photo Picker & Preview    │    │  - Inline Draft Editor       │  │
│  │  - Tone Selection            │    │  - ShareSheet / Copy / Export│  │
│  └──────────────────────────────┘    └──────────────────────────────┘  │
│  ┌──────────────────────────────────────────────────────────────────┐  │
│  │               VisualQuoteCardRenderer & Exporter                 │  │
│  └──────────────────────────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────────────────────────┘
```

#### Core Data Entities

```kotlin
data class AdministrativeDetails(
    val village: String = "",
    val tehsil: String = "",
    val district: String = "",
    val state: String = ""
)

enum class CampaignTheme(val labelHi: String, val labelEn: String) {
    GROUND_REALITY("ज़मीनी हकीकत (Ground Reality vs Official Claims)", "Ground Reality vs Official Claims"),
    RURAL_DEVELOPMENT("ग्रामीण विकास (Rural Development)", "Rural Development"),
    COMMUNITY_EMPOWERMENT("सामुदायिक सशक्तिकरण (Community Empowerment)", "Community Empowerment"),
    EDUCATION_YOUTH("शिक्षा एवं युवा (Education & Youth)", "Education & Youth"),
    LOCAL_ACHIEVEMENTS("स्थानीय उपलब्धियाँ (Local Achievements)", "Local Achievements")
}

enum class ReportTone(val labelHi: String, val labelEn: String) {
    INVESTIGATIVE("खोजी व तार्किक", "Investigative & Direct"),
    CONSTRUCTIVE("सकारात्मक आलोचना", "Constructive Critique"),
    STORYTELLING("ज़मीनी दास्तान", "Storytelling / Ground Report")
}

data class PlatformPost(
    val hindiText: String,
    val englishText: String,
    val handlesToTag: List<String> = emptyList(),
    val hashtags: List<String> = emptyList(),
    val characterCount: Int = 0
)

data class GeneratedSocialBundle(
    val twitter: PlatformPost,
    val instagram: PlatformPost,
    val linkedIn: PlatformPost,
    val facebook: PlatformPost
)
```

#### Firebase AI Setup & Prompt Structure
- Uses `gemini-3.8-flash` via `Firebase.ai.generativeModel`.
- Enforces strict JSON schema via `Schema.obj` with properties `twitterHindi`, `twitterEnglish`, `instagramHindi`, `instagramEnglish`, `linkedInHindi`, `linkedInEnglish`, `facebookHindi`, `facebookEnglish`, `hashtags`, and `suggestedHandles`.
- Tailored prompt emphasizing Saathi Foundation citizen journalism guidelines, public accountability, respect, factual rigor, and civic empowerment.

---

### 5. Implementation Stages & Verification Plan

1. **Step 1 - Project Identity & Permissions:** Update `metadata.json` and `strings.xml` to "GramAwaz - Saathi Post Generator". Update `AndroidManifest.xml` with Internet permission and FileProvider for graphic card sharing.
2. **Step 2 - Dependencies & Firebase Provisioning:** Enable Firebase AI Logic and App Check dependencies in `build.gradle.kts`. Provision via `show_aistudio_ui` / `rpc_action`.
3. **Step 3 - Data Models & Generation Engines:** Implement `SocialPostModels`, `FirebaseAiRepository`, and the resilient `OfflineTemplateEngine`.
4. **Step 4 - ViewModel & State Management:** Implement `GramAwazViewModel` with reactive state for form entries, sample presets (e.g. Singrauli water scheme ground reality), generation progress, inline editing, and clipboard copy.
5. **Step 5 - UI Components:**
   - `ReportFormPane`: Form with clean M3 input fields, chips, photo picker, and presets.
   - `SocialPreviewDeck`: Platform switcher tabs (Twitter/X, Instagram, LinkedIn, Facebook), language toggle (Hindi/English), live character counter, inline editing.
   - `VisualQuoteCard`: Beautiful shareable graphic card layout with village photo, badges, and quote.
   - `AdaptiveContainer`: Responsive dual-pane layout for wide screens and tabbed layout for phones.
6. **Step 6 - Native Sharing & Export:** Implement Android `Intent.ACTION_SEND` ShareSheet, image saving/sharing, and clipboard copy with toast notifications.
7. **Step 7 - Verification & Compilation:** Run `compile_applet` to guarantee error-free compilation and flawless runtime stability.
