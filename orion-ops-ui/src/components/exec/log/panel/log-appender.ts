import type { ILogAppender, LogAddons, LogAppenderConf, LogDomRef } from './const';
import { AppenderOptions } from './const';
import type { ExecTailRequest } from '@/api/exec/exec';
import { getExecLogTailToken } from '@/api/exec/exec';
import { webSocketBaseUrl } from '@/utils/env';
import { Message } from '@arco-design/web-vue';
import { createWebSocket } from '@/utils';
import { useDebounceFn } from '@vueuse/core';
import { addEventListen, removeEventListen } from '@/utils/event';
import { copy as copyText } from '@/hooks/copy';
import { Terminal } from 'xterm';
import { FitAddon } from 'xterm-addon-fit';
import { SearchAddon } from 'xterm-addon-search';
import { WebLinksAddon } from 'xterm-addon-web-links';
import { WebglAddon } from 'xterm-addon-webgl';

// 执行日志 appender 实现
export default class LogAppender implements ILogAppender {

  private current: LogAppenderConf;

  private client?: WebSocket;

  private readonly config: ExecTailRequest;

  private readonly appenderRel: Record<string, LogAppenderConf>;

  private keepAliveTask?: number;

  private readonly fitAllFn: () => {};

  constructor(config: ExecTailRequest) {
    this.current = undefined as unknown as LogAppenderConf;
    this.config = config;
    this.appenderRel = {};
    this.fitAllFn = useDebounceFn(this.fitAll).bind(this);
  }

  // 初始化
  async init(logDomRefs: Array<LogDomRef>) {
    // 初始化 appender
    this.initAppender(logDomRefs);
    // 初始化 client
    await this.openClient();
  }

  // 初始化 appender
  initAppender(logDomRefs: Array<LogDomRef>) {
    // 打开 log-view
    for (let logDomRef of logDomRefs) {
      // 初始化 terminal
      const terminal = new Terminal(AppenderOptions);
      // 初始化快捷键
      this.initCustomKey(terminal);
      // 初始化插件
      const addons = this.initAddons(terminal);
      // 打开终端
      terminal.open(logDomRef.el);
      // 自适应
      addons.fit.fit();
      this.appenderRel[logDomRef.id] = {
        ...logDomRef,
        terminal,
        addons
      };
    }
    // 设置当前对象
    this.current = this.appenderRel[logDomRefs[0].id];
    // 注册自适应事件
    addEventListen(window, 'resize', this.fitAllFn);
  }

  // 初始化快捷键操作
  initCustomKey(terminal: Terminal) {
    terminal.attachCustomKeyEventHandler((e: KeyboardEvent) => {
      if (e.type !== 'keydown') {
        return true;
      }
      if (e.code === 'Enter') {
        // 新起一行
        e.preventDefault();
        terminal.write('\r\n');
      } else if (e.code === 'Home') {
        // 上移一页
        e.preventDefault();
        terminal.scrollPages(-1);
      } else if (e.code === 'ArrowUp') {
        // 上移
        e.preventDefault();
        terminal.scrollLines(-1);
      } else if (e.code === 'End') {
        // 下移一页
        e.preventDefault();
        terminal.scrollPages(1);
      } else if (e.code === 'ArrowDown') {
        // 下移
        e.preventDefault();
        terminal.scrollLines(1);
      } else if (e.ctrlKey && e.code === 'KeyA') {
        // 全选
        e.preventDefault();
        this.selectAll();
        return false;
      } else if (e.ctrlKey && e.code === 'KeyC') {
        // 复制
        e.preventDefault();
        this.copy();
        return false;
      } else if (e.ctrlKey && e.code === 'KeyF') {
        // 搜索
        e.preventDefault();
        this.current.openSearch();
        return false;
      } else if (e.ctrlKey && e.code === 'KeyL') {
        // 清空
        e.preventDefault();
        this.clear();
        return false;
      }
      return true;
    });
  }

  // 初始化插件
  initAddons(terminal: Terminal): LogAddons {
    const fit = new FitAddon();
    const search = new SearchAddon();
    const webgl = new WebglAddon();
    const weblink = new WebLinksAddon();
    terminal.loadAddon(fit);
    terminal.loadAddon(search);
    terminal.loadAddon(webgl);
    terminal.loadAddon(weblink);
    return {
      fit,
      search,
      webgl,
      weblink
    };
  }

  // 初始化 client
  async openClient() {
    // 获取 token
    const { data } = await getExecLogTailToken(this.config);
    // 打开会话
    try {
      this.client = await createWebSocket(`${webSocketBaseUrl}/exec/log/${data}`);
    } catch (e) {
      Message.error('连接失败');
      console.error('log error', e);
      return;
    }
    this.client.onclose = event => {
      console.warn('log close', event);
    };
    this.client.onmessage = this.processMessage.bind(this);
    // 注册持久化
    this.keepAliveTask = setInterval(() => {
      if (this.client?.readyState === WebSocket.OPEN) {
        this.client?.send('p');
      }
    }, 15000);
  }

  // 设置当前元素
  setCurrent(id: number): void {
    const rel = this.appenderRel[id];
    if (!rel) {
      return;
    }
    this.current = rel;
    // 自适应
    rel.addons.fit.fit();
    this.focus();
  }

  // 打开搜索
  openSearch() {
    this.current.openSearch();
  }

  // 查找关键字
  find(word: string, next: boolean, options: any) {
    if (next) {
      this.current.addons.search.findNext(word, options);
    } else {
      this.current.addons.search.findPrevious(word, options);
    }
  }

  // 去顶部
  toTop(): void {
    this.current.terminal.scrollToTop();
    this.focus();
  }

  // 去底部
  toBottom(): void {
    this.current.terminal.scrollToBottom();
    this.focus();
  }

  // 添加字体大小
  addFontSize(addSize: number): void {
    this.current.terminal.options['fontSize'] = this.current.terminal.options['fontSize'] as number + addSize;
    this.current.addons.fit.fit();
    this.focus();
  }

  // 复制
  copy(): void {
    copyText(this.current.terminal.getSelection(), '已复制');
    this.focus();
  }

  // 复制全部
  copyAll(): void {
    this.selectAll();
    this.copy();
    this.current.terminal.clearSelection();
    this.focus();
  }

  // 选中全部
  selectAll(): void {
    this.current.terminal.selectAll();
    this.focus();
  }

  // 清空
  clear(): void {
    this.current.terminal.clear();
    this.current.terminal.clearSelection();
    this.focus();
  }

  // 聚焦
  focus(): void {
    this.current.terminal.focus();
  }

  // 自适应全部
  fitAll(): void {
    Object.values(this.appenderRel).forEach(s => {
      s.addons.fit.fit();
    });
  }

  // 关闭 client
  closeClient(): void {
    try {
      // 清理持久化
      clearInterval(this.keepAliveTask);
      // 关闭 ws
      if (this.client && this.client.readyState === WebSocket.OPEN) {
        this.client.close();
      }
    } catch (e) {
    }
  }

  // 关闭 view
  closeView(): void {
    // 移除自适应事件
    removeEventListen(window, 'resize', this.fitAllFn);
    // 关闭 terminal
    Object.values(this.appenderRel).forEach(s => {
      try {
        // 卸载插件
        Object.values(s.addons)
          .filter(Boolean)
          .forEach(s => s.dispose());
        // 卸载终端
        setTimeout(() => {
          s.terminal?.dispose();
        }, 300);
      } catch (e) {
        // 卸载可能会报错
      }
    });
  }

  // 关闭
  close(): void {
    this.closeClient();
    this.closeView();
  }

  // 处理消息
  processMessage({ data }: MessageEvent<string>) {
    // pong
    if (data === 'p') {
      return;
    }
    const separatorIndex = data.indexOf('|');
    const id = data.substring(0, separatorIndex);
    const text = data.substring(separatorIndex + 1, data.length);
    // 获取 appender
    const appender = this.appenderRel[id];
    if (!appender) {
      return;
    }
    appender.terminal.write(text);
  }

}
