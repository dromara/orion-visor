<template>
  <a-modal v-model:visible="visible"
           title-align="start"
           :title="title"
           :width="960"
           :top="80"
           :body-style="{ padding: '0 8px' }"
           :align-center="false"
           :draggable="true"
           :mask-closable="false"
           :unmount-on-close="true"
           :footer="false"
           @close="handleClose">
    <a-card class="general-card table-card">
      <template #title>
        <!-- 左侧操作 -->
        <div class="table-left-bar-handle">
        </div>
        <!-- 右侧操作 -->
        <div class="table-right-bar-handle">
          <a-space>
            <a-input-search v-model="form.searchValue"
                            placeholder="搜索值"
                            @search="() => fetchTableData()"
                            @keyup.enter="() => fetchTableData()" />
          </a-space>
        </div>
      </template>
      <!-- table -->
      <a-table row-key="id"
               ref="tableRef"
               :loading="loading"
               :columns="columns"
               :data="tableRenderData"
               :pagination="pagination"
               :bordered="false"
               @page-change="(page: number) => fetchTableData(page, pagination.pageSize)"
               @page-size-change="(size: number) => fetchTableData(1, size)">
        <!-- 修改前 -->
        <template #beforeValue="{ record }">
          <span class="copy-left"
                title="复制"
                @click="copy(record.beforeValue, '已复制')">
            <icon-copy />
          </span>
          <span>{{ record.beforeValue }}</span>
        </template>
        <!-- 修改后 -->
        <template #afterValue="{ record }">
          <span class="copy-left"
                title="复制"
                @click="copy(record.afterValue, '已复制')">
            <icon-copy />
          </span>
          <span>{{ record.afterValue }}</span>
        </template>
        <!-- 操作 -->
        <template #handle="{ record }">
          <div class="table-handle-wrapper">
            <!-- 回滚 -->
            <a-popconfirm content="确认要回滚到当前记录?"
                          position="left"
                          type="warning"
                          @ok="rollbackValue(record)">
              <a-button type="text" size="mini">
                回滚
              </a-button>
            </a-popconfirm>
          </div>
        </template>
      </a-table>
    </a-card>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'historyValueModal'
  };
</script>

<script lang="ts" setup>
  import type { HistoryValueQueryRequest, HistoryValueQueryResponse } from '@/api/meta/history-value';
  import { reactive, ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import { getHistoryValuePage } from '@/api/meta/history-value';
  import { useTablePagination } from '@/hooks/table';
  import { copy } from '@/hooks/copy';
  import columns from './table.columns';
  import { Message } from '@arco-design/web-vue';

  const props = defineProps<{
    type: string;
    rollback: Function;
  }>();
  const emits = defineEmits(['updated']);

  const pagination = useTablePagination();
  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const title = ref<string>();
  const tableRenderData = ref<HistoryValueQueryResponse[]>([]);
  const form = reactive<HistoryValueQueryRequest>({
    searchValue: undefined,
    relId: undefined,
  });

  // 打开
  const open = (id: number, value: string) => {
    form.relId = id;
    title.value = `历史值 - ${value}`;
    setVisible(true);
    fetchTableData();
  };

  defineExpose({ open });

  // 回滚
  const rollbackValue = async (record: HistoryValueQueryResponse) => {
    try {
      setLoading(true);
      await (props.rollback as Function)(form.relId, record.id);
      Message.success('回滚成功');
      emits('updated');
      handleClear();
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 加载数据
  const doFetchTableData = async (request: HistoryValueQueryRequest) => {
    try {
      setLoading(true);
      const { data } = await getHistoryValuePage({
        ...request,
        type: props.type,
        ...form
      });
      tableRenderData.value = data.rows;
      pagination.total = data.total;
      pagination.current = request.page;
      pagination.pageSize = request.limit;
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 切换页码
  const fetchTableData = (page = 1, limit = pagination.pageSize) => {
    doFetchTableData({ page, limit });
  };

  // 关闭回调
  const handleClose = () => {
    handleClear();
  };

  // 清空
  const handleClear = () => {
    setLoading(false);
    setVisible(false);
  };

</script>

<style lang="less" scoped>
</style>
