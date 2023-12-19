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
                       @change="changeNewConnectionType" />
        <!-- 过滤 -->
        <a-auto-complete v-model="filterValue"
                         class="host-filter"
                         placeholder="别名/名称/编码/IP @标签"
                         :allow-clear="true"
                         :data="filterOptions"
                         :filter-option="searchFilter">
          <template #option="{ data: { raw: { label, isTag} } }">
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
          <hosts-view v-else
                      class="host-view-container"
                      :hosts="hosts"
                      :filter-value="filterValue"
                      :new-connection-type="newConnectionType" />
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
  import { getCurrentAuthorizedHost } from '@/api/asset/asset-authorized-data';
  import { onBeforeMount, ref } from 'vue';
  import { NewConnectionType, NewConnectionTypeKey } from '../../types/terminal.const';
  import useLoading from '@/hooks/loading';
  import { useDictStore, useTerminalStore } from '@/store';
  import { dataColor } from '@/utils';
  import { tagColor } from '@/views/asset/host-list/types/const';
  import HostsView from './hosts-view.vue';

  const { loading, setLoading } = useLoading();
  const { toOptions } = useDictStore();
  const { preference, changeNewConnectionType } = useTerminalStore();

  const newConnectionType = ref(preference.newConnectionType || NewConnectionType.GROUP);
  const filterValue = ref('');
  const filterOptions = ref<Array<SelectOptionData>>([]);
  const hosts = ref<AuthorizedHostQueryResponse>({} as AuthorizedHostQueryResponse);

  // 过滤输入
  const searchFilter = (searchValue: string, option: SelectOptionData) => {
    if (searchValue.startsWith('@')) {
      // tag 过滤
      return option.isTag && (option.label as string).toLowerCase().startsWith(searchValue.substring(1, searchValue.length).toLowerCase());
    } else {
      // 文本过滤
      return !option.isTag && (option.label as string).toLowerCase().indexOf(searchValue.toLowerCase()) > -1;
    }
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
      return { label: value, value: `@${value}`, isTag: true };
    }).forEach(s => filterOptions.value.push(s));
    // 添加主机信息
    const hostMeta = hosts.value.hostList?.map(s => {
      return [s.name, s.code, s.address, s.alias];
    }).filter(Boolean).flat(1);
    [...new Set(hostMeta)].map(value => {
      return { label: value, value };
    }).forEach(s => filterOptions.value.push(s));
  };

  // 初始化
  const init = async () => {
    try {
      setLoading(true);
      const { data } = await getCurrentAuthorizedHost();
      hosts.value = data;
      // 初始化过滤项
      initFilterOptions();
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
