<template>
  <grant-layout :type="type"
                :loading="loading"
                @fetch="fetchAuthorizedGroup"
                @grant="doGrant">
    <!-- 主机身份表格 -->
    <a-table row-key="id"
             class="host-identity-main-table"
             :columns="hostIdentityColumns"
             v-model:selected-keys="selectedKeys"
             :row-selection="rowSelection"
             row-class="pointer"
             :sticky-header="true"
             :data="hostIdentities"
             :pagination="false"
             :bordered="false"
             @row-click="clickRow">
      <!-- 秘钥名称 -->
      <template #keyId="{ record }">
        <a-tag color="arcoblue" v-if="record.keyId">
          {{ hostKeys.find(s => s.id === record.keyId)?.name }}
        </a-tag>
      </template>
    </a-table>
  </grant-layout>
</template>

<script lang="ts">
  export default {
    name: 'hostGroupGrant'
  };
</script>

<script lang="ts" setup>
  import type { TableData } from '@arco-design/web-vue/es/table/interface';
  import type { AssetAuthorizedDataQueryRequest, AssetDataGrantRequest } from '@/api/asset/asset-data-grant';
  import type { HostIdentityQueryResponse } from '@/api/asset/host-identity';
  import type { HostKeyQueryResponse } from '@/api/asset/host-key';
  import { ref, onMounted } from 'vue';
  import useLoading from '@/hooks/loading';
  import { getAuthorizedHostIdentity, grantHostIdentity } from '@/api/asset/asset-data-grant';
  import { Message } from '@arco-design/web-vue';
  import { hostIdentityColumns } from '../types/table.columns';
  import { useCacheStore } from '@/store';
  import { useRowSelection } from '@/types/table';
  import GrantLayout from './grant-layout.vue';

  const props = defineProps({
    type: String,
  });

  const cacheStore = useCacheStore();
  const rowSelection = useRowSelection();
  const { loading, setLoading } = useLoading();

  const selectedKeys = ref<Array<number>>([]);
  const hostIdentities = ref<Array<HostIdentityQueryResponse>>([]);
  const hostKeys = ref<Array<HostKeyQueryResponse>>([]);

  // 获取授权列表
  const fetchAuthorizedGroup = async (request: AssetAuthorizedDataQueryRequest) => {
    setLoading(true);
    try {
      const { data } = await getAuthorizedHostIdentity(request);
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
      await grantHostIdentity({
        ...request,
        idList: selectedKeys.value
      });
      Message.success('授权成功');
    } catch (e) {
    } finally {
      setLoading(false);
    }
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

  // 初始化数据
  onMounted(async () => {
    setLoading(true);
    try {
      // 加载主机身份
      hostIdentities.value = await cacheStore.loadHostIdentities();
    } catch (e) {
    } finally {
      setLoading(false);
    }
  });

  // 初始化数据
  onMounted(async () => {
    // 加载主机秘钥
    hostKeys.value = await cacheStore.loadHostKeys();
  });

</script>

<style lang="less" scoped>
</style>
