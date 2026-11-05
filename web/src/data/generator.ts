import { ReportFormState, GeneratedPostBundle, LanguageMode } from '../types';

export function generateGroundReport(form: ReportFormState, lang: LanguageMode): string {
  const isHi = lang === 'hi';
  const now = new Date().toLocaleDateString(isHi ? 'hi-IN' : 'en-US', {
    day: 'numeric',
    month: 'long',
    year: 'numeric'
  });

  const locationStr = [form.localUnit, form.subDistrict, form.district, form.state]
    .filter(Boolean)
    .join(', ');

  if (isHi) {
    return `# ग्राउंड इन्वेस्टिगेशन रिपोर्ट: ${form.headline || 'जन-मुद्दा जाँच'}
**दस्तावेज़ संख्या:** PC-GR-${Date.now().toString().slice(-6)} | **दिनांक:** ${now}
**भौगोलिक स्तर:** ${form.localUnit ? form.localUnit + ' | ' : ''}${form.district || ''} (${form.state || ''})
**जाँच एजेंसी / न्यूज़रूम:** पोस्ट क्राफ्ट सिटीजन जर्नलिज्म डेस्क

---

### १. कार्यकारी सारांश (Executive Summary)
${locationStr} के अंतर्गत ${form.category} से जुड़ी गंभीर समस्या सामने आई है। जहाँ आधिकारिक दस्तावेज़ों में सब कुछ सुचारू होने का दावा किया जा रहा है, वहीं ज़मीनी हक़ीक़त में आम नागरिकों को भारी कठिनाइयों का सामना करना पड़ रहा है। लगभग ${form.affectedPopulation || 'सैकड़ों नागरिक'} इससे सीधे प्रभावित हैं।

### २. प्रशासनिक एवं भौगोलिक स्थिति (Administrative Context)
* **गाँव / वार्ड:** ${form.localUnit || 'निर्दिष्ट नहीं'}
* **ब्लॉक / तहसील:** ${form.subDistrict || 'निर्दिष्ट नहीं'}
* **ज़िला एवं राज्य:** ${form.district || ''}, ${form.state || ''}
* **संबंधित विभाग:** ${form.department || 'संबंधित नोडल विभाग'}

### ३. आधिकारिक रिकॉर्ड एवं सरकारी दावा (Official Claims)
सरकारी रिकॉर्ड, विभागीय पोर्टल एवं संबंधित अधिकारियों के अनुसार:
> "${form.officialClaim || 'विभागीय पोर्टल पर कार्य संतोषजनक व चालू स्थिति में दर्ज है।'}"

### ४. ज़मीनी हक़ीक़त एवं प्रत्यक्षदर्शी साक्ष्य (Ground Reality)
न्यूज़रूम व स्थानीय नागरिक रिपोर्टरों द्वारा मौके पर की गई जाँच में पाया गया:
> "${form.groundReality || 'मौके पर स्थिति अत्यंत दयनीय है और नागरिक बुनियादी सुविधाओं से वंचित हैं।'}"

### ५. प्रभावित जनसमुदाय एवं प्रभाव क्षेत्र (Impact Assessment)
* **प्रभावित जनसंख्या / परिवार:** ${form.affectedPopulation || 'व्यापक जनसमुदाय'}
* **दैनिक जीवन पर प्रभाव:** आजीविका, स्वास्थ्य व आवागमन में निरंतर बाधा।
* **जोखिम स्तर:** उच्च (जनस्वास्थ्य व सुरक्षा संकट)

### ६. प्रत्यक्षदर्शी नागरिकों के बयान (Citizen Testimonies)
${form.citizenQuotes || 'स्थानीय निवासियों का कहना है कि बारंबार लिखित शिकायतों के बाद भी प्रशासन द्वारा कोई सुध नहीं ली गई।'}

### ७. साक्ष्य एवं दस्तावेज़ी रिकॉर्ड (Evidence Dossier)
* **दस्तावेज़ / शिकायत संख्या:** ${form.evidenceNotes || 'स्थानीय शिकायत प्रतिलिपि व जनसुनवाई पंजीकरण'}
* **मल्टीमीडिया साक्ष्य:** जियो-टैग्ड फोटोग्राफ्स, वीडियो बयान व ऑन-साइट पंचनामा सुरक्षित।

### ८. बजट एवं वित्तीय पृष्ठभूमि (Financial & Budgetary Allocation)
विभागीय स्वीकृतियों के बावजूद कार्य की गुणवत्ता और क्रियान्वयन में भारी विसंगति उजागर हुई है। ठेकेदार व तकनीकी पर्यवेक्षकों की भूमिका की निष्पक्ष जाँच आवश्यक है।

### ९. विनियामक एवं वैधानिक उल्लंघन (Statutory Non-Compliance)
नागरिक चार्टर के तय मानकों एवं लोक सेवा गारंटी अधिनियम के प्रावधानों का खुला उल्लंघन पाया गया है।

### १०. संस्थागत विफलता के बिंदु (Points of Institutional Neglect)
१. नियमित जमीनी निगरानी एवं सत्यापन का अभाव।
२. जनसुनवाई व ऑनलाइन शिकायत निवारण पोर्टलों पर केवल कागजी समाधान दिखाना।
३. दोषी संवेदकों व लापरवाह कर्मियों पर दंड का न होना।

### ११. नागरिक जनहित माँगें (Citizen Demands)
${form.actionDemands || '१. मौके पर तकनीकी दल भेजकर तत्काल कार्य शुरू किया जाए।\n२. प्रभावित क्षेत्र में वैकल्पिक आपातकालीन व्यवस्था हो।\n३. लापरवाही बरतने वाले अधिकारियों पर अनुशासनात्मक कार्रवाई की जाए।'}

### १२. अपेक्षित समाधान समय-सीमा (Resolution Timeline)
* **समय-सीमा:** ${form.deadline || 'आगामी ७ कार्यदिवस'}
* **जवाबदेह अधिकारी:** ${form.relevantOfficial || 'ज़िला दंडाधिकारी व संबंधित विभागाध्यक्ष'}
`;
  } else {
    return `# GROUND INVESTIGATION REPORT: ${form.headline || 'Civic Issue Investigation'}
**Dossier Reference:** PC-GR-${Date.now().toString().slice(-6)} | **Filing Date:** ${now}
**Geographic Scope:** ${form.localUnit ? form.localUnit + ' | ' : ''}${form.district || ''} (${form.state || ''})
**Editorial Unit:** PostCraft Citizen Journalism & Ground Inquiry Desk

---

### 1. Executive Summary
A critical civic inquiry in ${locationStr} regarding ${form.category} reveals a stark contrast between paper claims and on-the-ground execution. While administrative records claim compliance, over ${form.affectedPopulation || 'hundreds of local residents'} continue to face severe disruption in everyday life.

### 2. Administrative & Geographic Coordinates
* **Local Unit (Ward / Village):** ${form.localUnit || 'Unspecified'}
* **Block / Tehsil / Zone:** ${form.subDistrict || 'Unspecified'}
* **District & State:** ${form.district || ''}, ${form.state || ''}
* **Responsible Department:** ${form.department || 'Nodal Municipal/Public Agency'}

### 3. Documented Official Claim
According to statutory filings, departmental dashboards, and official statements:
> "${form.officialClaim || 'Official portal states work is completed and fully operational.'}"

### 4. Verified Ground Reality
Independent on-site inspection by local citizen correspondents establishes:
> "${form.groundReality || 'On-site infrastructure is severely degraded, defective, or completely non-functional.'}"

### 5. Community Impact Assessment
* **Affected Population:** ${form.affectedPopulation || 'Widespread resident community'}
* **Public Hardship:** Severe impediment to hygiene, safety, commute, and fundamental rights.
* **Severity Matrix:** Critical (Demands immediate intervention)

### 6. Citizen Testimony & Witness Statements
${form.citizenQuotes || 'Residents report that despite repeated written applications, municipal authorities have failed to inspect the site.'}

### 7. Documentary Evidence & Audit Trail
* **Filing / Complaint Reference:** ${form.evidenceNotes || 'Grievance ticket & public petition copy'}
* **Verification Artifacts:** Timestamped geo-tagged photography, video logs, and community endorsements.

### 8. Budgetary & Execution Discrepancy
Public expenditure records warrant an immediate independent quality audit into contractor compliance and departmental inspection sign-offs.

### 9. Statutory & Regulatory Violations
Non-compliance with Public Service Guarantee benchmarks, statutory citizen charters, and basic infrastructure safety mandates.

### 10. Key Points of Administrative Inaction
1. Complete breakdown of periodic physical field inspections.
2. Nominal paper disposals of citizen grievance petitions.
3. Total absence of liquidated damages or accountability measures on errant vendors.

### 11. Core Citizen Action Demands
${form.actionDemands || '1. Dispatch emergency technical response team for site rectification.\n2. Provide immediate relief services to affected residents.\n3. Hold an open public grievance audit with nodal officers present.'}

### 12. Resolution Timeline & Accountability
* **Statutory Deadline:** ${form.deadline || 'Within 7 business days'}
* **Designated Nodal Authorities:** ${form.relevantOfficial || 'District Magistrate & Chief Departmental Engineer'}
`;
  }
}

export function generateSocialBundle(form: ReportFormState, lang: LanguageMode): GeneratedPostBundle {
  const isHi = lang === 'hi';
  const now = new Date().toLocaleDateString(isHi ? 'hi-IN' : 'en-US', {
    day: 'numeric',
    month: 'long',
    year: 'numeric'
  });
  const location = [form.localUnit, form.district, form.state].filter(Boolean).join(', ');
  const tags = form.relevantOfficial || (isHi ? '@DistrictMagistrate @CMOffice' : '@DistrictMagistrate @UrbanGov');

  const defaultHashtags = isHi
    ? ['#जनआवाज़', '#ग्राउंडरिपोर्ट', '#पोस्टक्राफ्ट', '#जवाबदेही', '#सच्चाई']
    : ['#GroundReport', '#CivicAccountability', '#PostCraft', '#CitizenJournalism', '#PublicInterest'];

  // X / Twitter (High Impact, tight concise text)
  const xPost = isHi
    ? `🚨 ग्राउंड रिपोर्ट: ${form.headline || 'गंभीर जन-मुद्दा'}

📍 स्थान: ${location || 'स्थानीय क्षेत्र'}
📑 सरकारी दावा: ${form.officialClaim || 'योजना पूर्ण व चालू'}
❌ ज़मीनी हक़ीक़त: ${form.groundReality || 'मौके पर स्थिति अत्यंत खराब'}
👥 प्रभावित: ${form.affectedPopulation || 'सैकड़ों नागरिक'}

संज्ञान लें ${tags}
तत्काल समाधान अपेक्षित!
${defaultHashtags.slice(0, 4).join(' ')}`
    : `🚨 Ground Reality: ${form.headline || 'Critical Public Issue'}

📍 Location: ${location || 'Local jurisdiction'}
📑 Official Claim: ${form.officialClaim || 'Marked completed on portal'}
❌ Reality on Ground: ${form.groundReality || 'Broken infrastructure, zero service'}
👥 Impact: ${form.affectedPopulation || 'Hundreds of citizens'}

Urgent action needed ${tags}
${defaultHashtags.slice(0, 4).join(' ')}`;

  // Instagram (Visual story, hook, details, call to action)
  const instaPost = isHi
    ? `📌 पोस्ट क्राफ्ट इन्वेस्टिगेशन | ${form.headline || 'जन-मुद्दा रिपोर्ट'}

कागज़ों में विकास की चमक, ज़मीन पर बुनियादी हक़ की जंग!

📍 क्षेत्र: ${location}
🏛️ विभाग: ${form.department || 'संबंधित विभाग'}

🔍 दावा बनाम हक़ीक़त:
• क्या है सरकारी दावा?
"${form.officialClaim || 'योजना पूरी तरह चालू है।'}"

• क्या कहती है ज़मीनी हक़ीक़त?
"${form.groundReality || 'मौके पर कार्य अधूरा पड़ा है और लोग परेशान हैं।'}"

🗣️ जनता की आवाज़:
${form.citizenQuotes || 'नागरिकों का कहना है कि शिकायत के बाद भी कोई सुनवाई नहीं हुई।'}

⚖️ प्रमुख माँगें:
${form.actionDemands || 'तत्काल मरम्मत व जवाबदेही तय की जाए।'}

👉 स्वाइप करें और ग्राउंड तस्वीरों के साथ पूरी रिपोर्ट देखें।
शेयर करें ताकि ज़िम्मेदार अधिकारियों तक आवाज़ पहुँचे!

---
${defaultHashtags.join(' ')} #नागरिकअधिकार #जनसुनवाई #पारदर्शिता`
    : `📌 PostCraft Field Report | ${form.headline || 'Public Issue Dossier'}

When official reports paint a picture of completion, but ground inspection tells a starkly different story.

📍 Location: ${location}
🏛️ Agency: ${form.department || 'Designated Public Department'}

🔍 Official Claim vs Ground Reality:
• The Paper Claim:
"${form.officialClaim || 'Officially recorded as fully operational.'}"

• The Ground Inspection:
"${form.groundReality || 'Severely broken infrastructure causing daily distress to residents.'}"

🗣️ Voices from the Ground:
${form.citizenQuotes || 'Local residents report absolute inaction despite multiple registered petitions.'}

⚖️ Public Demands:
${form.actionDemands || 'Immediate technical audit and emergency restoration.'}

👉 Swipe to inspect verified ground evidence and field documentation.
Share to demand accountability from designated authorities!

---
${defaultHashtags.join(' ')} #Transparency #PublicSafety #RightToCivicServices`;

  // Facebook (Community narrative, detailed, dialogue oriented)
  const fbPost = isHi
    ? `📢 जनहित रिपोर्ट: ${form.headline || 'जन-समस्या की ग्राउंड पड़ताल'}

${location} से एक बेहद गंभीर जनहित मामला सामने आया है।

सरकारी पोर्टलों और विज्ञापनों में जिस काम को 'पूर्ण' घोषित कर दिया गया है, मौके पर जाकर देखने पर सच्चाई इसके ठीक उलट दिखाई देती है। 

🔴 क्या दावा किया गया था?
${form.officialClaim || 'काम पूरा हो चुका है और नागरिक लाभान्वित हो रहे हैं।'}

🔴 ज़मीन पर क्या मिला?
${form.groundReality || 'पाइपलाइन टूटी है, गड्ढे खुले हैं और लोग बुनियादी सुविधा के लिए तरस रहे हैं।'}

लगभग ${form.affectedPopulation || 'सैकड़ों परिवार'} इस लापरवाही का दंश झेल रहे हैं। स्थानीय नागरिक स्पष्ट कह रहे हैं:
"${form.citizenQuotes || 'हमने हर स्तर पर गुहार लगाई लेकिन केवल तारीखें मिलीं।'}"

हम प्रशासन (${tags}) से माँग करते हैं कि:
${form.actionDemands || '१. मौके पर तुरंत राहत प्रदान की जाए।\n२. बजट की निष्पक्ष जाँच हो।'}

क्या आपके इलाके में भी ऐसी ही स्थिति है? कमेंट में बताएं और इस रिपोर्ट को साझा कर जन-आवाज़ को मजबूत करें।`
    : `📢 Community Public Alert: ${form.headline || 'Investigative Civic Inspection'}

A critical ground situation has emerged in ${location} requiring immediate administrative intervention.

While official departments record this project as successfully completed on government dashboards, our physical verification reveals severe infrastructure failure.

🔴 Documented Official Claim:
${form.officialClaim || 'Project is operational with zero pending grievances.'}

🔴 Verified Ground Reality:
${form.groundReality || 'Dilapidated structure, waterlogging, or complete cessation of essential utility.'}

Over ${form.affectedPopulation || 'hundreds of households'} are directly suffering from this departmental neglect. Local residents stated:
"${form.citizenQuotes || 'We have submitted repeated petitions with no resolution in sight.'}"

We urge the administration (${tags}) to take decisive action:
${form.actionDemands || '1. Urgent on-site inspection and restoration.\n2. Disciplinary action against negligent contractors.'}

Is your ward experiencing similar neglect? Share your ground observations in the comments below.`;

  // LinkedIn (Editorial policy, governance, institutional analysis)
  const linkedInPost = isHi
    ? `🏛️ नीति, प्रशासन एवं नागरिक जवाबदेही: एक ज़मीनी अध्ययन

प्रशासनिक दक्षता की वास्तविक कसौटी फाइलों में नहीं, बल्कि ज़मीन पर नागरिक अनुभव में होती है।

हाल ही में ${location} में ${form.department || 'संबंधित विभाग'} से जुड़ी एक परियोजना की निष्पक्ष नागरिक पड़ताल की गई।

📊 मुख्य अवलोकन:
१. आधिकारिक पोर्टल रिकॉर्ड: "${form.officialClaim || 'योजना पूर्ण'}"
२. ज़मीनी सत्यापन: "${form.groundReality || 'कार्यान्वयन में गंभीर खामियाँ'}"
३. सामाजिक प्रभाव: ${form.affectedPopulation || 'सैकड़ों नागरिक'} बुनियादी सेवाओं से प्रभावित।

यह मामला संस्थागत निगरानी (Institutional Monitoring), तृतीय-पक्ष ऑडिट (Third-Party Auditing), और समयबद्ध शिकायत निवारण (Time-Bound Grievance Redressal) की प्रणालीगत कमियों को रेखांकित करता है।

जनहित में सुधारात्मक कदम:
${form.actionDemands || '१. स्वतंत्र तकनीकी ऑडिट\n२. पारदर्शी नागरिक निगरानी समिति की स्थापना'}

#लोकप्रशासन #सार्वजनिकनीति #गुडगवर्नेंस #जवाबदेही #पोस्टक्राफ्ट`
    : `🏛️ Governance, Policy Audit & Citizen Accountability: A Ground Inquiry

The true benchmark of public policy execution lies not on bureaucratic dashboards, but in the verified daily reality of citizens.

Our field investigation in ${location} regarding civic infrastructure overseen by ${form.department || 'the municipal authority'} reveals significant implementation variance.

📊 Core Findings:
1. Statutory Dashboard Claim: "${form.officialClaim || 'Completed and fully operational'}"
2. Physical Ground Reality: "${form.groundReality || 'Operational collapse and physical deterioration'}"
3. Demographic Impact: ${form.affectedPopulation || 'Over hundreds of citizens'} without essential civic coverage.

This case exemplifies the critical need for third-party empirical audits, outcome-based milestone disbursement, and automated escalation in citizen grievance redressal systems.

Key Policy Interventions Urged:
${form.actionDemands || '1. Independent quality appraisal\n2. Constituting an empowered citizen oversight committee'}

#PublicPolicy #GoodGovernance #CivicAccountability #InfrastructureAudit #PostCraft`;

  // Press Release (Formal, news agency wire ready)
  const pressRelease = isHi
    ? `प्रेस विज्ञप्ति | तत्काल प्रकाशनार्थ
स्थान: ${location}
दिनांक: ${now}

विषय: ${form.headline || 'जन-समस्या को लेकर स्वतंत्र ग्राउंड रिपोर्ट जारी'}

${location}: पोस्ट क्राफ्ट नागरिक पत्रकारिता मंच ने ${form.department || 'संबंधित विभाग'} की कार्यप्रणाली और ज़मीनी हक़ीक़त पर एक विस्तृत साक्ष्य-आधारित रिपोर्ट जारी की है।

रिपोर्ट के अनुसार, सरकारी पोर्टलों पर योजना को पूर्ण और चालू दर्शाया गया है, जबकि मौके की जाँच में सामने आया कि:
"${form.groundReality || 'स्थिति अत्यंत गंभीर बनी हुई है और जनता परेशान है।'}"

इस समस्या से लगभग ${form.affectedPopulation || 'सैकड़ों लोग'} सीधे प्रभावित हैं। प्रत्यक्षदर्शियों ने बताया कि शिकायतों के बावजूद कोई ठोस कदम नहीं उठाया गया।

नागरिक मंच ने ज़िला प्रशासन से निम्नलिखित माँगें की हैं:
${form.actionDemands || '१. तत्काल तकनीकी दल का निरीक्षण\n२. दोषी ठेकेदार पर दंडात्मक कार्रवाई'}

प्रशासन को समस्या समाधान हेतु ${form.deadline || '७ दिन'} की समय-सीमा दी गई है। अन्यथा नागरिक शांतिपूर्ण लोकतांत्रिक प्रदर्शन के लिए बाध्य होंगे।

संपर्क / मीडिया सेल:
पोस्ट क्राफ्ट न्यूज़रूम
ईमेल: newsroom@postcraft.org`
    : `PRESS RELEASE | FOR IMMEDIATE RELEASE
Location: ${location}
Date: ${now}

SUBJECT: INDEPENDENT GROUND INVESTIGATION DOSSIER RELEASED ON ${form.headline?.toUpperCase() || 'CIVIC DEFICIENCY'}

${location}: The PostCraft Independent Citizen Journalism Desk has released an empirical ground investigation dossier detailing serious discrepancies in public service delivery under ${form.department || 'the nodal department'}.

While administrative compliance dashboards report standard operation, on-site physical verification establishes:
"${form.groundReality || 'Severe infrastructural breakdown with prolonged service cessation.'}"

An estimated ${form.affectedPopulation || 'hundreds of households'} are impacted. Local community members testified that formal grievance petitions have gone unheeded.

The citizen forum places the following statutory demands before the district administration:
${form.actionDemands || '1. Immediate deployment of rapid inspection team.\n2. Penalty enforcement on defaulted vendors.'}

A compliance window of ${form.deadline || '7 business days'} has been requested, failing which civic representatives will escalate the matter to statutory grievance tribunals.

Press Contact:
PostCraft Editorial & Media Secretariat
Email: newsroom@postcraft.org`;

  // Policy Brief (Executive decision makers, legislative summary)
  const policyBrief = isHi
    ? `📋 नीति एवं विनियामक संक्षिप्त पत्र (Policy Brief)
शीर्षक: ${form.headline || 'जनहित बुनियादी ढाँचा ऑडिट'}
लक्षित प्राधिकारी: ${form.relevantOfficial || 'ज़िला प्रशासन एवं संबंधित मंत्रालय'}

१. समस्या का संदर्भ:
${location} में ${form.category} से संबंधित नागरिक अधिकारों और लोक सेवा वितरण का उल्लंघन।

२. डेटा विसंगति:
सरकारी दावा: ${form.officialClaim || '१००% पूर्णता'}
वास्तविक स्थिति: ${form.groundReality || 'अधूरा या गैर-कार्यात्मक'}

३. प्रणालीगत सुधार हेतु नीतिगत सिफ़ारिशें:
१. भुगतान को नागरिक संतुष्टि स्कोर (Citizen Verification OTP) से जोड़ा जाए।
२. सोशल ऑडिट एवं जनसुनवाई को अनिवार्य बनाया जाए।
३. समय-सीमा उल्लंघन पर लोक सेवा गारंटी अधिनियम के तहत स्वचालित हर्जाना लागू हो।`
    : `📋 POLICY & REGULATORY BRIEF
Title: ${form.headline || 'Civic Infrastructure Governance Assessment'}
Target Authorities: ${form.relevantOfficial || 'District Collectorate & Departmental Secretariat'}

1. Legislative & Jurisdictional Context:
Examination of public service delivery failure in ${location} concerning ${form.category}.

2. Data Asymmetry:
Statutory Portal Claim: ${form.officialClaim || '100% compliance'}
Physical Audit Verification: ${form.groundReality || 'Functional cessation'}

3. Strategic Policy Recommendations:
1. Vendor release milestones must require dual-factor geo-verified citizen sign-offs.
2. Institute mandatory quarterly social audits with open public streaming.
3. Enforce automated statutory penalty deduction under the Public Service Guarantee Act.`;

  return {
    x: xPost,
    instagram: instaPost,
    facebook: fbPost,
    linkedin: linkedInPost,
    pressRelease: pressRelease,
    policyBrief: policyBrief
  };
}
