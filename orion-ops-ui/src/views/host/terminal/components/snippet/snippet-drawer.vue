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
      <div class="snippet-header-container">
        <!-- 头部操作 -->
        <div class="snippet-header">
          <a-button @click="toggle">新建</a-button>
          <a-button>搜索</a-button>
        </div>
        <!-- 提示 -->
        <a-alert v-if="isNotTipped(snippetTipsKey)"
                 class="snippet-tips"
                 :closable="true"
                 @on-close="closeTips">
          双击命令直接运行
        </a-alert>
      </div>
      <!-- 命令片段 -->
      <div class="snippet-list-container">
        <a-collapse v-if="snippet.groups.length"
                    :bordered="false">
          <a-collapse-item v-for="group in snippet.groups"
                           :key="group.id"
                           :header="group.name">
            <!-- 总量 -->
            <template #extra>
              {{ group.idList.length }} 条
            </template>
            {{ group }}
          </a-collapse-item>
        </a-collapse>
        <div>
          <div v-for="item in snippet.snippets"
               :key="item.id"
               class="snippet-item-wrapper"
               :class="[loading&&item.id===3 ? 'snippet-item-wrapper-expand' : '']">
            <div class="snippet-item">
            <span class="snippet-item-title">
              {{ item.name }}
            </span>
              <span class="snippet-item-command">
              {{ item.command }}
            </span>
            </div>
          </div>
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
  import { ref } from 'vue';

  export interface SnippetGroupResponse {
    groups: Array<SnippetGroup>;
    snippets: Array<Snippet>;
  }

  export interface SnippetGroup {
    id: number;
    name: string;
    idList: Array<number>;
  }

  export interface Snippet {
    id: number;
    name: string;
    command: string;
  }

  import { useTipsStore } from '@/store';
  import useVisible from '@/hooks/visible';
  import { snippetTipsKey } from '../../types/terminal.const';
  import useLoading from '@/hooks/loading';

  const { isNotTipped, setTipped } = useTipsStore();
  const { loading, toggle } = useLoading();
  const { visible, setVisible } = useVisible(true);
  const snippet = ref<SnippetGroupResponse>({
    groups: [{
      id: 1,
      name: 'group1',
      idList: [1, 2]
    }, {
      id: 2,
      name: 'group2',
      idList: [3, 4]
    }],
    snippets: [{
      id: 1,
      name: 'command1command1command1command1command1command1command1command1command1command1command1command1command1command1command1',
      command: 'echo Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad adipisci aliquid, atque cupiditate doloribus eligendi enim fugiat itaque iusto laborum magnam maiores natus nemo, neque quae, reprehenderit sed ullam voluptatem?'
    }, {
      id: 2,
      name: 'command2',
      command: 'echo Lorem'
    }, {
      id: 3,
      name: 'command3',
      command: 'echo Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad adipisci aliquid, atque cupiditate doloribus eligendi enim fugiat itaque iusto laborum magnam maiores natus nemo, neque quae, reprehenderit sed ullam voluptatem?'
    }, {
      id: 4,
      name: 'command4',
      command: 'echo Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad adipisci aliquid, atque cupiditate doloribus eligendi enim fugiat itaque iusto laborum magnam maiores natus nemo, neque quae, reprehenderit sed ullam voluptatem?'
    }, {
      id: 5,
      name: 'command5',
      command: 'echo Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad adipisci aliquid, atque cupiditate doloribus eligendi enim fugiat itaque iusto laborum magnam maiores natus nemo, neque quae, reprehenderit sed ullam voluptatem?'
    }, {
      id: 6,
      name: 'command6',
      command: 'echo Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad adipisci aliquid, atque cupiditate doloribus eligendi enim fugiat itaque iusto laborum magnam maiores natus nemo, neque quae, reprehenderit sed ullam voluptatem?'
    }]
  });

  // 打开
  const open = () => {
    setVisible(true);
    // loading
  };

  // 关闭提示
  const closeTips = () => {
    console.log('close');
  };

  defineExpose({ open });

</script>


<style lang="less" scoped>
  @transform-x: 8px;
  @drawer-width: 388px;
  @item-wrapper-p-y: 4px;
  @item-wrapper-p-x: 12px;
  @item-p: 8px;
  @item-width: @drawer-width - @item-wrapper-p-x * 2;
  @item-width-transform: @item-width + @transform-x;
  @item-inline-width: @item-width - @item-p * 2;

  .snippet-drawer-title {
    font-size: 14px;
  }

  .snippet-container {
    position: relative;
    background: var(--color-bg-2);
    height: 100%;

    .snippet-header-container {
      padding: 12px;
      //height: 104px;

      .snippet-header {
      }

      .snippet-tips {
        margin-top: 8px;
      }
    }
  }

  .snippet-list-container {
    position: relative;
    //height: calc(100% - 104px);
    overflow: auto;
  }

  .snippet-item-wrapper {
    padding: @item-wrapper-p-y 0;
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;

    &-expand {

      .snippet-item {
        width: @item-width-transform !important;
        background: var(--color-fill-3) !important;

        :hover {
        }

        .snippet-item-command {
          color: var(--color-text-1);
          text-overflow: unset;
          word-break: break-all;
          white-space: unset;
        }
      }
    }

    .snippet-item {
      display: flex;
      flex-direction: column;
      padding: @item-p;
      background: var(--color-fill-2);
      border-radius: 4px;
      cursor: pointer;
      transition: all 0.2s;
      width: @item-width;

      &:hover {
        width: @item-width-transform;
        background: var(--color-fill-3);
      }

      &-title {
        color: var(--color-text-1);
        margin-bottom: 8px;
        overflow: hidden;
        text-overflow: ellipsis;
        width: @item-inline-width;
      }

      &-command {
        color: var(--color-text-2);
        font-size: 12px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: pre;
        width: @item-inline-width;
      }
    }
  }

  :deep(.arco-collapse-item) {
    border: none;

    .arco-collapse-item-header-title {
      user-select: none;
    }

    .arco-collapse-item-header {
      border: none;
    }

    .arco-collapse-item-content {
      background-color: unset;
      padding: 0;
    }

    .arco-collapse-item-content-box {
      padding: 0;
    }

  }

</style>
