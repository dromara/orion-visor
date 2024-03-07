<template>
  <div class="layout-container" v-if="render">
    <!-- 列表-表格 -->
    <dict-key-table ref="table"
                    @openAdd="() => modal.openAdd()"
                    @openUpdate="(e) => modal.openUpdate(e)"
                    @openView="(v, t) => view.open(v, t)" />
    <!-- 添加修改模态框 -->
    <dict-key-form-modal ref="modal"
                         @added="modalAddCallback"
                         @updated="modalUpdateCallback" />
    <!-- json 查看器模态框 -->
    <json-editor-modal ref="view" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'dictKey'
  };
</script>

<script lang="ts" setup>
  import { ref, onBeforeMount } from 'vue';
  import { useDictStore } from '@/store';
  import { dictKeys } from './types/const';
  import DictKeyTable from './components/dict-key-table.vue';
  import DictKeyFormModal from './components/dict-key-form-modal.vue';
  import JsonEditorModal from '@/components/view/json-editor/modal/index.vue';

  const render = ref(false);
  const table = ref();
  const modal = ref();
  const view = ref();

  // 添加回调
  const modalAddCallback = () => {
    table.value.addedCallback();
  };

  // 修改回调
  const modalUpdateCallback = () => {
    table.value.updatedCallback();
  };

  onBeforeMount(async () => {
    const dictStore = useDictStore();
    await dictStore.loadKeys(dictKeys);
    render.value = true;
  });

</script>

<style lang="less" scoped>

</style>
