<template>
  <a-scrollbar>
    <div class="role-container">
      <!-- 角色列表 -->
      <tab-router v-if="rolesRouter.length"
                  class="role-router"
                  v-model="value"
                  :items="rolesRouter"
                  @change="(key, item) => emits('change', key, item)" />
      <!-- 加载中 -->
      <a-skeleton v-else-if="loading" class="skeleton-wrapper">
        <a-skeleton-line :rows="4" />
      </a-skeleton>
      <!-- 暂无数据 -->
      <a-empty v-else class="role-empty">
        <div slot="description">
          暂无角色数据
        </div>
      </a-empty>
    </div>
  </a-scrollbar>
</template>

<script lang="ts">
  export default {
    name: 'router-roles'
  };
</script>

<script lang="ts" setup>
  import type { TabRouterItem } from '@/components/view/tab-router/types';
  import { computed, onMounted, ref } from 'vue';
  import { useCacheStore } from '@/store';
  import { getRoleList } from '@/api/user/role';
  import { Message } from '@arco-design/web-vue';
  import useLoading from '@/hooks/loading';

  const props = defineProps({
    modelValue: Number
  });

  const emits = defineEmits(['update:modelValue', 'change']);

  const { loading, setLoading } = useLoading();
  const cacheStore = useCacheStore();

  const rolesRouter = ref<Array<TabRouterItem>>([]);

  const value = computed({
    get() {
      return props.modelValue;
    },
    set(e) {
      emits('update:modelValue', e);
    }
  });

  // 加载角色列表
  const loadRoleList = async () => {
    setLoading(true);
    try {
      const { data } = await getRoleList();
      // 设置到缓存
      cacheStore.set('roles', data);
    } catch (e) {
      Message.error('角色列表加载失败');
    } finally {
      setLoading(false);
    }
  };

  // 加载主机
  onMounted(async () => {
    if (!cacheStore.roles.length) {
      await loadRoleList();
    }
    rolesRouter.value = cacheStore.roles.map(s => {
      return {
        key: s.id,
        text: `${s.name} (${s.code})`,
        code: s.code
      };
    });
  });

</script>

<style lang="less" scoped>
  @width: 198px;
  @height: 198px;

  :deep(.arco-scrollbar-container) {
    height: 100%;
    overflow-y: auto;
  }

  .role-container {
    overflow: hidden;

    .role-router {
      height: 100%;
      min-width: @width;
      width: max-content;
    }

    .skeleton-wrapper, .role-empty {
      width: @width;
      height: 200px;
      padding: 8px;
    }
  }
</style>
