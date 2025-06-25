<template>
  <div class="container">
    <!-- 顶部 logo -->
    <div class="logo">
      <img class="logo-img"
           alt="logo"
           src="@/assets/logo.svg?url" />
      <div class="logo-text">Orion Visor</div>
    </div>
    <!-- 左侧 banner -->
    <login-banner />
    <!-- 主体部分 -->
    <div class="content">
      <div class="content-inner">
        <!-- 登录表单 -->
        <login-form />
      </div>
      <!-- 页脚 -->
      <div class="footer">
        <app-footer />
      </div>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'login',
  };
</script>

<script lang="ts" setup>
  import { Notification } from '@arco-design/web-vue';
  import { reLoginTipsKey } from '@/types/symbol';
  import LoginBanner from './components/banner.vue';
  import LoginForm from './components/login-form.vue';
  import AppFooter from '@/components/app/app-footer/index.vue';

  // 登录提示
  const tips = window.sessionStorage.getItem(reLoginTipsKey);
  window.sessionStorage.removeItem(reLoginTipsKey);
  if (tips) {
    Notification.error({
      closable: true,
      content: tips,
      duration: 5 * 1000,
    });
  }
</script>

<style lang="less" scoped>
  .container {
    display: flex;
    height: 100vh;

    .banner {
      width: 550px;
      background: linear-gradient(163.85deg, #1d2129 0%, #00308f 100%);
    }

    .content {
      position: relative;
      display: flex;
      flex: 1;
      align-items: center;
      justify-content: center;
      padding-bottom: 40px;
    }

    .footer {
      position: absolute;
      right: 0;
      bottom: 0;
      width: 100%;
    }
  }

  .logo {
    position: fixed;
    top: 24px;
    left: 22px;
    z-index: 1;
    display: inline-flex;
    align-items: center;

    &-img {
      width: 32px;
      height: 32px;
    }

    &-text {
      display: flex;
      margin-right: 4px;
      margin-left: 4px;
      color: var(--color-fill-1);
      font-size: 20px;
    }
  }
</style>

<style lang="less" scoped>
  // responsive
  @media (max-width: @screen-lg) {
    .container {
      .banner {
        width: 25%;
      }
    }
  }
</style>
