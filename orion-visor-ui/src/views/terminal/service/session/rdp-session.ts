import type { IGuacdChannel, IGuacdSessionDisplayHandler, IRdpSession, TerminalSessionTabItem } from '@/views/terminal/interfaces';
import { InputProtocol } from '@/views/terminal/types/protocol';
import { fitDisplayValue } from '@/views/terminal/types/const';
import { Message } from '@arco-design/web-vue';
import { useTerminalStore } from '@/store';
import Guacamole from 'guacamole-common-js';
import RdpChannel from '../channel/rdp-channel';
import BaseGuacdSession from './base-guacd-session';
import GuacdSessionDisplayHandler from '../handler/guacd-session-display-handler';

export const AUDIO_INPUT_MIMETYPE = 'audio/L16;rate=44100,channels=2';

// RDP 会话实现
export default class RdpSession extends BaseGuacdSession implements IRdpSession {

  public fileSystemName: string;

  constructor(item: TerminalSessionTabItem) {
    super(item);
    this.fileSystemName = 'Shared Driver';
  }

  // 创建 channel
  protected createChannel(): IGuacdChannel {
    return new RdpChannel(this);
  }

  // 创建 display
  protected createDisplay(): IGuacdSessionDisplayHandler {
    const rdpGraphSetting = useTerminalStore().preference.rdpGraphSetting;
    // 创建 display
    const displayHandler = new GuacdSessionDisplayHandler(this);
    // 设置自适应
    const autoFit = rdpGraphSetting?.displaySize === fitDisplayValue;
    displayHandler.autoFit = autoFit;
    // 非自适应设置分辨率
    if (!autoFit) {
      displayHandler.setDisplaySize(rdpGraphSetting?.displayWidth || 1024, rdpGraphSetting?.displayHeight || 768);
    }
    return displayHandler;
  }

  // 注册 client 事件
  protected registerClientEvent() {
    super.registerClientEvent();
    // 注册文件系统回调
    this.client.onfilesystem = (_, fileSystemName) => {
      if (fileSystemName) {
        this.fileSystemName = fileSystemName;
      }
    };
    // 下载文件回调
    this.client.onfile = (stream, mimetype, filename) => {
      if (!this.isWriteable()) {
        Message.error('无写入权限');
        return;
      }
      // 记录事件
      this.onFileSystemEvent({ event: 'terminal:rdp-download', path: filename });
      // 下载文件
      useTerminalStore().transferManager.rdp.addDownload(this, stream, mimetype, filename);
    };
  }

  // 连接成功回调
  protected onConnected() {
    super.onConnected();
    // 监听音频输入
    if (useTerminalStore().preference.rdpSessionSetting?.enableAudioInput) {
      const requestAudioStream = (client: Guacamole.Client) => {
        const stream = client.createAudioStream(AUDIO_INPUT_MIMETYPE);
        let recorder;
        try {
          recorder = Guacamole.AudioRecorder.getInstance(stream, AUDIO_INPUT_MIMETYPE);
        } catch (e) {
          recorder = null;
        }
        if (!recorder) {
          // 创建失败
          stream.sendEnd();
        } else {
          // 完成重新回调
          recorder.onclose = requestAudioStream.bind(this, client);
        }
      };
      requestAudioStream(this.client);
    }
  }

  // 文件系统事件
  onFileSystemEvent(event: Record<string, any>): void {
    this.channel.send(InputProtocol.RDP_FILE_SYSTEM_EVENT, { event: JSON.stringify(event) });
  }

  // 断开连接
  disconnect(): void {
    super.disconnect();
    // 关闭文件传输
    useTerminalStore().transferManager.rdp.closeBySessionKey(this.sessionKey);
  }

}
