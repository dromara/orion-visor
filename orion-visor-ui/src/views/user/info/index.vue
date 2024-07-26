<template>
  <div class="tabs-container" v-if="render">
    <a-tabs type="rounded"
            size="medium"
            position="left"
            :lazy-load="true"
            :destroy-on-hide="true"
            @tab-click="(k: string) => clickTab(k)">
      <!-- 个人信息 -->
      <a-tab-pane key="mineInfo"
                  v-if="!user || hasPermission('infra:system-user:update')"
                  :title="user ? '用户信息' : '个人信息'">
        <user-base-info :user="user" />
      </a-tab-pane>
      <!-- 登录日志 -->
      <a-tab-pane key="loginHistory"
                  v-if="!user || hasPermission('infra:system-user:login-history')"
                  title="登录日志">
        <login-history :user="user" />
      </a-tab-pane>
      <!-- 登录设备 -->
      <a-tab-pane key="userSession"
                  v-if="!user || hasPermission('infra:system-user:query-session')"
                  title="登录设备">
        <user-session :user="user" />
      </a-tab-pane>
      <!-- 操作日志 -->
      <a-tab-pane key="operatorLog"
                  v-if="!user || hasPermission('infra:operator-log:query')"
                  title="操作日志">
        <user-operator-log :user="user" />
      </a-tab-pane>
      <!-- 返回 -->
      <a-tab-pane key="back" v-if="userId">
        <template #title>
          <icon-left style="font-size: 16px; padding-top: 2px;" />
          返回
        </template>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'userInfo'
  };
</script>

<script lang="ts" setup>
  import type { UserQueryResponse } from '@/api/user/user';
  import { useRoute, useRouter } from 'vue-router';
  import usePermission from '@/hooks/permission';
  import { onBeforeMount, ref } from 'vue';
  import { useUserStore } from '@/store';
  import { getUser } from '@/api/user/user';
  import UserBaseInfo from './components/user-base-info.vue';
  import UserSession from './components/user-session.vue';
  import UserOperatorLog from './components/user-operator-log.vue';
  import LoginHistory from './components/login-history.vue';

  const route = useRoute();
  const router = useRouter();
  const userStore = useUserStore();
  const { hasPermission } = usePermission();

  const render = ref<boolean>(false);
  const userId = ref<number>();
  const user = ref<UserQueryResponse>();

  // 点击 tab
  const clickTab = (key: string) => {
    if (key === 'back') {
      router.back();
    }
  };

  // 初始化
  const init = async () => {
    // 获取 userId
    const queryUserId = route.query.id as string;
    if (!queryUserId) {
      return;
    }
    const id = parseInt(queryUserId);
    userId.value = id;
    // 当前用户 直接返回
    if (userStore.id === id) {
      return;
    }
    // 查询用户信息
    const { data } = await getUser(id);
    user.value = data;
  };

  onBeforeMount(async () => {
    // 初始化
    await init();
    render.value = true;
  });

</script>

<style lang="less" scoped>
  .tabs-container {
    background: var(--color-bg-2);
    margin: 16px;
    padding: 16px;
    display: flex;
    flex-direction: column;
    border-radius: 4px;
  }

  :deep(.arco-tabs-nav-tab-list) {
    width: 88px;
  }

  :deep(.arco-tabs-pane) {
    border-left: 1px var(--color-neutral-3) solid;
  }

  :deep(.arco-tabs-tab) {
    user-select: none;
    white-space: nowrap;
    display: flex;
    justify-content: flex-end;
  }

</style>
