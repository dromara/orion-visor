<template>
  <div class="card-list-layout">
    <!-- 头部固定 -->
    <div class="card-list-layout-top" :style="{width: `calc(100% - ${menuWidth}px)`}">
      <!-- 路由面包屑 -->
      <Breadcrumb />
      <!-- header -->
      <div class="card-list-header">
        <!-- 左侧固定 -->
        <div class="card-list-header-left">
          <a-space>
            <!-- 创建 -->
            <div class="header-icon-wrapper">
              <icon-plus />
            </div>
            <!-- 条件显示 -->

          </a-space>
        </div>
        <!-- 右侧固定 -->
        <div class="card-list-header-right">
          <a-space>
            <!-- 搜索框 -->
            <a-input size="small" placeholder="输入名称/地址">
              <template #suffix>
                <icon-search />
              </template>
            </a-input>
            <!-- 条件 -->
            <div class="header-icon-wrapper">
              <icon-filter />
            </div>
            <!-- 刷新 -->
            <div class="header-icon-wrapper">
              <icon-refresh />
            </div>
          </a-space>
        </div>
      </div>
    </div>
    <!-- 固定身体 -->
    <div class="card-list-layout-body">
      <!-- 卡片列表 -->
      <a-row v-if="list.length === 0"
             :gutter="cardLayoutGutter">
        <!-- 数据卡片 -->
        <a-col v-for="item in list"
               :key="item.id"
               v-bind="cardLayoutCols">
          <a-card class="general-card card-list-item"
                  :bordered="false"
                  :hoverable="true">
            <template #title>
              {{ item.name }}
            </template>
            <template #extra>
              <icon-more />
            </template>
          </a-card>
        </a-col>
        <!-- 添加卡片 -->
        <a-col v-bind="cardLayoutCols">
          <a-card class="general-card card-list-item"
                  :body-style="{ height: '100%' }"
                  :bordered="false"
                  :hoverable="true">
            <div class="create-card-body">
              <icon-plus class="create-card-body-icon" />
              <span class="create-card-body-text">点击进行创建</span>
            </div>
          </a-card>
        </a-col>
      </a-row>
      <!-- 空列表 -->
      <template v-else>
        <a-card class="general-card empty-list-card"
                :body-style="{ height: '100%' }">
          <a-empty description="暂无数据 点击进行创建" />
        </a-card>
      </template>
      <!-- 翻页区域 -->
      <div>
        page 区域
      </div>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'host-card-list'
  };
</script>

<script setup lang="ts">
  import { computed, ref } from 'vue';
  import { useAppStore } from '@/store';

  const appStore = useAppStore();
  const menuWidth = computed(() => {
    return appStore.menu && !appStore.topMenu && !appStore.hideMenu
      ? appStore.menuCollapse ? 48 : appStore.menuWidth
      : 0;
  });

  const cardLayoutGutter = [
    { xs: 16, sm: 16, md: 16 },
    { xs: 16, sm: 16, md: 16 }
  ];

  const cardLayoutCols = {
    xs: 24,
    sm: 12,
    md: 8,
    lg: 8,
    xl: 6,
    xxl: 4,
  };

  const list = ref<Array<any>>([]);

  for (let i = 0; i < 18; i++) {
    list.value.push({
      id: i + 1,
      name: `名称 ${i + 1}`,
      host: `192.168.1.${i}`
    });
  }

</script>

<style scoped lang="less">
  @header-height: 48;
  @top-width: 16 + 24 + 16 + 48 + 16;
  @header-height-px: @{header-height}px;
  @top-width-px: @{top-width}px;
  @card-height: 120;
  @card-height-px: @{card-height}px;
  @empty-list-card-height: 120 * 2 + 16;
  @empty-list-card-height-px: @{empty-list-card-height}px;

  .card-list-layout-body {
    margin-top: @top-width-px;
  }

  .card-list-layout {

    &-top {
      padding: 16px;
      position: fixed;
      background: var(--color-fill-2);
      z-index: 999;
      height: @top-width-px;
      transition: none;
    }

    &-body {
      padding: 0 16px 16px 16px;
    }

    .card-list-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      background: var(--color-bg-4);
      height: @header-height-px;
      padding: 12px;
      margin-top: 16px;

      &-left {
        display: flex;
        align-items: center;
      }

      &-right {
        display: flex;
        align-items: center;
      }
    }

  }

  .header-icon-wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 27px;
    height: 27px;
    color: var(--color-text-2);
    background: var(--color-fill-2);
    border-radius: 2px;
    cursor: pointer;
    border: 1px solid transparent;
    transition: background-color 0.1s cubic-bezier(0, 0, 1, 1);
  }

  .header-icon-wrapper:hover {
    background: var(--color-fill-3);
  }

  .card-list-item {
    height: @card-height-px;
    transition-property: all;
  }

  .card-list-item:hover {
    transform: translateY(-4px);
    box-shadow: 2px 2px 12px rgba(0, 0, 0, .15);
  }

  .create-card-body {
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    flex-direction: column;
    justify-content: center;
    cursor: pointer;

    &-icon {
      font-size: 18px;
      margin-bottom: 4px;
    }

    &-text {
      color: rgb(var(--arcoblue-6)) !important;
    }
  }

  .empty-list-card {
    height: @empty-list-card-height-px;
  }

  .empty-list {
    height: 100%;
    display: flex;
    align-items: center;
    flex-direction: column;
    justify-content: center;
  }

</style>
