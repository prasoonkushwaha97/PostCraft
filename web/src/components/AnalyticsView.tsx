import React from 'react';
import { 
  BarChart3, 
  TrendingUp, 
  Users, 
  Share2, 
  CheckCircle2, 
  MapPin, 
  Layers 
} from 'lucide-react';
import { LanguageMode } from '../types';
import { locales } from '../data/locales';

interface AnalyticsViewProps {
  lang: LanguageMode;
}

export const AnalyticsView: React.FC<AnalyticsViewProps> = ({ lang }) => {
  const t = locales[lang];
  const isHi = lang === 'hi';

  const categoryBreakdown = [
    { label: isHi ? 'शहरी नागरिक अवसंरचना' : 'Urban Civic & Infrastructure', percent: 38, count: 24 },
    { label: isHi ? 'ग्रामीण विकास एवं नल-जल' : 'Rural Development & Water', percent: 28, count: 18 },
    { label: isHi ? 'शिक्षा एवं प्राथमिक स्वास्थ्य' : 'Education & Healthcare', percent: 18, count: 12 },
    { label: isHi ? 'कानून, राशन एवं जन-अधिकार' : 'Legal & Public Rights', percent: 16, count: 10 },
  ];

  const districtReach = [
    { district: 'सोनभद्र (Sonbhadra)', issues: 8, reach: '42,000+' },
    { district: 'कानपुर नगर (Kanpur)', issues: 12, reach: '58,000+' },
    { district: 'मिर्ज़ापुर (Mirzapur)', issues: 6, reach: '24,000+' },
    { district: 'प्रयागराज (Prayagraj)', issues: 9, reach: '38,000+' },
  ];

  return (
    <div className="max-w-4xl mx-auto space-y-6 pb-16 animate-fade-in">
      <div className="p-6 rounded-2xl bg-white dark:bg-navy-dark border border-slate-200 dark:border-slate-800 shadow-sm flex items-center justify-between">
        <div>
          <h2 className="text-lg md:text-xl font-bold text-slate-900 dark:text-white">
            {t.navAnalytics}
          </h2>
          <p className="text-xs text-slate-500 dark:text-slate-400 mt-0.5">
            {isHi ? 'नागरिक ग्राउंड रिपोर्टिंग का प्रभाव, रीच और प्रशासनिक समाधान दर' : 'Real ground reporting reach, verification stats and administrative response rate'}
          </p>
        </div>

        <div className="flex items-center gap-2">
          <span className="px-3 py-1.5 rounded-xl bg-emerald-100 text-emerald-800 dark:bg-emerald-950/60 dark:text-emerald-400 font-bold text-xs flex items-center gap-1">
            <TrendingUp size={14} />
            <span>+24% Impact MoM</span>
          </span>
        </div>
      </div>

      {/* Category Breakdown */}
      <div className="p-6 rounded-2xl bg-white dark:bg-navy-dark border border-slate-200 dark:border-slate-800 shadow-sm space-y-4">
        <h3 className="text-sm font-bold text-slate-900 dark:text-white">
          {isHi ? 'श्रेणी अनुसार मुद्दे एवं रिपोर्टिंग वितरण' : 'Issue Distribution by Category'}
        </h3>

        <div className="space-y-3">
          {categoryBreakdown.map((item, idx) => (
            <div key={idx} className="space-y-1.5">
              <div className="flex justify-between text-xs font-semibold text-slate-700 dark:text-slate-300">
                <span>{item.label}</span>
                <span className="font-bold">{item.count} {isHi ? 'रिपोर्ट्स' : 'Reports'} ({item.percent}%)</span>
              </div>
              <div className="h-2.5 w-full bg-slate-100 dark:bg-slate-800 rounded-full overflow-hidden">
                <div 
                  className="h-full bg-gradient-to-r from-secondary-blue to-cyan-accent rounded-full transition-all duration-500" 
                  style={{ width: `${item.percent}%` }}
                />
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* District Reach Table */}
      <div className="p-6 rounded-2xl bg-white dark:bg-navy-dark border border-slate-200 dark:border-slate-800 shadow-sm space-y-4">
        <h3 className="text-sm font-bold text-slate-900 dark:text-white">
          {isHi ? 'ज़िलावार ग्राउंड कवरेज एवं सत्यापित नागरिक रीच' : 'District Coverage & Verified Reach'}
        </h3>

        <div className="divide-y divide-slate-100 dark:divide-slate-800">
          {districtReach.map((item, idx) => (
            <div key={idx} className="py-3 flex items-center justify-between text-xs">
              <div className="flex items-center gap-2 text-slate-900 dark:text-white font-semibold">
                <MapPin size={14} className="text-cyan-accent" />
                <span>{item.district}</span>
              </div>
              <div className="flex items-center gap-4 text-slate-500 dark:text-slate-400">
                <span>{item.issues} {isHi ? 'मुद्दे दर्ज' : 'Filed'}</span>
                <span className="font-bold text-slate-900 dark:text-white">{item.reach}</span>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};
