<template>
  <a-table v-model:selected-keys="selectedKeysValue"
           row-key="id"
           ref="tableRef"
           class="table-resize"
           :columns="columns"
           :row-selection="rowSelection"
           row-class="pointer"
           :data="hostList"
           :scroll="{ y: '100%' }"
           :pagination="false"
           :bordered="true"
           :column-resizable="true"
           @row-click="clickRow">
    <!-- 空 -->
    <template #empty>
      <a-empty>
        {{ emptyMessage }}
      </a-empty>
    </template>
    <!-- 名称 -->
    <template #name="{ record }">
      {{ record.name }}
    </template>
    <!-- 编码 -->
    <template #code="{ record }">
      <a-tag>{{ record.code }}</a-tag>
    </template>
    <!-- 地址 -->
    <template #address="{ record }">
      <span class="span-blue">{{ record.address }}</span>
    </template>
    <!-- 标签 -->
    <template #tags="{ record }">
      <a-space v-if="record.tags"
               style="margin-bottom: -8px;"
               :wrap="true">
        <a-tag v-for="tag in record.tags"
               :key="tag.id"
               :color="dataColor(tag.name, tagColor)">
          {{ tag.name }}
        </a-tag>
      </a-space>
    </template>
  </a-table>
</template>

<script lang="ts">
  export default {
    name: 'hostTable'
  };
</script>

<script lang="ts" setup>
  import type { TableData } from '@arco-design/web-vue';
  import type { HostQueryResponse } from '@/api/asset/host';
  import { dataColor } from '@/utils';
  import { tagColor } from '@/views/asset/host-list/types/const';
  import { useRowSelection } from '@/hooks/table';
  import columns from '../types/table.columns';
  import { computed } from 'vue';

  const rowSelection = useRowSelection();

  const props = defineProps<{
    selectedKeys: Array<number>;
    hostList: Array<HostQueryResponse>;
    emptyMessage: string;
  }>();

  const emits = defineEmits(['update:selectedKeys']);

  // 选中数据
  const selectedKeysValue = computed<Array<number>>({
    get() {
      return props.selectedKeys;
    },
    set(e) {
      if (e) {
        emits('update:selectedKeys', e);
      } else {
        emits('update:selectedKeys', []);
      }
    }
  });

  // 点击行
  const clickRow = ({ id }: TableData) => {
    const result = [...props.selectedKeys];
    const index = result.indexOf(id);
    if (index === -1) {
      result.push(id);
    } else {
      result.splice(index, 1);
    }
    emits('update:selectedKeys', result);
  };

</script>

<style lang="less" scoped>

</style>
