<template>
  <a-list size="small"
          max-height="100%"
          :hoverable="true"
          :data="selectedGroupHosts"
          :loading="loading">
    <!-- 表头 -->
    <template #header>
      <span class="hosts-header-title">组内数据</span>
      <span class="span-blue">{{ props.group?.title }}</span>
    </template>
    <!-- 空数据 -->
    <template #empty>
      <span class="host-list-empty">当前分组未配置主机</span>
    </template>
    <!-- 数据 -->
    <template #item="{ item }">
      <a-list-item :title="`${item.name}(${item.code}) - ${ item.address}`">
        <icon-desktop class="host-list-icon" />
        <span>{{ `${item.name}(${item.code}) - ` }}</span>
        <span class="span-blue">{{ item.address }}</span>
      </a-list-item>
    </template>
  </a-list>
</template>

<script lang="ts">
  export default {
    name: 'host-list'
  };
</script>

<script lang="ts" setup>
  import type { TreeNodeData } from '@arco-design/web-vue';
  import type { HostQueryResponse } from '@/api/asset/host';
  import type { PropType } from 'vue';
  import useLoading from '@/hooks/loading';
  import { useCacheStore } from '@/store';
  import { onBeforeMount, ref, watch } from 'vue';
  import { getHostGroupRelList } from '@/api/asset/host-group';
  import { getHostList } from '@/api/asset/host';
  import { Message } from '@arco-design/web-vue';

  const props = defineProps({
    group: {
      type: Object as PropType<TreeNodeData>,
      default: () => {
        return {};
      }
    }
  });

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
      selectedGroupHosts.value = data.map(s => cacheStore.hosts.find(h => h.id === s) as HostQueryResponse)
        .filter(Boolean);
      for (let i = 1800; i < 2000; i++) {
        selectedGroupHosts.value.push({
          id: i,
          name: i + '',
          code: i + '',
        } as any);
      }
    } catch (e) {
    } finally {
      setLoading(false);
    }
  });

  // 加载主机列表
  const loadHostList = async () => {
    try {
      const { data } = await getHostList();
      // 设置到缓存
      cacheStore.set('hosts', data);
    } catch (e) {
      Message.error('主机列表加载失败');
    }
  };

  onBeforeMount(async () => {
    if (!cacheStore.hosts.length) {
      // 加载用户列表
      await loadHostList();
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
