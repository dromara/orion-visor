import type { TipsState } from './types';
import { defineStore } from 'pinia';
import { setTipsTipped } from '@/api/user/tips';

export default defineStore('tips', {
  state: (): TipsState => ({
    tippedKeys: []
  }),

  actions: {
    set(keys: Array<string>) {
      this.tippedKeys = keys;
    },
    isTipped(key: string): boolean {
      return this.tippedKeys.includes(key);
    },
    isNotTipped(key: string): boolean {
      return !this.tippedKeys.includes(key);
    },
    async setTipped(key: string) {
      try {
        await setTipsTipped(key);
        this.tippedKeys.push(key);
      } catch (e) {
      }
    }
  },
});
