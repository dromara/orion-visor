<template>
  <div class="terminal-setting-container">
    <div class="terminal-setting-wrapper">
      <!-- 主标题 -->
      <h2 class="terminal-setting-title">终端设置</h2>
      <!-- SSH 交互设置 -->
      <terminal-ssh-interact-block />
      <!-- SSH 插件设置 -->
      <terminal-ssh-plugins-block />
      <!-- RDP 图形化设置 -->
      <terminal-rdp-graph-block />
      <!-- RDP 会话设置 -->
      <terminal-rdp-session-block />
      <!-- VNC 图形化设置 -->
      <terminal-vnc-graph-block />
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'terminalGeneralSetting'
  };
</script>

<script lang="ts" setup>
  import TerminalSshInteractBlock from './terminal-ssh-interact-block.vue';
  import TerminalSshPluginsBlock from './terminal-ssh-plugins-block.vue';
  import TerminalRdpGraphBlock from './terminal-rdp-graph-block.vue';
  import TerminalRdpSessionBlock from './terminal-rdp-session-block.vue';
  import TerminalVncGraphBlock from './terminal-vnc-graph-block.vue';

</script>

<style lang="less" scoped>

</style>
