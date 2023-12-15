<template>
  <div class="terminal-setting-container">
    <div class="terminal-setting-wrapper">
      <!-- 主标题 -->
      <h2 class="terminal-setting-title">新建连接</h2>
      <!-- 操作栏 -->
      <div class="terminal-setting-block header-actions">
        <!-- 视图类型 -->
        <a-radio-group v-model="newConnectionType"
                       type="button"
                       class="usn"
                       :options="toOptions(NewConnectionTypeKey)"
                       @change="changeConnectionType" />
        <!-- 过滤 -->
        <a-auto-complete v-model="filterValue"
                         class="host-filter"
                         placeholder="输入名称/编码/IP @标签"
                         :allow-clear="true"
                         :data="filterOptions"
                         :filter-option="searchFilter"
                         @change="shuffleHosts">
          <template #option="{ data: { raw: { value, isTag} } }">
            <!-- tag -->
            <a-tag v-if="isTag" :color="dataColor(value, tagColor)">
              {{ value }}
            </a-tag>
            <!-- 文本 -->
            <template v-else>
              {{ value }}
            </template>
          </template>
        </a-auto-complete>
      </div>
      <!-- 授权主机 -->
      <div class="terminal-setting-block" style="margin: 0;">
        <!-- 顶部 -->
        <div class="terminal-setting-subtitle-wrapper">
          <h3 class="terminal-setting-subtitle">
            授权主机
          </h3>
        </div>
        <!-- 内容区域 -->
        <div class="terminal-setting-body body-container">
          <!-- 加载中 -->
          <a-skeleton v-if="loading"
                      class="hosts-skeleton"
                      :animation="true">
            <a-skeleton-line :rows="6"
                             :line-height="40"
                             :line-spacing="20" />
          </a-skeleton>
          <!-- 无数据 -->
          <a-empty v-else-if="!hosts.hostList?.length">
            <template #image>
              <icon-desktop />
            </template>
            Oops! 无授权主机 请联系管理员授权后重试!
          </a-empty>
          <!-- 主机列表 -->
          <div v-else class="host-view-container">
            <!-- 分组视图列表 -->
            <host-group-view v-if="NewConnectionType.GROUP === newConnectionType"
                             :hosts="hosts"
                             :filter-value="filterValue" />
            <!-- 列表视图 -->
            <host-list-view v-if="NewConnectionType.LIST === newConnectionType"
                            :host-list="hostList"
                            empty-value="无授权主机!" />
            <!-- 我的收藏 -->
            <host-list-view v-if="NewConnectionType.FAVORITE === newConnectionType"
                            :host-list="[]"
                            empty-value="无收藏记录, 快去点击主机右侧的⭐进行收藏吧!" />
            <!-- 最近连接 -->
            <host-list-view v-if="NewConnectionType.LATEST === newConnectionType"
                            :host-list="[]"
                            empty-value="暂无连接记录, 快去体验吧!" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'newConnectionView'
  };
</script>

<script lang="ts" setup>
  import type { SelectOptionData } from '@arco-design/web-vue';
  import type { AuthorizedHostQueryResponse } from '@/api/asset/asset-authorized-data';
  import type { HostQueryResponse } from '@/api/asset/host';
  import { getCurrentAuthorizedHost } from '@/api/asset/asset-authorized-data';
  import { onBeforeMount, ref, watch } from 'vue';
  import { NewConnectionType, NewConnectionTypeKey } from '../../types/terminal.const';
  import useLoading from '@/hooks/loading';
  import { useDictStore } from '@/store';
  import { dataColor } from '@/utils';
  import { tagColor } from '@/views/asset/host-list/types/const';
  import HostGroupView from './host-group-view.vue';
  import HostListView from './host-list-view.vue';

  const { loading, setLoading } = useLoading();
  const { toOptions } = useDictStore();

  const newConnectionType = ref(NewConnectionType.LIST);
  const filterValue = ref();
  const filterOptions = ref<Array<SelectOptionData>>([]);
  const hosts = ref<AuthorizedHostQueryResponse>({} as AuthorizedHostQueryResponse);
  const hostList = ref<Array<HostQueryResponse>>([]);

  // 修改连接类型
  const changeConnectionType = () => {
    // FIXME 持久化
  };

  // 过滤输入
  const searchFilter = (searchValue: string, option: SelectOptionData) => {
    if (searchValue.startsWith('@')) {
      // tag 过滤
      return option.isTag && (option.value as string).toLowerCase().startsWith(searchValue.substring(1, searchValue.length).toLowerCase());
    } else {
      // 文本过滤
      return !option.isTag && (option.value as string).toLowerCase().indexOf(searchValue.toLowerCase()) > -1;
    }
  };

  // 数据处理
  const shuffleHosts = () => {
    let list = [...hosts.value?.hostList];
    // 过滤
    if (filterValue.value) {
      console.log(filterValue.value);
    }
    // 排序
    hostList.value = list?.sort((o1, o2) => {
      if (o1.favorite || o2.favorite) {
        if (o1.favorite && o2.favorite) {
          return o2.id < o1.id ? 1 : -1;
        }
        return o2.favorite ? 1 : -1;
      } else {
        return o2.id < o1.id ? 1 : -1;
      }
    });
  };

  // 初始化过滤器项
  const initFilterOptions = () => {
    // 添加 tags
    const tagNames = hosts.value.hostList?.map(s => s.tags)
      .filter(s => s?.length)
      .flat(1)
      .sort((o1, o2) => o1.id - o2.id)
      .map(s => s.name);
    [...new Set(tagNames)].map(value => {
      return { value: value, isTag: true };
    }).forEach(s => filterOptions.value.push(s));
    // 添加主机信息
    const hostMeta = hosts.value.hostList?.map(s => {
      return [s.name, s.code, s.address];
    }).flat(1);
    [...new Set(hostMeta)].map(value => {
      return { value };
    }).forEach(s => filterOptions.value.push(s));
    //       // 添加主机信息
    // hosts.value.hostList?.map(s => {
    //   return `${s.name} (${s.code}) - ${s.address}`;
    // }).map(value => {
    //   return { value };
    // }).forEach(s => filterOptions.value.push(s));
  };

  // 初始化
  const init = async () => {
    try {
      setLoading(true);
      const { data } = await getCurrentAuthorizedHost();
      hosts.value = data;
      // 初始化过滤项
      initFilterOptions();
      // 处理数据
      shuffleHosts();
    } finally {
      setLoading(false);
    }
  };

  // 加载主机信息
  onBeforeMount(init);

</script>

<style lang="less" scoped>
  .header-actions {
    display: flex;
    align-items: center;
    justify-content: space-between;

    :deep(.host-filter) {
      width: 36%;
    }
  }

  .body-container {
    justify-content: space-between;

    .hosts-skeleton {
      width: 100%;
    }

    .host-view-container {
      width: 100%;
      height: calc(100vh - 240px);
      position: relative;
    }
  }

</style>
