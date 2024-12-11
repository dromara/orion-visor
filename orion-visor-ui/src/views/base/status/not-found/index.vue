<template>
  <div class="content">
    <a-result class="result" status="404" subtitle="糟糕! 页面不见了!" />
    <a-space>
      <a-button type="primary" @click="() => logout()">重新登录</a-button>
      <a-button type="primary" @click="() => $router.push(DEFAULT_ROUTE_NAME)">返回工作台</a-button>
    </a-space>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'notFound',
  };
</script>

<script lang="ts" setup>
  import useUser from '@/hooks/user';
  import { DEFAULT_ROUTE_NAME } from '@/router/constants';

  const { logout } = useUser();

</script>

<style lang="less" scoped>
  .content {
    position: absolute;
    top: 50%;
    left: 50%;
    margin-left: -96px;
    margin-top: -124px;
    text-align: center;
  }
</style>
