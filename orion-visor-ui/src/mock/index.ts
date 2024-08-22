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

// @ts-ignore
Mock.XHR.prototype.send = (() => {
  // @ts-ignore
  const _send = Mock.XHR.prototype.send;
  const defaultEvent = () => {
  };
  return function (this: XHRCustom) {
    if (!this.match) {
      this.custom.xhr.responseType = this.responseType || '';
      this.custom.xhr.timeout = this.timeout || 0;
      this.custom.xhr.withCredentials = this.withCredentials || false;
      this.custom.xhr.onabort = this.onabort || defaultEvent;
      this.custom.xhr.onerror = this.onerror || defaultEvent;
      this.custom.xhr.onload = this.onload || defaultEvent;
      this.custom.xhr.onloadend = this.onloadend || defaultEvent;
      this.custom.xhr.onloadstart = this.onloadstart || defaultEvent;
      this.custom.xhr.onprogress = this.onprogress || defaultEvent;
      this.custom.xhr.onreadystatechange = this.onreadystatechange || defaultEvent;
      this.custom.xhr.ontimeout = this.ontimeout || defaultEvent;
    }
    return _send.apply(this, arguments);
  };
})();

Mock.setup({
  timeout: '600-1000',
});
