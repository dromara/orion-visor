<template>
  <div class="layout-container" v-if="render">
    <!-- 列表-表格 -->
    <alarm-event-table :source-type="AlarmSourceType.HOST" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'alarmEvent'
  };
</script>

<script lang="ts" setup>
  import { ref, onBeforeMount } from 'vue';
  import { useDictStore, useCacheStore } from '@/store';
  import { dictKeys, AlarmSourceType } from './types/const';
  import AlarmEventTable from './components/alarm-event-table.vue';

  const render = ref(false);

  onBeforeMount(async () => {
    const cacheStore = useCacheStore();
    // 加载告警策略
    await cacheStore.loadMonitorAlarmPolicy();
    // 加载指标列表
    await cacheStore.loadMonitorMetricsList();
    // 加载字典值
    const dictStore = useDictStore();
    await dictStore.loadKeys(dictKeys);
    render.value = true;
  });

</script>

<style lang="less" scoped>

</style>
