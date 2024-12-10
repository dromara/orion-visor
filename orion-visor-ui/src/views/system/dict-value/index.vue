<template>
  <div class="layout-container">
    <!-- 列表-表格 -->
    <dict-value-table ref="table"
                      @open-add="() => modal.openAdd()"
                      @open-update="(e) => modal.openUpdate(e)"
                      @open-history="(e) => history.open(e.id, e.label)" />
    <!-- 添加修改模态框 -->
    <dict-value-form-modal ref="modal"
                           @added="() => table.reload()"
                           @updated="() => table.reload()" />
    <!-- 历史值模态框 -->
    <history-value-modal ref="history"
                         :type="historyType"
                         :rollback="rollback"
                         @updated="() => table.reload()" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'dictValue'
  };
</script>

<script lang="ts" setup>
  import { ref, onUnmounted } from 'vue';
  import { historyType } from './types/const';
  import { useCacheStore } from '@/store';
  import { rollbackDictValue } from '@/api/system/dict-value';
  import DictValueTable from './components/dict-value-table.vue';
  import DictValueFormModal from './components/dict-value-form-modal.vue';
  import HistoryValueModal from '@/components/meta/history/modal/index.vue';

  const table = ref();
  const modal = ref();
  const history = ref();
  const cacheStore = useCacheStore();

  // 回滚
  const rollback = async (id: number, valueId: number) => {
    await rollbackDictValue({ id, valueId });
  };

  // 卸载时清除 cache
  onUnmounted(() => {
    cacheStore.reset('dictKeys');
  });

</script>

<style lang="less" scoped>
</style>
