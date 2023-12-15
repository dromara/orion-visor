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
        <a-input-search v-model="filterValue"
                        class="host-filter"
                        placeholder="输入名称/编码/IP @标签"
                        :allow-clear="true" />
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
                             :hosts="hosts" />
            <!-- 列表视图 -->
            <host-list-view v-if="NewConnectionType.LIST === newConnectionType"
                             :hosts="hosts" />

            <!-- 我的收藏 -->

            <!-- 最近连接 -->

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
  import type { AuthorizedHostQueryResponse } from '@/api/asset/asset-authorized-data';
  import { getCurrentAuthorizedHost } from '@/api/asset/asset-authorized-data';
  import { onBeforeMount, ref } from 'vue';
  import { NewConnectionType, NewConnectionTypeKey } from '../../types/terminal.const';
  import useLoading from '@/hooks/loading';
  import { useDictStore } from '@/store';
  import HostGroupView from './host-group-view.vue';
  import HostListView from './host-list-view.vue';

  const { loading, setLoading } = useLoading();
  const { toOptions } = useDictStore();

  const newConnectionType = ref(NewConnectionType.GROUP);
  const filterValue = ref();
  const hosts = ref<AuthorizedHostQueryResponse>({} as AuthorizedHostQueryResponse);

  // 修改连接类型
  const changeConnectionType = () => {
    // FIXME 持久化
  };

  // 加载主机信息
  onBeforeMount(async () => {
    try {
      setLoading(true);
      const { data } = await getCurrentAuthorizedHost();
      hosts.value = data;
    } finally {
      setLoading(false);
    }
  });
</script>

<style lang="less" scoped>
  .header-actions {
    display: flex;
    align-items: center;
    justify-content: space-between;

    .host-filter {
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
