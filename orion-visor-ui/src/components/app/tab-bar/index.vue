<template>
  <div class="tab-bar-container">
    <a-affix ref="affixRef" :offset-top="offsetTop">
      <div class="tab-bar-box">
        <div class="tab-bar-scroll">
          <div class="tags-wrap">
            <tab-item v-for="(tag, index) in tagList"
                      :key="tag.fullPath"
                      :closeable="tagList.length > 1"
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
  import { getRouteTag, getRouteTitle } from '@/router';
  import { listenerRouteChange, removeRouteListener } from '@/utils/route-listener';
  import { useAppStore, useTabBarStore } from '@/store';
  import qs from 'query-string';
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

  // 监听修改位置
  watch(() => appStore.navbar, () => {
    affixRef.value.updatePosition();
  });

  // 监听路由变化
  listenerRouteChange((route: RouteLocationNormalized) => {
    // 不固定
    if (route.meta.noAffix) {
      return;
    }

    const tag = tagList.value.find((tag) => tag.path === route.path);
    if (tag) {
      if (route.meta.multipleTab) {
        // 找到 支持多标签 通过全路径去找
        const fullTag = tagList.value.find((tag) => tag.fullPath === route.fullPath);
        if (fullTag) {
          return;
        }
        // 没有通过全路径找到则打开新的页签
        tabBarStore.addTab(getRouteTag(route), route.meta?.ignoreCache as unknown as boolean);
      } else {
        // 找到 更新信息
        tag.fullPath = route.fullPath;
        tag.query = qs.parseUrl(route.fullPath).query;
        tag.title = getRouteTitle(route);
      }
    } else {
      // 未找到 添加标签
      tabBarStore.addTab(getRouteTag(route), route.meta?.ignoreCache as unknown as boolean);
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
