import { Md5 } from 'ts-md5';
import { Ref } from 'vue';

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
 * 触发鼠标事件
 */
export const triggerMouseEvent = (ref: Ref, e = 'click') => {
  const event = new MouseEvent(e, {
    view: window,
    bubbles: true,
    cancelable: true,
  });
  ref.value.dispatchEvent(event);
};

/**
 * md5
 */
export function md5(plain: string): string {
  return Md5.hashStr(plain);
}

/**
 * 获取数据颜色
 */
export function dataColor(str: string, colors: string[]): string {
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
export const resetObject = (obj: any) => {
  Object.keys(obj).forEach(k => {
    obj[k] = undefined;
  });
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
 * 获取页面路由
 */
export function getRoute(url = location.href) {
  return url.substring(url.lastIndexOf('#') + 1).split('?')[0];
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

export default null;
