import { SavedReport, ReportFormState, CampaignThemeId } from '../types';

export interface QuickPreset {
  id: string;
  category: CampaignThemeId;
  labelHi: string;
  labelEn: string;
  icon: string;
  formDataHi: Partial<ReportFormState>;
  formDataEn: Partial<ReportFormState>;
}

export const quickPresets: QuickPreset[] = [
  {
    id: 'chhamrachh-water',
    category: 'RURAL_DEV',
    labelHi: 'छमराछ - टूटी नल-जल पाइपलाइन',
    labelEn: 'Chhamrachh - Broken Water Line',
    icon: '🚰',
    formDataHi: {
      category: 'RURAL_DEV',
      scope: 'WARD_VILLAGE',
      headline: 'छमराछ में पिछले ४ महीने से नल-जल पाइपलाइन टूटी, ५०० घरों में पेयजल संकट',
      localUnit: 'छमराछ (वार्ड ३ एवं ४)',
      subDistrict: 'तहसील मुख्यालय',
      district: 'सोनभद्र',
      state: 'उत्तर प्रदेश',
      officialClaim: 'जल जीवन मिशन पोर्टल पर योजना १००% पूर्ण और नल से जल आपूर्ति नियमित दर्ज है।',
      groundReality: 'मुख्य पाइपलाइन टूटी पड़ी है, गड्ढा खुला पड़ा है, मोटर जली है और लोग २ किमी दूर से पानी लाने को मजबूर हैं।',
      citizenQuotes: 'रामेश्वर सिंह (६२ वर्ष): "४ महीने से कोई अधिकारी झाँकने नहीं आया। बच्चे स्कूल जाने से पहले पानी भरने जाते हैं।"',
      affectedPopulation: 'लगभग ५२० परिवार (२,४०० से अधिक निवासी)',
      department: 'जल जीवन मिशन एवं ग्रामीण पेयजल विभाग',
      evidenceNotes: 'शिकायत सं. GR/2024/7821, मौके की ५ तस्वीरें और वीडियो रिकॉर्डिंग सुरक्षित।',
      actionDemands: '१. टूटी पाइपलाइन की तत्काल मरम्मत, २. स्वच्छ पेयजल टैंकर व्यवस्था, ३. दोषी ठेकेदार पर जुर्माना।',
      deadline: 'आगामी ७ कार्यदिवस',
      relevantOfficial: '@DistrictMagistrate @JalJeevanMission @UPGovt'
    },
    formDataEn: {
      category: 'RURAL_DEV',
      scope: 'WARD_VILLAGE',
      headline: 'Broken drinking water pipeline under Jal Jeevan Mission in Chhamrachh leaves 500 households in acute crisis',
      localUnit: 'Chhamrachh (Wards 3 & 4)',
      subDistrict: 'Tehsil Headquarters',
      district: 'Sonbhadra',
      state: 'Uttar Pradesh',
      officialClaim: 'Jal Jeevan Mission dashboard marks scheme 100% completed with operational tap connections.',
      groundReality: 'Main conduit line ruptured, exposed open trench hazard, pump motor burnt out, residents trekking 2 km for water.',
      citizenQuotes: 'Rameshwar Singh (62): "No nodal officer has visited for 4 months. Schoolchildren are forced to haul water."',
      affectedPopulation: 'Approx 520 households (over 2,400 residents)',
      department: 'Jal Jeevan Mission & Rural Water Supply Department',
      evidenceNotes: 'Grievance ID #GR/2024/7821, timestamped photos and on-site video logs preserved.',
      actionDemands: '1. Immediate pipeline rectification, 2. Emergency drinking water tankers, 3. Penalty audit on contractor.',
      deadline: 'Within 7 working days',
      relevantOfficial: '@DistrictMagistrate @JalJeevanMission @StateGovt'
    }
  },
  {
    id: 'urban-waterlogging',
    category: 'URBAN_CIVIC',
    labelHi: 'शहरी जलभराव एवं खुला नाला',
    labelEn: 'Urban Sewage & Waterlogging',
    icon: '🌧️',
    formDataHi: {
      category: 'URBAN_CIVIC',
      scope: 'CITY_BLOCK',
      headline: 'वार्ड १२ की मुख्य सड़क पर २ महीने से सीवर ओवरफ्लो, डेंगू का प्रकोप और आवागमन ठप',
      localUnit: 'वार्ड १२, आदर्श नगर',
      subDistrict: 'मध्य ज़ोन',
      district: 'कानपुर नगर',
      state: 'उत्तर प्रदेश',
      officialClaim: 'नगर निगम दावा करता है कि मानसून पूर्व नाला सफ़ाई १००% पूरी हो चुकी है।',
      groundReality: 'सीवर लाइन पूरी तरह चोक है, गंदा पानी घरों में घुस रहा है, ५ बच्चे डेंगू से पीड़ित हैं।',
      citizenQuotes: 'सुनीता देवी: "दुकानें बंद हैं, बदबू से साँस लेना दूभर है, नगर निगम केवल आश्वासन दे रहा है।"',
      affectedPopulation: '८०० परिवार एवं मुख्य बाज़ार के २०० दुकानदार',
      department: 'नगर निगम एवं जलकल विभाग',
      evidenceNotes: 'नगर स्वास्थ्य अधिकारी को सौंपे गए ३ ज्ञापन एवं जनसुनवाई पोर्टल टोकन #NN/88231',
      actionDemands: '१. सुपर सक्शन मशीन से तत्काल सीवर सफ़ाई, २. फॉगिंग एवं एंटी-लार्वा छिड़काव, ३. सड़क की पुनः मरम्मत।',
      deadline: '४८ घंटे के भीतर',
      relevantOfficial: '@MunicipalComm @CityMayor @HealthDept'
    },
    formDataEn: {
      category: 'URBAN_CIVIC',
      scope: 'CITY_BLOCK',
      headline: 'Ward 12 main arterial road submerged under sewage overflow for 2 months, sparking dengue outbreak',
      localUnit: 'Ward 12, Adarsh Nagar',
      subDistrict: 'Central Zone',
      district: 'Kanpur',
      state: 'Uttar Pradesh',
      officialClaim: 'Municipal Corporation portal logs pre-monsoon storm drain cleaning as 100% completed.',
      groundReality: 'Choked trunk sewer line pushing wastewater into residences; 5 pediatric dengue cases confirmed.',
      citizenQuotes: 'Sunita Devi: "Local shops are shuttered, the stench is unbearable, yet the civic authority gives only false promises."',
      affectedPopulation: '800 households and 200 local shopkeepers',
      department: 'Municipal Corporation & Urban Sewerage Board',
      evidenceNotes: '3 formal memorandums submitted to Health Officer & Grievance Token #NN/88231',
      actionDemands: '1. Immediate super-sucker line de-silting, 2. Anti-larval chemical fogging, 3. Road resurfacing.',
      deadline: 'Within 48 hours',
      relevantOfficial: '@MunicipalComm @CityMayor @UrbanAffairs'
    }
  },
  {
    id: 'phc-doctor-absence',
    category: 'EDU_HEALTH',
    labelHi: 'प्राथमिक स्वास्थ्य केंद्र पर डॉक्टर नदारद',
    labelEn: 'Rural PHC Doctor Absenteeism',
    icon: '🏥',
    formDataHi: {
      category: 'EDU_HEALTH',
      scope: 'WARD_VILLAGE',
      headline: 'प्राथमिक स्वास्थ्य केंद्र पर ताला: प्रसव पीड़ा में महिला को ले जाना पड़ा ४० किमी दूर',
      localUnit: 'पीएचसी बरगवां',
      subDistrict: 'घोरावल',
      district: 'मिर्ज़ापुर',
      state: 'उत्तर प्रदेश',
      officialClaim: 'स्वास्थ्य विभाग के अनुसार पीएचसी पर २४ घंटे इमरजेंसी एवं प्रसव सेवा उपलब्ध है।',
      groundReality: 'डॉक्टर पिछले ३ महीने से अनुपस्थित हैं, फार्मासिस्ट दवा बांटता है, प्रसव कक्ष में ताला लगा है।',
      citizenQuotes: 'दिनेश यादव: "रात में प्रसव के लिए एंबुलेंस नहीं मिली, निजी वाहन से ले जाते समय रास्ते में डिलीवरी हुई।"',
      affectedPopulation: '१५ गाँवों की लगभग २५,००० ग्रामीण आबादी',
      department: 'मुख्य चिकित्सा अधिकारी (CMO) कार्यालय',
      evidenceNotes: 'हस्ताक्षर युक्त ग्रामीण पंचनामा, बंद अस्पताल का वीडियो, CMO को भेजी गई स्पीड पोस्ट रसीद।',
      actionDemands: '१. एमबीबीएस डॉक्टर एवं एएनएम की तत्काल नियमित तैनाती, २. २४x७ एंबुलेंस सेवा, ३. अनुपस्थित डॉक्टर पर कार्रवाई।',
      deadline: '५ कार्यदिवस',
      relevantOfficial: '@CMO_Office @HealthMin @DistrictMagistrate'
    },
    formDataEn: {
      category: 'EDU_HEALTH',
      scope: 'WARD_VILLAGE',
      headline: 'Primary Health Center locked shut: Expectant mother forced into hazardous 40 km transit for emergency delivery',
      localUnit: 'PHC Bargawan',
      subDistrict: 'Ghorawal',
      district: 'Mirzapur',
      state: 'Uttar Pradesh',
      officialClaim: 'State health dashboard records 24/7 institutional delivery and emergency medical services operational.',
      groundReality: 'Medical officer absent for 3 consecutive months; lone pharmacist dispenses paracetamol; maternity ward locked.',
      citizenQuotes: 'Dinesh Yadav: "No ambulance responded at midnight; our family had to arrange a private vehicle with zero medical support."',
      affectedPopulation: 'Catchment of 15 villages representing 25,000 rural residents',
      department: 'Chief Medical Officer (CMO) & Directorate of Health Services',
      evidenceNotes: 'Signed community resolution, timestamped video of padlocked PHC, postal receipt of CMO petition.',
      actionDemands: '1. Immediate posting of regular MBBS doctor & staff nurse, 2. Dedicated 108 ambulance, 3. Disciplinary inquiry.',
      deadline: 'Within 5 working days',
      relevantOfficial: '@CMO_Office @HealthMinister @DistrictMagistrate'
    }
  },
  {
    id: 'ration-pds-scam',
    category: 'LEGAL_RIGHTS',
    labelHi: 'राशन कटौती एवं ई-पॉस धांधली',
    labelEn: 'PDS Foodgrain Diversion',
    icon: '⚖️',
    formDataHi: {
      category: 'LEGAL_RIGHTS',
      scope: 'WARD_VILLAGE',
      headline: 'कोटेदार द्वारा प्रति कार्ड २ किलो राशन की कटौती और अंगूठा लगवाकर भगाने का आरोप',
      localUnit: 'ग्राम पंचायत रामपुर',
      subDistrict: 'सदर',
      district: 'प्रयागराज',
      state: 'उत्तर प्रदेश',
      officialClaim: 'खाद्य एवं रसद विभाग दावा करता है कि ई-पॉस मशीन द्वारा पारदर्शी और १००% राशन वितरण हो रहा है।',
      groundReality: 'प्रति यूनिट १ से २ किलो अनाज काटा जाता है, विरोध करने पर कार्ड निरस्त करने की धमकी दी जाती है।',
      citizenQuotes: 'कौशल्या देवी (गरीबी रेखा कार्डधारक): "अंगूठा लगवा लिया और कहा सर्वर डाउन है, राशन अगले महीने मिलेगा।"',
      affectedPopulation: '३०० अंत्योदय एवं बीपीएल राशन कार्डधारक',
      department: 'ज़िला आपूर्ति अधिकारी (DSO) कार्यालय',
      evidenceNotes: '६० कार्डधारकों के हस्ताक्षरित बयान, तौल में चोरी का स्टिंग वीडियो फुटेज।',
      actionDemands: '१. कोटेदार का लाइसेंस तत्काल निलंबित हो, २. काटा गया अनाज वापस दिया जाए, ३. निष्पक्ष राशन वितरण समिति बने।',
      deadline: '३ कार्यदिवस',
      relevantOfficial: '@DistrictSupplyOfficer @FoodDept @DMPrayagraj'
    },
    formDataEn: {
      category: 'LEGAL_RIGHTS',
      scope: 'WARD_VILLAGE',
      headline: 'Fair Price Shop dealer siphoning 2 kg foodgrains per ration card while threatening vulnerable beneficiaries',
      localUnit: 'Gram Panchayat Rampur',
      subDistrict: 'Sadar Block',
      district: 'Prayagraj',
      state: 'Uttar Pradesh',
      officialClaim: 'Food & Civil Supplies Department claims 100% biometric transparency via automated electronic PoS machines.',
      groundReality: 'Systematic under-weighing of 1 to 2 kg per family; biometric thumbprints collected while grain is withheld.',
      citizenQuotes: 'Kaushalya Devi (BPL Beneficiary): "My thumb was scanned, but the dealer claimed server error and refused our monthly foodgrain quota."',
      affectedPopulation: '300 Antyodaya and Priority Household ration card holders',
      department: 'District Supply Office (DSO) & Food Safety Directorate',
      evidenceNotes: 'Affidavits from 60 registered cardholders and undercover video verifying short-weight delivery.',
      actionDemands: '1. Immediate suspension of dealership license, 2. Back-distribution of diverted grain, 3. Citizen vigilance team.',
      deadline: 'Within 3 business days',
      relevantOfficial: '@DistrictSupplyOfficer @FoodDept @DistrictMagistrate'
    }
  }
];

export const initialMockReports: SavedReport[] = [
  {
    id: 'report-001',
    createdAt: Date.now() - 3600000 * 24,
    status: 'UNDER_INVESTIGATION',
    formData: quickPresets[0].formDataHi as ReportFormState,
    generatedReport: '',
    socialBundle: {
      x: '',
      instagram: '',
      facebook: '',
      linkedin: '',
      pressRelease: '',
      policyBrief: ''
    }
  },
  {
    id: 'report-002',
    createdAt: Date.now() - 3600000 * 72,
    status: 'IN_REVIEW',
    formData: quickPresets[1].formDataHi as ReportFormState,
    generatedReport: '',
    socialBundle: {
      x: '',
      instagram: '',
      facebook: '',
      linkedin: '',
      pressRelease: '',
      policyBrief: ''
    }
  },
  {
    id: 'report-003',
    createdAt: Date.now() - 3600000 * 120,
    status: 'RESOLVED',
    formData: quickPresets[2].formDataHi as ReportFormState,
    generatedReport: '',
    socialBundle: {
      x: '',
      instagram: '',
      facebook: '',
      linkedin: '',
      pressRelease: '',
      policyBrief: ''
    }
  }
];
