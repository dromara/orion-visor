// 输入协议
export const InputProtocol = {
  // 主机连接检查
  CHECK: {
    type: 'ck',
    template: ['type', 'sessionId', 'hostId', 'connectType']
  },
  // 连接主机
  CONNECT: {
    type: 'co',
    template: ['type', 'sessionId', 'terminalType', 'cols', 'rows']
  },
  // 关闭连接
  CLOSE: {
    type: 'cl',
    template: ['type', 'sessionId']
  },
  // ping
  PING: {
    type: 'p',
    template: ['type']
  },
  // SSH 修改大小
  SSH_RESIZE: {
    type: 'rs',
    template: ['type', 'sessionId', 'cols', 'rows']
  },
  // SSH 输入
  SSH_INPUT: {
    type: 'i',
    template: ['type', 'sessionId', 'command']
  },
  // SFTP 文件列表
  SFTP_LIST: {
    type: 'ls',
    template: ['type', 'sessionId', 'path']
  },
};

// 输出协议
export const OutputProtocol = {
  // 主机连接检查
  CHECK: {
    type: 'ck',
    template: ['type', 'sessionId', 'result', 'msg'],
    processMethod: 'processCheck'
  },
  // 主机连接
  CONNECT: {
    type: 'co',
    template: ['type', 'sessionId', 'result', 'msg'],
    processMethod: 'processConnect'
  },
  // 主机连接关闭
  CLOSE: {
    type: 'cl',
    template: ['type', 'sessionId', 'msg'],
    processMethod: 'processClose'
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
    template: ['type', 'sessionId', 'body'],
    processMethod: 'processSshOutput'
  },
  // SFTP 文件列表
  SFTP_LIST: {
    type: 'ls',
    template: ['type', 'sessionId', 'result', 'path', 'body'],
    processMethod: 'processSftpList'
  },
};
