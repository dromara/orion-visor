<template>
  <div>
    {{ tab }}
    <div>header</div>
    <div>table</div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'sftpView'
  };
</script>

<script lang="ts" setup>
  import { TerminalTabItem } from '../../types/terminal.type';

  const props = defineProps<{
    tab: TerminalTabItem
  }>();

</script>

<style lang="less" scoped>

</style>
