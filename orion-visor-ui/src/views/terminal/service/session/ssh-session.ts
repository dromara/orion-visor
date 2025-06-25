import type { ISshChannel, ISshSession, ISshSessionHandler, ReactiveSessionStatus, SshInitConfig, TerminalSessionTabItem } from '@/views/terminal/interfaces';
import type { UnwrapRef } from 'vue';
import type { ISearchOptions } from '@xterm/addon-search';
import { SearchAddon } from '@xterm/addon-search';
import type { TerminalPreference } from '@/store/modules/terminal/types';
import type { XtermAddons } from '@/types/xterm';
import { defaultFontFamily } from '@/types/xterm';
import { useTerminalStore } from '@/store';
import { InputProtocol } from '@/views/terminal/types/protocol';
import { TerminalShortcutType } from '@/views/terminal/types/const';
import { Terminal } from '@xterm/xterm';
import { FitAddon } from '@xterm/addon-fit';
import { WebLinksAddon } from '@xterm/addon-web-links';
import { ImageAddon } from '@xterm/addon-image';
import { Unicode11Addon } from '@xterm/addon-unicode11';
import { CanvasAddon } from '@xterm/addon-canvas';
import { WebglAddon } from '@xterm/addon-webgl';
import { playBell } from '@/utils/bell';
import { addEventListen } from '@/utils/event';
import { screenshot } from '@/views/terminal/types/utils';
import BaseSession from './base-session';
import SshChannel from '../channel/ssh-channel';
import SshSessionHandler from '../handler/ssh-session-handler';

// SSH 会话实现
export default class SshSession extends BaseSession<ReactiveSessionStatus, ISshChannel> implements ISshSession {

  public inst: Terminal;

  public config: SshInitConfig;

  public readonly handler: ISshSessionHandler;

  private readonly addons: XtermAddons;

  constructor(item: TerminalSessionTabItem) {
    super(item, {});
    this.config = {} as SshInitConfig;
    this.inst = new Terminal();
    this.handler = new SshSessionHandler(this);
    this.addons = {} as XtermAddons;
  }

  // 初始化
  async init(config: SshInitConfig): Promise<void> {
    this.config = config;
    // 获取偏好配置
    const { preference } = useTerminalStore();
    const fontFamily = preference.sshDisplaySetting.fontFamily;
    // 初始化实例
    this.inst.options = {
      ...(preference.sshDisplaySetting as any),
      theme: preference.theme.schema,
      fastScrollModifier: !!preference.interactSetting.fastScrollModifier ? 'alt' : 'none',
      altClickMovesCursor: !!preference.interactSetting.altClickMovesCursor,
      rightClickSelectsWord: !!preference.interactSetting.rightClickSelectsWord,
      wordSeparator: preference.interactSetting.wordSeparator,
      fontFamily: fontFamily === '_' ? defaultFontFamily : `${fontFamily}, ${defaultFontFamily}`,
      scrollback: preference.sessionSetting.scrollBackLine,
      allowProposedApi: true,
    };
    // 初始化 channel
    await this.initChannel();
    // 注册快捷键
    this.registerShortcut(preference);
    // 注册事件
    this.registerEvent(config.viewport, preference);
    // 注册插件
    this.registerAddons(preference);
    // 打开终端
    this.inst.open(config.viewport);
    // 自适应
    this.addons.fit?.fit();
  }

  // 重新初始化
  async reInit(): Promise<void> {
    // 初始化 channel
    await this.initChannel();
  }

  // 初始化 channel
  private async initChannel(): Promise<void> {
    this.channel = new SshChannel(this);
    // 初始化 channel
    await this.channel.init();
  }

  // 注册快捷键
  private registerShortcut(preference: UnwrapRef<TerminalPreference>) {
    // 处理自定义按键
    this.inst.attachCustomKeyEventHandler((e: KeyboardEvent) => {
      if (e.type !== 'keydown') {
        return true;
      }
      // 检测是否为内置快捷键
      if (this.handler.checkIsBuiltin(e)) {
        return true;
      }
      // 检测是否阻止默认行为
      if (this.handler.checkPreventDefault(e)) {
        e.preventDefault();
      }
      // 检查重新连接
      if (!this.status.connected && this.status.canReconnect && e.key === 'Enter') {
        // 防止重复回车
        this.status.canReconnect = false;
        // 异步作用域重新连接
        setTimeout(async () => {
          await useTerminalStore().reOpenSession(this.sessionKey);
        }, 50);
        return true;
      }
      // 自定义快捷键
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
      if (!this.status.canWrite || !this.status.connected) {
        return;
      }
      // 输入
      this.channel.send(InputProtocol.SSH_INPUT, {
        command: s
      });
    });
    // 启用响铃
    if (preference.interactSetting.enableBell) {
      this.inst.onBell(() => {
        // 播放响铃
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
      if (!this.status.connected) {
        return;
      }
      this.channel.send(InputProtocol.RESIZE, {
        width: cols,
        height: rows,
      });
    });
    // 设置右键选项
    addEventListen(dom, 'contextmenu', async () => {
      // 右键粘贴逻辑
      if (preference.interactSetting.rightClickPaste) {
        if (!this.status.canWrite || !this.status.connected) {
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
    if (preference.pluginsSetting.enableWebglPlugin && this.config.webglAvailable) {
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
    // unicode11 插件
    if (preference.pluginsSetting.enableUnicodePlugin) {
      this.addons.unicode = new Unicode11Addon();
    }
    // 加载插件
    for (const addon of Object.values(this.addons)) {
      if (addon) {
        this.inst.loadAddon(addon);
      }
    }
    // 设置 unicode11 版本
    if (this.addons.unicode) {
      this.inst.unicode.activeVersion = '11';
    }
  }

  // 连接会话
  connect(): void {
    // 设置连接中
    super.setConnecting();
    // 发送 connect 命令
    this.channel.send(InputProtocol.CONNECT, {
      body: JSON.stringify({
        width: this.inst.cols,
        height: this.inst.rows,
      })
    });
  }

  // 写入数据
  write(value: string): void {
    this.inst.write(value);
  }

  // 修改大小
  resize(width: number, height: number): void {
    if (this.inst.cols === width && this.inst.rows === height) {
      return;
    }
    this.inst.resize(width, height);
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

  // 截屏
  async screenshot() {
    await screenshot(this.inst.element as HTMLElement);
  }

  // 查找
  find(word: string, next: boolean, options: ISearchOptions): void {
    if (next) {
      this.addons.search.findNext(word, options);
    } else {
      this.addons.search.findPrevious(word, options);
    }
  }

  // 关闭
  close(): void {
    // 关闭会话
    super.close();
    try {
      // 卸载插件
      Object.values(this.addons)
        .filter(Boolean)
        .forEach(s => s.dispose());
      // 卸载终端
      setTimeout(() => {
        this.inst.dispose();
      }, 300);
    } catch (e) {
      // 卸载可能会报错
    }
  }

  // 设置已连接
  setConnected(): void {
    super.setConnected();
    // 聚焦
    this.inst.focus();
  }

  // 设置是否可写
  setCanWrite(canWrite: boolean): void {
    super.setCanWrite(canWrite);
    if (canWrite) {
      this.inst.options.cursorBlink = useTerminalStore().preference.sshDisplaySetting.cursorBlink;
    } else {
      this.inst.options.cursorBlink = false;
    }
  }

}
