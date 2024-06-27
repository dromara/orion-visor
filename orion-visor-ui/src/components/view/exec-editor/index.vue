<template>
  <editor language="shell"
          :suggestions="false" />
</template>

<script lang="ts">
  export default {
    name: 'execEditor'
  };
</script>

<script lang="ts" setup>
  import type { TemplateParam } from './const';
  import { onMounted, onUnmounted, ref } from 'vue';
  import { builtinParams, templatePrefix, templateSuffix, triggerPrefix } from './const';
  import * as monaco from 'monaco-editor';
  import { language } from 'monaco-editor/esm/vs/basic-languages/shell/shell.js';

  const props = defineProps<{
    parameter?: Array<TemplateParam>
  }>();

  const suggestionsDispose = ref();

  // 加载代码提示
  onMounted(() => {
    if (suggestionsDispose.value) {
      return;
    }
    // 代码提示
    suggestionsDispose.value = monaco.languages.registerCompletionItemProvider('shell', {
      provideCompletionItems() {
        const suggestions: any = [];
        language.keywords?.forEach((item: any) => {
          suggestions.push({
            label: item,
            kind: monaco.languages.CompletionItemKind.Keyword,
            insertText: item
          });
        });
        language.builtins?.forEach((item: any) => {
          suggestions.push({
            label: item,
            kind: monaco.languages.CompletionItemKind.Function,
            insertText: item,
          });
        });
        // 内置参数提示
        builtinParams.forEach(s => {
          suggestions.push({
            label: triggerPrefix + s.name,
            kind: monaco.languages.CompletionItemKind.Variable,
            insertText: templatePrefix + s.name + templateSuffix,
            detail: s.desc || '',
            documentation: s.desc || '',
          });
        });
        // 命令参数提示
        props.parameter?.forEach(s => {
          if (!s.name) {
            return;
          }
          suggestions.push({
            label: triggerPrefix + s.name,
            kind: monaco.languages.CompletionItemKind.Variable,
            insertText: templatePrefix + s.name + templateSuffix,
            detail: s.desc || '',
            documentation: s.desc || '',
          });
        });
        return {
          suggestions: [...new Set(suggestions)],
        };
      },
    });
  });

  // 卸载代码提示
  onUnmounted(() => {
    suggestionsDispose.value?.dispose();
    suggestionsDispose.value = undefined;
  });

</script>

<style lang="less" scoped>

</style>
