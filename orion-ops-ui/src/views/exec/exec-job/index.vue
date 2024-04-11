<template>
  <div class="layout-container" v-if="render">
    <!-- 列表-表格 -->
    <exec-job-table ref="table"
                    @open-add="() => drawer.openAdd()"
                    @open-update="(e) => drawer.openUpdate(e)"
                    @open-detail="(e) => detail.open(e)"
                    @test-cron="openNextCron" />
    <!-- 添加修改模态框 -->
    <exec-job-form-drawer ref="drawer"
                          @added="modalAddCallback"
                          @updated="modalUpdateCallback"
                          @open-host="(e) => hostModal.open(e)"
                          @test-cron="openNextCron" />
    <!-- 详情模态框 -->
    <exec-job-detail-drawer ref="detail" />
    <!-- cron 执行时间模态框 -->
    <next-cron-modal ref="nextCron" />
    <!-- 主机模态框 -->
    <authorized-host-modal ref="hostModal"
                           @selected="(e) => drawer.setSelectedHost(e)" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'execJob'
  };
</script>

<script lang="ts" setup>
  import { ref, onBeforeMount } from 'vue';
  import { useDictStore } from '@/store';
  import { CronNextTimes, dictKeys } from './types/const';
  import ExecJobTable from './components/exec-job-table.vue';
  import ExecJobFormDrawer from './components/exec-job-form-drawer.vue';
  import ExecJobDetailDrawer from './components/exec-job-detail-drawer.vue';
  import AuthorizedHostModal from '@/components/asset/host/authorized-host-modal/index.vue';
  import NextCronModal from '@/components/meta/expression/next-cron-modal/index.vue';

  const render = ref(false);
  const table = ref();
  const drawer = ref();
  const detail = ref();
  const nextCron = ref();
  const hostModal = ref();

  // 添加回调
  const modalAddCallback = () => {
    table.value.addedCallback();
  };

  // 修改回调
  const modalUpdateCallback = () => {
    table.value.updatedCallback();
  };

  // 打开下次执行时间
  const openNextCron = (cron: string) => {
    nextCron.value.open({ expression: cron, times: CronNextTimes });
  };

  onBeforeMount(async () => {
    const dictStore = useDictStore();
    await dictStore.loadKeys(dictKeys);
    render.value = true;
  });

</script>

<style lang="less" scoped>

</style>
