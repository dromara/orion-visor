<template>
  <a-layout-footer class="footer">
    <div class="footer-text">
      项目地址 <a target="_blank" href="https://github.com/lijiahangmax/orion-ops-pro">github</a> - <a
      target="_blank" href="https://gitee.com/lijiahangmax/orion-ops-pro">gitee</a>
      <a class="license" target="_blank" href="https://github.com/lijiahangmax/orion-ops-pro/blob/main/LICENSE">License - Apache 2.0</a>
    </div>
  </a-layout-footer>
</template>

<script lang="ts" setup>
</script>

<style lang="less" scoped>
  .footer {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 40px;
    color: var(--color-text-2);
    text-align: center;

    &-text {
      text-wrap: none;

      a {
        text-decoration: none;
      }

      .license {
        display: inline-block;
        margin-left: 16px;
      }
    }
  }
</style>
