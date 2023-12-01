<template>
  <div class="tab-bar-container">
    <a-affix ref="affixRef" :offset-top="offsetTop">
      <div class="tab-bar-box">
        <div class="tab-bar-scroll">
          <div class="tags-wrap">
            <TabItem v-for="(tag, index) in tagList"
                     :key="tag.fullPath"
                     :index="index"
                     :item-data="tag" />
          </div>
        </div>
        <div class="tag-bar-operation"></div>
      </div>
    </a-affix>
  </div>
</template>

<script lang="ts" setup>
  import type { RouteLocationNormalized } from 'vue-router';
  import { computed, onUnmounted, ref, watch } from 'vue';
  import { routerToTag } from '@/router/constants';
  import { listenerRouteChange, removeRouteListener, } from '@/utils/route-listener';
  import { useAppStore, useTabBarStore } from '@/store';
  import TabItem from './tab-item.vue';

  const appStore = useAppStore();
  const tabBarStore = useTabBarStore();

  const affixRef = ref();
  const tagList = computed(() => {
    return tabBarStore.getTabList;
  });
  const offsetTop = computed(() => {
    return appStore.navbar ? 60 : 0;
  });

  watch(
    () => appStore.navbar,
    () => {
      affixRef.value.updatePosition();
    }
  );

  // 监听路由变化
  listenerRouteChange((route: RouteLocationNormalized) => {
    if (
      !route.meta.noAffix &&
      !tagList.value.some((tag) => tag.path === route.path)
    ) {
      // 固定并且没有此 tab 则添加
      tabBarStore.addTab(routerToTag(route), route.meta?.ignoreCache as unknown as boolean);
    }
  }, true);

  onUnmounted(() => {
    removeRouteListener();
  });
</script>

<style lang="less" scoped>
  .tab-bar-container {
    position: relative;
    background-color: var(--color-bg-2);

    .tab-bar-box {
      display: flex;
      padding: 0 0 0 6px;
      background-color: var(--color-bg-2);
      border-bottom: 1px solid var(--color-border);

      .tab-bar-scroll {
        height: 32px;
        flex: 1;
        overflow: hidden;

        .tags-wrap {
          padding: 4px 0;
          height: 48px;
          white-space: nowrap;
          overflow-x: auto;

          :deep(.arco-tag) {
            display: inline-flex;
            align-items: center;
            margin-right: 6px;
            cursor: pointer;

            &:first-child {
              .arco-tag-close-btn {
                display: none;
              }
            }

            .tag-link {
              user-select: none;
            }
          }
        }
      }
    }

    .tag-bar-operation {
      width: 100px;
      height: 32px;
    }
  }
</style>
