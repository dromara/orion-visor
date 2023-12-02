<template>
  <a-scrollbar>
    <div class="user-container">
      <!-- 用户列表 -->
      <tab-router v-if="usersRouter.length"
                  class="user-router"
                  v-model="value"
                  :items="usersRouter"
                  @change="(key, item) => emits('change', key, item)" />
      <!-- 加载中 -->
      <a-skeleton v-else-if="loading" class="skeleton-wrapper">
        <a-skeleton-line :rows="4" />
      </a-skeleton>
      <!-- 暂无数据 -->
      <a-empty v-else class="user-empty">
        <div slot="description">
          暂无用户数据
        </div>
      </a-empty>
    </div>
  </a-scrollbar>
</template>

<script lang="ts">
  export default {
    name: 'router-users'
  };
</script>

<script lang="ts" setup>
  import type { TabRouterItem } from '@/components/view/tab-router/types';
  import { computed, onMounted, ref } from 'vue';
  import { useCacheStore } from '@/store';
  import { Message } from '@arco-design/web-vue';
  import useLoading from '@/hooks/loading';

  const props = defineProps({
    modelValue: Number
  });

  const emits = defineEmits(['update:modelValue', 'change']);

  const { loading, setLoading } = useLoading();
  const cacheStore = useCacheStore();

  const usersRouter = ref<Array<TabRouterItem>>([]);

  const value = computed({
    get() {
      return props.modelValue;
    },
    set(e) {
      emits('update:modelValue', e);
    }
  });

  // 加载用户列表
  const loadUserList = async () => {
    setLoading(true);
    try {
      const users = await cacheStore.loadUsers();
      usersRouter.value = users.map(s => {
        return {
          key: s.id,
          text: `${s.nickname} (${s.username})`
        };
      });
    } catch (e) {
      Message.error('用户列表加载失败');
    } finally {
      setLoading(false);
    }
  };

  // 加载用户列表
  onMounted(() => {
    loadUserList();
  });

</script>

<style lang="less" scoped>
  @width: 198px;
  @height: 198px;

  :deep(.arco-scrollbar-container) {
    height: 100%;
    overflow-y: auto;
  }

  .user-container {
    overflow: hidden;

    .user-router {
      height: 100%;
      min-width: @width;
      width: max-content;
    }

    .skeleton-wrapper, .user-empty {
      width: @width;
      height: @height;
      padding: 8px;
    }
  }
</style>
