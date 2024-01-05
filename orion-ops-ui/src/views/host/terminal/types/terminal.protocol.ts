// 终端协议
export interface Protocol {
  type: string;
  template: string[];
}

// 终端内容
export interface Payload {
  type?: string;
  session?: string;

  [key: string]: unknown;
}

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

// 分隔符
export const SEPARATOR = '|';

// 解析参数
export const parse = (payload: string) => {
  const protocols = Object.values(OutputProtocol);
  const useProtocol = protocols.find(p => payload.startsWith(p.type + SEPARATOR) || p.type === payload);
  if (!useProtocol) {
    return undefined;
  }
  const template = useProtocol.template;
  const res: Record<string, any> = {};
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
export const format = (protocol: Protocol, payload: Payload) => {
  payload.type = protocol.type;
  return protocol.template
    .map(i => payload[i] || '')
    .join(SEPARATOR);
};
