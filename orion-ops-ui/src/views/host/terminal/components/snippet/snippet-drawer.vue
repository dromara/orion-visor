<template>
  <a-drawer v-model:visible="visible"
            :width="368"
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
          <a-button>新建</a-button>
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
               class="snippet-item-wrapper">
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

  const { isNotTipped, setTipped } = useTipsStore();
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
      command: 'echo 123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123'
    }, {
      id: 2,
      name: 'command2',
      command: 'echo 123123123123123123123123123123123123123123'
    }, {
      id: 3,
      name: 'command3',
      command: 'echo 123123123123123123123123123123123123123123'
    }, {
      id: 4,
      name: 'command4',
      command: 'echo 123123123123123123123123123123123123123123'
    }, {
      id: 5,
      name: 'command5',
      command: 'echo 123123123123123123123123123123123123123123'
    }, {
      id: 6,
      name: 'command6',
      command: 'echo 123123123123123123123123123123123123123123'
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
  .snippet-drawer-title {
    font-size: 14px;
  }

  .snippet-container {
    position: relative;
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
    padding: 4px 12px;

    .snippet-item {
      display: flex;
      flex-direction: column;
      padding: 8px;
      background: var(--color-fill-1);
      border-radius: 4px;
      cursor: pointer;
      transition: transform 0.3s ease;
      will-change: transform;

      &:hover {
        transform: scale(1.02);
        background: var(--color-fill-2);
      }

      &-title {
        color: var(--color-text-1);
        margin-bottom: 8px;
        overflow: hidden;
        text-overflow: ellipsis;
      }

      &-command {
        color: var(--color-text-2);
        font-size: 12px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: pre;
        word-break: break-all;
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
