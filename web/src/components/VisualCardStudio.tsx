import React, { useRef, useEffect, useState } from 'react';
import { 
  Download, 
  Copy, 
  Check, 
  Image as ImageIcon, 
  Sparkles, 
  Layers, 
  Palette, 
  RefreshCw,
  Eye
} from 'lucide-react';
import { ReportFormState, LanguageMode } from '../types';
import { locales } from '../data/locales';

interface VisualCardStudioProps {
  form: ReportFormState;
  lang: LanguageMode;
}

type CardTemplate = 'BREAKING' | 'GROUND_REPORT' | 'CLAIM_REALITY' | 'CITIZEN_VOICE' | 'PUBLIC_ALERT';

export const VisualCardStudio: React.FC<VisualCardStudioProps> = ({
  form,
  lang
}) => {
  const t = locales[lang];
  const isHi = lang === 'hi';

  const canvasRef = useRef<HTMLCanvasElement | null>(null);
  const [selectedTemplate, setSelectedTemplate] = useState<CardTemplate>('GROUND_REPORT');
  const [copied, setCopied] = useState(false);
  const [isRendering, setIsRendering] = useState(false);

  const templates: { id: CardTemplate; label: string }[] = [
    { id: 'GROUND_REPORT', label: isHi ? 'ग्राउंड रिपोर्ट' : 'Ground Report' },
    { id: 'BREAKING', label: isHi ? 'ब्रेकिंग न्यूज़' : 'Breaking News' },
    { id: 'CLAIM_REALITY', label: isHi ? 'दावा बनाम हक़ीक़त' : 'Claim vs Reality' },
    { id: 'CITIZEN_VOICE', label: isHi ? 'जन-आवाज़' : 'Citizen Voice' },
    { id: 'PUBLIC_ALERT', label: isHi ? 'पब्लिक अलर्ट' : 'Public Alert' }
  ];

  // Canvas Drawing Function
  const renderCanvas = () => {
    const canvas = canvasRef.current;
    if (!canvas) return;
    const ctx = canvas.getContext('2d');
    if (!ctx) return;

    setIsRendering(true);

    const width = 1080;
    const height = 1350;
    canvas.width = width;
    canvas.height = height;

    // Background: Clean Light Background with Navy border
    ctx.fillStyle = '#F4F7FA';
    ctx.fillRect(0, 0, width, height);

    // Header Background: Navy Blue #10243A
    ctx.fillStyle = '#10243A';
    ctx.fillRect(0, 0, width, 180);

    // Accent line: Electric Cyan #20C5D8
    ctx.fillStyle = '#20C5D8';
    ctx.fillRect(0, 180, width, 8);

    // Header Branding
    ctx.fillStyle = '#FFFFFF';
    ctx.font = 'bold 44px sans-serif';
    ctx.fillText('POSTCRAFT | पोस्ट क्राफ्ट', 60, 85);

    ctx.fillStyle = '#20C5D8';
    ctx.font = 'bold 22px sans-serif';
    ctx.fillText(
      isHi ? 'सोशल मीडिया पोस्ट एवं ग्राउंड रिपोर्ट जनरेटर' : 'CITIZEN JOURNALISM & GROUND REPORTING DESK',
      60,
      130
    );

    // Date & Location Pill
    const locationStr = [form.localUnit, form.district, form.state].filter(Boolean).join(', ') || 'स्थानीय क्षेत्र';
    ctx.fillStyle = '#176B87';
    ctx.beginPath();
    ctx.roundRect(width - 380, 50, 320, 50, 12);
    ctx.fill();

    ctx.fillStyle = '#FFFFFF';
    ctx.font = 'bold 20px sans-serif';
    ctx.textAlign = 'center';
    ctx.fillText(locationStr.slice(0, 24), width - 220, 82);
    ctx.textAlign = 'left';

    // Badge based on template
    let badgeText = isHi ? 'ग्राउंड पड़ताल' : 'GROUND INVESTIGATION';
    let badgeBg = '#D64545'; // Error red
    if (selectedTemplate === 'BREAKING') {
      badgeText = isHi ? 'ब्रेकिंग न्यूज़ अलर्ट' : 'BREAKING CIVIC ALERT';
      badgeBg = '#D64545';
    } else if (selectedTemplate === 'CLAIM_REALITY') {
      badgeText = isHi ? 'दावा बनाम हक़ीक़त' : 'CLAIM VS GROUND REALITY';
      badgeBg = '#176B87';
    } else if (selectedTemplate === 'CITIZEN_VOICE') {
      badgeText = isHi ? 'प्रत्यक्षदर्शी गवाह' : 'EYEWITNESS CITIZEN VOICE';
      badgeBg = '#16845B';
    }

    ctx.fillStyle = badgeBg;
    ctx.beginPath();
    ctx.roundRect(60, 220, 280, 46, 8);
    ctx.fill();

    ctx.fillStyle = '#FFFFFF';
    ctx.font = 'bold 20px sans-serif';
    ctx.fillText(badgeText, 80, 250);

    // Department Tag
    if (form.department) {
      ctx.fillStyle = '#E2E8F0';
      ctx.beginPath();
      ctx.roundRect(360, 220, 420, 46, 8);
      ctx.fill();

      ctx.fillStyle = '#172B3A';
      ctx.font = 'bold 18px sans-serif';
      ctx.fillText(form.department.slice(0, 32), 375, 250);
    }

    // Main Headline Box
    ctx.fillStyle = '#172B3A';
    ctx.font = 'bold 38px sans-serif';
    wrapText(ctx, form.headline || (isHi ? 'गंभीर जनहित समस्या पर स्वतंत्र ग्राउंड रिपोर्ट' : 'Critical Public Issue Investigation'), 60, 320, width - 120, 52);

    // Content Card: White with shadow
    const cardY = 460;
    ctx.fillStyle = '#FFFFFF';
    ctx.shadowColor = 'rgba(16, 36, 58, 0.08)';
    ctx.shadowBlur = 24;
    ctx.shadowOffsetY = 12;
    ctx.beginPath();
    ctx.roundRect(60, cardY, width - 120, 680, 24);
    ctx.fill();
    ctx.shadowColor = 'transparent';

    // Claim Box (Blueish-Grey background)
    ctx.fillStyle = '#F8FAFC';
    ctx.beginPath();
    ctx.roundRect(90, cardY + 30, width - 180, 160, 16);
    ctx.fill();

    ctx.fillStyle = '#176B87';
    ctx.font = 'bold 22px sans-serif';
    ctx.fillText(isHi ? '📑 सरकारी / आधिकारिक दावा:' : '📑 OFFICIAL DEPARTMENTAL CLAIM:', 120, cardY + 70);

    ctx.fillStyle = '#334155';
    ctx.font = '24px sans-serif';
    wrapText(ctx, `"${form.officialClaim || (isHi ? 'योजना कागज़ों पर पूर्ण दर्ज है।' : 'Portal logs indicate work is completed.')}"`, 120, cardY + 110, width - 240, 34);

    // Ground Reality Box (Crisp Alert Red Outline)
    ctx.fillStyle = '#FFF5F5';
    ctx.beginPath();
    ctx.roundRect(90, cardY + 220, width - 180, 220, 16);
    ctx.fill();
    ctx.lineWidth = 3;
    ctx.strokeStyle = '#D64545';
    ctx.stroke();

    ctx.fillStyle = '#D64545';
    ctx.font = 'bold 24px sans-serif';
    ctx.fillText(isHi ? '❌ ज़मीनी हक़ीक़त (मौके की जाँच):' : '❌ VERIFIED GROUND REALITY:', 120, cardY + 265);

    ctx.fillStyle = '#172B3A';
    ctx.font = 'bold 26px sans-serif';
    wrapText(ctx, `"${form.groundReality || (isHi ? 'मौके पर कार्य अधूरा है और नागरिक परेशान हैं।' : 'Ground inspection confirms severe infrastructure breakdown.')}"`, 120, cardY + 310, width - 240, 38);

    // Eyewitness Quote or Action Demands
    ctx.fillStyle = '#10243A';
    ctx.font = 'bold 22px sans-serif';
    ctx.fillText(isHi ? '🗣️ प्रत्यक्षदर्शी नागरिक बयान:' : '🗣️ CITIZEN TESTIMONY:', 100, cardY + 490);

    ctx.fillStyle = '#475569';
    ctx.font = 'italic 23px sans-serif';
    wrapText(ctx, form.citizenQuotes || (isHi ? 'शिकायतों के बाद भी कोई अधिकारी झाँकने नहीं आया।' : 'Multiple petitions submitted; zero departmental response.'), 100, cardY + 530, width - 200, 34);

    // Key Stats at bottom of card
    ctx.fillStyle = '#F1F5F9';
    ctx.beginPath();
    ctx.roundRect(90, cardY + 590, width - 180, 60, 12);
    ctx.fill();

    ctx.fillStyle = '#172B3A';
    ctx.font = 'bold 20px sans-serif';
    ctx.fillText(
      `${isHi ? '👥 प्रभावित:' : '👥 IMPACT:'} ${form.affectedPopulation || (isHi ? 'सैकड़ों परिवार' : 'Hundreds of residents')}`,
      120,
      cardY + 628
    );

    ctx.fillStyle = '#D99A24';
    ctx.fillText(
      `${isHi ? '⏱️ समय-सीमा:' : '⏱️ DEADLINE:'} ${form.deadline || (isHi ? '७ कार्यदिवस' : '7 Days')}`,
      width - 450,
      cardY + 628
    );

    // Footer Bar
    ctx.fillStyle = '#10243A';
    ctx.fillRect(0, height - 140, width, 140);

    ctx.fillStyle = '#20C5D8';
    ctx.font = 'bold 22px sans-serif';
    ctx.fillText('POSTCRAFT NEWSROOM | जन-आवाज़ डिजिटल प्लेटफॉर्म', 60, height - 75);

    ctx.fillStyle = '#94A3B8';
    ctx.font = '18px sans-serif';
    ctx.fillText(
      isHi ? 'साक्ष्य सुरक्षित • निष्पक्ष पत्रकारिता • जवाबदेही' : 'Verified Evidence • Independent Journalism • Accountability',
      60,
      height - 45
    );

    // Official Tags at Footer Right
    ctx.fillStyle = '#FFFFFF';
    ctx.font = 'bold 20px sans-serif';
    ctx.textAlign = 'right';
    ctx.fillText(form.relevantOfficial || '@DistrictMagistrate', width - 60, height - 60);
    ctx.textAlign = 'left';

    setIsRendering(false);
  };

  function wrapText(
    ctx: CanvasRenderingContext2D,
    text: string,
    x: number,
    y: number,
    maxWidth: number,
    lineHeight: number
  ) {
    const words = text.split(' ');
    let line = '';
    let currentY = y;

    for (let n = 0; n < words.length; n++) {
      const testLine = line + words[n] + ' ';
      const metrics = ctx.measureText(testLine);
      const testWidth = metrics.width;
      if (testWidth > maxWidth && n > 0) {
        ctx.fillText(line, x, currentY);
        line = words[n] + ' ';
        currentY += lineHeight;
        if (currentY > y + lineHeight * 4) {
          // Truncate if too long
          ctx.fillText(line + '...', x, currentY);
          return;
        }
      } else {
        line = testLine;
      }
    }
    ctx.fillText(line, x, currentY);
  }

  useEffect(() => {
    renderCanvas();
  }, [form, selectedTemplate, lang]);

  const handleDownload = () => {
    const canvas = canvasRef.current;
    if (!canvas) return;
    const url = canvas.toDataURL('image/png');
    const a = document.createElement('a');
    a.href = url;
    a.download = `postcraft-${selectedTemplate.toLowerCase()}-${Date.now()}.png`;
    a.click();
  };

  const handleCopyImage = async () => {
    const canvas = canvasRef.current;
    if (!canvas) return;
    try {
      canvas.toBlob(async (blob) => {
        if (!blob) return;
        await navigator.clipboard.write([
          new ClipboardItem({ 'image/png': blob })
        ]);
        setCopied(true);
        setTimeout(() => setCopied(false), 2000);
      });
    } catch (e) {
      handleDownload();
    }
  };

  return (
    <div className="max-w-4xl mx-auto space-y-6 pb-16 animate-fade-in">
      {/* Top Header Card */}
      <div className="p-6 rounded-2xl bg-white dark:bg-navy-dark border border-slate-200 dark:border-slate-800 shadow-sm flex flex-col md:flex-row items-start md:items-center justify-between gap-4">
        <div>
          <div className="flex items-center gap-2">
            <span className="px-2.5 py-0.5 rounded-md text-[11px] font-extrabold uppercase bg-cyan-accent/20 text-cyan-accent border border-cyan-accent/30">
              HD 1080x1350
            </span>
          </div>
          <h2 className="text-lg md:text-xl font-bold text-slate-900 dark:text-white mt-1">
            {t.visualStudioTitle}
          </h2>
          <p className="text-xs text-slate-500 dark:text-slate-400">
            {t.visualStudioSubtitle}
          </p>
        </div>

        {/* Action Buttons */}
        <div className="flex items-center gap-2 shrink-0">
          <button
            onClick={handleCopyImage}
            className="px-3.5 py-2 rounded-xl border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800/80 hover:border-cyan-accent text-slate-800 dark:text-slate-200 text-xs font-semibold flex items-center gap-1.5 transition-all shadow-sm"
          >
            {copied ? <Check size={14} className="text-emerald-500" /> : <Copy size={14} />}
            <span>{copied ? t.copied : t.copyCard}</span>
          </button>

          <button
            onClick={handleDownload}
            className="px-4 py-2 rounded-xl bg-cyan-accent text-slate-950 font-bold text-xs hover:bg-cyan-accent/90 transition-all flex items-center gap-1.5 shadow-md"
          >
            <Download size={14} />
            <span>{t.downloadCard}</span>
          </button>
        </div>
      </div>

      {/* Template Selector Pills */}
      <div className="flex items-center gap-2 overflow-x-auto pb-1 scrollbar-none">
        {templates.map((tpl) => {
          const isActive = selectedTemplate === tpl.id;
          return (
            <button
              key={tpl.id}
              onClick={() => setSelectedTemplate(tpl.id)}
              className={`px-4 py-2 rounded-xl text-xs font-bold whitespace-nowrap transition-all ${
                isActive
                  ? 'bg-navy-dark text-white ring-2 ring-cyan-accent shadow-sm'
                  : 'bg-white dark:bg-navy-dark border border-slate-200 dark:border-slate-800 text-slate-600 dark:text-slate-300 hover:border-slate-300'
              }`}
            >
              {tpl.label}
            </button>
          );
        })}
      </div>

      {/* Live Canvas Preview */}
      <div className="p-6 rounded-2xl bg-white dark:bg-navy-dark border border-slate-200 dark:border-slate-800 shadow-sm flex flex-col items-center justify-center space-y-4">
        <div className="relative max-w-sm w-full rounded-2xl overflow-hidden shadow-2xl border-2 border-slate-300 dark:border-slate-700 bg-slate-100">
          <canvas
            ref={canvasRef}
            className="w-full h-auto block"
          />
        </div>

        <p className="text-[11px] text-slate-400 font-medium text-center">
          {isHi 
            ? '१०८०x१३५० पिक्सल का उच्च-गुणवत्ता ग्राफिक्स कार्ड इंस्टाग्राम एवं फ़ेसबुक के लिए तैयार।'
            : '1080x1350 High-Definition graphic card optimized for Instagram, Facebook, and WhatsApp status.'}
        </p>
      </div>
    </div>
  );
};
