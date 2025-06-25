import type { FavoriteItem } from '@/types/global';
import type { FavoriteType } from '@/api/meta/favorite';
import { addFavorite, cancelFavorite } from '@/api/meta/favorite';
import { ref } from 'vue';

export default function useFavorite(type: FavoriteType) {
  const loading = ref(false);

  const toggle = async <T extends FavoriteItem>(record: T, id: number) => {
    // 防抖
    if (loading.value) {
      return;
    }
    const request = { relId: id, type };
    try {
      loading.value = true;
      if (record.favorite) {
        // 取消收藏
        await cancelFavorite(request);
        record.favorite = false;
      } else {
        // 添加收藏
        await addFavorite(request);
        record.favorite = true;
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

