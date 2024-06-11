/// <reference types="vite/client" />

declare module '*.vue' {
  import { DefineComponent } from 'vue';
  // eslint-disable-next-line @typescript-eslint/no-explicit-any, @typescript-eslint/ban-types
  const component: DefineComponent<{}, {}, any>;
  export default component;
}

// window
interface CustomWindow extends Window {
  deferredPrompt?: any;
}

declare const window: CustomWindow;

// .env
interface ImportMetaEnv {
  readonly VITE_API_BASE_URL: string;
  readonly VITE_WS_BASE_URL: string;
  readonly VITE_APP_VERSION: string;
  readonly VITE_APP_RELEASE: string;
  readonly VITE_SFTP_PREVIEW_MB: string;
  readonly VITE_DEMO_MODE: string;
}

// editor
declare module 'monaco-editor';
declare module 'monaco-editor/esm/vs/editor/editor.worker?worker'
declare module 'monaco-editor/esm/vs/language/json/json.worker?worker'
declare module 'monaco-editor/esm/vs/basic-languages/yaml/yaml.js';
declare module 'monaco-editor/esm/vs/basic-languages/shell/shell.js';
