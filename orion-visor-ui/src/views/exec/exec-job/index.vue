<template>
  <div class="layout-container" v-if="render">
    <!-- 列表-表格 -->
    <exec-job-table ref="table"
                    @open-add="() => drawer.openAdd()"
                    @open-update="(e) => drawer.openUpdate(e)"
                    @open-detail="(e) => detail.open(e)"
                    @update-exec-user="(e) => execUserModal.open(e)"
                    @test-cron="openNextCron" />
    <!-- 添加修改模态框 -->
    <exec-job-form-drawer ref="drawer"
                          @added="() => table.reload()"
                          @updated="() => table.reload()"
                          @open-host="(e) => hostModal.open(e)"
                          @open-template="() => templateModal.open()"
                          @gen-cron="(e) => genModal.open(e)"
                          @test-cron="openNextCron" />
    <!-- 任务详情模态框 -->
    <exec-job-detail-drawer ref="detail" />
    <!-- 修改执行用户模态框 -->
    <exec-user-update-modal ref="execUserModal" />
    <!-- cron 执行时间模态框 -->
    <cron-next-modal ref="nextCron" />
    <!-- cron 生成模态框 -->
    <cron-generator-modal ref="genModal"
                          @ok="(e) => drawer.setExpression(e)" />
    <!-- 执行模板模态框 -->
    <exec-template-modal ref="templateModal"
                         @selected="(e) => drawer.setWithTemplate(e)" />
    <!-- 主机模态框 -->
    <authorized-host-modal ref="hostModal"
                           type="SSH"
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
  import ExecUserUpdateModal from './components/exec-user-update-modal.vue';
  import AuthorizedHostModal from '@/components/asset/host/authorized-host-modal/index.vue';
  import ExecTemplateModal from '@/components/exec/template/modal/index.vue';
  import CronNextModal from '@/components/meta/cron/next-modal/index.vue';
  import CronGeneratorModal from '@/components/meta/cron/generator-model/index.vue';

  const render = ref(false);
  const table = ref();
  const drawer = ref();
  const detail = ref();
  const execUserModal = ref();
  const nextCron = ref();
  const genModal = ref();
  const templateModal = ref();
  const hostModal = ref();

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
