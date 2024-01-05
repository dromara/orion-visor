// 输入协议
export const InputProtocol = {
  // 主机连接检查
  CHECK: {
    type: 'ck',
    template: ['type', 'session', 'hostId']
  },
  // 连接主机
  CONNECT: {
    type: 'co',
    template: ['type', 'session', 'cols', 'rows']
  },
  // 关闭连接
  CLOSE: {
    type: 'cl',
    template: ['type', 'session']
  },
  // ping
  PING: {
    type: 'p',
    template: ['type']
  },
  // 修改大小
  RESIZE: {
    type: 'rs',
    template: ['type', 'session', 'cols', 'rows']
  },
  // 执行
  EXEC: {
    type: 'e',
    template: ['type', 'session', 'command']
  },
  // 输入
  INPUT: {
    type: 'i',
    template: ['type', 'session', 'command']
  }
};

// 输出协议
export const OutputProtocol = {
  // 主机连接检查
  CHECK: {
    type: 'ck',
    template: ['type', 'session', 'result', 'errorMessage']
  },
  // 主机连接
  CONNECT: {
    type: 'co',
    template: ['type', 'session', 'result', 'errorMessage']
  },
  // pong
  PONG: {
    type: 'p',
    template: ['type']
  },
  // 输出
  OUTPUT: {
    type: 'o',
    template: ['type', 'session', 'body']
  },
};
