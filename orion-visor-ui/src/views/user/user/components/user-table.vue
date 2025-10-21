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
                        allow-clear
                        hide-button />
      </a-form-item>
      <!-- 用户名 -->
      <a-form-item field="username" label="用户名">
        <a-input v-model="formModel.username"
                 placeholder="请输入用户名"
                 allow-clear />
      </a-form-item>
      <!-- 花名 -->
      <a-form-item field="nickname" label="花名">
        <a-input v-model="formModel.nickname"
                 placeholder="请输入花名"
                 allow-clear />
      </a-form-item>
      <!-- 用户状态 -->
      <a-form-item field="status" label="用户状态">
        <a-select v-model="formModel.status"
                  :options="toOptions(userStatusKey)"
                  placeholder="请选择用户状态"
                  allow-clear />
      </a-form-item>
      <!-- 手机号 -->
      <a-form-item field="mobile" label="手机号">
        <a-input v-model="formModel.mobile"
                 placeholder="请输入手机号"
                 allow-clear />
      </a-form-item>
      <!-- 邮箱 -->
      <a-form-item field="email" label="邮箱">
        <a-input v-model="formModel.email"
                 placeholder="请输入邮箱"
                 allow-clear />
      </a-form-item>
      <!-- 用户描述 -->
      <a-form-item field="description" label="用户描述">
        <a-input v-model="formModel.description"
                 placeholder="请输入用户描述"
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
          用户列表
        </div>
      </div>
      <!-- 右侧操作 -->
      <div class="table-right-bar-handle">
        <a-space>
          <!-- 新增 -->
          <a-button type="primary"
                    v-permission="['infra:system-user:create']"
                    @click="emits('openAdd')">
            新增
            <template #icon>
              <icon-plus />
            </template>
          </a-button>
          <!-- 删除 -->
          <a-popconfirm :content="`确认删除选中的 ${selectedKeys.length} 条记录吗?`"
                        position="br"
                        type="warning"
                        @ok="deleteSelectedRows">
            <a-button v-permission="['infra:system-user:delete']"
                      type="primary"
                      status="danger"
                      :disabled="selectedKeys.length === 0">
              删除
              <template #icon>
                <icon-delete />
              </template>
            </a-button>
          </a-popconfirm>
          <!-- 调整 -->
          <table-adjust :columns="columns"
                        :columns-hook="columnsHook"
                        :query-order="queryOrder"
                        @query="fetchTableData" />
        </a-space>
      </div>
    </template>
    <!-- table -->
    <a-table v-model:selected-keys="selectedKeys"
             row-key="id"
             ref="tableRef"
             class="table-resize"
             :loading="loading"
             :columns="tableColumns"
             :row-selection="rowSelection"
             :data="tableRenderData"
             :pagination="pagination"
             :bordered="false"
             :column-resizable="true"
             @page-change="(page: number) => fetchTableData(page, pagination.pageSize)"
             @page-size-change="(size: number) => fetchTableData(1, size)">
      <!-- 用户名 -->
      <template #username="{ record }">
        <span class="text-copy" @click="copy(record.username)">
          {{ record.username }}
        </span>
      </template>
      <!-- 状态 -->
      <template #status="{ record }">
        <!-- 有修改权限 -->
        <a-switch v-if="hasPermission('infra:system-user:update-status')"
                  type="round"
                  v-model="record.status"
                  :disabled="record.id === userStore.id"
                  :checked-text="getDictValue(userStatusKey, UserStatus.ENABLED)"
                  :unchecked-text="getDictValue(userStatusKey, UserStatus.DISABLED)"
                  :checked-value="UserStatus.ENABLED"
                  :unchecked-value="UserStatus.DISABLED"
                  :before-change="(s: any) => updateStatus(record.id, s as number)" />
        <!-- 无修改权限 -->
        <span v-else>
          <span class="circle" :style="{
            background: getDictValue(userStatusKey, record.status, 'color')
          }" />
          {{ getDictValue(userStatusKey, record.status) }}
        </span>
      </template>
      <!-- 操作 -->
      <template #handle="{ record }">
        <div class="table-handle-wrapper">
          <!-- 修改 -->
          <a-button type="text"
                    size="mini"
                    v-permission="['infra:system-user:update']"
                    @click="emits('openUpdate', record)">
            修改
          </a-button>
          <!-- 详情 -->
          <a-button type="text"
                    size="mini"
                    @click="openUserDetail(record.id)">
            详情
          </a-button>
          <!-- 重置密码 -->
          <a-button type="text"
                    size="mini"
                    :disabled="record.id === userStore.id"
                    v-permission="['infra:system-user:management:reset-password']"
                    @click="emits('openResetPassword', record)">
            重置密码
          </a-button>
          <!-- 分配角色 -->
          <a-button type="text"
                    size="mini"
                    v-permission="['infra:system-user:grant-role']"
                    @click="emits('openGrantRole', record)">
            分配角色
          </a-button>
          <!-- 删除 -->
          <a-popconfirm content="确认删除这条记录吗?"
                        position="left"
                        type="warning"
                        @ok="deleteRow(record)">
            <a-button v-permission="['infra:system-user:delete']"
                      type="text"
                      size="mini"
                      status="danger"
                      :disabled="record.id === userStore.id">
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
    name: 'userTable'
  };
</script>

<script lang="ts" setup>
  import type { UserQueryRequest, UserQueryResponse } from '@/api/user/user';
  import { reactive, ref, onMounted } from 'vue';
  import { batchDeleteUser, deleteUser, getUserPage, updateUserStatus } from '@/api/user/user';
  import { Message } from '@arco-design/web-vue';
  import columns from '../types/table.columns';
  import { TableName, userStatusKey, UserStatus } from '../types/const';
  import useLoading from '@/hooks/loading';
  import { useTablePagination, useRowSelection, useTableColumns } from '@/hooks/table';
  import usePermission from '@/hooks/permission';
  import { useRouter } from 'vue-router';
  import { useCacheStore, useDictStore, useUserStore } from '@/store';
  import { copy } from '@/hooks/copy';
  import { useQueryOrder, ASC } from '@/hooks/query-order';
  import TableAdjust from '@/components/app/table-adjust/index.vue';

  const emits = defineEmits(['openAdd', 'openUpdate', 'openResetPassword', 'openGrantRole']);

  const cacheStore = useCacheStore();
  const rowSelection = useRowSelection();
  const pagination = useTablePagination();
  const queryOrder = useQueryOrder(TableName, ASC);
  const { tableColumns, columnsHook } = useTableColumns(TableName, columns);
  const { hasPermission } = usePermission();
  const { loading, setLoading } = useLoading();
  const { toOptions, getDictValue } = useDictStore();
  const router = useRouter();
  const userStore = useUserStore();

  const selectedKeys = ref<Array<number>>([]);
  const tableRenderData = ref<Array<UserQueryResponse>>([]);
  const formModel = reactive<UserQueryRequest>({
    id: undefined,
    username: undefined,
    password: undefined,
    nickname: undefined,
    mobile: undefined,
    email: undefined,
    status: undefined,
    description: undefined,
  });

  // 删除当前行
  const deleteRow = async (record: UserQueryResponse) => {
    try {
      setLoading(true);
      // 调用删除接口
      await deleteUser(record.id);
      Message.success('删除成功');
      // 重新加载
      reload();
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 删除选中行
  const deleteSelectedRows = async () => {
    try {
      setLoading(true);
      // 调用删除接口
      await batchDeleteUser(selectedKeys.value);
      Message.success(`成功删除 ${selectedKeys.value.length} 条数据`);
      selectedKeys.value = [];
      // 重新加载
      reload();
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 更新状态
  const updateStatus = (id: number, status: number) => {
    return updateUserStatus({
      id, status
    }).then(() => {
      Message.success('已' + getDictValue(userStatusKey, status, 'label'));
    });
  };

  // 打开详情
  const openUserDetail = (id: number) => {
    router.push({
      name: 'userInfo',
      query: { id }
    });
  };

  // 重新加载
  const reload = () => {
    // 加载数据
    fetchTableData();
    // 清空缓存
    cacheStore.reset('users');
  };

  defineExpose({ reload });

  // 加载数据
  const doFetchTableData = async (request: UserQueryRequest) => {
    try {
      setLoading(true);
      const { data } = await getUserPage(queryOrder.markOrderly(request));
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

  onMounted(() => {
    fetchTableData();
  });

</script>

<style lang="less" scoped>

</style>
