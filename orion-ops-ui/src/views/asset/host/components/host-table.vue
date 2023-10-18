<template>
  <!-- 搜索 -->
  <a-card class="general-card table-search-card">
    <a-query-header :model="formModel"
                    label-align="left"
                    @submit="fetchTableData"
                    @reset="fetchTableData">
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
                            tag-type="hostTags"
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
          <!-- 仅看收藏 -->
          <a-checkbox v-model="formModel.favorite" @change="fetchTableData()">
            <template #checkbox="{ checked }">
              <a-tag :checked="checked"
                     class="only-favorite"
                     size="large"
                     color="arcoblue"
                     checkable>
                仅看收藏
              </a-tag>
            </template>
          </a-checkbox>
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
        <a-tooltip content="点击复制">
          <span class="host-address" @click="copy(record.address)">
            <icon-copy class="mr4" />{{ record.address }}
          </span>
        </a-tooltip>
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
          <!-- 收藏 -->
          <span v-if="record.favorite"
                title="取消收藏"
                class="host-favorite host-favorite-choice"
                @click="() => toggleFavorite(record, record.id)">
            <icon-star-fill />
          </span>
          <span v-else
                title="收藏"
                class="host-favorite host-favorite-un-choice"
                @click="() => toggleFavorite(record, record.id)">
           <icon-star />
          </span>
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
  import { deleteHost, getHostPage, HostQueryRequest, HostQueryResponse } from '@/api/asset/host';
  import { Message } from '@arco-design/web-vue';
  import useLoading from '@/hooks/loading';
  import columns from '../types/host.table.columns';
  import { tagColor } from '../types/const';
  import { usePagination } from '@/types/table';
  import useCopy from '@/hooks/copy';
  import useFavorite from '@/hooks/favorite';
  import { dataColor } from '@/utils';
  import TagMultiSelector from '@/components/meta/tag/tag-multi-selector.vue';

  const tagSelector = ref();
  const tableRenderData = ref<HostQueryResponse[]>([]);
  const { loading, setLoading } = useLoading();
  const emits = defineEmits(['openAdd', 'openUpdate', 'openUpdateConfig']);

  const { copy } = useCopy();
  const { toggle: toggleFavorite } = useFavorite('HOST');

  const pagination = usePagination();

  const formModel = reactive<HostQueryRequest>({
    id: undefined,
    name: undefined,
    code: undefined,
    address: undefined,
    favorite: undefined,
    tags: undefined,
    extra: true
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

      :hover {
        transition: .2s;
        color: rgba(232, 203, 12, .9);
      }
    }

    .host-favorite-un-choice {
      color: rgb(var(--gray-6));

      :hover {
        transition: .2s;
        color: rgb(var(--yellow-6));
      }
    }
  }

  .only-favorite {
    user-select: none;
    color: rgb(var(--arcoblue-5));
    background: rgb(var(--gray-2));
  }

</style>
