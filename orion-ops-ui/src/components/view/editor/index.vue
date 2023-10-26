<template>
  <div ref="editorContainer"
       :class="{
         'editor-wrapper': true,
         [containerClass]: !!containerClass
       }"
       :style="{
          ...containerStyle
       }" />
</template>

<script lang="ts">
  export default {
    name: 'index'
  };
</script>

<script lang="ts" setup>
  import type { Theme, Options } from './core';
  import type { PropType } from 'vue';
  import * as monaco from 'monaco-editor';
  import { createDefaultOptions } from './core';
  import { onBeforeUnmount, onMounted, ref, watch } from 'vue';
  import { useAppStore } from '@/store';

  const appStore = useAppStore();

  const emits = defineEmits(['update:modelValue', 'change', 'editor-mounted']);

  const props = defineProps({
    modelValue: {
      type: String,
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '100%'
    },
    readonly: {
      type: Boolean,
      default: false
    },
    language: {
      type: String,
      default: 'json',
    },
    containerClass: String,
    containerStyle: Object,
    theme: {
      type: [String, Boolean] as PropType<Theme | boolean>,
      default: true,
    },
    options: {
      type: Object as PropType<Options>,
      default: () => {
        return {};
      }
    }
  });

  const editorContainer = ref();
  let editor: any;

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
    // 监听值的变化
    editor.onDidChangeModelContent(() => {
      const value = editor.getValue();
      emits('update:modelValue', value);
      emits('change', value);
    });
    emits('editor-mounted', editor);
  };

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
  });

  onMounted(() => {
    init();
  });

  onBeforeUnmount(() => {
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
