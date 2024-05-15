<template>
  <div class="layout-container" v-if="render">
    <!-- 列表-表格 -->
    <exec-command-log-table ref="tableRef"
                            @view-command="viewCommand"
                            @view-params="viewParams"
                            @view-log="viewLog"
                            @open-clear="openClearModal" />
    <!-- 清理模态框 -->
    <exec-command-log-clear-modal ref="clearModal"
                                  @clear="clearCallback" />
    <!-- 执行日志模态框 -->
    <exec-log-panel-modal ref="logModal"
                          type="BATCH" />
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
    name: 'execCommandLog'
  };
</script>

<script lang="ts" setup>
  import { ref, onBeforeMount } from 'vue';
  import { useDictStore } from '@/store';
  import { dictKeys } from '@/components/exec/log/const';
  import { useRouter } from 'vue-router';
  import { openNewRoute } from '@/router';
  import ExecCommandLogTable from './components/exec-command-log-table.vue';
  import ExecCommandLogClearModal from './components/exec-command-log-clear-modal.vue';
  import JsonEditorModal from '@/components/view/json-editor/modal/index.vue';
  import ShellEditorModal from '@/components/view/shell-editor/modal/index.vue';
  import ExecLogPanelModal from '@/components/exec/log/panel-modal/index.vue';

  const router = useRouter();

  const render = ref(false);
  const tableRef = ref();
  const logModal = ref();
  const clearModal = ref();
  const jsonModal = ref();
  const shellModal = ref();

  // 打开清理模态框
  const openClearModal = (e: any) => {
    clearModal.value.open(e);
  };

  // 查看命令
  const viewCommand = (data: string) => {
    shellModal.value.open(data, '命令');
  };

  // 查看参数
  const viewParams = (data: string) => {
    jsonModal.value.open(JSON.parse(data));
  };

  // 查看日志
  const viewLog = (id: number, newWindow: boolean) => {
    if (newWindow) {
      // 跳转新页面
      openNewRoute({
        name: 'execCommand',
        query: {
          id
        }
      });
    } else {
      logModal.value.open(id);
    }
  };

  // 清理回调
  const clearCallback = () => {
    tableRef.value.fetchTableData();
  };

  onBeforeMount(async () => {
    const dictStore = useDictStore();
    await dictStore.loadKeys(dictKeys);
    render.value = true;
  });

</script>

<style lang="less" scoped>

</style>
