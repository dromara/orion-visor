<template>
  <grant-layout :type="type"
                :loading="loading"
                @fetch="fetchAuthorizedData"
                @grant="doGrant">
    <!-- 分组 -->
    <host-group-tree v-model:checked-keys="checkedGroups"
                     outer-class="group-main-tree"
                     :checkable="true"
                     :editable="false"
                     :loading="loading"
                     @set-loading="setLoading"
                     @selected-node="(e) => selectedGroup = e" />
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
  import { Message } from '@arco-design/web-vue';
  import HostGroupTree from '@/components/asset/host-group/tree/index.vue';
  import HostList from './host-list.vue';
  import GrantLayout from './grant-layout.vue';

  const props = defineProps<{
    type: string;
  }>();

  const { loading, setLoading } = useLoading();

  const authorizedGroups = ref<Array<number>>([]);
  const checkedGroups = ref<Array<number>>([]);
  const selectedGroup = ref<TreeNodeData>({});

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
