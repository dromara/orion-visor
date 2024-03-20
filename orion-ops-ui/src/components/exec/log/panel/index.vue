<template>
  <div class="log-panel-container" v-if="command">
    <!-- 执行主机 -->
    <exec-host class="exec-host-container"
               :visibleBack="visibleBack"
               :current="currentHostExecId"
               :hosts="command.hosts"
               @selected="selectedHost"
               @back="emits('back')" />
    <!-- 日志容器 -->
    <log-view ref="logView"
              class="log-view-container"
              :current="currentHostExecId"
              :command="command" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'execLogPanel'
  };
</script>

<script lang="ts" setup>
  import type { ExecLogQueryResponse } from '@/api/exec/exec-log';
  import { onUnmounted, ref, nextTick } from 'vue';
  import { getExecLogStatus } from '@/api/exec/exec-log';
  import { execHostStatus, execStatus } from './const';
  import ExecHost from './exec-host.vue';
  import LogView from './log-view.vue';

  const props = defineProps<{
    visibleBack: boolean
  }>();

  const emits = defineEmits(['back']);

  const logView = ref();
  const currentHostExecId = ref();
  const statusIntervalId = ref();
  const finishIntervalId = ref();
  const command = ref<ExecLogQueryResponse>();

  // 打开
  const open = (record: ExecLogQueryResponse) => {
    command.value = record;
    currentHostExecId.value = record.hosts[0].id;
    // 定时查询执行状态
    if (record.status === execStatus.WAITING ||
      record.status === execStatus.RUNNING) {
      // 注册状态轮询
      statusIntervalId.value = setInterval(fetchTaskStatus, 5000);
      // 注册完成时间轮询
      finishIntervalId.value = setInterval(setTaskFinishTime, 1000);
    }
    // 打开日志
    nextTick(() => {
      logView.value?.open();
    });
  };

  // 加载状态
  const fetchTaskStatus = async () => {
    if (!command.value) {
      return;
    }
    // 加载状态
    const { data: { logList, hostList } } = await getExecLogStatus([command.value.id]);
    if (logList.length) {
      command.value.status = logList[0].status;
      command.value.startTime = logList[0].startTime;
      command.value.finishTime = logList[0].finishTime;
    }
    // 设置主机状态
    for (let host of command.value.hosts) {
      const hostStatus = hostList.find(s => s.id === host.id);
      if (hostStatus) {
        host.status = hostStatus.status;
        host.startTime = hostStatus.startTime;
        if (hostStatus.finishTime) {
          host.finishTime = hostStatus.finishTime;
        }
        host.exitStatus = hostStatus.exitStatus;
        host.errorMessage = hostStatus.errorMessage;
      }
    }
    // 已完成跳过
    if (command.value.status === execStatus.COMPLETED ||
      command.value.status === execStatus.FAILED) {
      closeClient();
    }
  };

  // 设置完成时间
  const setTaskFinishTime = () => {
    const hosts = command.value?.hosts;
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
    // 关闭日志
    logView.value?.closeClient();
    // 清理轮询
    clearAllInterval();
  };

  // 清理并且关闭
  const closeAll = () => {
    // 关闭日志
    logView.value?.closeAll();
    // 清理轮询
    clearAllInterval();
  };

  // 清理轮询
  const clearAllInterval = () => {
    // 关闭状态轮询
    clearInterval(statusIntervalId.value);
    // 关闭使用时间轮询
    clearInterval(finishIntervalId.value);
  };

  onUnmounted(closeAll);

</script>

<style lang="less" scoped>
  @host-width: 420px;
  @host-real-width: @host-width + 16px;

  .log-panel-container {
    width: calc(100% - 32px);
    height: calc(100% - 32px);
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
