import React, { useState, useEffect } from 'react';
import { NavigationTab, LanguageMode, ReportFormState, GeneratedPostBundle, SavedReport, ReportStatus, SocialPlatformId } from './types';
import { Sidebar } from './components/Sidebar';
import { Header } from './components/Header';
import { Dashboard } from './components/Dashboard';
import { IntakeForm } from './components/IntakeForm';
import { GroundReportView } from './components/GroundReportView';
import { SocialDeck } from './components/SocialDeck';
import { VisualCardStudio } from './components/VisualCardStudio';
import { ContentLibrary } from './components/ContentLibrary';
import { ContentCalendar } from './components/ContentCalendar';
import { AnalyticsView } from './components/AnalyticsView';
import { SettingsView } from './components/SettingsView';
import { quickPresets, initialMockReports } from './data/mockData';
import { generateGroundReport, generateSocialBundle } from './data/generator';

const STORAGE_KEY_REPORTS = 'postcraft_reports_v2';
const STORAGE_KEY_LANG = 'postcraft_lang_v2';
const STORAGE_KEY_THEME = 'postcraft_theme_v2';

export const App: React.FC = () => {
  // Navigation & UI States
  const [currentTab, setCurrentTab] = useState<NavigationTab>('dashboard');
  const [lang, setLang] = useState<LanguageMode>(() => {
    return (localStorage.getItem(STORAGE_KEY_LANG) as LanguageMode) || 'hi';
  });
  const [isDark, setIsDark] = useState<boolean>(() => {
    return localStorage.getItem(STORAGE_KEY_THEME) === 'dark';
  });
  const [sidebarCollapsed, setSidebarCollapsed] = useState(false);
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');

  // Active Report Form State
  const initialPreset = quickPresets[0];
  const [form, setForm] = useState<ReportFormState>(() => {
    return (lang === 'hi' ? initialPreset.formDataHi : initialPreset.formDataEn) as ReportFormState;
  });

  // Generated Artifacts
  const [reportMarkdown, setReportMarkdown] = useState<string>(() => generateGroundReport(form, lang));
  const [socialBundle, setSocialBundle] = useState<GeneratedPostBundle>(() => generateSocialBundle(form, lang));
  const [reportStatus, setReportStatus] = useState<ReportStatus>('UNDER_INVESTIGATION');

  // Dossiers Archive
  const [savedReports, setSavedReports] = useState<SavedReport[]>(() => {
    try {
      const saved = localStorage.getItem(STORAGE_KEY_REPORTS);
      if (saved) return JSON.parse(saved);
    } catch (e) {
      // Fallback
    }
    return initialMockReports;
  });

  // Sync Theme to HTML root
  useEffect(() => {
    if (isDark) {
      document.documentElement.classList.add('dark');
      localStorage.setItem(STORAGE_KEY_THEME, 'dark');
    } else {
      document.documentElement.classList.remove('dark');
      localStorage.setItem(STORAGE_KEY_THEME, 'light');
    }
  }, [isDark]);

  // Sync Language
  useEffect(() => {
    localStorage.setItem(STORAGE_KEY_LANG, lang);
  }, [lang]);

  // Save to LocalStorage
  useEffect(() => {
    try {
      localStorage.setItem(STORAGE_KEY_REPORTS, JSON.stringify(savedReports));
    } catch (e) {}
  }, [savedReports]);

  // Handle Form Change
  const handleFormChange = (updates: Partial<ReportFormState>) => {
    setForm(prev => {
      const next = { ...prev, ...updates };
      return next;
    });
  };

  // Trigger Generation
  const handleGenerate = () => {
    const md = generateGroundReport(form, lang);
    const bundle = generateSocialBundle(form, lang);
    setReportMarkdown(md);
    setSocialBundle(bundle);

    // Save as new or update existing in library
    const newReport: SavedReport = {
      id: `report-${Date.now()}`,
      createdAt: Date.now(),
      status: 'UNDER_INVESTIGATION',
      formData: { ...form },
      generatedReport: md,
      socialBundle: bundle
    };

    setSavedReports(prev => [newReport, ...prev]);
    setCurrentTab('ground-report');
  };

  const handleResetForm = () => {
    const fresh = lang === 'hi' ? initialPreset.formDataHi : initialPreset.formDataEn;
    setForm(fresh as ReportFormState);
  };

  const handleToggleLanguage = () => {
    const nextLang: LanguageMode = lang === 'hi' ? 'en' : 'hi';
    setLang(nextLang);
    // Regenerate current artifacts for new language
    setReportMarkdown(generateGroundReport(form, nextLang));
    setSocialBundle(generateSocialBundle(form, nextLang));
  };

  const handleSelectReport = (report: SavedReport) => {
    setForm({ ...report.formData });
    setReportStatus(report.status);
    setReportMarkdown(report.generatedReport || generateGroundReport(report.formData, lang));
    setSocialBundle(report.socialBundle || generateSocialBundle(report.formData, lang));
  };

  const handleDeleteReport = (id: string) => {
    setSavedReports(prev => prev.filter(r => r.id !== id));
  };

  const handleUpdateSocialDraft = (platform: SocialPlatformId, text: string) => {
    setSocialBundle(prev => ({ ...prev, [platform]: text }));
  };

  return (
    <div className="flex h-screen bg-slate-50 dark:bg-slate-950 font-sans overflow-hidden transition-colors">
      {/* Desktop Sidebar */}
      <div className="hidden md:flex shrink-0 h-full">
        <Sidebar
          currentTab={currentTab}
          onSelectTab={(tab) => setCurrentTab(tab)}
          collapsed={sidebarCollapsed}
          onToggleCollapse={() => setSidebarCollapsed(!sidebarCollapsed)}
          lang={lang}
        />
      </div>

      {/* Mobile Drawer Backdrop */}
      {mobileMenuOpen && (
        <div 
          onClick={() => setMobileMenuOpen(false)}
          className="fixed inset-0 bg-slate-950/60 backdrop-blur-sm z-40 md:hidden animate-fade-in"
        />
      )}

      {/* Mobile Drawer */}
      <div className={`fixed inset-y-0 left-0 z-50 md:hidden transition-transform duration-300 ${
        mobileMenuOpen ? 'translate-x-0' : '-translate-x-full'
      }`}>
        <Sidebar
          currentTab={currentTab}
          onSelectTab={(tab) => {
            setCurrentTab(tab);
            setMobileMenuOpen(false);
          }}
          collapsed={false}
          onToggleCollapse={() => setMobileMenuOpen(false)}
          lang={lang}
        />
      </div>

      {/* Main Content Area */}
      <div className="flex-1 flex flex-col h-full overflow-hidden">
        <Header
          currentTab={currentTab}
          lang={lang}
          onToggleLanguage={handleToggleLanguage}
          isDark={isDark}
          onToggleTheme={() => setIsDark(!isDark)}
          onOpenMobileMenu={() => setMobileMenuOpen(true)}
          onNewReportClick={() => setCurrentTab('new-intake')}
          searchQuery={searchQuery}
          onSearchChange={setSearchQuery}
        />

        <main className="flex-1 overflow-y-auto px-4 md:px-8 py-6">
          {currentTab === 'dashboard' && (
            <Dashboard
              reports={savedReports}
              onSelectReport={handleSelectReport}
              onNavigateTab={(tab) => setCurrentTab(tab)}
              lang={lang}
            />
          )}

          {currentTab === 'new-intake' && (
            <IntakeForm
              form={form}
              onChange={handleFormChange}
              onGenerate={handleGenerate}
              onReset={handleResetForm}
              lang={lang}
            />
          )}

          {currentTab === 'ground-report' && (
            <GroundReportView
              reportMarkdown={reportMarkdown}
              headline={form.headline}
              location={[form.localUnit, form.district, form.state].filter(Boolean).join(', ')}
              status={reportStatus}
              onStatusChange={setReportStatus}
              onNavigateToSocialDeck={() => setCurrentTab('social-deck')}
              lang={lang}
            />
          )}

          {currentTab === 'social-deck' && (
            <SocialDeck
              bundle={socialBundle}
              onUpdateDraft={handleUpdateSocialDraft}
              onNavigateToVisualStudio={() => setCurrentTab('visual-studio')}
              lang={lang}
            />
          )}

          {currentTab === 'visual-studio' && (
            <VisualCardStudio
              form={form}
              lang={lang}
            />
          )}

          {currentTab === 'library' && (
            <ContentLibrary
              reports={savedReports}
              onSelectReport={handleSelectReport}
              onDeleteReport={handleDeleteReport}
              onNavigateTab={(tab) => setCurrentTab(tab)}
              lang={lang}
            />
          )}

          {currentTab === 'calendar' && (
            <ContentCalendar lang={lang} />
          )}

          {currentTab === 'analytics' && (
            <AnalyticsView lang={lang} />
          )}

          {currentTab === 'brand-settings' && (
            <SettingsView lang={lang} />
          )}
        </main>
      </div>
    </div>
  );
};
