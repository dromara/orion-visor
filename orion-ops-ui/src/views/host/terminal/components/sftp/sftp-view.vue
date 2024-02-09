<template>
  <div class="sftp-container">
    <a-split class="split-view"
             v-model:size="splitSize"
             :min="0.3"
             :disabled="!editView">
      <!-- 左侧面板表格 -->
      <template #first>
        <!-- FIXME spin -->
        <div class="sftp-table-container">
          <!-- 表头 -->
          <sftp-table-header class="sftp-table-header"
                             :current-path="currentPath"
                             :session="session"
                             :selected-files="selectFiles"
                             @load-file="loadFiles" />
          <!-- 表格 -->
          <sftp-table class="sftp-table-wrapper"
                      v-model:selected-files="selectFiles"
                      :session="session"
                      :list="fileList"
                      :loading="tableLoading" />
        </div>
      </template>
      <template #second v-if="editView">
        <div>editor</div>
      </template>
    </a-split>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'sftpView'
  };
</script>

<script lang="ts" setup>
  import type { ISftpSession, SftpFile, TerminalTabItem } from '../../types/terminal.type';
  import { onMounted, onUnmounted, ref } from 'vue';
  import { useTerminalStore } from '@/store';
  import useLoading from '@/hooks/loading';
  import mockData from './data';
  import { Message } from '@arco-design/web-vue';
  import SftpTable from './sftp-table.vue';
  import SftpTableHeader from './sftp-table-header.vue';

  const props = defineProps<{
    tab: TerminalTabItem
  }>();

  const { preference, sessionManager } = useTerminalStore();
  const { loading: tableLoading, setLoading: setTableLoading } = useLoading(true);

  const session = ref<ISftpSession>();
  const currentPath = ref<string>('');
  const fileList = ref<Array<SftpFile>>(mockData);
  const selectFiles = ref<Array<string>>([]);
  const splitSize = ref(1);
  const editView = ref(true);

  // 连接成功回调
  const connectCallback = () => {
    loadFiles(undefined);
  };

  // 加载文件列表
  const loadFiles = (path: string | undefined) => {
    setTableLoading(true);
    session.value?.list(path);
  };

  // 接收列表回调
  const resolveList = (result: string, path: string, list: Array<SftpFile>) => {
    const success = !!Number.parseInt(result);
    setTableLoading(false);
    if (!success) {
      Message.error('查询失败');
      return;
    }
    currentPath.value = path;
    fileList.value = list;
  };

  // 初始化会话
  onMounted(async () => {
    // 创建终端处理器
    session.value = await sessionManager.openSftp(props.tab, {
      connectCallback,
      resolveList
    });
  });

  // 关闭会话
  onUnmounted(() => {
    sessionManager.closeSession(props.tab.key);
  });

</script>

<style lang="less" scoped>
  @sftp-table-header-height: 32px + 8px;

  .sftp-container {
    width: 100%;
    height: calc(100vh - var(--header-height) - var(--panel-nav-height));
    position: relative;

    .split-view {
      width: 100%;
      height: 100%;
    }
  }

  .sftp-table-container {
    padding: 8px;
    height: 100%;

    .sftp-table-header {
      width: 100%;
      height: @sftp-table-header-height;
      padding-bottom: 8px;
    }

    .sftp-table-wrapper {
      height: calc(100% - @sftp-table-header-height);
    }
  }

</style>
