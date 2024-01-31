<template>
  <div class="host-space-setting-block">
    <!-- 顶部 -->
    <div class="host-space-setting-subtitle-wrapper">
      <h3 class="host-space-setting-subtitle">
        快捷键操作
      </h3>
    </div>
    <!-- 内容区域 -->
    <div class="host-space-setting-body setting-body">
      <!-- 提示 -->
      <a-alert class="mb16">点击保存按钮后需要刷新页面生效 (恢复默认配置后也需要点击保存按钮) (设置时需要避免与浏览器内置快捷键冲突)</a-alert>
      <a-space class="action-container" size="mini">
        <!-- 是否启用 -->
        <a-switch v-model="value"
                  checked-text="启用"
                  unchecked-text="禁用"
                  type="round" />
        <a-button size="small"
                  type="text"
                  @click="emits('save')">
          保存
        </a-button>
        <a-button size="small"
                  type="text"
                  @click="emits('reset')">
          恢复默认配置
        </a-button>
      </a-space>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'terminalShortcutActionBlock'
  };
</script>

<script lang="ts" setup>
  import { computed } from 'vue';

  const props = defineProps<{
    enabled: boolean
  }>();

  const emits = defineEmits(['update:enabled', 'save', 'reset']);

  const value = computed<boolean>({
    get() {
      return props.enabled;
    },
    set(e) {
      emits('update:enabled', e);
    }
  });

</script>

<style lang="less" scoped>
  .setting-body {
    flex-direction: column;
  }

  .action-container {
    display: flex;
    align-items: center;
  }
</style>
