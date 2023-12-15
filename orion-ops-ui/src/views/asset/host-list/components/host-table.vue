<template>
  <!-- 搜索 -->
  <a-card class="general-card table-search-card">
    <a-query-header :model="formModel"
                    label-align="left"
                    @submit="fetchTableData"
                    @reset="fetchTableData"
                    @keyup.enter="() => fetchTableData()">
      <!-- id -->
      <a-form-item field="id" label="主机id" label-col-flex="50px">
        <a-input-number v-model="formModel.id"
                        placeholder="请输入主机id"
                        allow-clear
                        hide-button />
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
      <!-- 主机标签 -->
      <a-form-item field="tags" label="主机标签" label-col-flex="50px">
        <tag-multi-selector v-model="formModel.tags"
                            ref="tagSelector"
                            :allowCreate="false"
                            :limit="0"
                            type="HOST"
                            placeholder="请选择主机标签" />
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
          主机列表
        </div>
      </div>
      <!-- 右侧操作 -->
      <div class="table-right-bar-handle">
        <a-space>
          <!-- 主机分组 -->
          <a-button type="primary"
                    v-permission="['asset:host-group:update']"
                    @click="emits('openHostGroup')">
            主机分组
            <template #icon>
              <icon-layers />
            </template>
          </a-button>
          <!-- 角色授权 -->
          <a-button type="primary"
                    v-permission="['asset:host-group:grant']"
                    @click="$router.push({ name: GrantRouteName, query: { key: GrantKey.HOST_GROUP_ROLE }})">
            角色授权
            <template #icon>
              <icon-user-group />
            </template>
          </a-button>
          <!-- 用户授权 -->
          <a-button type="primary"
                    v-permission="['asset:host-group:grant']"
                    @click="$router.push({ name: GrantRouteName, query: { key: GrantKey.HOST_GROUP_USER }})">
            用户授权
            <template #icon>
              <icon-user />
            </template>
          </a-button>
          <!-- 新增 -->
          <a-button type="primary"
                    v-permission="['asset:host:create']"
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
      <!-- 地址 -->
      <template #address="{ record }">
        <span class="copy-left" @click="copy(record.address)" title="复制">
          <icon-copy />
        </span>
        <span class="span-blue">{{ record.address }}</span>
      </template>
      <!-- 标签 -->
      <template #tags="{ record }">
        <a-space v-if="record.tags">
          <a-tag v-for="tag in record.tags"
                 :key="tag.id"
                 :color="dataColor(tag.name, tagColor)">
            {{ tag.name }}
          </a-tag>
        </a-space>
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
          <!-- 配置 -->
          <a-button type="text"
                    size="mini"
                    v-permission="['asset:host:update-config']"
                    @click="emits('openUpdateConfig', record)">
            配置
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
  import type { HostQueryRequest, HostQueryResponse } from '@/api/asset/host';
  import { reactive, ref, onMounted } from 'vue';
  import { deleteHost, getHostPage } from '@/api/asset/host';
  import { Message } from '@arco-design/web-vue';
  import useLoading from '@/hooks/loading';
  import columns from '../types/host.table.columns';
  import { tagColor } from '../types/const';
  import { usePagination } from '@/types/table';
  import useCopy from '@/hooks/copy';
  import { dataColor } from '@/utils';
  import TagMultiSelector from '@/components/meta/tag/tag-multi-selector.vue';
  import { GrantKey, GrantRouteName } from '@/views/asset/grant/types/const';

  const tagSelector = ref();
  const tableRenderData = ref<HostQueryResponse[]>([]);
  const { loading, setLoading } = useLoading();
  const emits = defineEmits(['openAdd', 'openUpdate', 'openUpdateConfig', 'openHostGroup']);

  const { copy } = useCopy();

  const pagination = usePagination();

  const formModel = reactive<HostQueryRequest>({
    id: undefined,
    name: undefined,
    code: undefined,
    address: undefined,
    tags: undefined,
    queryTag: true
  });

  // 删除当前行
  const deleteRow = async ({ id }: {
    id: number
  }) => {
    try {
      setLoading(true);
      // 调用删除接口
      await deleteHost(id);
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
  const doFetchTableData = async (request: HostQueryRequest) => {
    try {
      setLoading(true);
      const { data } = await getHostPage(request);
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

  .row-handle-wrapper {
    display: flex;
    align-items: center;
  }
</style>
