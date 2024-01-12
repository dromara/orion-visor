<template>
  <!-- 终端右键菜单 -->
  <a-dropdown trigger="contextMenu"
              position="bl"
              alignPoint>
    <!-- 终端插槽 -->
    <slot />
    <!-- 右键菜单 -->
    <template v-if="preference.interactSetting.enableRightClickMenu" #content>
      <a-doption>Option 1</a-doption>
      <a-doption>Option 2</a-doption>
      <a-doption>Option 3</a-doption>
    </template>
  </a-dropdown>
</template>

<script lang="ts">
  export default {
    name: 'terminalContextMenu'
  };
</script>

<script lang="ts" setup>
  import { useTerminalStore } from '@/store';

  const { preference } = useTerminalStore();

  // TODO 颜色 配置 触发事件

</script>

<style lang="less" scoped>

</style>
