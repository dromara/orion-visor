<template>
  <a-list-item class="transfer-item-wrapper">
    <div class="transfer-item">
      <!-- 左侧图标 -->
      <div class="transfer-item-left">
        <span class="file-icon">
          <icon-upload v-if="task.type === TransferType.UPLOAD" />
          <icon-download v-else-if="task.type === TransferType.DOWNLOAD" />
        </span>
      </div>
      <!-- 中间信息 -->
      <div class="transfer-item-center">
        <!-- 文件名称 -->
        <span class="file-name text-copy"
              :title="task.fileItem.name"
              @click="copy(task.fileItem.name)">
          {{ task.fileItem.name }}
        </span>
        <!-- 传输进度 -->
        <span class="transfer-progress">
          <!-- 当前大小 -->
          <span v-if="task.state.status === TransferStatus.TRANSFERRING && task.fileItem.unknownSize !== true">{{ getFileSize(task.state.currentSize) }}</span>
          <span class="mx4" v-if="task.state.status === TransferStatus.TRANSFERRING && task.fileItem.unknownSize !== true">/</span>
          <!-- 总大小 -->
          <span>{{ getFileSize(task.state.totalSize) }}</span>
          <!-- 进度百分比 -->
          <span class="ml8" v-if="task.state.status === TransferStatus.TRANSFERRING && task.fileItem.unknownSize !== true">
            {{ task.state.progress }}%
          </span>
        </span>
        <!-- 目标目录 -->
        <span class="target-path text-copy"
              :title="task.fileItem.parentPath"
              @click="copy(task.fileItem.parentPath)">
          {{ task.fileItem.parentPath }}
        </span>
        <!-- 错误信息 -->
        <a-tooltip v-if="task.state.errorMessage"
                   position="top"
                   :mini="true"
                   :auto-fix-position="false"
                   content-class="terminal-tooltip-content"
                   arrow-class="terminal-tooltip-content"
                   :content="task.state.errorMessage">
          <span class="error-message">
            {{ task.state.errorMessage }}
          </span>
        </a-tooltip>
      </div>
      <!-- 右侧状态/操作-->
      <div class="transfer-item-right">
        <!-- 传输状态 -->
        <div class="transfer-item-right-progress">
          <!-- 等待传输 -->
          <icon-clock-circle v-if="task.state.status === TransferStatus.WAITING" />
          <!-- 传输中-但不知道文件大小 -->
          <icon-loading v-else-if="task.state.status === TransferStatus.TRANSFERRING && task.fileItem.unknownSize === true" />
          <!-- 传输进度 -->
          <a-progress v-else
                      type="circle"
                      size="mini"
                      :status="getDictValue(transferStatusKey, task.state.status, 'status')"
                      :percent="task.state.currentSize / task.state.totalSize" />
        </div>
        <!-- 传输操作 -->
        <div class="transfer-item-right-actions">
          <!-- 关闭 -->
          <span class="close-icon" @click="removeTask">
            <icon-close />
          </span>
        </div>
      </div>
    </div>
  </a-list-item>
</template>

<script lang="ts">
  export default {
    name: 'transferTask'
  };
</script>

<script lang="ts" setup>
  import type { IFileTransferTask } from '@/views/terminal/interfaces';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import { useDictStore, useTerminalStore } from '@/store';
  import { copy } from '@/hooks/copy';
  import { getFileSize } from '@/utils/file';
  import { TransferStatus, TransferType, transferStatusKey, TransferSource } from '../../types/const';

  const props = defineProps<{
    task: IFileTransferTask;
  }>();

  const { transferManager } = useTerminalStore();
  const { getDictValue } = useDictStore();
  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  // 移除任务
  const removeTask = () => {
    const fileId = props.task.fileId;
    let manager;
    if (props.task.source === TransferSource.SFTP) {
      manager = transferManager.sftp;
    } else if (props.task.source === TransferSource.RDP) {
      manager = transferManager.rdp;
    }
    manager?.cancelTransfer(fileId);
  };

</script>

<style lang="less" scoped>
  @icon-size: 20px;
  @item-left-width: 42px;
  @item-right-width: 42px;
  @item-center-width: 388px - @item-left-width - @item-right-width;

  .transfer-item {
    min-height: 36px;
    padding: 8px 0;
    display: flex;
    align-items: center;

    &:hover {
      .transfer-item-right-progress {
        display: none;
      }

      .transfer-item-right-actions {
        display: flex;
      }
    }

    &-left {
      width: @item-left-width;
      display: flex;
      justify-content: center;

      .file-icon {
        color: rgb(var(--arcoblue-6));
        font-size: 18px;
      }
    }

    &-center {
      width: @item-center-width;
      display: flex;
      flex-direction: column;

      .file-name, .target-path {
        color: var(--color-content-text-1);
        overflow: hidden;
        text-overflow: ellipsis;
        width: fit-content;
        max-width: 100%;
        white-space: nowrap;
      }

      .transfer-progress, .target-path, .error-message {
        padding-top: 4px;
        font-size: 13px;
        color: var(--color-neutral-8);
        width: fit-content;
      }

      .error-message {
        color: rgba(var(--red-6));
      }
    }

    &-right {
      width: @item-right-width;

      &-progress {
        display: flex;
        justify-content: center;
      }

      &-actions {
        display: none;
        justify-content: center;

        .close-icon {
          width: @icon-size;
          height: @icon-size;
          border-radius: 50%;
          display: flex;
          justify-content: center;
          align-items: center;
          cursor: pointer;

          &:hover {
            background: var(--color-fill-2);
          }
        }
      }
    }
  }

</style>
