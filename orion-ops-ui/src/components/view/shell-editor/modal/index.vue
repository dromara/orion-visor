<template>
  <a-modal v-model:visible="visible"
           title-align="start"
           :width="width"
           :body-style="{padding: '16px 8px'}"
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
              :auto-focus="true"
              :theme="dark ? 'vs-dark' : 'vs'" />
    </div>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'shellEditorModal'
  };
</script>

<script lang="ts" setup>
  import { onMounted, ref } from 'vue';
  import useVisible from '@/hooks/visible';
  import * as monaco from 'monaco-editor';
  import { language } from 'monaco-editor/esm/vs/basic-languages/shell/shell.js';

  const props = defineProps({
    width: {
      type: [String, Number],
      default: '60%'
    },
    height: {
      type: String,
      default: 'calc(100vh - 280px)'
    },
    dark: {
      type: Boolean,
      default: true
    },
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
  };

  // 初始化
  onMounted(() => {
    // 代码提示
    monaco.languages.registerCompletionItemProvider('shell', {
      provideCompletionItems() {
        const suggestions: any = [];
        language.keywords?.forEach((item: any) => {
          suggestions.push({
            label: item,
            kind: monaco.languages.CompletionItemKind.Keyword,
            insertText: item,
          });
        });
        language.builtins?.forEach((item: any) => {
          suggestions.push({
            label: item,
            kind: monaco.languages.CompletionItemKind.Function,
            insertText: item,
          });
        });
        return {
          suggestions: [...new Set(suggestions)],
        };
      },
    });
  });

</script>

<style lang="less" scoped>
  :deep(.arco-modal-title) {
    font-size: 14px;
  }
</style>
