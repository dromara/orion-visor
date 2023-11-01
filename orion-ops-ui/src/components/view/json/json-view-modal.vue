<template>
  <a-modal v-model:visible="visible"
           title-align="start"
           :width="width"
           :body-style="{padding: '16px 8px'}"
           :top="80"
           :title="title"
           :align-center="false"
           :draggable="true"
           :mask-closable="false"
           :unmount-on-close="true"
           :footer="false"
           @close="handleClose">
    <div :style="{width: '100%', 'height': height}">
      <editor v-model="value" readonly />
    </div>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'json-view-modal'
  };
</script>

<script lang="ts" setup>
  import { ref } from 'vue';
  import useVisible from '@/hooks/visible';
  import { isString } from '@/utils/is';

  const props = defineProps({
    width: {
      type: [String, Number],
      default: '60%'
    },
    height: {
      type: String,
      default: 'calc(100vh - 240px)'
    }
  });

  const { visible, setVisible } = useVisible();

  const title = ref<string>();
  const value = ref<string | any>();


  // 打开
  const open = (editorValue: string | any, editorTitle = 'json') => {
    title.value = editorTitle;
    if (isString(editorValue)) {
      value.value = editorValue;
    } else {
      value.value = JSON.stringify(editorValue, undefined, 4);
    }
    setVisible(true);
  };

  defineExpose({ open });

  // 关闭
  const handleClose = () => {
    setVisible(false);
  };
</script>

<style lang="less" scoped>
  :deep(.arco-modal-title) {
    font-size: 14px;
  }
</style>
