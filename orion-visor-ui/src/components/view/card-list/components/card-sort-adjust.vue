<template>
  <!-- 调整排序 -->
  <a-popover class="table-adjust-popover"
             position="br"
             trigger="click"
             :content-style="{ padding: 0 }"
             @popup-visible-change="visibleChanged">
    <!-- 触发按钮 -->
    <a-button class="icon-button card-header-icon-button" title="调整排序">
      <icon-sort-ascending />
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
      <!-- 排序信息 -->
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
</template>

<script lang="ts">
  export default {
    name: 'cardSortAdjust'
  };
</script>

<script lang="ts" setup>
  import type { QueryOrderData } from '@/hooks/query-order';
  import { ref } from 'vue';
  import { ASC, DESC } from '@/hooks/query-order';

  const emits = defineEmits(['query']);

  const props = defineProps<{
    queryOrder: QueryOrderData;
  }>();

  const orderValue = ref<number>(ASC);

  // 排序显示切换
  const visibleChanged = (show: boolean) => {
    if (show) {
      orderValue.value = props.queryOrder.order.value;
    }
  };

  // 修改排序
  const sortChanged = () => {
    props.queryOrder.saveConfig(orderValue.value);
    emits('query');
  };

</script>

<style lang="less" scoped>
</style>
