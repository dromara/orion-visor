<template>
  <div class="log-panel-container" v-if="execLog && appender">
    <!-- 执行主机 -->
    <exec-host class="exec-host-container"
               :visibleBack="visibleBack"
               :current="currentHostExecId"
               :hosts="execLog.hosts as any"
               @selected="selectedHost"
               @back="emits('back')" />
    <!-- 日志容器 -->
    <log-view ref="logViewRef"
              class="log-view-container"
              :type="type"
              :current="currentHostExecId"
              :exec-log="execLog"
              :appender="appender"
              @ready="openLog(currentHostExecId)" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'execLogPanel'
  };
</script>

<script lang="ts" setup>
  import type { ExecLogQueryResponse } from '@/api/exec/exec-log';
  import type { ExecType, ILogAppender } from '../const';
  import { onUnmounted, ref, nextTick, onMounted, markRaw } from 'vue';
  import { getExecCommandLogStatus } from '@/api/exec/exec-command-log';
  import { getExecJobLogStatus } from '@/api/exec/exec-job-log';
  import { dictKeys, ExecHostStatus, ExecStatus } from '../const';
  import { useCacheStore, useDictStore } from '@/store';
  import { toAnonymousNumber } from '@/utils';
  import ExecHost from './exec-host.vue';
  import LogView from './log-view.vue';
  import LogAppender from './log-appender';

  const props = defineProps<{
    visibleBack: boolean;
    type: ExecType;
  }>();

  const emits = defineEmits(['back']);

  const logViewRef = ref();
  const currentHostExecId = ref(0);
  const pullIntervalId = ref();
  const execLog = ref<ExecLogQueryResponse>();
  const appender = ref<ILogAppender>();

  // 打开
  const open = async (record: ExecLogQueryResponse) => {
    execLog.value = { ...record };
    currentHostExecId.value = record.hosts[0].id;
    // 获取最大显示行数
    const { 'log.web-scroll-lines': webScrollLines } = await useCacheStore().loadSystemSetting();
    const scrollLines = toAnonymousNumber(webScrollLines) || 1000;
    // 创建 appender
    appender.value = markRaw(new LogAppender({
      id: record.id,
      type: props.type,
      scrollLines,
    }));
    // 定时查询执行状态
    if (record.status === ExecStatus.WAITING ||
      record.status === ExecStatus.RUNNING) {
      // 等待一秒后先查询一下状态
      setTimeout(pullExecStatus, 1000);
      // 注册状态轮询
      pullIntervalId.value = window.setInterval(pullExecStatus, 5000);
    }
    // 打开日志
    nextTick(() => {
      logViewRef.value?.open();
    });
  };

  // 加载状态
  const pullExecStatus = async () => {
    if (!execLog.value) {
      return;
    }
    // 加载状态
    let statusGetter;
    if (props.type === 'BATCH') {
      // 批量执行日志状态
      statusGetter = getExecCommandLogStatus([execLog.value.id]);
    } else {
      // 计划任务日志状态
      statusGetter = getExecJobLogStatus([execLog.value.id]);
    }
    const { data: { logList, hostList } } = await statusGetter;
    if (logList.length) {
      execLog.value.status = logList[0].status;
      execLog.value.startTime = logList[0].startTime;
      execLog.value.finishTime = logList[0].finishTime;
    }
    // 设置主机状态
    for (let hostRow of hostList) {
      const execLogHost = execLog.value.hosts.find(s => s.id === hostRow.id);
      if (execLogHost) {
        execLogHost.status = hostRow.status;
        execLogHost.startTime = hostRow.startTime;
        // 结束时间绑定了使用时间 如果未完成则使用当前时间
        execLogHost.finishTime = hostRow.finishTime || Date.now();
        execLogHost.exitCode = hostRow.exitCode;
        execLogHost.errorMessage = hostRow.errorMessage;
      }
      // 当前选中主机非等待状态则打开日志
      if (hostRow.id === currentHostExecId.value) {
        openLog(hostRow.id);
      }
    }
    // 已完成关闭轮询
    if (execLog.value.status === ExecStatus.COMPLETED ||
      execLog.value.status === ExecStatus.FAILED) {
      clearAllInterval();
    }
  };

  defineExpose({ open });

  // 选中主机
  const selectedHost = (id: number) => {
    currentHostExecId.value = id;
    // 打开日志
    openLog(id);
  };

  // 打开日志
  const openLog = (id: number) => {
    // 获取状态
    const status = execLog.value?.hosts.find(s => s.id === id)?.status;
    if (status && status !== ExecHostStatus.WAITING) {
      // 打开日志
      appender.value?.openLog(id);
    }
  };

  // 清理并且关闭
  const closeAll = () => {
    // 清理轮询
    clearAllInterval();
    // 关闭 appender
    appender.value?.close();
  };

  // 清理轮询
  const clearAllInterval = () => {
    // 关闭状态轮询
    clearInterval(pullIntervalId.value);
  };

  // 加载字典值
  onMounted(async () => {
    const dictStore = useDictStore();
    await dictStore.loadKeys(dictKeys);
  });

  onUnmounted(closeAll);

</script>

<style lang="less" scoped>
  @host-width: 380px;
  @host-real-width: @host-width + 16px;

  .log-panel-container {
    width: 100%;
    height: 100%;
    display: flex;
    position: absolute;
  }

  .exec-host-container, .log-view-container {
    height: 100%;
    border-radius: 4px;
    position: relative;
    background: var(--color-bg-2);
  }

  .exec-host-container {
    width: @host-width;
    padding: 16px;
    margin-right: 16px;
  }

  .log-view-container {
    width: calc(100% - @host-real-width);
  }

</style>
