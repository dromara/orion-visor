<template>
  <a-list size="small"
          max-height="100%"
          :hoverable="true"
          :data="selectedGroupHosts"
          :loading="loading">
    <!-- 表头 -->
    <template #header>
      <span class="hosts-header-title">组内数据</span>
      <span class="span-blue">{{ group?.title }}</span>
    </template>
    <!-- 空数据 -->
    <template #empty>
      <span class="host-list-empty">当前分组未配置主机</span>
    </template>
    <!-- 数据 -->
    <template #item="{ item }">
      <a-tooltip :content="`${item.name} - ${item.address}`">
        <a-list-item>
          <icon-desktop class="host-list-icon" />
          <span>{{ `${item.name} - ` }}</span>
          <span class="span-blue">{{ item.address }}</span>
        </a-list-item>
      </a-tooltip>
    </template>
  </a-list>
</template>

<script lang="ts">
  export default {
    name: 'hostList'
  };
</script>

<script lang="ts" setup>
  import type { TreeNodeData } from '@arco-design/web-vue';
  import type { HostQueryResponse } from '@/api/asset/host';
  import useLoading from '@/hooks/loading';
  import { useCacheStore } from '@/store';
  import { ref, watch } from 'vue';
  import { getHostGroupRelList } from '@/api/asset/host-group';

  const props = defineProps<Partial<{
    group: TreeNodeData;
  }>>();

  const cacheStore = useCacheStore();
  const { loading, setLoading } = useLoading();

  const selectedGroupHosts = ref<Array<HostQueryResponse>>([]);

  // 监听分组变化 加载组内数据
  watch(() => props.group?.key, async (groupId) => {
    if (!groupId) {
      return;
    }
    // 加载组内数据
    try {
      setLoading(true);
      const { data } = await getHostGroupRelList(groupId as number);
      const hosts = await cacheStore.loadHosts(undefined);
      selectedGroupHosts.value = data.map(s => hosts.find(h => h.id === s) as HostQueryResponse)
        .filter(Boolean);
    } catch (e) {
    } finally {
      setLoading(false);
    }
  });

</script>

<style lang="less" scoped>
  .hosts-header-title {
    &:after {
      content: '-';
      margin: 0 8px;
    }
  }

  .host-list-empty {
    padding: 16px 24px;
    text-align: center;
    color: var(--color-text-2);
    display: block;
  }

  :deep(.arco-scrollbar) {
    width: 100%;
    height: 100%;
  }

  :deep(.arco-list-item-content) {
    display: flex;
    align-items: center;
    color: var(--color-text-1);
    overflow: hidden;
    word-break: keep-all;
    white-space: pre;

    .host-list-icon {
      font-size: 24px;
      padding: 4px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: var(--color-white);
      background: rgb(var(--blue-6));
      margin-right: 8px;
      border-radius: 4px;
    }
  }
</style>
