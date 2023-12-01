<template>
  <div class="user-container">
    <!-- 用户列表 -->
    <tab-router v-if="usersRouter.length"
                class="user-router"
                v-model="value"
                :items="usersRouter"
                @change="(key, item) => emits('change', key, item)" />
    <!-- 暂无数据 -->
    <a-empty v-else class="user-empty">
      <div slot="description">
        暂无用户数据
      </div>
    </a-empty>
  </div>
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
  import { getUserList } from '@/api/user/user';
  import { Message } from '@arco-design/web-vue';

  const props = defineProps({
    modelValue: Number
  });

  const emits = defineEmits(['update:modelValue', 'change']);

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
    try {
      const { data } = await getUserList();
      // 设置到缓存
      cacheStore.set('users', data);
    } catch (e) {
      Message.error('用户列表加载失败');
    }
  };

  // 加载主机
  onMounted(async () => {
    if (!cacheStore.users.length) {
      await loadUserList();
    }
    usersRouter.value = cacheStore.users.map(s => {
      return {
        key: s.id,
        text: `${s.nickname} (${s.username})`
      };
    });
  });

</script>

<style lang="less" scoped>
  .user-container {
    margin-right: 16px;

    .user-router {
      height: 100%;
      min-width: max-content;
      border-right: 1px var(--color-neutral-3) solid;
    }

    .user-empty {
      width: 198px;
    }
  }
</style>
