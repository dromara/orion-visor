<template>
  <!-- 搜索 -->
  <a-card class="general-card table-search-card">
    <query-header :model="formModel"
                  label-align="left"
                  @submit="fetchTableData"
                  @reset="fetchTableData"
                  @keyup.enter="() => fetchTableData()">
      <!-- 角色名称 -->
      <a-form-item field="name" label="角色名称">
        <a-input v-model="formModel.name"
                 placeholder="请输入角色名称"
                 allow-clear />
      </a-form-item>
      <!-- 角色编码 -->
      <a-form-item field="code" label="角色编码">
        <a-input v-model="formModel.code"
                 placeholder="请输入角色编码"
                 allow-clear />
      </a-form-item>
      <!-- 角色状态 -->
      <a-form-item field="status" label="角色状态">
        <a-select v-model="formModel.status"
                  placeholder="请选择角色状态"
                  :options="toOptions(roleStatusKey)"
                  allow-clear />
      </a-form-item>
      <!-- 角色描述 -->
      <a-form-item field="description" label="角色描述">
        <a-input v-model="formModel.description"
                 placeholder="请输入角色描述"
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
          角色列表
        </div>
      </div>
      <!-- 右侧操作 -->
      <div class="table-right-bar-handle">
        <a-space>
          <!-- 新增 -->
          <a-button type="primary"
                    v-permission="['infra:system-role:create']"
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
      <!-- 编码 -->
      <template #code="{ record }">
        <a-tag>{{ record.code }}</a-tag>
      </template>
      <!-- 状态 -->
      <template #status="{ record }">
        <!-- 有修改权限 -->
        <a-switch v-if="hasPermission('infra:system-role:update-status')"
                  type="round"
                  v-model="record.status"
                  :disabled="record.code === AdminRoleCode"
                  :checked-text="getDictValue(roleStatusKey, RoleStatus.ENABLED)"
                  :unchecked-text="getDictValue(roleStatusKey, RoleStatus.DISABLED)"
                  :checked-value="RoleStatus.ENABLED"
                  :unchecked-value="RoleStatus.DISABLED"
                  :before-change="(s) => updateStatus(record.id, s as number)" />
        <!-- 无修改权限 -->
        <span v-else>
          <span class="circle" :style="{
            background: getDictValue(roleStatusKey, record.status, 'color')
          }" />
          {{ getDictValue(roleStatusKey, record.status) }}
        </span>
      </template>
      <!-- 操作 -->
      <template #handle="{ record }">
        <div class="table-handle-wrapper">
          <!-- 分配菜单 -->
          <a-button v-permission="['infra:system-role:grant-menu']"
                    :disabled="record.code === AdminRoleCode"
                    type="text"
                    size="mini"
                    @click="emits('openGrant', record)">
            分配菜单
          </a-button>
          <!-- 修改 -->
          <a-button v-permission="['infra:system-role:update']"
                    type="text"
                    size="mini"
                    @click="emits('openUpdate', record)">
            修改
          </a-button>
          <!-- 删除 -->
          <a-popconfirm content="确认删除这条记录吗?"
                        position="left"
                        type="warning"
                        @ok="deleteRow(record)">
            <a-button v-permission="['infra:system-role:delete']"
                      :disabled="record.code === AdminRoleCode"
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
    name: 'roleTable'
  };
</script>

<script lang="ts" setup>
  import type { RoleQueryRequest, RoleQueryResponse } from '@/api/user/role';
  import { reactive, ref, onMounted } from 'vue';
  import { deleteRole, getRolePage, updateRoleStatus } from '@/api/user/role';
  import { Message } from '@arco-design/web-vue';
  import useLoading from '@/hooks/loading';
  import columns from '../types/table.columns';
  import { TableName, RoleStatus, roleStatusKey } from '../types/const';
  import { useTablePagination, useTableColumns } from '@/hooks/table';
  import usePermission from '@/hooks/permission';
  import { useCacheStore, useDictStore } from '@/store';
  import { AdminRoleCode } from '@/types/const';
  import { useQueryOrder, ASC } from '@/hooks/query-order';
  import TableAdjust from '@/components/app/table-adjust/index.vue';

  const emits = defineEmits(['openAdd', 'openUpdate', 'openGrant']);

  const cacheStore = useCacheStore();
  const pagination = useTablePagination();
  const queryOrder = useQueryOrder(TableName, ASC);
  const { tableColumns, columnsHook } = useTableColumns(TableName, columns);
  const { hasPermission } = usePermission();
  const { loading, setLoading } = useLoading();
  const { toOptions, getDictValue } = useDictStore();

  const tableRenderData = ref<Array<RoleQueryResponse>>([]);
  const formModel = reactive<RoleQueryRequest>({
    id: undefined,
    name: undefined,
    code: undefined,
    status: undefined,
    description: undefined,
  });

  // 更新状态
  const updateStatus = (id: number, status: number) => {
    return updateRoleStatus({
      id, status
    }).then(() => {
      Message.success('已' + getDictValue(roleStatusKey, status, 'label'));
    });
  };

  // 删除当前行
  const deleteRow = async (record: RoleQueryResponse) => {
    try {
      setLoading(true);
      // 调用删除接口
      await deleteRole(record.id);
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
    // 加载数据
    fetchTableData();
    // 清空缓存
    cacheStore.reset('roles');
  };

  defineExpose({ reload });

  // 加载数据
  const doFetchTableData = async (request: RoleQueryRequest) => {
    try {
      setLoading(true);
      const { data } = await getRolePage(queryOrder.markOrderly(request));
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
