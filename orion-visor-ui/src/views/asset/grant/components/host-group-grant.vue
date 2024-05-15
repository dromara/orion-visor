<template>
  <grant-layout :type="type"
                :loading="loading"
                @fetch="fetchAuthorizedData"
                @grant="doGrant"
                @select-all="selectAll"
                @reverse="reverseSelect">
    <!-- 分组 -->
    <host-group-tree v-model:checked-keys="checkedGroups"
                     ref="tree"
                     outer-class="group-main-tree"
                     :checkable="true"
                     :editable="false"
                     :loading="loading"
                     @set-loading="setLoading"
                     @selected-node="(e) => selectedGroup = e"
                     @on-selected="clickGroup" />
    <!-- 主机列表 -->
    <host-list class="group-main-hosts sticky-list" :group="selectedGroup" />
  </grant-layout>
</template>

<script lang="ts">
  export default {
    name: 'hostGroupGrant'
  };
</script>

<script lang="ts" setup>
  import type { TreeNodeData } from '@arco-design/web-vue';
  import type { AssetAuthorizedDataQueryRequest, AssetDataGrantRequest } from '@/api/asset/asset-data-grant';
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import { getAuthorizedHostGroup, grantHostGroup } from '@/api/asset/asset-data-grant';
  import { useCacheStore } from '@/store';
  import { flatNodeKeys } from '@/utils/tree';
  import { Message } from '@arco-design/web-vue';
  import HostGroupTree from '@/components/asset/host-group/tree/index.vue';
  import HostList from './host-list.vue';
  import GrantLayout from './grant-layout.vue';

  const props = defineProps<{
    type: string;
  }>();

  const cacheStore = useCacheStore();
  const { loading, setLoading } = useLoading();

  const tree = ref();
  const authorizedGroups = ref<Array<number>>([]);
  const checkedGroups = ref<Array<number>>([]);
  const selectedGroup = ref<TreeNodeData>({});

  // 点击分组
  const clickGroup = (groups: Array<number>) => {
    if (groups && groups.length) {
      const group = groups[0];
      const index = checkedGroups.value.findIndex((s) => s === group);
      if (index === -1) {
        checkedGroups.value.push(group);
      } else {
        checkedGroups.value.splice(index, 1);
      }
    }
  };

  // 获取授权列表
  const fetchAuthorizedData = async (request: AssetAuthorizedDataQueryRequest) => {
    setLoading(true);
    try {
      const { data } = await getAuthorizedHostGroup(request);
      authorizedGroups.value = data;
      checkedGroups.value = data;
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
      await grantHostGroup({
        ...request,
        idList: checkedGroups.value
      });
      Message.success('授权成功');
    } catch (e) {
    } finally {
      setLoading(false);
    }
    // 查询数据
    await fetchAuthorizedData(request);
  };

  // 全选
  const selectAll = async () => {
    // 从缓存中查询全部分组
    const groups = await cacheStore.loadHostGroups();
    const groupKeys: number[] = [];
    flatNodeKeys(groups, groupKeys);
    checkedGroups.value = groupKeys;
  };

  // 反选
  const reverseSelect = async () => {
    // 从缓存中查询全部分组
    const groups = await cacheStore.loadHostGroups();
    const groupKeys: number[] = [];
    flatNodeKeys(groups, groupKeys);
    checkedGroups.value = groupKeys.filter(s => !checkedGroups.value.includes(s));
  };

</script>

<style lang="less" scoped>
  .group-main-tree {
    width: calc(60% - 16px);
    height: 100%;
    margin-right: 16px;
  }

  .group-main-hosts {
    width: 40%;
  }
</style>
