<template>
  <a-table row-key="id"
           class="host-identity-main-table"
           label-align="left"
           :columns="hostIdentityColumns"
           v-model:selected-keys="selectedKeys"
           :row-selection="rowSelection"
           :sticky-header="true"
           :data="hostIdentities"
           :pagination="false"
           :bordered="false">
    <!-- 秘钥名称 -->
    <template #keyId="{ record }">
      <a-tag color="arcoblue" v-if="record.keyName">{{ record.keyName }}</a-tag>
    </template>
  </a-table>
</template>

<script lang="ts">
  export default {
    name: 'host-identity-grant-table'
  };
</script>

<script lang="ts" setup>
  import type { PropType } from 'vue';
  import type { HostIdentityQueryResponse } from '@/api/asset/host-identity';
  import { hostIdentityColumns } from '../types/table.columns';
  import { useRowSelection } from '@/types/table';
  import { computed, ref, onMounted } from 'vue';
  import { useCacheStore } from '@/store';

  const props = defineProps({
    modelValue: {
      type: Array as PropType<Array<number>>,
      default: () => []
    }
  });
  const emits = defineEmits(['loading', 'update:modelValue']);

  const cacheStore = useCacheStore();
  const rowSelection = useRowSelection();

  const hostIdentities = ref<Array<HostIdentityQueryResponse>>([]);

  const selectedKeys = computed({
    get() {
      return props.modelValue;
    },
    set(e) {
      emits('update:modelValue', e);
    }
  });

  // 初始化数据
  onMounted(async () => {
    emits('loading', true);
    try {
      const keys = await cacheStore.loadHostKeys();
      const identities = await cacheStore.loadHostIdentities();
      hostIdentities.value = identities.map(s => {
        return {
          ...s,
          keyName: s.keyId ? keys.find(k => k.id === s.keyId)?.name : undefined
        };
      });
    } catch (e) {
    } finally {
      emits('loading', false);
    }
  });

</script>

<style lang="less" scoped>

</style>
