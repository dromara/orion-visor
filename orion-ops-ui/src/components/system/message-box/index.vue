<template>
  <div class="full">
    <!-- 消息分类 -->
    <a-spin class="message-classify-container"
            :hide-icon="true"
            :loading="fetchLoading">
      <a-tabs v-model:activeKey="currentClassify"
              type="rounded"
              :hide-content="true"
              @change="loadClassifyMessage">
        <!-- 消息列表 -->
        <a-tab-pane v-for="item in toOptions(messageClassifyKey)"
                    :key="item.value">
          <!-- 标题 -->
          <template #title>
            <span class="usn">{{ item.label }} ({{ classifyCount[item.value] || 0 }})</span>
          </template>
          <!-- 消息列表 -->
        </a-tab-pane>
        <!-- 右侧操作 -->
        <template #extra>
          <a-space>
            <!-- 状态 -->
            <a-switch v-model="queryUnread"
                      type="round"
                      checked-text="未读"
                      unchecked-text="全部"
                      @change="changeMessageStatus" />
            <!-- 全部已读 -->
            <a-button class="header-button"
                      type="text"
                      size="small"
                      @click="setAllRead">
              全部已读
            </a-button>
            <!-- 清空 -->
            <a-button class="header-button"
                      type="text"
                      size="small"
                      @click="clearAllMessage">
              清空
            </a-button>
          </a-space>
        </template>
      </a-tabs>
    </a-spin>
    <!-- 消息列表 -->
    <list :fetch-loading="fetchLoading"
          :message-loading="messageLoading"
          :has-more="hasMore"
          :message-list="messageList"
          @load="loadMessage"
          @click="clickMessage"
          @view="viewMessage"
          @delete="deleteMessage" />
    <!-- 模态框 -->
    <modal ref="modalRef"
           @delete="deleteMessage" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'messageBox'
  };
</script>

<script lang="ts" setup>
  import type { MessageRecordResponse } from '@/api/system/message';
  import { ref, onMounted } from 'vue';
  import {
    clearSystemMessage,
    deleteSystemMessage,
    getSystemMessageCount,
    getSystemMessageList,
    updateSystemMessageRead,
    updateSystemMessageReadAll
  } from '@/api/system/message';
  import useLoading from '@/hooks/loading';
  import { useRouter } from 'vue-router';
  import { useDictStore } from '@/store';
  import { dictKeys, messageClassifyKey, messageTypeKey, defaultClassify, messageLimit, MessageStatus } from './const';
  import List from './list.vue';
  import Modal from './modal.vue';
  import { clearHtmlTag, replaceHtmlTag } from '@/utils';

  const { loading: fetchLoading, setLoading: setFetchLoading } = useLoading();
  const { loading: messageLoading, setLoading: setMessageLoading } = useLoading();
  const { loadKeys, toOptions, getDictValue } = useDictStore();
  const router = useRouter();

  const currentClassify = ref(defaultClassify);
  const queryUnread = ref(false);
  const classifyCount = ref<Record<string, number>>({});
  const messageList = ref<Array<MessageRecordResponse>>([]);
  const hasMore = ref(true);
  const modalRef = ref();

  // 修改消息状态
  const changeMessageStatus = async () => {
    hasMore.value = true;
    messageList.value = [];
    // 查询数量
    queryMessageCount();
    // 加载列表
    await loadMessage();
  };

  // 获取数量
  const queryMessageCount = async () => {
    setFetchLoading(true);
    try {
      const { data } = await getSystemMessageCount(queryUnread.value);
      classifyCount.value = data;
    } catch (ex) {
    } finally {
      setFetchLoading(false);
    }
  };

  // 查询分类消息
  const loadClassifyMessage = async () => {
    hasMore.value = true;
    messageList.value = [];
    await loadMessage();
  };

  // 加载消息
  const loadMessage = async () => {
    hasMore.value = true;
    setFetchLoading(true);
    try {
      const maxId = messageList.value.length
        ? messageList.value[messageList.value.length - 1].id
        : undefined;
      // 查询数据
      const { data } = await getSystemMessageList({
        limit: messageLimit,
        classify: currentClassify.value,
        queryUnread: queryUnread.value,
        maxId,
      });
      data.forEach(s => {
        messageList.value.push({
          ...s,
          content: clearHtmlTag(s.content),
          contentHtml: replaceHtmlTag(s.content),
        });
      });
      hasMore.value = data.length === messageLimit;
    } catch (ex) {
    } finally {
      setFetchLoading(false);
    }
  };

  // 设置全部已读
  const setAllRead = async () => {
    setMessageLoading(true);
    try {
      // 设置为已读
      await updateSystemMessageReadAll(currentClassify.value);
      // 修改状态
      messageList.value.forEach(s => s.status = MessageStatus.READ);
    } catch (ex) {
    } finally {
      setMessageLoading(false);
    }
  };

  // 清理已读消息
  const clearAllMessage = async () => {
    setMessageLoading(true);
    try {
      // 清理消息
      await clearSystemMessage(currentClassify.value);
    } catch (ex) {
    } finally {
      setMessageLoading(false);
    }
    // 查询消息
    await changeMessageStatus();
  };

  // 点击消息
  const clickMessage = (message: MessageRecordResponse) => {
    // 设置为已读
    if (message.status === MessageStatus.UNREAD) {
      updateSystemMessageRead(message.id);
      message.status = MessageStatus.READ;
    }
    const redirectComponent = getDictValue(messageTypeKey, message.type, 'redirectComponent');
    if (redirectComponent && redirectComponent !== '0') {
      // 跳转组件
      router.push({ name: redirectComponent, query: { key: message.relKey } });
    } else {
      // 打开消息模态框
      modalRef.value.open(message);
    }
  };

  // 查看消息
  const viewMessage = async (message: MessageRecordResponse) => {
    setMessageLoading(true);
    try {
      // 设置为已读
      if (message.status === MessageStatus.UNREAD) {
        await updateSystemMessageRead(message.id);
        message.status = MessageStatus.READ;
      }
      // 打开消息模态框
      modalRef.value.open(message);
    } catch (ex) {
    } finally {
      setMessageLoading(false);
    }
  };

  // 删除消息
  const deleteMessage = async (message: MessageRecordResponse) => {
    setMessageLoading(true);
    try {
      // 删除消息
      await deleteSystemMessage(message.id);
      // 减少数量
      classifyCount.value[currentClassify.value] -= 1;
      // 移除
      const index = messageList.value.findIndex(s => s.id === message.id);
      messageList.value.splice(index, 1);
    } catch (ex) {
    } finally {
      setMessageLoading(false);
    }
  };

  // 加载字典值
  onMounted(() => {
    loadKeys(dictKeys);
  });

  // 获取消息
  onMounted(changeMessageStatus);

</script>

<style lang="less" scoped>
  :deep(.arco-popover-popup-content) {
    padding: 0;
  }

  :deep(.arco-tabs-nav) {
    padding: 10px 12px;
    border-bottom: 1px solid var(--color-neutral-3);
  }

  :deep(.arco-tabs-content) {
    padding-top: 0;
  }

  .message-classify-container {
    width: 100%;
    height: 100%;
    display: block;

    .header-button {
      padding: 0 6px;
    }
  }

</style>
