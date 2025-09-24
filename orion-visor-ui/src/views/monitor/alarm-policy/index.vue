<template>
  <div class="layout-container" v-if="render">
    <!-- 列表-表格 -->
    <alarm-policy-table ref="table"
                        @open-add="() => modal.openAdd()"
                        @open-update="(e: any) => modal.openUpdate(e)"
                        @open-copy="(e: any) => modal.openCopy(e)" />
    <!-- 添加修改模态框 -->
    <alarm-policy-form-modal ref="modal"
                             @added="reload"
                             @updated="reload" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'alarmPolicy'
  };
</script>

<script lang="ts" setup>
  import { ref, onBeforeMount } from 'vue';
  import AlarmPolicyTable from './components/alarm-policy-table.vue';
  import AlarmPolicyFormModal from './components/alarm-policy-form-modal.vue';

  const render = ref(false);
  const table = ref();
  const modal = ref();

  // 重新加载
  const reload = () => {
    table.value.reload();
  };

  onBeforeMount(async () => {
    render.value = true;
  });

</script>

<style lang="less" scoped>

</style>
