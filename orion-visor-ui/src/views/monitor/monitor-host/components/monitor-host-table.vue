<template>
  <!-- 搜索 -->
  <a-card class="general-card table-search-card">
    <query-header :model="formModel"
                  label-align="left"
                  @submit="fetchTableData"
                  @reset="fetchTableData"
                  @keyup.enter="() => fetchTableData()">
      <!-- 主机名称 -->
      <a-form-item field="name" label="主机名称">
        <a-input v-model="formModel.name" placeholder="请输入主机名称" allow-clear />
      </a-form-item>
      <!-- 主机地址 -->
      <a-form-item field="address" label="主机地址">
        <a-input v-model="formModel.address" placeholder="请输入主机地址" allow-clear />
      </a-form-item>
      <!-- 在线状态 -->
      <a-form-item field="agentOnlineStatus" label="在线状态">
        <a-select v-model="formModel.agentOnlineStatus"
                  :options="toOptions(OnlineStatusKey)"
                  placeholder="请选择在线状态"
                  allow-clear />
      </a-form-item>
      <!-- 探针状态 -->
      <a-form-item field="agentInstallStatus" label="探针状态">
        <a-select v-model="formModel.agentInstallStatus"
                  :options="toOptions(InstallStatusKey)"
                  placeholder="请选择探针状态"
                  allow-clear />
      </a-form-item>
      <!-- 告警开关 -->
      <a-form-item field="alarmSwitch" label="告警开关">
        <a-select v-model="formModel.alarmSwitch"
                  :options="toOptions(AlarmSwitchKey)"
                  placeholder="请选择告警开关"
                  allow-clear />
      </a-form-item>
      <!-- agentKey -->
      <a-form-item field="agentKey" label="agentKey">
        <a-input v-model="formModel.agentKey"
                 placeholder="请输入 agentKey"
                 allow-clear />
      </a-form-item>
      <!-- 负责人 -->
      <a-form-item field="ownerUserId" label="负责人">
        <user-selector v-model="formModel.ownerUserId"
                       placeholder="请选择负责人"
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
          监控主机列表
        </div>
      </div>
      <!-- 右侧操作 -->
      <div class="table-right-bar-handle">
        <a-space>
          <!-- 开启告警 -->
          <a-button v-if="selectedKeys.length"
                    v-permission="['monitor:monitor-host:update', 'monitor:monitor-host:update-switch']"
                    type="primary"
                    status="success"
                    @click="toggleAlarmSwitchBatch(selectedKeys, AlarmSwitch.ON)">
            开启告警
            <template #icon>
              <icon-play-arrow-fill />
            </template>
          </a-button>
          <!-- 关闭告警 -->
          <a-button v-if="selectedKeys.length"
                    v-permission="['monitor:monitor-host:update', 'monitor:monitor-host:update-switch']"
                    type="primary"
                    status="warning"
                    @click="toggleAlarmSwitchBatch(selectedKeys, AlarmSwitch.OFF)">
            关闭告警
            <template #icon>
              <icon-pause />
            </template>
          </a-button>
          <!-- 安装 -->
          <a-button v-permission="['asset:host:install-agent']"
                    type="primary"
                    :disabled="selectedKeys.length === 0"
                    @click="installAgent(selectedKeys)">
            安装
            <template #icon>
              <icon-plus />
            </template>
          </a-button>
          <!-- 自动刷新 -->
          <a-tooltip content="开启后每 60s 会自动刷新" mini>
            <a-button @click="toggleAutoRefresh">
              {{ autoRefresh ? '关闭自动刷新' : '开启自动刷新' }}
              <template #icon>
                <icon-refresh />
              </template>
            </a-button>
          </a-tooltip>
          <!-- 上传发布包 -->
          <a-button @click="emits('openUpload')">
            上传发布包
            <template #icon>
              <icon-upload />
            </template>
          </a-button>
          <!-- 调整 -->
          <table-adjust :columns="columns"
                        :columns-hook="columnsHook"
                        @query="fetchTableData" />
        </a-space>
      </div>
    </template>
    <!-- table -->
    <a-table v-model:selected-keys="selectedKeys"
             row-key="hostId"
             ref="tableRef"
             class="table-resize"
             :loading="loading"
             :columns="tableColumns"
             :data="tableRenderData"
             :pagination="pagination"
             :row-class="setRowClassName"
             :row-selection="rowSelection"
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
            <span class="info-label">主机地址</span>
            <span class="info-value span-blue text-copy text-ellipsis"
                  :title="record.address"
                  @click="copy(record.address, true)">
              {{ record.address }}
            </span>
          </div>
        </div>
      </template>
      <!-- 在线状态 -->
      <template #agentOnlineStatus="{ record }">
        <monitor-cell :data-cell="false" :record="record">
          <a-tooltip :content="'切换分区时间: ' + dateFormat(new Date(record.agentOnlineChangeTime))" mini>
            <a-tag :color="getDictValue(OnlineStatusKey, record.agentOnlineStatus, 'color')">
              <template #icon>
                <component :is="getDictValue(OnlineStatusKey, record.agentOnlineStatus, 'icon')" />
              </template>
              {{ getDictValue(OnlineStatusKey, record.agentOnlineStatus) }}
            </a-tag>
          </a-tooltip>
        </monitor-cell>
      </template>
      <!-- cpu -->
      <template #cpuUsage="{ record }">
        <monitor-cell :data-cell="true" :record="record">
          <a-tooltip :content="record.config?.cpuName +': ' + record.metricsData?.cpuUsagePercent?.toFixed(2) + '%'" mini>
            <a-progress size="large"
                        width="120px"
                        :animation="true"
                        :show-text="false"
                        :color="getPercentProgressColor(record.metricsData.cpuUsagePercent / 100)"
                        :percent="record.metricsData.cpuUsagePercent / 100" />
          </a-tooltip>
          <span class="metrics-value-per">{{ record.metricsData.cpuUsagePercent.toFixed(2) }}</span>
        </monitor-cell>
      </template>
      <!-- 内存 -->
      <template #memoryUsage="{ record }">
        <monitor-cell :data-cell="true" :record="record">
          <a-tooltip :content="getFileSize(record.metricsData?.memoryUsageBytes)" mini>
            <a-progress size="large"
                        width="120px"
                        :animation="true"
                        :show-text="false"
                        :color="getPercentProgressColor(record.metricsData.memoryUsagePercent / 100)"
                        :percent="record.metricsData.memoryUsagePercent / 100" />
          </a-tooltip>
          <span class="metrics-value-per">{{ record.metricsData.memoryUsagePercent.toFixed(2) }}</span>
        </monitor-cell>
      </template>
      <!-- 磁盘 -->
      <template #diskUsage="{ record }">
        <monitor-cell :data-cell="true" :record="record">
          <a-tooltip :content="record.config?.diskName +': ' + getFileSize(record.metricsData?.diskUsageBytes)" mini>
            <a-progress size="large"
                        width="120px"
                        :animation="true"
                        :show-text="false"
                        :color="getPercentProgressColor(record.metricsData.diskUsagePercent / 100)"
                        :percent="record.metricsData.diskUsagePercent / 100" />
          </a-tooltip>
          <span class="metrics-value-per">{{ record.metricsData.diskUsagePercent.toFixed(2) }}</span>
        </monitor-cell>
      </template>
      <!-- 网络 -->
      <template #network="{ record }">
        <monitor-cell data-class="network"
                      :data-cell="true"
                      :record="record">
          <!-- 上行速度 -->
          <a-tooltip :content="record.config?.networkName +': ' + getFileSize(record.metricsData?.networkSentPreBytes) + '/s'" mini>
            <b class="span-green" title="上行速度">
              <icon-arrow-up class="mr2" />
              {{ getFileSize(record.metricsData.networkSentPreBytes) }}/s
            </b>
          </a-tooltip>
          <!-- 下行速度 -->
          <a-tooltip :content="record.config?.networkName +': ' + getFileSize(record.metricsData?.networkRecvPreBytes) + '/s'" mini>
            <b class="mt2 span-blue" title="下行速度">
              <icon-arrow-down class="mr2" />
              {{ getFileSize(record.metricsData.networkRecvPreBytes) }}/s
            </b>
          </a-tooltip>
        </monitor-cell>
      </template>
      <!-- 负载 -->
      <template #load="{ record }">
        <monitor-cell :data-cell="true" :record="record">
          <b>{{ record.metricsData?.load1?.toFixed(2) }}, {{ record.metricsData?.load5?.toFixed(2) }}, {{ record.metricsData?.load15?.toFixed(2) }}</b>
        </monitor-cell>
      </template>
      <!-- 告警策略 -->
      <template #alarmPolicy="{ record }">
        <monitor-cell :data-cell="false" :record="record">
          <b class="pointer"
             :style="{ color: record.alarmSwitch ? 'rgb(var(--green-6))' : 'rgb(var(--gray-6))' }"
             @click="emits('toPolicy', record)">
            {{ record.policyName || '-' }}
          </b>
        </monitor-cell>
      </template>
      <!-- 告警负责人 -->
      <template #ownerUsername="{ record }">
        <monitor-cell :data-cell="false" :record="record">
          {{ record.ownerUsername }}
        </monitor-cell>
      </template>
      <!-- 探针版本 -->
      <template #agentVersion="{ record }">
        <!-- 安装状态 -->
        <div v-if="record.installLog?.status === AgentLogStatus.WAIT
                    || record.installLog?.status === AgentLogStatus.RUNNING
                    || record.installLog?.status === AgentLogStatus.FAILED"
             class="flex-center">
          <!-- 当前状态 -->
          <a-tag :color="getDictValue(AgentLogStatusKey, record.installLog.status, 'color')"
                 :loading="getDictValue(AgentLogStatusKey, record.installLog.status, 'loading')">
            {{ getDictValue(AgentLogStatusKey, record.installLog.status, 'installLabel') }}
          </a-tag>
          <!-- 提示信息 -->
          <a-tooltip v-if="record.installLog.message"
                     :content="record.installLog.message"
                     mini>
            <icon-question-circle class="fs16 span-red ml4" />
          </a-tooltip>
        </div>
        <!-- 已安装显示版本号 -->
        <b v-else-if="record.agentInstallStatus === AgentInstallStatus.INSTALLED"
           :class="record.latestVersion && record.latestVersion !== record.agentVersion ? 'span-red' : ''">
          {{ record.agentVersion ? 'v' + record.agentVersion : '-' }}
          <a-tooltip v-if="record.latestVersion && record.latestVersion !== record.agentVersion"
                     :content="'存在新版本 v' + record.latestVersion + ', 请及时升级'"
                     mini>
            <icon-arrow-rise />
          </a-tooltip>
        </b>
        <!-- 显示未安装 -->
        <span v-else>
          <a-tag>{{ getDictValue(InstallStatusKey, record.agentInstallStatus) }}</a-tag>
        </span>
      </template>
      <!-- 标签 -->
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
      <!-- agentKey -->
      <template #agentKey="{ record }">
        <span class="text-copy text-ellipsis"
              :title="record.agentKey"
              @click="copy(record.agentKey, true)">
          {{ record.agentKey }}
        </span>
      </template>
      <!-- 操作 -->
      <template #handle="{ record }">
        <div class="table-handle-wrapper">
          <a-button v-permission="['monitor:monitor-host:query']"
                    type="text"
                    size="mini"
                    :disabled="record.agentInstallStatus !== AgentInstallStatus.INSTALLED"
                    @click="openDetail(record.hostId, record.name)">
            详情
          </a-button>
          <a-dropdown trigger="hover" :popup-max-height="false">
            <a-button type="text" size="mini">
              操作
            </a-button>
            <template #content>
              <!-- 修改 -->
              <a-doption v-if="record.agentInstallStatus === AgentInstallStatus.INSTALLED"
                         v-permission="['monitor:monitor-host:update']"
                         @click="emits('openUpdate', record)">
                <span class="more-doption normal">修改配置</span>
              </a-doption>
              <!-- 复制 Key -->
              <a-doption @click="copy(record.agentKey)">
                <span class="more-doption normal">复制 Key</span>
              </a-doption>
              <!-- 安装探针 -->
              <a-doption v-permission="['asset:host:install-agent']"
                         :disabled="record.installLog?.status === AgentLogStatus.WAIT || record.installLog?.status === AgentLogStatus.RUNNING"
                         @click="installAgent([record.hostId])">
                <span class="more-doption normal">安装探针</span>
              </a-doption>
              <!-- 安装成功 -->
              <a-doption v-if="record.installLog?.id && record.installLog?.status !== AgentLogStatus.SUCCESS"
                         v-permission="['asset:host:install-agent']"
                         @click="setInstallSuccess(record.installLog)">
                <span class="more-doption normal">安装成功</span>
              </a-doption>
              <!-- 告警开关 -->
              <a-doption v-if="record.id"
                         v-permission="['monitor:monitor-host:update', 'monitor:monitor-host:update-switch']"
                         @click="toggleAlarmSwitch(record)">
                <span class="more-doption normal">
                  {{ toggleDictValue(AlarmSwitchKey, record.alarmSwitch, 'label') + '告警' }}
                </span>
              </a-doption>
              <!-- 连接终端 单协议连接 -->
              <a-doption v-if="record.types?.length === 1"
                         v-permission="['terminal:terminal:access']"
                         @click="openNewRoute({ name: 'terminal', query: { connect: record.hostId, type: record.types[0] } })">
                <span class="more-doption normal">
                  连接终端
                </span>
              </a-doption>
              <!-- 连接终端 多协议连接 -->
              <a-popover v-if="(record.types?.length || 0) > 1"
                         :title="undefined"
                         position="left"
                         :content-style="{ padding: '8px' }">
                <a-doption v-permission="['terminal:terminal:access']">
                  <span class="more-doption normal">
                    连接终端
                  </span>
                </a-doption>
                <template #content>
                  <a-space direction="vertical">
                    <a-button v-for="type in record.types"
                              :key="type"
                              size="mini"
                              @click="openNewRoute({ name: 'terminal', query: { connect: record.hostId, type }})">
                      {{ type }}
                    </a-button>
                  </a-space>
                </template>
              </a-popover>
            </template>
          </a-dropdown>
        </div>
      </template>
    </a-table>
  </a-card>
</template>

<script lang="ts">
  export default {
    name: 'monitorHostTable'
  };
</script>

<script lang="ts" setup>
  import type { MonitorHostQueryRequest, MonitorHostQueryResponse } from '@/api/monitor/monitor-host';
  import { reactive, ref, onMounted } from 'vue';
  import { getMonitorHostPage, updateMonitorHostAlarmSwitch } from '@/api/monitor/monitor-host';
  import useLoading from '@/hooks/loading';
  import columns from '../types/table.columns';
  import { TableName, AlarmSwitch, AlarmSwitchKey, OnlineStatusKey, InstallStatusKey, AgentLogStatus, AgentLogStatusKey } from '../types/const';
  import { AgentInstallStatus, tagColor } from '@/views/asset/host-list/types/const';
  import { useTablePagination, useTableColumns, useRowSelection } from '@/hooks/table';
  import { useDictStore } from '@/store';
  import { copy } from '@/hooks/copy';
  import { getPercentProgressColor } from '@/utils/charts';
  import { getFileSize } from '@/utils/file';
  import { openNewRoute } from '@/router';
  import { dateFormat, dataColor } from '@/utils';
  import { Message, Modal } from '@arco-design/web-vue';
  import useMonitorHostList from '../types/use-monitor-host-list';
  import MonitorCell from './monitor-cell.vue';
  import TableAdjust from '@/components/app/table-adjust/index.vue';
  import UserSelector from '@/components/user/user/selector/index.vue';

  const emits = defineEmits(['openUpdate', 'openUpload', 'toPolicy']);

  const rowSelection = useRowSelection();
  const pagination = useTablePagination();
  const { loading, setLoading } = useLoading();
  const { tableColumns, columnsHook } = useTableColumns(TableName, columns);
  const { toOptions, getDictValue, toggleDictValue } = useDictStore();

  const selectedKeys = ref<Array<number>>([]);
  const tableRenderData = ref<Array<MonitorHostQueryResponse>>([]);
  const formModel = reactive<MonitorHostQueryRequest>({
    alarmSwitch: undefined,
    ownerUserId: undefined,
    policyId: undefined,
    name: undefined,
    code: undefined,
    address: undefined,
    agentInstallStatus: undefined,
    agentOnlineStatus: undefined,
    description: undefined,
    tags: undefined,
  });

  // 重新加载
  const reload = () => {
    // 重新加载数据
    fetchTableData();
  };

  defineExpose({ reload });

  const {
    autoRefresh,
    openDetail,
    toggleAutoRefresh,
    installAgent,
    setInstallSuccess,
    toggleAlarmSwitch,
  } = useMonitorHostList({
    hosts: tableRenderData,
    setLoading,
    reload,
  });

  // 获取行样式
  const setRowClassName = (record: MonitorHostQueryResponse) => {
    if (record.agentInstallStatus === AgentInstallStatus.NOT_INSTALL) {
      return 'not-install';
    }
    return 'installed';
  };

  // 批量修改告警开关状态
  const toggleAlarmSwitchBatch = async (hostIdList: Array<number>, alarmSwitch: number) => {
    const label = getDictValue(AlarmSwitchKey, alarmSwitch);
    Modal.confirm({
      title: `${label}确认`,
      titleAlign: 'start',
      content: `确定要${label}告警功能吗?`,
      okText: '确定',
      onOk: async () => {
        try {
          setLoading(true);
          const rows = tableRenderData.value.filter(s => hostIdList.includes(s.hostId));
          if (!rows.length) {
            return;
          }
          const idList = rows.map(s => s.id).filter(Boolean);
          // 调用修改接口
          await updateMonitorHostAlarmSwitch({ idList, alarmSwitch });
          rows.forEach(s => s.alarmSwitch = alarmSwitch);
          Message.success(`已${label}`);
        } catch (e) {
        } finally {
          setLoading(false);
        }
      }
    });
  };

  // 加载数据
  const doFetchTableData = async (request: MonitorHostQueryRequest) => {
    try {
      setLoading(true);
      const { data } = await getMonitorHostPage(request);
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

  onMounted(fetchTableData);

</script>

<style lang="less" scoped>

  :deep(.not-install) {
    .arco-table-td {
      background-color: rgb(var(--gray-1)) !important;
    }

    .arco-table-td::before {
      background-color: rgb(var(--gray-1)) !important;
    }
  }

  .info-wrapper {
    padding: 4px 0;

    .info-item {
      display: flex;

      &:not(:last-child) {
        margin-bottom: 4px;
      }

      .info-label {
        width: 60px;
        margin-right: 8px;
        user-select: none;
        font-weight: 600;

        &::after {
          content: ':';
        }
      }

      .info-value {
        width: calc(100% - 68px);
      }
    }
  }

  .row-handle-wrapper {
    display: flex;
    align-items: center;
  }

</style>
