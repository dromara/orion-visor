import type { ITerminalAddon } from '@xterm/xterm';
import type { FitAddon } from '@xterm/addon-fit';
import type { WebglAddon } from '@xterm/addon-webgl';
import type { CanvasAddon } from '@xterm/addon-canvas';
import type { WebLinksAddon } from '@xterm/addon-web-links';
import type { SearchAddon } from '@xterm/addon-search';
import type { ImageAddon } from '@xterm/addon-image';
import type { Unicode11Addon } from '@xterm/addon-unicode11';

// 默认字体
export const defaultFontFamily = 'Consolas, Courier New, Monaco, courier, monospace';

// 默认主题
export const defaultTheme = {
  foreground: '#FFFFFF',
  background: '#1C1C1C',
  selectionBackground: '#444444',
};

// xterm 插件
export interface XtermAddons extends Record<string, ITerminalAddon> {
  fit: FitAddon;
  webgl: WebglAddon;
  canvas: CanvasAddon;
  weblink: WebLinksAddon;
  search: SearchAddon;
  image: ImageAddon;
  unicode: Unicode11Addon;
}
