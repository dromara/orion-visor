<template>
  <a-drawer v-model:visible="visible"
            class="transfer-drawer"
            :width="388"
            :unmount-on-close="false"
            :footer="false"
            @close="emits('closed')">
    <!-- 标题 -->
    <template #title>
      <span class="path-drawer-title usn">
        文件传输列表
      </span>
    </template>
    <a-spin class="full transfer-container" :loading="loading">
      <!-- 头部操作 -->
      <div class="transfer-header">
        <!-- 左侧按钮 -->
        <a-space size="small">
          <!-- 清空 -->
          <a-button class="transfer-header-icon icon-button"
                    title="清空"
                    @click="removeAllTask">
            <icon-close />
          </a-button>
        </a-space>
        <!-- 右侧数量 -->
        <a-space size="small">
          <a-tag v-for="option in toOptions(transferStatusKey)"
                 class="pointer px12"
                 size="large"
                 :color="option.color as string"
                 :title="option.label"
                 :bordered="option.value === filterStatus"
                 :checked="option.value === filterStatus"
                 @click="checkFilterStatus(option.value as string)">
            <!-- 图标 -->
            <component :is="option.icon" />
            <!-- 数量 -->
            <span class="status-count">
              {{ transferManager.transferList.filter(s => s.status === option.value).length }}
            </span>
          </a-tag>
        </a-space>
      </div>
      <!-- 文件列表 -->
      <a-list class="transfer-item-container"
              size="small"
              max-height="100%"
              :hoverable="true"
              :bordered="false"
              :data="transferManager.transferList">
        <!-- 空数据 -->
        <template #empty>
          <a-empty class="list-empty" description="无传输文件" />
        </template>
        <!-- 数据 -->
        <template #item="{ item }">
          <!-- 传输 item -->
          <transfer-item v-show="filterItem(item)" :item="item" />
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
  import type { SftpTransferItem } from '../../types/define';
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import { useDictStore, useTerminalStore } from '@/store';
  import { transferStatusKey } from '../../types/const';
  import TransferItem from './transfer-item.vue';

  const emits = defineEmits(['closed']);

  const { transferManager } = useTerminalStore();
  const { toOptions } = useDictStore();
  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const filterStatus = ref<string>();

  // 打开
  const open = () => {
    setVisible(true);
  };

  defineExpose({ open });

  // 选中过滤状态
  const checkFilterStatus = (status: string) => {
    // 相同则设置为空
    if (status === filterStatus.value) {
      filterStatus.value = undefined;
    } else {
      filterStatus.value = status;
    }
  };

  // 过滤传输行
  const filterItem = (item: SftpTransferItem) => {
    return !filterStatus.value || item.status === filterStatus.value;
  };

  // 移除全部任务
  const removeAllTask = () => {
    transferManager.cancelAllTransfer();
  };

</script>

<style lang="less">

  .transfer-drawer {
    .arco-drawer-body {
      overflow: hidden;
      position: relative;
    }
  }

</style>

<style lang="less" scoped>
  @header-height: 48px;

  .transfer-container {
    position: relative;

    .transfer-header {
      padding: 8px;
      width: 100%;
      height: @header-height;
      position: absolute;
      display: flex;
      align-items: center;
      justify-content: space-between;

      &-icon {
        width: 32px;
        height: 32px;
      }

      .status-count {
        display: inline-block;
        margin-left: 4px;
        user-select: none;
      }
    }

    .transfer-item-container {
      width: 100%;
      height: calc(100% - @header-height);
      position: absolute;
      top: @header-height;
      overflow: auto;

      :deep(.arco-list-item) {
        padding: 0 !important;
      }

      :deep(.arco-scrollbar) {
        height: 100%;
      }
    }
  }

  .list-empty {
    flex-direction: column;
    margin-top: 32px;
  }

</style>
