<template>
  <!-- 搜索 -->
  <a-card class="general-card table-search-card">
    <a-query-header :model="formModel"
                    label-align="left"
                    @submit="fetchTableData"
                    @reset="fetchTableData">
      <!-- id -->
      <a-form-item field="id" label="id" label-col-flex="60px">
        <a-input-number v-model="formModel.id" placeholder="请输入id" allow-clear/>
      </a-form-item>
      <!-- 用户名 -->
      <a-form-item field="username" label="用户名" label-col-flex="60px">
        <a-input v-model="formModel.username" placeholder="请输入用户名" allow-clear/>
      </a-form-item>
      <!-- 密码 -->
      <a-form-item field="password" label="密码" label-col-flex="60px">
        <a-input v-model="formModel.password" placeholder="请输入密码" allow-clear/>
      </a-form-item>
      <!-- 花名 -->
      <a-form-item field="nickname" label="花名" label-col-flex="60px">
        <a-input v-model="formModel.nickname" placeholder="请输入花名" allow-clear/>
      </a-form-item>
      <!-- 头像地址 -->
      <a-form-item field="avatar" label="头像地址" label-col-flex="60px">
        <a-input v-model="formModel.avatar" placeholder="请输入头像地址" allow-clear/>
      </a-form-item>
      <!-- 手机号 -->
      <a-form-item field="mobile" label="手机号" label-col-flex="60px">
        <a-input v-model="formModel.mobile" placeholder="请输入手机号" allow-clear/>
      </a-form-item>
      <!-- 邮箱 -->
      <a-form-item field="email" label="邮箱" label-col-flex="60px">
        <a-input v-model="formModel.email" placeholder="请输入邮箱" allow-clear/>
      </a-form-item>
      <!-- 用户状态 0停用 1启用 2锁定 -->
      <a-form-item field="status" label="用户状态 0停用 1启用 2锁定" label-col-flex="60px">
        <a-input-number v-model="formModel.status" placeholder="请输入用户状态 0停用 1启用 2锁定" allow-clear/>
      </a-form-item>
      <!-- 最后登录时间 -->
      <a-form-item field="lastLoginTime" label="最后登录时间" label-col-flex="60px">
        <a-date-picker v-model="formModel.lastLoginTime"
                       style="width: 100%"
                       placeholder="请选择最后登录时间"
                       show-time
                       allow-clear/>
      </a-form-item>
    </a-query-header>
  </a-card>
  <!-- 表格 -->
  <a-card class="general-card table-card">
    <template #title>
      <!-- 左侧标题 -->
      <div class="table-title">
        用户列表
      </div>
      <!-- 右侧按钮 -->
      <div class="table-bar-handle">
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
          <a-popconfirm position="br"
                        type="warning"
                        :content="`确认删除选中的${selectedKeys.length}条记录吗?`"
                        @ok="deleteSelectRows">
            <a-button v-permission="['infra:system-user:delete']"
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
             class="table-wrapper-8"
             ref="tableRef"
             label-align="left"
             :loading="loading"
             :columns="columns"
             v-model:selectedKeys="selectedKeys"
             :row-selection="rowSelection"
             :data="tableRenderData"
             :pagination="pagination"
             @page-change="(page) => fetchTableData(page, pagination.pageSize)"
             @page-size-change="(size) => fetchTableData(pagination.current, size)"
             :bordered="false">
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
          <!-- 删除 -->
          <a-popconfirm content="确认删除这条记录吗?"
                        position="left"
                        type="warning"
                        @ok="deleteRow(record)">
            <a-button type="text" size="mini"
                      status="danger"
                      v-permission="['infra:system-user:delete']">
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
    name: 'user-user-table'
  };
</script>

<script lang="ts" setup>
  import { reactive, ref } from 'vue';
  import { batchDeleteUser, deleteUser, getUserPage, UserQueryRequest, UserQueryResponse } from '@/api/user/user';
  import { Message } from '@arco-design/web-vue';
  import useLoading from '@/hooks/loading';
  import columns from '../types/table.columns';
  import { defaultPagination, defaultRowSelection } from '@/types/table';

  const tableRenderData = ref<UserQueryResponse[]>();
  const { loading, setLoading } = useLoading();
  const emits = defineEmits(['openAdd', 'openUpdate']);

  const pagination = reactive(defaultPagination());
  const selectedKeys = ref<number[]>([]);
  const rowSelection = reactive(defaultRowSelection());

  const formModel = reactive<UserQueryRequest>({
    id: undefined,
    username: undefined,
    password: undefined,
    nickname: undefined,
    avatar: undefined,
    mobile: undefined,
    email: undefined,
    status: undefined,
    lastLoginTime: undefined,
  });

  // 删除选中行
  const deleteSelectRows = async () => {
    try {
      setLoading(true);
      // 调用删除接口
      await batchDeleteUser(selectedKeys.value);
      Message.success(`成功删除${selectedKeys.value.length}条数据`);
      selectedKeys.value = [];
      // 重新加载数据
      await fetchTableData();
    } finally {
      setLoading(false);
    }
  };

  // 删除当前行
  const deleteRow = async ({ id }: { id: number }) => {
    try {
      setLoading(true);
      // 调用删除接口
      await deleteUser(id);
      Message.success('删除成功');
      // 重新加载数据
      await fetchTableData();
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
  const doFetchTableData = async (request: UserQueryRequest) => {
    try {
      setLoading(true);
      const { data } = await getUserPage(request);
      tableRenderData.value = data.rows;
      pagination.total = data.total;
      pagination.current = request.page;
      pagination.pageSize = request.limit;
    } finally {
      setLoading(false);
    }
  };

  // 切换页码
  const fetchTableData = (page = 1, limit = pagination.pageSize, form = formModel) => {
    doFetchTableData({ page, limit, ...form });
  };
  fetchTableData();

</script>

<style lang="less" scoped>

</style>
