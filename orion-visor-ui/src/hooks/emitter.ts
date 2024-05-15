const emitter = 'emitter';

export default function useEmitter(emits: any) {
  // 事件向上冒泡
  const bubblesEmitter = (event: string, ...args: any[]) => {
    if (args) {
      emits(emitter, event, ...args);
    } else {
      emits(emitter, event);
    }
  };

  // 处理冒泡事件
  const dispatchEmitter = (event: string, ...args: any[]) => {
    if (args) {
      emits(event, ...args);
    } else {
      emits(event);
    }
  };

  return {
    bubblesEmitter,
    dispatchEmitter
  };
}
