<template>
  <!-- 搜索 -->
  <a-card class="general-card table-search-card">
    <query-header :model="formModel"
                  label-align="left"
                  :itemOptions="{ 4: { span: 2 } }"
                  @submit="fetchTableData"
                  @reset="fetchTableData"
                  @keyup.enter="() => fetchTableData()">
      <!-- 操作用户 -->
      <a-form-item field="userId" label="操作用户">
        <user-selector v-model="formModel.userId"
                       placeholder="请选择用户"
                       allow-clear />
      </a-form-item>
      <!-- 操作主机 -->
      <a-form-item field="hostId" label="操作主机">
        <host-selector v-model="formModel.hostId"
                       placeholder="请选择主机"
                       allow-clear />
      </a-form-item>
      <!-- 操作类型 -->
      <a-form-item field="type" label="操作类型">
        <a-select v-model="formModel.type"
                  placeholder="请选择类型"
                  :options="toOptions(sftpOperatorTypeKey)"
                  allow-clear />
      </a-form-item>
      <!-- 执行结果 -->
      <a-form-item field="result" label="执行结果">
        <a-select v-model="formModel.result"
                  placeholder="请选择执行结果"
                  :options="toOptions(sftpOperatorResultKey)"
                  allow-clear />
      </a-form-item>
      <!-- 开始时间 -->
      <a-form-item field="startTimeRange" label="开始时间">
        <a-range-picker v-model="formModel.startTimeRange"
                        :time-picker-props="{ defaultValue: ['00:00:00', '23:59:59'] }"
                        show-time
                        format="YYYY-MM-DD HH:mm:ss" />
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
          SFTP 操作日志
        </div>
      </div>
      <!-- 右侧操作 -->
      <div class="table-right-bar-handle">
        <a-space>
          <!-- 删除 -->
          <a-popconfirm :content="`确认删除选中的 ${selectedKeys.length} 条记录吗?`"
                        position="br"
                        type="warning"
                        @ok="deleteSelectRows">
            <a-button v-permission="['infra:operator-log:delete', 'asset:host-sftp-log:management:delete']"
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
    <!-- table -->
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
      <!-- 操作用户 -->
      <template #username="{ record }">
        {{ record.username }}
      </template>
      <!-- 操作主机 -->
      <template #hostName="{ record }">
        <span class="table-cell-value" :title="record.hostName">
          {{ record.hostName }}
        </span>
        <br>
        <span class="table-cell-sub-value text-copy"
              :title="record.hostAddress"
              @click="copy(record.hostAddress)">
          {{ record.hostAddress }}
        </span>
      </template>
      <!-- 操作类型 -->
      <template #type="{ record }">
        {{ getDictValue(sftpOperatorTypeKey, record.type) }}
      </template>
      <!-- 操作文件 -->
      <template #paths="{ record }">
        <div class="paths-wrapper">
          <span v-for="path in record.paths"
                class="path-wrapper text-ellipsis text-copy"
                :title="path"
                @click="copy(path)">
            {{ path }}
          </span>
          <!-- 移动目标路径 -->
          <span class="table-cell-sub-value" v-if="SftpOperatorType.SFTP_MOVE === record.type">
            移动到 {{ record.extra?.target }}
          </span>
          <!-- 提权信息 -->
          <span class="table-cell-sub-value" v-if="SftpOperatorType.SFTP_CHMOD === record.type">
            提权 {{ record.extra?.mod }} {{ permission10toString(record.extra?.mod as number) }}
          </span>
        </div>
      </template>
      <!-- 执行结果 -->
      <template #result="{ record }">
        <a-tag :color="getDictValue(sftpOperatorResultKey, record.result, 'color')">
          {{ getDictValue(sftpOperatorResultKey, record.result) }}
        </a-tag>
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
          <!-- 删除 -->
          <a-popconfirm content="确认删除这条记录吗?"
                        position="left"
                        type="warning"
                        @ok="deleteRow(record)">
            <a-button v-permission="['infra:operator-log:delete', 'asset:host-sftp-log:management:delete']"
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
    name: 'sftpLogTable'
  };
</script>

<script lang="ts" setup>
  import type { HostSftpLogQueryRequest, HostSftpLogQueryResponse } from '@/api/asset/host-sftp-log';
  import { reactive, ref, onMounted } from 'vue';
  import { getHostSftpLogPage, deleteHostSftpLog } from '@/api/asset/host-sftp-log';
  import { sftpOperatorTypeKey, sftpOperatorResultKey, SftpOperatorType } from '../types/const';
  import { usePagination, useRowSelection } from '@/types/table';
  import { useDictStore } from '@/store';
  import { Message } from '@arco-design/web-vue';
  import columns from '../types/table.columns';
  import useLoading from '@/hooks/loading';
  import useCopy from '@/hooks/copy';
  import { permission10toString } from '@/utils/file';
  import UserSelector from '@/components/user/user/selector/index.vue';
  import HostSelector from '@/components/asset/host/selector/index.vue';

  const tableRenderData = ref<HostSftpLogQueryResponse[]>([]);
  const selectedKeys = ref<number[]>([]);

  const pagination = usePagination();
  const rowSelection = useRowSelection();
  const { loading, setLoading } = useLoading();
  const { toOptions, getDictValue } = useDictStore();
  const { copy } = useCopy();

  const formModel = reactive<HostSftpLogQueryRequest>({
    userId: undefined,
    hostId: undefined,
    type: undefined,
    result: undefined,
    startTimeRange: undefined,
  });

  // 加载数据
  const doFetchTableData = async (request: HostSftpLogQueryRequest) => {
    try {
      setLoading(true);
      const { data } = await getHostSftpLogPage(request);
      tableRenderData.value = data.rows;
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
  const fetchTableData = (page = 1, limit = pagination.pageSize, form = formModel) => {
    doFetchTableData({ page, limit, ...form });
  };

  // 删除选中行
  const deleteSelectRows = async () => {
    try {
      setLoading(true);
      // 调用删除接口
      await deleteHostSftpLog(selectedKeys.value);
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
      await deleteHostSftpLog([id]);
      Message.success('删除成功');
      selectedKeys.value = [];
      // 重新加载数据
      fetchTableData();
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  onMounted(() => {
    fetchTableData();
  });

</script>

<style lang="less" scoped>
  .paths-wrapper {
    display: flex;
    flex-direction: column;

    .path-wrapper {
      display: block;
      padding: 2px;
    }
  }
</style>
