<template>
  <div class="card-list-layout">
    <!-- 头部部分-固定 -->
    <card-header v-bind="props" @emitter="dispatchEmitter">
      <!-- 左侧侧操作槽位 -->
      <template #leftHandle>
        <slot name="leftHandle" />
      </template>
      <!-- 右侧操作槽位 -->
      <template #rightHandle>
        <slot name="rightHandle" />
      </template>
      <!-- 过滤表单 -->
      <template #filterContent>
        <slot name="filterContent" />
      </template>
    </card-header>
    <!-- 身体部分 -->
    <a-spin class="card-list-layout-body" :loading="loading as boolean">
      <!-- 卡片列表 -->
      <a-row v-if="list?.length !== 0"
             :gutter="cardLayoutGutter as any"
             align="stretch">
        <!-- 添加卡片 -->
        <a-col v-permission="addPermission"
               v-if="createCardPosition === 'head'"
               v-bind="cardLayoutCols">
          <create-card v-bind="props" @click="emits('add')" />
        </a-col>
        <!-- 数据卡片 -->
        <a-col v-for="(item, index) in list"
               :key="item[key]"
               v-bind="cardLayoutCols"
               :class="{ 'disabled-col': item.disabled === true }">
          <!-- 右键菜单 -->
          <a-dropdown trigger="contextMenu" alignPoint>
            <!-- 卡片 -->
            <card-item v-bind="props"
                       :index="index"
                       :item="item"
                       @emitter="dispatchEmitter">
              <!-- 自定义插槽 -->
              <template v-for="slot in Object.keys($slots)" :key="slot" #[slot]>
                <slot :name="slot" :record="item" :index="index" :key="item[key]" />
              </template>
            </card-item>
            <!-- 右键菜单 -->
            <template v-if="contextMenu" #content>
              <slot name="contextMenu" :record="item" :index="index" :key="item[key]" />
            </template>
          </a-dropdown>
        </a-col>
        <!-- 添加卡片 -->
        <a-col v-permission="addPermission"
               v-if="createCardPosition === 'tail'"
               v-bind="cardLayoutCols">
          <create-card v-bind="props" @click="emits('add')" />
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
    name: 'cardList'
  };
</script>

<script lang="ts" setup>
  import CreateCard from './components/create-card.vue';
  import CardHeader from './components/card-header.vue';
  import CardItem from './components/card-item.vue';
  import { Emitter } from './types/emits';
  import { CardProps } from './types/props';
  import useEmitter from '@/hooks/emitter';

  const props = withDefaults(defineProps<CardProps>(), {
    key: 'id',
    pagination: false,
    loading: false,
    cardHeight: '100%',
    contextMenu: true,
    filterCount: 0,
    searchInputWidth: '200px',
    searchValue: '',
    createCardDescription: '点击此处进行创建',
    createCardPosition: 'head',
    addPermission: () => [],
    cardLayoutGutter: () => [16, 16],
    cardLayoutCols: () => {
      return {
        span: 6
      };
    },
    handleVisible: () => {
      return {};
    },
    list: () => []
  });

  const emits = defineEmits(Emitter);
  const { dispatchEmitter } = useEmitter(emits);

</script>

<style lang="less">
  .card-list-layout {
    --header-info-height: 48px;
    --header-handler-height: 48px;
    --top-height: calc(16px + var(--header-info-height) + var(--header-handler-height) + 12px);

    &-body {
      display: block;
      margin-top: calc(var(--top-height) - 16px);
      padding-top: 4px;
    }

    .disabled-col {
      cursor: not-allowed;
    }
  }

  .empty-list-card-body {
    height: calc(var(--top-height) + 258px);
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
    padding: 0;
  }

</style>
