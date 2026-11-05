import React, { useState } from 'react';
import { 
  Sliders, 
  Palette, 
  ShieldCheck, 
  Check, 
  Sparkles, 
  Globe, 
  Save 
} from 'lucide-react';
import { LanguageMode } from '../types';
import { locales } from '../data/locales';

interface SettingsViewProps {
  lang: LanguageMode;
}

export const SettingsView: React.FC<SettingsViewProps> = ({ lang }) => {
  const t = locales[lang];
  const isHi = lang === 'hi';

  const [saved, setSaved] = useState(false);
  const [newsroomName, setNewsroomName] = useState('PostCraft | पोस्ट क्राफ्ट');
  const [tagline, setTagline] = useState('सोशल मीडिया पोस्ट एवं ग्राउंड रिपोर्ट जनरेटर');
  const [includeWatermark, setIncludeWatermark] = useState(true);
  const [includeOfficialTags, setIncludeOfficialTags] = useState(true);

  const handleSave = () => {
    setSaved(true);
    setTimeout(() => setSaved(false), 2000);
  };

  return (
    <div className="max-w-4xl mx-auto space-y-6 pb-16 animate-fade-in">
      <div className="p-6 rounded-2xl bg-white dark:bg-navy-dark border border-slate-200 dark:border-slate-800 shadow-sm flex items-center justify-between">
        <div>
          <h2 className="text-lg md:text-xl font-bold text-slate-900 dark:text-white">
            {t.navBrandSettings}
          </h2>
          <p className="text-xs text-slate-500 dark:text-slate-400 mt-0.5">
            {isHi ? 'न्यूज़रूम ब्रांड किट, वॉटरमार्क और डिफ़ॉल्ट संपादकीय प्राथमिकताएँ' : 'Newsroom brand kit, watermarking and default editorial preferences'}
          </p>
        </div>

        <button
          onClick={handleSave}
          className="px-4 py-2 rounded-xl bg-cyan-accent text-slate-950 font-bold text-xs hover:bg-cyan-accent/90 transition-all flex items-center gap-1.5 shadow-sm"
        >
          {saved ? <Check size={14} /> : <Save size={14} />}
          <span>{saved ? t.copied : t.saveDraft}</span>
        </button>
      </div>

      {/* Brand Identity Settings */}
      <div className="p-6 rounded-2xl bg-white dark:bg-navy-dark border border-slate-200 dark:border-slate-800 shadow-sm space-y-4">
        <h3 className="text-sm font-bold text-slate-900 dark:text-white flex items-center gap-2">
          <Palette size={16} className="text-cyan-accent" />
          <span>{isHi ? 'ब्रांड पहचान एवं वॉटरमार्क' : 'Brand Identity & Watermark'}</span>
        </h3>

        <div className="space-y-4">
          <div>
            <label className="block text-xs font-semibold text-slate-700 dark:text-slate-300 mb-1">
              {isHi ? 'न्यूज़रूम / संस्था का नाम' : 'Newsroom / Brand Label'}
            </label>
            <input
              type="text"
              value={newsroomName}
              onChange={(e) => setNewsroomName(e.target.value)}
              className="w-full px-3 py-2 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-xs text-slate-900 dark:text-white focus:outline-none focus:ring-2 focus:ring-cyan-accent/50"
            />
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-700 dark:text-slate-300 mb-1">
              {isHi ? 'आधिकारिक टैगलाइन' : 'Official Tagline'}
            </label>
            <input
              type="text"
              value={tagline}
              onChange={(e) => setTagline(e.target.value)}
              className="w-full px-3 py-2 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-xs text-slate-900 dark:text-white focus:outline-none focus:ring-2 focus:ring-cyan-accent/50"
            />
          </div>

          <div className="pt-2 space-y-2.5">
            <label className="flex items-center gap-2 text-xs font-semibold text-slate-800 dark:text-slate-200 cursor-pointer">
              <input
                type="checkbox"
                checked={includeWatermark}
                onChange={(e) => setIncludeWatermark(e.target.checked)}
                className="rounded border-slate-300 text-cyan-accent focus:ring-cyan-accent"
              />
              <span>{isHi ? 'एक्सपोर्ट कार्ड्स में पोस्ट क्राफ्ट 3D लोगो वॉटरमार्क शामिल करें' : 'Include PostCraft 3D Logo watermark in exported visual cards'}</span>
            </label>

            <label className="flex items-center gap-2 text-xs font-semibold text-slate-800 dark:text-slate-200 cursor-pointer">
              <input
                type="checkbox"
                checked={includeOfficialTags}
                onChange={(e) => setIncludeOfficialTags(e.target.checked)}
                className="rounded border-slate-300 text-cyan-accent focus:ring-cyan-accent"
              />
              <span>{isHi ? 'सोशल पोस्ट ड्राफ्ट्स में स्वतः आधिकारिक नोडल हैंडल जोड़ें' : 'Automatically append designated official handles to generated drafts'}</span>
            </label>
          </div>
        </div>
      </div>
    </div>
  );
};
