<template>
  <grant-layout :type="type"
                :loading="loading"
                @fetch="fetchAuthorizedGroup"
                @grant="doGrant">
    <!-- 主机秘钥表格 -->
    <a-table row-key="id"
             class="host-key-main-table"
             :columns="hostKeyColumns"
             v-model:selected-keys="selectedKeys"
             :row-selection="rowSelection"
             :sticky-header="true"
             :data="hostKeys"
             :pagination="false"
             :bordered="false" />
  </grant-layout>
</template>

<script lang="ts">
  export default {
    name: 'hostGroupGrant'
  };
</script>

<script lang="ts" setup>
  import type { AssetAuthorizedDataQueryRequest, AssetDataGrantRequest } from '@/api/asset/asset-data-grant';
  import type { HostKeyQueryResponse } from '@/api/asset/host-key';
  import { ref, onMounted } from 'vue';
  import { getAuthorizedHostKey, grantHostKey } from '@/api/asset/asset-data-grant';
  import useLoading from '@/hooks/loading';
  import { useRowSelection } from '@/types/table';
  import { useCacheStore } from '@/store';
  import { Message } from '@arco-design/web-vue';
  import { hostKeyColumns } from '../types/table.columns';
  import GrantLayout from './grant-layout.vue';

  const props = defineProps({
    type: String,
  });
  const cacheStore = useCacheStore();
  const rowSelection = useRowSelection();
  const { loading, setLoading } = useLoading();

  const selectedKeys = ref<Array<number>>([]);
  const hostKeys = ref<Array<HostKeyQueryResponse>>([]);

  // 获取授权列表
  const fetchAuthorizedGroup = async (request: AssetAuthorizedDataQueryRequest) => {
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
      await grantHostKey({
        ...request,
        idList: selectedKeys.value
      });
      Message.success('授权成功');
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 初始化数据
  onMounted(async () => {
    setLoading(true);
    try {
      hostKeys.value = await cacheStore.loadHostKeys();
    } catch (e) {
    } finally {
      setLoading(false);
    }
  });

</script>

<style lang="less" scoped>
</style>
