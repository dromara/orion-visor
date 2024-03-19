import { Md5 } from 'ts-md5';

type TargetContext = '_self' | '_parent' | '_blank' | '_top';

/**
 * 打开新窗口
 */
export const openWindow = (
  url: string,
  opts?: { target?: TargetContext; [key: string]: any }
) => {
  const { target = '_blank', ...others } = opts || {};
  window.open(
    url,
    target,
    Object.entries(others)
      .reduce((preValue: string[], curValue) => {
        const [key, value] = curValue;
        return [...preValue, `${key}=${value}`];
      }, [])
      .join(',')
  );
};

/**
 * url 正则
 */
export const regexUrl = new RegExp(
  '^(?!mailto:)(?:(?:http|https|ftp)://)(?:\\S+(?::\\S*)?@)?(?:(?:(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[0-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\u00a1-\\uffff0-9]+-?)*[a-z\\u00a1-\\uffff0-9]+)(?:\\.(?:[a-z\\u00a1-\\uffff0-9]+-?)*[a-z\\u00a1-\\uffff0-9]+)*(?:\\.(?:[a-z\\u00a1-\\uffff]{2,})))|localhost)(?::\\d{2,5})?(?:(/|\\?|#)[^\\s]*)?$',
  'i'
);

/**
 * md5
 */
export function md5(plain: string): string {
  return Md5.hashStr(plain);
}

/**
 * 获取数据颜色
 */
export function dataColor(str: string, colors: string[], defaultColor = ''): string {
  if (!colors?.length) {
    return defaultColor;
  }
  let total = 0;
  for (let i = 0; i < str.length; i++) {
    total += str.charCodeAt(i);
  }
  return colors[total % colors.length];
}

/**
 * 判断值是否非空
 */
export function isEmptyStr(val: any) {
  return typeof (val) === 'undefined' || val == null || val === '';
}

export const YMD_HMS = 'yyyy-MM-dd HH:mm:ss';

/**
 * 格式化时间
 */
export function dateFormat(date = new Date(), pattern = YMD_HMS) {
  const o = {
    'M+': date.getMonth() + 1,
    'd+': date.getDate(),
    'H+': date.getHours(),
    'm+': date.getMinutes(),
    's+': date.getSeconds(),
    'q+': Math.floor((date.getMonth() + 3) / 3),
    'S+': date.getMilliseconds()
  };
  let reg = /(y+)/;
  if (reg.test(pattern)) {
    // @ts-ignore
    const match = reg.exec(pattern)[1];
    pattern = pattern.replace(match, (date.getFullYear() + '').substring(4 - match.length));
  }
  for (const k in o) {
    let reg = new RegExp('(' + k + ')');
    if (reg.test(pattern)) {
      // @ts-ignore
      const match = reg.exec(pattern)[1];
      // @ts-ignore
      pattern = pattern.replace(match, (match.length === 1) ? o[k] : ('00' + o[k]).substring(('' + o[k]).length));
    }
  }
  return pattern;
}

/**
 * 格式化秒
 */
export function formatSecond(second: number, p = 'HH:mm') {
  return dateFormat(new Date(~~second * 1000), p);
}

/**
 * 格式化持续时间
 * @param start
 * @param end
 */
export function formatDuration(start: number, end?: number): string {
  if (!end) {
    return '';
  }
  const duration = (end - start) / 1000;
  if (duration < 1) {
    return `${duration.toFixed(1)}s`;
  }
  const minutes = Math.floor(duration / 60);
  const seconds = Math.floor(duration % 60);
  let result = '';
  if (minutes > 0) {
    result += `${minutes}min`;
    if (seconds > 0) {
      result += ' ';
    }
  }
  if (seconds > 0) {
    result += `${seconds}s`;
  }
  return result;
}

/**
 * 格式化数字为 ,分割
 */
export function formatNumber(value: number = 0) {
  const list = (value + '').split('.');
  const prefix = list[0].charAt(0) === '-' ? '-' : '';
  let num = prefix ? list[0].slice(1) : list[0];
  let result = '';
  while (num.length > 3) {
    result = `,${num.slice(-3)}${result}`;
    num = num.slice(0, num.length - 3);
  }
  if (num) {
    result = num + result;
  }
  return `${prefix}${result}${list[1] ? `.${list[1]}` : ''}`;
}

/**
 * 判断是否为数字
 */
export function isNumber(value: any, decimal = true, negative = true) {
  let reg;
  if (decimal && negative) {
    reg = /^-?[0-9]*(\.[0-9]*)?$/;
  } else if (!decimal && negative) {
    reg = /^-?[0-9]*$/;
  } else if (decimal && !negative) {
    reg = /^[0-9]*(\.[0-9]*)?$/;
  } else if (!decimal && !negative) {
    reg = /^[0-9]*$/;
  } else {
    return false;
  }
  return (!isNaN(value) && reg.test(value)) || value === '';
}

/**
 * 替换数字
 */
export function replaceNumber(value: string) {
  const s = value.charAt(value.length - 1);
  if (s === '.' || s === '-') {
    return s.substring(0, value.length - 1);
  }
  return value;
}

/**
 * 重设对象
 */
export const resetObject = (obj: any, ignore: string[] = []) => {
  Object.keys(obj)
    .filter(s => !ignore.includes(s))
    .forEach(k => {
      obj[k] = undefined;
    });
};

/**
 * 重设对象
 */
export const objectTruthKeyCount = (obj: any, ignore: string[] = []) => {
  return Object.keys(obj)
    .filter(s => !ignore.includes(s))
    .reduce(function(acc, curr) {
      const currVal = obj[curr];
      return acc + ~~(currVal !== undefined && currVal !== null && currVal?.length !== 0 && currVal !== '');
    }, 0);
};

/**
 * 创建 websocket
 */
export const createWebSocket = async (url: string) => {
  return new Promise<WebSocket>((resolve, reject) => {
    const socket = new WebSocket(url);

    socket.addEventListener('open', () => {
      resolve(socket);
    });

    socket.addEventListener('error', (error) => {
      reject(error);
    });
  });
};

/**
 * 休眠
 */
export const sleep = (ms: number) => {
  return new Promise(resolve => setTimeout(resolve, ms));
};

/**
 * 获取当前页面的缩放值
 */
export function detectZoom() {
  let ratio = 0;
  if (window.devicePixelRatio !== undefined) {
    ratio = window.devicePixelRatio;
  } else if (window.outerWidth !== undefined && window.innerWidth !== undefined) {
    ratio = window.outerWidth / window.innerWidth;
  }
  if (ratio) {
    ratio = Math.round(ratio * 100);
  }
  return ratio;
}

/**
 * 获取唯一的 UUID
 */
export function getUUID() {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
    const r = Math.random() * 16 | 0;
    const v = c === 'x' ? r : (r & 0x3 | 0x8);
    return v.toString(16);
  });
}

/**
 * 获取会话id
 */
export const nextId = (len: number): string => {
  return getUUID().replaceAll('-', '').substring(0, len);
};

/**
 * 清除 xss
 */
export function cleanXss(s: string) {
  return s.replaceAll('&', '&amp;')
    .replaceAll('<', '&lt;')
    .replaceAll('>', '&gt;')
    .replaceAll('\'', '&apos;')
    .replaceAll('"', '&quot;')
    .replaceAll('\n', '<br/>')
    .replaceAll('\t', '&nbsp;&nbsp;&nbsp;&nbsp;');
}

/**
 * 替换 html 标签
 */
export function replaceHtmlTag(message: string) {
  return cleanXss(message)
    .replaceAll('&lt;sb&gt;', '<span class="span-blue mx0">')
    .replaceAll('&lt;sb 2&gt;', '<span class="span-blue mx2">')
    .replaceAll('&lt;sb 4&gt;', '<span class="span-blue mx4">')
    .replaceAll('&lt;/sb&gt;', '</span>')
    .replaceAll('&lt;sr&gt;', '<span class="span-red mx0">')
    .replaceAll('&lt;sr 2&gt;', '<span class="span-red mx2">')
    .replaceAll('&lt;sr 4&gt;', '<span class="span-red mx4">')
    .replaceAll('&lt;/sr&gt;', '</span>')
    .replaceAll('&lt;b&gt;', '<b>')
    .replaceAll('&lt;/b&gt;', '</b>');
}

/**
 * 清除 html 标签
 */
export function clearHtmlTag(message: string) {
  return cleanXss(message)
    .replaceAll('&lt;sb 0&gt;', '')
    .replaceAll('&lt;sb 2&gt;', '')
    .replaceAll('&lt;sb&gt;', '')
    .replaceAll('&lt;/sb&gt;', '')
    .replaceAll('&lt;sr 0&gt;', '')
    .replaceAll('&lt;sr 2&gt;', '')
    .replaceAll('&lt;sr&gt;', '')
    .replaceAll('&lt;/sr&gt;', '')
    .replaceAll('&lt;b&gt;', '')
    .replaceAll('&lt;/b&gt;', '')
    .replaceAll('<br/>', '\n');
}

export default null;
