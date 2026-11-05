# PostCraft Product Architecture & Redesign Implementation Plan

## 1. Executive Summary & Brand Identity
**PostCraft (पोस्ट क्राफ्ट)** is an AI-powered Citizen Journalism, Ground Reporting & Multi-Platform Social Media Content Studio. It converts raw ground realities, public grievances, and official claims into structured investigative reports, multi-platform social drafts, press notes, and news-style visual cards.

- **Primary Colors**:
  - Primary Navy: `#10243A`
  - Secondary Blue: `#176B87`
  - Accent Cyan: `#20C5D8`
  - Main Light Background: `#F4F7FA`
  - White Cards: `#FFFFFF`
  - Primary Dark Text: `#172B3A`
  - Secondary Text: `#64748B`
  - Border: `#E2E8F0`
  - Success Green: `#16845B`
  - Warning Amber: `#D99A24`
  - Error Red: `#D64545`

- **Typography**:
  - English: Inter & Manrope
  - Hindi: Noto Sans Devanagari

---

## 2. Global Architecture & Navigation Shell

### A. Left Sidebar Navigation
Organized into 5 logical workflow groups:
1. **OVERVIEW**:
   - `dashboard`: Newsroom Dashboard & KPIs
   - `activity`: Recent Ground & Publishing Activity
2. **REPORTING**:
   - `issue_form`: जन-मुद्दा फॉर्म (Structured Intake)
   - `ground_reports`: Ground Reports & Dossiers (12-section investigative layout)
   - `evidence_library`: Visual & Document Evidence Repository
3. **CONTENT STUDIO**:
   - `social_deck`: सोशल डेक (Multi-Platform Studio: X, Instagram, Facebook, LinkedIn)
   - `ai_generator`: AI Content Engine & Writing Assistant
   - `visual_cards`: Visual Card Studio (Canvas-based export)
   - `carousel_maker`: Carousel Slides Maker
   - `press_studio`: Press Release & Media Bulletin Studio
4. **MANAGEMENT**:
   - `content_library`: Content Archive & Search
   - `calendar`: Editorial Content Calendar
   - `projects`: Citizen Campaigns & Projects
   - `analytics`: Editorial Impact & Reach
5. **SETTINGS**:
   - `brand_kit`: Brand Identity & Watermark Kit
   - `workspace`: Newsroom Workspace & Reporters
   - `settings`: Account & Accessibility Settings

### B. Top Header
- Logo with 3D monogram badge + PostCraft branding
- Active module breadcrumb
- Language Pill Switcher (**हिंदी** / **English**) with instantaneous single-language mode
- Theme Switcher (Light Newsroom Desk / Dark Newsroom Night Desk)
- Global Search modal trigger
- New Report Primary CTA button

---

## 3. Core Modules Specification

### 1. Dashboard
- Newsroom greeting: `"नमस्कार, आपका PostCraft न्यूज़रूम तैयार है।"`
- Real Metric KPI Cards (Reports Filed, Active Drafts, Media Releases, Verified Claims)
- Quick Action shortcuts
- Recent Ground Reports list with status chips (Under Investigation, In Review, Resolved)
- Recent publishing timeline

### 2. जन-मुद्दा फॉर्म (Issue Intake Studio)
- **Section A: Issue Details**:
  - Title, Category (Urban Civic, Rural Development, Governance/Policy, Education/Health, Legal Rights)
  - Scope Level (Ward/Village, City/Tehsil, District, State/National)
  - 2-Column Administrative Coordinates with scope-aware placeholders
  - Claim vs Ground Reality with character limits
- **Section B: Citizen & Community Input**:
  - Impact assessment, affected citizens count, local community feedback
- **Section C: Evidence & Verification**:
  - Photo attachment & camera proof
  - Google Search Grounding verification trigger with source citations
- **Section D: Action & Demands**:
  - Demands from authorities, proposed resolution
- **Action Toolbar**: Save Draft, Load Quick Presets, Generate Multi-Platform Content

### 3. Ground Report Generator (Investigative 12-Section Format)
- Headline, Executive Summary, Background, Field Observations, Evidence Breakdown, Official Stance vs Ground Reality, Public Impact, Questions for Authorities, Action Demands, Citations.
- Export as Formal Memorandum or Press Briefing.

### 4. Social Deck (6 Platform Formats)
- **X (Twitter)**: Real-time 280-char counter, hashtag tags, authority handles (`@MoHUA_India`, `@CollectorSingrauli`).
- **Instagram**: Storytelling hook, bulleted facts, carousel slide scripts.
- **Facebook**: Community engagement format with citizen mobilization questions.
- **LinkedIn / Policy Brief**: Governance analysis, systemic policy recommendations.
- **Press Release / WhatsApp Media Bulletin**: Inverted-pyramid 3-paragraph news release.
- **Policy Brief**: Problem definition, data points, responsible ministries, action checklist.

### 5. AI Visual Card Studio
- 6 Templates: Breaking News, Ground Report, Public Issue, Fact Sheet, Evidence Card, Awareness Campaign.
- Live Canvas rendering with pure typography (Noto Sans Devanagari).
- Download as PNG, Copy Image to Clipboard, Native Share.

### 6. Content Library, Calendar & Settings
- Searchable dossier archive with category, status, and date filters.
- Editorial calendar with scheduled media releases.
- Brand profile settings (Organization name, watermark badge, default language).

---

## 4. Technical Stack & Deployment
- **Web**: React 18 + TypeScript + Vite + Tailwind CSS + Lucide Icons + HTML5 Canvas.
- **Vercel Readiness**: `vercel.json` configured at root and `web/` with build scripts for zero-config Git push to Vercel.
- **Mobile Harmony**: Android Jetpack Compose codebase retains 100% sync, matching Modern Newsroom color tokens (`Color.kt`, `Theme.kt`, `GramAwazApp.kt`).
