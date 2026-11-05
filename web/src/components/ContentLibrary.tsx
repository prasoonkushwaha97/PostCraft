import React, { useState } from 'react';
import { 
  FolderArchive, 
  Search, 
  Filter, 
  Trash2, 
  ExternalLink, 
  FileText, 
  Clock, 
  MapPin, 
  Share2,
  ChevronRight,
  ShieldCheck,
  CheckCircle2
} from 'lucide-react';
import { SavedReport, LanguageMode, ReportStatus, NavigationTab } from '../types';
import { locales } from '../data/locales';

interface ContentLibraryProps {
  reports: SavedReport[];
  onSelectReport: (report: SavedReport) => void;
  onDeleteReport: (id: string) => void;
  onNavigateTab: (tab: NavigationTab) => void;
  lang: LanguageMode;
}

export const ContentLibrary: React.FC<ContentLibraryProps> = ({
  reports,
  onSelectReport,
  onDeleteReport,
  onNavigateTab,
  lang
}) => {
  const t = locales[lang];
  const isHi = lang === 'hi';

  const [search, setSearch] = useState('');
  const [statusFilter, setStatusFilter] = useState<string>('ALL');

  const filteredReports = reports.filter((r) => {
    const matchesSearch = 
      (r.formData.headline || '').toLowerCase().includes(search.toLowerCase()) ||
      (r.formData.localUnit || '').toLowerCase().includes(search.toLowerCase()) ||
      (r.formData.district || '').toLowerCase().includes(search.toLowerCase()) ||
      (r.formData.department || '').toLowerCase().includes(search.toLowerCase());
    
    const matchesStatus = statusFilter === 'ALL' || r.status === statusFilter;

    return matchesSearch && matchesStatus;
  });

  return (
    <div className="max-w-5xl mx-auto space-y-6 pb-16 animate-fade-in">
      {/* Top Header Card */}
      <div className="p-6 rounded-2xl bg-white dark:bg-navy-dark border border-slate-200 dark:border-slate-800 shadow-sm flex flex-col md:flex-row items-start md:items-center justify-between gap-4">
        <div>
          <h2 className="text-lg md:text-xl font-bold text-slate-900 dark:text-white">
            {t.libraryTitle}
          </h2>
          <p className="text-xs text-slate-500 dark:text-slate-400 mt-0.5">
            {t.librarySubtitle}
          </p>
        </div>

        <div className="flex items-center gap-2">
          <span className="px-3 py-1.5 rounded-xl bg-slate-100 dark:bg-slate-800 text-xs font-bold text-slate-700 dark:text-slate-300">
            {filteredReports.length} {isHi ? 'डोजियर' : 'Dossiers'}
          </span>
        </div>
      </div>

      {/* Filter and Search Bar */}
      <div className="p-4 rounded-2xl bg-white dark:bg-navy-dark border border-slate-200 dark:border-slate-800 shadow-sm flex flex-col sm:flex-row gap-3">
        <div className="relative flex-1">
          <Search size={15} className="absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400" />
          <input
            type="text"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            placeholder={isHi ? 'शीर्षक, स्थान या विभाग से खोजें...' : 'Search by headline, location or agency...'}
            className="w-full pl-9 pr-4 py-2 rounded-xl border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-900/60 text-xs text-slate-900 dark:text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-cyan-accent/50"
          />
        </div>

        <div className="flex items-center gap-2">
          <Filter size={15} className="text-slate-400 shrink-0" />
          <select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            className="px-3 py-2 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-xs text-slate-800 dark:text-slate-200 focus:outline-none focus:ring-2 focus:ring-cyan-accent/50"
          >
            <option value="ALL">{isHi ? 'सभी स्थितियाँ' : 'All Statuses'}</option>
            <option value="UNDER_INVESTIGATION">{isHi ? 'जाँच जारी' : 'Under Investigation'}</option>
            <option value="IN_REVIEW">{isHi ? 'समीक्षाधीन' : 'In Review'}</option>
            <option value="RESOLVED">{isHi ? 'समाधान संपन्न' : 'Resolved'}</option>
          </select>
        </div>
      </div>

      {/* Dossiers Grid */}
      <div className="space-y-3">
        {filteredReports.length === 0 ? (
          <div className="p-12 text-center rounded-2xl bg-white dark:bg-navy-dark border border-slate-200 dark:border-slate-800 text-slate-400 space-y-2">
            <FolderArchive size={36} className="mx-auto text-slate-300 dark:text-slate-600" />
            <p className="text-sm font-semibold">{isHi ? 'कोई रिपोर्ट नहीं मिली' : 'No dossiers found matching filter'}</p>
          </div>
        ) : (
          filteredReports.map((report) => (
            <div
              key={report.id}
              className="p-5 rounded-2xl bg-white dark:bg-navy-dark border border-slate-200 dark:border-slate-800 hover:border-cyan-accent/50 shadow-sm transition-all flex flex-col md:flex-row items-start md:items-center justify-between gap-4"
            >
              <div className="space-y-1.5 flex-1 min-w-0">
                <div className="flex items-center gap-2 flex-wrap">
                  <span className={`px-2.5 py-0.5 rounded text-[10px] font-extrabold uppercase ${
                    report.status === 'RESOLVED' 
                      ? 'bg-emerald-100 text-emerald-800 dark:bg-emerald-950/60 dark:text-emerald-400' 
                      : report.status === 'IN_REVIEW'
                      ? 'bg-amber-100 text-amber-800 dark:bg-amber-950/60 dark:text-amber-400'
                      : 'bg-blue-100 text-blue-800 dark:bg-blue-950/60 dark:text-blue-400'
                  }`}>
                    {report.status === 'RESOLVED' ? t.statusResolved : report.status === 'IN_REVIEW' ? t.statusReview : t.statusInvestigation}
                  </span>
                  <span className="text-[11px] text-slate-400 flex items-center gap-1">
                    <Clock size={12} />
                    {new Date(report.createdAt).toLocaleDateString()}
                  </span>
                  <span className="text-[11px] font-semibold text-cyan-accent">
                    {report.formData.category}
                  </span>
                </div>

                <h3 className="font-bold text-slate-900 dark:text-white text-sm line-clamp-1">
                  {report.formData.headline || 'जन-मुद्दा रिपोर्ट'}
                </h3>

                <div className="flex items-center gap-3 text-xs text-slate-500 dark:text-slate-400 flex-wrap">
                  <span className="flex items-center gap-1">
                    <MapPin size={13} />
                    {[report.formData.localUnit, report.formData.district, report.formData.state].filter(Boolean).join(', ')}
                  </span>
                  <span>•</span>
                  <span>{report.formData.department || 'संबंधित विभाग'}</span>
                </div>
              </div>

              {/* Action Buttons */}
              <div className="flex items-center gap-2 shrink-0 self-end md:self-center">
                <button
                  onClick={() => {
                    onSelectReport(report);
                    onNavigateTab('ground-report');
                  }}
                  className="px-3.5 py-1.5 rounded-xl bg-slate-100 dark:bg-slate-800 hover:bg-slate-200 dark:hover:bg-slate-700 text-slate-800 dark:text-slate-200 text-xs font-bold transition-all flex items-center gap-1.5"
                >
                  <FileText size={14} />
                  <span>{isHi ? 'रिपोर्ट देखें' : 'View Report'}</span>
                </button>

                <button
                  onClick={() => {
                    onSelectReport(report);
                    onNavigateTab('social-deck');
                  }}
                  className="px-3.5 py-1.5 rounded-xl bg-navy-dark dark:bg-cyan-accent text-white dark:text-slate-950 text-xs font-bold transition-all flex items-center gap-1.5 shadow-sm"
                >
                  <Share2 size={14} />
                  <span>{isHi ? 'सोशल डेक' : 'Social Deck'}</span>
                </button>

                <button
                  onClick={() => onDeleteReport(report.id)}
                  className="p-1.5 rounded-lg text-slate-400 hover:text-rose-500 hover:bg-rose-50 dark:hover:bg-rose-950/40 transition-colors"
                  title="Delete Dossier"
                >
                  <Trash2 size={16} />
                </button>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
};
