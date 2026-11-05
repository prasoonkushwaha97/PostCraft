import React, { useState } from 'react';
import { 
  Twitter, 
  Instagram, 
  Facebook, 
  Linkedin, 
  FileText, 
  Send, 
  Copy, 
  Check, 
  Share2, 
  Sparkles, 
  Hash, 
  AtSign, 
  Image as ImageIcon,
  CheckCircle2
} from 'lucide-react';
import { SocialPlatformId, GeneratedPostBundle, LanguageMode } from '../types';
import { locales } from '../data/locales';

interface SocialDeckProps {
  bundle: GeneratedPostBundle;
  onUpdateDraft: (platform: SocialPlatformId, text: string) => void;
  onNavigateToVisualStudio: () => void;
  lang: LanguageMode;
}

export const SocialDeck: React.FC<SocialDeckProps> = ({
  bundle,
  onUpdateDraft,
  onNavigateToVisualStudio,
  lang
}) => {
  const t = locales[lang];
  const isHi = lang === 'hi';

  const [activePlatform, setActivePlatform] = useState<SocialPlatformId>('x');
  const [copied, setCopied] = useState(false);

  const platforms: { id: SocialPlatformId; label: string; icon: React.ReactNode; maxChars?: number }[] = [
    { id: 'x', label: 'X (Twitter)', icon: <Twitter size={16} />, maxChars: 280 },
    { id: 'instagram', label: 'Instagram', icon: <Instagram size={16} /> },
    { id: 'facebook', label: 'Facebook', icon: <Facebook size={16} /> },
    { id: 'linkedin', label: 'LinkedIn', icon: <Linkedin size={16} /> },
    { id: 'pressRelease', label: isHi ? 'प्रेस विज्ञप्ति' : 'Press Release', icon: <FileText size={16} /> },
    { id: 'policyBrief', label: isHi ? 'पॉलिसी ब्रीफ' : 'Policy Brief', icon: <Send size={16} /> },
  ];

  const currentPlatform = platforms.find(p => p.id === activePlatform) || platforms[0];
  const currentText = bundle[activePlatform] || '';
  const charLength = currentText.length;
  const isOverLimit = currentPlatform.maxChars ? charLength > currentPlatform.maxChars : false;

  const handleCopy = () => {
    navigator.clipboard.writeText(currentText);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  const handleNativeShare = async () => {
    if (navigator.share) {
      try {
        await navigator.share({
          title: 'PostCraft Report',
          text: currentText,
        });
      } catch (err) {
        // Ignored or cancelled
      }
    } else {
      handleCopy();
    }
  };

  return (
    <div className="max-w-4xl mx-auto space-y-6 pb-16 animate-fade-in">
      {/* Top Header Card */}
      <div className="p-6 rounded-2xl bg-white dark:bg-navy-dark border border-slate-200 dark:border-slate-800 shadow-sm flex flex-col md:flex-row items-start md:items-center justify-between gap-4">
        <div>
          <div className="flex items-center gap-2">
            <span className="px-2.5 py-0.5 rounded-md text-[11px] font-extrabold uppercase bg-cyan-accent/20 text-cyan-accent border border-cyan-accent/30">
              {isHi ? 'मल्टी-प्लेटफ़ॉर्म स्टूडियो' : 'Multi-Platform Studio'}
            </span>
          </div>
          <h2 className="text-lg md:text-xl font-bold text-slate-900 dark:text-white mt-1">
            {t.socialDeckTitle}
          </h2>
          <p className="text-xs text-slate-500 dark:text-slate-400">
            {t.socialDeckSubtitle}
          </p>
        </div>

        <button
          onClick={onNavigateToVisualStudio}
          className="px-4 py-2 rounded-xl bg-slate-900 dark:bg-slate-800 hover:bg-slate-800 border border-slate-700 text-white font-bold text-xs flex items-center gap-2 shadow-sm transition-all"
        >
          <ImageIcon size={15} className="text-cyan-accent" />
          <span>{isHi ? 'विजुअल कार्ड जोड़ें' : 'Generate Graphic Card'}</span>
        </button>
      </div>

      {/* Horizontal Platform Switcher Pills */}
      <div className="flex items-center gap-2 overflow-x-auto pb-1 scrollbar-none">
        {platforms.map((platform) => {
          const isActive = activePlatform === platform.id;
          return (
            <button
              key={platform.id}
              onClick={() => setActivePlatform(platform.id)}
              className={`px-4 py-2.5 rounded-xl text-xs font-bold whitespace-nowrap transition-all flex items-center gap-2 ${
                isActive
                  ? 'bg-navy-dark text-white ring-2 ring-cyan-accent shadow-sm'
                  : 'bg-white dark:bg-navy-dark border border-slate-200 dark:border-slate-800 text-slate-600 dark:text-slate-300 hover:border-slate-300'
              }`}
            >
              <div className={isActive ? 'text-cyan-accent' : 'text-slate-400'}>
                {platform.icon}
              </div>
              <span>{platform.label}</span>
            </button>
          );
        })}
      </div>

      {/* Active Editor & Action Card */}
      <div className="p-6 md:p-8 rounded-2xl bg-white dark:bg-navy-dark border border-slate-200 dark:border-slate-800 shadow-sm space-y-4">
        {/* Sub-bar with Platform Info and Character Counter */}
        <div className="flex items-center justify-between pb-3 border-b border-slate-100 dark:border-slate-800">
          <div className="flex items-center gap-2">
            <span className="font-bold text-sm text-slate-900 dark:text-white">
              {currentPlatform.label}
            </span>
            {currentPlatform.maxChars && (
              <span className={`px-2 py-0.5 rounded text-[11px] font-bold ${
                isOverLimit 
                  ? 'bg-rose-100 text-rose-700 dark:bg-rose-950/60 dark:text-rose-400' 
                  : 'bg-slate-100 text-slate-600 dark:bg-slate-800 dark:text-slate-300'
              }`}>
                {charLength} / {currentPlatform.maxChars}
              </span>
            )}
          </div>

          <div className="flex items-center gap-2">
            <button
              onClick={handleCopy}
              className="px-3 py-1.5 rounded-lg border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800/80 hover:border-cyan-accent text-slate-800 dark:text-slate-200 text-xs font-semibold flex items-center gap-1.5 transition-all shadow-sm"
            >
              {copied ? <Check size={14} className="text-emerald-500" /> : <Copy size={14} />}
              <span>{copied ? t.copied : t.copyText}</span>
            </button>

            <button
              onClick={handleNativeShare}
              className="px-3.5 py-1.5 rounded-lg bg-cyan-accent text-slate-950 font-bold text-xs hover:bg-cyan-accent/90 transition-all flex items-center gap-1.5 shadow-sm"
            >
              <Share2 size={14} />
              <span>{t.sharePost}</span>
            </button>
          </div>
        </div>

        {/* Text Area Draft */}
        <div>
          <textarea
            rows={12}
            value={currentText}
            onChange={(e) => onUpdateDraft(activePlatform, e.target.value)}
            className="w-full p-4 rounded-xl border border-slate-200 dark:border-slate-700 bg-slate-50/50 dark:bg-slate-900/60 text-xs md:text-sm text-slate-900 dark:text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-cyan-accent/50 font-sans leading-relaxed resize-y"
          />
        </div>

        {/* Suggested Hashtags Bar */}
        <div className="pt-2 flex flex-wrap items-center gap-2 text-xs text-slate-500 dark:text-slate-400">
          <span className="font-bold flex items-center gap-1 text-slate-700 dark:text-slate-300">
            <Hash size={13} className="text-cyan-accent" />
            <span>{t.suggestedHashtags}:</span>
          </span>
          {(isHi 
            ? ['#जनआवाज़', '#ग्राउंडरिपोर्ट', '#पोस्टक्राफ्ट', '#जवाबदेही', '#सच्चाई']
            : ['#GroundReport', '#CivicAccountability', '#PostCraft', '#CitizenJournalism', '#PublicInterest']
          ).map((tag, i) => (
            <span 
              key={i} 
              onClick={() => onUpdateDraft(activePlatform, currentText + ' ' + tag)}
              className="px-2.5 py-1 rounded-md bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 hover:text-cyan-accent hover:border-cyan-accent border border-transparent cursor-pointer font-medium text-[11px] transition-all"
            >
              {tag}
            </span>
          ))}
        </div>
      </div>
    </div>
  );
};
