/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        newsroom: {
          navy: '#10243A',
          blue: '#176B87',
          cyan: '#20C5D8',
          cyanDark: '#0EA5B6',
          cyanSubtle: '#E0F7FA',
          bg: '#F4F7FA',
          card: '#FFFFFF',
          cardMuted: '#F8FAFC',
          textPrimary: '#172B3A',
          textSecondary: '#64748B',
          border: '#E2E8F0',
          borderHover: '#CBD5E1',
          success: '#16845B',
          successSubtle: '#ECFDF5',
          warning: '#D99A24',
          warningSubtle: '#FFFBEB',
          error: '#D64545',
          errorSubtle: '#FEF2F2',
        }
      },
      fontFamily: {
        sans: ['Inter', 'Noto Sans Devanagari', 'system-ui', 'sans-serif'],
      }
    },
  },
  plugins: [],
};
