import { ref } from 'vue';
import { Message } from '@arco-design/web-vue';

export default function useLimit(limit = 500) {
  const last = ref(0);

  const checkLimited = (tips: string | boolean = true) => {
    const now = Date.now();
    if (now > last.value + limit) {
      last.value = now;
      return true;
    } else {
      if (tips === true) {
        Message.error('操作频率过快, 请稍后再试');
      } else if (tips) {
        Message.error(tips);
      }
      return false;
    }
  };

  return {
    last,
    checkLimited,
  };
}
