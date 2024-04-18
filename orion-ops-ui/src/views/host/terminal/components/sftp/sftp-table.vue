<template>
  <a-table row-key="path"
           ref="tableRef"
           class="sftp-table"
           :columns="columns"
           v-model:selected-keys="selectedKeys"
           :row-selection="rowSelection"
           :sticky-header="true"
           :data="list"
           :pagination="false"
           :bordered="false"
           :loading="loading"
           @cell-mouse-enter="setRowOperable"
           @cell-mouse-leave="unsetRowOperable">
    <!-- 文件搜索框 -->
    <template #nameFilter="{ filterValue, setFilterValue, handleFilterConfirm, handleFilterReset}">
      <div class="name-filter">
        <a-space direction="vertical">
          <!-- 过滤输入框 -->
          <a-input size="small"
                   :ref="setAutoFocus as unknown as VNodeRef"
                   :model-value="filterValue[0]"
                   @input="(value) => setFilterValue([value])"
                   @press-enter="handleFilterConfirm" />
          <!-- 按钮 -->
          <div class="name-filter-footer">
            <a-button size="small" @click="handleFilterConfirm">过滤</a-button>
            <a-button size="small" @click="handleFilterReset">重置</a-button>
          </div>
        </a-space>
      </div>
    </template>
    <!-- 文件名称 -->
    <template #fileName="{ record }">
      <span :title="record.name">
        <!-- 文件图标 -->
        <span class="file-name-icon" :title="formatFileType(record.attr).label">
          <component :is="formatFileType(record.attr).icon" />
        </span>
        <!-- 文件名称 -->
        <span :class="[ record.isDir ? 'pointer' : 'text-copy']"
              @click="clickFilename(record)">
          {{ record.name }}
        </span>
      </span>
    </template>
    <!-- 文件大小 -->
    <template #size="{ record }">
      <span>{{ record.size }}</span>
    </template>
    <!-- 修改时间/操作 -->
    <template #modifyTime="{ record }">
      <!-- 修改时间 -->
      <span v-if="editRowName !== record.name">{{ dateFormat(new Date(record.modifyTime)) }}</span>
      <!-- 操作 -->
      <a-space v-else>
        <!-- 复制路径 -->
        <a-tooltip position="top"
                   :mini="true"
                   :overlay-inverse="true"
                   :auto-fix-position="false"
                   content-class="terminal-tooltip-content"
                   arrow-class="terminal-tooltip-content"
                   content="复制路径">
          <span class="click-icon-wrapper row-action-icon"
                @click="copy(record.path, '已复制')">
            <icon-copy />
          </span>
        </a-tooltip>
        <!-- 编辑内容 -->
        <a-tooltip v-if="canEditable(record.size, record.attr)"
                   position="top"
                   :mini="true"
                   :overlay-inverse="true"
                   :auto-fix-position="false"
                   content-class="terminal-tooltip-content"
                   arrow-class="terminal-tooltip-content"
                   content="编辑内容">
          <span class="click-icon-wrapper row-action-icon"
                @click="editFile(record)">
            <icon-edit />
          </span>
        </a-tooltip>
        <!-- 删除 -->
        <a-tooltip position="top"
                   :mini="true"
                   :auto-fix-position="false"
                   content-class="terminal-tooltip-content"
                   arrow-class="terminal-tooltip-content"
                   content="删除">
          <span class="click-icon-wrapper row-action-icon"
                @click="deleteFile(record.path)">
            <icon-delete />
          </span>
        </a-tooltip>
        <!-- 下载 -->
        <a-tooltip position="top"
                   :mini="true"
                   :auto-fix-position="false"
                   content-class="terminal-tooltip-content"
                   arrow-class="terminal-tooltip-content"
                   content="下载">
          <span class="click-icon-wrapper row-action-icon"
                @click="downloadFile(record.path)">
            <icon-download />
          </span>
        </a-tooltip>
        <!-- 移动 -->
        <a-tooltip position="top"
                   :mini="true"
                   :auto-fix-position="false"
                   content-class="terminal-tooltip-content"
                   arrow-class="terminal-tooltip-content"
                   content="移动">
          <span class="click-icon-wrapper row-action-icon"
                @click="moveFile(record.path)">
            <icon-paste />
          </span>
        </a-tooltip>
        <!-- 提权 -->
        <a-tooltip position="top"
                   :mini="true"
                   :auto-fix-position="false"
                   content-class="terminal-tooltip-content"
                   arrow-class="terminal-tooltip-content"
                   content="提权">
          <span class="click-icon-wrapper row-action-icon"
                @click="chmodFile(record.path, record.permission)">
            <icon-user-group />
          </span>
        </a-tooltip>
      </a-space>
    </template>
  </a-table>
</template>

<script lang="ts">
  export default {
    name: 'sftpTable'
  };
</script>

<script lang="ts" setup>
  import type { TableData } from '@arco-design/web-vue/es/table/interface';
  import type { SftpFile, ISftpSession } from '../../types/terminal.type';
  import type { VNodeRef } from 'vue';
  import { ref, computed, watch, inject } from 'vue';
  import { useRowSelection } from '@/types/table';
  import { dateFormat } from '@/utils';
  import { setAutoFocus } from '@/utils/dom';
  import { copy } from '@/hooks/copy';
  import columns from './types/table.columns';
  import { FILE_TYPE, openSftpChmodModalKey, openSftpMoveModalKey } from '../../types/terminal.const';

  const previewSize = import.meta.env.VITE_SFTP_PREVIEW_MB;

  const props = defineProps<{
    session?: ISftpSession;
    list: Array<SftpFile>;
    loading: boolean;
    closed: boolean;
    selectedFiles: Array<string>;
  }>();

  const emits = defineEmits(['update:selectedFiles', 'loadFile', 'editFile', 'download']);

  const openSftpMoveModal = inject(openSftpMoveModalKey) as (sessionId: string, path: string) => void;
  const openSftpChmodModal = inject(openSftpChmodModalKey) as (sessionId: string, path: string, permission: number) => void;

  const rowSelection = useRowSelection({ width: 40 });

  // 切换页面自动清空过滤
  watch(() => props.list, () => {
    tableRef.value?.clearFilters();
  });

  const selectedKeys = computed({
    get() {
      return props.selectedFiles;
    },
    set(e) {
      emits('update:selectedFiles', e);
    }
  });

  const editRowName = ref<string>('');
  const tableRef = ref();

  // 设置行可操作状态
  const setRowOperable = (record: TableData) => {
    editRowName.value = record.name;
    record.hover = true;
  };

  // 设置行不可操作状态
  const unsetRowOperable = (record: TableData) => {
    setTimeout(() => {
      // 等待后如果还是当前行 但是未被选中则代表已经被失焦
      if (record.name === editRowName.value && !record.hover) {
        editRowName.value = '';
      }
    }, 20);
    record.hover = false;
  };

  // 是否可编辑
  const canEditable = (size: number, attr: string) => {
    // 是普通文件 && 文件小于 配置大小(MB) 可以编辑
    return FILE_TYPE.NORMAL_FILE.value == formatFileType(attr).value
      && size <= previewSize * 1024 * 1024;
  };

  // 点击文件名称
  const clickFilename = (record: TableData) => {
    if (record.isDir) {
      // 检查是否断开
      if (props.closed) {
        return;
      }
      // 进入文件夹
      emits('loadFile', record.path);
    } else {
      copy(record.name, '名称已复制');
    }
  };

  // 编辑文件
  const editFile = (record: TableData) => {
    // 检查是否断开
    if (props.closed) {
      return;
    }
    emits('editFile', record.name, record.path);
    props.session?.getContent(record.path);
  };

  // 删除文件
  const deleteFile = (path: string) => {
    // 检查是否断开
    if (props.closed) {
      return;
    }
    props.session?.remove([path]);
  };

  // 下载文件
  const downloadFile = (path: string) => {
    // 检查是否断开
    if (props.closed) {
      return;
    }
    emits('download', [path]);
  };

  // 移动文件
  const moveFile = (path: string) => {
    // 检查是否断开
    if (props.closed) {
      return;
    }
    openSftpMoveModal(props.session?.sessionId as string, path);
  };

  // 文件提权
  const chmodFile = (path: string, permission: number) => {
    // 检查是否断开
    if (props.closed) {
      return;
    }
    openSftpChmodModal(props.session?.sessionId as string, path, permission);
  };

  // 格式化文件类型
  const formatFileType = (attr: string) => {
    return Object.values(FILE_TYPE).find(s => {
      return s.value === attr.charAt(0);
    }) || FILE_TYPE.NORMAL_FILE;
  };

</script>

<style lang="less" scoped>
  .sftp-table {
    position: relative;
    height: 100%;
  }

  .name-filter {
    padding: 12px;
    background: var(--color-bg-5);
    border: 1px solid var(--color-neutral-3);
    border-radius: var(--border-radius-medium);
    box-shadow: 0 2px 5px rgb(0 0 0 / 10%);

    &-footer {
      display: flex;
      justify-content: space-between;
    }
  }

  .file-name-icon {
    font-size: 16px;
    line-height: 16px;
    margin-right: 6px;
  }

  .row-action-icon {
    font-size: 16px;
    padding: 4px;
    background: unset;

    &:hover {
      background: var(--color-fill-3);
    }
  }

  :deep(.action-cell .arco-table-cell) {
    padding: 0;
  }

  :deep(.arco-table-th-title) {
    user-select: none;
  }

</style>
