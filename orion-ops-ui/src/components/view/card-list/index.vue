<template>
  <div class="card-list-layout">
    <!-- 头部部分-固定 -->
    <div class="card-list-layout-header" :style="{width: headerWidth}">
      <div class="card-list-layout-header-wrapper">
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
                   v-show="!handleVisible.disableAdd"
                   class="click-icon-wrapper header-icon-wrapper"
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
                     class="click-icon-wrapper header-icon-wrapper"
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
                   class="click-icon-wrapper header-icon-wrapper"
                   title="搜索"
                   @click="emits('search')">
                <icon-search />
              </div>
              <!-- 重置 -->
              <div v-if="!handleVisible.disableReset"
                   class="click-icon-wrapper header-icon-wrapper"
                   title="重置"
                   @click="emits('reset')">
                <icon-refresh />
              </div>
            </a-space>
          </div>
        </div>
      </div>
    </div>
    <!-- 身体部分 -->
    <a-spin class="card-list-layout-body" :loading="loading">
      <!-- 卡片列表 -->
      <a-row v-if="list.length !== 0"
             :gutter="cardLayoutGutter"
             align="stretch">
        <!-- 添加卡片 -->
        <a-col v-permission="addPermission"
               v-if="createCardPosition === 'head'"
               v-bind="cardLayoutCols">
          <create-card :card-height="cardHeight"
                       :create-card-description="createCardDescription"
                       @click="emits('add')" />
        </a-col>
        <!-- 数据卡片 -->
        <a-col v-for="(item, index) in list"
               :key="item[key]"
               v-bind="cardLayoutCols"
               :class="{
                 'disabled-col': item.disabled === true
               }">
          <!-- 右键菜单 -->
          <a-dropdown trigger="contextMenu" alignPoint>
            <!-- 卡片 -->
            <a-card :class="[
                      'card-list-item',
                      item.disabled === true ? 'card-list-item-disabled' : undefined,
                      !!cardClass ? cardClass : undefined
                    ]"
                    :style="{ height: cardHeight }"
                    :bordered="false"
                    :hoverable="true"
                    :body-style="cardBodyStyle"
                    @contextmenu.prevent="() => false"
                    @click="emits('click', item, index)"
                    @dblclick="emits('dblclick', item, index)">
              <!-- 标题 -->
              <template #title>
                <slot name="title" :record="item" :index="index" :key="item[key]" />
              </template>
              <!-- 拓展部分 -->
              <template #extra>
                <slot name="extra" :record="item" :index="index" :key="item[key]" />
              </template>
              <!-- 内容-字段 -->
              <template v-if="fieldConfig && fieldConfig.fields">
                <div :class="['fields-container', fieldConfig.bodyClass]">
                  <template v-for="(field, index) in fieldConfig.fields">
                    <a-row :align="fieldConfig.rowAlign || field.rowAlign || 'center'"
                           :style="{
                           'margin-bottom': index !== fieldConfig.fields.length - 1 ? (fieldConfig.rowGap || '12px') : false,
                           'height': fieldConfig.height || field.height || 'unset',
                           'min-height': fieldConfig.minHeight || field.minHeight || 'unset'
                         }">
                      <!-- label -->
                      <a-col :span="fieldConfig.labelSpan || 8" :offset="fieldConfig.labelOffset || 0"
                             :style="{
                             'text-align': fieldConfig.labelAlign || 'left'
                           }"
                             :class="[fieldConfig.labelClass,
                             field.labelClass,
                             'field-label'
                           ]">
                        <span>{{ field.label + (fieldConfig.showColon ? ' :' : '') }}</span>
                      </a-col>
                      <!-- value -->
                      <a-col :span="24 - (fieldConfig.labelSpan || 8) + (fieldConfig.labelOffset || 0)"
                             :style="{
                             'text-align': fieldConfig.valueAlign || 'left'
                           }"
                             :class="[fieldConfig.valueClass,
                             field.valueClass,
                             'field-value',
                             field.ellipsis ? 'field-value-ellipsis' : ''
                           ]">
                        <slot :name="field.slotName" :record="item" :index="index" :key="item[key]">
                          <a-tooltip v-if="field.tooltip" :content="item[field.dataIndex]">
                            <span v-if="field.render" v-html="field.render({ record: item, index })" />
                            <span v-else>{{ item[field.dataIndex] }}</span>
                          </a-tooltip>
                          <template v-else>
                            <span v-if="field.render" v-html="field.render({ record: item, index })" />
                            <span v-else>{{ item[field.dataIndex] }}</span>
                          </template>
                        </slot>
                      </a-col>
                    </a-row>
                  </template>
                </div>
              </template>
              <!-- 内容-自定义槽 -->
              <template v-else>
                <slot name="card" :record="item" :index="index" :key="item[key]" />
              </template>
              <!-- 卡片底部 -->
              <slot name="cardFooter" :record="item" :index="index" :key="item[key]" />
            </a-card>
            <!-- 右键菜单 -->
            <template v-if="contextMenu" #content>
              <slot name="contextMenu" :record="item" :index="index" :key="item[key]" />
            </template>
          </a-dropdown>
        </a-col>
        <!-- 添加卡片 -->
        <a-col v-permission="addPermission"
               v-show="createCardPosition === 'tail'"
               v-bind="cardLayoutCols">
          <create-card :card-height="cardHeight"
                       :create-card-description="createCardDescription"
                       @click="emits('add')" />
        </a-col>
      </a-row>
      <!-- 空列表 -->
      <template v-else>
        <a-card class="general-card empty-list-card">
          <a-empty class="empty-list-card-body" description="暂无数据" />
        </a-card>
      </template>
    </a-spin>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'card-list'
  };
</script>

<script lang="ts" setup>
  import type { CSSProperties, PropType } from 'vue';
  import type { PaginationProps, ResponsiveValue } from '@arco-design/web-vue';
  import type { CardRecord, ColResponsiveValue, HandleVisible, CardFieldConfig, CardPosition } from '@/types/card';
  import { compile, computed, h, ref } from 'vue';
  import { useAppStore } from '@/store';
  import { triggerMouseEvent } from '@/utils';

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

  const props = defineProps({
    key: {
      type: String,
      default: 'id'
    },
    pagination: {
      type: [Object, Boolean] as PropType<PaginationProps | boolean>,
      default: false
    },
    loading: {
      type: Boolean as PropType<Boolean>,
      default: false
    },
    fieldConfig: {
      type: Object as PropType<CardFieldConfig>,
      default: []
    },
    cardHeight: {
      type: String,
      default: '100%'
    },
    cardClass: String,
    cardBodyStyle: Object as PropType<CSSProperties>,
    contextMenu: {
      type: Boolean,
      default: true
    },
    filterCount: {
      type: Number,
      default: 0
    },
    searchInputPlaceholder: String,
    searchInputWidth: {
      type: String,
      default: '200px'
    },
    searchValue: {
      type: String,
      default: ''
    },
    createCardDescription: {
      type: String,
      default: '点击此处进行创建'
    },
    createCardPosition: {
      type: String as PropType<CardPosition>,
      default: false
    },
    addPermission: {
      type: Array as PropType<String[]>,
      default: []
    },
    cardLayoutGutter: {
      type: [Number, Object, Array] as PropType<Number | ResponsiveValue | Array<Number> | Array<ResponsiveValue>>,
      default: [16, 16]
    },
    cardLayoutCols: {
      type: Object as PropType<ColResponsiveValue>,
      default: () => {
        return {
          span: 6
        };
      }
    },
    handleVisible: {
      type: Object as PropType<HandleVisible>,
      default: () => {
        return {};
      }
    },
    list: {
      type: Array as PropType<Array<CardRecord>>,
      default: []
    },
  });

  // 创建卡片
  const CreateCard = {
    props: ['cardHeight', 'createCardDescription'],
    setup(props: { cardHeight: any; createCardDescription: any; }) {
      return () => {
        return h(compile(`
          <a-card class="card-list-item create-card"
                  :style="{ height: '${props.cardHeight}' }"
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

<style lang="less" scoped>
  @header-info-height: 48px;
  @header-handler-height: 48px;
  @top-height: 16 + @header-info-height + @header-handler-height + 12px;

  .card-list-layout {

    &-header {
      margin: -16px -16px 0 -16px;
      padding: 16px 16px 12px 16px;
      position: fixed;
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
    height: 27px;
    padding: 6px;
  }

  .filter-bottom-container {
    display: flex;
    justify-content: flex-end;
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
      height: calc(100vh - @top-height - 140px);
      display: flex;
      align-items: center;
      justify-content: center;
      flex-direction: column;
      padding: 0;
    }
  }

  .fields-container {

    .field-label {
      color: var(--color-text-3);
      word-break: break-all;
      white-space: pre-wrap;
      padding-right: 8px;
      font-size: 12px;
      font-weight: 500;
      user-select: none;
    }

    .field-value {
      color: var(--gray-8);
      word-break: break-all;
      white-space: pre-wrap;
      padding-right: 8px;
      font-size: 14px;
      font-weight: 400;
    }

    .field-value.field-value-ellipsis {
      overflow: hidden;
      text-overflow: ellipsis;
      word-break: keep-all;
    }
  }

</style>
