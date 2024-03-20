<template>
  <div class="layout-container full">
    <!-- 执行面板 -->
    <exec-panel v-show="!logVisible"
                @submit="openLog" />
    <!-- 执行日志 -->
    <exec-log-panel v-if="logVisible"
                    ref="log"
                    :visibleBack="true"
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
  import ExecLogPanel from '@/components/exec/log/panel/index.vue';

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
