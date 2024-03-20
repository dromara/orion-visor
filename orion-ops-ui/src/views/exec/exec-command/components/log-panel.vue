<template>
  <div class="log-panel-container" v-if="command">
    <!-- 执行主机 -->
    <log-panel-host class="host-container"
                    :current="currentHostExecId"
                    :hosts="command.hosts"
                    @selected="selectedHost"
                    @back="emits('back')" />
    <!-- 日志容器 -->
    <log-panel-view ref="logContainer"
                    class="log-container"
                    :current="currentHostExecId"
                    :command="command" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'logPanel'
  };
</script>

<script lang="ts" setup>
  import type { ExecCommandResponse } from '@/api/exec/exec';
  import { onUnmounted, ref, nextTick } from 'vue';
  import { getExecLogStatus } from '@/api/exec/exec-log';
  import { execHostStatus, execStatus } from '@/views/exec/exec-log/types/const';
  import LogPanelHost from './log-panel-host.vue';
  import LogPanelView from './log-panel-view.vue';

  const emits = defineEmits(['back']);

  const logContainer = ref();
  const currentHostExecId = ref();
  const statusIntervalId = ref();
  const finishIntervalId = ref();
  const command = ref<ExecCommandResponse>();

  // 打开
  const open = (record: ExecCommandResponse) => {
    command.value = record;
    currentHostExecId.value = record.hosts[0].id;
    // 注册状态轮询
    statusIntervalId.value = setInterval(fetchTaskStatus, 5000);
    // 注册完成时间轮询
    finishIntervalId.value = setInterval(setTaskFinishTime, 1000);
    // 打开日志
    nextTick(() => {
      logContainer.value?.open();
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
        host.finishTime = hostStatus.finishTime;
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
    logContainer.value?.closeClient();
    // 清理轮询
    clearAllInterval();
  };

  // 清理并且关闭
  const closeAll = () => {
    // 关闭日志
    logContainer.value?.closeAll();
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
    width: 100%;
    height: 100%;
    display: flex;
  }

  .host-container, .log-container {
    height: 100%;
    border-radius: 4px;
    position: relative;
    background: var(--color-bg-2);
  }

  .host-container {
    width: @host-width;
    padding: 16px;
    margin-right: 16px;
  }

  .log-container {
    width: calc(100% - @host-real-width);
  }

</style>
