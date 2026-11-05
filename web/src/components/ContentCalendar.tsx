import React from 'react';
import { 
  Calendar as CalendarIcon, 
  Clock, 
  ChevronLeft, 
  ChevronRight, 
  Plus, 
  MapPin, 
  CheckCircle2, 
  AlertCircle 
} from 'lucide-react';
import { LanguageMode } from '../types';
import { locales } from '../data/locales';

interface ContentCalendarProps {
  lang: LanguageMode;
}

export const ContentCalendar: React.FC<ContentCalendarProps> = ({ lang }) => {
  const t = locales[lang];
  const isHi = lang === 'hi';

  const scheduledEvents = [
    {
      day: '10 Nov',
      time: '10:00 AM',
      category: 'RURAL_DEV',
      title: isHi ? 'सोनभद्र जल-जीवन मिशन ग्राउंड फ़ॉलो-अप' : 'Sonbhadra Water Supply Ground Follow-Up',
      platform: 'X & Press Release',
      status: 'SCHEDULED'
    },
    {
      day: '12 Nov',
      time: '02:30 PM',
      category: 'URBAN_CIVIC',
      title: isHi ? 'कानपुर सीवरेज भराव वीडियो डॉक्यूमेंट्री' : 'Kanpur Urban Waterlogging Video Release',
      platform: 'Instagram Carousel',
      status: 'SCHEDULED'
    },
    {
      day: '15 Nov',
      time: '11:00 AM',
      category: 'LEGAL_RIGHTS',
      title: isHi ? 'प्रयागराज राशन डीलर जनसुनवाई लाइव' : 'Prayagraj PDS Citizen Hearing Live Report',
      platform: 'Facebook & X',
      status: 'PLANNED'
    }
  ];

  return (
    <div className="max-w-4xl mx-auto space-y-6 pb-16 animate-fade-in">
      <div className="p-6 rounded-2xl bg-white dark:bg-navy-dark border border-slate-200 dark:border-slate-800 shadow-sm flex items-center justify-between">
        <div>
          <h2 className="text-lg md:text-xl font-bold text-slate-900 dark:text-white">
            {t.navContentCalendar}
          </h2>
          <p className="text-xs text-slate-500 dark:text-slate-400 mt-0.5">
            {isHi ? 'नागरिक ग्राउंड रिपोर्ट्स एवं सोशल मीडिया प्रकाशन की समय-सारणी' : 'Editorial scheduling for investigative ground releases & social campaigns'}
          </p>
        </div>

        <div className="flex items-center gap-2">
          <span className="px-3 py-1.5 rounded-xl bg-cyan-accent/20 text-cyan-accent font-bold text-xs">
            November 2026
          </span>
        </div>
      </div>

      <div className="p-6 rounded-2xl bg-white dark:bg-navy-dark border border-slate-200 dark:border-slate-800 shadow-sm space-y-4">
        <h3 className="text-sm font-bold text-slate-900 dark:text-white flex items-center gap-2">
          <Clock size={16} className="text-cyan-accent" />
          <span>{isHi ? 'आगामी निर्धारित प्रकाशन' : 'Upcoming Scheduled Publications'}</span>
        </h3>

        <div className="space-y-3">
          {scheduledEvents.map((evt, idx) => (
            <div 
              key={idx}
              className="p-4 rounded-xl border border-slate-200 dark:border-slate-800 bg-slate-50/50 dark:bg-slate-900/40 flex flex-col sm:flex-row items-start sm:items-center justify-between gap-3"
            >
              <div className="flex items-center gap-3">
                <div className="px-3 py-2 rounded-xl bg-navy-dark text-white font-bold text-center text-xs shrink-0">
                  <div>{evt.day}</div>
                  <div className="text-[10px] text-cyan-accent font-medium">{evt.time}</div>
                </div>
                <div>
                  <h4 className="font-bold text-slate-900 dark:text-white text-xs md:text-sm">
                    {evt.title}
                  </h4>
                  <div className="flex items-center gap-2 text-[11px] text-slate-500 dark:text-slate-400 mt-0.5">
                    <span className="font-semibold text-cyan-accent">{evt.platform}</span>
                    <span>•</span>
                    <span>{evt.category}</span>
                  </div>
                </div>
              </div>

              <span className="px-2.5 py-1 rounded-md text-[10px] font-extrabold uppercase bg-emerald-100 text-emerald-800 dark:bg-emerald-950/60 dark:text-emerald-400">
                {evt.status}
              </span>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};
