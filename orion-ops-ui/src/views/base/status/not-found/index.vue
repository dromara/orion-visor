<template>
  <div class="content">
    <a-result class="result" status="404" subtitle="糟糕! 页面不见了!" />
    <div class="operation-row">
      <a-button class="mr8" key="back" type="primary" @click="logout">重新登录</a-button>
      <a-button key="back" type="primary" @click="to('workplace')">返回工作台</a-button>
    </div>
  </div>
</template>

<script lang="ts" setup>
  import { useRouter } from 'vue-router';
  import { useUserStore } from '@/store';

  const router = useRouter();
  const to = (name: string) => {
    router.push({ name: name });
  };

  // 重新登录
  const logout = () => {
    const userStore = useUserStore();
    userStore.logout();
    to('login');
  };
</script>

<script lang="ts">
  export default {
    name: 'notFound',
  };
</script>

<style lang="less" scoped>
  .content {
    // padding-top: 100px;
    position: absolute;
    top: 50%;
    left: 50%;
    margin-left: -95px;
    margin-top: -121px;
    text-align: center;
  }
</style>
