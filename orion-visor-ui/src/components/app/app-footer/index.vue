<template>
  <a-layout-footer class="footer">
    <a-space direction="vertical" size="small">
      <a-space size="large">
        <a-link target="_blank" href="https://github.com/dromara/orion-visor">github</a-link>
        <a-link target="_blank" href="https://gitee.com/dromara/orion-visor">gitee</a-link>
        <a-link target="_blank" href="https://visor.orionsec.cn">文档</a-link>
        <a-link target="_blank" href="https://github.com/dromara/orion-visor/blob/main/LICENSE">License</a-link>
        <a-link target="_blank" :href="`https://github.com/dromara/orion-visor/releases/tag/v${version}`">v{{ version }}</a-link>
      </a-space>
      <span class="copyright">
        Copyright<icon-copyright /> 2023 - {{ new Date().getFullYear() }} <a href="https://dromara.org">Dromara</a>, All rights reserved. Designed by
        <a href="https://orionsec.cn" target="_blank">Jiahang Li.</a>
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
