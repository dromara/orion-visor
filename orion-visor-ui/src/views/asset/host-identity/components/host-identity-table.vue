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
      <!-- 名称 -->
      <a-form-item field="name" label="名称">
        <a-input v-model="formModel.name"
                 placeholder="请输入名称"
                 allow-clear />
      </a-form-item>
      <!-- 类型 -->
      <a-form-item field="type" label="类型">
        <a-select v-model="formModel.type"
                  placeholder="请选择类型"
                  :options="toOptions(identityTypeKey)"
                  allow-clear />
      </a-form-item>
      <!-- 用户名 -->
      <a-form-item field="username" label="用户名">
        <a-input v-model="formModel.username"
                 placeholder="请输入用户名"
                 allow-clear />
      </a-form-item>
      <!-- 主机密钥 -->
      <a-form-item field="keyId" label="主机密钥">
        <host-key-selector v-model="formModel.keyId" allow-clear />
      </a-form-item>
      <!-- 描述 -->
      <a-form-item field="description" label="描述">
        <a-input v-model="formModel.description"
                 placeholder="请输入描述"
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
          身份列表
        </div>
      </div>
      <!-- 右侧操作 -->
      <div class="table-right-bar-handle">
        <a-space>
          <!-- 角色授权 -->
          <a-button type="primary"
                    v-permission="['asset:host-identity:grant']"
                    @click="router.push({ name: GrantRouteName, query: { key: GrantKey.HOST_IDENTITY_ROLE }})">
            角色授权
            <template #icon>
              <icon-user-group />
            </template>
          </a-button>
          <!-- 用户授权 -->
          <a-button type="primary"
                    v-permission="['asset:host-identity:grant']"
                    @click="router.push({ name: GrantRouteName, query: { key: GrantKey.HOST_IDENTITY_USER }})">
            用户授权
            <template #icon>
              <icon-user />
            </template>
          </a-button>
          <!-- 新增 -->
          <a-button type="primary"
                    v-permission="['asset:host-identity:create']"
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
            <a-button v-permission="['asset:host-identity:delete']"
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
      <!-- 类型 -->
      <template #type="{ record }">
        <a-tag :color="getDictValue(identityTypeKey, record.type, 'color')">
          {{ getDictValue(identityTypeKey, record.type) }}
        </a-tag>
      </template>
      <!-- 用户名 -->
      <template #username="{ record }">
        <span class="span-blue text-copy" @click="copy(record.username)">
          {{ record.username }}
        </span>
      </template>
      <!-- 密钥名称 -->
      <template #keyId="{ record }">
        <!-- 有密钥 -->
        <template v-if="record.keyId && record.type === IdentityType.KEY">
          <!-- 可查看详情 -->
          <a-tooltip v-if="hasAnyPermission(['asset:host-key:detail', 'asset:host-key:update'])"
                     content="点击查看详情">
            <a-tag :checked="true"
                   checkable
                   @click="emits('openKeyView', { id: record.keyId })">
              {{ record.keyName }}
            </a-tag>
          </a-tooltip>
          <!-- 不可查看详情 -->
          <a-tag v-else>
            {{ record.keyName }}
          </a-tag>
        </template>
        <!-- 无密钥 -->
        <template v-else>
          <span>-</span>
        </template>
      </template>
      <!-- 操作 -->
      <template #handle="{ record }">
        <div class="table-handle-wrapper">
          <!-- 修改 -->
          <a-button type="text"
                    size="mini"
                    v-permission="['asset:host-identity:update']"
                    @click="emits('openUpdate', record)">
            修改
          </a-button>
          <!-- 删除 -->
          <a-popconfirm content="确认删除这条记录吗?"
                        position="left"
                        type="warning"
                        @ok="deleteRow(record)">
            <a-button v-permission="['asset:host-identity:delete']"
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
    name: 'hostIdentityTable'
  };
</script>

<script lang="ts" setup>
  import type { HostIdentityQueryRequest, HostIdentityQueryResponse } from '@/api/asset/host-identity';
  import { reactive, ref, onMounted } from 'vue';
  import { deleteHostIdentity, batchDeleteHostIdentity, getHostIdentityPage } from '@/api/asset/host-identity';
  import { Message } from '@arco-design/web-vue';
  import columns from '../types/table.columns';
  import useLoading from '@/hooks/loading';
  import usePermission from '@/hooks/permission';
  import { copy } from '@/hooks/copy';
  import { useCacheStore, useDictStore } from '@/store';
  import { useTablePagination, useRowSelection, useTableColumns } from '@/hooks/table';
  import { GrantKey, GrantRouteName } from '@/views/asset/grant/types/const';
  import { TableName, IdentityType, identityTypeKey } from '../types/const';
  import { useQueryOrder, ASC } from '@/hooks/query-order';
  import { useRouter } from 'vue-router';
  import HostKeySelector from '@/components/asset/host-key/selector/index.vue';
  import TableAdjust from '@/components/app/table-adjust/index.vue';

  const emits = defineEmits(['openAdd', 'openUpdate', 'openKeyView']);

  const router = useRouter();
  const cacheStore = useCacheStore();
  const rowSelection = useRowSelection();
  const pagination = useTablePagination();
  const queryOrder = useQueryOrder(TableName, ASC);
  const { tableColumns, columnsHook } = useTableColumns(TableName, columns);
  const { toOptions, getDictValue } = useDictStore();
  const { loading, setLoading } = useLoading();
  const { hasAnyPermission } = usePermission();

  const selectedKeys = ref<Array<number>>([]);
  const tableRenderData = ref<Array<HostIdentityQueryResponse>>([]);
  const formModel = reactive<HostIdentityQueryRequest>({
    id: undefined,
    name: undefined,
    type: undefined,
    username: undefined,
    keyId: undefined,
    description: undefined,
  });

  // 删除当前行
  const deleteRow = async (record: HostIdentityQueryResponse) => {
    try {
      setLoading(true);
      // 调用删除接口
      await deleteHostIdentity(record.id);
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
      await batchDeleteHostIdentity(selectedKeys.value);
      Message.success(`成功删除 ${selectedKeys.value.length} 条数据`);
      selectedKeys.value = [];
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
    // 清空缓存
    cacheStore.reset('hostIdentities', 'authorizedHostIdentities');
  };

  defineExpose({ reload });

  // 加载数据
  const doFetchTableData = async (request: HostIdentityQueryRequest) => {
    try {
      setLoading(true);
      const { data } = await getHostIdentityPage(queryOrder.markOrderly(request));
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
