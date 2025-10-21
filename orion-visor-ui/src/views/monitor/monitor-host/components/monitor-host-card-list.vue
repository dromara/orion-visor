<template>
  <card-list v-model:searchValue="formModel.searchValue"
             search-input-placeholder="输入 id / 名称 / 编码 / 地址"
             :create-card-position="false"
             :loading="loading"
             :field-config="cardFieldConfig"
             :list="renderList"
             :pagination="pagination"
             :card-layout-cols="cardColLayout"
             :filter-count="filterCount"
             :fields-hook="fieldsHook"
             :handle-visible="{ disableAdd: true }"
             @reset="reset"
             @search="fetchCardData"
             @page-change="fetchCardData">
    <!-- 左侧操作 -->
    <template #leftHandle>
      <a-space>
        <!-- 自动刷新 -->
        <a-tooltip content="开启后每 60s 会自动刷新" mini>
          <a-button class="card-header-button" @click="toggleAutoRefresh">
            {{ autoRefresh ? '关闭自动刷新' : '开启自动刷新' }}
            <template #icon>
              <icon-refresh />
            </template>
          </a-button>
        </a-tooltip>
        <!-- 上传发布包 -->
        <a-button class="card-header-button" @click="emits('openUpload')">
          上传发布包
          <template #icon>
            <icon-upload />
          </template>
        </a-button>
      </a-space>
    </template>
    <!-- 过滤条件 -->
    <template #filterContent>
      <a-form :model="formModel"
              class="card-filter-form"
              size="small"
              ref="formRef"
              label-align="right"
              :auto-label-width="true"
              @keyup.enter="() => fetchCardData()">
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
      </a-form>
    </template>
    <!-- 标题 -->
    <template #title="{ record }">
      {{ record.name }}
    </template>
    <!-- 主机地址 -->
    <template #address="{ record }">
      <span class="span-blue text-copy"
            title="复制"
            @click="copy(record.address)">
        {{ record.address }}
      </span>
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
                      :animation="true"
                      :show-text="false"
                      :color="getPercentProgressColor(record.metricsData?.cpuUsagePercent / 100)"
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
                      :animation="true"
                      :show-text="false"
                      :color="getPercentProgressColor(record.metricsData?.memoryUsagePercent / 100)"
                      :percent="record.metricsData.memoryUsagePercent / 100" />
        </a-tooltip>
        <span class="metrics-value-per">{{ record.metricsData?.memoryUsagePercent?.toFixed(2) }}</span>
      </monitor-cell>
    </template>
    <!-- 磁盘 -->
    <template #diskUsage="{ record }">
      <monitor-cell :data-cell="true" :record="record">
        <a-tooltip :content="record.config?.diskName +': ' + getFileSize(record.metricsData?.diskUsageBytes)" mini>
          <a-progress size="large"
                      :animation="true"
                      :show-text="false"
                      :color="getPercentProgressColor(record.metricsData?.diskUsagePercent / 100)"
                      :percent="record.metricsData.diskUsagePercent / 100" />
        </a-tooltip>
        <span class="metrics-value-per">{{ record.metricsData?.diskUsagePercent?.toFixed(2) }}</span>
      </monitor-cell>
    </template>
    <!-- 网络 -->
    <template #network="{ record }">
      <monitor-cell data-class="network"
                    :data-cell="true"
                    :record="record">
        <div class="network-inline">
          <!-- 上行速度 -->
          <a-tooltip :content="record.config?.networkName +': ' + getFileSize(record.metricsData?.networkSentPreBytes) + '/s'" mini>
            <b class="span-green fs12" title="上行速度">
              <icon-arrow-up />
              {{ getFileSize(record.metricsData?.networkSentPreBytes) }}/s
            </b>
          </a-tooltip>
          <!-- 下行速度 -->
          <a-tooltip :content="record.config?.networkName +': ' + getFileSize(record.metricsData?.networkRecvPreBytes) + '/s'" mini>
            <b class="span-blue fs12" title="下行速度">
              <icon-arrow-down />
              {{ getFileSize(record.metricsData?.networkRecvPreBytes) }}/s
            </b>
          </a-tooltip>
        </div>
      </monitor-cell>
    </template>
    <!-- 负载 -->
    <template #load="{ record }">
      <monitor-cell :data-cell="true" :record="record">
        <b class="fs12">
          {{ record.metricsData?.load1?.toFixed(2) }}, {{ record.metricsData?.load5?.toFixed(2) }}, {{ record.metricsData?.load15?.toFixed(2) }}
        </b>
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
    <!-- 标签 -->
    <template #tags="{ record }">
      <a-space v-if="record.tags?.length"
               style="margin-bottom: -8px;"
               :wrap="true">
        <a-tag v-for="tag in record.tags"
               :key="tag.id"
               :color="dataColor(tag.name, tagColor)">
          {{ tag.name }}
        </a-tag>
      </a-space>
      <span v-else>-</span>
    </template>
    <!-- agentKey -->
    <template #agentKey="{ record }">
        <span class="text-copy text-ellipsis"
              :title="record.agentKey"
              @click="copy(record.agentKey, true)">
          {{ record.agentKey }}
        </span>
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
         class="fs12"
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
    <!-- 拓展操作 -->
    <template #extra="{ record }">
      <a-space>
        <a-button v-permission="['monitor:monitor-host:query']"
                  type="text"
                  size="mini"
                  :disabled="record.agentInstallStatus !== AgentInstallStatus.INSTALLED"
                  @click="openDetail(record.hostId, record.name)">
          详情
        </a-button>
        <!-- 更多操作 -->
        <a-dropdown trigger="hover" :popup-max-height="false">
          <icon-more class="card-extra-icon" />
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
      </a-space>
    </template>
  </card-list>
</template>

<script lang="ts">
  export default {
    name: 'monitorHostCardList'
  };
</script>

<script lang="ts" setup>
  import type { MonitorHostQueryRequest, MonitorHostQueryResponse } from '@/api/monitor/monitor-host';
  import { useCardPagination, useCardColLayout, useCardFieldConfig } from '@/hooks/card';
  import { computed, reactive, ref, onMounted, } from 'vue';
  import useLoading from '@/hooks/loading';
  import { dateFormat, objectTruthKeyCount, resetObject, dataColor } from '@/utils';
  import fieldConfig from '../types/card.fields';
  import { TableName, AlarmSwitchKey, OnlineStatusKey, InstallStatusKey, AgentLogStatus, AgentLogStatusKey } from '../types/const';
  import { getMonitorHostPage } from '@/api/monitor/monitor-host';
  import { useDictStore } from '@/store';
  import { AgentInstallStatus, tagColor } from '@/views/asset/host-list/types/const';
  import { copy } from '@/hooks/copy';
  import { getFileSize } from '@/utils/file';
  import { openNewRoute } from '@/router';
  import { getPercentProgressColor } from '@/utils/charts';
  import useMonitorHostList from '../types/use-monitor-host-list';
  import MonitorCell from './monitor-cell.vue';
  import UserSelector from '@/components/user/user/selector/index.vue';

  const emits = defineEmits(['openUpdate', 'openUpload', 'toPolicy']);

  const cardColLayout = useCardColLayout();
  const pagination = useCardPagination();
  const { loading, setLoading } = useLoading();
  const { cardFieldConfig, fieldsHook } = useCardFieldConfig(TableName, fieldConfig);
  const { toOptions, getDictValue, toggleDictValue } = useDictStore();

  const renderList = ref<Array<MonitorHostQueryResponse>>([]);
  const formRef = ref();
  const formModel = reactive<MonitorHostQueryRequest>({
    searchValue: undefined,
    alarmSwitch: undefined,
    ownerUserId: undefined,
    policyId: undefined,
    name: undefined,
    code: undefined,
    address: undefined,
    agentInstallStatus: AgentInstallStatus.INSTALLED,
    agentOnlineStatus: undefined,
    description: undefined,
    tags: undefined,
  });

  // 条件数量
  const filterCount = computed(() => {
    return objectTruthKeyCount(formModel, ['searchValue']);
  });

  // 重新加载
  const reload = () => {
    // 重新加载数据
    fetchCardData();
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
    hosts: renderList,
    setLoading,
    reload,
  });

  // 重置条件
  const reset = () => {
    resetObject(formModel);
    fetchCardData();
  };

  // 加载数据
  const doFetchCardData = async (request: MonitorHostQueryRequest) => {
    try {
      setLoading(true);
      const { data } = await getMonitorHostPage(request);
      renderList.value = data.rows;
      pagination.total = data.total;
      pagination.current = request.page;
      pagination.pageSize = request.limit;
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 切换页码
  const fetchCardData = (page = 1, limit = pagination.pageSize, form = formModel) => {
    doFetchCardData({ page, limit, ...form });
  };

  onMounted(fetchCardData);

</script>

<style lang="less" scoped>
  .network-inline {
    b {
      width: 100px;
      display: inline-block;
    }
  }
</style>
