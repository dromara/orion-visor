<template>
  <!-- 头部部分-固定 -->
  <div class="card-list-header" :style="{width: headerWidth}">
    <div class="card-list-header-wrapper">
      <!-- 信息部分 -->
      <div class="card-list-info">
        <!-- 路由面包屑 -->
        <breadcrumb />
        <!-- 分页部分 -->
        <div class="pagination-wrapper">
          <a-pagination v-if="pagination"
                        size="mini"
                        v-model:current="(pagination as PaginationProps).current"
                        v-model:page-size="(pagination as PaginationProps).pageSize"
                        v-bind="pagination as any"
                        :auto-adjust="false"
                        @change="page => bubblesEmitter(HeaderEmitter.PAGE_CHANGE, page, (pagination as PaginationProps).pageSize)"
                        @page-size-change="limit => bubblesEmitter(HeaderEmitter.PAGE_CHANGE, 1, limit)" />
        </div>
      </div>
      <!-- 操作部分 -->
      <div class="card-list-handler">
        <!-- 左侧固定 -->
        <div class="card-list-handler-left">
          <a-space>
            <!-- 创建 -->
            <div v-permission="addPermission"
                 v-if="!handleVisible?.disableAdd"
                 class="click-icon-wrapper card-header-icon-wrapper"
                 title="创建"
                 @click="bubblesEmitter(HeaderEmitter.ADD)">
              <icon-plus />
            </div>
            <!-- 左侧侧操作槽位 -->
            <slot name="leftHandle" />
          </a-space>
        </div>
        <!-- 右侧固定 -->
        <div class="card-list-handler-right">
          <a-space>
            <!-- 右侧操作槽位 -->
            <slot name="rightHandle" />
            <!-- 搜索框 -->
            <div v-if="!handleVisible?.disableSearchInput"
                 class="header-input-wrapper"
                 :style="{width: searchInputWidth}">
              <a-input v-model="searchValueRef"
                       :placeholder="searchInputPlaceholder as string"
                       size="small"
                       allow-clear
                       @input="e => bubblesEmitter(HeaderEmitter.UPDATE_SEARCH_VALUE, e)"
                       @change="e => bubblesEmitter(HeaderEmitter.UPDATE_SEARCH_VALUE, e)"
                       @keyup.enter="bubblesEmitter(HeaderEmitter.SEARCH)" />
            </div>
            <!-- 过滤条件 -->
            <a-popover position="br" trigger="click" content-class="card-filter-wrapper">
              <div v-if="!handleVisible?.disableFilter"
                   ref="filterRef"
                   class="click-icon-wrapper card-header-icon-wrapper"
                   title="选择过滤条件">
                <a-badge :count="filterCount as number" :dot-style="{zoom: '.75'}" :offset="[9, -6]">
                  <icon-filter />
                </a-badge>
              </div>
              <template #content>
                <div class="card-filter-container">
                  <!-- 过滤表单 -->
                  <slot name="filterContent" />
                  <!-- 操作按钮 -->
                  <div class="card-filter-footer">
                    <a-space>
                      <a-button size="mini" @click="filterReset">重置</a-button>
                      <a-button size="mini" type="primary" @click="filterSearch">过滤</a-button>
                    </a-space>
                  </div>
                </div>
              </template>
            </a-popover>
            <!-- 搜索 -->
            <div v-if="!handleVisible?.disableSearch"
                 class="click-icon-wrapper card-header-icon-wrapper"
                 title="搜索"
                 @click="bubblesEmitter(HeaderEmitter.SEARCH)">
              <icon-search />
            </div>
            <!-- 重置 -->
            <div v-if="!handleVisible?.disableReset"
                 class="click-icon-wrapper card-header-icon-wrapper"
                 title="重置"
                 @click="bubblesEmitter(HeaderEmitter.RESET)">
              <icon-refresh />
            </div>
          </a-space>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'cardHeader'
  };
</script>

<script lang="ts" setup>
  import type { PaginationProps } from '@arco-design/web-vue';
  import type { CardProps } from '../types/props';
  import { ref, computed } from 'vue';
  import { useAppStore } from '@/store';
  import { triggerMouseEvent } from '@/utils/event';
  import { HeaderEmitter } from '../types/emits';
  import useEmitter from '@/hooks/emitter';

  const props = defineProps<CardProps>();
  const emits = defineEmits(['emitter']);

  const { bubblesEmitter } = useEmitter(emits);

  const appStore = useAppStore();

  const filterRef = ref();

  // 头部长度 fixed 脱离了文档流 需要计算
  const headerWidth = computed(() => {
    const menuWidth = appStore.menu && !appStore.topMenu && !appStore.hideMenu
      ? appStore.menuCollapse ? 48 : appStore.menuWidth
      : 0;
    return `calc(100% - ${menuWidth}px)`;
  });

  const searchValueRef = computed<string>({
    get() {
      return props.searchValue as string;
    },
    set(e) {
      if (e) {
        bubblesEmitter(HeaderEmitter.UPDATE_SEARCH_VALUE, e);
      } else {
        bubblesEmitter(HeaderEmitter.UPDATE_SEARCH_VALUE, null);
      }
    }
  });

  // 重置过滤
  const filterReset = () => {
    bubblesEmitter(HeaderEmitter.RESET);
  };

  // 搜索
  const filterSearch = () => {
    bubblesEmitter(HeaderEmitter.SEARCH);
    triggerMouseEvent(filterRef);
  };

</script>

<style lang="less" scoped>
  body[arco-theme='dark'] .card-list-header {
    background: #29292C;
  }

  .card-list-header {
    margin: -17px -16px 0 -15px;
    padding: 16px 16px 12px 16px;
    position: fixed;
    background: var(--color-fill-2);
    z-index: 999;
    height: var(--top-height);
    transition: none;

    &-wrapper {
      background: var(--color-bg-2);
      padding: 0 12px;
      border-radius: 4px;

      .card-list-info {
        height: var(--header-info-height);
        border-bottom: 1px solid var(--color-border-2);
        display: flex;
        justify-content: space-between;
        align-items: center;
      }
    }
  }

  .card-list-handler {
    height: var(--header-handler-height);
    display: flex;
    justify-content: space-between;
    align-items: center;

    &-left {
      display: flex;
      align-items: center;
    }

    &-right {
      display: flex;
      align-items: center;
    }
  }

</style>
