<template>
  <a-drawer v-model:visible="visible"
            :width="388"
            :footer="false">
    <!-- 表头 -->
    <template #title>
      <span class="snippet-drawer-title">
        <icon-code />
        命令片段
      </span>
    </template>
    <!-- 命令容器 -->
    <div class="snippet-container">
      <!-- 命令头部 -->
      <div class="snippet-header">
        <!-- 创建命令 -->
        <span class="click-icon-wrapper snippet-header-icon" title="创建命令">
          <icon-plus />
        </span>
        <!-- 搜索框 -->
        <a-input-search class="snippet-header-input"
                        placeholder="名称"
                        allow-clear />
      </div>
      <!-- 命令片段 -->
      <div class="snippet-list-container">
        <snippet-group :snippet="snippet" />
        <div>
          <snippet-item v-for="item in snippet.items"
                        :key="item.id"
                        :item="item" />
        </div>
      </div>
    </div>
  </a-drawer>
</template>

<script lang="ts">
  export default {
    name: 'snippetDrawer'
  };
</script>

<script lang="ts" setup>
  import type { CommandSnippetWrapperResponse } from '@/api/asset/command-snippet';
  import { onMounted, ref } from 'vue';
  import useVisible from '@/hooks/visible';
  import useLoading from '@/hooks/loading';
  import SnippetItem from './snippet-item.vue';
  import SnippetGroup from './snippet-group.vue';

  const { loading, toggle } = useLoading();
  const { visible, setVisible } = useVisible();
  const snippet = ref<CommandSnippetWrapperResponse>({
    groups: [],
    items: []
  });

  // 打开
  const open = () => {
    setVisible(true);

    console.log('loading');
    // loading
  };

  defineExpose({ open });

  onMounted(() => {
    open();
  });

</script>


<style lang="less" scoped>
  .snippet-drawer-title {
    font-size: 14px;
  }

  .snippet-container {
    position: relative;
    background: var(--color-bg-2);
    height: 100%;

    .snippet-header {
      padding: 12px;
      height: 56px;
      display: flex;
      align-items: center;
      justify-content: space-between;

      &-icon {
        width: 32px;
        height: 32px;
        font-size: 16px;
      }

      &-input {
        width: 220px;
      }
    }
  }

  .snippet-list-container {
    position: relative;
    height: calc(100% - 56px);
    overflow: auto;
    padding-bottom: 4px;
  }

</style>
