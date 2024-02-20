<template>
  <div class="sftp-container">
    <a-split class="split-view"
             v-model:size="splitSize"
             :min="0.3"
             :disabled="!editView">
      <!-- 左侧面板表格 -->
      <template #first>
        <a-spin class="sftp-table-container"
                :loading="tableLoading"
                :hide-icon="true">
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
                      :loading="tableLoading"
                      @load-file="loadFiles" />
        </a-spin>
      </template>
      <template #second v-if="editView">
        <div>editor</div>
      </template>
    </a-split>
    <!-- 创建文件模态框 -->
    <sftp-create-modal ref="createModal" />
    <!-- 移动文件模态框 -->
    <sftp-move-modal ref="moveModal" />
    <!-- 文件提权模态框 -->
    <sftp-chmod-modal ref="chmodModal" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'sftpView'
  };
</script>

<script lang="ts" setup>
  import type { ISftpSession, SftpFile, TerminalTabItem } from '../../types/terminal.type';
  import { onMounted, onUnmounted, provide, ref } from 'vue';
  import { useTerminalStore } from '@/store';
  import { Message } from '@arco-design/web-vue';
  import useLoading from '@/hooks/loading';
  import { openSftpCreateModalKey, openSftpMoveModalKey, openSftpChmodModalKey } from '../../types/terminal.const';
  import SftpTable from './sftp-table.vue';
  import SftpTableHeader from './sftp-table-header.vue';
  import SftpCreateModal from './sftp-create-modal.vue';
  import SftpMoveModal from './sftp-move-modal.vue';
  import SftpChmodModal from './sftp-chmod-modal.vue';

  const props = defineProps<{
    tab: TerminalTabItem
  }>();

  const { preference, sessionManager } = useTerminalStore();
  const { loading: tableLoading, setLoading: setTableLoading } = useLoading(true);

  const session = ref<ISftpSession>();
  const currentPath = ref<string>('');
  const fileList = ref<Array<SftpFile>>([]);
  const selectFiles = ref<Array<string>>([]);
  const splitSize = ref(1);
  const editView = ref(true);
  const createModal = ref();
  const moveModal = ref();
  const chmodModal = ref();

  // 暴露打开创建模态框
  provide(openSftpCreateModalKey, (sessionId: string, path: string, isTouch: boolean) => {
    createModal.value?.open(sessionId, path, isTouch);
  });

  // 暴露打开移动模态框
  provide(openSftpMoveModalKey, (sessionId: string, path: string) => {
    moveModal.value?.open(sessionId, path);
  });

  // 暴露打开提权模态框
  provide(openSftpChmodModalKey, (sessionId: string, path: string, permission: number) => {
    chmodModal.value?.open(sessionId, path, permission);
  });

  // 连接成功回调
  const connectCallback = () => {
    loadFiles(undefined);
  };

  // 加载文件列表
  const loadFiles = (path: string | undefined) => {
    setTableLoading(true);
    session.value?.list(path);
  };

  // 检查结果
  const checkResult = (result: string, msg: string): boolean => {
    const success = !!Number.parseInt(result);
    if (!success) {
      Message.error(msg);
    }
    return success;
  };

  // 接收列表回调
  const resolveList = (result: string, path: string, list: Array<SftpFile>) => {
    setTableLoading(false);
    if (!checkResult(result, '查询失败')) {
      return;
    }
    currentPath.value = path;
    fileList.value = list;
    selectFiles.value = [];
  };

  // 接收文件响应
  const resolveFileAction = (result: string, msg: string) => {
    setTableLoading(false);
    // 检查结果
    if (!checkResult(result, msg)) {
      return;
    }
    Message.success('操作成功');
    // 刷新列表
    loadFiles(currentPath.value);
  };

  // 接收获取文件内容响应
  const resolveSftpGetContent = (path: string, result: string, content: string) => {
  };

  // 接收修改文件内容响应
  const resolveSftpSetContent = (result: string, msg: string) => {
  };

  // 初始化会话
  onMounted(async () => {
    // 创建终端处理器
    session.value = await sessionManager.openSftp(props.tab, {
      connectCallback,
      resolveList,
      resolveSftpMkdir: resolveFileAction,
      resolveSftpTouch: resolveFileAction,
      resolveSftpMove: resolveFileAction,
      resolveSftpRemove: resolveFileAction,
      resolveSftpChmod: resolveFileAction,
      resolveSftpGetContent,
      resolveSftpSetContent,
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
    width: 100%;
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
