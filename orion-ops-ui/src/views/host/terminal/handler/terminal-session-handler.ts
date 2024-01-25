import type { ShortcutKey, TerminalInteractSetting, TerminalShortcutKey } from '@/store/modules/terminal/types';
import type { ITerminalSession, ITerminalSessionHandler, ITerminalTabManager, TerminalDomRef } from '../types/terminal.type';
import type { Terminal } from 'xterm';
import useCopy from '@/hooks/copy';
import html2canvas from 'html2canvas';
import { useTerminalStore, useUserStore } from '@/store';
import { InnerTabs } from '../types/terminal.const';
import { saveAs } from 'file-saver';
import { Message } from '@arco-design/web-vue';
import { dateFormat } from '@/utils';

// 组织默认行为的快捷键
const preventKeys: Array<ShortcutKey> = [
  {
    ctrlKey: true,
    altKey: false,
    shiftKey: true,
    code: 'KeyV'
  }, {
    ctrlKey: false,
    altKey: false,
    shiftKey: true,
    code: 'Insert'
  },
];

const { copy: copyValue, readText } = useCopy();

// 终端会话处理器实现
export default class TerminalSessionHandler implements ITerminalSessionHandler {

  private readonly domRef: TerminalDomRef;

  private readonly inst: Terminal;

  private readonly session: ITerminalSession;

  private readonly interactSetting: TerminalInteractSetting;

  private readonly shortcutKeys: Array<TerminalShortcutKey>;

  private readonly tabManager: ITerminalTabManager;

  constructor(session: ITerminalSession,
              domRef: TerminalDomRef) {
    this.session = session;
    this.inst = session.inst;
    this.domRef = domRef;
    const { preference, tabManager } = useTerminalStore();
    this.interactSetting = preference.interactSetting;
    this.shortcutKeys = preference.shortcutSetting.keys;
    this.tabManager = tabManager;
  }

  // 检测是否忽略默认行为
  checkPreventDefault(e: KeyboardEvent): boolean {
    if (e.type !== 'keydown') {
      return false;
    }
    return !!preventKeys.find(key => {
      return key.code === e.code
        && key.altKey === e.altKey
        && key.shiftKey === e.shiftKey
        && key.ctrlKey === e.ctrlKey;
    });
  }

  // 启用状态
  enabledStatus(option: string): boolean {
    switch (option) {
      case 'paste':
      case 'pasteTrimEnd':
      case 'pasteOrigin':
      case 'interrupt':
      case 'enter':
      case 'commandEditor':
      case 'checkAppendMissing':
        return this.session.canWrite;
      case 'disconnect':
        return this.session.connected;
      default:
        return true;
    }
  }

  // 调用处理方法
  invokeHandle(option: string) {
    // 检测启用状态
    if (!this.enabledStatus(option)) {
      return;
    }
    // 调用实际处理方法
    const handler = this[option as keyof this] as () => void;
    handler && handler.call(this);
  }

  // 触发快捷键
  triggerShortcutKey(e: KeyboardEvent): boolean {
    // 检测触发的快捷键
    const key = this.shortcutKeys.find(key => {
      return key.code === e.code
        && key.altKey === e.altKey
        && key.shiftKey === e.shiftKey
        && key.ctrlKey === e.ctrlKey;
    });
    if (key) {
      // 调用处理方法
      this.invokeHandle.call(this, key.item);
      return false;
    } else {
      return true;
    }
  }

  // 复制选中
  copy() {
    let selection = this.inst.getSelection();
    if (selection) {
      // 去除尾部空格
      if (this.interactSetting.copyAutoTrim) {
        selection = selection.trimEnd();
      }
      // 复制
      copyValue(selection, false);
    }
    // 聚焦
    this.inst.focus();
  }

  // 从剪切板粘贴并且去除尾部空格 (如果配置)
  paste() {
    readText().then(s => this.pasteTrimEnd(s));
  }

  // 粘贴并且去除尾部空格 (如果配置)
  pasteTrimEnd(value: string) {
    if (this.interactSetting.pasteAutoTrim) {
      // 粘贴前去除尾部空格
      this.inst.paste(value.trimEnd());
    } else {
      this.inst.paste(value);
    }
    this.inst.focus();
  }

  // 粘贴原文
  pasteOrigin(value: string) {
    this.inst.paste(value);
    this.inst.focus();
  }

  // 选中全部
  selectAll() {
    this.inst.selectAll();
    this.inst.focus();
  }

  // 去顶部
  toTop(): void {
    this.inst.scrollToTop();
    this.inst.focus();
  }

  // 去底部
  toBottom(): void {
    this.inst.scrollToBottom();
    this.inst.focus();
  }

  // 打开搜索
  search() {
    this.domRef.searchModal?.toggle();
  }

  // 增大字号
  fontSizePlus() {
    this.fontSizeAdd(1);
  }

  // 减小字号
  fontSizeSubtract() {
    this.fontSizeAdd(-1);
  }

  // 字号增加
  private fontSizeAdd(addSize: number) {
    this.inst.options['fontSize'] = this.inst.options['fontSize'] as number + addSize;
    if (this.session.connected) {
      this.session.fit();
      this.inst.focus();
    }
  }

  // 打开命令编辑器
  commandEditor() {
    this.domRef.editorModal?.open('', '');
  }

  // ctrl + c
  interrupt() {
    this.inst.paste(String.fromCharCode(3));
  }

  // 回车
  enter() {
    this.inst.paste(String.fromCharCode(13));
  }

  // 清空
  clear() {
    this.inst.clear();
    this.inst.clearSelection();
    this.inst.focus();
  }

  // 断开连接
  disconnect() {
    this.session.disconnect();
  }

  // 检查追加缺失的部分
  checkAppendMissing(value: string): void {
    // 获取最后一行数据
    const buffer = this.inst.buffer?.active;
    let lastLine = '';
    if (buffer) {
      for (let i = buffer.viewportY + buffer.cursorY; i >= 0; i--) {
        lastLine = (buffer.getLine(i)?.translateToString() || '').trimEnd() + lastLine;
        if (lastLine.length > value.length) {
          break;
        }
      }
    }
    // 边界检查
    const lastLineLen = lastLine.length;
    const spinPartLen = value.length;
    let checkEnd;
    if (spinPartLen >= lastLineLen) {
      checkEnd = lastLineLen;
    } else {
      checkEnd = spinPartLen;
    }
    // 获取缺失的数据
    let append = undefined;
    for (let i = 0; i < checkEnd; i++) {
      if (lastLine.endsWith(value.substring(0, checkEnd - i))) {
        append = value.substring(checkEnd - i, spinPartLen);
        break;
      }
    }
    // 全部缺失
    if (append == undefined) {
      append = value;
    }
    // 追加
    this.pasteOrigin(append);
  }

  // 截图
  async screenshot() {
    try {
      // 获取截屏
      const canvas = await html2canvas(this.inst.element as HTMLElement, {
        useCORS: true,
        backgroundColor: 'transparent',
      });

      // 绘制水印
      const ctx = canvas.getContext('2d') as CanvasRenderingContext2D;
      const wPx = canvas.style.width;
      const hPx = canvas.style.height;
      const w = Number.parseInt(wPx.substring(0, wPx.length - 2));
      const h = Number.parseInt(hPx.substring(0, hPx.length - 2));
      const fontSize = 14;
      ctx.fillStyle = useTerminalStore().preference.theme.dark ? 'rgba(255, 255, 255, 0.1)' : 'rgba(0, 0, 0, 0.1)';
      ctx.font = `${fontSize}px Arial`;
      ctx.rotate(-24 * Math.PI / 180);
      // 水印内容
      const watermark = useUserStore().username || '';
      const time = '(' + dateFormat() + ')';
      const textWidth = ctx.measureText(time.length > watermark.length ? time : watermark).width * 1.5;
      const textHeight = (textWidth / 4 * 3) * 1.5;
      // 绘制文本
      for (let yi = -1; yi < h / textHeight + 2; yi++) {
        for (let xi = -1; xi < w / textWidth + 2; xi++) {
          if ((xi % 2 === 0 && yi % 2 === 0) || (xi % 2 !== 0 && yi % 2 !== 0)) {
            continue;
          }
          ctx.fillText(watermark, textWidth * (xi - 1), textHeight * (yi + 1));
          ctx.fillText(time, textWidth * (xi - 1), textHeight * (yi + 1) + fontSize * 1.12);
        }
      }

      // 保存图片
      const blob = await new Promise((resolve, reject) => {
        canvas.toBlob((blob) => {
          if (!blob) {
            reject();
          }
          resolve(blob);
        }, 'image/png');
      });
      saveAs(blob as Blob, `screenshot-${Date.now()}.png`);
    } catch (e) {
      Message.error('保存失败');
    }
  }

  // 关闭 tab
  closeTab() {
    this.tabManager.deleteTab(this.session.sessionId);
  }

  // 切换到前一个 tab
  changeToPrevTab() {
    this.tabManager.changeToPrevTab();
  }

  // 切换到后一个 tab
  changeToNextTab() {
    this.tabManager.changeToNextTab();
  }

  // 复制终端 tab
  openCopyTerminalTab() {
    useTerminalStore().openCopyTerminal(this.session.hostId);
  }

  // 打开新建连接 tab
  openNewConnectTab() {
    this.tabManager.openTab(InnerTabs.NEW_CONNECTION);
  }

}
