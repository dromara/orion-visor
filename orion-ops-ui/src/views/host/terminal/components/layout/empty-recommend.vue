<template>
  <div class="terminal-setting-container">
    <div class="terminal-setting-wrapper">
      <!-- 组合容器 -->
      <div class="combined-container">
        <div class="combined-handler" v-for="(handler, index) in combinedHandlers"
             :key="index">
          <!-- 左侧固定 -->
          <div class="combined-handler-left">
            <!-- 图标 -->
            <div class="combined-handler-icon">
              <component :is="handler.icon" />
            </div>
            <!-- 内容 -->
            <div class="combined-handler-text">
              {{ handler.title }}
            </div>
          </div>
          <!-- 操作 -->
          <div class="combined-handler-actions">
            <!-- 跳转页面 -->
            <a-tooltip position="top"
                       :mini="true"
                       :auto-fix-position="false"
                       content-class="terminal-tooltip-content"
                       arrow-class="terminal-tooltip-content"
                       content="跳转页面">
              <span v-if="!handler.host"
                    class="click-icon-wrapper"
                    @click="openTab(handler)">
                <icon-right />
              </span>
            </a-tooltip>
            <!-- 打开 SSH -->
            <a-tooltip position="top"
                       :mini="true"
                       :auto-fix-position="false"
                       content-class="terminal-tooltip-content"
                       arrow-class="terminal-tooltip-content"
                       content="打开 SSH">
              <span v-if="handler.host"
                    class="click-icon-wrapper"
                    @click="openSession(handler.host as any, PanelSessionType.SSH)">
                <icon-thunderbolt />
              </span>
            </a-tooltip>
            <!-- 打开 SFTP -->
            <a-tooltip position="top"
                       :mini="true"
                       :auto-fix-position="false"
                       content-class="terminal-tooltip-content"
                       arrow-class="terminal-tooltip-content"
                       content="打开 SFTP">
              <span v-if="handler.host"
                    class="click-icon-wrapper"
                    @click="openSession(handler.host as any, PanelSessionType.SFTP)">
                <icon-folder />
              </span>
            </a-tooltip>
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
  import { PanelSessionType, TerminalTabs } from '../../types/terminal.const';

  const totalCount = 7;
  const { tabManager, hosts, openSession } = useTerminalStore();

  const combinedHandlers = ref<Array<CombinedHandlerItem>>([{
    title: TerminalTabs.NEW_CONNECTION.title,
    tab: TerminalTabs.NEW_CONNECTION,
    icon: TerminalTabs.NEW_CONNECTION.icon
  }]);

  // 打开 tab
  const openTab = (item: CombinedHandlerItem) => {
    if (item.host) {
      return;
    }
    tabManager.openTab(item.tab as TerminalTabItem);
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
          host: s,
          icon: 'icon-desktop'
        };
      });
    // 插入主机列表
    combinedHandlers.value.push(...combinedHosts);
    // 不足显示的行数用设置补充
    if (totalCount - 1 - combinedHosts.length > 0) {
      const fillTabs = Object.values(TerminalTabs)
        .filter(s => s.key !== TerminalTabs.NEW_CONNECTION.key)
        .slice(0, totalCount - 1 - combinedHosts.length)
        .map(s => {
          return {
            title: s.title,
            tab: s,
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
  @icon-size: @handler-height;
  @actions-width: 80px;

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
    width: @container-width - @transform-x;
    height: @handler-height;
    border-radius: 4px;
    margin-bottom: 6px;
    color: var(--color-content-text-1);
    background-color: var(--color-fill-2);
    display: flex;
    align-items: center;
    justify-content: space-between;
    transition: all 0.2s;

    &:hover {
      width: @container-width;

      .combined-handler-left {
        width: calc(100% - @actions-width);
      }

      .combined-handler-actions {
        display: flex;
      }
    }

    &-left {
      width: 100%;
      display: flex;
      align-items: center;
    }

    &-icon {
      width: @icon-size;
      height: @icon-size;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 16px;
    }

    &-text {
      height: 100%;
      width: calc(100% - @icon-size - 12px);
      display: flex;
      align-items: center;
      font-size: 12px;
      overflow: hidden;
      white-space: nowrap;
    }

    &-actions {
      display: none;
      width: @actions-width;
      height: 100%;
      padding-right: 8px;
      align-items: center;
      justify-content: flex-end;

      .click-icon-wrapper {
        font-size: 18px;
        padding: 4px;
        border-radius: 4px;
        margin: 0 4px;

        &:hover {
          background: var(--color-neutral-4);
        }
      }
    }
  }
</style>
