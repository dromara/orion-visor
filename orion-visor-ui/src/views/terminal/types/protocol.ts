// 终端协议
export interface Protocol {
  type: string;
  template: string[];

  [key: string]: unknown;
}

// 终端输入消息内容
export interface InputPayload {
  type?: string;

  [key: string]: unknown;
}

// 终端输出消息内容
export interface OutputPayload {
  type: string;

  [key: string]: string;
}

// 分隔符
export const SEPARATOR = '|';

// 输入协议
export const InputProtocol = {
  // 连接主机
  CONNECT: {
    type: 'co',
    template: ['type', 'body']
  },
  // 关闭连接
  CLOSE: {
    type: 'cl',
    template: ['type']
  },
  // ping
  PING: {
    type: 'p',
    template: ['type']
  },
  // 修改大小
  RESIZE: {
    type: 'rs',
    template: ['type', 'width', 'height']
  },
  // SSH 输入
  SSH_INPUT: {
    type: 'i',
    template: ['type', 'command']
  },
  // SFTP 文件列表
  SFTP_LIST: {
    type: 'ls',
    template: ['type', 'showHiddenFile', 'path']
  },
  // SFTP 创建文件夹
  SFTP_MKDIR: {
    type: 'mk',
    template: ['type', 'path']
  },
  // SFTP 创建文件
  SFTP_TOUCH: {
    type: 'to',
    template: ['type', 'path']
  },
  // SFTP 移动文件
  SFTP_MOVE: {
    type: 'mv',
    template: ['type', 'path', 'target']
  },
  // SFTP 删除文件
  SFTP_REMOVE: {
    type: 'rm',
    template: ['type', 'path']
  },
  // SFTP 修改文件权限
  SFTP_CHMOD: {
    type: 'chm',
    template: ['type', 'path', 'mod']
  },
  // SFTP 修改文件权限
  SFTP_DOWNLOAD_FLAT_DIRECTORY: {
    type: 'df',
    template: ['type', 'currentPath', 'path']
  },
  // SFTP 获取内容
  SFTP_GET_CONTENT: {
    type: 'gc',
    template: ['type', 'path']
  },
  // SFTP 修改内容
  SFTP_SET_CONTENT: {
    type: 'sc',
    template: ['type', 'path']
  },
  // guacd 指令
  GUACD_INSTRUCTION: {
    type: 'gi',
    template: ['type', 'instruction']
  }
};

// 输出协议
export const OutputProtocol = {
  // 设置id
  SET_ID: {
    type: 'id',
    template: ['type', 'sessionId'],
    processMethod: 'processSetId'
  },
  // 设置信息
  SET_INFO: {
    type: 'if',
    template: ['type', 'info'],
    processMethod: 'processSetInfo'
  },
  // 已连接
  CONNECTED: {
    type: 'co',
    template: ['type'],
    processMethod: 'processConnected'
  },
  // 已关闭
  CLOSED: {
    type: 'cl',
    template: ['type', 'code', 'msg'],
    processMethod: 'processClosed'
  },
  // 修改大小
  RESIZE: {
    type: 'rs',
    template: ['type', 'width', 'height'],
    processMethod: 'processResize'
  },
  // pong
  PONG: {
    type: 'p',
    template: ['type'],
    processMethod: 'processPong'
  },
  // SSH 输出
  SSH_OUTPUT: {
    type: 'o',
    template: ['type', 'body'],
    processMethod: 'processSshOutput'
  },
  // SFTP 文件列表
  SFTP_LIST: {
    type: 'ls',
    template: ['type', 'path', 'result', 'msg', 'body'],
    processMethod: 'processSftpList'
  },
  // SFTP 创建文件夹
  SFTP_MKDIR: {
    type: 'mk',
    template: ['type', 'result', 'msg'],
    processMethod: 'processSftpMkdir'
  },
  // SFTP 创建文件
  SFTP_TOUCH: {
    type: 'to',
    template: ['type', 'result', 'msg'],
    processMethod: 'processSftpTouch'
  },
  // SFTP 移动文件
  SFTP_MOVE: {
    type: 'mv',
    template: ['type', 'result', 'msg'],
    processMethod: 'processSftpMove'
  },
  // SFTP 删除文件
  SFTP_REMOVE: {
    type: 'rm',
    template: ['type', 'result', 'msg'],
    processMethod: 'processSftpRemove'
  },
  // SFTP 修改文件权限
  SFTP_CHMOD: {
    type: 'chm',
    template: ['type', 'result', 'msg'],
    processMethod: 'processSftpChmod'
  },
  // SFTP 修改文件权限
  SFTP_DOWNLOAD_FLAT_DIRECTORY: {
    type: 'df',
    template: ['type', 'currentPath', 'result', 'msg', 'body'],
    processMethod: 'processDownloadFlatDirectory'
  },
  // SFTP 获取文件内容
  SFTP_GET_CONTENT: {
    type: 'gc',
    template: ['type', 'result', 'msg', 'token'],
    processMethod: 'processSftpGetContent'
  },
  // SFTP 修改文件内容
  SFTP_SET_CONTENT: {
    type: 'sc',
    template: ['type', 'result', 'msg', 'token'],
    processMethod: 'processSftpSetContent'
  },
  // guacd 指令
  GUACD_INSTRUCTION: {
    type: 'gi',
    template: ['type', 'instruction'],
    processMethod: 'processInstruction'
  }
};

// 解析参数
export const parse = (payload: string) => {
  const protocols = Object.values(OutputProtocol);
  const useProtocol = protocols.find(p => payload.startsWith(p.type + SEPARATOR) || p.type === payload);
  if (!useProtocol) {
    return undefined;
  }
  const template = useProtocol.template;
  const res = {} as OutputPayload;
  let curr = 0;
  let len = payload.length;
  for (let i = 0, pl = template.length; i < pl; i++) {
    if (i == pl - 1) {
      // 最后一次
      res[template[i]] = payload.substring(curr, len);
    } else {
      // 非最后一次
      let tmp = '';
      for (; curr < len; curr++) {
        const c = payload.charAt(curr);
        if (c == SEPARATOR) {
          res[template[i]] = tmp;
          curr++;
          break;
        } else {
          tmp += c;
        }
      }
    }
  }
  return res;
};

// 格式化参数
export const format = (protocol: Protocol, payload: InputPayload | OutputPayload) => {
  payload.type = protocol.type;
  return protocol.template
    .map(i => getPayloadValueString(payload[i]))
    .join(SEPARATOR);
};

// 获取默认值
export const getPayloadValueString = (value: unknown): any => {
  if (value === undefined || value === null) {
    return '';
  }
  return value;
};
