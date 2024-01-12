<template>
  <div>
    <!-- 分组视图列表 -->
    <host-group-view v-if="NewConnectionType.GROUP === newConnectionType"
                     v-model="selectedGroup"
                     :group-tree="hosts.groupTree"
                     :tree-nodes="treeNodes"
                     :host-list="hostList"
                     :filter-value="filterValue" />
    <!-- 列表视图 -->
    <host-list-view v-if="NewConnectionType.LIST === newConnectionType"
                    :hostList="hostList"
                    empty-value="无授权主机!" />
    <!-- 我的收藏 -->
    <host-list-view v-if="NewConnectionType.FAVORITE === newConnectionType"
                    class="list-view-container"
                    :hostList="hostList"
                    empty-value="无收藏记录, 快去点击主机右侧的⭐进行收藏吧!" />
    <!-- 最近连接 -->
    <host-list-view v-if="NewConnectionType.LATEST === newConnectionType"
                    class="list-view-container"
                    :hostList="hostList"
                    empty-value="暂无连接记录, 快去体验吧!" />
    <!-- 修改主机设置模态框 -->
    <ssh-extra-modal ref="sshModal" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'hostsView'
  };
</script>

<script lang="ts" setup>
  import { onMounted, provide, ref, watch } from 'vue';
  import { NewConnectionType, openSshModalKey } from '../../types/terminal.const';
  import { AuthorizedHostQueryResponse } from '@/api/asset/asset-authorized-data';
  import { HostQueryResponse } from '@/api/asset/host';
  import HostGroupView from './host-group-view.vue';
  import HostListView from './host-list-view.vue';
  import SshExtraModal from './ssh-extra-modal.vue';

  const props = defineProps<{
    hosts: AuthorizedHostQueryResponse,
    filterValue: string,
    newConnectionType: string
  }>();

  const hostList = ref<Array<HostQueryResponse>>([]);
  const treeNodes = ref<Record<string, Array<number>>>({});
  const selectedGroup = ref(
    props.hosts?.groupTree?.length
      ? props.hosts.groupTree[0].key
      : 0
  );
  const sshModal = ref();

  // 暴露打开 ssh 配置模态框
  provide(openSshModalKey, (record: any) => {
    sshModal.value?.open(record);
  });

  // 主机数据处理
  const shuffleHosts = () => {
    let list = [...props.hosts?.hostList];
    // 过滤
    const filterVal = props.filterValue.toLowerCase();
    if (filterVal) {
      list = filterVal.startsWith('@')
        // tag 过滤
        ? list.filter(item => item.tags.some(tag => (tag.name as string).toLowerCase().startsWith(filterVal.substring(1, filterVal.length))))
        // 名称/编码/地址 过滤
        : list.filter(item => {
          return (item.name as string)?.toLowerCase().indexOf(filterVal) > -1
            || (item.code as string)?.toLowerCase().indexOf(filterVal) > -1
            || (item.alias as string)?.toLowerCase().indexOf(filterVal) > -1
            || (item.address as string)?.toLowerCase().indexOf(filterVal) > -1;
        });
    }
    // 判断类型
    if (NewConnectionType.GROUP === props.newConnectionType) {
      // 过滤-分组
      const groupNodes = { ...props.hosts.treeNodes };
      Object.keys(groupNodes).forEach(k => {
        groupNodes[k] = (groupNodes[k] || []).filter(item => list.some(host => host.id === item));
      });
      treeNodes.value = groupNodes;
      // 当前组内数据
      list = list.filter(item => groupNodes[selectedGroup.value]?.some(id => id === item.id));
    } else if (NewConnectionType.FAVORITE === props.newConnectionType) {
      // 过滤-个人收藏
      list = list.filter(item => item.favorite);
    } else if (NewConnectionType.LATEST === props.newConnectionType) {
      // 过滤-最近连接
      list = props.hosts.latestHosts
        .map(s => list.find(item => item.id === s) as HostQueryResponse)
        .filter(Boolean);
    }
    // 非最近连接排序
    if (NewConnectionType.LATEST !== props.newConnectionType) {
      hostList.value = list.sort((o1, o2) => {
        if (o1.favorite || o2.favorite) {
          if (o1.favorite && o2.favorite) {
            return o2.id < o1.id ? 1 : -1;
          }
          return o2.favorite ? 1 : -1;
        } else {
          return o2.id < o1.id ? 1 : -1;
        }
      });
    } else {
      // 最近连接不排序
      hostList.value = list;
    }
  };

  // 监听搜索值变化
  watch(() => props.filterValue, shuffleHosts);

  // 监听类型变化
  watch(() => props.newConnectionType, shuffleHosts);

  // 监听分组变化
  watch(selectedGroup, shuffleHosts);

  // 初始化 加载主机
  onMounted(shuffleHosts);

</script>

<style lang="less" scoped>
  .list-view-container {
    max-height: 100%;
    width: 100%;
    overflow: auto;
    position: relative;
  }
</style>
