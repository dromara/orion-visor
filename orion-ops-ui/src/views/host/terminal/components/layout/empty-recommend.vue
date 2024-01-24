<template>
  <div class="terminal-setting-container">
    <div class="terminal-setting-wrapper">
      <!-- 组合容器 -->
      <div class="combined-container">
        <!-- 新建连接 -->
        <div class="combined-handler" v-for="(handler, index) in combinedHandlers"
             :key="index"
             @click="clickHandlerItem(handler)">
          <!-- 图标 -->
          <div class="combined-handler-icon">
            <component :is="handler.icon" />
          </div>
          <!-- 内容 -->
          <div class="combined-handler-text">
            {{ handler.title }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'emptyRecommend'
  };
</script>

<script lang="ts" setup>
  import type { TerminalTabItem, CombinedHandlerItem } from '../../types/terminal.type';
  import type { HostQueryResponse } from '@/api/asset/host';
  import { onMounted, ref } from 'vue';
  import { useTerminalStore } from '@/store';
  import { InnerTabs, TerminalTabType } from '../../types/terminal.const';
  import { get } from 'lodash';

  const totalCount = 7;
  const { tabManager, hosts, openTerminal } = useTerminalStore();

  const combinedHandlers = ref<Array<CombinedHandlerItem>>([{
    title: InnerTabs.NEW_CONNECTION.title,
    settingTab: InnerTabs.NEW_CONNECTION,
    type: TerminalTabType.SETTING,
    icon: InnerTabs.NEW_CONNECTION.icon
  }]);

  // 点击组合操作元素
  const clickHandlerItem = (item: CombinedHandlerItem) => {
    if (item.type === TerminalTabType.SETTING) {
      // 打开内置 tab
      tabManager.openTab(item.settingTab as TerminalTabItem);
    } else {
      // 打开终端
      openTerminal(item.host as HostQueryResponse);
    }
  };

  // 组合主机列表
  onMounted(() => {
    // 推荐的主机 tab
    const combinedHosts = [
      ...new Set([
        ...hosts.latestHosts,
        ...hosts.hostList.filter(s => s.favorite).map(s => s.id),
        ...hosts.hostList.map(s => s.id)
      ])
    ].slice(0, totalCount - 1)
      .map(s => hosts.hostList.find(t => t.id === s) as HostQueryResponse)
      .filter(Boolean)
      .map(s => {
        return {
          title: `${s.alias || s.name} (${s.address})`,
          type: TerminalTabType.TERMINAL,
          host: s,
          icon: 'icon-desktop'
        };
      });
    // 插入主机列表
    combinedHandlers.value.push(...combinedHosts);
    // 不足显示的行数用设置补充
    if (totalCount - 1 - combinedHosts.length > 0) {
      const fillTabs = Object.keys(InnerTabs)
        .filter(s => s !== 'NEW_CONNECTION')
        .map(s => get(InnerTabs, s) as TerminalTabItem)
        .slice(0, totalCount - 1 - combinedHosts.length)
        .map(s => {
          return {
            title: s.title,
            settingTab: s,
            type: TerminalTabType.SETTING,
            icon: s.icon as string
          };
        });
      combinedHandlers.value.push(...fillTabs);
    }
  });

</script>

<style lang="less" scoped>
  @transform-x: 8px;
  @container-width: 406px;
  @container-height: 448px;
  @handler-height: 44px;

  .combined-container {
    padding: 12px;
    margin: 64px auto;
    width: @container-width;
    height: @container-height;
    display: flex;
    flex-direction: column;
    align-items: center;
    box-sizing: content-box;
    overflow: hidden;

    &:hover {
      overflow: auto;
    }
  }

  .combined-handler {
    width: calc(@container-width - @transform-x);
    height: @handler-height;
    border-radius: 4px;
    margin-bottom: 6px;
    color: var(--color-content-text-1);
    background-color: var(--color-fill-2);
    display: flex;
    align-items: center;
    cursor: pointer;
    transition: all 0.2s;

    &:hover {
      width: @container-width;
    }

    &-icon {
      width: @handler-height;
      height: @handler-height;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 16px;
    }

    &-text {
      height: 100%;
      width: calc(100% - @handler-height - 12px);
      display: flex;
      align-items: center;
      font-size: 12px;

      &-wrapper {
        display: block;
        overflow: hidden;
        white-space: pre;
        text-overflow: ellipsis;
      }
    }
  }
</style>
