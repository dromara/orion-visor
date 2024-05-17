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
                       :options="toRadioOptions(newConnectionTypeKey)"
                       @change="s => updateTerminalPreference(TerminalPreferenceItem.NEW_CONNECTION_TYPE, s as string, true)" />
        <!-- 过滤 -->
        <a-auto-complete v-model="filterValue"
                         class="host-filter"
                         placeholder="别名/名称/编码/IP @标签"
                         :allow-clear="true"
                         :data="filterOptions"
                         :filter-option="tagLabelFilter">
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
          <!-- 无数据 -->
          <a-empty v-if="!hosts.hostList?.length">
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
  import { onBeforeMount, ref } from 'vue';
  import { NewConnectionType, newConnectionTypeKey } from '../../types/terminal.const';
  import { useDictStore, useTerminalStore } from '@/store';
  import { TerminalPreferenceItem } from '@/store/modules/terminal';
  import { dataColor } from '@/utils';
  import { tagLabelFilter } from '@/types/form';
  import { tagColor } from '@/views/asset/host-list/types/const';
  import { getAuthorizedHostOptions } from '@/types/options';
  import HostsView from './hosts-view.vue';

  const { toRadioOptions } = useDictStore();
  const { preference, updateTerminalPreference, hosts } = useTerminalStore();

  const newConnectionType = ref(preference.newConnectionType || NewConnectionType.GROUP);
  const filterValue = ref('');
  const filterOptions = ref<Array<SelectOptionData>>([]);

  // 初始化过滤器项
  onBeforeMount(() => {
    filterOptions.value = getAuthorizedHostOptions(hosts.hostList);
  });

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

    .host-view-container {
      width: 100%;
      height: calc(100vh - 240px);
      position: relative;
    }
  }

</style>
