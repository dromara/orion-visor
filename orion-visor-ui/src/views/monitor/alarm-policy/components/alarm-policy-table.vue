<template>
  <!-- 搜索 -->
  <a-card class="general-card table-search-card">
    <query-header :model="formModel"
                  label-align="left"
                  @submit="fetchTableData"
                  @reset="fetchTableData"
                  @keyup.enter="() => fetchTableData()">
      <!-- id -->
      <a-form-item field="id" label="id">
        <a-input-number v-model="formModel.id"
                        placeholder="请输入id"
                        hide-button
                        allow-clear />
      </a-form-item>
      <!-- 策略名称 -->
      <a-form-item field="name" label="策略名称">
        <a-input v-model="formModel.name"
                 placeholder="请输入策略名称"
                 allow-clear />
      </a-form-item>
      <!-- 策略描述 -->
      <a-form-item field="description" label="策略描述">
        <a-input v-model="formModel.description"
                 placeholder="请输入策略描述"
                 allow-clear />
      </a-form-item>
    </query-header>
  </a-card>
  <!-- 表格 -->
  <a-card class="general-card table-card">
    <template #title>
      <!-- 左侧操作 -->
      <div class="table-left-bar-handle">
        <!-- 标题 -->
        <div class="table-title">
          告警策略列表
        </div>
      </div>
      <!-- 右侧操作 -->
      <div class="table-right-bar-handle">
        <a-space>
          <!-- 新增 -->
          <a-button v-permission="['monitor:alarm-policy:create']"
                    type="primary"
                    @click="emits('openAdd')">
            新增
            <template #icon>
              <icon-plus />
            </template>
          </a-button>
          <!-- 调整 -->
          <table-adjust :columns="columns"
                        :columns-hook="columnsHook"
                        :query-order="queryOrder"
                        @query="fetchTableData" />
        </a-space>
      </div>
    </template>
    <!-- table -->
    <a-table row-key="id"
             ref="tableRef"
             class="table-resize"
             :loading="loading"
             :columns="tableColumns"
             :data="tableRenderData"
             :pagination="pagination"
             :bordered="false"
             :column-resizable="true"
             @page-change="(page: number) => fetchTableData(page, pagination.pageSize)"
             @page-size-change="(size: number) => fetchTableData(1, size)">
      <!-- 策略名称 -->
      <template #name="{ record }">
        <span class="ml4 span-blue pointer" @click="openRules(record)">
          {{ record.name }}
        </span>
      </template>
      <!-- 规则数量 -->
      <template #ruleCount="{ record }">
        <b class="ml4 span-blue pointer" @click="openRules(record)">{{ record.ruleCount }} 个</b>
      </template>
      <!-- 主机数量 -->
      <template #hostCount="{ record }">
        <b class="ml4 span-blue">{{ record.hostCount }} 个</b>
      </template>
      <!-- 今日触发次数 -->
      <template #todayTriggerCount="{ record }">
        <b class="ml4" :class="[ (record.todayTriggerCount || 0) > 0 ? 'span-red' : '' ]">{{ record.todayTriggerCount || 0 }} 次</b>
      </template>
      <!-- 7日触发次数 -->
      <template #weekTriggerCount="{ record }">
        <b class="ml4" :class="[ (record.weekTriggerCount || 0) > 0 ? 'span-red' : '' ]">{{ record.weekTriggerCount || 0 }} 次</b>
      </template>
      <!-- 操作 -->
      <template #handle="{ record }">
        <div class="table-handle-wrapper">
          <!-- 修改 -->
          <a-button v-permission="['monitor:alarm-policy:update']"
                    type="text"
                    size="mini"
                    @click="emits('openUpdate', record)">
            修改
          </a-button>
          <!-- 告警规则 -->
          <a-button v-permission="['monitor:alarm-policy:update']"
                    type="text"
                    size="mini"
                    @click="openRules(record)">
            告警规则
          </a-button>
          <!-- 复制策略 -->
          <a-button v-permission="['monitor:alarm-policy:create']"
                    type="text"
                    size="mini"
                    @click="emits('openCopy', record)">
            复制策略
          </a-button>
          <!-- 删除 -->
          <a-popconfirm content="确认删除这条记录吗?"
                        position="left"
                        type="warning"
                        @ok="deleteRow(record)">
            <a-button v-permission="['monitor:alarm-policy:delete']"
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
</template>

<script lang="ts">
  export default {
    name: 'alarmPolicyTable'
  };
</script>

<script lang="ts" setup>
  import type { AlarmPolicyQueryRequest, AlarmPolicyQueryResponse } from '@/api/monitor/alarm-policy';
  import { reactive, ref, onMounted } from 'vue';
  import { deleteAlarmPolicy, getAlarmPolicyPage } from '@/api/monitor/alarm-policy';
  import { Message } from '@arco-design/web-vue';
  import useLoading from '@/hooks/loading';
  import columns from '../types/table.columns';
  import { useRouter } from 'vue-router';
  import { AlarmPolicyType, TableName } from '../types/const';
  import { useTablePagination, useTableColumns } from '@/hooks/table';
  import { useQueryOrder, ASC } from '@/hooks/query-order';
  import TableAdjust from '@/components/app/table-adjust/index.vue';

  const emits = defineEmits(['openAdd', 'openUpdate', 'openCopy']);

  const router = useRouter();
  const pagination = useTablePagination();
  const { loading, setLoading } = useLoading();
  const queryOrder = useQueryOrder(TableName, ASC);
  const { tableColumns, columnsHook } = useTableColumns(TableName, columns);

  const tableRenderData = ref<Array<AlarmPolicyQueryResponse>>([]);
  const formModel = reactive<AlarmPolicyQueryRequest>({
    id: undefined,
    type: AlarmPolicyType.HOST,
    name: undefined,
    description: undefined,
  });

  // 打开规则页面
  const openRules = (record: AlarmPolicyQueryResponse) => {
    router.push({
      name: 'alarmRule',
      query: {
        id: record.id,
        name: record.name,
      },
    });
  };

  // 删除当前行
  const deleteRow = async (record: AlarmPolicyQueryResponse) => {
    try {
      setLoading(true);
      // 调用删除接口
      await deleteAlarmPolicy(record.id);
      Message.success('删除成功');
      // 重新加载
      reload();
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 重新加载
  const reload = () => {
    // 重新加载数据
    fetchTableData();
  };

  defineExpose({ reload });

  // 加载数据
  const doFetchTableData = async (request: AlarmPolicyQueryRequest) => {
    try {
      setLoading(true);
      const { data } = await getAlarmPolicyPage(queryOrder.markOrderly(request));
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
  const fetchTableData = (page = 1, limit = pagination.pageSize, form = formModel) => {
    doFetchTableData({ page, limit, ...form });
  };

  onMounted(() => {
    fetchTableData();
  });

</script>

<style lang="less" scoped>

</style>
