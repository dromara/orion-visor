<template>
  <!-- 消息列表 -->
  <a-spin class="message-list-container" :loading="messageLoading">
    <!-- 加载中 -->
    <div v-if="!messageList.length && fetchLoading">
      <!-- 加载中 -->
      <a-skeleton class="skeleton-wrapper" :animation="true">
        <a-skeleton-line :rows="3"
                         :line-height="86"
                         :line-spacing="8" />
      </a-skeleton>
    </div>
    <!-- 无数据 -->
    <div v-else-if="!messageList.length && !fetchLoading">
      <a-result status="404">
        <template #subtitle>暂无内容</template>
      </a-result>
    </div>
    <!-- 消息容器 -->
    <div v-else class="message-list-wrapper">
      <a-scrollbar style="overflow-y: auto; height: 100%;">
        <!-- 消息列表-->
        <div v-for="message in messageList"
             class="message-item"
             :class="[ message.status === MessageStatus.READ ? 'message-item-read' : 'message-item-unread' ]"
             @click="emits('click', message)">
          <!-- 标题 -->
          <div class="message-item-title">
            <!-- 标题 -->
            <div class="message-item-title-text text-ellipsis" :title="message.title">
              {{ message.title }}
            </div>
            <!-- tag -->
            <div class="message-item-title-status">
              <template v-if="getDictValue(messageTypeKey, message.type, 'tagVisible', false)">
                <a-tag size="mini" :color="getDictValue(messageTypeKey, message.type, 'tagColor')">
                  {{ getDictValue(messageTypeKey, message.type, 'tagLabel') }}
                </a-tag>
              </template>
            </div>
            <!-- 操作 -->
            <div class="message-item-title-actions">
              <!-- 查看 -->
              <a-button class="mr4"
                        size="mini"
                        type="text"
                        @click.stop="emits('view', message)">
                查看
              </a-button>
              <!-- 删除 -->
              <a-button size="mini"
                        type="text"
                        status="danger"
                        @click.stop="emits('delete', message)">
                删除
              </a-button>
            </div>
          </div>
          <!-- 文本 -->
          <div v-html="message.contentHtml"
               class="message-item-content text-ellipsis"
               :title="message.content" />
        </div>
        <!-- 加载中 -->
        <a-skeleton v-if="fetchLoading"
                    class="skeleton-wrapper"
                    :animation="true">
          <a-skeleton-line :rows="3"
                           :line-height="86"
                           :line-spacing="8" />
        </a-skeleton>
        <!-- 加载更多 -->
        <div v-if="hasMore" class="load-more-wrapper">
          <a-button size="small"
                    :fetchLoading="fetchLoading"
                    @click="() => emits('load')">
            加载更多
          </a-button>
        </div>
      </a-scrollbar>
    </div>
  </a-spin>
</template>

<script lang="ts">
  export default {
    name: 'messageBoxList'
  };
</script>

<script lang="ts" setup>
  import type { MessageRecordResponse } from '@/api/system/message';
  import { MessageStatus, messageTypeKey } from './const';
  import { useDictStore } from '@/store';

  const emits = defineEmits(['load', 'click', 'view', 'delete']);
  const props = defineProps<{
    fetchLoading: boolean;
    messageLoading: boolean;
    hasMore: boolean;
    messageList: Array<MessageRecordResponse>;
  }>();

  const { getDictValue } = useDictStore();

</script>

<style lang="less" scoped>
  @gap: 8px;
  @message-height: 86px;
  @actions-width: 82px;

  .skeleton-wrapper {
    padding: 8px 12px 0 12px;
  }

  .message-list-container {
    width: 100%;
    height: 344px;
    display: block;

    .message-list-wrapper {
      width: 100%;
      height: 100%;
      position: relative;
    }

    .load-more-wrapper {
      display: flex;
      justify-content: center;
      margin: 12px 0;
    }
  }

  .message-item {
    height: @message-height;
    padding: 16px 20px;
    border-bottom: 1px solid var(--color-neutral-3);
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    font-size: 14px;
    color: var(--color-text-1);
    cursor: pointer;

    &-title {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;

      &-text {
        width: calc(100% - @actions-width - @gap);
        display: block;
        font-size: 15px;
        font-weight: 600;
        text-overflow: clip;
      }

      &-status {
        width: @actions-width;
        display: flex;
        align-items: flex-start;
        justify-content: flex-end;
      }

      &-actions {
        width: @actions-width;
        display: none;
        justify-content: flex-end;
        align-items: flex-end;

        button {
          padding: 0 6px !important;

          &:hover {
            background: var(--color-fill-3) !important;
          }
        }
      }
    }

    &-content {
      display: block;
      padding-bottom: 2px;
      color: var(--color-text-1);
      text-overflow: clip;
    }
  }

  .message-item:hover {
    background: var(--color-fill-1);

    .message-item-title-status {
      display: none;
    }

    .message-item-title-actions {
      display: flex;
      opacity: 1;
    }
  }

  .message-item-read {
    .message-item-title-text, .message-item-title-status, .message-item-content {
      opacity: .65;
    }
  }

  :deep(.arco-scrollbar) {
    position: absolute;
    height: 100%;
    width: 100%;

    .arco-scrollbar-track-direction-horizontal {
      display: none;
    }
  }

</style>
