<template>
  <div class="guacd-container">
    <!-- 状态 -->
    <guacd-status v-if="session"
                  class="guacd-status-mask"
                  :session="session" />
    <!-- 工具栏 -->
    <vnc-action-bar v-if="session"
                    class="guacd-action-bar"
                    :class="[actionBarDirection === 'right' ? 'right' : 'top']"
                    :session="session"
                    :direction="actionBarDirection" />
    <!-- 视口 -->
    <div class="guacd-viewport" ref="viewport" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'vnc-view'
  };
</script>

<script lang="ts" setup>
  import type { TerminalSessionTabItem, IVncSession } from '@/views/terminal/interfaces';
  import { onMounted, ref, onUnmounted } from 'vue';
  import { useTerminalStore } from '@/store';
  import { ActionBarPosition } from '@/views/terminal/types/const';
  import GuacdStatus from '../guacd/guacd-status.vue';
  import VncActionBar from './vnc-action-bar.vue';

  const props = defineProps<{
    item: TerminalSessionTabItem;
  }>();

  const { preference, sessionManager } = useTerminalStore();

  const actionBarDirection = ref(ActionBarPosition.TOP);

  const viewport = ref();
  const session = ref<IVncSession>();

  // 初始化会话
  onMounted(async () => {
    // 工具栏方向
    actionBarDirection.value = preference.vncActionBarSetting.position || ActionBarPosition.TOP;
    // 创建终端会话
    session.value = sessionManager.createSession<IVncSession>(props.item);
    // 打开终端会话
    await sessionManager.openVnc(props.item, {
      viewport: viewport.value,
    });
  });

  // 关闭会话
  onUnmounted(() => {
    if (props.item.key) {
      sessionManager.closeSession(props.item.key);
    }
  });
</script>

<style lang="less" scoped>
</style>
