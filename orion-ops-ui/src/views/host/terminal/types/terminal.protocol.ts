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
    template: ['type', 'sessionId', 'showHiddenFile', 'path']
  },
  // SFTP 创建文件夹
  SFTP_MKDIR: {
    type: 'mk',
    template: ['type', 'sessionId', 'path']
  },
  // SFTP 创建文件
  SFTP_TOUCH: {
    type: 'to',
    template: ['type', 'sessionId', 'path']
  },
  // SFTP 移动文件
  SFTP_MOVE: {
    type: 'mv',
    template: ['type', 'sessionId', 'path', 'target']
  },
  // SFTP 删除文件
  SFTP_REMOVE: {
    type: 'rm',
    template: ['type', 'sessionId', 'path']
  },
  // SFTP 修改文件权限
  SFTP_CHMOD: {
    type: 'cm',
    template: ['type', 'sessionId', 'path', 'mod']
  },
  // SFTP 获取内容
  SFTP_GET_CONTENT: {
    type: 'gc',
    template: ['type', 'sessionId', 'path']
  },
  // SFTP 修改内容
  SFTP_SET_CONTENT: {
    type: 'sc',
    template: ['type', 'sessionId', 'path', 'content']
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
    template: ['type', 'sessionId', 'path', 'result', 'body'],
    processMethod: 'processSftpList'
  },
  // SFTP 创建文件夹
  SFTP_MKDIR: {
    type: 'mk',
    template: ['type', 'sessionId', 'result', 'msg'],
    processMethod: 'processSftpMkdir'
  },
  // SFTP 创建文件
  SFTP_TOUCH: {
    type: 'to',
    template: ['type', 'sessionId', 'result', 'msg'],
    processMethod: 'processSftpTouch'
  },
  // SFTP 移动文件
  SFTP_MOVE: {
    type: 'mv',
    template: ['type', 'sessionId', 'result', 'msg'],
    processMethod: 'processSftpMove'
  },
  // SFTP 删除文件
  SFTP_REMOVE: {
    type: 'rm',
    template: ['type', 'sessionId', 'result', 'msg'],
    processMethod: 'processSftpRemove'
  },
  // SFTP 修改文件权限
  SFTP_CHMOD: {
    type: 'cm',
    template: ['type', 'sessionId', 'result', 'msg'],
    processMethod: 'processSftpChmod'
  },
  // SFTP 获取文件内容
  SFTP_GET_CONTENT: {
    type: 'gc',
    template: ['type', 'sessionId', 'path', 'result', 'content'],
    processMethod: 'processSftpGetContent'
  },
  // SFTP 修改文件内容
  SFTP_SET_CONTENT: {
    type: 'sc',
    template: ['type', 'sessionId', 'result', 'msg'],
    processMethod: 'processSftpSetContent'
  },
};
