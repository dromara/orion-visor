<template>
  <grant-layout :type="type"
                :loading="loading"
                @fetch="fetchAuthorizedData"
                @grant="doGrant"
                @select-all="selectAll"
                @reverse="reverseSelect">
    <!-- 主机分组 -->
    <host-group-tree v-model:checked-keys="checkedGroups"
                     ref="tree"
                     outer-class="group-main-tree"
                     :checkable="true"
                     :editable="false"
                     :loading="loading"
                     @set-loading="setLoading"
                     @selected-node="(e) => selectedGroup = e"
                     @on-selected="clickGroup" />
    <a-divider direction="vertical" />
    <!-- 主机表格 -->
    <a-table class="group-main-hosts"
             row-key="id"
             :sticky-header="true"
             :loading="loading"
             :columns="hostColumns"
             :data="selectedGroupHosts"
             :pagination="false"
             :bordered="false">
      <!-- 空状态 -->
      <template #empty>
        <a-empty style="margin: 32px 0;" description="当前分组内无主机" />
      </template>
      <!-- 主机类型 -->
      <template #type="{ record }">
        <a-tag class="flex-center" :color="getDictValue(hostTypeKey, record.type, 'color')">
          <!-- 系统类型图标 -->
          <component v-if="getHostOsIcon(record.osType)"
                     :is="getHostOsIcon(record.osType)"
                     class="os-icon" />
          <!-- 主机类型 -->
          <span>{{ getDictValue(hostTypeKey, record.type) }}</span>
        </a-tag>
      </template>
      <!-- 地址 -->
      <template #address="{ record }">
        {{ record.address }}:{{ record.port }}
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
  import type { TreeNodeData } from '@arco-design/web-vue';
  import type { HostQueryResponse } from '@/api/asset/host';
  import type { AssetAuthorizedDataQueryRequest, AssetDataGrantRequest } from '@/api/asset/asset-data-grant';
  import { ref, watch } from 'vue';
  import useLoading from '@/hooks/loading';
  import { getAuthorizedHostGroup, grantHostGroup } from '@/api/asset/asset-data-grant';
  import { useCacheStore, useDictStore } from '@/store';
  import { flatNodeKeys } from '@/utils/tree';
  import { Message } from '@arco-design/web-vue';
  import { hostColumns } from '../types/table.columns';
  import { getHostOsIcon, hostTypeKey } from '@/views/asset/host-list/types/const';
  import { getHostGroupRelList } from '@/api/asset/host-group';
  import HostGroupTree from '@/components/asset/host-group/tree/index.vue';
  import GrantLayout from './grant-layout.vue';

  const props = defineProps<{
    type: string;
  }>();

  const cacheStore = useCacheStore();
  const { getDictValue } = useDictStore();
  const { loading, setLoading } = useLoading();

  const tree = ref();
  const authorizedGroups = ref<Array<number>>([]);
  const checkedGroups = ref<Array<number>>([]);
  const selectedGroup = ref<TreeNodeData>({});
  const selectedGroupHosts = ref<Array<HostQueryResponse>>([]);

  // 监听分组变化 加载组内数据
  watch(() => selectedGroup.value?.key, async (groupId) => {
    if (!groupId) {
      return;
    }
    setLoading(true);
    try {
      // 加载组内数据
      const { data } = await getHostGroupRelList(groupId as number);
      const hosts = await cacheStore.loadHosts();
      selectedGroupHosts.value = data.map(s => hosts.find(h => h.id === s) as HostQueryResponse)
        .filter(Boolean);
    } catch (e) {
    } finally {
      setLoading(false);
    }
  });

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
      // 清空缓存
      cacheStore.reset('authorizedHost_ALL', 'authorizedHost_SSH');
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
    width: calc(100% - 496px);
    height: 100%;
  }

  .group-main-hosts {
    width: 480px;
    height: 100%;

    .os-icon {
      width: 16px;
      height: 16px;
      margin-right: 6px;
    }
  }
</style>
