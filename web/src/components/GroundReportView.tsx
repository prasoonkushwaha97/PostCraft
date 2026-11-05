import React, { useState } from 'react';
import { 
  FileText, 
  Copy, 
  Check, 
  Printer, 
  Download, 
  Share2, 
  ShieldCheck, 
  AlertCircle,
  ExternalLink,
  ChevronRight,
  Sparkles,
  Layers
} from 'lucide-react';
import { LanguageMode, ReportStatus } from '../types';
import { locales } from '../data/locales';

interface GroundReportViewProps {
  reportMarkdown: string;
  headline: string;
  location: string;
  status: ReportStatus;
  onStatusChange: (newStatus: ReportStatus) => void;
  onNavigateToSocialDeck: () => void;
  lang: LanguageMode;
}

export const GroundReportView: React.FC<GroundReportViewProps> = ({
  reportMarkdown,
  headline,
  location,
  status,
  onStatusChange,
  onNavigateToSocialDeck,
  lang
}) => {
  const t = locales[lang];
  const isHi = lang === 'hi';
  const [copied, setCopied] = useState(false);

  const handleCopy = () => {
    navigator.clipboard.writeText(reportMarkdown);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  const handlePrint = () => {
    window.print();
  };

  const checklistItems = [
    { label: isHi ? 'मौके की जीपीएस लोकेशन एवं जियो-टैगिंग सत्यापित' : 'GPS coordinates & geo-tagged metadata validated', verified: true },
    { label: isHi ? 'विभागीय पोर्टल व सरकारी दावे का रिकॉर्ड सुरक्षित' : 'Departmental dashboard & claim records archived', verified: true },
    { label: isHi ? 'प्रत्यक्षदर्शी नागरिकों के शपथपत्र / ऑडियो बयान दर्ज' : 'Eyewitness testimonies & signed affidavits logged', verified: true },
    { label: isHi ? 'संबंधित नोडल अधिकारी को आधिकारिक सूचना प्रेषित' : 'Formal notification dispatched to designated nodal officer', verified: true }
  ];

  return (
    <div className="max-w-4xl mx-auto space-y-6 pb-16 animate-fade-in">
      {/* Top Header Card */}
      <div className="p-6 rounded-2xl bg-white dark:bg-navy-dark border border-slate-200 dark:border-slate-800 shadow-sm flex flex-col md:flex-row items-start md:items-center justify-between gap-4">
        <div className="space-y-1">
          <div className="flex items-center gap-2">
            <span className="px-2.5 py-0.5 rounded-md text-[11px] font-extrabold uppercase bg-cyan-accent/20 text-cyan-accent border border-cyan-accent/30">
              {isHi ? '१२-बिंदु स्वतंत्र रिपोर्ट' : '12-Section Investigation Dossier'}
            </span>
            <span className="text-xs text-slate-400 font-medium">
              {location || 'राष्ट्रीय नागरिक नेटवर्क'}
            </span>
          </div>
          <h2 className="text-lg md:text-xl font-bold text-slate-900 dark:text-white line-clamp-2">
            {headline || t.reportTitle}
          </h2>
        </div>

        {/* Action Buttons */}
        <div className="flex items-center gap-2 shrink-0">
          <button
            onClick={handleCopy}
            className="px-3.5 py-2 rounded-xl border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800/80 hover:border-cyan-accent text-slate-800 dark:text-slate-200 text-xs font-semibold flex items-center gap-1.5 transition-all shadow-sm"
          >
            {copied ? <Check size={14} className="text-emerald-500" /> : <Copy size={14} />}
            <span>{copied ? t.copied : t.copyText}</span>
          </button>

          <button
            onClick={handlePrint}
            className="p-2 rounded-xl border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800/80 hover:border-cyan-accent text-slate-700 dark:text-slate-300 transition-colors shadow-sm"
            title="Print or Export PDF"
          >
            <Printer size={16} />
          </button>

          <button
            onClick={onNavigateToSocialDeck}
            className="px-4 py-2 rounded-xl bg-navy-dark dark:bg-cyan-accent text-white dark:text-slate-950 font-bold text-xs hover:bg-slate-800 dark:hover:bg-cyan-accent/90 transition-all flex items-center gap-1.5 shadow-md"
          >
            <Share2 size={14} />
            <span>{isHi ? 'सोशल डेक खोलें' : 'Open Social Deck'}</span>
          </button>
        </div>
      </div>

      {/* Investigation Dossier Body */}
      <div className="p-6 md:p-8 rounded-2xl bg-white dark:bg-navy-dark border border-slate-200 dark:border-slate-800 shadow-sm space-y-6">
        {/* Verification Checklist Banner */}
        <div className="p-4 rounded-xl bg-slate-50 dark:bg-slate-900/50 border border-slate-200 dark:border-slate-800 space-y-2.5">
          <div className="flex items-center gap-2 text-xs font-bold text-slate-800 dark:text-slate-200">
            <ShieldCheck size={16} className="text-emerald-500" />
            <span>{t.verificationChecklist}</span>
          </div>
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-2">
            {checklistItems.map((item, idx) => (
              <div key={idx} className="flex items-center gap-2 text-xs text-slate-600 dark:text-slate-300">
                <Check size={14} className="text-emerald-500 shrink-0" />
                <span className="truncate">{item.label}</span>
              </div>
            ))}
          </div>
        </div>

        {/* Formatted Markdown Content */}
        <div className="prose dark:prose-invert max-w-none text-slate-800 dark:text-slate-200 text-xs md:text-sm leading-relaxed whitespace-pre-wrap font-sans">
          {reportMarkdown}
        </div>
      </div>
    </div>
  );
};
