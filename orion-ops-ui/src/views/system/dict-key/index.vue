<template>
  <div class="layout-container">
    <!-- 列表-表格 -->
    <dict-key-table ref="table"
                    @openAdd="() => modal.openAdd()"
                    @openUpdate="(e) => modal.openUpdate(e)" />
    <!-- 添加修改模态框 -->
    <dict-key-form-modal ref="modal"
                         @added="modalAddCallback"
                         @updated="modalUpdateCallback" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'systemDictKey'
  };
</script>

<script lang="ts" setup>
  import { ref } from 'vue';
  import DictKeyTable from './components/dict-key-table.vue';
  import DictKeyFormModal from './components/dict-key-form-modal.vue';

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
