/**
 * 获取 base64 实际数据
 */
export function getBase64Data(e: string) {
  return e.substring(e.indexOf(',') + 1);
}

/**
 * 读取 blob 内容 返回 promise
 */
export function readBlobText(blob: Blob) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onload = event => {
      resolve(event.target?.result as string);
    };
    reader.onerror = err => {
      reject(err);
    };
    reader.readAsText(blob);
  });
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
  if (name && name !== '/') {
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
 * 获取文件名
 */
export function getFileName(path: string) {
  path = getPath(path);
  return path.substring(path.lastIndexOf('/') + 1);
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
 * 打开下载文件
 */
export function openDownloadFile(url: string) {
  try {
    // 创建隐藏的可下载链接
    let element = document.createElement('a');
    element.setAttribute('href', url);
    element.setAttribute('download', '');
    element.style.display = 'none';
    // 将其附加到文档中
    document.body.appendChild(element);
    // 点击该下载链接
    element.click();
    // 移除已下载的链接
    document.body.removeChild(element);
  } catch (e) {
    window.open(url, 'newWindow');
  }
}

/**
 * 下载文件
 */
export function downloadFile(res: any, fileName: string = '') {
  const blob = new Blob([res.data], { type: 'application/octet-stream' });
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

// 获取文件大小
export function getFileSize(size: number, scale: number = 2) {
  let result;
  let unit;
  if (size >= 1024 * 1024 * 1024) {
    result = (size / (1024 * 1024 * 1024)).toFixed(scale);
    unit = 'GB';
  } else if (size >= 1024 * 1024) {
    result = (size / (1024 * 1024)).toFixed(scale);
    unit = 'MB';
  } else if (size >= 1024) {
    result = (size / 1024).toFixed(scale);
    unit = 'KB';
  } else {
    result = size;
    unit = 'B';
  }
  return `${result} ${unit}`;
}

export default null;
