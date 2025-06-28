<template>
  <div v-if="session.state.connectStatus === TerminalStatus.CONNECTED">
    <!-- 工具栏 -->
    <a-popover v-model:popup-visible="visible"
               :title="undefined"
               trigger="click"
               :content-class="['tool-bar-popover', direction]"
               :content-style="{ '--action-count': Math.max(actions.length, 6) }"
               :position="direction === 'right' ? 'left' : 'bottom'"
               :show-arrow="false"
               :auto-fix-position="false">
      <!-- 触发器 -->
      <div class="tool-bar" :class="[direction === 'right' ? 'right' : 'top']" />
      <!-- 工具内容 -->
      <template #content>
        <!-- 按钮 -->
        <a-space class="tool-bar-actions"
                 :direction="direction === 'right' ? 'vertical' : 'horizontal'"
                 :size="16">
          <div v-for="action in actions" :key="action.item">
            <a-tooltip :mini="true"
                       :auto-fix-position="false"
                       :position="direction === 'right' ? 'left' : 'bottom'"
                       content-class="terminal-tooltip-content"
                       :show-arrow="false"
                       :content="action.content">
              <a-button class="tool-bar-button"
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
        <div v-if="current === RdpActionItemKeys.DISPLAY" class="tool-bar-content">
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
          <a-space class="tool-bar-content-footer">
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
        <div v-else-if="current === RdpActionItemKeys.COMBINATION_KEY" class="tool-bar-content">
          <a-row :gutter="[12, 12]" wrap>
            <a-col v-for="item in RdpCombinationKeyItems"
                   :key="item.name"
                   :span="12"
                   class="combination-key-item"
                   @click="triggerCombinationKey(item.keys)">
              <span>{{ item.name }}</span>
            </a-col>
          </a-row>
        </div>
        <!-- 剪切板 -->
        <div v-else-if="current === RdpActionItemKeys.CLIPBOARD" class="tool-bar-content">
          <a-textarea class="tool-bar-clipboard"
                      v-model="clipboardData"
                      :ref="setAutoFocus"
                      placeholder="远程剪切板"
                      allow-clear />
          <!-- 按钮 -->
          <a-space class="tool-bar-content-footer">
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
        <!-- 文件上传 -->
        <div v-else-if="current === RdpActionItemKeys.UPLOAD" class="tool-bar-content">
          <a-upload class="tool-bar-upload"
                    v-model:file-list="fileList"
                    :auto-upload="false"
                    :show-file-list="false"
                    :draggable="true"
                    :tip="fileList.length ? fileList[0]?.name : '选择文件后会自动上传至驱动目录'"
                    @change="onSelectFile" />
          <!-- 按钮 -->
          <a-space class="tool-bar-content-footer">
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
    RdpCombinationKeyItems,
    RdpActionItemKeys,
    RdpActionBarItems,
    screenResolutionKey,
    fitDisplayValue
  } from '@/views/terminal/types/const';
  import { computed, ref, watch, onMounted } from 'vue';
  import { setAutoFocus } from '@/utils/dom';
  import { saveAs } from 'file-saver';
  import { readText } from '@/hooks/copy';
  import { useTerminalStore, useDictStore } from '@/store';
  import { getDisplaySize } from '@/views/terminal/types/utils';
  import useVisible from '@/hooks/visible';

  const props = defineProps<{
    session: IRdpSession;
    direction: string;
  }>();

  const { preference, transferManager } = useTerminalStore();
  const { toOptions, getDictValue } = useDictStore();
  const { visible, setVisible } = useVisible();

  const current = ref('');
  const displaySize = ref(fitDisplayValue);
  const clipboardData = ref('');
  const fileList = ref<FileItem[]>([]);

  const actions = computed(() => {
    return RdpActionBarItems.filter(item => {
      return preference.rdpActionBarSetting[item.item] !== false;
    }).map(item => {
      const key = item.item;
      return {
        ...item,
        active: current.value === key,
        disabled: (key === RdpActionItemKeys.DISPLAY || key === RdpActionItemKeys.SAVE_RDP || RdpActionItemKeys.DISCONNECT || key === RdpActionItemKeys.CLOSE) ? false : !props.session.isWriteable(),
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
    if (key === RdpActionItemKeys.DISPLAY) {
      // 显示设置
      current.value = RdpActionItemKeys.DISPLAY;
      if (props.session.displayHandler?.autoFit) {
        displaySize.value = fitDisplayValue;
      } else {
        displaySize.value = `${props.session.displayHandler?.displayWidth || 0}x${props.session.displayHandler?.displayHeight || 0}`;
      }
    } else if (key === RdpActionItemKeys.COMBINATION_KEY) {
      // 组合键
      current.value = RdpActionItemKeys.COMBINATION_KEY;
    } else if (key === RdpActionItemKeys.CLIPBOARD) {
      // 剪切板
      current.value = RdpActionItemKeys.CLIPBOARD;
      readText(false)
        .then(s => clipboardData.value = s)
        .catch(() => clipboardData.value = '');
    } else if (key === RdpActionItemKeys.UPLOAD) {
      // 文件上传
      current.value = RdpActionItemKeys.UPLOAD;
      fileList.value = [];
    } else if (key === RdpActionItemKeys.SAVE_RDP) {
      // 保存 rdp 文件
      saveRdpFile();
    } else if (key === RdpActionItemKeys.DISCONNECT) {
      // 断开连接
      disconnect();
    } else if (key === RdpActionItemKeys.CLOSE) {
      // 关闭工具栏
      setVisible(false);
    }
  };

  // 临时自适应
  const fitOnce = () => {
    props.session.displayHandler?.fit(true);
    setVisible(false);
  };

  // 设置显示大小
  const setDisplaySize = () => {
    const displayHandler = props.session.displayHandler;
    if (!displayHandler) {
      return;
    }
    if (displaySize.value === fitDisplayValue) {
      // 设置自适应
      displayHandler.autoFit = true;
      displayHandler.fit(true);
    } else {
      try {
        // 获取大小
        const [width, height] = getDisplaySize(displaySize.value, true);
        // 取消自适应
        displayHandler.autoFit = false;
        // 设置大小
        displayHandler.resize(width, height);
      } catch (e) {
        return;
      }
    }
    setVisible(false);
  };

  // 保存 rdp 文件
  const saveRdpFile = () => {
    const address = props.session.info.address;
    const port = props.session.info.port;
    const username = props.session.info.username;
    const content = `auto connect:i:1\nfull address:s:${address}:${port}\nusername:s:${username}`;
    saveAs(new Blob([content], { type: 'text/plain;charset=utf-8' }), `${address}.rdp`);
  };

  // 触发组合键
  const triggerCombinationKey = (keys: Array<number>) => {
    props.session.sendKeys(keys);
    setVisible(false);
  };

  // 发送剪切板数据
  const sendClipboardData = () => {
    // 粘贴
    props.session.paste(clipboardData.value);
    setVisible(false);
  };

  // 清空剪切板数据
  const clearClipboardData = () => {
    clipboardData.value = '';
  };

  // 上传文件
  const uploadFile = () => {
    transferManager.rdp.addUpload(props.session, fileList.value[0].file as File);
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

  // 设置选中
  onMounted(() => {
    if (actions.value?.length) {
      current.value = actions.value[0].item;
    }
  });

</script>

<style lang="less" scoped>
  .tool-bar {
    display: flex;
    border-radius: 8px;
    transition: .3s all;
    background: var(--color-bg-rdp-toolbar);
    filter: contrast(50%) brightness(50%);

    &.top {
      width: 240px;
      height: 8px;

      &:hover {
        transform: translateY(2px);
      }
    }

    &.right {
      width: 8px;
      height: 228px;

      &:hover {
        transform: translateX(-2px);
      }
    }

    &:hover {
      background: var(--color-bg-rdp-toolbar-hover);
    }
  }
</style>

<style lang="less">
  @action-size: 42px;

  .tool-bar-popover.top {
    .arco-popover-content {
      flex-direction: column;
      width: var(--actions-width);
    }

    .tool-bar-content {
      margin-top: 16px;
      max-height: var(--actions-width);
    }

    .tool-bar-upload, .tool-bar-clipboard {
      height: 186px;
    }
  }

  .tool-bar-popover.right {
    .arco-popover-content {
      flex-direction: row-reverse;
      max-height: var(--actions-width);
    }

    .tool-bar-content {
      margin-right: 16px;
      width: var(--actions-width);
    }

    .tool-bar-upload, .tool-bar-clipboard {
      height: calc(var(--actions-width) - 40px);
    }
  }

  .tool-bar-popover {
    --actions-width: calc(var(--action-count) * @action-size + (var(--action-count) - 1) * 16px);
    background: var(--color-bg-2);

    .arco-popover-content {
      margin-top: 0;
      display: flex;
    }

    .tool-bar-button {
      width: @action-size !important;
      height: @action-size !important;
      font-size: 20px;
    }

    .tool-bar-content {
      display: flex;
      flex-direction: column;
    }

    .tool-bar-content-footer {
      margin-top: 12px;
      display: flex;
      justify-content: flex-end;
    }

    .display-size-label {
      padding-right: 6px;
      user-select: none;
      text-align: end;
    }

    .display-size-input {
      width: 198px;
    }

    .tool-bar-upload, .tool-bar-clipboard {
      display: flex;
    }

    .combination-key-item {
      span {
        display: block;
        padding: 6px 12px;
        cursor: pointer;
        background: var(--color-fill-1);
        border-radius: 2px;
        user-select: none;
        transition: 0.2s ALL;

        &:hover {
          background: var(--color-fill-2);
        }
      }
    }
  }

</style>
