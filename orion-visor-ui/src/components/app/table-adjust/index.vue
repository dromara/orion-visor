<template>
  <a-button-group v-if="columnsHook || queryOrder">
    <!-- 调整列 -->
    <a-popover v-if="columnsHook && columns"
               class="table-adjust-popover"
               position="br"
               trigger="click"
               :content-style="{ padding: 0 }"
               @popup-visible-change="columnsVisibleChanged">
      <!-- 触发按钮 -->
      <a-button title="调整列">
        <template #icon>
          <icon-list />
        </template>
      </a-button>
      <!-- 调整内容 -->
      <template #content>
        <!-- 顶部按钮 -->
        <div class="table-adjust-popover-header">
          <span class="table-adjust-title">
            表格展示列
          </span>
          <a-button type="text"
                    size="mini"
                    @click="resetColumns">
            重置
          </a-button>
        </div>
        <!-- 分隔符 -->
        <a-divider :margin="4" />
        <!-- 列信息 -->
        <a-checkbox-group v-model="columnsValue"
                          direction="vertical"
                          @change="columnsChanged">
          <a-checkbox v-for="column in columns"
                      :key="column.dataIndex || column.slotName"
                      :value="column.dataIndex || column.slotName">
            {{ column.title }}
          </a-checkbox>
        </a-checkbox-group>
      </template>
    </a-popover>
    <!-- 调整排序 -->
    <a-popover v-if="queryOrder"
               class="table-adjust-popover"
               position="br"
               trigger="click"
               :content-style="{ padding: 0 }"
               @popup-visible-change="sortVisibleChanged">
      <!-- 触发按钮 -->
      <a-button title="调整排序">
        <template #icon>
          <icon-sort-ascending />
        </template>
      </a-button>
      <!-- 调整内容 -->
      <template #content>
        <!-- 顶部按钮 -->
        <div class="table-adjust-popover-header">
          <span class="table-adjust-title">
            表格查询排序
          </span>
        </div>
        <!-- 分隔符 -->
        <a-divider :margin="4" />
        <!-- 列信息 -->
        <a-radio-group v-model="orderValue"
                       direction="vertical"
                       @change="sortChanged">
          <a-radio :value="ASC">
            升序排序
          </a-radio>
          <a-radio :value="DESC">
            降序排序
          </a-radio>
        </a-radio-group>
      </template>
    </a-popover>
  </a-button-group>
</template>

<script lang="ts">
  export default {
    name: 'tableAdjust'
  };
</script>

<script lang="ts" setup>
  import type { TableColumnData } from '@arco-design/web-vue';
  import type { TableColumnsHook } from '@/hooks/table';
  import type { QueryOrderData } from '@/hooks/query-order';
  import { ref } from 'vue';
  import { ASC, DESC } from '@/hooks/query-order';

  const emits = defineEmits(['query']);

  const props = defineProps<Partial<{
    columns: Array<TableColumnData>;
    columnsHook: TableColumnsHook;
    queryOrder: QueryOrderData;
  }>>();

  const columnsValue = ref<Array<string>>([]);
  const orderValue = ref<number>(ASC);

  // 列显示切换
  const columnsVisibleChanged = (show: boolean) => {
    if (show && props.columnsHook) {
      columnsValue.value = props.columnsHook.getConfig();
    }
  };

  // 排序显示切换
  const sortVisibleChanged = (show: boolean) => {
    if (show && props.queryOrder) {
      orderValue.value = props.queryOrder.order.value;
    }
  };

  // 重置列
  const resetColumns = () => {
    if (props.columnsHook) {
      props.columnsHook.saveConfig(undefined);
      columnsValue.value = props.columnsHook.getConfig();
    }
  };

  // 修改列
  const columnsChanged = () => {
    props.columnsHook?.saveConfig(columnsValue.value);
  };

  // 修改排序
  const sortChanged = () => {
    props.queryOrder?.saveConfig(orderValue.value);
    emits('query');
  };

</script>

<style lang="less" scoped>
</style>
