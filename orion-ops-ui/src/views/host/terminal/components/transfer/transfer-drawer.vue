<template>
  <a-drawer v-model:visible="visible"
            title="文件传输列表"
            :width="388"
            :unmount-on-close="false"
            :footer="false">
    <a-spin class="full" :loading="loading">
      <a-list class="hosts-list-container"
              size="smail"
              max-height="100%"
              :hoverable="true"
              :bordered="false"
              :data="transferManager.transferList">
        <!-- 空数据 -->
        <template #empty>
          <a-empty style="flex-direction: column;"
                   description="无传输文件" />
        </template>
        <!-- 数据 -->
        <template #item="{ item }">
          <a-list-item class="transfer-item-wrapper">
            <div class="transfer-item">
              <!-- 左侧图标 -->
              <div class="transfer-item-left">
                <span class="file-icon">
                  <icon-upload v-if="item.type === TransferType.UPLOAD" />
                  <icon-download v-else-if="item.type === TransferType.DOWNLOAD" />
                </span>
              </div>
              <!-- 中间信息 -->
              <div class="transfer-item-center">
                <!-- 文件名称 -->
                <a-tooltip position="top"
                           :mini="true"
                           :auto-fix-position="false"
                           content-class="terminal-tooltip-content"
                           arrow-class="terminal-tooltip-content"
                           :content="item.name">
                  <span class="file-name">
                    {{ item.name }}
                  </span>
                </a-tooltip>
                <!-- 传输进度 -->
                <span class="transfer-progress">
                  <!-- 当前大小 -->
                  <span v-if="item.status === TransferStatus.TRANSFERRING">{{ getFileSize(item.currentSize) }}</span>
                  <span class="mx4" v-if="item.status === TransferStatus.TRANSFERRING">/</span>
                  <!-- 总大小 -->
                  <span>{{ getFileSize(item.totalSize) }}</span>
                  <!-- 进度百分比 -->
                  <span class="ml8" v-if="item.status === TransferStatus.TRANSFERRING">
                    {{ (item.currentSize / item.totalSize * 100).toFixed(2) }}%
                  </span>
                </span>
                <!-- 目标目录 -->
                <a-tooltip v-if="item.parentPath"
                           position="top"
                           :mini="true"
                           :auto-fix-position="false"
                           content-class="terminal-tooltip-content"
                           arrow-class="terminal-tooltip-content"
                           :content="item.parentPath">
                  <span class="target-path">
                    {{ item.parentPath }}
                  </span>
                </a-tooltip>
                <!-- 错误信息 -->
                <a-tooltip v-if="item.errorMessage"
                           position="top"
                           :mini="true"
                           :auto-fix-position="false"
                           content-class="terminal-tooltip-content"
                           arrow-class="terminal-tooltip-content"
                           :content="item.errorMessage">
                  <span class="error-message">
                    {{ item.errorMessage }}
                  </span>
                </a-tooltip>
              </div>
              <!-- 右侧状态/操作-->
              <div class="transfer-item-right">
                <!-- 传输状态 -->
                <div class="transfer-item-right-progress">
                  <!-- 等待传输 -->
                  <icon-clock-circle v-if="item.status === TransferStatus.WAITING" />
                  <!-- 传输进度 -->
                  <a-progress v-else
                              type="circle"
                              size="mini"
                              :status="item.status"
                              :percent="item.currentSize / item.totalSize" />
                </div>
                <!-- 传输操作 -->
                <div class="transfer-item-right-actions">
                  <!-- 关闭 -->
                  <span class="close-icon" @click="removeTask(item.fileId)">
                    <icon-close />
                 </span>
                </div>
              </div>
            </div>
          </a-list-item>
        </template>
      </a-list>
    </a-spin>
  </a-drawer>
</template>

<script lang="ts">
  export default {
    name: 'transferDrawer'
  };
</script>

<script lang="ts" setup>
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import { useTerminalStore } from '@/store';
  import { getFileSize } from '@/utils/file';
  import { TransferStatus, TransferType } from '../../types/terminal.const';

  const { transferManager } = useTerminalStore();
  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  // 打开
  const open = () => {
    setVisible(true);
  };

  defineExpose({ open });

  // 移除任务
  const removeTask = (fileId: string) => {
    transferManager.cancelTransfer(fileId);
  };

  // 关闭
  const handleClose = () => {
    handlerClear();
  };

  // 清空
  const handlerClear = () => {
    setLoading(false);
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

      .file-name {
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
