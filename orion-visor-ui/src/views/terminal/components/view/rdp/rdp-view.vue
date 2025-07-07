<template>
  <div class="guacd-container">
    <!-- 状态 -->
    <guacd-status v-if="session"
                  class="guacd-status-mask"
                  :session="session" />
    <!-- 工具栏 -->
    <rdp-action-bar v-if="session"
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
    name: 'rdpView'
  };
</script>

<script setup lang="ts">
  import type { TerminalSessionTabItem, IRdpSession } from '@/views/terminal/interfaces';
  import { onMounted, ref, onUnmounted } from 'vue';
  import { useTerminalStore } from '@/store';
  import { ActionBarPosition } from '@/views/terminal/types/const';
  import RdpActionBar from './rdp-action-bar.vue';
  import GuacdStatus from '../guacd/guacd-status.vue';

  const props = defineProps<{
    item: TerminalSessionTabItem;
  }>();

  const { preference, sessionManager } = useTerminalStore();

  const actionBarDirection = ref(ActionBarPosition.TOP);

  const viewport = ref();
  const session = ref<IRdpSession>();

  // 初始化会话
  onMounted(async () => {
    // 工具栏方向
    actionBarDirection.value = preference.rdpActionBarSetting.position || ActionBarPosition.TOP;
    // 创建终端会话
    session.value = sessionManager.createSession<IRdpSession>(props.item);
    // 打开终端会话
    await sessionManager.openRdp(props.item, {
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
