<template>
  <a-table row-key="name"
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
    <!-- 文件大小 -->
    <template #size="{ record }">
      <span v-if="editRecord.name === record.name">操作</span>
      <span v-else>{{ record.size }}</span>
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
  import columns from './types/table.columns';

  const props = defineProps<{
    list: Array<SftpFile>;
    loading: boolean;
  }>();

  const rowSelection = useRowSelection();
  const selectedKeys = ref<Array<string>>([]);
  const editRecord = ref<TableData>({});

  // 设置选中状态
  const setEditable = (record: TableData) => {
    editRecord.value = record;
    record.hover = true;
  };

  // 设置未选中状态
  const unsetEditable = (record: TableData) => {
    setTimeout(() => {
      if (record.name === editRecord.value.name && !record.hover) {
        editRecord.value = {};
      }
    }, 20);
    record.hover = false;
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

</style>
