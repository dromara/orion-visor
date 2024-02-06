<template>
  <div class="sftp-container">
    <!-- 头部 -->
    <div class="sftp-header">
      <!-- 左侧操作 -->
      <div class="sftp-header-left">
        home input
      </div>
      <!-- 右侧操作 -->
      <div class="sftp-header-right">
        上传 下载 删除 刷新 copy touch mk
      </div>
    </div>
    <a-split class="split-view"
             v-model:size="splitSize"
             :min="0.3"
             :disabled="!editView">
      <!-- 表格 -->
      <template #first>
        <sftp-table :list="list"
                    :loading="loading" />
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
  import SftpTable from '@/views/host/terminal/components/sftp/sftp-table.vue';

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
  @sftp-header-height: 36px;

  .sftp-container {
    width: 100%;
    height: calc(100vh - var(--header-height) - var(--panel-nav-height));
    position: relative;
  }

  .split-view {
    width: 100%;
    height: calc(100% - @sftp-header-height);
  }

  .sftp-header {
    width: 100%;
    height: @sftp-header-height;
    padding: 0 8px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    background: var(--color-bg-panel-bar);

    &-left, &-right {
      display: flex;
      align-items: center;
      height: 100%;
    }

    &-right {
      justify-content: flex-end;
    }
  }

</style>
