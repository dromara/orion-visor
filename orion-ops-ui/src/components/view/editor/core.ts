import JsonWorker from 'monaco-editor/esm/vs/language/json/json.worker?worker';
import EditorWorker from 'monaco-editor/esm/vs/editor/editor.worker?worker';

/**
 * 主题
 */
export type Theme = 'vs' | 'vs-dark'

/**
 * 折叠方式
 */
export type FoldingStrategy = 'auto' | 'indentation'

/**
 * 是否一直显示折叠
 */
export  type ShowFoldingControls = 'always' | 'mouseover';

/**
 * 行亮
 */
export type RenderLineHighlight = 'all' | 'line' | 'none' | 'gutter'

/**
 * 配置项
 */
export interface Options {
  // 自适应布局
  automaticLayout?: boolean;
  // 只读提示
  readOnlyMessage?: {
    value?: string,

    [key: string]: unknown;
  };
  // 是否折叠
  folding?: boolean;
  // 折叠方式
  foldingStrategy?: FoldingStrategy;
  // 是否折叠等高线
  foldingHighlight?: boolean;
  // 是否一直显示折叠
  showFoldingControls?: ShowFoldingControls;
  // 等宽优化
  disableLayerHinting?: boolean;
  // 行亮
  renderLineHighlight?: RenderLineHighlight;
  // 点击行号时是否应该选择相应的行
  selectOnLineNumbers?: boolean;
  // 行号最小字符
  lineNumbersMinChars?: number;
  // 输入提示
  placeholder?: string;
  // 小地图
  minimap?: {
    // 关闭小地图
    enabled?: boolean;

    [key: string]: unknown;
  };
  // 字体大小
  fontSize?: number;
  // 取消代码后面一大段空白
  scrollBeyondLastLine?: boolean;
  // 不要滚动条的边框
  overviewRulerBorder?: boolean;
  // 颜色装饰器
  colorDecorators?: boolean;
  // 将溢出小部件显示为 fixed  编辑器较小的话需要设置为 true 否则 suggest 会被覆盖
  fixedOverflowWidgets?: boolean;

  [key: string]: unknown;
}

/**
 * 创建默认配置
 */
export const createDefaultOptions = (): Options => {
  return {
    automaticLayout: true,
    readOnlyMessage: {
      value: '当前为只读状态'
    },
    folding: true,
    foldingHighlight: true,
    foldingStrategy: 'indentation',
    showFoldingControls: 'always',
    renderLineHighlight: 'line',
    selectOnLineNumbers: true,
    lineNumbersMinChars: 2,
    disableLayerHinting: true,
    minimap: {
      enabled: false,
    },
    fontSize: 14,
    scrollBeyondLastLine: false,
    overviewRulerBorder: false,
    colorDecorators: true,
    fixedOverflowWidgets: true,
  };
};

// worker 生成器
self.MonacoEnvironment = {
  getWorker(_: string, label: string) {
    if (label === 'json') {
      return new JsonWorker();
    }
    return new EditorWorker(label);
  },
};
