<template>
  <div class="tabs-container">
    <a-tabs v-model:active-key="activeKey"
            type="rounded"
            size="medium"
            position="left"
            :lazy-load="true">
      <!-- SFTP -->
      <a-tab-pane v-permission="['infra:system-setting:update']"
                  key="sftp"
                  title="SFTP">
        <sftp-setting />
      </a-tab-pane>
      <!-- 登录设置 -->
      <a-tab-pane v-permission="['infra:system-setting:update']"
                  key="login"
                  title="登录设置">
        <login-setting />
      </a-tab-pane>
      <!-- 加密设置 -->
      <a-tab-pane v-permission="['infra:system-setting:update']"
                  key="encrypt"
                  title="加密设置">
        <encrypt-setting />
      </a-tab-pane>
      <!-- 日志设置 -->
      <a-tab-pane v-permission="['infra:system-setting:update']"
                  key="log"
                  title="日志设置">
        <log-setting />
      </a-tab-pane>
      <!-- 自动清理 -->
      <a-tab-pane v-permission="['infra:system-setting:update']"
                  key="auto-clear"
                  title="自动清理">
        <auto-clear-setting />
      </a-tab-pane>
      <!-- 关于 -->
      <a-tab-pane key="about" title="关于">
        <about-setting />
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'systemSetting',
  };
</script>

<script lang="ts" setup>
  import { onBeforeMount, ref } from 'vue';
  import { useRoute } from 'vue-router';
  import usePermission from '@/hooks/permission';
  import SftpSetting from './components/sftp-setting.vue';
  import LoginSetting from './components/login-setting.vue';
  import EncryptSetting from './components/encrypt-setting.vue';
  import LogSetting from './components/log-setting.vue';
  import AutoClearSetting from './components/auto-clear-setting.vue';
  import AboutSetting from './components/about-setting.vue';

  const route = useRoute();
  const { hasPermission } = usePermission();

  const activeKey = ref('sftp');

  // 跳转到指定页
  onBeforeMount(() => {
    const key = route.query.key as string;
    if (key) {
      activeKey.value = key;
    } else {
      // 检查权限
      const has = hasPermission('infra:system-setting:update');
      if (has) {
        activeKey.value = 'sftp';
      } else {
        activeKey.value = 'about';
      }
    }
  });

</script>

<style lang="less" scoped>
  .tabs-container {
    background: var(--color-bg-2);
    margin: 16px;
    padding: 16px;
    display: flex;
    flex-direction: column;
    border-radius: 8px;
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
    justify-content: center;
  }

  :deep(.main-container) {
    padding: 16px 0 0 24px;
    width: 100%;

    .setting-header {
      color: var(--color-text-1);
      margin-top: 0;
      margin-bottom: 20px;
      font-weight: 600;
      user-select: none;
    }

    .setting-form {
      padding-left: 24px;
    }

    .arco-descriptions-title {
      font-weight: 600;
      user-select: none;
    }

    .alert-href {
      text-decoration: none;
    }

    .input-wrapper {
      width: 368px;
    }
  }

</style>
