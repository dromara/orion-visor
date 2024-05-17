<template>
  <div class="layout-container" v-if="render">
    <!-- 表格 -->
    <menu-table ref="table"
                @open-add="(e) => modal.openAdd(e)"
                @open-update="(e) => modal.openUpdate(e)" />
    <!-- 添加修改模态框 -->
    <menu-form-modal ref="modal"
                     @added="() => table.addedCallback()"
                     @updated="() => table.updatedCallback()" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'systemMenu'
  };
</script>

<script lang="ts" setup>
  import MenuTable from '@/views/system/menu/components/menu-table.vue';
  import MenuFormModal from '@/views/system/menu/components/menu-form-modal.vue';
  import { ref, onBeforeMount, onUnmounted } from 'vue';
  import { useCacheStore, useDictStore } from '@/store';
  import { dictKeys } from './types/const';

  const table = ref();
  const modal = ref();
  const render = ref(false);

  // 加载字典项
  onBeforeMount(async () => {
    const dictStore = useDictStore();
    await dictStore.loadKeys(dictKeys);
    render.value = true;
  });

  // 卸载时清除 cache
  onUnmounted(() => {
    const cacheStore = useCacheStore();
    cacheStore.reset('menus');
  });

</script>
