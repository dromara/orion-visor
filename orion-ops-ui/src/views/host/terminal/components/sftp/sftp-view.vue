<template>
  <div class="sftp-container">
    <a-split class="split-view"
             v-model:size="splitSize"
             :min="0.3"
             :disabled="!editView">
      <!-- 左侧面板表格 -->
      <template #first>
        <div class="sftp-table-container">
          <!-- 表头 -->
          <sftp-table-header class="sftp-table-header" />
          <!-- 表格 -->
          <sftp-table class="sftp-table-wrapper"
                      :list="list"
                      :loading="loading" />
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
  import data from './data';
  import SftpTable from './sftp-table.vue';
  import SftpTableHeader from './sftp-table-header.vue';

  const props = defineProps<{
    tab: TerminalTabItem
  }>();

  const { preference, sessionManager } = useTerminalStore();
  const { loading, setLoading } = useLoading(true);

  const session = ref<ISftpSession>();
  const currentPath = ref<string>('');
  const list = ref<Array<SftpFile>>(data);
  const splitSize = ref(1);
  const editView = ref(true);

  // 初始化会话
  onMounted(async () => {
    // 创建终端处理器
    session.value = await sessionManager.openSftp(props.tab, {
      list,
      currentPath,
      setLoading
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
