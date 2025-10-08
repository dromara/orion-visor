<template>
  <div v-if="render" class="full">
    <!-- 消息分类 -->
    <a-spin class="message-classify-container"
            :hide-icon="true"
            :loading="fetchLoading">
      <a-tabs v-model:active-key="currentClassify"
              type="rounded"
              :hide-content="true"
              @change="loadClassifyMessage">
        <!-- 消息列表 -->
        <a-tab-pane v-for="item in toOptions(messageClassifyKey)"
                    :key="item.value as string">
          <!-- 标题 -->
          <template #title>
            <span class="usn">{{ item.label }} ({{ classifyCount[item.value as any] || 0 }})</span>
          </template>
          <!-- 消息列表 -->
        </a-tab-pane>
        <!-- 右侧操作 -->
        <template #extra>
          <a-space>
            <!-- 状态 -->
            <a-switch v-model="queryUnread"
                      style="margin-right: 4px;"
                      type="round"
                      checked-text="未读"
                      unchecked-text="全部"
                      @change="reloadAllMessage" />
            <!-- 更多操作 -->
            <a-dropdown trigger="hover" :popup-max-height="false">
              <icon-more class="card-extra-icon" />
              <template #content>
                <!-- 全部已读 -->
                <a-doption title="=全部已读" @click="setAllRead">
                  <span class="more-doption normal">全部已读</span>
                </a-doption>
                <!-- 清空已读 -->
                <a-doption title="清空全部已读消息" @click="clearAllMessage">
                  <span class="more-doption normal">清空已读</span>
                </a-doption>
              </template>
            </a-dropdown>
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
  import { ref, onMounted, onUnmounted } from 'vue';
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
  import { clearHtmlTag, replaceHtmlTag } from '@/utils';
  import { useDictStore } from '@/store';
  import { dictKeys, messageClassifyKey, messageTypeKey, defaultClassify, MESSAGE_CONFIG_KEY, messageLimit, MessageStatus } from './const';
  import List from './list.vue';

  const { loading: fetchLoading, setLoading: setFetchLoading } = useLoading();
  const { loading: messageLoading, setLoading: setMessageLoading } = useLoading();
  const { loadKeys, toOptions, getDictValue } = useDictStore();
  const router = useRouter();

  const currentClassify = ref(defaultClassify);
  const queryUnread = ref(false);
  const classifyCount = ref<Record<string, number>>({});
  const messageList = ref<Array<MessageRecordResponse>>([]);
  const hasMore = ref(true);
  const render = ref(false);

  // 重新加载消息
  const reloadAllMessage = async () => {
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
        page: 1,
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
    await reloadAllMessage();
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
  onMounted(async () => {
    await loadKeys(dictKeys);
    render.value = true;
  });

  // 获取消息
  onMounted(() => {
    // 获取配置缓存
    const item = localStorage.getItem(MESSAGE_CONFIG_KEY);
    if (item) {
      const config = JSON.parse(item) as Record<string, any>;
      if (config?.currentClassify) {
        currentClassify.value = config.currentClassify;
      }
      if (config?.queryUnread) {
        queryUnread.value = config.queryUnread;
      }
    }
    // 查询数据
    reloadAllMessage();
  });

  // 设置缓存配置
  onUnmounted(() => {
    localStorage.setItem(MESSAGE_CONFIG_KEY, JSON.stringify({
      currentClassify: currentClassify.value,
      queryUnread: queryUnread.value,
    }));
  });

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

    :deep(.arco-tabs-tab) {
      margin: 0 6px 0 0 !important;
    }
  }

</style>
