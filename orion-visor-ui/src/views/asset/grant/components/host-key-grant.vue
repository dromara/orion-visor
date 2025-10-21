<template>
  <grant-layout :type="type"
                :loading="loading"
                @fetch="fetchAuthorizedData"
                @grant="doGrant"
                @select-all="selectAll"
                @reverse="reverseSelect">
    <!-- 主机密钥表格 -->
    <a-table v-model:selected-keys="selectedKeys"
             row-key="id"
             class="table-resize host-key-main-table"
             :columns="hostKeyColumns"
             :row-selection="rowSelection"
             row-class="pointer"
             :sticky-header="true"
             :data="hostKeys"
             :pagination="false"
             :bordered="false"
             :column-resizable="true"
             @row-click="clickRow" />
  </grant-layout>
</template>

<script lang="ts">
  export default {
    name: 'hostKeyGrant'
  };
</script>

<script lang="ts" setup>
  import type { TableData } from '@arco-design/web-vue';
  import type { AssetAuthorizedDataQueryRequest, AssetDataGrantRequest } from '@/api/asset/asset-data-grant';
  import type { HostKeyQueryResponse } from '@/api/asset/host-key';
  import { ref, onMounted, onActivated } from 'vue';
  import { getAuthorizedHostKey, grantHostKey } from '@/api/asset/asset-data-grant';
  import useLoading from '@/hooks/loading';
  import { useRowSelection } from '@/hooks/table';
  import { useCacheStore } from '@/store';
  import { Message } from '@arco-design/web-vue';
  import { hostKeyColumns } from '../types/table.columns';
  import GrantLayout from './grant-layout.vue';

  const props = defineProps<{
    type: string;
  }>();

  const cacheStore = useCacheStore();
  const rowSelection = useRowSelection();
  const { loading, setLoading } = useLoading();

  const selectedKeys = ref<Array<number>>([]);
  const hostKeys = ref<Array<HostKeyQueryResponse>>([]);

  // 获取授权列表
  const fetchAuthorizedData = async (request: AssetAuthorizedDataQueryRequest) => {
    setLoading(true);
    try {
      const { data } = await getAuthorizedHostKey(request);
      selectedKeys.value = data;
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 授权
  const doGrant = async (request: AssetDataGrantRequest) => {
    setLoading(true);
    try {
      // 执行授权
      await grantHostKey({
        ...request,
        idList: selectedKeys.value
      });
      Message.success('授权成功');
      // 清空缓存
      cacheStore.reset('authorizedHostKeys');
    } catch (e) {
    } finally {
      setLoading(false);
    }
    // 查询数据
    await fetchAuthorizedData(request);
  };

  // 全选
  const selectAll = () => {
    selectedKeys.value = hostKeys.value.map(s => s.id);
  };

  // 反选
  const reverseSelect = () => {
    selectedKeys.value = hostKeys.value.map(s => s.id)
      .filter(s => !selectedKeys.value.includes(s));
  };

  // 点击行
  const clickRow = ({ id }: TableData) => {
    const index = selectedKeys.value.indexOf(id);
    if (index === -1) {
      selectedKeys.value.push(id);
    } else {
      selectedKeys.value.splice(index, 1);
    }
  };

  // 初始化主机密钥
  const initKeys = async () => {
    setLoading(true);
    try {
      hostKeys.value = await cacheStore.loadHostKeys();
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 初始化主机密钥
  onMounted(initKeys);
  onActivated(initKeys);

</script>

<style lang="less" scoped>
</style>
