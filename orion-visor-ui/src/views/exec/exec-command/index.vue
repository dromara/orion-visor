<template>
  <div class="layout-container full">
    <!-- 执行面板 -->
    <div v-show="!logVisible" class="panel-wrapper">
      <exec-command-panel @submit="openLog" />
    </div>
    <!-- 执行日志 -->
    <div v-if="logVisible" class="panel-wrapper">
      <exec-log-panel ref="log"
                      type="BATCH"
                      :visibleBack="true"
                      @back="setLogVisible(false)" />
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'execCommand'
  };
</script>

<script lang="ts" setup>
  import type { ExecLogQueryResponse } from '@/api/exec/exec-log';
  import { nextTick, onMounted, ref } from 'vue';
  import useVisible from '@/hooks/visible';
  import { useDictStore } from '@/store';
  import { dictKeys } from '@/components/exec/log/const';
  import { useRoute } from 'vue-router';
  import { getExecCommandLog } from '@/api/exec/exec-command-log';
  import ExecCommandPanel from './components/exec-command-panel.vue';
  import ExecLogPanel from '@/components/exec/log/panel/index.vue';

  const { visible: logVisible, setVisible: setLogVisible } = useVisible();
  const route = useRoute();

  const log = ref();

  // 打开日志
  const openLog = (record: ExecLogQueryResponse) => {
    setLogVisible(true);
    nextTick(() => {
      log.value.open(record);
    });
  };

  // 打开日志
  const openLogWithId = async (id: number) => {
    setLogVisible(true);
    // 查询日志
    const { data } = await getExecCommandLog(id);
    openLog(data);
  };

  // 加载字典值
  onMounted(async () => {
    const dictStore = useDictStore();
    await dictStore.loadKeys(dictKeys);
  });

  // 跳转日志
  onMounted(async () => {
    const idParam = route.query.id as string;
    const keyParam = route.query.key as string;
    if (idParam) {
      await openLogWithId(Number.parseInt(idParam));
    } else if (keyParam) {
      await openLogWithId(Number.parseInt(keyParam));
    }
  });

</script>

<style lang="less" scoped>

  .panel-wrapper {
    width: 100%;
    height: 100%;
    position: relative;
  }

</style>
