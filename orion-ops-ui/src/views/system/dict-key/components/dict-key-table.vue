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
          <!-- 刷新缓存 -->
          <a-popconfirm content="确定要刷新全局字典缓存吗?"
                        position="left"
                        type="warning"
                        @ok="doRefreshCache">
            <a-button type="primary" status="warning"
                      v-permission="['infra:dict-key:refresh-cache']">
              刷新缓存
              <template #icon>
                <icon-sync />
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
             :data="tableRenderData"
             :pagination="pagination"
             @page-change="(page) => fetchTableData(page, pagination.pageSize)"
             @page-size-change="(size) => fetchTableData(1, size)"
             :bordered="false">
      <!-- 配置项 -->
      <template #keyName="{ record }">
        <span class="copy-left" title="复制" @click="copy(record.keyName)">
          <icon-copy />
        </span>
        <span>{{ record.keyName }}</span>
      </template>
      <!-- 配置值类型 -->
      <template #valueType="{ record }">
        <a-tag :color="getDictValue(dictValueTypeKey, record.valueType, 'color')">
          {{ getDictValue(dictValueTypeKey, record.valueType) }}
        </a-tag>
      </template>
      <!-- 额外参数 -->
      <template #extraSchema="{ record }">
        <template v-if="record.extraSchema">
          <a-space>
            <template v-for="item in JSON.parse(record.extraSchema)" :key="item.name">
              <a-tag :color="getDictValue(dictValueTypeKey, item.type, 'color')">
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
          <!-- 查看 -->
          <a-button type="text"
                    size="mini"
                    @click="openView(record)">
            查看
          </a-button>
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
  import type { DictKeyQueryRequest, DictKeyQueryResponse } from '@/api/system/dict-key';
  import { reactive, ref, onMounted } from 'vue';
  import { batchDeleteDictKey, deleteDictKey, getDictKeyPage, refreshCache } from '@/api/system/dict-key';
  import { Message } from '@arco-design/web-vue';
  import useLoading from '@/hooks/loading';
  import columns from '../types/table.columns';
  import { usePagination } from '@/types/table';
  import { dictValueTypeKey } from '../types/const';
  import useCopy from '@/hooks/copy';
  import { useCacheStore, useDictStore } from '@/store';
  import { getDictValueList } from '@/api/system/dict-value';

  const tableRenderData = ref<DictKeyQueryResponse[]>([]);
  const emits = defineEmits(['openAdd', 'openUpdate', 'openView']);

  const pagination = usePagination();
  const { copy } = useCopy();
  const { loading, setLoading } = useLoading();
  const { toOptions, getDictValue } = useDictStore();
  const cacheStore = useCacheStore();

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
      cacheStore.reset('dictKeys');
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
    cacheStore.reset('dictKeys');
  };

  // 更新后回调
  const updatedCallback = () => {
    fetchTableData();
    cacheStore.reset('dictKeys');
  };

  defineExpose({
    addedCallback, updatedCallback
  });

  // 打开查看视图
  const openView = async (record: DictKeyQueryResponse) => {
    try {
      setLoading(true);
      // 查看
      const { data } = await getDictValueList([record.keyName]);
      emits('openView', data[record.keyName], `${record.keyName} - ${record.description}`);
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 刷新缓存
  const doRefreshCache = async () => {
    try {
      setLoading(true);
      await refreshCache();
      Message.success('刷新成功 页面缓存刷新后生效');
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

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

  onMounted(() => {
    fetchTableData();
  });

</script>

<style lang="less" scoped>

</style>
