import type { UnwrapRef } from 'vue';
import type { TerminalPreference } from '@/store/modules/terminal/types';
import type { ISshSession, ISshSessionHandler, ITerminalChannel, XtermAddons, XtermDomRef } from '../types/terminal.type';
import { useTerminalStore } from '@/store';
import { InputProtocol } from '../types/terminal.protocol';
import { fontFamilySuffix, TerminalShortcutType, TerminalStatus } from '../types/terminal.const';
import { Terminal } from 'xterm';
import { FitAddon } from 'xterm-addon-fit';
import { WebLinksAddon } from 'xterm-addon-web-links';
import { ISearchOptions, SearchAddon } from 'xterm-addon-search';
import { ImageAddon } from 'xterm-addon-image';
import { CanvasAddon } from 'xterm-addon-canvas';
import { WebglAddon } from 'xterm-addon-webgl';
import { playBell } from '@/utils/bell';
import { addEventListen } from '@/utils/event';
import SshSessionHandler from './ssh-session-handler';

// ssh 会话实现
export default class SshSession implements ISshSession {

  public readonly hostId: number;

  public sessionId: string;

  public inst: Terminal;

  public connected: boolean;

  public canWrite: boolean;

  public status: number;

  public handler: ISshSessionHandler;

  private readonly channel: ITerminalChannel;

  private readonly addons: XtermAddons;

  constructor(hostId: number,
              sessionId: string,
              channel: ITerminalChannel) {
    this.hostId = hostId;
    this.sessionId = sessionId;
    this.channel = channel;
    this.connected = false;
    this.canWrite = false;
    this.status = TerminalStatus.CONNECTING;
    this.inst = undefined as unknown as Terminal;
    this.handler = undefined as unknown as ISshSessionHandler;
    this.addons = {} as XtermAddons;
  }

  // 初始化
  init(domRef: XtermDomRef): void {
    const { preference } = useTerminalStore();
    // 初始化实例
    this.inst = new Terminal({
      ...(preference.displaySetting as any),
      theme: preference.theme.schema,
      fastScrollModifier: !!preference.interactSetting.fastScrollModifier ? 'alt' : 'none',
      altClickMovesCursor: !!preference.interactSetting.altClickMovesCursor,
      rightClickSelectsWord: !!preference.interactSetting.rightClickSelectsWord,
      fontFamily: preference.displaySetting.fontFamily + fontFamilySuffix,
      wordSeparator: preference.interactSetting.wordSeparator,
      scrollback: preference.sessionSetting.scrollBackLine,
    });
    // 处理器
    this.handler = new SshSessionHandler(this, domRef);
    // 注册快捷键
    this.registerShortcut(preference);
    // 注册事件
    this.registerEvent(domRef.el, preference);
    // 注册插件
    this.registerAddons(preference);
    // 打开终端
    this.inst.open(domRef.el);
    // 自适应
    this.addons.fit.fit();
  }

  // 注册快捷键
  private registerShortcut(preference: UnwrapRef<TerminalPreference>) {
    // 处理自定义按键
    this.inst.attachCustomKeyEventHandler((e: KeyboardEvent) => {
      if (e.type !== 'keydown') {
        return true;
      }
      // 检测是否阻止默认行为
      if (this.handler.checkPreventDefault(e)) {
        e.preventDefault();
      }
      if (preference.shortcutSetting.enabled && preference.shortcutSetting.keys.length) {
        // 获取触发的快捷键
        const shortcutKey = this.handler.getShortcutKey(e);
        // 触发终端快捷键
        if (shortcutKey?.type === TerminalShortcutType.TERMINAL) {
          this.handler.invokeHandle.call(this.handler, shortcutKey.item);
          return false;
        }
      }
      return true;
    });
  }

  // 注册事件
  private registerEvent(dom: HTMLElement, preference: UnwrapRef<TerminalPreference>) {
    // 注册输入事件
    this.inst.onData(s => {
      if (!this.canWrite || !this.connected) {
        return;
      }
      // 输入
      this.channel.send(InputProtocol.SSH_INPUT, {
        sessionId: this.sessionId,
        command: s
      });
    });
    // 启用响铃
    if (preference.interactSetting.enableBell) {
      this.inst.onBell(() => {
        // 播放蜂鸣
        playBell();
      });
    }
    // 选中复制
    if (preference.interactSetting.selectionChangeCopy) {
      this.inst.onSelectionChange(() => {
        // 复制选中内容
        this.handler.copy();
      });
    }
    // 注册 resize 事件
    this.inst.onResize(({ cols, rows }) => {
      if (!this.connected) {
        return;
      }
      this.channel.send(InputProtocol.SSH_RESIZE, {
        sessionId: this.sessionId,
        cols,
        rows
      });
    });
    // 设置右键选项
    addEventListen(dom, 'contextmenu', async () => {
      // 右键粘贴逻辑
      if (preference.interactSetting.rightClickPaste) {
        if (!this.canWrite || !this.connected) {
          return;
        }
        // 未开启右键选中 || 开启并无选中的内容则粘贴
        if (!preference.interactSetting.rightClickSelectsWord || !this.inst.hasSelection()) {
          this.handler.paste();
        }
      }
    });
  }

  // 注册插件
  private registerAddons(preference: UnwrapRef<TerminalPreference>) {
    this.addons.fit = new FitAddon();
    this.addons.search = new SearchAddon();
    // 超链接插件
    if (preference.pluginsSetting.enableWeblinkPlugin) {
      this.addons.weblink = new WebLinksAddon();
    }
    if (preference.pluginsSetting.enableWebglPlugin) {
      // WebGL 渲染插件
      this.addons.webgl = new WebglAddon(true);
    } else {
      // canvas 渲染插件
      this.addons.canvas = new CanvasAddon();
    }
    // 图片渲染插件
    if (preference.pluginsSetting.enableImagePlugin) {
      this.addons.image = new ImageAddon();
    }
    // 加载插件
    for (const addon of Object.values(this.addons)) {
      if (addon) {
        this.inst.loadAddon(addon);
      }
    }
  }

  // 设置已连接
  connect(): void {
    this.status = TerminalStatus.CONNECTED;
    this.connected = true;
    this.inst.focus();
  }

  // 设置是否可写
  setCanWrite(canWrite: boolean): void {
    this.canWrite = canWrite;
    if (canWrite) {
      this.inst.options.cursorBlink = useTerminalStore().preference.displaySetting.cursorBlink;
    } else {
      this.inst.options.cursorBlink = false;
    }
  }

  // 写入数据
  write(value: string | Uint8Array): void {
    this.inst.write(value);
  }

  // 聚焦
  focus(): void {
    this.inst.focus();
  }

  // 失焦
  blur(): void {
    this.inst.blur();
  }

  // 自适应
  fit(): void {
    this.addons.fit?.fit();
  }

  // 查找
  find(word: string, next: boolean, options: ISearchOptions): void {
    if (next) {
      this.addons.search.findNext(word, options);
    } else {
      this.addons.search.findPrevious(word, options);
    }
  }

  // 断开连接
  disconnect(): void {
    // 发送关闭消息
    this.channel.send(InputProtocol.CLOSE, {
      sessionId: this.sessionId
    });
  }

  // 关闭
  close(): void {
    try {
      // 卸载插件
      Object.values(this.addons)
        .filter(Boolean)
        .forEach(s => s.dispose());
      // 卸载终端
      this.inst.dispose();
    } catch (e) {
      // 卸载可能会报错
    }
  }

}
