import React from 'react';
import { 
  FileText, 
  Share2, 
  CheckCircle2, 
  Users, 
  ArrowUpRight, 
  Sparkles, 
  AlertCircle, 
  ChevronRight,
  PlusCircle,
  Clock,
  MapPin,
  ExternalLink
} from 'lucide-react';
import { SavedReport, LanguageMode, NavigationTab } from '../types';
import { locales } from '../data/locales';

interface DashboardProps {
  reports: SavedReport[];
  onSelectReport: (report: SavedReport) => void;
  onNavigateTab: (tab: NavigationTab) => void;
  lang: LanguageMode;
}

export const Dashboard: React.FC<DashboardProps> = ({
  reports,
  onSelectReport,
  onNavigateTab,
  lang
}) => {
  const t = locales[lang];
  const isHi = lang === 'hi';

  const stats = [
    {
      title: isHi ? 'सक्रिय ग्राउंड जाँच' : 'Active Investigations',
      value: '14',
      change: '+3 this week',
      icon: FileText,
      color: 'text-cyan-accent',
      bgColor: 'bg-cyan-accent/10'
    },
    {
      title: isHi ? 'प्रकाशित सोशल पोस्ट्स' : 'Social Campaigns Formatted',
      value: '52',
      change: 'X, Insta, FB, LI',
      icon: Share2,
      color: 'text-blue-500',
      bgColor: 'bg-blue-500/10'
    },
    {
      title: isHi ? 'समाधान संपन्न मुद्दे' : 'Civic Issues Resolved',
      value: '9',
      change: 'Official impact verified',
      icon: CheckCircle2,
      color: 'text-emerald-500',
      bgColor: 'bg-emerald-500/10'
    },
    {
      title: isHi ? 'अनुमानित नागरिक रीच' : 'Verified Civic Reach',
      value: '148K',
      change: '15 Districts covered',
      icon: Users,
      color: 'text-amber-500',
      bgColor: 'bg-amber-500/10'
    }
  ];

  return (
    <div className="space-y-6 pb-12 animate-fade-in">
      {/* Editorial Hero Banner */}
      <div className="relative rounded-2xl bg-gradient-to-r from-navy-dark via-navy-light to-navy-dark border border-slate-700/60 p-6 md:p-8 text-white shadow-xl overflow-hidden">
        <div className="absolute right-0 top-0 bottom-0 w-1/3 opacity-10 bg-radial pointer-events-none" />
        <div className="relative z-10 max-w-2xl space-y-3">
          <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-cyan-accent/20 border border-cyan-accent/40 text-cyan-accent text-xs font-bold tracking-wide">
            <Sparkles size={14} />
            <span>{isHi ? 'डिजिटल सिटीजन न्यूज़रूम' : 'Digital Citizen Newsroom'}</span>
          </div>
          <h2 className="text-2xl md:text-3xl font-black tracking-tight">
            {t.appName} - {t.tagline}
          </h2>
          <p className="text-sm md:text-base text-slate-300 font-normal leading-relaxed">
            {t.brandPositioning}. {isHi 
              ? 'ग्राम, वार्ड और शहर स्तर की समस्याओं के साक्ष्य दर्ज करें और तुरंत १२-बिंदु स्वतंत्र रिपोर्ट व सभी सोशल मीडिया प्रारूप तैयार करें।'
              : 'Transform raw ground intelligence, official discrepancies, and eyewitness testimony into publication-ready dossiers and social media decks.'}
          </p>
          <div className="pt-2 flex flex-wrap gap-3">
            <button
              onClick={() => onNavigateTab('new-intake')}
              className="px-4 py-2.5 rounded-xl bg-cyan-accent text-slate-950 font-bold text-xs hover:bg-cyan-accent/90 transition-all flex items-center gap-2 shadow-md shadow-cyan-accent/20"
            >
              <PlusCircle size={16} />
              <span>{isHi ? 'नई ग्राउंड रिपोर्ट दर्ज करें' : 'File New Ground Report'}</span>
            </button>
            <button
              onClick={() => onNavigateTab('visual-studio')}
              className="px-4 py-2.5 rounded-xl bg-slate-800/80 hover:bg-slate-700/80 border border-slate-600/80 text-white font-semibold text-xs transition-all flex items-center gap-2"
            >
              <span>{isHi ? 'विजुअल कार्ड स्टूडियो खोलें' : 'Open Visual Card Studio'}</span>
              <ArrowUpRight size={15} />
            </button>
          </div>
        </div>
      </div>

      {/* KPI Cards Grid */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {stats.map((stat, i) => {
          const Icon = stat.icon;
          return (
            <div 
              key={i} 
              className="p-5 rounded-2xl bg-white dark:bg-navy-dark border border-slate-200 dark:border-slate-800 shadow-sm hover:shadow-md transition-all flex items-center justify-between"
            >
              <div className="space-y-1">
                <span className="text-xs font-semibold text-slate-500 dark:text-slate-400">
                  {stat.title}
                </span>
                <div className="text-2xl font-black text-slate-900 dark:text-white">
                  {stat.value}
                </div>
                <span className="text-[11px] font-medium text-slate-400 dark:text-slate-500">
                  {stat.change}
                </span>
              </div>
              <div className={`w-12 h-12 rounded-xl flex items-center justify-center shrink-0 ${stat.bgColor} ${stat.color}`}>
                <Icon size={24} />
              </div>
            </div>
          );
        })}
      </div>

      {/* 2-Column Section: Quick Action Hub & Recent Dossiers */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Left 2 Cols: Recent Ground Investigations */}
        <div className="lg:col-span-2 space-y-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-2">
              <FileText className="text-navy-primary dark:text-cyan-accent" size={18} />
              <h3 className="font-bold text-slate-900 dark:text-white text-base">
                {isHi ? 'हालिया ग्राउंड इन्वेस्टिगेशन डोजियर' : 'Recent Ground Investigation Dossiers'}
              </h3>
            </div>
            <button
              onClick={() => onNavigateTab('library')}
              className="text-xs font-bold text-cyan-accent hover:underline flex items-center gap-1"
            >
              <span>{isHi ? 'सभी देखें' : 'View All'}</span>
              <ChevronRight size={14} />
            </button>
          </div>

          <div className="space-y-3">
            {reports.map((report) => (
              <div
                key={report.id}
                onClick={() => {
                  onSelectReport(report);
                  onNavigateTab('ground-report');
                }}
                className="group p-4 rounded-xl bg-white dark:bg-navy-dark border border-slate-200 dark:border-slate-800 hover:border-cyan-accent/50 dark:hover:border-cyan-accent/50 shadow-sm hover:shadow-md transition-all cursor-pointer flex flex-col sm:flex-row items-start sm:items-center justify-between gap-3"
              >
                <div className="space-y-1.5 min-w-0 flex-1">
                  <div className="flex items-center gap-2">
                    <span className={`px-2 py-0.5 rounded text-[10px] font-extrabold uppercase ${
                      report.status === 'RESOLVED' 
                        ? 'bg-emerald-100 text-emerald-800 dark:bg-emerald-950/60 dark:text-emerald-400' 
                        : report.status === 'IN_REVIEW'
                        ? 'bg-amber-100 text-amber-800 dark:bg-amber-950/60 dark:text-amber-400'
                        : 'bg-blue-100 text-blue-800 dark:bg-blue-950/60 dark:text-blue-400'
                    }`}>
                      {report.status === 'RESOLVED' 
                        ? (isHi ? 'समाधान' : 'Resolved') 
                        : report.status === 'IN_REVIEW' 
                        ? (isHi ? 'समीक्षाधीन' : 'In Review')
                        : (isHi ? 'जाँच जारी' : 'Investigation')}
                    </span>
                    <span className="text-[11px] text-slate-400 flex items-center gap-1">
                      <Clock size={12} />
                      {new Date(report.createdAt).toLocaleDateString()}
                    </span>
                  </div>

                  <h4 className="font-bold text-slate-900 dark:text-white text-sm line-clamp-1 group-hover:text-cyan-accent transition-colors">
                    {report.formData.headline || 'जन-मुद्दा रिपोर्ट'}
                  </h4>

                  <div className="flex items-center gap-3 text-xs text-slate-500 dark:text-slate-400">
                    <span className="flex items-center gap-1">
                      <MapPin size={13} className="text-slate-400" />
                      {report.formData.localUnit || report.formData.district || 'स्थानीय क्षेत्र'}
                    </span>
                    <span>•</span>
                    <span className="truncate max-w-[200px]">
                      {report.formData.department || 'संबंधित विभाग'}
                    </span>
                  </div>
                </div>

                <div className="flex items-center gap-2 shrink-0 self-end sm:self-center">
                  <span className="text-xs font-semibold text-slate-400 group-hover:text-cyan-accent transition-colors">
                    {isHi ? 'डोजियर खोलें' : 'Open Dossier'}
                  </span>
                  <ChevronRight size={16} className="text-slate-400 group-hover:translate-x-1 group-hover:text-cyan-accent transition-all" />
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Right 1 Col: Quick Feature Shortcuts */}
        <div className="space-y-4">
          <h3 className="font-bold text-slate-900 dark:text-white text-base">
            {isHi ? 'त्वरित न्यूज़रूम एक्शन' : 'Newsroom Action Studio'}
          </h3>

          <div className="space-y-3">
            <div 
              onClick={() => onNavigateTab('new-intake')}
              className="p-4 rounded-xl bg-white dark:bg-navy-dark border border-slate-200 dark:border-slate-800 hover:border-cyan-accent transition-all cursor-pointer group shadow-sm"
            >
              <div className="flex items-center justify-between mb-2">
                <span className="text-xs font-bold text-cyan-accent">01. जन-मुद्दा फ़ॉर्म</span>
                <PlusCircle size={16} className="text-slate-400 group-hover:text-cyan-accent transition-colors" />
              </div>
              <h4 className="font-bold text-slate-900 dark:text-white text-sm mb-1">
                {isHi ? 'नया मुद्दा दर्ज करें' : 'Intake Civic Incident'}
              </h4>
              <p className="text-xs text-slate-500 dark:text-slate-400">
                {isHi ? 'सरकारी दावा बनाम ज़मीनी हक़ीक़त और साक्ष्य दर्ज करें' : 'Record official claim vs ground reality with eyewitness evidence.'}
              </p>
            </div>

            <div 
              onClick={() => onNavigateTab('social-deck')}
              className="p-4 rounded-xl bg-white dark:bg-navy-dark border border-slate-200 dark:border-slate-800 hover:border-cyan-accent transition-all cursor-pointer group shadow-sm"
            >
              <div className="flex items-center justify-between mb-2">
                <span className="text-xs font-bold text-blue-500">02. सोशल मीडिया डेक</span>
                <Share2 size={16} className="text-slate-400 group-hover:text-blue-500 transition-colors" />
              </div>
              <h4 className="font-bold text-slate-900 dark:text-white text-sm mb-1">
                {isHi ? '६ प्लेटफ़ॉर्म पोस्ट्स तैयार करें' : 'Multi-Platform Social Deck'}
              </h4>
              <p className="text-xs text-slate-500 dark:text-slate-400">
                {isHi ? 'X, Instagram, Facebook, LinkedIn, प्रेस नोट और पॉलिसी ब्रीफ' : 'Tailored high-impact content for X, Instagram, Facebook, LinkedIn & PR.'}
              </p>
            </div>

            <div 
              onClick={() => onNavigateTab('visual-studio')}
              className="p-4 rounded-xl bg-white dark:bg-navy-dark border border-slate-200 dark:border-slate-800 hover:border-cyan-accent transition-all cursor-pointer group shadow-sm"
            >
              <div className="flex items-center justify-between mb-2">
                <span className="text-xs font-bold text-amber-500">03. विजुअल न्यूज़ कार्ड</span>
                <ExternalLink size={16} className="text-slate-400 group-hover:text-amber-500 transition-colors" />
              </div>
              <h4 className="font-bold text-slate-900 dark:text-white text-sm mb-1">
                {isHi ? '१०८०x१३५० न्यूज़ कार्ड बनाएँ' : '1080x1350 Graphic Card Studio'}
              </h4>
              <p className="text-xs text-slate-500 dark:text-slate-400">
                {isHi ? 'सोशल मीडिया शेयरिंग हेतु ब्रैंडेड ग्राफिक कार्ड एक क्लिक में डाउनलोड करें' : 'Export verified news-style graphic cards for viral social advocacy.'}
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
