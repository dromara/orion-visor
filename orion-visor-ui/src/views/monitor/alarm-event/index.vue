<template>
  <div class="layout-container" v-if="render">
    <!-- 列表-表格 -->
    <alarm-event-table ref="table"
                       @open-handle="(e: any) => handleModal.open(e)"
                       @open-clear="(e: any) => clearModal.open(e)" />
    <!-- 处理模态框-->
    <alarm-event-handle-modal ref="handleModal"
                              @handled="(e: any) => table.alarmHandled(e)" />
    <!-- 清理模态框-->
    <alarm-event-clear-modal ref="clearModal"
                             @clear="() => table.reload()" />
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
  import { dictKeys } from './types/const';
  import AlarmEventTable from './components/alarm-event-table.vue';
  import AlarmEventClearModal from './components/alarm-event-clear-modal.vue';
  import AlarmEventHandleModal from './components/alarm-event-handle-modal.vue';

  const render = ref(false);
  const table = ref();
  const handleModal = ref();
  const clearModal = ref();

  onBeforeMount(async () => {
    const cacheStore = useCacheStore();
    await cacheStore.loadMonitorAlarmPolicy();
    await cacheStore.loadMonitorMetricsList();
    const dictStore = useDictStore();
    await dictStore.loadKeys(dictKeys);
    render.value = true;
  });

</script>

<style lang="less" scoped>

</style>
