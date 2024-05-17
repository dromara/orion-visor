import type { FavoriteType } from '@/api/meta/favorite';
import { addFavorite, cancelFavorite } from '@/api/meta/favorite';
import { ref } from 'vue';

export default function useFavorite(type: FavoriteType) {
  const loading = ref(false);
  const toggle = async (record: any, id: number, cancelField = 'favorite') => {
    const request = { relId: id, type };
    try {
      loading.value = true;
      if (record[cancelField]) {
        // 取消收藏
        await cancelFavorite(request);
        record[cancelField] = false;
      } else {
        // 添加收藏
        await addFavorite(request);
        record[cancelField] = true;
      }
    } catch (e) {
    } finally {
      loading.value = false;
    }
  };
  return {
    loading,
    toggle
  };
}


