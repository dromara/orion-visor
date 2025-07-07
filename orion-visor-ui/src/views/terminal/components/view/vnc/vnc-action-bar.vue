<template>
  <div v-if="session.state.connectStatus === TerminalStatus.CONNECTED">
    <!-- 工具栏 -->
    <a-popover v-model:popup-visible="visible"
               :title="undefined"
               trigger="click"
               :content-class="['guacd-action-bar-popover', direction]"
               :content-style="{ '--action-count': Math.max(actions.length, direction === ActionBarPosition.RIGHT ? 5 : 6) }"
               :position="direction === ActionBarPosition.RIGHT ? 'left' : 'bottom'"
               :show-arrow="false"
               :auto-fix-position="false">
      <!-- 触发器 -->
      <div class="action-bar-trigger" :class="[direction === ActionBarPosition.RIGHT ? 'right' : 'top']" />
      <!-- 工具内容 -->
      <template #content>
        <!-- 按钮 -->
        <a-space class="action-bar-actions"
                 :direction="direction === ActionBarPosition.RIGHT ? 'vertical' : 'horizontal'"
                 :size="16">
          <div v-for="action in actions" :key="action.item">
            <a-tooltip :mini="true"
                       :auto-fix-position="false"
                       :position="direction === ActionBarPosition.RIGHT ? 'left' : 'bottom'"
                       content-class="terminal-tooltip-content"
                       :show-arrow="false"
                       :content="action.content">
              <a-button class="action-bar-button"
                        :disabled="action.disabled"
                        :type="action.active ? 'primary' : 'secondary'"
                        @click="toggleClickAction(action.item)">
                <template #icon>
                  <component :is="action.icon" />
                </template>
              </a-button>
            </a-tooltip>
          </div>
        </a-space>
        <!-- 显示设置 -->
        <div v-if="current === GuacdActionItemKeys.DISPLAY" class="action-bar-content">
          <!-- 分辨率 -->
          <a-space>
            <span class="display-size-label">分辨率</span>
            <a-select v-model="displaySize"
                      class="display-size-input"
                      placeholder="请选择分辨率"
                      :options="toOptions(screenResolutionKey)"
                      allow-create />
          </a-space>
          <!-- 按钮 -->
          <a-space class="action-bar-content-footer">
            <a-button type="primary"
                      size="small"
                      @click="fitOnce">
              临时自适应
            </a-button>
            <a-button type="primary"
                      size="small"
                      @click="setDisplaySize">
              设置
            </a-button>
          </a-space>
        </div>
        <!-- 组合键 -->
        <div v-else-if="current === GuacdActionItemKeys.COMBINATION_KEY" class="action-bar-content">
          <a-row :gutter="[12, 12]" wrap>
            <a-col v-for="item in GuacdCombinationKeyItems"
                   :key="item.name"
                   :span="12"
                   class="combination-key-item"
                   @click="triggerCombinationKey(item.keys)">
              <span>{{ item.name }}</span>
            </a-col>
          </a-row>
        </div>
        <!-- 剪切板 -->
        <div v-else-if="current === GuacdActionItemKeys.CLIPBOARD" class="action-bar-content">
          <a-textarea class="action-bar-clipboard"
                      v-model="clipboardData"
                      :ref="setAutoFocus"
                      placeholder="远程剪切板"
                      allow-clear />
          <!-- 按钮 -->
          <a-space class="action-bar-content-footer">
            <a-button size="small" @click="clearClipboardData">
              清空
            </a-button>
            <a-button type="primary"
                      size="small"
                      :disabled="!clipboardData"
                      @click="sendClipboardData">
              发送
            </a-button>
          </a-space>
        </div>
      </template>
    </a-popover>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'vncActionBar'
  };
</script>

<script lang="ts" setup>
  import type { IVncSession } from '@/views/terminal/interfaces';
  import {
    TerminalStatus,
    GuacdCombinationKeyItems,
    GuacdActionItemKeys,
    VncActionBarItems,
    screenResolutionKey,
    fitDisplayValue, ActionBarPosition,
  } from '@/views/terminal/types/const';
  import { computed, ref, watch, onMounted } from 'vue';
  import { setAutoFocus } from '@/utils/dom';
  import { readText } from '@/hooks/copy';
  import { useTerminalStore, useDictStore } from '@/store';
  import useGuacdActionBar from '@/views/terminal/types/use-guacd-action-bar';
  import useVisible from '@/hooks/visible';

  const props = defineProps<{
    session: IVncSession;
    direction: string;
  }>();

  const { preference } = useTerminalStore();
  const { toOptions, getDictValue } = useDictStore();
  const { visible, setVisible } = useVisible();

  const {
    displaySize,
    clipboardData,
    fitOnce,
    setDisplaySize,
    triggerCombinationKey,
    sendClipboardData,
    clearClipboardData,
    disconnect,
  } = useGuacdActionBar({
    session: props.session,
    setVisible,
  });

  const current = ref('');

  const actions = computed(() => {
    return VncActionBarItems.filter(item => {
      return preference.vncActionBarSetting[item.item] !== false;
    }).map(item => {
      const key = item.item;
      return {
        ...item,
        active: current.value === key,
        disabled: (key === GuacdActionItemKeys.DISPLAY || GuacdActionItemKeys.DISCONNECT || key === GuacdActionItemKeys.CLOSE) ? false : !props.session.isWriteable(),
      };
    });
  });

  // 修改数据值
  watch(() => visible.value, (val) => {
    if (!val) {
      return;
    }
    // 重新触发点击
    toggleClickAction(current.value);
  });

  // 触发 action
  const toggleClickAction = (key: string) => {
    if (key === GuacdActionItemKeys.DISPLAY) {
      // 显示设置
      current.value = GuacdActionItemKeys.DISPLAY;
      if (props.session.displayHandler?.autoFit) {
        displaySize.value = fitDisplayValue;
      } else {
        displaySize.value = `${props.session.displayHandler?.displayWidth || 0}x${props.session.displayHandler?.displayHeight || 0}`;
      }
    } else if (key === GuacdActionItemKeys.COMBINATION_KEY) {
      // 组合键
      current.value = GuacdActionItemKeys.COMBINATION_KEY;
    } else if (key === GuacdActionItemKeys.CLIPBOARD) {
      // 剪切板
      current.value = GuacdActionItemKeys.CLIPBOARD;
      readText(false)
        .then(s => clipboardData.value = s)
        .catch(() => clipboardData.value = '');
    } else if (key === GuacdActionItemKeys.DISCONNECT) {
      // 断开连接
      disconnect();
    } else if (key === GuacdActionItemKeys.CLOSE) {
      // 关闭工具栏
      setVisible(false);
    }
  };

  // 设置选中
  onMounted(() => {
    if (actions.value?.length) {
      current.value = actions.value[0].item;
    }
  });

</script>

<style lang="less" scoped>
</style>
