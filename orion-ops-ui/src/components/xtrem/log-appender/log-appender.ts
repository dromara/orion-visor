import type { ExecTailRequest } from '@/api/exec/exec';
import { getExecLogTailToken } from '@/api/exec/exec';
import type { ILogAppender, LogAddons, LogAppenderConf, LogDomRef } from './appender.const';
import { AppenderOptions } from './appender.const';
import { webSocketBaseUrl } from '@/utils/env';
import { Message } from '@arco-design/web-vue';
import { createWebSocket } from '@/utils';
import { Terminal } from 'xterm';
import { FitAddon } from 'xterm-addon-fit';
import { SearchAddon } from 'xterm-addon-search';
import { CanvasAddon } from 'xterm-addon-canvas';
import { useDebounceFn } from '@vueuse/core';
import { addEventListen, removeEventListen } from '@/utils/event';

// todo SEARCH addon setfixed
// todo font-size totop copy tobottom selectall clear
// todo 批量执行的 warn

// 执行日志 appender 实现
export default class LogAppender implements ILogAppender {

  private config: ExecTailRequest;

  private client?: WebSocket;

  private readonly appenderRel: Record<string, LogAppenderConf>;

  private keepAliveTask?: number;

  private readonly fitFn: () => {};

  constructor(config: ExecTailRequest) {
    this.config = config;
    this.appenderRel = {};
    this.fitFn = useDebounceFn(this.fit).bind(this);
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
    // 注册自适应事件
    addEventListen(window, 'resize', this.fitFn);
  }

  // 初始化插件
  initAddons(terminal: Terminal): LogAddons {
    const fit = new FitAddon();
    const search = new SearchAddon();
    const canvas = new CanvasAddon();
    terminal.loadAddon(fit);
    terminal.loadAddon(search);
    terminal.loadAddon(canvas);
    return {
      fit,
      search,
      canvas
    };
  }

  // 初始化 client
  async openClient() {
    // 获取 token
    const { data } = await getExecLogTailToken(this.config);
    // 打开会话
    this.client = await createWebSocket(`${webSocketBaseUrl}/exec/log/${data}`);
    this.client.onerror = event => {
      Message.error('连接失败');
      console.error('log error', event);
    };
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

  // 自适应
  fit(): void {
    Object.values(this.appenderRel).forEach(s => {
      console.log(s);
      s.addons?.fit?.fit();
    });
  }

  // 关闭 client
  closeClient(): void {
    // 关闭 ws
    if (this.client && this.client.readyState === WebSocket.OPEN) {
      this.client.close();
    }
    // 清理持久化
    clearInterval(this.keepAliveTask);
  }

  // 关闭 view
  closeView(): void {
    // 关闭 terminal
    Object.values(this.appenderRel).forEach(s => {
      s.terminal?.dispose();
      if (s.addons) {
        Object.values(s.addons).forEach(s => s.dispose());
      }
    });
    // 移除自适应事件
    removeEventListen(window, 'resize', this.fitFn);
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
