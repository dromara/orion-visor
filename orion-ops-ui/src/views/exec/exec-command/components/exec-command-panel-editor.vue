<template>
  <div class="container">
    <!-- 表头 -->
    <div class="panel-header">
      <h3>执行命令</h3>
      <span v-permission="['asset:exec-template:query']"
            class="span-blue usn pointer"
            @click="openTemplate">
        从模板中选择
      </span>
    </div>
    <!-- 命令编辑器 -->
    <div class="editor-wrapper">
      <slot />
    </div>
    <!-- 命名提示信息 -->
    <div v-pre class="editor-help">
      使用 @{{ xxx }} 来替换参数, 输入_可以获取全部变量
    </div>
    <!-- 命令模板模态框 -->
    <exec-template-modal ref="templateModal"
                         @selected="s => emits('selected', s)" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'execCommandPanelEditor'
  };
</script>

<script lang="ts" setup>
  import { ref } from 'vue';
  import ExecTemplateModal from '@/components/exec/template/modal/index.vue';

  const emits = defineEmits(['selected']);

  const templateModal = ref<any>();

  // 打开模板
  const openTemplate = () => {
    templateModal.value.open();
  };

</script>

<style lang="less" scoped>
  .editor-wrapper {
    width: 100%;
    height: calc(100% - 56px);
    position: relative;
  }

  .editor-help {
    user-select: none;
    display: flex;
    margin-top: 8px;
    height: 18px;
    color: var(--color-text-3);
    overflow: hidden;
    white-space: nowrap;
  }

</style>
