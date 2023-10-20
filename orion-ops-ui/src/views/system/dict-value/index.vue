<template>
  <div class="layout-container">
    <!-- 列表-表格 -->
    <dict-value-table ref="table"
                  @openAdd="() => modal.openAdd()"
                  @openUpdate="(e) => modal.openUpdate(e)" />
    <!-- 添加修改模态框 -->
    <dict-value-form-modal ref="modal"
                   @added="modalAddCallback"
                   @updated="modalUpdateCallback" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'systemDictValue'
  };
</script>

<script lang="ts" setup>
  import DictValueTable from './components/dict-value-table.vue';
  import DictValueFormModal from './components/dict-value-form-modal.vue';

  import { ref } from 'vue';

  const table = ref();
  const modal = ref();

  // 添加回调
  const modalAddCallback = () => {
    table.value.addedCallback();
  };

  // 修改回调
  const modalUpdateCallback = () => {
    table.value.updatedCallback();
  };

</script>

<style lang="less" scoped>

</style>
