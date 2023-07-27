import { createI18n } from 'vue-i18n';
import cn from './zh-CN';

export const LOCALE_OPTIONS = [{ label: '中文', value: 'zh-CN' }];
const defaultLocale = localStorage.getItem('locale') || 'zh-CN';

const i18n = createI18n({
  locale: defaultLocale,
  fallbackLocale: 'en-US',
  legacy: false,
  allowComposition: true,
  messages: {
    'zh-CN': cn,
  },
});

export default i18n;
