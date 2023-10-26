<template>
  <a-modal v-model:visible="visible"
           title-align="start"
           width="60%"
           :body-style="{padding: '16px 8px'}"
           :top="80"
           :title="title"
           :align-center="false"
           :draggable="true"
           :mask-closable="false"
           :unmount-on-close="true"
           :footer="false"
           @close="handleClose">
    <a-spin :loading="loading" style="width: 100%; height: calc(100vh - 240px)">
      <editor v-model="value" readonly />
    </a-spin>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'dict-key-view-modal'
  };
</script>

<script lang="ts" setup>
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import { getDictValueList } from '@/api/system/dict-value';

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const title = ref<string>();
  const value = ref<string>();

  // 打开新增
  const open = (e: any) => {
    title.value = e.keyName;
    value.value = undefined;
    render(e.keyName);
    setVisible(true);
  };

  // 渲染
  const render = async (keyName: string) => {
    try {
      setLoading(true);
      // 查看
      const { data } = await getDictValueList([keyName]);
      value.value = JSON.stringify(data[keyName], undefined, 4);
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  defineExpose({ open });

  // 关闭
  const handleClose = () => {
    setLoading(false);
    setVisible(false);
  };

</script>

<style lang="less" scoped>
  :deep(.arco-modal-title) {
    font-size: 14px;
  }
</style>
