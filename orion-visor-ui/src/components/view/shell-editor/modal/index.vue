<template>
  <a-modal v-model:visible="visible"
           title-align="start"
           :width="width"
           :body-style="{ padding: '8px' }"
           :top="80"
           :title="title"
           :align-center="false"
           :draggable="true"
           :esc-to-close="false"
           :mask-closable="false"
           :unmount-on-close="true"
           @close="handleClose">
    <div :style="{ width: '100%', 'height': height }">
      <editor v-model="value"
              language="shell"
              :suggestions="true"
              :auto-focus="true"
              :theme="dark ? 'vs-dark' : 'vs'"
              :readonly="readonly" />
    </div>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'shellEditorModal'
  };
</script>

<script lang="ts" setup>
  import { ref } from 'vue';
  import useVisible from '@/hooks/visible';

  const emits = defineEmits(['close']);

  const props = withDefaults(defineProps<Partial<{
    width: string | number;
    height: string;
    dark: boolean;
    readonly: boolean;
  }>>(), {
    width: '60%',
    height: 'calc(100vh - 280px)',
    dark: true,
    readonly: false,
  });

  const { visible, setVisible } = useVisible();

  const title = ref<string>();
  const value = ref<string | any>();

  // 打开
  const open = (editorValue: string | any, editorTitle: string) => {
    title.value = editorTitle;
    value.value = editorValue;
    setVisible(true);
  };

  // 获取值
  const getValue = () => {
    return value.value;
  };

  defineExpose({ open, getValue });

  // 关闭
  const handleClose = () => {
    setVisible(false);
    emits('close', value.value);
  };

</script>

<style lang="less" scoped>
  :deep(.arco-modal-title) {
    font-size: 14px;
  }
</style>
