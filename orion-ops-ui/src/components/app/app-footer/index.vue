<template>
  <a-layout-footer class="footer">
    <a-space direction="vertical" size="small">
      <a-space size="large">
        <!-- <a-link target="_blank" href="https://github.com/lijiahangmax/orion-ops-pro">官网</a-link> -->
        <!-- <a-link target="_blank" href="https://github.com/lijiahangmax/orion-ops-pro">教程</a-link> -->
        <a-link target="_blank" href="https://github.com/lijiahangmax/orion-ops-pro">github</a-link>
        <a-link target="_blank" href="https://gitee.com/lijiahangmax/orion-ops-pro">gitee</a-link>
        <a-link target="_blank" href="https://lijiahangmax.github.io/orion-ops-pro">文档</a-link>
        <a-link target="_blank" href="https://github.com/lijiahangmax/orion-ops-pro/blob/main/LICENSE">License</a-link>
        <a-link target="_blank" :href="`https://github.com/lijiahangmax/orion-ops-pro/releases/tag/v${version}`">v{{ version }} 开源版</a-link>
      </a-space>
      <span class="copyright">
        Copyright<icon-copyright /> {{ new Date().getFullYear() }} Li Jiahang All rights reserved.
      </span>
    </a-space>
  </a-layout-footer>
</template>

<script lang="ts" setup>
  const version = import.meta.env.VITE_APP_VERSION;
</script>

<style lang="less" scoped>
  .footer {
    display: flex;
    align-items: flex-start;
    justify-content: center;
    text-align: center;
    height: 64px;

    a, span {
      text-decoration: none;
      color: rgb(var(--primary-6));
    }

    .copyright {
      color: var(--color-text-3);
    }

  }
</style>
