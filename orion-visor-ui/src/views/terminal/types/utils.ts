import { useTerminalStore, useUserStore } from '@/store';
import { dateFormat } from '@/utils';
import html2canvas from 'html2canvas';
import { saveAs } from 'file-saver';
import { Message } from '@arco-design/web-vue';

// 获取显示大小
export const getDisplaySize = (size: string, tips: boolean = false): [number, number] => {
  if (size?.includes('x')) {
    const [w, h] = size.split('x');
    return [Number.parseInt(w), Number.parseInt(h)];
  }
  if (tips) {
    Message.error('分辨率格式不正确, 请重新选择或输入 (如: 800x600)');
  }
  throw new Error('Invalid size');
};

// 截屏
export const screenshot = async (el: HTMLElement) => {
  if (!el) {
    return;
  }
  try {
    // 获取截屏
    const canvas = await html2canvas(el, {
      useCORS: true,
      backgroundColor: 'transparent',
    });
    // 绘制水印
    const ctx = canvas.getContext('2d') as CanvasRenderingContext2D;
    const wPx = canvas.style.width;
    const hPx = canvas.style.height;
    const w = Number.parseInt(wPx.substring(0, wPx.length - 2));
    const h = Number.parseInt(hPx.substring(0, hPx.length - 2));
    const fontSize = 14;
    ctx.fillStyle = useTerminalStore().preference.theme.dark ? 'rgba(255, 255, 255, 0.1)' : 'rgba(0, 0, 0, 0.1)';
    ctx.font = `${fontSize}px Arial`;
    ctx.rotate(-24 * Math.PI / 180);
    // 水印内容
    const watermark = useUserStore().username || '';
    const time = '(' + dateFormat() + ')';
    const textWidth = ctx.measureText(time.length > watermark.length ? time : watermark).width * 1.5;
    const textHeight = (textWidth / 4 * 3) * 1.5;
    // 绘制文本
    for (let yi = -1; yi < h / textHeight + 2; yi++) {
      for (let xi = -1; xi < w / textWidth + 2; xi++) {
        if ((xi % 2 === 0 && yi % 2 === 0) || (xi % 2 !== 0 && yi % 2 !== 0)) {
          continue;
        }
        ctx.fillText(watermark, textWidth * (xi - 1), textHeight * (yi + 1));
        ctx.fillText(time, textWidth * (xi - 1), textHeight * (yi + 1) + fontSize * 1.12);
      }
    }
    // 保存图片
    const blob = await new Promise((resolve, reject) => {
      canvas.toBlob((blob) => {
        if (!blob) {
          reject();
        }
        resolve(blob);
      }, 'image/png');
    });
    saveAs(blob as Blob, `screenshot-${Date.now()}.png`);
  } catch (e) {
    Message.error('保存失败');
  }
};
