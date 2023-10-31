<template>
  <div class="tabs-container">
    <a-tabs type="rounded"
            size="medium"
            position="left"
            :lazy-load="true"
            :destroy-on-hide="true">
      <!-- 个人信息 -->
      <a-tab-pane key="1" title="个人信息">
        <user-info />
      </a-tab-pane>
      <!-- 登录日志 -->
      <a-tab-pane key="2" title="登录日志">
        <login-history />
      </a-tab-pane>
      <!-- 登录设备 -->
      <a-tab-pane key="3" title="登录设备">
        <login-history />
      </a-tab-pane>
      <!-- 操作日志 -->
      <a-tab-pane key="4" title="操作日志">
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
  import UserInfo from './components/user-info.vue';
  import LoginHistory from './components/login-history.vue';
</script>

<style lang="less" scoped>
  .tabs-container {
    background: #FFFFFF;
    margin: 16px 16px 0 16px;
    padding: 16px;
    display: flex;
    flex-direction: column;
    border-radius: 4px;
  }

  :deep(.arco-tabs-pane) {
    border-left: 1px var(--color-neutral-3) solid;
  }

  :deep(.arco-tabs-tab-title) {
    user-select: none;
  }
</style>
