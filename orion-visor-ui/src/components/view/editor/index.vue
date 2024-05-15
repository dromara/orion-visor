<template>
  <div ref="editorContainer"
       class="editor-wrapper"
       :class="[ !!containerClass ? containerClass : '' ]"
       :style="{ ...containerStyle }" />
</template>

<script lang="ts">
  export default {
    name: 'editor'
  };
</script>

<script lang="ts" setup>
  import type { Theme, Options } from './core';
  import type { CSSProperties } from 'vue';
  import * as monaco from 'monaco-editor';
  import { createDefaultOptions } from './core';
  import { onBeforeUnmount, onMounted, ref, watch } from 'vue';
  import { useAppStore } from '@/store';
  import shellSuggestions from './languages/shell-suggestions';

  const appStore = useAppStore();

  const emits = defineEmits(['update:modelValue', 'change', 'editor-mounted']);

  const props = withDefaults(defineProps<Partial<{
    modelValue: string;
    width: string;
    height: string;
    readonly: boolean;
    autoFocus: boolean;
    language: string;
    suggestions: boolean;
    containerClass: string;
    containerStyle: CSSProperties;
    theme: Theme | boolean;
    options: Options;
  }>>(), {
    width: '100%',
    height: '100%',
    readonly: false,
    autoFocus: false,
    language: 'json',
    suggestions: false,
    theme: true,
    options: () => {
      return {};
    },
  });

  const editorContainer = ref();
  let editor: any;
  let completionItemProvider: any;

  // 初始化
  const init = () => {
    const options = {
      value: props.modelValue,
      language: props.language,
      readOnly: props.readonly,
      theme: props.theme === true ? (appStore.theme === 'dark' ? 'vs-dark' : 'vs') : props.theme,
      ...createDefaultOptions(),
      ...props.options,
    };
    // 创建编辑器
    editor = monaco.editor.create(editorContainer.value, options);
    // 注册代码提示
    registerSuggestions();
    // 自动聚焦
    if (props.autoFocus) {
      editor.focus();
    }
    // 监听值的变化
    editor.onDidChangeModelContent(() => {
      const value = editor.getValue();
      emits('update:modelValue', value);
      emits('change', value);
    });
    emits('editor-mounted', editor);
  };

  // 获取值
  const getValue = () => {
    return editor?.getValue();
  };

  // 设置值
  const setValue = (value: string) => {
    editor?.setValue(value);
  };

  defineExpose({ getValue, setValue });

  // 监听主题变更
  watch(() => appStore.theme, (v) => {
    if (editor && props.theme === true) {
      editor.updateOptions({ theme: v === 'dark' ? 'vs-dark' : 'vs' });
    }
  });

  // 监听数据变更
  watch(() => props.modelValue, (v) => {
    if (editor) {
      const value = editor.getValue();
      if (v !== value) {
        editor.setValue(v);
      }
    }
  });

  // 监听配置变更
  watch(() => props.options, (v) => {
    if (editor) {
      editor.updateOptions(v);
    }
  }, { deep: true });

  // 监听只读
  watch(() => props.readonly, () => {
    if (editor) {
      editor.updateOptions({ readOnly: props.readonly });
    }
  });

  // 修改语言
  watch(() => props.language, (v) => {
    if (editor) {
      monaco.editor.setModelLanguage(editor?.getModel(), v as string);
    }
    // 注册代码提示
    registerSuggestions();
  });

  // 注册代码提示
  const registerSuggestions = () => {
    if (!props.suggestions) {
      return;
    }
    if (props.language === 'shell') {
      completionItemProvider?.dispose();
      completionItemProvider = monaco.languages.registerCompletionItemProvider(props.language, shellSuggestions);
    }
  };

  // 初始化
  onMounted(init);

  // 卸载
  onBeforeUnmount(() => {
    completionItemProvider?.dispose();
    completionItemProvider = undefined;
    editor?.dispose();
    editor = undefined;
  });

</script>

<style lang="less" scoped>
  .editor-wrapper {
    width: 100%;
    height: 100%;
  }
</style>
