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
      <!-- VNC 工具栏 -->
      <terminal-vnc-action-bar-block />
      <!-- SSH 右键菜单 -->
      <terminal-ssh-right-menu-block />
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
  import TerminalSshRightMenuBlock from './terminal-ssh-right-menu-block.vue';
  import TerminalRdpActionBarBlock from './terminal-rdp-action-bar-block.vue';
  import TerminalVncActionBarBlock from './terminal-vnc-action-bar-block.vue';

</script>

<style lang="less" scoped>

</style>
