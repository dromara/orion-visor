<template>
  <div v-if="session.state.connectStatus !== TerminalStatus.CONNECTED && visible">
    <!-- 连接中 -->
    <a-spin v-if="session.state.connectStatus === TerminalStatus.CONNECTING"
            tip="正在连接会话..."
            dot />
    <!-- 会话关闭 -->
    <a-card v-if="session.state.connectStatus === TerminalStatus.CLOSED"
            class="status-wrapper"
            title="会话已关闭">
      <!-- 错误信息 -->
      <a-descriptions size="large"
                      :label-style="{ display: 'flex', width: '74px' }"
                      :column="1">
        <!-- 连接地址 -->
        <a-descriptions-item label="连接地址">
          <span class="text-copy" @click="copy(session.info.address, true)">
            {{ session.info.address }}
          </span>
        </a-descriptions-item>
        <!-- 错误码 -->
        <a-descriptions-item label="错误码">
          {{ session.state.closeCode }}
        </a-descriptions-item>
        <!-- 错误信息 -->
        <a-descriptions-item label="错误信息">
          <span class="span-red">
            <!-- 异地登录 -->
            <template v-if="session.state.closeCode === TerminalCloseCode.LOGGED_ELSEWHERE">
              {{ TerminalMessages.loggedElsewhere }} ({{ dateFormat() }})
            </template>
            <!-- 其他错误 -->
            <template v-else>
              {{ session.state.closeMessage ?? '-' }}
            </template>
          </span>
        </a-descriptions-item>
      </a-descriptions>
      <!-- 按钮 -->
      <a-space class="status-button">
        <a-button @click="setVisible(false)">关闭</a-button>
        <a-button v-if="session.state.closeCode !== TerminalCloseCode.FORCE && session.state.canReconnect"
                  type="primary"
                  @click="reOpenSession(session.sessionKey)">
          重连
        </a-button>
      </a-space>
    </a-card>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'guacdStatus'
  };
</script>

<script lang="ts" setup>
  import type { IGuacdSession } from '@/views/terminal/interfaces';
  import { TerminalStatus, TerminalCloseCode, TerminalMessages } from '@/views/terminal/types/const';
  import { copy } from '@/hooks/copy';
  import { dateFormat } from '@/utils';
  import { useTerminalStore } from '@/store';
  import useVisible from '@/hooks/visible';

  const props = defineProps<{
    session: IGuacdSession;
  }>();

  const { visible, setVisible } = useVisible(true);
  const { reOpenSession } = useTerminalStore();

</script>

<style lang="less" scoped>
  .status-wrapper {
    width: 520px;
    min-height: 180px;
    border-radius: 8px;
    background: var(--color-bg-panel);
  }

  .status-button {
    width: 100%;
    padding: 8px 8px 8px 0;
    display: flex;
    justify-content: flex-end;
  }
</style>
