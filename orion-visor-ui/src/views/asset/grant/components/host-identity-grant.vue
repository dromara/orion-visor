<template>
  <grant-layout :type="type"
                :loading="loading"
                @fetch="fetchAuthorizedData"
                @grant="doGrant"
                @select-all="selectAll"
                @reverse="reverseSelect">
    <!-- 主机身份表格 -->
    <a-table v-model:selected-keys="selectedKeys"
             row-key="id"
             class="table-resize host-identity-main-table"
             :columns="hostIdentityColumns"
             :row-selection="rowSelection"
             row-class="pointer"
             :data="hostIdentities"
             :sticky-header="true"
             :pagination="false"
             :bordered="false"
             :column-resizable="true"
             @row-click="clickRow">
      <!-- 类型 -->
      <template #type="{ record }">
        <a-tag :color="getDictValue(identityTypeKey, record.type, 'color')">
          {{ getDictValue(identityTypeKey, record.type) }}
        </a-tag>
      </template>
      <!-- 密钥名称 -->
      <template #keyId="{ record }">
        <!-- 有密钥 -->
        <template v-if="record.keyId && record.type === 'KEY'">
          <a-tag color="arcoblue" v-if="record.keyId">
            {{ hostKeys.find(s => s.id === record.keyId)?.name }}
          </a-tag>
        </template>
        <!-- 无密钥 -->
        <template v-else>
          <span>-</span>
        </template>
      </template>
    </a-table>
  </grant-layout>
</template>

<script lang="ts">
  export default {
    name: 'hostIdentityGrant'
  };
</script>

<script lang="ts" setup>
  import type { TableData } from '@arco-design/web-vue';
  import type { AssetAuthorizedDataQueryRequest, AssetDataGrantRequest } from '@/api/asset/asset-data-grant';
  import type { HostIdentityQueryResponse } from '@/api/asset/host-identity';
  import type { HostKeyQueryResponse } from '@/api/asset/host-key';
  import { ref, onMounted, onActivated } from 'vue';
  import useLoading from '@/hooks/loading';
  import { useRowSelection } from '@/hooks/table';
  import { getAuthorizedHostIdentity, grantHostIdentity } from '@/api/asset/asset-data-grant';
  import { useCacheStore, useDictStore } from '@/store';
  import { hostIdentityColumns } from '../types/table.columns';
  import { identityTypeKey } from '../types/const';
  import { Message } from '@arco-design/web-vue';
  import GrantLayout from './grant-layout.vue';

  const props = defineProps<{
    type: string;
  }>();

  const cacheStore = useCacheStore();
  const rowSelection = useRowSelection();
  const { getDictValue } = useDictStore();
  const { loading, setLoading } = useLoading();

  const selectedKeys = ref<Array<number>>([]);
  const hostIdentities = ref<Array<HostIdentityQueryResponse>>([]);
  const hostKeys = ref<Array<HostKeyQueryResponse>>([]);

  // 获取授权列表
  const fetchAuthorizedData = async (request: AssetAuthorizedDataQueryRequest) => {
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
      // 执行授权
      await grantHostIdentity({
        ...request,
        idList: selectedKeys.value
      });
      Message.success('授权成功');
      // 清空缓存
      cacheStore.reset('authorizedHostIdentities');
    } catch (e) {
    } finally {
      setLoading(false);
    }
    // 查询数据
    await fetchAuthorizedData(request);
  };

  // 全选
  const selectAll = () => {
    selectedKeys.value = hostIdentities.value.map(s => s.id);
  };

  // 反选
  const reverseSelect = () => {
    selectedKeys.value = hostIdentities.value.map(s => s.id)
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

  // 初始化主机身份
  const initIdentities = async () => {
    setLoading(true);
    try {
      // 加载主机身份
      hostIdentities.value = await cacheStore.loadHostIdentities();
      // 加载主机密钥
      hostKeys.value = await cacheStore.loadHostKeys();
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 初始化身份数据
  onMounted(initIdentities);
  onActivated(initIdentities);

</script>

<style lang="less" scoped>
</style>
