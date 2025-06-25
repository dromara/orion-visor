<template>
  <div class="terminal-setting-container">
    <div class="terminal-setting-wrapper">
      <!-- 主标题 -->
      <h2 class="terminal-setting-title">显示设置</h2>
      <!-- SSH 显示偏好 -->
      <terminal-ssh-display-block />
      <!-- SSH 操作栏 -->
      <terminal-ssh-action-bar-block />
      <!-- RDP 工具栏 -->
      <terminal-rdp-action-bar-block />
      <!-- 右键菜单 -->
      <terminal-right-menu-block />
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'terminalDisplaySetting'
  };
</script>

<script lang="ts" setup>
  import TerminalSshDisplayBlock from './terminal-ssh-display-block.vue';
  import TerminalSshActionBarBlock from './terminal-ssh-action-bar-block.vue';
  import TerminalRdpActionBarBlock from './terminal-rdp-action-bar-block.vue';
  import TerminalRightMenuBlock from './terminal-right-menu-block.vue';

</script>

<style lang="less" scoped>

</style>
