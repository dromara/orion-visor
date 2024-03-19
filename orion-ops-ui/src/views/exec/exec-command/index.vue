<template>
  <div class="layout-container full">
    <!-- 执行面板 -->
    <exec-panel v-show="!logVisible"
                @submit="openLog" />
    <!-- 执行日志 -->
    <log-panel v-if="logVisible"
               ref="log"
               @back="setLogVisible(false)" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'execCommand'
  };
</script>

<script lang="ts" setup>
  import type { ExecCommandResponse } from '@/api/exec/exec';
  import { nextTick, onMounted, ref } from 'vue';
  import useVisible from '@/hooks/visible';
  import { useDictStore } from '@/store';
  import { dictKeys } from '@/views/exec/exec-log/types/const';
  import ExecPanel from './components/exec-panel.vue';
  import LogPanel from './components/log-panel.vue';

  const { visible: logVisible, setVisible: setLogVisible } = useVisible();
  const { loadKeys } = useDictStore();

  const log = ref();

  // 打开日志
  const openLog = (record: ExecCommandResponse) => {
    setLogVisible(true);
    nextTick(() => {
      log.value.open(record);
    });
  };

  // 加载字典值
  onMounted(async () => {
    await loadKeys(dictKeys);
  });

  onMounted(() => {
    openLog({
      id: 65,
      hosts: [
        {
          id: 103,
          hostId: 5,
          hostName: 'main-55',
          hostAddress: '192.412.53.2',
          status: 'INTERRUPTED'
        }, {
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
          status: 'TIMEOUT'
        },]
    } as ExecCommandResponse);
  });

</script>

<style lang="less" scoped>

  :deep(.panel-header) {
    width: 100%;
    height: 28px;
    margin-bottom: 4px;
    display: flex;
    justify-content: space-between;
    align-items: flex-start;

    h3, > span {
      margin: 0;
      overflow: hidden;
      white-space: nowrap;
    }

    h3 {
      color: var(--color-text-1);
    }
  }

</style>
