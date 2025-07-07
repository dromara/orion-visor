import type { IGuacdSession, IGuacdSessionDisplayHandler } from '@/views/terminal/interfaces';
import { useDebounceFn } from '@vueuse/core';
import Guacamole from 'guacamole-common-js';

// guacd 会话视图处理器实现
export default class GuacdSessionDisplayHandler implements IGuacdSessionDisplayHandler {

  private readonly session: IGuacdSession;

  public displayWidth: number;
  public displayHeight: number;
  public displayDpi: number;
  public autoFit: boolean;
  public localCursor: boolean;

  private display?: Guacamole.Display;
  private sink?: Guacamole.InputSink;
  private mouse?: Guacamole.Mouse;
  private touchscreen?: Guacamole.Mouse.Touchscreen;
  private keyboard?: Guacamole.Keyboard;

  private readonly focusSink: () => void;

  constructor(session: IGuacdSession) {
    this.session = session;
    this.displayWidth = 0;
    this.displayHeight = 0;
    this.displayDpi = 96;
    this.autoFit = true;
    this.localCursor = true;
    this.focusSink = useDebounceFn(() => this.sink?.focus(), 300).bind(this);
  }

  // 初始化
  init(): void {
    // 清空
    this.session.config.viewport.innerHTML = '';
    // 设置大小
    if (this.autoFit) {
      this.setDisplaySize(this.session.config.viewport.offsetWidth, this.session.config.viewport.offsetHeight);
    }
    // 创建交互视图
    this.display = this.session.client.getDisplay();
    const displayElement = this.display.getElement();
    this.session.config.viewport.appendChild(displayElement);
    // 注册聚焦
    this.sink = new Guacamole.InputSink();
    const sinkElement = this.sink.getElement();
    this.session.config.viewport.appendChild(sinkElement);
    // 大小修改事件
    this.display.onresize = this.setDisplaySize.bind(this);
    // 光标移动事件
    this.display.oncursor = this.onCursor.bind(this);
    // 注册鼠标事件
    this.mouse = new Guacamole.Mouse(displayElement);
    this.mouse.on('mousemove', this.syncMouseState.bind(this));
    this.mouse.on('mouseup', this.syncMouseState.bind(this));
    this.mouse.on('mousedown', this.onMouseDown.bind(this));
    this.mouse.on('mouseout', this.onMouseOut.bind(this));
    // 注册触摸事件
    this.touchscreen = new Guacamole.Mouse.Touchscreen(this.session.config.viewport);
    this.touchscreen.on('mousemove', this.syncTouchscreenMouseState.bind(this));
    this.touchscreen.on('mouseup', this.syncTouchscreenMouseState.bind(this));
    this.touchscreen.on('mousedown', this.onTouchscreenMouseDown.bind(this));
    // 注册键盘事件
    this.keyboard = new Guacamole.Keyboard(sinkElement);
    this.keyboard.onkeyup = this.onKeyboardKeyup.bind(this);
    this.keyboard.onkeydown = this.onKeyboardKeydown.bind(this);
    // 隐藏光标
    this.display.showCursor(false);
    // 聚焦
    this.focusSink();
  }

  // 聚焦
  focus(): void {
    this.focusSink();
  }

  // 失焦
  blur(): void {
    this.keyboard?.reset();
  }

  // 自适应
  fit(force: boolean = false): void {
    const width = this.session.config?.viewport?.offsetWidth;
    const height = this.session.config?.viewport?.offsetHeight;
    if (!width || !height) {
      return;
    }
    // 大小不变
    if (width === this.displayWidth && height === this.displayHeight) {
      return;
    }
    // 自动 fit 或强制 fit
    if (this.autoFit || force) {
      this.resize(width, height);
    }
  }

  // 修改大小
  resize(width: number, height: number): void {
    // 是否可写
    if (!this.session.isWriteable()) {
      return;
    }
    this.session.resize(width, height);
  }

  // 设置显示大小
  setDisplaySize(width: number, height: number) {
    this.displayWidth = width;
    this.displayHeight = height;
  }

  // 更改光标事件
  private onCursor(canvas: HTMLCanvasElement, x: number, y: number) {
    // 是否可写
    if (!this.session.isWriteable()) {
      return;
    }
    this.localCursor = true;
  }

  // 鼠标按下事件
  private onMouseDown(event: any) {
    // 是否可写
    if (!this.session.isWriteable()) {
      return;
    }
    // 聚焦
    this.focusSink();
    // 同步光标状态
    this.syncMouseState(event);
  }

  // 鼠标移出事件
  private onMouseOut(event: any) {
    // 是否可写
    if (!this.session.isWriteable()) {
      return;
    }
    this.display?.showCursor(false);
  }

  // 同步鼠标状态
  private syncMouseState(event: any) {
    // 是否可写
    if (!this.session.isWriteable()) {
      return;
    }
    // 设置鼠标状态
    this.display?.showCursor(!this.localCursor);
    // 发送鼠标状态
    this.session.client.sendMouseState(event.state, true);
  }

  // 触摸按下事件
  private onTouchscreenMouseDown(event: any) {
    // 是否可写
    if (!this.session.isWriteable()) {
      return;
    }
    // 聚焦
    this.focusSink();
    // 同步光标状态
    this.syncTouchscreenMouseState(event);
  }

  // 触摸同步鼠标事件
  private syncTouchscreenMouseState(event: any) {
    // 是否可写
    if (!this.session.isWriteable()) {
      return;
    }
    // 设置鼠标状态
    this.display?.showCursor(true);
    // 发送鼠标状态
    this.session.client.sendMouseState(event.state, true);
  }

  // 键盘抬起事件
  private onKeyboardKeyup(key: number) {
    // 是否可写
    if (!this.session.isWriteable()) {
      return;
    }
    this.session.client.sendKeyEvent(0, key);
  }

  // 键盘按下事件
  private onKeyboardKeydown(key: number) {
    // 是否可写
    if (!this.session.isWriteable()) {
      return;
    }
    this.session.client.sendKeyEvent(1, key);
    // // 处理退格
    // if (key === 65288) {
    //   return false;
    // }
  }

}
