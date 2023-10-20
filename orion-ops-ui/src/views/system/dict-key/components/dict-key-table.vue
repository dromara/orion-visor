<template>
  <!-- 搜索 -->
  <a-card class="general-card table-search-card">
    <a-query-header :model="formModel"
                    label-align="left"
                    @submit="fetchTableData"
                    @reset="fetchTableData"
                    @keyup.enter="() => fetchTableData()">
      <!-- 配置项 -->
      <a-form-item field="keyName" label="配置项" label-col-flex="50px">
        <a-input v-model="formModel.keyName" placeholder="请输入配置项" allow-clear />
      </a-form-item>
      <!-- 配置描述 -->
      <a-form-item field="description" label="配置描述" label-col-flex="50px">
        <a-input v-model="formModel.description" placeholder="请输入配置描述" allow-clear />
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
          字典配置项
        </div>
      </div>
      <!-- 右侧操作 -->
      <div class="table-right-bar-handle">
        <a-space>
          <!-- 新增 -->
          <a-button type="primary"
                    v-permission="['infra:dict-key:create']"
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
      <!-- 配置值类型 -->
      <template #valueType="{ record }">
        <a-tag :color="getEnumValue(record.valueType, ValueTypeEnum,'color')">
          {{ getEnumValue(record.valueType, ValueTypeEnum) }}
        </a-tag>
      </template>
      <!-- 额外参数 -->
      <template #extraSchema="{ record }">
        <template v-if="record.extraSchema">
          <a-space>
            <template v-for="item in JSON.parse(record.extraSchema)" :key="item.name">
              <a-tag :color="getEnumValue(item.type, ValueTypeEnum,'color')">
                {{ item.name }}
              </a-tag>
            </template>
          </a-space>
        </template>
        <template v-else>
          -
        </template>
      </template>
      <!-- 操作 -->
      <template #handle="{ record }">
        <div class="table-handle-wrapper">
          <!-- 修改 -->
          <a-button type="text"
                    size="mini"
                    v-permission="['infra:dict-key:update']"
                    @click="emits('openUpdate', record)">
            修改
          </a-button>
          <!-- 删除 -->
          <a-popconfirm content="确认删除这条记录吗?"
                        position="left"
                        type="warning"
                        @ok="deleteRow(record)">
            <a-button v-permission="['infra:dict-key:delete']"
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
    name: 'system-dict-key-table'
  };
</script>

<script lang="ts" setup>
  import { reactive, ref } from 'vue';
  import { batchDeleteDictKey, deleteDictKey, getDictKeyPage, DictKeyQueryRequest, DictKeyQueryResponse } from '@/api/system/dict-key';
  import { Message } from '@arco-design/web-vue';
  import useLoading from '@/hooks/loading';
  import columns from '../types/table.columns';
  import { usePagination } from '@/types/table';
  import {} from '../types/const';
  import { ValueTypeEnum } from '../types/enum.types';
  import { toOptions, getEnumValue } from '@/utils/enum';
  import { MenuStatusEnum } from '@/views/system/menu/types/enum.types';

  const tableRenderData = ref<DictKeyQueryResponse[]>([]);
  const { loading, setLoading } = useLoading();
  const emits = defineEmits(['openAdd', 'openUpdate']);

  const pagination = usePagination();

  const formModel = reactive<DictKeyQueryRequest>({
    id: undefined,
    keyName: undefined,
    description: undefined,
  });

  // 删除当前行
  const deleteRow = async ({ id }: {
    id: number
  }) => {
    try {
      setLoading(true);
      // 调用删除接口
      await deleteDictKey(id);
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
  const doFetchTableData = async (request: DictKeyQueryRequest) => {
    try {
      setLoading(true);
      const { data } = await getDictKeyPage(request);
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
  fetchTableData();

</script>

<style lang="less" scoped>

</style>
