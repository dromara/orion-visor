<template>
  <a-drawer v-model:visible="visible"
            :width="368"
            :footer="false">
    <!-- 表头 -->
    <template #title>
      <span class="snippet-drawer-title">
        <icon-code />
        代码片段
      </span>
    </template>
    <!-- 命令容器 -->
    <div class="snippet-container">
      <!-- 命令头部 -->
      <div class="snippet-header-container">
        <!-- 头部操作 -->
        <div class="snippet-header">
          <a-button>新建分组</a-button>
          <a-button>新建命令</a-button>
          <a-button>搜索</a-button>
        </div>
        <!-- 提示 -->
        <a-alert v-if="isNotTipped(snippetTipsKey)"
                 class="snippet-tips"
                 :closable="true"
                 @on-close="closeTips">
          双击命令直接粘贴
        </a-alert>
      </div>
      <!-- 命令片段 -->
      <div class="snippet-list-container">
        <a-collapse :default-active-key="['1']" :bordered="false">
          <a-collapse-item header="group 1" key="1" style="border: none;">
          </a-collapse-item>
          <a-collapse-item header="group 2" key="2" style="border: none;">
          </a-collapse-item>
          <a-collapse-item header="group 3" key="3" style="border: none;">
          </a-collapse-item>
        </a-collapse>
        123123 <br>123123 <br>123123 <br>123123 <br>123123 <br>123123 <br>123123 <br>123123 <br>123123 <br>123123 <br>123123 <br>123123 <br>123123 <br>
        123123 <br>123123 <br>123123 <br>123123 <br>123123 <br>123123 <br>123123 <br>123123 <br>123123 <br>123123 <br>123123 <br>123123 <br>123123 <br>
        123123 <br>123123 <br>123123 <br>123123 <br>123123 <br>123123 <br>123123 <br>123123 <br>123123 <br>123123 <br>123123 <br>123123 <br>123123 <br>
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
  import { useTipsStore } from '@/store';
  import useVisible from '@/hooks/visible';
  import { snippetTipsKey } from '../../types/terminal.const';

  const { isNotTipped, setTipped } = useTipsStore();
  const { visible, setVisible } = useVisible(true);

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

      .snippet-tips{
        margin-top: 8px;
      }
    }
  }

  .snippet-list-container {
    position: relative;
    //height: calc(100% - 104px);
    overflow: auto;
    padding: 0 12px 12px 12px;
  }

  :deep(.arco-collapse-item-header) {
    border: none;
  }

</style>
