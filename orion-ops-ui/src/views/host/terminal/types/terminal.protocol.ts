// 输入协议
export const InputProtocol = {
  // 主机连接检查
  CHECK: {
    type: 'ck',
    template: ['type', 'sessionId', 'hostId']
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
  // 修改大小
  RESIZE: {
    type: 'rs',
    template: ['type', 'sessionId', 'cols', 'rows']
  },
  // 执行
  EXEC: {
    type: 'e',
    template: ['type', 'sessionId', 'command']
  },
  // 输入
  INPUT: {
    type: 'i',
    template: ['type', 'sessionId', 'command']
  }
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
  // 输出
  OUTPUT: {
    type: 'o',
    template: ['type', 'sessionId', 'body'],
    processMethod: 'processOutput'
  },
};
