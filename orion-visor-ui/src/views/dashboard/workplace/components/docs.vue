<template>
  <a-card class="general-card"
          title="帮助文档"
          :header-style="{ paddingBottom: '4px' }"
          :body-style="{ padding: '0px 20px 8px 20px' }">
    <a-row>
      <a-col :span="12">
        <a-link target="_blank" href="https://github.com/dromara/orion-visor">github</a-link>
      </a-col>
      <a-col :span="12">
        <a-link target="_blank" href="https://gitee.com/dromara/orion-visor">gitee</a-link>
      </a-col>
      <a-col :span="12">
        <a-link target="_blank" href="https://github.com/dromara/orion-visor/blob/main/LICENSE">License</a-link>
      </a-col>
      <a-col :span="12">
        <a-link target="_blank" href="https://github.com/dromara/orion-visor/issues">上报 bug</a-link>
      </a-col>
      <a-col :span="12">
        <a-link target="_blank" href="https://visor.orionsec.cn/operator/asset.html">操作手册</a-link>
      </a-col>
      <a-col :span="12">
        <a-link target="_blank" href="https://visor.orionsec.cn/update/change-log.html">更新日志</a-link>
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
