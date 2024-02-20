import { isEmptyStr } from './index';

/**
 * 获取 base64 实际数据
 */
export function getBase64Data(e: string) {
  return e.substring(e.indexOf(',') + 1);
}

/**
 * 读取文件内容 返回 promise
 */
export function readFileText(e: File, encoding = 'UTF-8'): Promise<string> {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsText(e, encoding);
    reader.onload = res => {
      resolve(res.target?.result as string);
    };
    reader.onerror = err => {
      reject(err);
    };
  });
}

/**
 * 解析路径类型
 */
export interface PathAnalysis {
  name: string;
  path: string;
}

/**
 * 获取解析路径
 */
export function getPathAnalysis(path: string, paths: PathAnalysis[] = []): PathAnalysis[] {
  const lastSeparatorIndex = path.lastIndexOf('/');
  if (lastSeparatorIndex === -1) {
    return paths;
  }
  const name = path.substring(lastSeparatorIndex, path.length);
  if (!isEmptyStr(name) && name !== '/') {
    paths.unshift({
      name: name.substring(1, name.length),
      path: path
    });
  }
  return getPathAnalysis(path.substring(0, lastSeparatorIndex), paths);
}

/**
 * 替换路径
 */
export function getPath(path: string) {
  return path.replace(new RegExp('\\\\+', 'g'), '/')
    .replace(new RegExp('/+', 'g'), '/');
}

/**
 * 获取父级路径
 */
export function getParentPath(path: string) {
  const paths = getPath(path).split('/');
  const len = paths.length;
  if (len <= 2) {
    return '/';
  }
  let parent = '';
  for (let i = 0; i < len - 1; i++) {
    parent += paths[i];
    if (i !== len - 2) {
      parent += '/';
    }
  }
  return parent;
}

/**
 * 下载文件
 */
export function downloadFile(res: any, fileName: string) {
  const blob = new Blob([res.data]);
  const tempLink = document.createElement('a');
  const blobURL = window.URL.createObjectURL(blob);
  tempLink.style.display = 'none';
  tempLink.href = blobURL;
  if (!fileName) {
    fileName = res.headers['content-disposition']
      ? res.headers['content-disposition'].split(';')[1].split('=')[1]
      : new Date().getTime();
  }
  tempLink.download = decodeURIComponent(fileName);
  if (typeof tempLink.download === 'undefined') {
    tempLink.target = '_blank';
  }
  document.body.appendChild(tempLink);
  tempLink.click();
  document.body.removeChild(tempLink);
  window.URL.revokeObjectURL(blobURL);
}

/**
 * 10进制权限 转 字符串权限
 */
export function permission10toString(permission: number) {
  if (permission === undefined) {
    return '---';
  }
  const ps = (permission + '');
  let res = '';
  for (let i = 0; i < ps.length; i++) {
    const per = ps.charAt(i) as unknown as number;
    if ((per & 4) === 0) {
      res += '-';
    } else {
      res += 'r';
    }
    if ((per & 2) === 0) {
      res += '-';
    } else {
      res += 'w';
    }
    if ((per & 1) === 0) {
      res += '-';
    } else {
      res += 'x';
    }
  }
  if (res.length <= 9) {
    res = res.padEnd(9, '-');
  } else {
    res = res.substring(0, 9);
  }
  return res;
}

export default null;
