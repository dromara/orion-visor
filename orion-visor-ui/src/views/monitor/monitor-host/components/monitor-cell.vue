<template>
  <div v-if="record.agentInstallStatus === AgentInstallStatus.INSTALLED">
    <!-- 数据列 -->
    <template v-if="dataCell">
      <div v-if="!record.metricsData?.noData"
           class="metrics-wrapper"
           :class="dataClass">
        <slot name="default" />
      </div>
      <span v-else class="nodata">{{ NODATA_TIPS }}</span>
    </template>
    <!-- 非数据列 -->
    <template v-else>
      <slot name="default" />
    </template>
  </div>
  <!-- 未安装则不显示 -->
  <span v-else>-</span>
</template>

<script lang="ts">
  export default {
    name: 'monitorCell'
  };
</script>

<script lang="ts" setup>
  import type { MonitorHostQueryResponse } from '@/api/monitor/monitor-host';
  import { NODATA_TIPS } from '@/views/monitor/monitor-host/types/const';
  import { AgentInstallStatus } from '@/views/asset/host-list/types/const';

  defineProps<{
    dataCell: boolean;
    dataClass?: string;
    record: MonitorHostQueryResponse;
  }>();
</script>

<style lang="less" scoped>
  .nodata {
    color: var(--color-text-3);
    font-size: 12px;
  }

  :deep(.metrics-wrapper) {
    display: flex;
    align-items: center;
    justify-content: space-between;

    &.network {
      flex-direction: column;
      align-items: flex-start !important;
    }

    .metrics-value-per {
      width: 60px;
      text-align: end;
      font-size: 10px;
      font-weight: 600;

      &::after {
        content: '%';
        font-weight: 600;
      }
    }
  }

</style>
