<template>
  <!-- 搜索 -->
  <a-card class="general-card table-search-card">
    <a-query-header :model="formModel"
                    label-align="left"
                    @submit="fetchTableData"
                    @reset="fetchTableData">
      <!-- 配置项 fixme 修改为下拉框 -->
      <a-form-item field="keyName" label="配置项" label-col-flex="50px">
        <a-input v-model="formModel.keyName" placeholder="请输入配置项" allow-clear />
      </a-form-item>
      <!-- 配置名称 -->
      <a-form-item field="name" label="配置名称" label-col-flex="50px">
        <a-input v-model="formModel.name" placeholder="请输入配置名称" allow-clear />
      </a-form-item>
      <!-- 配置值 -->
      <a-form-item field="value" label="配置值" label-col-flex="50px">
        <a-input v-model="formModel.value" placeholder="请输入配置值" allow-clear />
      </a-form-item>
      <!-- 配置描述 -->
      <a-form-item field="label" label="配置描述" label-col-flex="50px">
        <a-input v-model="formModel.label" placeholder="请输入配置描述" allow-clear />
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
          数据字典
        </div>
      </div>
      <!-- 右侧操作 -->
      <div class="table-right-bar-handle">
        <a-space>
          <!-- 新增 -->
          <a-button type="primary"
                    v-permission="['infra:dict-value:create']"
                    @click="emits('openAdd')">
            新增字典值
            <template #icon>
              <icon-plus />
            </template>
          </a-button>
          <!-- 删除 -->
          <a-popconfirm position="br"
                        type="warning"
                        :content="`确认删除选中的${selectedKeys.length}条记录吗?`"
                        @ok="deleteSelectRows">
            <a-button v-permission="['infra:dict-value:delete']"
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
             v-model:selected-keys="selectedKeys"
             :row-selection="rowSelection"
             :data="tableRenderData"
             :pagination="pagination"
             @page-change="(page) => fetchTableData(page, pagination.pageSize)"
             @page-size-change="(size) => fetchTableData(1, size)"
             :bordered="false">
      <!-- 操作 -->
      <template #handle="{ record }">
        <div class="table-handle-wrapper">
          <!-- 修改 -->
          <a-button type="text"
                    size="mini"
                    v-permission="['infra:dict-value:update']"
                    @click="emits('openUpdate', record)">
            修改
          </a-button>
          <!-- 历史 -->
          <a-button type="text"
                    size="mini"
                    v-permission="['infra:dict-value:update']"
                    @click="emits('openUpdate', record)">
            历史
          </a-button>
          <!-- 删除 -->
          <a-popconfirm content="确认删除这条记录吗?"
                        position="left"
                        type="warning"
                        @ok="deleteRow(record)">
            <a-button v-permission="['infra:dict-value:delete']"
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
    name: 'system-dict-value-table'
  };
</script>

<script lang="ts" setup>
  import { reactive, ref } from 'vue';
  import { batchDeleteDictValue, deleteDictValue, getDictValuePage, DictValueQueryRequest, DictValueQueryResponse } from '@/api/system/dict-value';
  import { Message } from '@arco-design/web-vue';
  import useLoading from '@/hooks/loading';
  import columns from '../types/table.columns';
  import { usePagination, useRowSelection } from '@/types/table';
  import {} from '../types/const';
  import {} from '../types/enum.types';
  import { toOptions, getEnumValue } from '@/utils/enum';

  const tableRenderData = ref<DictValueQueryResponse[]>([]);
  const { loading, setLoading } = useLoading();
  const emits = defineEmits(['openAdd', 'openUpdate']);

  const pagination = usePagination();
  const selectedKeys = ref<number[]>([]);
  const rowSelection = useRowSelection();

  const formModel = reactive<DictValueQueryRequest>({
    id: undefined,
    keyId: undefined,
    keyName: undefined,
    name: undefined,
    value: undefined,
    label: undefined,
    extra: undefined,
    sort: undefined,
  });

  // 删除选中行
  const deleteSelectRows = async () => {
    try {
      setLoading(true);
      // 调用删除接口
      await batchDeleteDictValue(selectedKeys.value);
      Message.success(`成功删除${selectedKeys.value.length}条数据`);
      selectedKeys.value = [];
      // 重新加载数据
      await fetchTableData();
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
      await deleteDictValue(id);
      Message.success('删除成功');
      // 重新加载数据
      await fetchTableData();
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
  const doFetchTableData = async (request: DictValueQueryRequest) => {
    try {
      setLoading(true);
      const { data } = await getDictValuePage(request);
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
  fetchTableData();

</script>

<style lang="less" scoped>

</style>
