import React from 'react';
import { 
  Search, 
  Globe, 
  Sun, 
  Moon, 
  Bell, 
  Menu, 
  Plus, 
  Sparkles,
  CheckCircle2,
  Share2
} from 'lucide-react';
import { LanguageMode, NavigationTab } from '../types';
import { locales } from '../data/locales';

interface HeaderProps {
  currentTab: NavigationTab;
  lang: LanguageMode;
  onToggleLanguage: () => void;
  isDark: boolean;
  onToggleTheme: () => void;
  onOpenMobileMenu: () => void;
  onNewReportClick: () => void;
  searchQuery: string;
  onSearchChange: (q: string) => void;
}

export const Header: React.FC<HeaderProps> = ({
  currentTab,
  lang,
  onToggleLanguage,
  isDark,
  onToggleTheme,
  onOpenMobileMenu,
  onNewReportClick,
  searchQuery,
  onSearchChange
}) => {
  const t = locales[lang];

  const getTabBreadcrumb = () => {
    switch (currentTab) {
      case 'dashboard': return t.navDashboard;
      case 'new-intake': return t.navNewIntake;
      case 'ground-report': return t.navGroundReport;
      case 'social-deck': return t.navSocialDeck;
      case 'visual-studio': return t.navVisualStudio;
      case 'library': return t.navContentLibrary;
      case 'calendar': return t.navContentCalendar;
      case 'analytics': return t.navAnalytics;
      case 'brand-settings': return t.navBrandSettings;
      default: return t.navDashboard;
    }
  };

  return (
    <header className="h-20 bg-white dark:bg-navy-dark border-b border-slate-200 dark:border-slate-800 px-4 md:px-8 flex items-center justify-between gap-4 sticky top-0 z-20 transition-colors">
      {/* Left: Mobile Toggle & Breadcrumb */}
      <div className="flex items-center gap-3 min-w-0">
        <button
          onClick={onOpenMobileMenu}
          className="md:hidden p-2 rounded-xl text-slate-600 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors"
          aria-label="Open navigation"
        >
          <Menu size={20} />
        </button>

        <div className="flex flex-col min-w-0">
          <div className="flex items-center gap-2 text-xs text-slate-500 dark:text-slate-400 font-medium">
            <span>{t.appName}</span>
            <span>/</span>
            <span className="text-navy-primary dark:text-cyan-accent font-semibold truncate">
              {getTabBreadcrumb()}
            </span>
          </div>
          <h1 className="text-base md:text-lg font-bold text-slate-900 dark:text-white truncate">
            {getTabBreadcrumb()}
          </h1>
        </div>
      </div>

      {/* Middle: Universal Search Bar */}
      <div className="hidden lg:flex items-center flex-1 max-w-md mx-4">
        <div className="relative w-full">
          <Search size={16} className="absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400" />
          <input
            type="text"
            value={searchQuery}
            onChange={(e) => onSearchChange(e.target.value)}
            placeholder={t.searchPlaceholder}
            className="w-full pl-9 pr-4 py-2 bg-slate-50 dark:bg-slate-900/60 border border-slate-200 dark:border-slate-700/80 rounded-xl text-xs text-slate-800 dark:text-slate-200 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-cyan-accent/50 transition-all"
          />
        </div>
      </div>

      {/* Right Controls: Actions & Toggles */}
      <div className="flex items-center gap-2.5">
        {/* Language Switcher Pill */}
        <button
          onClick={onToggleLanguage}
          className="flex items-center gap-2 px-3 py-1.5 rounded-full border border-slate-200 dark:border-slate-700 bg-slate-100 dark:bg-slate-800/80 hover:border-cyan-accent text-slate-800 dark:text-slate-200 text-xs font-semibold shadow-sm transition-all"
          title="Switch Language"
        >
          <Globe size={14} className="text-cyan-accent" />
          <span className="tracking-wide">
            {lang === 'hi' ? 'English' : 'हिंदी'}
          </span>
        </button>

        {/* Theme Toggle (Light / Dark) */}
        <button
          onClick={onToggleTheme}
          className="p-2 rounded-xl border border-slate-200 dark:border-slate-700 bg-slate-100 dark:bg-slate-800/80 hover:border-cyan-accent text-slate-600 dark:text-slate-300 transition-colors"
          title={isDark ? 'Switch to Light Mode' : 'Switch to Dark Mode'}
        >
          {isDark ? <Sun size={16} className="text-amber-400" /> : <Moon size={16} className="text-slate-700" />}
        </button>

        {/* Primary CTA: New Intake */}
        <button
          onClick={onNewReportClick}
          className="hidden sm:flex items-center gap-2 px-3.5 py-2 bg-cyan-accent text-slate-950 font-bold text-xs rounded-xl hover:bg-cyan-accent/90 shadow-sm transition-all"
        >
          <Plus size={15} strokeWidth={2.5} />
          <span>{t.newReportBtn}</span>
        </button>

        {/* Workspace Avatar */}
        <div className="flex items-center gap-2 pl-2 border-l border-slate-200 dark:border-slate-800">
          <div className="w-8 h-8 rounded-full bg-navy-light text-white font-bold text-xs flex items-center justify-center ring-2 ring-cyan-accent/40">
            PC
          </div>
        </div>
      </div>
    </header>
  );
};
