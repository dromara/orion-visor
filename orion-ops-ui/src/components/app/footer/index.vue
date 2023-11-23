<template>
  <a-layout-footer class="footer">
    <a-space direction="vertical" size="medium">
      <a-space size="large">
        <a target="_blank" href="https://github.com/lijiahangmax/orion-ops-pro">官网</a>
        <a target="_blank" href="https://github.com/lijiahangmax/orion-ops-pro">文档</a>
        <a target="_blank" href="https://github.com/lijiahangmax/orion-ops-pro">教程</a>
        <a target="_blank" href="https://github.com/lijiahangmax/orion-ops-pro">github</a>
        <a target="_blank" href="https://gitee.com/lijiahangmax/orion-ops-pro">gitee</a>
      </a-space>
      <span class="copyright">
          Copyright<icon-copyright /> 2023 By OrionOpsPro
        </span>
    </a-space>
  </a-layout-footer>
</template>

<script lang="ts" setup>
</script>

<style lang="less" scoped>
  .footer {
    display: flex;
    align-items: flex-start;
    justify-content: center;
    text-align: center;
    height: 64px;

    a {
      text-decoration: none;
      color: rgb(var(--primary-6));
    }

    .copyright {
      color: var(--color-text-3)
    }

  }
</style>
