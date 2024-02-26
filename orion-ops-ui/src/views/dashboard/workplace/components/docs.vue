<template>
  <a-card class="general-card"
          title="帮助文档"
          :header-style="{ paddingBottom: '0' }"
          :body-style="{ padding: '8px 20px 8px 20px' }">
    <a-row>
      <a-col :span="12">
        <a-link target="_blank" href="https://github.com/lijiahangmax/orion-ops-pro">github</a-link>
      </a-col>
      <a-col :span="12">
        <a-link target="_blank" href="https://gitee.com/lijiahangmax/orion-ops-pro">gitee</a-link>
      </a-col>
      <a-col :span="12">
        <a-link target="_blank" href="https://github.com/lijiahangmax/orion-ops-pro/blob/main/LICENSE">License</a-link>
      </a-col>
      <a-col :span="12">
        <a-link target="_blank" href="https://lijiahangmax.gitee.io/orion-ops-pro">操作文档</a-link>
      </a-col>
    </a-row>
  </a-card>
</template>

<script setup lang="ts">
</script>

<style lang="less" scoped>
  .arco-card-body .arco-link {
    margin: 10px 0;
    color: rgb(var(--gray-8));
  }
</style>
