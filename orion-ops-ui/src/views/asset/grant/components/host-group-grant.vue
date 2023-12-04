<template>
  <grant-layout :type="type"
                :loading="loading"
                @fetch="fetchAuthorizedGroup"
                @grant="doGrant">
    <!-- 分组 -->
    <host-group-tree outer-class="group-main-tree"
                     :checkable="true"
                     :checked-keys="checkedGroups"
                     :editable="false"
                     :loading="loading"
                     @loading="setLoading"
                     @select-node="e => selectedGroup = e"
                     @update:checked-keys="updateCheckedGroups" />
    <!-- 主机列表 -->
    <host-list class="group-main-hosts sticky-list" :group="selectedGroup" />
  </grant-layout>
</template>

<script lang="ts">
  export default {
    name: 'host-group-grant'
  };
</script>

<script lang="ts" setup>
  import type { TreeNodeData } from '@arco-design/web-vue';
  import type { AssetAuthorizedDataQueryRequest, AssetDataGrantRequest } from '@/api/asset/asset-data-grant';
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import { getAuthorizedHostGroup, grantHostGroup } from '@/api/asset/asset-data-grant';
  import { Message } from '@arco-design/web-vue';
  import HostGroupTree from '@/components/asset/host-group/host-group-tree.vue';
  import HostList from './host-list.vue';
  import GrantLayout from './grant-layout.vue';

  const props = defineProps({
    type: String,
  });

  const { loading, setLoading } = useLoading();

  const authorizedGroups = ref<Array<number>>([]);
  const checkedGroups = ref<Array<number>>([]);
  const selectedGroup = ref<TreeNodeData>({});

  // 选择分组
  const updateCheckedGroups = (e: Array<number>) => {
    checkedGroups.value = e;
  };

  // 获取授权列表
  const fetchAuthorizedGroup = async (request: AssetAuthorizedDataQueryRequest) => {
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
      await grantHostGroup({
        ...request,
        idList: checkedGroups.value
      });
      Message.success('授权成功');
    } catch (e) {
    } finally {
      setLoading(false);
    }
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
