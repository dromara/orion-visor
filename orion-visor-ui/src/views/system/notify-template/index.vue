<template>
  <div class="layout-container" v-if="render">
    <!-- 列表-表格 -->
    <notify-template-table ref="table"
                           @open-add="() => drawer.openAdd()"
                           @open-copy="(e: any) => drawer.openCopy(e)"
                           @open-update="(e: any) => drawer.openUpdate(e)" />
    <!-- 添加修改抽屉 -->
    <notify-template-form-drawer ref="drawer"
                                 @added="reload"
                                 @updated="reload" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'notifyTemplate'
  };
</script>

<script lang="ts" setup>
  import { ref, onBeforeMount } from 'vue';
  import { useDictStore } from '@/store';
  import { dictKeys } from './types/const';
  import NotifyTemplateTable from './components/notify-template-table.vue';
  import NotifyTemplateFormDrawer from './components/notify-template-form-drawer.vue';

  const render = ref(false);
  const table = ref();
  const drawer = ref();

  // 重新加载
  const reload = () => {
    table.value.reload();
  };

  onBeforeMount(async () => {
    const dictStore = useDictStore();
    await dictStore.loadKeys(dictKeys);
    render.value = true;
  });

</script>

<style lang="less" scoped>

</style>
