<template>
  <div class="log-panel-container" v-if="execLog && appender">
    <!-- 执行主机 -->
    <exec-host class="exec-host-container"
               :visibleBack="visibleBack"
               :current="currentHostExecId"
               :hosts="execLog.hosts"
               @selected="selectedHost"
               @back="emits('back')" />
    <!-- 日志容器 -->
    <log-view ref="logViewRef"
              class="log-view-container"
              :type="type"
              :current="currentHostExecId"
              :exec-log="execLog"
              :appender="appender" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'execLogPanel'
  };
</script>

<script lang="ts" setup>
  import type { ExecLogQueryResponse } from '@/api/exec/exec-log';
  import type { ILogAppender } from './appender-const';
  import type { ExecType } from '../const';
  import { onUnmounted, ref, nextTick, onMounted } from 'vue';
  import { getExecCommandLogStatus } from '@/api/exec/exec-command-log';
  import { getExecJobLogStatus } from '@/api/job/exec-job-log';
  import { dictKeys, execHostStatus, execStatus } from '../const';
  import { useDictStore } from '@/store';
  import ExecHost from './exec-host.vue';
  import LogView from './log-view.vue';
  import LogAppender from './log-appender';

  const props = defineProps<{
    visibleBack: boolean;
    type: ExecType;
  }>();

  const emits = defineEmits(['back']);

  const logViewRef = ref();
  const currentHostExecId = ref();
  const statusIntervalId = ref();
  const execLog = ref<ExecLogQueryResponse>();
  const appender = ref<ILogAppender>();

  // 打开
  const open = (record: ExecLogQueryResponse) => {
    appender.value = new LogAppender(props.type, { execId: record.id });
    execLog.value = record;
    currentHostExecId.value = record.hosts[0].id;
    // 定时查询执行状态
    if (record.status === execStatus.WAITING ||
      record.status === execStatus.RUNNING) {
      // 等待一秒后先查询一下状态
      setTimeout(fetchTaskStatus, 1000);
      // 注册状态轮询
      statusIntervalId.value = setInterval(fetchTaskStatus, 5000);
    }
    // 打开日志
    nextTick(() => {
      logViewRef.value?.open();
    });
  };

  // 加载状态
  const fetchTaskStatus = async () => {
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
    for (let host of execLog.value.hosts) {
      const hostStatus = hostList.find(s => s.id === host.id);
      if (hostStatus) {
        host.status = hostStatus.status;
        host.startTime = hostStatus.startTime;
        // 结束时间绑定了使用时间 如果未完成则使用当前时间
        host.finishTime = hostStatus.finishTime || Date.now();
        host.exitStatus = hostStatus.exitStatus;
        host.errorMessage = hostStatus.errorMessage;
      }
    }
    // 已完成跳过
    if (execLog.value.status === execStatus.COMPLETED ||
      execLog.value.status === execStatus.FAILED) {
      closeClient();
    }
  };

  // 设置完成时间
  const setTaskFinishTime = () => {
    const hosts = execLog.value?.hosts;
    if (!hosts) {
      return;
    }
    hosts.forEach(s => {
      // 未完成自动设置完成时间为当前时间 用于展示使用时间
      if (s.status === execHostStatus.WAITING ||
        s.status === execHostStatus.RUNNING) {
        if (!s.startTime) {
          s.startTime = Date.now();
        }
        s.finishTime = Date.now();
      }
    });
  };

  defineExpose({ open });

  // 选中主机
  const selectedHost = (hostId: number) => {
    currentHostExecId.value = hostId;
  };

  // 关闭连接
  const closeClient = () => {
    // 清理轮询
    clearAllInterval();
    // 关闭 client
    appender.value?.closeClient();
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
    clearInterval(statusIntervalId.value);
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
