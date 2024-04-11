<template>
  <!-- 查询头 -->
  <a-card class="general-card table-search-card">
    <!-- 查询头组件 -->
    <operator-log-query-header @submit="(e) => fetchTableData(undefined, undefined, e)" />
  </a-card>
  <!-- 表格 -->
  <a-card class="general-card table-card">
    <template #title>
      <!-- 左侧操作 -->
      <div class="table-left-bar-handle">
        <!-- 标题 -->
        <div class="table-title">
          操作日志
        </div>
      </div>
      <!-- 右侧操作 -->
      <div class="table-right-bar-handle">
        <a-space>
          <!-- 清空 -->
          <a-button v-permission="['infra:operator-log:management:clear']"
                    status="danger"
                    @click="openClear">
            清空
            <template #icon>
              <icon-close />
            </template>
          </a-button>
          <!-- 删除 -->
          <a-popconfirm :content="`确认删除选中的 ${selectedKeys.length} 条记录吗?`"
                        position="br"
                        type="warning"
                        @ok="deleteSelectRows">
            <a-button v-permission="['infra:operator-log:delete']"
                      type="secondary"
                      status="danger"
                      :disabled="selectedKeys.length === 0">
              删除
              <template #icon>
                <icon-delete />
              </template>
            </a-button>
          </a-popconfirm>
        </a-space>
      </div>
    </template>
    <!-- 表格 -->
    <a-table row-key="id"
             ref="tableRef"
             :loading="loading"
             v-model:selected-keys="selectedKeys"
             :row-selection="rowSelection"
             :columns="columns"
             :data="tableRenderData"
             :pagination="pagination"
             @page-change="(page) => fetchTableData(page, pagination.pageSize)"
             @page-size-change="(size) => fetchTableData(1, size)"
             :bordered="false">
      <!-- 操作模块 -->
      <template #module="{ record }">
        {{ getDictValue(operatorLogModuleKey, record.module) }}
        <icon-oblique-line />
        {{ getDictValue(operatorLogTypeKey, record.type) }}
      </template>
      <!-- 风险等级 -->
      <template #riskLevel="{ record }">
        <a-tag :color="getDictValue(operatorRiskLevelKey, record.riskLevel, 'color')">
          {{ getDictValue(operatorRiskLevelKey, record.riskLevel) }}
        </a-tag>
      </template>
      <!-- 执行结果 -->
      <template #result="{ record }">
        <a-tag :color="getDictValue(operatorLogResultKey, record.result, 'color')">
          {{ getDictValue(operatorLogResultKey, record.result) }}
        </a-tag>
      </template>
      <!-- 操作日志 -->
      <template #originLogInfo="{ record }">
        <icon-copy class="copy-left" @click="copy(record.originLogInfo, '已复制')" />
        <span v-html="replaceHtmlTag(record.logInfo)" />
      </template>
      <!-- 留痕地址 -->
      <template #address="{ record }">
        <span class="table-cell-value" :title="record.location">
          {{ record.location }}
        </span>
        <br>
        <span class="table-cell-sub-value text-copy"
              :title="record.address"
              @click="copy(record.address)">
          {{ record.address }}
        </span>
      </template>
      <!-- 操作 -->
      <template #handle="{ record }">
        <div class="table-handle-wrapper">
          <!-- 详情 -->
          <a-button type="text"
                    size="mini"
                    @click="openLogDetail(record)">
            详情
          </a-button>
          <!-- 删除 -->
          <a-popconfirm content="确认删除这条记录吗?"
                        position="left"
                        type="warning"
                        @ok="deleteRow(record)">
            <a-button v-permission="['infra:operator-log:delete']"
                      type="text"
                      size="mini"
                      status="danger">
              删除
            </a-button>
          </a-popconfirm>
        </div>
      </template>
    </a-table>
  </a-card>
  <!-- 清理模态框 -->
  <operator-log-clear-modal ref="clearModal"
                            @clear="fetchTableData" />
  <!-- json 查看器模态框 -->
  <json-editor-modal ref="jsonView" />
</template>

<script lang="ts">
  export default {
    name: 'operatorLogTable'
  };
</script>

<script lang="ts" setup>
  import type { OperatorLogQueryRequest, OperatorLogQueryResponse } from '@/api/user/operator-log';
  import { ref, onMounted } from 'vue';
  import { operatorLogModuleKey, operatorLogTypeKey, operatorRiskLevelKey, operatorLogResultKey, getLogDetail } from '../types/const';
  import columns from '../types/table.columns';
  import { copy } from '@/hooks/copy';
  import useLoading from '@/hooks/loading';
  import { usePagination, useRowSelection } from '@/types/table';
  import { useDictStore } from '@/store';
  import { getOperatorLogPage, deleteOperatorLog } from '@/api/user/operator-log';
  import { replaceHtmlTag, clearHtmlTag } from '@/utils';
  import { Message } from '@arco-design/web-vue';
  import OperatorLogQueryHeader from './operator-log-query-header.vue';
  import OperatorLogClearModal from './operator-log-clear-modal.vue';
  import JsonEditorModal from '@/components/view/json-editor/modal/index.vue';

  const pagination = usePagination();
  const rowSelection = useRowSelection();
  const { loading, setLoading } = useLoading();
  const { getDictValue } = useDictStore();

  const clearModal = ref();
  const jsonView = ref();
  const tableRenderData = ref<OperatorLogQueryResponse[]>([]);
  const selectedKeys = ref<number[]>([]);

  // 查看详情
  const openLogDetail = (record: OperatorLogQueryResponse) => {
    jsonView.value.open(getLogDetail(record));
  };

  // 打开清空
  const openClear = () => {
    clearModal.value?.open();
  };

  // 删除选中行
  const deleteSelectRows = async () => {
    try {
      setLoading(true);
      // 调用删除接口
      await deleteOperatorLog(selectedKeys.value);
      Message.success(`成功删除 ${selectedKeys.value.length} 条数据`);
      selectedKeys.value = [];
      // 重新加载数据
      fetchTableData();
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 删除当前行
  const deleteRow = async ({ id }: {
    id: number
  }) => {
    try {
      setLoading(true);
      // 调用删除接口
      await deleteOperatorLog([id]);
      Message.success('删除成功');
      selectedKeys.value = [];
      // 重新加载数据
      fetchTableData();
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 加载数据
  const doFetchTableData = async (request: OperatorLogQueryRequest) => {
    try {
      setLoading(true);
      const { data } = await getOperatorLogPage(request);
      tableRenderData.value = data.rows.map(s => {
        return { ...s, originLogInfo: clearHtmlTag(s.logInfo) };
      });
      pagination.total = data.total;
      pagination.current = request.page;
      pagination.pageSize = request.limit;
      selectedKeys.value = [];
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 切换页码
  const fetchTableData = (page = 1, limit = pagination.pageSize, form = {}) => {
    doFetchTableData({ page, limit, ...form });
  };

  // 初始化
  onMounted(() => {
    fetchTableData();
  });

</script>

<style lang="less" scoped>
</style>
