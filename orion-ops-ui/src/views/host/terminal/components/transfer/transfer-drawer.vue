<template>
  <a-drawer v-model:visible="visible"
            title="文件传输列表"
            :width="388"
            :mask-closable="false"
            :unmount-on-close="false"
            :footer="false">
    <a-spin class="full" :loading="loading">
      {{ transferManager.transferList }}
    </a-spin>
  </a-drawer>
</template>

<script lang="ts">
  export default {
    name: 'transferDrawer'
  };
</script>

<script lang="ts" setup>
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import { useTerminalStore } from '@/store';

  const { transferManager } = useTerminalStore();
  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  // 打开
  const open = () => {
    setVisible(true);
  };

  defineExpose({ open });

  // 关闭
  const handleClose = () => {
    handlerClear();
  };

  // 清空
  const handlerClear = () => {
    setLoading(false);
  };

</script>

<style lang="less" scoped>
  .form-container {
    padding: 12px;
  }

  .command-editor {
    height: calc(100vh - 330px);
  }

</style>
