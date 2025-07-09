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
                        :disabled="!action.enabled()"
                        :type="action.active ? 'primary' : 'secondary'"
                        @click="toggleAction(action.item)">
                <template #icon>
                  <component :is="action.icon" />
                </template>
              </a-button>
            </a-tooltip>
          </div>
        </a-space>
        <!-- 连接信息 -->
        <div v-if="current === GuacdActionItemKeys.INFO" class="action-bar-content">
          <info-action :session="session" />
        </div>
        <!-- 显示设置 -->
        <div v-else-if="current === GuacdActionItemKeys.DISPLAY" class="action-bar-content">
          <display-action ref="display"
                          :session="session"
                          @close="close" />
        </div>
        <!-- 组合键 -->
        <div v-else-if="current === GuacdActionItemKeys.COMBINATION_KEY" class="action-bar-content">
          <combination-key-action :session="session"
                                  @close="close" />
        </div>
        <!-- 触发键 -->
        <div v-else-if="current === GuacdActionItemKeys.TRIGGER_KEY" class="action-bar-content">
          <trigger-key-action :session="session" />
        </div>
        <!-- 剪切板 -->
        <div v-else-if="current === GuacdActionItemKeys.CLIPBOARD" class="action-bar-content">
          <clipboard-action ref="clipboard"
                            :session="session"
                            @close="close" />
        </div>
      </template>
    </a-popover>
    <!-- sftp 上传框 -->
    <sftp-upload-modal ref="sftpUploadModalRef" :session="session" />
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
    GuacdActionItemKeys,
    VncActionBarItems,
    ActionBarPosition,
    TerminalSessionTypes,
  } from '@/views/terminal/types/const';
  import { computed, ref, watch, onMounted } from 'vue';
  import { useTerminalStore } from '@/store';
  import useVisible from '@/hooks/visible';
  import InfoAction from '../guacd/actions/info-action.vue';
  import DisplayAction from '../guacd/actions/display-action.vue';
  import ClipboardAction from '../guacd/actions/clipboard-action.vue';
  import TriggerKeyAction from '../guacd/actions/trigger-key-action.vue';
  import CombinationKeyAction from '../guacd/actions/combination-key-action.vue';
  import SftpUploadModal from '@/views/terminal/components/view/sftp/sftp-upload-modal.vue';

  const props = defineProps<{
    session: IVncSession;
    direction: string;
  }>();

  const { hosts, preference, openSession, reOpenSession } = useTerminalStore();
  const { visible, setVisible } = useVisible();

  const current = ref('');
  const sftpUploadModalRef = ref();

  const actions = computed(() => {
    return VncActionBarItems.filter(item => {
      return preference.vncActionBarSetting[item.item] !== false;
    }).map(item => {
      const key = item.item;
      return {
        ...item,
        active: current.value === key,
        enabled: () => {
          if (key === GuacdActionItemKeys.DISPLAY || key === GuacdActionItemKeys.DISCONNECT || key === GuacdActionItemKeys.RECONNECT || key === GuacdActionItemKeys.CLOSE) {
            return true;
          } else if (key === GuacdActionItemKeys.OPEN_SFTP || key === GuacdActionItemKeys.SFTP_UPLOAD) {
            if (!hosts.hostList.find(s => s.id === props.session.info.hostId)?.types?.includes?.(TerminalSessionTypes.SFTP.protocol)) {
              return false;
            }
          }
          return props.session.isWriteable();
        }
      };
    });
  });

  // 修改数据值
  watch(() => visible.value, (val) => {
    if (!val) {
      return;
    }
    // 重新触发
    toggleAction(current.value);
  });

  // 触发 action
  const toggleAction = (key: string) => {
    if (key === GuacdActionItemKeys.OPEN_SFTP) {
      // 打开 SFTP 会话
      openSession(hosts.hostList.find(s => s.id === props.session.info.hostId) as any, TerminalSessionTypes.SFTP);
      setVisible(false);
    } else if (key === GuacdActionItemKeys.SFTP_UPLOAD) {
      // 打开 SFTP 上传
      sftpUploadModalRef.value.open('/');
      setVisible(false);
    } else if (key === GuacdActionItemKeys.DISCONNECT) {
      // 断开连接
      disconnect();
    } else if (key === GuacdActionItemKeys.RECONNECT) {
      // 重新连接
      reconnect();
    } else if (key === GuacdActionItemKeys.CLOSE) {
      // 关闭工具栏
      setVisible(false);
    } else {
      current.value = key;
    }
  };

  // 关闭会话
  const disconnect = () => {
    props.session.disconnect();
    setVisible(false);
  };

  // 关闭会话
  const reconnect = () => {
    const session = props.session;
    // 断开连接
    session.disconnect();
    // 重新连接
    if (session.state.canReconnect) {
      reOpenSession(session.sessionKey);
    }
    setVisible(false);
  };

  // 关闭
  const close = () => {
    setVisible(false);
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
