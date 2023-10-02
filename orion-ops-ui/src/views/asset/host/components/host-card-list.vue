<template>
  <div class="card-list-layout">
    <!-- 头部部分-固定 -->
    <div class="card-list-layout-header" :style="{width: headerWidth}">
      <div class="card-list-layout-header-wrapper">
        <!-- 信息部分 -->
        <div class="card-list-info">
          <!-- 路由面包屑 -->
          <Breadcrumb />
          <!-- 分页部分 -->
          <div class="pagination-wrapper">
            <a-pagination v-if="pagination"
                          size="mini"
                          v-model:current="pagination.current"
                          v-model:page-size="pagination.pageSize"
                          v-bind="pagination" />
          </div>
        </div>
        <!-- 操作部分 -->
        <div class="card-list-handler">
          <!-- 左侧固定 -->
          <div class="card-list-handler-left">
            <a-space>
              <!-- 创建 -->
              <div class="header-icon-wrapper" title="创建">
                <icon-plus />
              </div>
              <!-- 条件显示 -->

            </a-space>
          </div>
          <!-- 右侧固定 -->
          <div class="card-list-handler-right">
            <a-space>
              <!-- 搜索框 -->
              <a-input size="small"
                       placeholder="输入名称/地址"
                       allow-clear />
              <!-- 过滤 -->
              <div class="header-icon-wrapper" title="选择过滤条件">
                <icon-filter />
              </div>
              <!-- 搜索 -->
              <div class="header-icon-wrapper" title="搜索">
                <icon-search />
              </div>
              <!-- 重置 -->
              <div class="header-icon-wrapper" title="重置">
                <icon-refresh />
              </div>
            </a-space>
          </div>
        </div>
      </div>
    </div>
    <!-- 身体部分 -->
    <div class="card-list-layout-body">
      <!-- 卡片列表 -->
      <a-row v-if="list.length !== 0"
             :gutter="cardLayoutGutter">
        <!-- 添加卡片 -->
        <a-col v-if="createCardPosition === 'head'" v-bind="cardLayoutCols">
          <create-card :card-height="cardHeight" :create-card-description="createCardDescription" />
        </a-col>
        <!-- 数据卡片 -->
        <a-col v-for="(item, index) in list"
               :key="item[key]"
               v-bind="cardLayoutCols"
               :class="{
                 'disabled-col': item.disabled === true
               }">
          <a-card :class="{
                    'general-card': true,
                    'card-list-item': true,
                    'card-list-item-disabled': item.disabled === true
                  }"
                  :style="{ height: `${cardHeight}px` }"
                  :body-style="{ height: 'calc(100% - 58px)' }"
                  :bordered="false"
                  :hoverable="true">
            <!-- 标题 -->
            <template #title>
              <slot name="title" :record="item" :index="index" :key="item[key]" />
            </template>
            <!-- 拓展部分 -->
            <template #extra>
              <slot name="extra" :record="item" :index="index" :key="item[key]" />
            </template>
            <!-- 内容 -->
            <slot name="card" :record="item" :index="index" :key="item[key]" />
          </a-card>
        </a-col>
        <!-- 添加卡片 -->
        <a-col v-if="createCardPosition === 'tail'" v-bind="cardLayoutCols">
          <create-card :card-height="cardHeight" :create-card-description="createCardDescription" />
        </a-col>
      </a-row>
      <!-- 空列表 -->
      <template v-else>
        <a-card class="general-card empty-list-card"
                :style="{ height: `${cardHeight * 2 + 16}px` }"
                :body-style="{ height: '100%' }">
          <a-empty :class="{'empty-list-card-body': true, 'empty-list-card-body-creatable': emptyToCreate }"
                   :description="emptyDescription" />
        </a-card>
      </template>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'host-card-list'
  };
</script>

<script setup lang="ts">
  import { compile, computed, h, PropType } from 'vue';
  import { useAppStore } from '@/store';
  import { PaginationProps, ResponsiveValue } from '@arco-design/web-vue';

  const appStore = useAppStore();
  const headerWidth = computed(() => {
    const menuWidth = appStore.menu && !appStore.topMenu && !appStore.hideMenu
      ? appStore.menuCollapse ? 48 : appStore.menuWidth
      : 0;
    return `calc(100% - ${menuWidth}px)`;
  });

  export type Position = 'head' | 'tail' | false

  export interface CardList {
    disabled?: boolean;

    [name: string]: any;
  }

  defineProps({
    key: {
      type: String,
      default: () => 'id'
    },
    pagination: {
      type: Object as PropType<PaginationProps> | PropType<boolean>,
      default: () => false
    },
    cardHeight: Number,
    createCardDescription: {
      type: String,
      default: () => '点击此处进行创建'
    },
    emptyToCreate: {
      type: Boolean,
      default: () => true
    },
    emptyDescription: {
      type: String,
      default: () => '暂无数据 点击此处进行创建'
    },
    createCardPosition: {
      type: String as PropType<Position>,
      default: () => '暂无数据 点击此处进行创建'
    },
    cardLayoutGutter: {
      type: [Number, Array] as PropType<Number> |
        PropType<ResponsiveValue> |
        PropType<Array<Number>> |
        PropType<Array<ResponsiveValue>>,
      default: () => [16, 16]
    },
    cardLayoutCols: {
      type: Object as PropType<ResponsiveValue & { span?: number, offset?: number, order?: number }>,
      default: () => {
        return {
          span: 6
        };
      }
    },
    list: {
      type: Array as PropType<Array<CardList>>,
      default: () => []
    },
  });

  // props
  // const pagination = useCardPagination();
  // const cardHeight = 120;
  // const createCardDescription = '点击此处进行创建';
  // const emptyToCreate = true;
  // const emptyDescription = '暂无数据 点击此处进行创建';
  // // head tail false
  // const createCardPosition = 'head';
  // const cardLayoutGutter = [
  //   { xs: 16, sm: 16, md: 16, lg: 16, xl: 16, xxl: 16 },
  //   { xs: 16, sm: 16, md: 16, lg: 16, xl: 16, xxl: 16 }
  // ];
  // const cardLayoutCols = {
  //   xs: 24,
  //   sm: 12,
  //   md: 8,
  //   lg: 8,
  //   xl: 6,
  //   xxl: 4,
  // };
  // // const cardHeight = 120;
  // // const pagination = useCardPagination();
  // const list = ref<Array<any>>([]);
  //
  // for (let i = 0; i < 270; i++) {
  //   list.value.push({
  //     id: i + 1,
  //     name: `名称 ${i + 1}`,
  //     host: `192.168.1.${i}`
  //   });
  // }
  // pagination.total = 270;

  // 创建卡片
  const CreateCard = {
    props: ['cardHeight', 'createCardDescription'],
    setup(props: { cardHeight: any; createCardDescription: any; }) {
      return () => {
        return h(compile(`
          <a-card class="general-card card-list-item create-card"
                  :style="{ height: '${props.cardHeight}px' }"
                  :body-style="{ height: '100%' }"
                  :bordered="false"
                  :hoverable="true">
            <div class="create-card-body">
              <icon-plus class="create-card-body-icon" />
              <span class="create-card-body-text">${props.createCardDescription}</span>
            </div>
          </a-card>
        `));
      };
    }
  };
</script>

<style scoped lang="less">
  @header-info-height: 48px;
  @header-handler-height: 48px;
  @top-height: 16 + @header-info-height + @header-handler-height + 12px;

  .card-list-layout {

    &-header {
      margin: -16px -16px 0 -16px;
      padding: 16px 16px 12px 16px;
      position: fixed;
      background: var(--color-fill-2);
      z-index: 999;
      height: @top-height;
      transition: none;

      &-wrapper {
        background: var(--color-bg-4);
        padding: 0 12px;
        border-radius: 4px;
      }
    }

    &-body {
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

    .card-list-info {
      height: @header-info-height;
      border-bottom: 1px solid var(--color-border-2);
      display: flex;
      justify-content: space-between;
      align-items: center;
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

  }

  .header-icon-wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 27px;
    padding: 6px;
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

  :deep(.create-card) {

    &-body {
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
        user-select: none;
      }

    }
  }

  .empty-list-card {

    &-body {
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      flex-direction: column;
      padding: 0;
    }

    &-body-creatable {
      cursor: pointer;
    }
  }

</style>
