<template>
  <div class="layout-container" v-if="render">
    <!-- 列表-表格 -->
    <alarm-rule-table ref="table"
                      @open-add="(e: any) => drawer.openAdd(e)"
                      @open-copy="(e: any) => drawer.openCopy(e)"
                      @open-update="(e: any) => drawer.openUpdate(e)" />
    <!-- 添加修改抽屉 -->
    <alarm-rule-form-drawer ref="drawer"
                            @added="reload"
                            @updated="reload" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'alarmRule'
  };
</script>

<script lang="ts" setup>
  import { ref, onBeforeMount } from 'vue';
  import { useDictStore, useCacheStore } from '@/store';
  import { dictKeys } from './types/const';
  import AlarmRuleTable from './components/alarm-rule-table.vue';
  import AlarmRuleFormDrawer from './components/alarm-rule-form-drawer.vue';

  const render = ref(false);
  const table = ref();
  const drawer = ref();

  // 重新加载
  const reload = () => {
    table.value.reload();
  };

  onBeforeMount(async () => {
    // 加载规则列表
    await useCacheStore().loadMonitorMetricsList();
    // 加载字典值
    const dictStore = useDictStore();
    await dictStore.loadKeys(dictKeys);
    render.value = true;
  });

</script>

<style lang="less" scoped>

</style>
