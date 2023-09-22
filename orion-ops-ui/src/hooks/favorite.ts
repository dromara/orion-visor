import { Message } from '@arco-design/web-vue';
import { FavoriteType, addFavorite, cancelFavorite } from '@/api/meta/favorite';

export default function useFavorite(type: FavoriteType) {
  const toggle = async (record: any, id: number, cancelField = 'favorite') => {
    const request = { relId: id, type };
    const loading = Message.loading(record[cancelField] ? '取消中' : '收藏中');
    try {
      if (record[cancelField]) {
        // 取消收藏
        await cancelFavorite(request);
        record[cancelField] = false;
        Message.success('已取消');
      } else {
        // 添加收藏
        await addFavorite(request);
        record[cancelField] = true;
        Message.success('已收藏');
      }
    } catch (e) {
    } finally {
      loading.close();
    }
  };
  return {
    toggle
  };
}


