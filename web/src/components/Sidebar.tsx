import React from 'react';
import { 
  LayoutDashboard, 
  FileEdit, 
  FileText, 
  Share2, 
  Image as ImageIcon, 
  FolderArchive, 
  Calendar, 
  BarChart3, 
  Sliders, 
  ChevronLeft, 
  ChevronRight,
  Flame,
  ShieldCheck,
  Radio
} from 'lucide-react';
import { NavigationTab, LanguageMode } from '../types';
import { locales } from '../data/locales';

interface SidebarProps {
  currentTab: NavigationTab;
  onSelectTab: (tab: NavigationTab) => void;
  collapsed: boolean;
  onToggleCollapse: () => void;
  lang: LanguageMode;
}

export const Sidebar: React.FC<SidebarProps> = ({
  currentTab,
  onSelectTab,
  collapsed,
  onToggleCollapse,
  lang
}) => {
  const t = locales[lang];

  const navGroups = [
    {
      groupTitle: t.navOverview,
      items: [
        { id: 'dashboard' as NavigationTab, label: t.navDashboard, icon: LayoutDashboard }
      ]
    },
    {
      groupTitle: t.navReporting,
      items: [
        { id: 'new-intake' as NavigationTab, label: t.navNewIntake, icon: FileEdit, badge: 'New' },
        { id: 'ground-report' as NavigationTab, label: t.navGroundReport, icon: FileText }
      ]
    },
    {
      groupTitle: t.navContentStudio,
      items: [
        { id: 'social-deck' as NavigationTab, label: t.navSocialDeck, icon: Share2, badge: '6' },
        { id: 'visual-studio' as NavigationTab, label: t.navVisualStudio, icon: ImageIcon, badge: 'HD' }
      ]
    },
    {
      groupTitle: t.navManagement,
      items: [
        { id: 'library' as NavigationTab, label: t.navContentLibrary, icon: FolderArchive },
        { id: 'calendar' as NavigationTab, label: t.navContentCalendar, icon: Calendar },
        { id: 'analytics' as NavigationTab, label: t.navAnalytics, icon: BarChart3 }
      ]
    },
    {
      groupTitle: t.navSettings,
      items: [
        { id: 'brand-settings' as NavigationTab, label: t.navBrandSettings, icon: Sliders }
      ]
    }
  ];

  return (
    <aside 
      className={`relative flex flex-col bg-navy-dark text-slate-100 transition-all duration-300 z-30 select-none shadow-xl border-r border-slate-800 ${
        collapsed ? 'w-20' : 'w-72'
      }`}
    >
      {/* Brand Header */}
      <div className="h-20 flex items-center px-4 border-b border-slate-800/80 bg-navy-dark/95 backdrop-blur-md justify-between">
        <div className="flex items-center gap-3 overflow-hidden cursor-pointer" onClick={() => onSelectTab('dashboard')}>
          <div className="relative w-11 h-11 shrink-0 rounded-xl overflow-hidden shadow-md ring-2 ring-cyan-accent/30 bg-slate-900 flex items-center justify-center">
            <img 
              src="/logo.jpg" 
              alt="PostCraft Logo" 
              className="w-full h-full object-cover"
              onError={(e) => {
                // fallback
                (e.target as HTMLElement).style.display = 'none';
              }} 
            />
            <span className="text-cyan-accent font-extrabold text-sm hidden">PC</span>
          </div>
          {!collapsed && (
            <div className="flex flex-col min-w-0">
              <div className="flex items-center gap-2">
                <span className="font-black text-lg text-white tracking-tight truncate">
                  {lang === 'hi' ? 'पोस्ट क्राफ्ट' : 'PostCraft'}
                </span>
                <span className="px-1.5 py-0.5 text-[10px] font-bold uppercase rounded bg-cyan-accent/20 text-cyan-accent border border-cyan-accent/30">
                  v2.5
                </span>
              </div>
              <span className="text-[11px] text-slate-400 font-medium truncate">
                {lang === 'hi' ? 'न्यूज़रूम डेस्क' : 'Newsroom Studio'}
              </span>
            </div>
          )}
        </div>

        <button 
          onClick={onToggleCollapse}
          className="p-1.5 rounded-lg text-slate-400 hover:text-white hover:bg-slate-800/60 transition-colors"
          title={collapsed ? 'Expand' : 'Collapse'}
        >
          {collapsed ? <ChevronRight size={18} /> : <ChevronLeft size={18} />}
        </button>
      </div>

      {/* Navigation Links */}
      <div className="flex-1 overflow-y-auto py-4 px-3 space-y-6 scrollbar-thin scrollbar-thumb-slate-800">
        {navGroups.map((group, gIdx) => (
          <div key={gIdx} className="space-y-1">
            {!collapsed && (
              <div className="px-3 pb-1 text-[11px] font-bold uppercase tracking-wider text-slate-400">
                {group.groupTitle}
              </div>
            )}
            <div className="space-y-1">
              {group.items.map((item) => {
                const Icon = item.icon;
                const isActive = currentTab === item.id;
                return (
                  <button
                    key={item.id}
                    onClick={() => onSelectTab(item.id)}
                    className={`w-full flex items-center gap-3 px-3 py-2.5 rounded-xl text-xs font-semibold transition-all relative ${
                      isActive 
                        ? 'bg-slate-800/90 text-white shadow-sm ring-1 ring-cyan-accent/50' 
                        : 'text-slate-300 hover:bg-slate-800/50 hover:text-white'
                    } ${collapsed ? 'justify-center' : 'justify-between'}`}
                    title={collapsed ? item.label : undefined}
                  >
                    <div className="flex items-center gap-3 min-w-0">
                      <div className={`shrink-0 ${isActive ? 'text-cyan-accent' : 'text-slate-400'}`}>
                        <Icon size={19} />
                      </div>
                      {!collapsed && (
                        <span className="truncate text-left">{item.label}</span>
                      )}
                    </div>

                    {!collapsed && item.badge && (
                      <span className={`px-1.5 py-0.5 rounded text-[10px] font-bold uppercase ${
                        isActive 
                          ? 'bg-cyan-accent text-slate-900' 
                          : 'bg-slate-800 text-slate-400'
                      }`}>
                        {item.badge}
                      </span>
                    )}

                    {/* Active Cyan Indicator Line */}
                    {isActive && (
                      <div className="absolute left-0 top-2 bottom-2 w-1 bg-cyan-accent rounded-r-full shadow-glow" />
                    )}
                  </button>
                );
              })}
            </div>
          </div>
        ))}
      </div>

      {/* Live Newsroom Status Pill */}
      {!collapsed && (
        <div className="p-3 mx-3 mb-3 rounded-xl bg-slate-900/80 border border-slate-800 flex items-center gap-2.5">
          <div className="relative flex h-2.5 w-2.5">
            <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-emerald-400 opacity-75"></span>
            <span className="relative inline-flex rounded-full h-2.5 w-2.5 bg-emerald-500"></span>
          </div>
          <div className="flex flex-col min-w-0">
            <span className="text-[11px] font-semibold text-slate-200 truncate">
              {lang === 'hi' ? 'सिटीजन डेस्क सक्रिय' : 'Citizen Desk Active'}
            </span>
            <span className="text-[10px] text-slate-400 truncate">
              {lang === 'hi' ? 'साक्ष्य सत्यापन एवं सुरक्षा' : 'Encrypted Evidence Store'}
            </span>
          </div>
        </div>
      )}
    </aside>
  );
};
