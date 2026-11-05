import React, { useState } from 'react';
import { 
  Building2, 
  Tractor, 
  Landmark, 
  GraduationCap, 
  Scale, 
  MapPin, 
  FileText, 
  AlertTriangle, 
  Users, 
  Clock, 
  Sparkles, 
  ChevronDown, 
  ChevronUp, 
  Search, 
  ArrowRight,
  RotateCcw,
  CheckCircle2,
  ExternalLink
} from 'lucide-react';
import { ReportFormState, CampaignThemeId, IssueScope, LanguageMode } from '../types';
import { locales } from '../data/locales';
import { quickPresets, QuickPreset } from '../data/mockData';

interface IntakeFormProps {
  form: ReportFormState;
  onChange: (updates: Partial<ReportFormState>) => void;
  onGenerate: () => void;
  onReset: () => void;
  lang: LanguageMode;
}

export const IntakeForm: React.FC<IntakeFormProps> = ({
  form,
  onChange,
  onGenerate,
  onReset,
  lang
}) => {
  const t = locales[lang];
  const isHi = lang === 'hi';

  const [expandedSections, setExpandedSections] = useState({
    details: true,
    claimReality: true,
    evidence: true,
    demands: true
  });

  const [isSearchingGoogle, setIsSearchingGoogle] = useState(false);
  const [googleResults, setGoogleResults] = useState<{ query: string; source: string; snippet: string } | null>(null);

  const toggleSection = (key: keyof typeof expandedSections) => {
    setExpandedSections(prev => ({ ...prev, [key]: !prev[key] }));
  };

  const categories: { id: CampaignThemeId; label: string; desc: string; icon: React.ReactNode }[] = [
    { id: 'URBAN_CIVIC', label: t.catUrban, desc: t.catUrbanDesc, icon: <Building2 size={18} /> },
    { id: 'RURAL_DEV', label: t.catRural, desc: t.catRuralDesc, icon: <Tractor size={18} /> },
    { id: 'GOVERNANCE', label: t.catGov, desc: t.catGovDesc, icon: <Landmark size={18} /> },
    { id: 'EDU_HEALTH', label: t.catEduHealth, desc: t.catEduHealthDesc, icon: <GraduationCap size={18} /> },
    { id: 'LEGAL_RIGHTS', label: t.catLegal, desc: t.catLegalDesc, icon: <Scale size={18} /> },
  ];

  const scopes: { id: IssueScope; label: string }[] = [
    { id: 'WARD_VILLAGE', label: t.scopeWard },
    { id: 'CITY_BLOCK', label: t.scopeCity },
    { id: 'DISTRICT', label: t.scopeDistrict },
    { id: 'STATE_NATIONAL', label: t.scopeState }
  ];

  const handleApplyPreset = (preset: QuickPreset) => {
    const data = isHi ? preset.formDataHi : preset.formDataEn;
    onChange(data);
  };

  const handleSimulateGoogleGrounding = () => {
    setIsSearchingGoogle(true);
    setTimeout(() => {
      setIsSearchingGoogle(false);
      setGoogleResults({
        query: `${form.headline || form.localUnit || form.district} official tender grievance notice`,
        source: 'Official e-Gazette & State Grievance Redressal Portal (CPGRAMS / IGRS)',
        snippet: isHi 
          ? `विभागीय पोर्टल सं. GR-2024-91 पर कार्य की स्वीकृति एवं ₹२४.५ लाख का आबंटन दर्ज पाया गया। कार्य पूर्णता प्रमाणपत्र लंबित है।`
          : `State Portal confirms project sanction reference #GR-2024-91 with total allocation of ₹24.5 Lakhs. Completion sign-off status pending audit.`
      });
    }, 800);
  };

  return (
    <div className="max-w-4xl mx-auto space-y-6 pb-16 animate-fade-in">
      {/* Title & Quick Info */}
      <div className="flex flex-col md:flex-row items-start md:items-center justify-between gap-4">
        <div>
          <h2 className="text-xl md:text-2xl font-black text-slate-900 dark:text-white tracking-tight">
            {t.formTitle}
          </h2>
          <p className="text-xs md:text-sm text-slate-500 dark:text-slate-400 mt-1">
            {t.formSubtitle}
          </p>
        </div>

        <button
          onClick={onReset}
          className="flex items-center gap-1.5 px-3 py-1.5 rounded-lg border border-slate-300 dark:border-slate-700 text-xs font-semibold text-slate-600 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors"
        >
          <RotateCcw size={14} />
          <span>{isHi ? 'फ़ॉर्म रीसेट करें' : 'Reset Form'}</span>
        </button>
      </div>

      {/* Quick Presets Bar */}
      <div className="p-4 rounded-2xl bg-white dark:bg-navy-dark border border-slate-200 dark:border-slate-800 shadow-sm space-y-3">
        <div className="flex items-center gap-2 text-xs font-bold text-slate-700 dark:text-slate-300">
          <Sparkles size={15} className="text-cyan-accent" />
          <span>{t.quickPresetsTitle}</span>
        </div>
        <div className="flex flex-wrap gap-2">
          {quickPresets.map((preset) => (
            <button
              key={preset.id}
              onClick={() => handleApplyPreset(preset)}
              className="px-3 py-1.5 rounded-full text-xs font-semibold bg-slate-100 dark:bg-slate-800/80 hover:bg-cyan-accent/10 hover:border-cyan-accent border border-slate-200 dark:border-slate-700 text-slate-800 dark:text-slate-200 transition-all flex items-center gap-1.5"
            >
              <span>{preset.icon}</span>
              <span>{isHi ? preset.labelHi : preset.labelEn}</span>
            </button>
          ))}
        </div>
      </div>

      {/* 1. Category Selection */}
      <div className="p-5 rounded-2xl bg-white dark:bg-navy-dark border border-slate-200 dark:border-slate-800 shadow-sm space-y-4">
        <label className="block text-xs font-bold uppercase tracking-wider text-slate-500 dark:text-slate-400">
          {t.categoryLabel}
        </label>
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3">
          {categories.map((cat) => {
            const isSelected = form.category === cat.id;
            return (
              <div
                key={cat.id}
                onClick={() => onChange({ category: cat.id })}
                className={`p-3.5 rounded-xl border cursor-pointer transition-all ${
                  isSelected
                    ? 'border-cyan-accent bg-cyan-accent/5 ring-1 ring-cyan-accent shadow-sm'
                    : 'border-slate-200 dark:border-slate-800 hover:border-slate-300 dark:hover:border-slate-700 bg-slate-50/50 dark:bg-slate-900/30'
                }`}
              >
                <div className="flex items-center gap-2 mb-1.5">
                  <div className={isSelected ? 'text-cyan-accent' : 'text-slate-500'}>
                    {cat.icon}
                  </div>
                  <h4 className="text-xs font-bold text-slate-900 dark:text-white">
                    {cat.label}
                  </h4>
                </div>
                <p className="text-[11px] text-slate-500 dark:text-slate-400 line-clamp-2">
                  {cat.desc}
                </p>
              </div>
            );
          })}
        </div>
      </div>

      {/* 2. Geographic Scope */}
      <div className="p-5 rounded-2xl bg-white dark:bg-navy-dark border border-slate-200 dark:border-slate-800 shadow-sm space-y-3">
        <label className="block text-xs font-bold uppercase tracking-wider text-slate-500 dark:text-slate-400">
          {t.scopeLabel}
        </label>
        <div className="flex flex-wrap gap-2">
          {scopes.map((s) => {
            const isSelected = form.scope === s.id;
            return (
              <button
                key={s.id}
                onClick={() => onChange({ scope: s.id })}
                className={`px-4 py-2 rounded-xl text-xs font-bold transition-all ${
                  isSelected
                    ? 'bg-navy-dark text-white ring-2 ring-cyan-accent shadow-sm'
                    : 'bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 hover:bg-slate-200 dark:hover:bg-slate-700'
                }`}
              >
                {s.label}
              </button>
            );
          })}
        </div>
      </div>

      {/* Form Section 1: Details & Location */}
      <div className="rounded-2xl bg-white dark:bg-navy-dark border border-slate-200 dark:border-slate-800 shadow-sm overflow-hidden">
        <div 
          onClick={() => toggleSection('details')}
          className="p-4 md:p-5 flex items-center justify-between cursor-pointer border-b border-slate-100 dark:border-slate-800/80 bg-slate-50/50 dark:bg-slate-900/40"
        >
          <div>
            <h3 className="text-sm font-bold text-slate-900 dark:text-white">
              {t.secDetails}
            </h3>
            <p className="text-xs text-slate-500 dark:text-slate-400">
              {t.secDetailsDesc}
            </p>
          </div>
          {expandedSections.details ? <ChevronUp size={18} /> : <ChevronDown size={18} />}
        </div>

        {expandedSections.details && (
          <div className="p-5 space-y-4">
            <div>
              <label className="block text-xs font-bold text-slate-700 dark:text-slate-300 mb-1">
                {t.headlineLabel} *
              </label>
              <input
                type="text"
                value={form.headline}
                onChange={(e) => onChange({ headline: e.target.value })}
                placeholder={t.headlinePlaceholder}
                className="w-full px-3.5 py-2.5 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-xs text-slate-900 dark:text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-cyan-accent/50"
              />
            </div>

            {/* 4-column Administrative Hierarchy Grid */}
            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-3">
              <div>
                <label className="block text-xs font-semibold text-slate-600 dark:text-slate-400 mb-1">
                  {t.localUnitLabel}
                </label>
                <input
                  type="text"
                  value={form.localUnit}
                  onChange={(e) => onChange({ localUnit: e.target.value })}
                  placeholder={isHi ? "उदा. छमराछ (वार्ड ३)" : "e.g. Ward 3"}
                  className="w-full px-3 py-2 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-xs text-slate-900 dark:text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-cyan-accent/50"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-600 dark:text-slate-400 mb-1">
                  {t.subDistrictLabel}
                </label>
                <input
                  type="text"
                  value={form.subDistrict}
                  onChange={(e) => onChange({ subDistrict: e.target.value })}
                  placeholder={isHi ? "उदा. सदर तहसील" : "e.g. Sadar Tehsil"}
                  className="w-full px-3 py-2 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-xs text-slate-900 dark:text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-cyan-accent/50"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-600 dark:text-slate-400 mb-1">
                  {t.districtLabel}
                </label>
                <input
                  type="text"
                  value={form.district}
                  onChange={(e) => onChange({ district: e.target.value })}
                  placeholder={isHi ? "उदा. सोनभद्र" : "e.g. Sonbhadra"}
                  className="w-full px-3 py-2 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-xs text-slate-900 dark:text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-cyan-accent/50"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-600 dark:text-slate-400 mb-1">
                  {t.stateLabel}
                </label>
                <input
                  type="text"
                  value={form.state}
                  onChange={(e) => onChange({ state: e.target.value })}
                  placeholder={isHi ? "उदा. उत्तर प्रदेश" : "e.g. Uttar Pradesh"}
                  className="w-full px-3 py-2 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-xs text-slate-900 dark:text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-cyan-accent/50"
                />
              </div>
            </div>

            <div>
              <label className="block text-xs font-bold text-slate-700 dark:text-slate-300 mb-1">
                {t.departmentLabel}
              </label>
              <input
                type="text"
                value={form.department}
                onChange={(e) => onChange({ department: e.target.value })}
                placeholder={t.departmentPlaceholder}
                className="w-full px-3.5 py-2.5 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-xs text-slate-900 dark:text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-cyan-accent/50"
              />
            </div>
          </div>
        )}
      </div>

      {/* Form Section 2: Official Claim vs Ground Reality */}
      <div className="rounded-2xl bg-white dark:bg-navy-dark border border-slate-200 dark:border-slate-800 shadow-sm overflow-hidden">
        <div 
          onClick={() => toggleSection('claimReality')}
          className="p-4 md:p-5 flex items-center justify-between cursor-pointer border-b border-slate-100 dark:border-slate-800/80 bg-slate-50/50 dark:bg-slate-900/40"
        >
          <div>
            <h3 className="text-sm font-bold text-slate-900 dark:text-white">
              {t.secInput}
            </h3>
            <p className="text-xs text-slate-500 dark:text-slate-400">
              {t.secInputDesc}
            </p>
          </div>
          {expandedSections.claimReality ? <ChevronUp size={18} /> : <ChevronDown size={18} />}
        </div>

        {expandedSections.claimReality && (
          <div className="p-5 space-y-4">
            <div>
              <div className="flex items-center justify-between mb-1">
                <label className="text-xs font-bold text-slate-700 dark:text-slate-300 flex items-center gap-1.5">
                  <span className="w-2 h-2 rounded-full bg-blue-500" />
                  <span>{t.officialClaimLabel}</span>
                </label>
                <button
                  type="button"
                  onClick={handleSimulateGoogleGrounding}
                  className="text-[11px] font-bold text-cyan-accent hover:underline flex items-center gap-1"
                >
                  <Search size={12} />
                  <span>{isSearchingGoogle ? (isHi ? 'खोज जारी...' : 'Searching...') : (isHi ? 'सरकारी पोर्टल सत्यापन खोजें' : 'Search Portal Records')}</span>
                </button>
              </div>
              <textarea
                rows={2}
                value={form.officialClaim}
                onChange={(e) => onChange({ officialClaim: e.target.value })}
                placeholder={t.officialClaimPlaceholder}
                className="w-full px-3.5 py-2.5 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-xs text-slate-900 dark:text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-cyan-accent/50 resize-y"
              />

              {/* Google Grounding Results Card */}
              {googleResults && (
                <div className="mt-2 p-3 rounded-xl bg-emerald-50 dark:bg-emerald-950/40 border border-emerald-200 dark:border-emerald-800 text-xs text-emerald-900 dark:text-emerald-200 space-y-1 animate-fade-in">
                  <div className="flex items-center justify-between font-bold">
                    <span className="flex items-center gap-1">
                      <CheckCircle2 size={13} className="text-emerald-600 dark:text-emerald-400" />
                      <span>{googleResults.source}</span>
                    </span>
                    <span className="text-[10px] text-emerald-600 dark:text-emerald-400">Verified Citation</span>
                  </div>
                  <p className="text-[11px] leading-relaxed opacity-90">{googleResults.snippet}</p>
                </div>
              )}
            </div>

            <div>
              <label className="text-xs font-bold text-slate-700 dark:text-slate-300 flex items-center gap-1.5 mb-1">
                <span className="w-2 h-2 rounded-full bg-red-500" />
                <span>{t.groundRealityLabel} *</span>
              </label>
              <textarea
                rows={3}
                value={form.groundReality}
                onChange={(e) => onChange({ groundReality: e.target.value })}
                placeholder={t.groundRealityPlaceholder}
                className="w-full px-3.5 py-2.5 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-xs text-slate-900 dark:text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-cyan-accent/50 resize-y"
              />
            </div>
          </div>
        )}
      </div>

      {/* Form Section 3: Evidence & Testimonies */}
      <div className="rounded-2xl bg-white dark:bg-navy-dark border border-slate-200 dark:border-slate-800 shadow-sm overflow-hidden">
        <div 
          onClick={() => toggleSection('evidence')}
          className="p-4 md:p-5 flex items-center justify-between cursor-pointer border-b border-slate-100 dark:border-slate-800/80 bg-slate-50/50 dark:bg-slate-900/40"
        >
          <div>
            <h3 className="text-sm font-bold text-slate-900 dark:text-white">
              {t.secEvidence}
            </h3>
            <p className="text-xs text-slate-500 dark:text-slate-400">
              {t.secEvidenceDesc}
            </p>
          </div>
          {expandedSections.evidence ? <ChevronUp size={18} /> : <ChevronDown size={18} />}
        </div>

        {expandedSections.evidence && (
          <div className="p-5 space-y-4">
            <div>
              <label className="block text-xs font-bold text-slate-700 dark:text-slate-300 mb-1">
                {t.citizenQuotesLabel}
              </label>
              <textarea
                rows={2}
                value={form.citizenQuotes}
                onChange={(e) => onChange({ citizenQuotes: e.target.value })}
                placeholder={t.citizenQuotesPlaceholder}
                className="w-full px-3.5 py-2.5 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-xs text-slate-900 dark:text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-cyan-accent/50 resize-y"
              />
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div>
                <label className="block text-xs font-bold text-slate-700 dark:text-slate-300 mb-1">
                  {t.affectedPopulationLabel}
                </label>
                <input
                  type="text"
                  value={form.affectedPopulation}
                  onChange={(e) => onChange({ affectedPopulation: e.target.value })}
                  placeholder={t.affectedPopulationPlaceholder}
                  className="w-full px-3.5 py-2.5 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-xs text-slate-900 dark:text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-cyan-accent/50"
                />
              </div>

              <div>
                <label className="block text-xs font-bold text-slate-700 dark:text-slate-300 mb-1">
                  {t.evidenceNotesLabel}
                </label>
                <input
                  type="text"
                  value={form.evidenceNotes}
                  onChange={(e) => onChange({ evidenceNotes: e.target.value })}
                  placeholder={t.evidenceNotesPlaceholder}
                  className="w-full px-3.5 py-2.5 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-xs text-slate-900 dark:text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-cyan-accent/50"
                />
              </div>
            </div>
          </div>
        )}
      </div>

      {/* Form Section 4: Demands & Accountability */}
      <div className="rounded-2xl bg-white dark:bg-navy-dark border border-slate-200 dark:border-slate-800 shadow-sm overflow-hidden">
        <div 
          onClick={() => toggleSection('demands')}
          className="p-4 md:p-5 flex items-center justify-between cursor-pointer border-b border-slate-100 dark:border-slate-800/80 bg-slate-50/50 dark:bg-slate-900/40"
        >
          <div>
            <h3 className="text-sm font-bold text-slate-900 dark:text-white">
              {t.secDemands}
            </h3>
            <p className="text-xs text-slate-500 dark:text-slate-400">
              {t.secDemandsDesc}
            </p>
          </div>
          {expandedSections.demands ? <ChevronUp size={18} /> : <ChevronDown size={18} />}
        </div>

        {expandedSections.demands && (
          <div className="p-5 space-y-4">
            <div>
              <label className="block text-xs font-bold text-slate-700 dark:text-slate-300 mb-1">
                {t.actionDemandsLabel}
              </label>
              <textarea
                rows={2}
                value={form.actionDemands}
                onChange={(e) => onChange({ actionDemands: e.target.value })}
                placeholder={t.actionDemandsPlaceholder}
                className="w-full px-3.5 py-2.5 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-xs text-slate-900 dark:text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-cyan-accent/50 resize-y"
              />
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div>
                <label className="block text-xs font-bold text-slate-700 dark:text-slate-300 mb-1">
                  {t.deadlineLabel}
                </label>
                <input
                  type="text"
                  value={form.deadline}
                  onChange={(e) => onChange({ deadline: e.target.value })}
                  placeholder={t.deadlinePlaceholder}
                  className="w-full px-3.5 py-2.5 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-xs text-slate-900 dark:text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-cyan-accent/50"
                />
              </div>

              <div>
                <label className="block text-xs font-bold text-slate-700 dark:text-slate-300 mb-1">
                  {t.relevantOfficialLabel}
                </label>
                <input
                  type="text"
                  value={form.relevantOfficial}
                  onChange={(e) => onChange({ relevantOfficial: e.target.value })}
                  placeholder={t.relevantOfficialPlaceholder}
                  className="w-full px-3.5 py-2.5 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-xs text-slate-900 dark:text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-cyan-accent/50"
                />
              </div>
            </div>
          </div>
        )}
      </div>

      {/* Primary Generator Action Button */}
      <div className="pt-2">
        <button
          onClick={onGenerate}
          className="w-full py-4 px-6 rounded-2xl bg-navy-dark dark:bg-cyan-accent text-white dark:text-slate-950 font-black text-sm md:text-base hover:bg-slate-800 dark:hover:bg-cyan-accent/90 shadow-xl transition-all flex items-center justify-center gap-3 group"
        >
          <Sparkles size={20} className="text-cyan-accent dark:text-slate-950 group-hover:rotate-12 transition-transform" />
          <span>{t.generateStudioBtn}</span>
          <ArrowRight size={18} className="group-hover:translate-x-1.5 transition-transform" />
        </button>
      </div>
    </div>
  );
};
