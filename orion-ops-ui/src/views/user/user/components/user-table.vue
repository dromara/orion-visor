<template>
  <!-- 搜索 -->
  <a-card class="general-card table-search-card">
    <query-header :model="formModel"
                  label-align="left"
                  @submit="fetchTableData"
                  @reset="fetchTableData"
                  @keyup.enter="() => fetchTableData()">
      <!-- id -->
      <a-form-item field="id" label="id" label-col-flex="50px">
        <a-input-number v-model="formModel.id"
                        placeholder="请输入id"
                        allow-clear
                        hide-button />
      </a-form-item>
      <!-- 用户名 -->
      <a-form-item field="username" label="用户名" label-col-flex="50px">
        <a-input v-model="formModel.username" placeholder="请输入用户名" allow-clear />
      </a-form-item>
      <!-- 花名 -->
      <a-form-item field="nickname" label="花名" label-col-flex="50px">
        <a-input v-model="formModel.nickname" placeholder="请输入花名" allow-clear />
      </a-form-item>
      <!-- 用户状态 -->
      <a-form-item field="status" label="用户状态" label-col-flex="50px">
        <a-select v-model="formModel.status"
                  :options="toOptions(userStatusKey)"
                  placeholder="请选择用户状态"
                  allow-clear />
      </a-form-item>
      <!-- 手机号 -->
      <a-form-item field="mobile" label="手机号" label-col-flex="50px">
        <a-input v-model="formModel.mobile" placeholder="请输入手机号" allow-clear />
      </a-form-item>
      <!-- 邮箱 -->
      <a-form-item field="email" label="邮箱" label-col-flex="50px">
        <a-input v-model="formModel.email" placeholder="请输入邮箱" allow-clear />
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
             @page-change="(page) => fetchTableData(page, pagination.pageSize)"
             @page-size-change="(size) => fetchTableData(1, size)"
             :bordered="false">
      <!-- 状态 -->
      <template #status="{ record }">
        <span class="circle" :style="{
          background: getDictValue(userStatusKey, record.status, 'color')
        }" />
        {{ getDictValue(userStatusKey, record.status) }}
      </template>
      <!-- 操作 -->
      <template #handle="{ record }">
        <div class="table-handle-wrapper">
          <!-- 启用/停用 -->
          <a-popconfirm :content="`确定要${UserStatus.ENABLED === record.status
                        ? getDictValue(userStatusKey, UserStatus.DISABLED)
                        : getDictValue(userStatusKey, UserStatus.ENABLED)}当前用户?`"
                        position="left"
                        type="warning"
                        @ok="updateStatus(record)">
            <a-button v-permission="['infra:system-user:update-status']"
                      type="text"
                      size="mini"
                      :disabled="record.id === userStore.id">
              {{
                UserStatus.ENABLED === record.status
                  ? getDictValue(userStatusKey, UserStatus.DISABLED)
                  : getDictValue(userStatusKey, UserStatus.ENABLED)
              }}
            </a-button>
          </a-popconfirm>
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
                    :disabled="record.id === userStore.id"
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
  import { deleteUser, getUserPage, updateUserStatus } from '@/api/user/user';
  import { Message } from '@arco-design/web-vue';
  import useLoading from '@/hooks/loading';
  import columns from '../types/table.columns';
  import { userStatusKey, UserStatus } from '../types/const';
  import { usePagination } from '@/types/table';
  import { useDictStore, useUserStore } from '@/store';
  import { useRouter } from 'vue-router';

  const emits = defineEmits(['openAdd', 'openUpdate', 'openResetPassword', 'openGrantRole']);

  const tableRenderData = ref<UserQueryResponse[]>([]);

  const pagination = usePagination();
  const { loading, setLoading } = useLoading();
  const { toOptions, getDictValue, getDict } = useDictStore();

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

  const router = useRouter();
  const userStore = useUserStore();

  // 删除当前行
  const deleteRow = async ({ id }: {
    id: number
  }) => {
    try {
      setLoading(true);
      // 调用删除接口
      await deleteUser(id);
      Message.success('删除成功');
      // 重新加载数据
      fetchTableData();
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 更新状态
  const updateStatus = async (record: any) => {
    try {
      setLoading(true);
      // 更新状态
      const newStatus = UserStatus.ENABLED === record.status
        ? getDict(userStatusKey, UserStatus.DISABLED)
        : getDict(userStatusKey, UserStatus.ENABLED);
      await updateUserStatus({
        id: record.id,
        status: newStatus.value as number
      });
      Message.success(`${newStatus.label}成功`);
      record.status = newStatus.value;
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 打开详情
  const openUserDetail = (id: number) => {
    router.push({
      name: 'userInfo',
      query: { id }
    });
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
