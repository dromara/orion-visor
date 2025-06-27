<template>
  <div class="rdp-container">
    <!-- 状态 -->
    <rdp-status v-if="session"
                class="rdp-status-mask"
                :session="session" />
    <!-- 工具栏 -->
    <rdp-action-bar v-if="session"
                    class="rdp-action-bar"
                    :class="[toolbarDirection === 'right' ? 'right' : 'top']"
                    :session="session"
                    :direction="toolbarDirection" />
    <!-- rdp 视口 -->
    <div class="rdp-viewport" ref="viewport" />
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
  import RdpStatus from './rdp-status.vue';
  import RdpActionBar from './rdp-action-bar.vue';

  const props = defineProps<{
    item: TerminalSessionTabItem;
  }>();

  const { preference, sessionManager } = useTerminalStore();

  const toolbarDirection = ref('top');

  const viewport = ref();
  const session = ref<IRdpSession>();

  // 初始化会话
  onMounted(async () => {
    // 工具栏方向
    toolbarDirection.value = preference.rdpActionBarSetting.position || 'top';
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

<style scoped lang="less">
  .rdp-container {
    width: 100%;
    height: 100%;
    position: relative;
  }

  .rdp-viewport {
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;

    :deep(> div) {
      position: relative;
      z-index: 8;
    }
  }

  .rdp-status-mask {
    width: 100%;
    height: 100%;
    position: absolute;
    backdrop-filter: blur(10px);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 9999;
  }

  .rdp-action-bar {
    position: absolute;
    cursor: pointer;
    z-index: 9998;

    &.top {
      top: 4px;
      left: 50%;
      transform: translateX(-50%);
    }

    &.right {
      right: 4px;
      top: 50%;
      transform: translateY(-50%);
    }
  }

</style>
