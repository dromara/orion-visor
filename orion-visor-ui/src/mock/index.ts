import Mock from 'mockjs';
// import './user';
import '@/views/dashboard/workplace/mock';

// Mock XHR
interface XHR {
  responseType: string;
  timeout: number;
  withCredentials: boolean;
  onabort: () => void;
  onerror: () => void;
  onload: () => void;
  onloadend: () => void;
  onloadstart: () => void;
  onprogress: () => void;
  onreadystatechange: () => void;
  ontimeout: () => void;
}

// Mock XHR Custom
interface XHRCustom extends XHR {
  custom: XHRCustom;
  xhr: XHR;
  match: boolean;
}

// 防止 blob 格式的文件流被污染
// @ts-ignore
Mock.XHR.prototype.send = (() => {
  // @ts-ignore
  const _send = Mock.XHR.prototype.send;
  return function (this: XHRCustom) {
    if (!this.match) {
      this.custom.xhr.responseType = this.responseType || '';
      this.custom.xhr.timeout = this.timeout || 0;
      this.custom.xhr.withCredentials = this.withCredentials || false;
    }
    return _send.apply(this, arguments);
  };
})();

Mock.setup({
  timeout: '600-1000',
});
