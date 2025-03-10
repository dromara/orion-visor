<template>
  <a-modal v-model:visible="visible"
           title-align="start"
           title="授权主机"
           :top="60"
           :width="968"
           :align-center="false"
           :draggable="true"
           :mask-closable="false"
           :unmount-on-close="true"
           :body-style="{ padding: '0' }"
           :ok-button-props="{ disabled: loading }"
           :cancel-button-props="{ disabled: loading }"
           :on-before-ok="handlerOk"
           @close="handleClose">
    <!-- 加载中 -->
    <a-skeleton v-if="loading"
                style="padding: 16px"
                :animation="true">
      <a-skeleton-line :rows="6"
                       :line-height="42"
                       :line-spacing="12" />
    </a-skeleton>
    <!-- 主机列表容器 -->
    <div v-else class="host-layout-container">
      <!-- 顶部操作 -->
      <div class="top-side-container">
        <!-- 视图类型 -->
        <a-radio-group v-model="newConnectionType"
                       type="button"
                       class="usn"
                       :options="toRadioOptions(newConnectionTypeKey)" />
        <!-- 过滤 -->
        <a-auto-complete v-model="filterValue"
                         class="host-filter"
                         placeholder="别名/名称/编码/IP @标签"
                         :allow-clear="true"
                         :data="filterOptions"
                         :filter-option="tagLabelFilter">
          <!-- 选项 -->
          <template #option="{ data: { raw: { label, isTag } } }">
            <!-- tag -->
            <a-tag v-if="isTag" :color="dataColor(label, tagColor)">
              {{ label }}
            </a-tag>
            <!-- 文本 -->
            <template v-else>
              {{ label }}
            </template>
          </template>
        </a-auto-complete>
      </div>
      <!-- 主机列表 -->
      <div class="host-container">
        <!-- 分组视图 -->
        <host-group v-if="newConnectionType === NewConnectionType.GROUP"
                    v-model:selected-keys="selectedKeys"
                    v-model:selected-group="selectedGroup"
                    :host-list="hostList"
                    :groups="hosts?.groupTree as any"
                    :nodes="treeNodes as any" />
        <!-- 列表视图 -->
        <host-table v-else
                    v-model:selected-keys="selectedKeys"
                    :host-list="hostList"
                    :empty-message="emptyMessage" />
      </div>
    </div>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'authorizedHostModal'
  };
</script>

<script lang="ts" setup>
  import type { SelectOptionData } from '@arco-design/web-vue';
  import type { AuthorizedHostQueryResponse } from '@/api/asset/asset-authorized-data';
  import type { HostQueryResponse } from '@/api/asset/host';
  import type { HostType } from '@/api/asset/host';
  import { onMounted, ref, watch, computed } from 'vue';
  import { dataColor } from '@/utils';
  import { dictKeys, NewConnectionType, newConnectionTypeKey } from './types/const';
  import { useCacheStore, useDictStore } from '@/store';
  import { tagLabelFilter } from '@/types/form';
  import { tagColor } from '@/views/asset/host-list/types/const';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import { getAuthorizedHostOptions } from '@/types/options';
  import { getLatestConnectHostId } from '@/api/asset/terminal-connect-log';
  import HostTable from './components/host-table.vue';
  import HostGroup from './components/host-group.vue';

  const props = withDefaults(defineProps<Partial<{
    type: HostType;
  }>>(), {
    type: undefined,
  });

  const emits = defineEmits(['selected']);

  const { toRadioOptions, loadKeys } = useDictStore();
  const { loading, setLoading } = useLoading();
  const { visible, setVisible } = useVisible();

  const hosts = ref<AuthorizedHostQueryResponse>();
  const hostList = ref<Array<HostQueryResponse>>([]);
  const treeNodes = ref<Record<string, Array<number>>>({});
  const selectedGroup = ref<number>(0);
  const selectedKeys = ref<Array<number>>([]);
  const newConnectionType = ref(NewConnectionType.GROUP);
  const filterValue = ref('');
  const filterOptions = ref<Array<SelectOptionData>>([]);

  const emptyMessage = computed(() => {
    if (newConnectionType.value === NewConnectionType.LIST) {
      // 列表
      return '无授权主机!';
    } else if (newConnectionType.value === NewConnectionType.FAVORITE) {
      // 收藏
      return '无收藏主机!';
    } else if (newConnectionType.value === NewConnectionType.LATEST) {
      // 最近连接
      return '暂无连接记录!';
    }
    return '';
  });

  // 打开
  const open = async (hostIdList: Array<number> = []) => {
    setVisible(true);
    // 加载主机列表
    await fetchHosts();
    // 设置选中分组
    selectedGroup.value = hosts.value?.groupTree?.length ? hosts.value.groupTree[0].key : 0;
    // 设置主机数据
    shuffleHosts();
    // 设置选中项
    selectedKeys.value = hosts.value?.hostList
      .map(s => s.id)
      .filter(s => hostIdList.includes(s)) || [];
  };

  // 加载主机列表
  const fetchHosts = async () => {
    if (hosts.value) {
      return;
    }
    setLoading(true);
    try {
      // 加载主机列表
      const data = await useCacheStore().loadAuthorizedHosts(props.type);
      // 禁用别名
      data.hostList.forEach(s => s.alias = undefined as unknown as string);
      // 查询最近连接的主机
      const { data: latestHosts } = await getLatestConnectHostId(props.type as string, 30);
      data.latestHosts = latestHosts;
      hosts.value = data;
      // 设置主机搜索选项
      filterOptions.value = getAuthorizedHostOptions(data.hostList);
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  defineExpose({ open });

  // 主机数据处理
  const shuffleHosts = () => {
    if (!hosts.value) {
      return;
    }
    let list = [...hosts.value.hostList];
    // 过滤
    const filterVal = filterValue.value.toLowerCase();
    if (filterVal) {
      list = filterVal.startsWith('@')
        // tag 过滤
        ? list.filter(item => item.tags.some(tag => tag.name?.toLowerCase().startsWith(filterVal.substring(1, filterVal.length))))
        // 名称/编码/地址 过滤
        : list.filter(item => {
          return (item.name as string)?.toLowerCase().indexOf(filterVal) > -1
            || (item.code as string)?.toLowerCase().indexOf(filterVal) > -1
            || (item.address as string)?.toLowerCase().indexOf(filterVal) > -1;
        });
    }
    // 判断类型
    if (NewConnectionType.GROUP === newConnectionType.value) {
      // 过滤-分组
      const groupNodes = { ...hosts.value.treeNodes };
      Object.keys(groupNodes).forEach(k => {
        groupNodes[k] = (groupNodes[k] || []).filter(item => list.some(host => host.id === item));
      });
      treeNodes.value = groupNodes;
      // 当前组内数据
      list = list.filter(item => groupNodes[selectedGroup.value]?.some(id => id === item.id));
    } else if (NewConnectionType.FAVORITE === newConnectionType.value) {
      // 过滤-个人收藏
      list = list.filter(item => item.favorite);
    } else if (NewConnectionType.LATEST === newConnectionType.value) {
      // 过滤-最近连接
      list = hosts.value.latestHosts
        .map(s => list.find(item => item.id === s) as HostQueryResponse)
        .filter(Boolean);
    }
    // 非最近连接排序
    if (NewConnectionType.LATEST !== newConnectionType.value) {
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
  watch(filterValue, shuffleHosts);

  // 监听类型变化
  watch(newConnectionType, shuffleHosts);

  // 监听分组变化
  watch(selectedGroup, shuffleHosts);

  // 确定
  const handlerOk = async () => {
    emits('selected', selectedKeys.value);
    // 清空
    handlerClear();
  };

  // 关闭
  const handleClose = () => {
    handlerClear();
  };

  // 清空
  const handlerClear = () => {
    setLoading(false);
  };

  // 加载字典值
  onMounted(async () => {
    await loadKeys(dictKeys);
  });

</script>

<style lang="less" scoped>
  .host-layout-container {
    padding: 16px;
    width: 100%;
    height: calc(100vh - 248px);
    overflow: hidden;
    position: relative;
  }

  .top-side-container {
    height: 32px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 16px;

    :deep(.host-filter) {
      width: 42%;
    }
  }

  .host-container {
    height: calc(100% - 48px);
  }

</style>
