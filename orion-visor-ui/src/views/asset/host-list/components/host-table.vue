<template>
  <!-- 搜索 -->
  <a-card class="general-card table-search-card">
    <query-header :model="formModel"
                  label-align="left"
                  @submit="fetchTableData"
                  @reset="fetchTableData"
                  @keyup.enter="() => fetchTableData()">
      <!-- id -->
      <a-form-item field="id" label="主机id">
        <a-input-number v-model="formModel.id"
                        placeholder="请输入主机id"
                        allow-clear
                        hide-button />
      </a-form-item>
      <!-- 主机名称 -->
      <a-form-item field="name" label="主机名称">
        <a-input v-model="formModel.name" placeholder="请输入主机名称" allow-clear />
      </a-form-item>
      <!-- 主机编码 -->
      <a-form-item field="code" label="主机编码">
        <a-input v-model="formModel.code" placeholder="请输入主机编码" allow-clear />
      </a-form-item>
      <!-- 主机地址 -->
      <a-form-item field="address" label="主机地址">
        <a-input v-model="formModel.address" placeholder="请输入主机地址" allow-clear />
      </a-form-item>
      <!-- 主机类型 -->
      <a-form-item field="type" label="主机类型">
        <a-select v-model="formModel.type"
                  :options="toOptions(hostTypeKey)"
                  placeholder="请选择主机类型"
                  allow-clear />
      </a-form-item>
      <!-- 主机状态 -->
      <a-form-item field="status" label="主机状态">
        <a-select v-model="formModel.status"
                  :options="toOptions(hostStatusKey)"
                  placeholder="请选择主机状态"
                  allow-clear />
      </a-form-item>
      <!-- 主机标签 -->
      <a-form-item field="tags" label="主机标签">
        <tag-multi-selector v-model="formModel.tags"
                            ref="tagSelector"
                            :limit="0"
                            type="HOST"
                            :tagColor="tagColor"
                            placeholder="请选择主机标签" />
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
          <!-- 删除 -->
          <a-popconfirm :content="`确认删除选中的 ${selectedKeys.length} 条记录吗?`"
                        position="br"
                        type="warning"
                        @ok="deleteSelectRows">
            <a-button v-permission="['asset:host:delete']"
                      type="primary"
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
    <a-table v-model:selected-keys="selectedKeys"
             row-key="id"
             ref="tableRef"
             :loading="loading"
             :columns="columns"
             :row-selection="rowSelection"
             :data="tableRenderData"
             :pagination="pagination"
             :bordered="false"
             @page-change="(page: number) => fetchTableData(page, pagination.pageSize)"
             @page-size-change="(size: number) => fetchTableData(1, size)">
      <!-- 主机类型 -->
      <template #type="{ record }">
        <a-tag :color="getDictValue(hostTypeKey, record.type, 'color')">
          {{ getDictValue(hostTypeKey, record.type) }}
        </a-tag>
      </template>
      <!-- 主机编码 -->
      <template #code="{ record }">
        <a-tag>{{ record.code }}</a-tag>
      </template>
      <!-- 地址 -->
      <template #address="{ record }">
        <span class="span-blue text-copy host-address"
              title="复制"
              @click="copy(record.address)">
          {{ record.address }}
        </span>
        <span class="span-blue text-copy"
              title="复制"
              @click="copy(record.port)">
          {{ record.port }}
        </span>
      </template>
      <!-- 主机状态 -->
      <template #status="{ record }">
        <a-tag :color="getDictValue(hostStatusKey, record.status, 'color')">
          {{ getDictValue(hostStatusKey, record.status) }}
        </a-tag>
      </template>
      <!-- 标签 -->
      <template #tags="{ record }">
        <a-space v-if="record.tags"
                 style="margin-bottom: -8px;"
                 :wrap="true">
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
          <!-- 更多 -->
          <a-dropdown trigger="hover" :popup-max-height="false">
            <a-button type="text" size="mini">
              更多
            </a-button>
            <template #content>
              <!-- 修改状态 -->
              <a-doption v-permission="['asset:host:update-status']"
                         @click="updateStatus(record)">
                <span class="more-doption"
                      :class="[toggleDictValue(hostStatusKey, record.status, 'status')]">
                  {{ toggleDictValue(hostStatusKey, record.status, 'label') }}
                </span>
              </a-doption>
              <!-- 复制 -->
              <a-doption v-permission="['asset:host:create']"
                         @click="emits('openCopy', record)">
                <span class="more-doption normal">
                  复制
                </span>
              </a-doption>
              <!-- SSH -->
              <a-doption v-if="record.type === hostType.SSH.type"
                         v-permission="['asset:host-terminal:access']"
                         @click="openNewRoute({ name: 'terminal', query: { connect: record.id, type: 'SSH' } })">
                <span class="more-doption normal">
                  SSH
                </span>
              </a-doption>
              <!-- SFTP -->
              <a-doption v-if="record.type === hostType.SSH.type"
                         v-permission="['asset:host-terminal:access']"
                         @click="openNewRoute({ name: 'terminal', query: { connect: record.id, type: 'SFTP' } })">
                <span class="more-doption normal">
                  SFTP
                </span>
              </a-doption>
            </template>
          </a-dropdown>
        </div>
      </template>
    </a-table>
  </a-card>
</template>

<script lang="ts">
  export default {
    name: 'hostTable'
  };
</script>

<script lang="ts" setup>
  import type { HostQueryRequest, HostQueryResponse } from '@/api/asset/host';
  import { reactive, ref, onMounted } from 'vue';
  import { deleteHost, batchDeleteHost, getHostPage, updateHostStatus } from '@/api/asset/host';
  import { Message, Modal } from '@arco-design/web-vue';
  import { tagColor, hostTypeKey, hostStatusKey, hostType } from '../types/const';
  import { useTablePagination, useRowSelection } from '@/hooks/table';
  import { useDictStore } from '@/store';
  import { copy } from '@/hooks/copy';
  import { dataColor } from '@/utils';
  import useLoading from '@/hooks/loading';
  import columns from '../types/table.columns';
  import { GrantKey, GrantRouteName } from '@/views/asset/grant/types/const';
  import { openNewRoute } from '@/router';
  import TagMultiSelector from '@/components/meta/tag/multi-selector/index.vue';

  const emits = defineEmits(['openCopy', 'openAdd', 'openUpdate', 'openUpdateConfig', 'openHostGroup']);

  const pagination = useTablePagination();
  const rowSelection = useRowSelection();
  const { loading, setLoading } = useLoading();
  const { toOptions, getDictValue, toggleDictValue, toggleDict } = useDictStore();

  const tagSelector = ref();
  const selectedKeys = ref<Array<number>>([]);
  const tableRenderData = ref<Array<HostQueryResponse>>([]);
  const formModel = reactive<HostQueryRequest>({
    id: undefined,
    name: undefined,
    code: undefined,
    address: undefined,
    type: undefined,
    status: undefined,
    tags: undefined,
    queryTag: true
  });

  // 更新状态
  const updateStatus = async (record: HostQueryResponse) => {
    const dict = toggleDict(hostStatusKey, record.status);
    Modal.confirm({
      title: `${dict.label}确认`,
      titleAlign: 'start',
      content: `确定要${dict.label}该主机吗?`,
      okText: '确定',
      onOk: async () => {
        try {
          setLoading(true);
          const newStatus = dict.value as string;
          // 调用修改接口
          await updateHostStatus({
            id: record.id,
            status: newStatus,
          });
          record.status = newStatus;
          Message.success(`已${dict.label}`);
        } catch (e) {
        } finally {
          setLoading(false);
        }
      }
    });
  };

  // 删除当前行
  const deleteRow = async (record: HostQueryResponse) => {
    try {
      setLoading(true);
      // 调用删除接口
      await deleteHost(record.id);
      Message.success('删除成功');
      // 重新加载数据
      fetchTableData();
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 删除选中行
  const deleteSelectRows = async () => {
    try {
      setLoading(true);
      // 调用删除接口
      await batchDeleteHost(selectedKeys.value);
      Message.success(`成功删除 ${selectedKeys.value.length} 条数据`);
      selectedKeys.value = [];
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

  .row-handle-wrapper {
    display: flex;
    align-items: center;
  }

  .host-address::after {
    content: ':';
    user-select: text;
  }

</style>
