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

  import { onUnmounted, ref } from 'vue';
  import { useCacheStore } from '@/store';
  import { getDictKeyList } from '@/api/system/dict-key';
  import { Message } from '@arco-design/web-vue';

  const table = ref();
  const modal = ref();
  const cacheStore = useCacheStore();

  // 添加回调
  const modalAddCallback = () => {
    table.value.addedCallback();
  };

  // 修改回调
  const modalUpdateCallback = () => {
    table.value.updatedCallback();
  };

  // 加载字典配置项
  const loadDictKeys = async () => {
    try {
      const { data } = await getDictKeyList();
      // 设置到缓存
      cacheStore.set('dictKeys', data);
    } catch {
      Message.error('配置项加载失败');
    }
  };
  loadDictKeys();

  // 卸载时清除 cache
  onUnmounted(() => {
    cacheStore.set('dictKeys', []);
  });

</script>

<style lang="less" scoped>

</style>
