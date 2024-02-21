<template>
  <a-drawer v-model:visible="visible"
            title="文件传输列表"
            :width="388"
            :mask-closable="false"
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
                  <icon-upload />
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
                  {{ getFileSize(item.currentSize) }}/{{ getFileSize(item.totalSize) }}
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
              </div>
              <!-- 右侧状态/操作-->
              <div class="transfer-item-right">
                <!-- 等待传输 -->
                <icon-loading v-if="item.status === TransferStatus.WAITING" />
                <!-- 传输进度 -->
                <a-progress v-else
                            type="circle"
                            size="mini"
                            :status="item.status"
                            :percent="item.currentSize / item.totalSize" />
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
  import { TransferStatus } from '../../types/terminal.const';

  const { transferManager } = useTerminalStore();
  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  // 打开
  const open = () => {
    setVisible(true);
  };

  defineExpose({ open });

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
  @item-left-width: 42px;
  @item-right-width: 42px;
  @item-center-width: 388px - @item-left-width - @item-right-width;

  .transfer-item {
    min-height: 36px;
    padding: 8px 0;
    display: flex;
    align-items: center;

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
      }

      .transfer-progress, .target-path {
        padding-top: 4px;
        font-size: 13px;
        color: var(--color-neutral-8);
        width: fit-content;
      }
    }

    &-right {
      width: @item-right-width;
      display: flex;
      justify-content: center;
    }
  }

</style>
