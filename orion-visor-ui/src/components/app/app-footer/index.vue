<template>
  <a-layout-footer class="footer">
    <a-space direction="vertical" size="small">
      <a-space size="large">
        <!-- <a-link target="_blank" href="https://github.com/lijiahangmax/orion-visor">官网</a-link> -->
        <!-- <a-link target="_blank" href="https://github.com/lijiahangmax/orion-visor">教程</a-link> -->
        <a-link target="_blank" href="https://github.com/lijiahangmax/orion-visor">github</a-link>
        <a-link target="_blank" href="https://gitee.com/lijiahangmax/orion-visor">gitee</a-link>
        <a-link target="_blank" href="https://lijiahangmax.github.io/orion-visor">文档</a-link>
        <a-link target="_blank" href="https://github.com/lijiahangmax/orion-visor/blob/main/LICENSE">License</a-link>
        <a-link target="_blank" :href="`https://github.com/lijiahangmax/orion-visor/releases/tag/v${version}`">V{{ version }} 社区版</a-link>
      </a-space>
      <span class="copyright">
        Copyright<icon-copyright /> 2023 - {{ new Date().getFullYear() }} Li Jiahang, All rights reserved.
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
