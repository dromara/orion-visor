import type { IGuacdSession, IGuacdSessionClipboardHandler } from '@/views/terminal/interfaces';
import Guacamole from 'guacamole-common-js';
import { copyToClipboard } from '@/hooks/copy';
import { isString } from '@/utils/is';

// guacd 会话剪切板处理器实现
export default class GuacdSessionClipboardHandler implements IGuacdSessionClipboardHandler {

  private readonly session: IGuacdSession;

  constructor(session: IGuacdSession) {
    this.session = session;
  }

  // 发送数据到远程剪切板
  sendDataToRemoteClipboard(data: string | File | Blob): void {
    // 创建流
    const stream = this.session.client.createClipboardStream('text/plain');
    if (isString(data)) {
      // 写入纯文本
      let writer = new Guacamole.StringWriter(stream);
      writer.sendText(data as string);
      writer.sendEnd();
    } else {
      // 写入文件
      let writer = new Guacamole.BlobWriter(stream);
      writer.oncomplete = () => {
        writer.sendEnd();
      };
      writer.sendBlob(data);
    }
  }

  // 接收远程剪切板数据
  receiveRemoteClipboardData(stream: Guacamole.InputStream, mimetype: string): void {
    if (/^text\//.exec(mimetype)) {
      // 处理文本消息
      let reader = new Guacamole.StringReader(stream);
      let data = '';
      reader.ontext = function textReceived(text) {
        data += text;
      };
      reader.onend = async () => {
        await copyToClipboard(data);
      };
    } else {
      let reader = new Guacamole.BlobReader(stream, mimetype);
      reader.onend = async () => {
        const text = await reader.getBlob().text();
        await copyToClipboard(text);
      };
    }
  }

}
