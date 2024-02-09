<template>
  <a-table row-key="path"
           class="sftp-table"
           label-align="left"
           :columns="columns"
           v-model:selected-keys="selectedKeys"
           :row-selection="rowSelection"
           :sticky-header="true"
           :data="list"
           :pagination="false"
           :bordered="false"
           @cell-mouse-enter="setEditable"
           @cell-mouse-leave="unsetEditable">
    <!-- 文件搜索框 -->
    <template #nameFilter="{ filterValue, setFilterValue, handleFilterConfirm, handleFilterReset}">
      <div class="name-filter">
        <a-space direction="vertical">
          <!-- 过滤输入框 -->
          <a-input size="small"
                   :model-value="filterValue[0]"
                   @input="(value) => setFilterValue([value])" />
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
      <!-- 文件图标 -->
      <span class="file-name-icon" :title="formatFileType(record.attr).label">
        <component :is="formatFileType(record.attr).icon" />
      </span>
      <span>
        {{ record.name }}
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
                @click="copy(record.path, false)">
            <icon-copy />
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
                @click="chmodFile(record.path, record.attr)">
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
  import type { SftpFile } from '../../types/terminal.type';
  import { ref } from 'vue';
  import { useRowSelection } from '@/types/table';
  import { dateFormat } from '@/utils';
  import columns from './types/table.columns';
  import useCopy from '@/hooks/copy';
  import { FILE_TYPE } from '../../types/terminal.const';

  import type { ISftpSession } from '../../types/terminal.type';
  import useLoading from '@/hooks/loading';
  import { useCacheStore } from '@/store';
  import { computed } from 'vue/dist/vue';

  const props = defineProps<{
    session: ISftpSession | undefined;
    list: Array<SftpFile>;
    loading: boolean;
    selectedFiles: Array<string>;
  }>();

  const emits = defineEmits(['update:selectedFiles']);

  const rowSelection = useRowSelection({ width: 40 });
  const { copy } = useCopy();

  const selectedKeys = computed({
    get() {
      return props.selectedFiles;
    },
    set(e) {
      emits('update:selectedFiles', e);
    }
  });

  const editRowName = ref<string>('');

  // 设置选中状态
  const setEditable = (record: TableData) => {
    editRowName.value = record.name;
    record.hover = true;
  };

  // 设置未选中状态
  const unsetEditable = (record: TableData) => {
    setTimeout(() => {
      // 等待后如果还是当前行 但是未被选中则代表已经被失焦
      if (record.name === editRowName.value && !record.hover) {
        editRowName.value = '';
      }
    }, 20);
    record.hover = false;
  };

  // 删除文件
  const deleteFile = (path: string) => {
    // confirm
  };

  // 下载文件
  const downloadFile = (path: string) => {
  };

  // 移动文件
  const moveFile = (path: string) => {
    // openModal('path')
  };

  // 文件提权
  const chmodFile = (path: string, attr: string) => {
    // openModal('path','mod')

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
