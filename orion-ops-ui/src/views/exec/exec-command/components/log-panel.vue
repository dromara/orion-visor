<template>
  <div class="log-panel-container">
    <!-- 执行主机 -->
    <log-panel-host class="host-container"
                    :current="currentHostId"
                    :hosts="command.hosts"
                    @selected="selectedHost"
                    @back="back" />
    <!-- 日志容器 -->
    <div class="log-container">
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'logPanel'
  };
</script>

<script lang="ts" setup>
  import type { ExecCommandResponse } from '@/api/exec/exec';
  import { ref } from 'vue';
  import 'xterm/css/xterm.css';
  import LogPanelHost from './log-panel-host.vue';

  const emits = defineEmits(['back']);

  const currentHostId = ref(1);
  const command = ref<ExecCommandResponse>({
    id: 50,
    hosts: [{
      id: 76,
      hostId: 1,
      hostName: 'main-11',
      hostAddress: '192.412.53.2',
      status: 'WAITING'
    }, {
      id: 77,
      hostId: 2,
      hostName: 'main-22',
      hostAddress: '192.412.53.2',
      status: 'RUNNING'
    }, {
      id: 78,
      hostId: 3,
      hostName: 'main-33',
      hostAddress: '192.412.53.2',
      status: 'COMPLETED'
    }, {
      id: 79,
      hostId: 4,
      hostName: 'main-44',
      hostAddress: '192.412.53.2',
      status: 'FAILED'
    }, {
      id: 80,
      hostId: 5,
      hostName: 'main-55',
      hostAddress: '192.412.53.2',
      status: 'INTERRUPTED'
    }]
  } as ExecCommandResponse);

  // 打开
  const open = (record: ExecCommandResponse) => {
    command.value = record;
    currentHostId.value = record.hosts[0].hostId;
    // 打开日志
    openLog();
  };

  defineExpose({ open });

  // 打开日志
  const openLog = () => {

  };

  // 选中主机
  const selectedHost = (hostId: number) => {
    currentHostId.value = hostId;
  };

  // 返回
  const back = () => {
    emits('back');
    // 清理
    clear();
  };

  // 清理
  const clear = () => {

  };

  // TODO pull status

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
    padding: 16px;
    border-radius: 4px;
    position: relative;
    background: var(--color-bg-2);
  }

  .host-container {
    width: @host-width;
    margin-right: 16px;
  }

  .log-container {
    width: calc(100% - @host-real-width);
  }

</style>
