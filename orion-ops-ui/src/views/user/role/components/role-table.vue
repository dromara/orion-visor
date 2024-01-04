<template>
  <!-- 搜索 -->
  <a-card class="general-card table-search-card">
    <a-query-header :model="formModel"
                    label-align="left"
                    @submit="fetchTableData"
                    @reset="fetchTableData"
                    @keyup.enter="() => fetchTableData()">
      <!-- 角色名称 -->
      <a-form-item field="name" label="角色名称" label-col-flex="50px">
        <a-input v-model="formModel.name" placeholder="请输入角色名称" allow-clear />
      </a-form-item>
      <!-- 角色编码 -->
      <a-form-item field="code" label="角色编码" label-col-flex="50px">
        <a-input v-model="formModel.code" placeholder="请输入角色编码" allow-clear />
      </a-form-item>
      <!-- 角色状态 -->
      <a-form-item field="status" label="角色状态" label-col-flex="50px">
        <a-select v-model="formModel.status"
                  placeholder="请选择角色状态"
                  :options="toOptions(roleStatusKey)"
                  allow-clear />
      </a-form-item>
    </a-query-header>
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
        </a-space>
      </div>
    </template>
    <!-- table -->
    <a-table row-key="id"
             class="table-wrapper-8"
             ref="tableRef"
             label-align="left"
             :loading="loading"
             :columns="columns"
             :data="tableRenderData"
             :pagination="pagination"
             @page-change="(page) => fetchTableData(page, pagination.pageSize)"
             @page-size-change="(size) => fetchTableData(1, size)"
             :bordered="false">
      <!-- 编码 -->
      <template #code="{ record }">
        <a-tag>{{ record.code }}</a-tag>
      </template>
      <!-- 状态 -->
      <template #status="{ record }">
        <span class="circle" :style="{
          background: getDictValue(roleStatusKey, record.status, 'color')
        }" />
        {{ getDictValue(roleStatusKey, record.status) }}
      </template>
      <!-- 操作 -->
      <template #handle="{ record }">
        <div class="table-handle-wrapper">
          <!-- 修改状态 -->
          <a-popconfirm :content="`确定要${toggleDictValue(roleStatusKey, record.status, 'label')}当前角色吗?`"
                        position="left"
                        type="warning"
                        @ok="toggleRoleStatus(record)">
            <a-button v-permission="['infra:system-role:delete']"
                      :disabled="record.code === AdminRoleCode"
                      :status="toggleDictValue(roleStatusKey, record.status, 'status')"
                      type="text"
                      size="mini">
              {{ toggleDictValue(roleStatusKey, record.status, 'label') }}
            </a-button>
          </a-popconfirm>
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
    name: 'userRoleTable'
  };
</script>

<script lang="ts" setup>
  import type { RoleQueryRequest, RoleQueryResponse } from '@/api/user/role';
  import { reactive, ref, onMounted } from 'vue';
  import { deleteRole, getRolePage, updateRoleStatus } from '@/api/user/role';
  import { Message } from '@arco-design/web-vue';
  import useLoading from '@/hooks/loading';
  import columns from '../types/table.columns';
  import { roleStatusKey } from '../types/const';
  import { usePagination } from '@/types/table';
  import { useDictStore } from '@/store';
  import { AdminRoleCode } from '@/types/const';

  const emits = defineEmits(['openAdd', 'openUpdate', 'openGrant']);

  const tableRenderData = ref<RoleQueryResponse[]>([]);

  const pagination = usePagination();
  const { loading, setLoading } = useLoading();
  const { toOptions, getDictValue, toggleDictValue, toggleDict } = useDictStore();

  const formModel = reactive<RoleQueryRequest>({
    id: undefined,
    name: undefined,
    code: undefined,
    status: undefined,
  });

  // 修改状态
  const toggleRoleStatus = async (record: any) => {
    try {
      setLoading(true);
      const toggleStatus = toggleDict(roleStatusKey, record.status);
      // 调用修改接口
      await updateRoleStatus({
        id: record.id,
        status: toggleStatus.value as number
      });
      Message.success(`${toggleStatus.label}成功`);
      // 修改行状态
      record.status = toggleStatus.value;
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
      await deleteRole(id);
      Message.success('删除成功');
      // 重新加载数据
      fetchTableData();
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 添加后回调
  const addedCallback = () => {
    fetchTableData();
  };

  // 更新后回调
  const updatedCallback = () => {
    fetchTableData();
  };

  defineExpose({
    addedCallback, updatedCallback
  });

  // 加载数据
  const doFetchTableData = async (request: RoleQueryRequest) => {
    try {
      setLoading(true);
      const { data } = await getRolePage(request);
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
