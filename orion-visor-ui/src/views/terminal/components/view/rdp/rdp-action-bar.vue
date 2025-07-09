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
          <display-action :session="session" @close="close" />
        </div>
        <!-- 组合键 -->
        <div v-else-if="current === GuacdActionItemKeys.COMBINATION_KEY" class="action-bar-content">
          <combination-key-action :session="session" @close="close" />
        </div>
        <!-- 触发键 -->
        <div v-else-if="current === GuacdActionItemKeys.TRIGGER_KEY" class="action-bar-content">
          <trigger-key-action :session="session" />
        </div>
        <!-- 剪切板 -->
        <div v-else-if="current === GuacdActionItemKeys.CLIPBOARD" class="action-bar-content">
          <clipboard-action :session="session" @close="close" />
        </div>
        <!-- RDP 文件上传 -->
        <div v-else-if="current === GuacdActionItemKeys.RDP_UPLOAD" class="action-bar-content">
          <a-upload class="action-bar-upload"
                    v-model:file-list="fileList"
                    :auto-upload="false"
                    :show-file-list="false"
                    :draggable="true"
                    :tip="fileList.length ? fileList[0]?.name : '选择文件后会自动上传至驱动目录'"
                    @change="onSelectFile" />
          <!-- 按钮 -->
          <a-space class="action-bar-content-footer">
            <a-button size="small" @click="clearUploadFile">
              清空
            </a-button>
            <a-button type="primary"
                      size="small"
                      :disabled="!fileList.length"
                      @click="uploadFile">
              上传
            </a-button>
          </a-space>
        </div>
      </template>
    </a-popover>
    <!-- sftp 上传框 -->
    <sftp-upload-modal ref="sftpUploadModalRef" :session="session" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'rdpActionBar'
  };
</script>

<script lang="ts" setup>
  import type { FileItem } from '@arco-design/web-vue';
  import type { IRdpSession } from '@/views/terminal/interfaces';
  import {
    TerminalStatus,
    GuacdActionItemKeys,
    RdpActionBarItems,
    ActionBarPosition, TerminalSessionTypes
  } from '@/views/terminal/types/const';
  import { computed, ref, watch, onMounted } from 'vue';
  import { saveAs } from 'file-saver';
  import { useTerminalStore } from '@/store';
  import useVisible from '@/hooks/visible';
  import InfoAction from '../guacd/actions/info-action.vue';
  import DisplayAction from '../guacd/actions/display-action.vue';
  import TriggerKeyAction from '../guacd/actions/trigger-key-action.vue';
  import ClipboardAction from '../guacd/actions/clipboard-action.vue';
  import CombinationKeyAction from '../guacd/actions/combination-key-action.vue';
  import SftpUploadModal from '@/views/terminal/components/view/sftp/sftp-upload-modal.vue';

  const props = defineProps<{
    session: IRdpSession;
    direction: string;
  }>();

  const { hosts, preference, openSession, reOpenSession, transferManager } = useTerminalStore();
  const { visible, setVisible } = useVisible();

  const current = ref('');
  const sftpUploadModalRef = ref();
  const fileList = ref<FileItem[]>([]);

  const actions = computed(() => {
    return RdpActionBarItems.filter(item => {
      return preference.rdpActionBarSetting[item.item] !== false;
    }).map(item => {
      const key = item.item;
      return {
        ...item,
        active: current.value === key,
        enabled: () => {
          if (key === GuacdActionItemKeys.DISPLAY || key === GuacdActionItemKeys.DISCONNECT || key === GuacdActionItemKeys.RECONNECT || key === GuacdActionItemKeys.SAVE_RDP || key === GuacdActionItemKeys.CLOSE) {
            return true;
          } else if (key === GuacdActionItemKeys.OPEN_SFTP || key === GuacdActionItemKeys.SFTP_UPLOAD) {
            // 支持 SFTP 协议
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
    // 重新触发点击
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
    } else if (key === GuacdActionItemKeys.RDP_UPLOAD) {
      // RDP 文件上传
      current.value = GuacdActionItemKeys.RDP_UPLOAD;
      fileList.value = [];
    } else if (key === GuacdActionItemKeys.SAVE_RDP) {
      // 保存 rdp 文件
      saveRdpFile();
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

  // 保存 rdp 文件
  const saveRdpFile = () => {
    const address = props.session.info.address;
    const port = props.session.info.port;
    const username = props.session.info.username;
    const content = `auto connect:i:1\nfull address:s:${address}:${port}\nusername:s:${username}`;
    saveAs(new Blob([content], { type: 'text/plain;charset=utf-8' }), `${address}.rdp`);
  };

  // 上传文件
  const uploadFile = () => {
    const file = fileList.value[0].file as File;
    // 记录事件
    props.session.onFileSystemEvent({ event: 'terminal:rdp-upload', path: file.name });
    // 上传文件
    transferManager.rdp.addUpload(props.session, file);
    fileList.value = [];
  };

  // 选择文件回调
  const onSelectFile = (files: Array<FileItem>) => {
    fileList.value = [files[files.length - 1]];
  };

  // 上传文件
  const clearUploadFile = () => {
    fileList.value = [];
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
