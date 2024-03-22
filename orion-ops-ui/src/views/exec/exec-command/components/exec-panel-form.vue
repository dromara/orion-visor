<template>
  <div class="container">
    <!-- 表头 -->
    <div class="panel-header">
      <h3>执行参数</h3>
      <!-- 操作 -->
      <a-button-group size="small">
        <a-button @click="emits('reset')">重置</a-button>
        <a-button type="primary" @click="emits('exec')">执行</a-button>
      </a-button-group>
    </div>
    <!-- 命令表单 -->
    <slot name="form" />
    <a-divider v-if="schemaCount"
               orientation="center"
               style="margin: 12px 0 26px 0;">
      命令参数
    </a-divider>
    <!-- 参数表单 -->
    <slot v-if="schemaCount" name="params" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'execPanelForm'
  };
</script>

<script lang="ts" setup>
  const emits = defineEmits(['reset', 'exec']);

  defineProps<{
    schemaCount: number
  }>();

</script>

<style lang="less" scoped>

</style>
