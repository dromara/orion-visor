<template>
  <a-drawer v-model:visible="visible"
            :title="title"
            :esc-to-close="false"
            :mask-closable="false"
            :unmount-on-close="true"
            @ok="handleOk"
            @cancel="handleCancel">
    <div class="full">
      <editor v-model="value"
              language="shell"
              :suggestions="true"
              :auto-focus="true"
              :theme="dark ? 'vs-dark' : 'vs'"
              :readonly="readonly" />
    </div>
  </a-drawer>
</template>

<script lang="ts">
  export default {
    name: 'shellEditorDrawer'
  };
</script>

<script lang="ts" setup>
  import { ref } from 'vue';
  import useVisible from '@/hooks/visible';

  const emits = defineEmits(['ok', 'cancel']);

  const props = withDefaults(defineProps<Partial<{
    dark: boolean;
    readonly: boolean;
  }>>(), {
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

  // 确定
  const handleOk = () => {
    setVisible(false);
    emits('ok', value.value);
  };

  // 取消
  const handleCancel = () => {
    setVisible(false);
    emits('cancel', value.value);
  };

</script>

<style lang="less" scoped>
</style>
