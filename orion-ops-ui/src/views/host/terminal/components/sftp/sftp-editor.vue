<template>
  <editor ref="editorRef"
          language="txt"
          :auto-focus="true"
          :theme="preference.theme.dark ? 'vs-dark' : 'vs'" />
</template>

<script lang="ts">
  export default {
    name: 'sftpFileEditor'
  };
</script>

<script lang="ts" setup>
  import { ref } from 'vue';
  import { useTerminalStore } from '@/store';

  const { preference } = useTerminalStore();

  const editorRef = ref();

  // 获取值
  const getValue = () => {
    return editorRef.value?.getValue();
  };

  // 设置值
  const setValue = (value: string) => {
    editorRef.value?.setValue(value);
  };

  defineExpose({ getValue, setValue });

</script>

<style lang="less" scoped>

</style>
