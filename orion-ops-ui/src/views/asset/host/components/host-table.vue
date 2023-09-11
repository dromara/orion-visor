<template>
  <!-- 搜索 -->
  <a-card class="general-card table-search-card">
    <a-query-header :model="formModel"
                    label-align="left"
                    @submit="fetchTableData"
                    @reset="fetchTableData">
      <!-- id -->
      <a-form-item field="id" label="主机id" label-col-flex="50px">
        <a-input-number v-model="formModel.id" placeholder="请输入主机id" allow-clear />
      </a-form-item>
      <!-- 主机名称 -->
      <a-form-item field="name" label="主机名称" label-col-flex="50px">
        <a-input v-model="formModel.name" placeholder="请输入主机名称" allow-clear />
      </a-form-item>
      <!-- 主机编码 -->
      <a-form-item field="code" label="主机编码" label-col-flex="50px">
        <a-input v-model="formModel.code" placeholder="请输入主机编码" allow-clear />
      </a-form-item>
      <!-- 主机地址 -->
      <a-form-item field="address" label="主机地址" label-col-flex="50px">
        <a-input v-model="formModel.address" placeholder="请输入主机地址" allow-clear />
      </a-form-item>
    </a-query-header>
  </a-card>
  <!-- 表格 -->
  <a-card class="general-card table-card">
    <template #title>
      <!-- 左侧标题 -->
      <div class="table-title">
        主机列表
      </div>
      <!-- 右侧按钮 -->
      <div class="table-bar-handle">
        <a-space>
          <!-- 新增 -->
          <a-button type="primary"
                    v-permission="['asset:host:create']"
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
            <a-button v-permission="['asset:host:delete']"
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
      <!-- 地址 -->
      <template #address="{ record }">
        <span class="host-address" title="点击复制" @click="copy(record.address)">
          {{ record.address }}
        </span>
      </template>
      <!-- 操作 -->
      <template #handle="{ record }">
        <div class="table-handle-wrapper row-handle-wrapper">
          <!-- 修改 -->
          <a-button type="text"
                    size="mini"
                    v-permission="['asset:host:update']"
                    @click="emits('openUpdate', record)">
            修改
          </a-button>
          <!-- 删除 -->
          <a-popconfirm content="确认删除这条记录吗?"
                        position="left"
                        type="warning"
                        @ok="deleteRow(record)">
            <a-button v-permission="['asset:host:delete']"
                      type="text"
                      size="mini"
                      status="danger">
              删除
            </a-button>
          </a-popconfirm>
          <!-- 收藏 -->
          <a-tooltip :content="record.favorite ? '取消收藏' : '收藏'">
            <icon-star-fill v-if="record.favorite" class="host-favorite host-favorite-choice" />
            <icon-star v-else class="host-favorite host-favorite-un-choice" />
          </a-tooltip>
        </div>
      </template>
    </a-table>
  </a-card>
</template>

<script lang="ts">
  export default {
    name: 'asset-host-table'
  };
</script>

<script lang="ts" setup>
  import { reactive, ref } from 'vue';
  import { batchDeleteHost, deleteHost, getHostPage, HostQueryRequest, HostQueryResponse } from '@/api/asset/host';
  import { Message } from '@arco-design/web-vue';
  import useLoading from '@/hooks/loading';
  import columns from '../types/table.columns';
  import { defaultPagination, defaultRowSelection } from '@/types/table';
  import {} from '../types/enum.types';
  import { toOptions } from '@/utils/enum';
  import useCopy from '@/hooks/copy';

  const tableRenderData = ref<HostQueryResponse[]>();
  const { loading, setLoading } = useLoading();
  const emits = defineEmits(['openAdd', 'openUpdate']);

  const pagination = reactive(defaultPagination());
  const selectedKeys = ref<number[]>([]);
  const rowSelection = reactive(defaultRowSelection());
  const { copy } = useCopy();

  const formModel = reactive<HostQueryRequest>({
    id: undefined,
    name: undefined,
    code: undefined,
    address: undefined,
  });

  // 删除选中行
  const deleteSelectRows = async () => {
    try {
      setLoading(true);
      // 调用删除接口
      await batchDeleteHost(selectedKeys.value);
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
      await deleteHost(id);
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
  const doFetchTableData = async (request: HostQueryRequest) => {
    try {
      setLoading(true);
      const { data } = await getHostPage(request);
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

  .host-address {
    cursor: pointer;
    color: rgb(var(--arcoblue-6))
  }

  .row-handle-wrapper {
    display: flex;
    align-items: center;

    .host-favorite {
      cursor: pointer;
      line-height: 19px;
      font-size: 19px;
      margin: 0 4px;
    }

    .host-favorite-choice {
      color: rgb(var(--yellow-6));
    }

    .host-favorite-un-choice {
      color: rgb(var(--gray-6));
    }

  }

</style>
