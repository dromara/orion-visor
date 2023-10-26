<template>
  <div class="layout-container">
    <!-- 表格 -->
    <menu-table ref="table"
                @openAdd="(e) => modal.openAdd(e)"
                @openUpdate="(e) => modal.openUpdate(e)" />
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

  import { onUnmounted, ref } from 'vue';
  import { useCacheStore, useDictStore } from '@/store';
  import { dictKeys } from './types/const';

  const table = ref();
  const modal = ref();

  // 加载字典项
  useDictStore().loadKeys(dictKeys);

  // 卸载时清除 menu cache
  onUnmounted(() => {
    const cacheStore = useCacheStore();
    cacheStore.set('menus', []);
  });

</script>
