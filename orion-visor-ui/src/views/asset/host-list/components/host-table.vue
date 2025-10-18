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
      <!-- 主机协议 -->
      <a-form-item field="type" label="主机协议">
        <a-select v-model="formModel.type"
                  :options="toOptions(hostTypeKey)"
                  placeholder="请选择主机协议"
                  allow-clear />
      </a-form-item>
      <!-- 系统类型 -->
      <a-form-item field="osType" label="系统类型">
        <a-select v-model="formModel.osType"
                  :options="toOptions(hostOsTypeKey)"
                  placeholder="请选择系统类型"
                  allow-clear />
      </a-form-item>
      <!-- 系统架构 -->
      <a-form-item field="archType" label="系统架构">
        <a-select v-model="formModel.archType"
                  :options="toOptions(hostArchTypeKey)"
                  placeholder="请选择系统架构"
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
                            type="HOST"
                            :limit="0"
                            :tag-color="tagColor"
                            placeholder="请选择主机标签" />
      </a-form-item>
      <!-- 主机描述 -->
      <a-form-item field="description" label="主机描述">
        <a-input v-model="formModel.description"
                 placeholder="请输入主机描述"
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
                    @click="router.push({ name: GrantRouteName, query: { key: GrantKey.HOST_GROUP_ROLE }})">
            角色授权
            <template #icon>
              <icon-user-group />
            </template>
          </a-button>
          <!-- 用户授权 -->
          <a-button type="primary"
                    v-permission="['asset:host-group:grant']"
                    @click="router.push({ name: GrantRouteName, query: { key: GrantKey.HOST_GROUP_USER }})">
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
                        @ok="deleteSelectedRows">
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
      <!-- 主机信息 -->
      <template #hostInfo="{ record }">
        <div class="info-wrapper">
          <div class="info-item">
            <span class="info-label">主机名称</span>
            <span class="info-value text-copy text-ellipsis"
                  :title="record.name"
                  @click="copy(record.name, true)">
              {{ record.name }}
            </span>
          </div>
          <div class="info-item">
            <span class="info-label">主机编码</span>
            <span class="info-value text-copy text-ellipsis"
                  :title="record.code"
                  @click="copy(record.code, true)">
              {{ record.code }}
            </span>
          </div>
          <div class="info-item">
            <span class="info-label">主机地址</span>
            <span class="info-value span-blue text-copy text-ellipsis"
                  :title="record.address"
                  @click="copy(record.address, true)">
              {{ record.address }}
            </span>
          </div>
        </div>
      </template>
      <!-- 主机规格 -->
      <template #hostSpec="{ record }">
        <div class="info-wrapper">
          <div class="info-item">
            <span class="spec-label">规格</span>
            <span v-if="record.spec" class="spec-value text-ellipsis">
              {{
                [
                  addSuffix(record.spec.cpuPhysicalCore, 'C'),
                  addSuffix(record.spec.memorySize, 'G'),
                  addSuffix(record.spec.diskSize, 'G')
                ].filter(Boolean).join(' / ') || '-'
              }}
            </span>
            <span v-else class="spec-value text-ellipsis">-</span>
          </div>
          <!-- 系统类型 -->
          <div class="info-item">
            <span class="spec-label">系统</span>
            <span class="spec-value text-ellipsis">
              {{ getDictValue(hostOsTypeKey, record.osType) }} - {{ getDictValue(hostArchTypeKey, record.archType) }}
            </span>
          </div>
        </div>
      </template>
      <!-- 主机协议 -->
      <template #protocols="{ record }">
        <a-space v-if="record.types?.length"
                 style="margin-bottom: -8px;"
                 wrap>
          <a-tag v-for="type in record.types"
                 :key="type"
                 :color="getDictValue(hostTypeKey, type, 'color')">
            {{ getDictValue(hostTypeKey, type) }}
          </a-tag>
        </a-space>
      </template>
      <!-- 主机状态 -->
      <template #status="{ record }">
        <a-tag :color="getDictValue(hostStatusKey, record.status, 'color')">
          {{ getDictValue(hostStatusKey, record.status) }}
        </a-tag>
      </template>
      <!-- 主机分组 -->
      <template #groups="{ record }">
        <a-space v-if="record.groupIdList?.length"
                 style="margin-bottom: -8px;"
                 :wrap="true">
          <template v-for="groupId in record.groupIdList"
                    :key="groupId">
            <a-tag>{{ hostGroupList.find((s: any) => s.key === groupId)?.title || groupId }}</a-tag>
          </template>
        </a-space>
      </template>
      <!-- 主机标签 -->
      <template #tags="{ record }">
        <a-space v-if="record.tags?.length"
                 style="margin-bottom: -8px;"
                 :wrap="true">
          <template v-for="tag in record.tags"
                    :key="tag.id">
            <a-tag :color="dataColor(tag.name, tagColor)">
              {{ tag.name }}
            </a-tag>
          </template>
        </a-space>
      </template>
      <!-- 操作 -->
      <template #handle="{ record }">
        <div class="table-handle-wrapper row-handle-wrapper">
          <!-- 单协议连接 -->
          <a-button v-if="record.types?.length === 1"
                    type="text"
                    size="mini"
                    v-permission="['terminal:terminal:access']"
                    @click="openNewRoute({ name: 'terminal', query: { connect: record.id, type: record.types[0] } })">
            连接
          </a-button>
          <!-- 多协议连接 -->
          <a-popover v-if="(record.types?.length || 0) > 1"
                     :title="undefined"
                     :content-style="{ padding: '8px' }">
            <a-button v-permission="['terminal:terminal:access']"
                      type="text"
                      size="mini">
              连接
            </a-button>
            <template #content>
              <a-space>
                <a-button v-for="type in record.types"
                          :key="type"
                          size="mini"
                          @click="openNewRoute({ name: 'terminal', query: { connect: record.id, type }})">
                  {{ type }}
                </a-button>
              </a-space>
            </template>
          </a-popover>
          <!-- 修改 -->
          <a-button type="text"
                    size="mini"
                    v-permission="['asset:host:update']"
                    @click="emits('openUpdate', record)">
            修改
          </a-button>
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
              <!-- 删除 -->
              <a-doption v-permission="['asset:host:delete']"
                         @click="deleteRow(record)">
                <span class="more-doption error">删除</span>
              </a-doption>
              <!-- 复制 -->
              <a-doption v-permission="['asset:host:create']"
                         @click="emits('openCopy', record)">
                <span class="more-doption normal">复制</span>
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
  import type { HostGroupQueryResponse } from '@/api/asset/host-group';
  import { reactive, ref, onMounted } from 'vue';
  import { deleteHost, batchDeleteHost, getHostPage, updateHostStatus } from '@/api/asset/host';
  import { Message, Modal } from '@arco-design/web-vue';
  import { tagColor, hostTypeKey, hostStatusKey, hostOsTypeKey, hostArchTypeKey, TableName } from '../types/const';
  import { useTablePagination, useRowSelection, useTableColumns } from '@/hooks/table';
  import { useQueryOrder, ASC } from '@/hooks/query-order';
  import { useCacheStore, useDictStore } from '@/store';
  import { copy } from '@/hooks/copy';
  import { addSuffix, dataColor } from '@/utils';
  import { useRouter } from 'vue-router';
  import useLoading from '@/hooks/loading';
  import columns from '../types/table.columns';
  import { GrantKey, GrantRouteName } from '@/views/asset/grant/types/const';
  import { openNewRoute } from '@/router';
  import TableAdjust from '@/components/app/table-adjust/index.vue';
  import TagMultiSelector from '@/components/meta/tag/multi-selector/index.vue';

  const emits = defineEmits(['openCopy', 'openAdd', 'openUpdate', 'openHostGroup']);

  const router = useRouter();
  const cacheStore = useCacheStore();
  const rowSelection = useRowSelection();
  const pagination = useTablePagination();
  const queryOrder = useQueryOrder(TableName, ASC);
  const { tableColumns, columnsHook } = useTableColumns(TableName, columns);
  const { loading, setLoading } = useLoading();
  const { toOptions, getDictValue, toggleDictValue, toggleDict } = useDictStore();

  const hostGroupList = ref<Array<HostGroupQueryResponse>>([]);
  const tagSelector = ref();
  const selectedKeys = ref<Array<number>>([]);
  const tableRenderData = ref<Array<HostQueryResponse>>([]);
  const formModel = reactive<HostQueryRequest>({
    id: undefined,
    name: undefined,
    code: undefined,
    address: undefined,
    type: undefined,
    osType: undefined,
    archType: undefined,
    status: undefined,
    tags: undefined,
    description: undefined,
    queryGroup: true,
    queryTag: true,
    querySpec: true,
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
    Modal.confirm({
      title: '删除前确认!',
      titleAlign: 'start',
      content: '确定要删除这条记录吗?',
      okText: '删除',
      onOk: async () => {
        try {
          setLoading(true);
          // 调用删除接口
          await deleteHost(record.id);
          Message.success('删除成功');
          // 重新加载
          reload();
        } catch (e) {
        } finally {
          setLoading(false);
        }
      }
    });
  };

  // 删除选中行
  const deleteSelectedRows = async () => {
    try {
      setLoading(true);
      // 调用删除接口
      await batchDeleteHost(selectedKeys.value);
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
    cacheStore.reset('host_ALL', 'host_SSH',
      'authorizedHost_ALL', 'authorizedHost_SSH');
  };

  defineExpose({ reload });

  // 加载数据
  const doFetchTableData = async (request: HostQueryRequest) => {
    try {
      setLoading(true);
      const { data } = await getHostPage(queryOrder.markOrderly(request));
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

  onMounted(async () => {
    // 加载分组数据
    hostGroupList.value = await useCacheStore().loadHostGroupList();
    // 加载数据
    fetchTableData();
  });

</script>

<style lang="less" scoped>

  .info-wrapper {
    padding: 4px 0;

    .info-item {
      display: flex;

      &:not(:last-child) {
        margin-bottom: 4px;
      }

      .info-label, .spec-label {
        margin-right: 8px;
        user-select: none;
        font-weight: 600;

        &::after {
          content: ':';
        }
      }

      .info-label {
        width: 60px;
      }

      .spec-label {
        width: 34px;
      }

      .info-value {
        width: calc(100% - 68px);
      }

      .spec-value {
        width: calc(100% - 42px);
      }
    }
  }

  .row-handle-wrapper {
    display: flex;
    align-items: center;
  }

</style>
