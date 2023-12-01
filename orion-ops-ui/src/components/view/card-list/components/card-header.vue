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
                        v-bind="pagination"
                        :auto-adjust="false"
                        @change="page => emits('pageChange', page, (pagination as PaginationProps).pageSize)"
                        @page-size-change="limit => emits('pageChange', 1, limit)" />
        </div>
      </div>
      <!-- 操作部分 -->
      <div class="card-list-handler">
        <!-- 左侧固定 -->
        <div class="card-list-handler-left">
          <a-space>
            <!-- 创建 -->
            <div v-permission="addPermission"
                 v-if="!handleVisible.disableAdd"
                 class="click-icon-wrapper card-header-icon-wrapper"
                 title="创建"
                 @click="emits('add')">
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
            <div v-if="!handleVisible.disableSearchInput"
                 class="header-input-wrapper"
                 :style="{width: searchInputWidth}">
              <a-input v-model="searchValueRef"
                       :placeholder="searchInputPlaceholder"
                       size="small"
                       allow-clear
                       @input="e => emits('update:searchValue', e)"
                       @change="e => emits('update:searchValue', e)"
                       @keyup.enter="emits('search')" />
            </div>
            <!-- 过滤条件 -->
            <a-popover position="br" trigger="click" content-class="card-filter-wrapper">
              <div v-if="!handleVisible.disableFilter"
                   ref="filterRef"
                   class="click-icon-wrapper card-header-icon-wrapper"
                   title="选择过滤条件">
                <a-badge :count="filterCount" :dot-style="{zoom: '.75'}" :offset="[9, -6]">
                  <icon-filter />
                </a-badge>
              </div>
              <template #content>
                <!-- 过滤表单 -->
                <slot name="filterContent" />
                <!-- 操作按钮 -->
                <div class="filter-bottom-container">
                  <a-space>
                    <a-button size="mini" @click="filterReset">重置</a-button>
                    <a-button size="mini" type="primary" @click="filterSearch">过滤</a-button>
                  </a-space>
                </div>
              </template>
            </a-popover>
            <!-- 搜索 -->
            <div v-if="!handleVisible.disableSearch"
                 class="click-icon-wrapper card-header-icon-wrapper"
                 title="搜索"
                 @click="emits('search')">
              <icon-search />
            </div>
            <!-- 重置 -->
            <div v-if="!handleVisible.disableReset"
                 class="click-icon-wrapper card-header-icon-wrapper"
                 title="重置"
                 @click="emits('reset')">
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
    name: 'card-header'
  };
</script>

<script lang="ts" setup>
  import type { PaginationProps } from '@arco-design/web-vue';
  import type { CardProps } from '../types/props';
  import { useAppStore } from '@/store';
  import { ref, computed } from 'vue';
  import { triggerMouseEvent } from '@/utils';

  const props = defineProps<CardProps>();

  const appStore = useAppStore();
  const headerWidth = computed(() => {
    const menuWidth = appStore.menu && !appStore.topMenu && !appStore.hideMenu
      ? appStore.menuCollapse ? 48 : appStore.menuWidth
      : 0;
    return `calc(100% - ${menuWidth}px)`;
  });

  const filterRef = ref();

  const searchValueRef = computed<string>({
    get() {
      return props.searchValue as string;
    },
    set(e) {
      if (e) {
        emits('update:searchValue', e);
      } else {
        emits('update:searchValue', null);
      }
    }
  });

  const emits = defineEmits(['add', 'update:searchValue', 'search',
    'reset', 'pageChange', 'click', 'dblclick']);

  // 重置过滤
  const filterReset = () => {
    emits('reset');
  };

  // 搜索
  const filterSearch = () => {
    emits('search');
    triggerMouseEvent(filterRef);
  };

</script>

<style lang="less">
  @header-info-height: 48px;
  @header-handler-height: 48px;
  @top-height: 16 + @header-info-height + @header-handler-height + 12px;

  .card-list-header {
    margin: -16px -16px 0 -16px;
    padding: 16px 16px 12px 16px;
    position: fixed;
    // FIXME 颜色不对   文件拆了
    background: var(--color-fill-2);
    z-index: 999;
    height: @top-height;
    transition: none;

    &-wrapper {
      background: var(--color-bg-4);
      padding: 0 12px;
      border-radius: 4px;

      .card-list-info {
        height: @header-info-height;
        border-bottom: 1px solid var(--color-border-2);
        display: flex;
        justify-content: space-between;
        align-items: center;
      }
    }
  }

  &-body {
    display: block;
    margin-top: @top-height - 16px;
    padding-top: 4px;
  }

  .disabled-col {
    cursor: not-allowed;

    .card-list-item-disabled {
      pointer-events: none;
      opacity: .5;
      background: var(--color-bg-1);
    }
  }


  .card-list-handler {
    height: @header-handler-height;
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
