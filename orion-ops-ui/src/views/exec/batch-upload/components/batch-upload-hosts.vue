<template>
  <div class="container">
    <!-- 表头 -->
    <div class="panel-header">
      <h3>上传主机</h3>
      <!-- 操作 -->
      <a-button-group size="mini">
        <!-- 返回 -->
        <a-button @click="emits('back')">返回</a-button>
        <!-- 取消上传 -->
        <a-button v-if="status.value === UploadTaskStepStatus.UPLOADING.value"
                  type="primary"
                  status="warning"
                  @click="emits('cancel')">
          取消上传
        </a-button>
      </a-button-group>
    </div>
    <!-- 主机列表 -->
    <div class="wrapper">
      <a-scrollbar style="overflow-y: auto; height: 100%;">
        <!-- 主机 -->
        <div v-for="host in task.hosts"
             class="host-item"
             :class="[ selectedHost === host.id ? 'host-item-active' : '']"
             @click="changeSelectedHost(host.id)">
          <!-- 主机信息 -->
          <div class="host-item-host">
            <!-- 主机名称 -->
            <div class="host-item-name text-ellipsis" :title="host.name">
              {{ host.name }}
            </div>
            <!-- 主机地址 -->
            <div class="host-item-address text-ellipsis" :title="host.address">
              {{ host.address }}
            </div>
          </div>
          <!-- 主机状态 -->
          <a-space class="host-item-status" direction="vertical">
            <!-- 未完成 -->
            <a-tag class="host-item-status-tag"
                   color="#73D13D"
                   title="未完成数量"
                   size="small">
              {{ host.files.length - getFinishCount(host.files) }}
              <template #icon>
                <icon-clock-circle class="host-item-status-icon" />
              </template>
            </a-tag>
            <!-- 已完成 -->
            <a-tag class="host-item-status-tag"
                   color="#40A9FF"
                   title="已完成数量"
                   size="small">
              {{ getFinishCount(host.files) }}
              <template #icon>
                <icon-check-circle class="host-item-status-icon" />
              </template>
            </a-tag>
          </a-space>
        </div>
      </a-scrollbar>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'batchUploadHosts'
  };
</script>

<script lang="ts" setup>
  import type { UploadTaskQueryResponse } from '@/api/exec/upload-task';
  import type { UploadTaskFile } from '@/api/exec/upload-task';
  import type { UploadTaskStatusType } from '../types/const';
  import { UploadTaskStepStatus } from '../types/const';
  import { UploadTaskFileStatus } from '@/views/exec/upload-task/types/const';

  const emits = defineEmits(['update:selectedHost', 'back', 'cancel']);
  const props = defineProps<{
    status: UploadTaskStatusType;
    selectedHost: number;
    task: UploadTaskQueryResponse;
  }>();

  // 修改选中的主机
  const changeSelectedHost = (id: number) => {
    emits('update:selectedHost', id);
  };

  // 获取已完成数量
  const getFinishCount = (files: Array<UploadTaskFile>) => {
    return files.filter(s => s.status === UploadTaskFileStatus.FINISHED
      || s.status === UploadTaskFileStatus.CANCELED
      || s.status === UploadTaskFileStatus.FAILED).length;
  };

</script>

<style lang="less" scoped>

  .wrapper {
    width: 100%;
    height: calc(100% - 36px);
    position: relative;

    .host-item {
      padding: 12px;
      border-radius: 6px;
      margin-bottom: 12px;
      display: flex;
      justify-content: space-between;
      position: relative;
      background: var(--color-fill-1);
      cursor: pointer;

      &:last-child {
        margin-bottom: 0;
      }

      &:hover {
        background: var(--color-fill-2);
      }

      &-host {
        width: calc(100% - 64px);
        display: flex;
        align-items: flex-start;
        flex-direction: column;
        justify-content: center;
      }

      &-status {
        user-select: none;

        &-tag {
          max-width: 64px;
          width: 100%;
        }

        &-icon {
          color: #FFFFFF;
        }
      }

      &-name {
        width: 100%;
        margin-bottom: 12px;
        font-size: 14px;
        color: var(--color-text-1);
      }

      &-address {
        width: 100%;
        font-size: 12px;
        color: var(--color-text-3);
      }

    }

    .host-item-active {
      background: var(--color-fill-2) !important;

      &::after {
        width: 3px;
        height: 100%;
        border-radius: 4px 6px 6px 4px;
        display: block;
        position: absolute;
        top: 0;
        right: 1px;
        background: rgb(var(--arcoblue-6));
        content: '';
      }
    }
  }

  :deep(.arco-scrollbar) {
    position: absolute;
    height: 100%;
    width: 100%;
  }

</style>
