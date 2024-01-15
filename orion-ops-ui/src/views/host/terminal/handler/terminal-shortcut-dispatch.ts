import type { TerminalShortcutKey } from '@/store/modules/terminal/types';
import type { ITerminalSession, ITerminalShortcutDispatcher } from '../types/terminal.type';
import useCopy from '@/hooks/copy';

const { readText } = useCopy();

// 终端快捷键调度实现
export default class TerminalShortcutDispatch implements ITerminalShortcutDispatcher {

  private readonly session: ITerminalSession;

  private readonly keys: Array<TerminalShortcutKey>;

  constructor(session: ITerminalSession, keys: Array<TerminalShortcutKey>) {
    this.session = session;
    // this.keys = keys;
    this.keys = [
      {
        option: 'copy',
        ctrlKey: true,
        shiftKey: true,
        altKey: false,
        key: 'C'
      }, {
        option: 'paste',
        ctrlKey: true,
        shiftKey: true,
        altKey: false,
        key: 'V'
      }, {
        option: 'toTop',
        ctrlKey: true,
        shiftKey: true,
        altKey: false,
        key: 'ArrowUp'
      }, {
        option: 'toBottom',
        ctrlKey: true,
        shiftKey: true,
        altKey: false,
        key: 'ArrowDown'
      }, {
        option: 'selectAll',
        ctrlKey: true,
        shiftKey: true,
        altKey: false,
        key: 'A'
      }, {
        option: 'search',
        ctrlKey: true,
        shiftKey: true,
        altKey: false,
        key: 'F'
      }, {
        option: 'fontSizePlus',
        ctrlKey: true,
        shiftKey: true,
        altKey: false,
        key: '+'
      }, {
        option: 'fontSizeSubtract',
        ctrlKey: true,
        shiftKey: true,
        altKey: false,
        key: '_'
      }, {
        option: 'commandEditor',
        ctrlKey: true,
        shiftKey: true,
        altKey: false,
        key: 'O'
      }, {
        option: 'close',
        ctrlKey: true,
        shiftKey: true,
        altKey: false,
        key: 'W'
      }
    ];
  }

  // 调度快捷键
  dispatch(e: KeyboardEvent): boolean {
    console.log(e);
    for (const key of this.keys) {
      if (key.altKey === e.altKey
        && key.shiftKey === e.shiftKey
        && key.ctrlKey === e.ctrlKey
        && key.key === e.key) {
        const runner = this[key.option as keyof this] as () => void;
        runner && runner.apply(this);
        return false;
      }
    }
    return true;
  }

  // 复制
  private copy(): void {
    this.session.copySelection();
  }

  // 粘贴
  private paste(): void {
    // FIXME status
    readText().then((e) => {
      this.session.pasteTrimEnd(e);
    });
  }

  // 去顶部
  private toTop(): void {
    this.session.toTop();
  }

  // 去底部
  private toBottom(): void {
    this.session.toBottom();
  }

  // 全选
  private selectAll(): void {
    this.session.selectAll();
  }

  // 搜索
  private search(): void {
    // fixme
  }

  // 增大字号
  private fontSizePlus(): void {
    this.fontSizeAdd(1);
  }

  // 减小字号
  private fontSizeSubtract(): void {
    this.fontSizeAdd(-1);
  }

  // 字号增加
  private fontSizeAdd(addSize: number) {
    this.session.setOption('fontSize', this.session.getOption('fontSize') + addSize);
    if (this.session.connected) {
      this.session.fit();
      this.session.focus();
    }
  }

  // 命令编辑器
  private commandEditor(): void {
    // fixme
  }

  // 关闭终端
  private close(): void {

  }

  private to(): void {

  }

  // 切换 tab
  // 打开 新

}
