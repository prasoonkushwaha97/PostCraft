import { LanguageMode, IssueScope, CampaignThemeId, SocialPlatformId, EventTypeId, ReportStatus } from '../types';

export interface Translations {
  appName: string;
  appSubtitle: string;
  tagline: string;
  brandPositioning: string;

  // Nav Groups
  navOverview: string;
  navDashboard: string;
  navReporting: string;
  navNewIntake: string;
  navGroundReport: string;
  navContentStudio: string;
  navSocialDeck: string;
  navVisualStudio: string;
  navManagement: string;
  navContentLibrary: string;
  navContentCalendar: string;
  navAnalytics: string;
  navSettings: string;
  navBrandSettings: string;

  // Header & Common
  searchPlaceholder: string;
  languageName: string;
  notifications: string;
  newReportBtn: string;
  saveDraft: string;
  publish: string;
  copyText: string;
  copied: string;
  downloadPng: string;
  exportPdf: string;
  statusInvestigation: string;
  statusReview: string;
  statusResolved: string;

  // Intake Form
  formTitle: string;
  formSubtitle: string;
  quickPresetsTitle: string;
  categoryLabel: string;
  scopeLabel: string;
  
  // Categories
  catUrban: string;
  catUrbanDesc: string;
  catRural: string;
  catRuralDesc: string;
  catGov: string;
  catGovDesc: string;
  catEduHealth: string;
  catEduHealthDesc: string;
  catLegal: string;
  catLegalDesc: string;

  // Scopes
  scopeWard: string;
  scopeCity: string;
  scopeDistrict: string;
  scopeState: string;

  // Form Sections
  secDetails: string;
  secDetailsDesc: string;
  secInput: string;
  secInputDesc: string;
  secEvidence: string;
  secEvidenceDesc: string;
  secDemands: string;
  secDemandsDesc: string;

  // Fields
  headlineLabel: string;
  headlinePlaceholder: string;
  locationLabel: string;
  localUnitLabel: string;
  subDistrictLabel: string;
  districtLabel: string;
  stateLabel: string;
  officialClaimLabel: string;
  officialClaimPlaceholder: string;
  groundRealityLabel: string;
  groundRealityPlaceholder: string;
  citizenQuotesLabel: string;
  citizenQuotesPlaceholder: string;
  affectedPopulationLabel: string;
  affectedPopulationPlaceholder: string;
  departmentLabel: string;
  departmentPlaceholder: string;
  evidenceNotesLabel: string;
  evidenceNotesPlaceholder: string;
  actionDemandsLabel: string;
  actionDemandsPlaceholder: string;
  deadlineLabel: string;
  deadlinePlaceholder: string;
  relevantOfficialLabel: string;
  relevantOfficialPlaceholder: string;
  generateStudioBtn: string;

  // Ground Report
  reportTitle: string;
  reportSubtitle: string;
  executiveSummary: string;
  investigativeDetails: string;
  verificationChecklist: string;

  // Social Deck
  socialDeckTitle: string;
  socialDeckSubtitle: string;
  charCount: string;
  suggestedHashtags: string;
  tagOfficial: string;
  sharePost: string;

  // Visual Studio
  visualStudioTitle: string;
  visualStudioSubtitle: string;
  templateStyle: string;
  downloadCard: string;
  copyCard: string;

  // Library & Analytics
  libraryTitle: string;
  librarySubtitle: string;
  totalDossiers: string;
  publishedPosts: string;
  resolvedIssues: string;
  totalReach: string;
}

export const locales: Record<LanguageMode, Translations> = {
  hi: {
    appName: "पोस्ट क्राफ्ट",
    appSubtitle: "सोशल मीडिया पोस्ट एवं ग्राउंड रिपोर्ट जनरेटर",
    tagline: "ज़मीनी सूचना से प्रकाशन-योग्य पत्रकारिता",
    brandPositioning: "नागरिक पत्रकारिता और साक्ष्य-आधारित जन-रिपोर्टिंग का डिजिटल न्यूज़रूम",

    navOverview: "अवलोकन",
    navDashboard: "न्यूज़रूम डैशबोर्ड",
    navReporting: "रिपोर्टिंग एवं जाँच",
    navNewIntake: "नया जन-मुद्दा फ़ॉर्म",
    navGroundReport: "ग्राउंड इन्वेस्टिगेशन रिपोर्ट",
    navContentStudio: "कंटेंट स्टूडियो",
    navSocialDeck: "सोशल मीडिया डेक (6 प्लेटफ़ॉर्म)",
    navVisualStudio: "विजुअल न्यूज़ कार्ड स्टूडियो",
    navManagement: "प्रबंधन",
    navContentLibrary: "कंटेंट लाइब्रेरी एवं आर्काइव",
    navContentCalendar: "प्रकाशन कैलेंडर",
    navAnalytics: "इम्पैक्ट एवं रीच एनालिटिक्स",
    navSettings: "कॉन्फ़िगरेशन",
    navBrandSettings: "न्यूज़रूम ब्रांड किट",

    searchPlaceholder: "मुद्दे, ज़िला, विभाग या कीवर्ड खोजें...",
    languageName: "हिंदी",
    notifications: "सूचनाएँ",
    newReportBtn: "नई रिपोर्ट बनाएँ",
    saveDraft: "ड्राफ़्ट सुरक्षित करें",
    publish: "प्रकाशित करें",
    copyText: "टेक्स्ट कॉपी करें",
    copied: "कॉपी हो गया!",
    downloadPng: "PNG कार्ड डाउनलोड करें",
    exportPdf: "PDF एक्सपोर्ट करें",
    statusInvestigation: "जाँच जारी",
    statusReview: "समीक्षाधीन",
    statusResolved: "समाधान संपन्न",

    formTitle: "नागरिक समस्या एवं जन-मुद्दा फ़ॉर्म",
    formSubtitle: "ज़मीनी साक्ष्य दर्ज करें और स्वचालित बहु-प्लेटफ़ॉर्म रिपोर्ट व सोशल पोस्ट तैयार करें",
    quickPresetsTitle: "त्वरित जनहित मुद्दे (क्लिक करें):",
    categoryLabel: "मुद्दे की श्रेणी",
    scopeLabel: "भौगोलिक स्तर / कार्यक्षेत्र",

    catUrban: "शहरी नागरिक मुद्दे",
    catUrbanDesc: "सड़क, जलभराव, सीवरेज, कचरा प्रबंधन, स्ट्रीट लाइट, वायु प्रदूषण",
    catRural: "ग्रामीण विकास एवं कृषि",
    catRuralDesc: "नल जल योजना, PMGSY सड़क, नहर, खाद-बीज संकट, मंडी, मनरेगा",
    catGov: "प्रशासन, नीति एवं जवाबदेही",
    catGovDesc: "सरकारी योजनाएँ, RTI, बजट विसंगति, भ्रष्टाचार, जनसुनवाई, लेटलतीफी",
    catEduHealth: "शिक्षा एवं स्वास्थ्य सेवाएँ",
    catEduHealthDesc: "प्राथमिक स्वास्थ्य केंद्र, डॉक्टर अनुपस्थिति, सरकारी स्कूल, छात्रवृत्ति",
    catLegal: "कानून, सुरक्षा एवं जन-अधिकार",
    catLegalDesc: "FIR/पुलिस कार्रवाई, राशन वितरण, उपभोक्ता अधिकार, मानवाधिकार हनन",

    scopeWard: "वार्ड / मोहल्ला / गाँव",
    scopeCity: "शहर / ब्लॉक / तहसील",
    scopeDistrict: "ज़िला स्तर",
    scopeState: "राज्य / राष्ट्रीय स्तर",

    secDetails: "१. मुद्दे का विवरण एवं प्रशासनिक स्तर",
    secDetailsDesc: "स्थान, भौगोलिक सीमा और संबंधित विभाग का सटीक ब्यौरा",
    secInput: "२. सरकारी दावा बनाम ज़मीनी हक़ीक़त",
    secInputDesc: "दस्तावेज़ी दावों और मौके की वास्तविक स्थिति का निष्पक्ष साक्ष्य",
    secEvidence: "३. जन-आवाज़ एवं प्रत्यक्षदर्शी साक्ष्य",
    secEvidenceDesc: "नागरिकों के बयान, प्रभावित जनसंख्या और दस्तावेज़ी प्रमाण",
    secDemands: "४. प्रशासनिक माँग एवं समय-सीमा",
    secDemandsDesc: "त्वरित समाधान हेतु आधिकारिक माँग और जवाबदेह अधिकारी",

    headlineLabel: "मुख्य शीर्षक / मुद्दे का नाम",
    headlinePlaceholder: "उदा. छमराछ में पिछले ४ महीने से जल-जीवन मिशन की पाइपलाइन टूटी, ५०० घरों में पेयजल संकट",
    locationLabel: "स्थान का विवरण",
    localUnitLabel: "गाँव / वार्ड / मोहल्ला",
    subDistrictLabel: "ब्लॉक / तहसील / ज़ोन",
    districtLabel: "ज़िला",
    stateLabel: "राज्य",
    officialClaimLabel: "सरकारी / आधिकारिक दावा",
    officialClaimPlaceholder: "उदा. सरकारी पोर्टल अनुसार योजना १००% पूर्ण और नल से जल आपूर्ति चालू दर्ज है",
    groundRealityLabel: "ज़मीनी हक़ीक़त (मौके की स्थिति)",
    groundRealityPlaceholder: "उदा. पाइपलाइन टूटी पड़ी है, गड्ढा खुला है, मोटर जली हुई है, लोग २ किमी दूर से पानी ला रहे हैं",
    citizenQuotesLabel: "नागरिकों के कथन / प्रत्यक्षदर्शी बयान",
    citizenQuotesPlaceholder: "उदा. 'रमेश कुमार (वार्ड ५): शिकायत दर्ज करने के बाद भी कोई अधिकारी झाँकने नहीं आया'",
    affectedPopulationLabel: "प्रभावित परिवारों / नागरिकों की संख्या",
    affectedPopulationPlaceholder: "उदा. लगभग ४५० परिवार, २२०० से अधिक ग्रामीण",
    departmentLabel: "जिम्मेदार विभाग / एजेंसी",
    departmentPlaceholder: "उदा. लोक स्वास्थ्य यांत्रिकी विभाग (PHED) / नगर निगम",
    evidenceNotesLabel: "साक्ष्य विवरण (फ़ोटो, वीडियो, RTI, शिकायत संख्या)",
    evidenceNotesPlaceholder: "उदा. शिकायत सं. GR/2024/8912, मौके की ५ तस्वीरें और वीडियो रिकॉर्डिंग सुरक्षित",
    actionDemandsLabel: "प्रमुख जनहित माँगें",
    actionDemandsPlaceholder: "उदा. १. टूटी पाइपलाइन की तत्काल मरम्मत, २. स्वच्छ पेयजल टैंकर व्यवस्था, ३. दोषी ठेकेदार पर जुर्माना",
    deadlineLabel: "समाधान की अपेक्षित समय-सीमा",
    deadlinePlaceholder: "उदा. आगामी ७ कार्यदिवसों के भीतर",
    relevantOfficialLabel: "जवाबदेह अधिकारी / जनप्रतिनिधि (टैग करने हेतु)",
    relevantOfficialPlaceholder: "उदा. @DistrictMagistrate, @PHED_Dept, @LocalMLA",
    generateStudioBtn: "पत्रकारिता रिपोर्ट एवं सोशल डेक तैयार करें",

    reportTitle: "इन्वेस्टिगेटिव ग्राउंड रिपोर्ट",
    reportSubtitle: "साक्ष्य-आधारित १२-बिंदु स्वतंत्र नागरिक रिपोर्ट",
    executiveSummary: "कार्यकारी सारांश",
    investigativeDetails: "गहन दस्तावेज़ी विश्लेषण",
    verificationChecklist: "तथ्य-जाँच एवं सत्यापन सूची",

    socialDeckTitle: "मल्टी-प्लेटफ़ॉर्म सोशल डेक",
    socialDeckSubtitle: "प्रत्येक प्लेटफ़ॉर्म के अनुकूलित प्रारूप में तैयार पोस्ट्स",
    charCount: "वर्ण सीमा",
    suggestedHashtags: "अनुशंसित हैशटैग्स",
    tagOfficial: "अधिकारियों को टैग करें",
    sharePost: "साझा करें",

    visualStudioTitle: "विजुअल न्यूज़ कार्ड स्टूडियो",
    visualStudioSubtitle: "१०८०x१३५० न्यूज़-ग्रेड सोशल कार्ड जनरेटर (HD रेडी)",
    templateStyle: "कार्ड टेम्पलेट शैली",
    downloadCard: "PNG कार्ड डाउनलोड करें",
    copyCard: "क्लिपबोर्ड पर कॉपी करें",

    libraryTitle: "कंटेंट लाइब्रेरी एवं आर्काइव",
    librarySubtitle: "आपके न्यूज़रूम की सभी रिपोर्ट्स, साक्ष्य और सोशल पोस्ट्स का सुरक्षित संग्रह",
    totalDossiers: "कुल ग्राउंड डोजियर",
    publishedPosts: "तैयार सोशल पोस्ट्स",
    resolvedIssues: "समाधान कराए गए मुद्दे",
    totalReach: "अनुमानित नागरिक रीच",
  },
  en: {
    appName: "PostCraft",
    appSubtitle: "Social Deck & Ground Reality Post Generator",
    tagline: "From Ground Information to Publication-Ready Content",
    brandPositioning: "AI-Powered Citizen Journalism, Ground Reporting & Editorial Studio",

    navOverview: "Overview",
    navDashboard: "Newsroom Dashboard",
    navReporting: "Reporting & Inquiry",
    navNewIntake: "New Issue Intake Form",
    navGroundReport: "Ground Investigation Report",
    navContentStudio: "Content Studio",
    navSocialDeck: "Multi-Platform Social Deck",
    navVisualStudio: "Visual News Card Studio",
    navManagement: "Management",
    navContentLibrary: "Content Library & Archive",
    navContentCalendar: "Publishing Calendar",
    navAnalytics: "Impact & Reach Analytics",
    navSettings: "Configuration",
    navBrandSettings: "Newsroom Brand Kit",

    searchPlaceholder: "Search issues, locations, departments or keywords...",
    languageName: "English",
    notifications: "Notifications",
    newReportBtn: "Create New Report",
    saveDraft: "Save Draft",
    publish: "Publish",
    copyText: "Copy Text",
    copied: "Copied!",
    downloadPng: "Download PNG Card",
    exportPdf: "Export PDF",
    statusInvestigation: "Under Investigation",
    statusReview: "In Review",
    statusResolved: "Resolved",

    formTitle: "Citizen Issue & Public Intake Form",
    formSubtitle: "Document ground evidence to automatically generate investigative reports and social campaigns",
    quickPresetsTitle: "Quick Civic Presets (Click to Load):",
    categoryLabel: "Issue Category",
    scopeLabel: "Geographic Scope",

    catUrban: "Urban Civic & Infrastructure",
    catUrbanDesc: "Roads, waterlogging, sewage overflow, garbage disposal, street lighting, air pollution",
    catRural: "Rural Development & Agriculture",
    catRuralDesc: "Tap water mission, PMGSY roads, irrigation canals, seed/fertilizer shortages, mandi, MGNREGA",
    catGov: "Governance, Policy & Accountability",
    catGovDesc: "Welfare scheme delivery, RTI delays, budget discrepancies, bureaucratic negligence, public hearings",
    catEduHealth: "Education & Public Healthcare",
    catEduHealthDesc: "Primary Health Center shortages, absent doctors, school building safety, student scholarships",
    catLegal: "Legal Rights, Safety & Justice",
    catLegalDesc: "Police FIR inaction, PDS ration fraud, consumer grievances, civic rights violations",

    scopeWard: "Ward / Mohalla / Village",
    scopeCity: "City / Block / Tehsil",
    scopeDistrict: "District Level",
    scopeState: "State / National Level",

    secDetails: "1. Issue Details & Geographic Scope",
    secDetailsDesc: "Exact location, administrative hierarchy, and responsible department",
    secInput: "2. Official Claim vs Ground Reality",
    secInputDesc: "Documented portal claims contrasted with verified ground observations",
    secEvidence: "3. Citizen Voices & Documented Evidence",
    secEvidenceDesc: "First-person citizen testimony, affected population figures, and media records",
    secDemands: "4. Action Demands & Resolution Timeline",
    secDemandsDesc: "Actionable public demands, statutory deadlines, and designated nodal officers",

    headlineLabel: "Main Headline / Issue Summary",
    headlinePlaceholder: "e.g., Broken pipeline under Jal Jeevan Mission in Chhamrachh leaves 500 households without drinking water for 4 months",
    locationLabel: "Geographic Location",
    localUnitLabel: "Village / Ward / Neighborhood",
    subDistrictLabel: "Block / Tehsil / Zone",
    districtLabel: "District",
    stateLabel: "State",
    officialClaimLabel: "Official / Departmental Claim",
    officialClaimPlaceholder: "e.g., Government portal records project 100% completed and regular tap water supply functional",
    groundRealityLabel: "Ground Reality (On-Site Verification)",
    groundRealityPlaceholder: "e.g., Main conduit ruptured, open trench hazard, motor burnt out, residents forced to fetch water from 2 km away",
    citizenQuotesLabel: "Citizen Testimony / Witness Quotes",
    citizenQuotesPlaceholder: "e.g., 'Ramesh Kumar (Ward 5): Multiple written petitions submitted; no official has visited site'",
    affectedPopulationLabel: "Estimated Affected Households / Population",
    affectedPopulationPlaceholder: "e.g., Approx 450 households, over 2,200 residents directly impacted",
    departmentLabel: "Responsible Department / Agency",
    departmentPlaceholder: "e.g., Public Health Engineering Department (PHED) / Municipal Corporation",
    evidenceNotesLabel: "Evidence Dossier (Photos, Video, RTI, Grievance ID)",
    evidenceNotesPlaceholder: "e.g., Complaint Ref #GR/2024/8912, geo-tagged photos and site video timestamped on record",
    actionDemandsLabel: "Key Public Demands",
    actionDemandsPlaceholder: "e.g., 1. Immediate pipeline repair, 2. Emergency drinking water tanker supply, 3. Contractor audit",
    deadlineLabel: "Expected Resolution Deadline",
    deadlinePlaceholder: "e.g., Within 7 official working days",
    relevantOfficialLabel: "Designated Officials / Representatives to Tag",
    relevantOfficialPlaceholder: "e.g., @DistrictMagistrate, @PHED_Dept, @LocalMLA",
    generateStudioBtn: "Generate Investigative Report & Social Deck",

    reportTitle: "Investigative Ground Report",
    reportSubtitle: "Evidence-Based 12-Section Independent Civic Dossier",
    executiveSummary: "Executive Summary",
    investigativeDetails: "Detailed Ground Investigation",
    verificationChecklist: "Fact-Checking & Verification Checklist",

    socialDeckTitle: "Multi-Platform Social Deck",
    socialDeckSubtitle: "Tailored publications optimized for each social platform",
    charCount: "Character Count",
    suggestedHashtags: "Recommended Hashtags",
    tagOfficial: "Tag Designated Officials",
    sharePost: "Share Content",

    visualStudioTitle: "Visual News Card Studio",
    visualStudioSubtitle: "1080x1350 High-Definition Newsroom Social Card Studio",
    templateStyle: "Card Template Style",
    downloadCard: "Download PNG Card",
    copyCard: "Copy to Clipboard",

    libraryTitle: "Content Library & Archive",
    librarySubtitle: "Secure archive of all newsroom dossiers, verified evidence, and published posts",
    totalDossiers: "Total Ground Dossiers",
    publishedPosts: "Social Posts Formatted",
    resolvedIssues: "Issues Resolved",
    totalReach: "Estimated Civic Reach",
  }
};
