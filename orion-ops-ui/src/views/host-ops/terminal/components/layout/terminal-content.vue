<template>
  <div class="terminal-content">
    <!-- 内容 tabs -->
    <a-tabs v-model:active-key="activeKey">
      <a-tab-pane v-for="tab in tabs"
                  :key="tab.key"
                  :title="tab.title">
        <!-- 设置 -->
        <template v-if="tab.type === TabType.SETTING">
          <!-- 新建连接 -->
          <new-connection-view v-if="tab.key === InnerTabs.NEW_CONNECTION.key" />
          <!-- 显示设置 -->
          <terminal-view-setting v-else-if="tab.key === InnerTabs.VIEW_SETTING.key" />
          <span v-else>
            {{ tab.key }}
            {{ tab.title }}
          </span>
        </template>
        <!-- 终端 -->
        <template v-else-if="tab.type === TabType.TERMINAL">
          终端 {{ tab.key }}
          <div v-for="i in 1000" :key="i">
            {{ tab.title }}
          </div>
        </template>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'TerminalContent'
  };
</script>

<script lang="ts" setup>
  import type { PropType } from 'vue';
  import type { TabItem } from '../../types/terminal.const';
  import { computed } from 'vue';
  import { TabType, InnerTabs } from '../../types/terminal.const';
  import TerminalViewSetting from '../view-setting/terminal-view-setting.vue';
  import NewConnectionView from '@/views/host-ops/terminal/components/new-connection/new-connection-view.vue';

  const props = defineProps({
    modelValue: {
      type: String,
      required: true
    },
    tabs: {
      type: Array as PropType<Array<TabItem>>,
      required: true
    },
  });

  const activeKey = computed<String>({
    get() {
      return props.modelValue;
    },
    set() {
    }
  });

</script>

<style lang="less" scoped>
  .terminal-content {
    width: 100%;
    height: 100%;
    position: relative;

    :deep(.arco-tabs) {
      width: 100%;
      height: 100%;

      .arco-tabs-nav {
        display: none;
      }

      .arco-tabs-content {
        padding: 0;
        width: 100%;
        height: 100%;
        overflow: auto;

        &::-webkit-scrollbar {
          display: none;
        }
      }
    }
  }
</style>
