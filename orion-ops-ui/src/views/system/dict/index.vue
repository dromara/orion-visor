<template>
  <div class="layout-container">
    <!-- 列表-表格 -->
    <dict-value-table ref="table"
                      @openAddKey="() => keyModal.openAdd()"
                      @openValueHistory="() => keyModal.openAdd()"
                      @openAddValue="() => valueModal.openAdd()"
                      @openUpdateValue="(e) => valueModal.openUpdate(e)" />
    <!-- 添加修改 配置项模态框 -->
    <dict-key-form-modal ref="keyModal"
                         @added="keyModalAddCallback"
                         @updated="keyModalUpdateCallback" />
    <!-- 添加修改 配置值模态框 -->
    <dict-value-form-modal ref="valueModal"
                           @added="valueModalAddCallback"
                           @updated="valueModalUpdateCallback" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'systemDict'
  };
</script>

<script lang="ts" setup>
  import DictValueTable from './components/dict-value-table.vue';
  import DictValueFormModal from './components/dict-value-form-modal.vue';
  import DictKeyFormModal from './components/dict-key-form-modal.vue';

  import { ref } from 'vue';

  const table = ref();
  const keyModal = ref();
  const valueModal = ref();

  // 配置项添加回调
  const keyModalAddCallback = () => {
    table.value.addedCallback();
  };

  // 配置项修改回调
  const keyModalUpdateCallback = () => {
    table.value.updatedCallback();
  };

  // 配置值添加回调
  const valueModalAddCallback = () => {
    table.value.addedCallback();
  };

  // 配置值修改回调
  const valueModalUpdateCallback = () => {
    table.value.updatedCallback();
  };

</script>

<style lang="less" scoped>

</style>
