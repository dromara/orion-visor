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
        <a-input v-model="formModel.name" placeholder="请输入名称" allow-clear />
      </a-form-item>
      <!-- 用户名 -->
      <a-form-item field="username" label="用户名">
        <a-input v-model="formModel.username" placeholder="请输入用户名" allow-clear />
      </a-form-item>
      <!-- 主机秘钥 -->
      <a-form-item field="keyId" label="主机秘钥">
        <host-key-selector v-model="formModel.keyId" allow-clear />
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
                    @click="$router.push({ name: GrantRouteName, query: { key: GrantKey.HOST_IDENTITY_ROLE }})">
            角色授权
            <template #icon>
              <icon-user-group />
            </template>
          </a-button>
          <!-- 用户授权 -->
          <a-button type="primary"
                    v-permission="['asset:host-identity:grant']"
                    @click="$router.push({ name: GrantRouteName, query: { key: GrantKey.HOST_IDENTITY_USER }})">
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
      <!-- 用户名 -->
      <template #username="{ record }">
        <span class="span-blue text-copy" @click="copy(record.username)">
          {{ record.username }}
        </span>
      </template>
      <!-- 秘钥名称 -->
      <template #keyId="{ record }">
        <template v-if="record.keyId">
          <!-- 可查看详情 -->
          <a-tooltip v-if="hasAnyPermission(['asset:host-key:detail', 'asset:host-key:update'])"
                     content="点击查看详情">
            <a-tag :checked="true"
                   checkable
                   @click="emits('openKeyView',{id: record.keyId})">
              {{ record.keyName }}
            </a-tag>
          </a-tooltip>
          <!-- 不可查看详情 -->
          <a-tag v-else>
            {{ record.keyName }}
          </a-tag>
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
  import { deleteHostIdentity, getHostIdentityPage } from '@/api/asset/host-identity';
  import { Message } from '@arco-design/web-vue';
  import useLoading from '@/hooks/loading';
  import columns from '../types/table.columns';
  import { usePagination } from '@/types/table';
  import HostKeySelector from '@/components/asset/host-key/selector/index.vue';
  import useCopy from '@/hooks/copy';
  import usePermission from '@/hooks/permission';
  import { GrantKey, GrantRouteName } from '@/views/asset/grant/types/const';

  const emits = defineEmits(['openAdd', 'openUpdate', 'openKeyView']);

  const tableRenderData = ref<HostIdentityQueryResponse[]>([]);

  const { copy } = useCopy();
  const pagination = usePagination();
  const { loading, setLoading } = useLoading();
  const { hasAnyPermission } = usePermission();

  const formModel = reactive<HostIdentityQueryRequest>({
    id: undefined,
    name: undefined,
    username: undefined,
    keyId: undefined,
  });

  // 删除当前行
  const deleteRow = async ({ id }: {
    id: number
  }) => {
    try {
      setLoading(true);
      // 调用删除接口
      await deleteHostIdentity(id);
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
  const doFetchTableData = async (request: HostIdentityQueryRequest) => {
    try {
      setLoading(true);
      const { data } = await getHostIdentityPage(request);
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
