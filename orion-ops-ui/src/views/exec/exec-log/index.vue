<template>
  <div class="layout-container" v-if="render">
    <!-- 列表-表格 -->
    <exec-log-table @view-command="viewCommand"
                    @view-params="viewParams" />
    <!-- json 模态框 -->
    <json-editor-modal ref="jsonModal"
                       :esc-to-close="true" />
    <!-- shell 模态框 -->
    <shell-editor-modal ref="shellModal"
                        :footer="false"
                        :esc-to-close="true" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'execLog'
  };
</script>

<script lang="ts" setup>
  import { ref, onBeforeMount } from 'vue';
  import { useDictStore } from '@/store';
  import { dictKeys } from './types/const';
  import ExecLogTable from './components/exec-log-table.vue';
  import JsonEditorModal from '@/components/view/json-editor/modal/index.vue';
  import ShellEditorModal from '@/components/view/shell-editor/modal/index.vue';

  const render = ref(false);
  const jsonModal = ref();
  const shellModal = ref();

  // 查看命令
  const viewCommand = (data: string) => {
    shellModal.value.open(data, '命令');
  };

  // 查看参数
  const viewParams = (data: string) => {
    jsonModal.value.open(JSON.parse(data));
  };

  onBeforeMount(async () => {
    const dictStore = useDictStore();
    await dictStore.loadKeys(dictKeys);
    render.value = true;
  });

</script>

<style lang="less" scoped>

</style>
