<template>
  <a-spin :loading="loading" class="main-container">
    <span class="extra-message">所有登录设备的会话列表</span>
    <a-timeline>
      <template v-for="item in list"
                :key="item.loginTime">
        <a-timeline-item v-if="item.visible">
          <!-- 图标 -->
          <template #dot>
            <div class="icon-container">
              <icon-mobile v-if="isMobile(item.userAgent)" />
              <icon-desktop v-else />
            </div>
          </template>
          <!-- 会话行 -->
          <div class="session-line">
            <!-- 地址行 -->
            <a-space class="address-line">
              <span>{{ item.address }}</span>
              <span>{{ item.location }}</span>
              <a-tag v-if="item.current" color="arcoblue">当前会话</a-tag>
              <a-button v-else
                        style="font-weight: 600;"
                        type="text"
                        size="mini"
                        status="danger"
                        @click="offline(item)">
                下线
              </a-button>
            </a-space>
            <!-- 时间行 -->
            <span class="time-line">
            {{ dateFormat(new Date(item.loginTime)) }}
          </span>
            <!-- ua -->
            <span class="ua-line">
            {{ item.userAgent }}
          </span>
          </div>
        </a-timeline-item>
      </template>
    </a-timeline>
  </a-spin>
</template>

<script lang="ts">
  export default {
    name: 'user-session'
  };
</script>

<script lang="ts" setup>
  import type { UserSessionQueryResponse } from '@/api/user/user';
  import useLoading from '@/hooks/loading';
  import { ref, onMounted } from 'vue';
  import { getCurrentUserSessionList, offlineCurrentUserSession } from '@/api/user/mine';
  import { dateFormat } from '@/utils';
  import { isMobile } from '@/utils/is';
  import { Message } from '@arco-design/web-vue';

  const list = ref<UserSessionQueryResponse[]>([]);

  const { loading, setLoading } = useLoading();

  // 下线
  const offline = async (item: UserSessionQueryResponse) => {
    try {
      setLoading(true);
      await offlineCurrentUserSession({
        timestamp: item.loginTime
      });
      Message.success('操作成功');
      item.visible = false;
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 查询登录会话
  onMounted(async () => {
    try {
      setLoading(true);
      const { data } = await getCurrentUserSessionList();
      data.forEach(s => s.visible = true);
      list.value = data;
    } catch (e) {
    } finally {
      setLoading(false);
    }
  });

</script>

<style lang="less" scoped>
  .main-container {
    width: 100%;
    min-height: 200px;
    padding-left: 48px;
  }

  .extra-message {
    margin-bottom: 38px;
    margin-left: -24px;
    display: block;
    color: var(--color-text-3);
    user-select: none;
  }

  .icon-container {
    border-radius: 50%;
    width: 56px;
    height: 56px;
    background: var(--color-fill-4);
    font-size: 28px;
    color: #FFFFFF;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  :deep(.arco-timeline-item-content-wrapper) {
    position: relative;
    margin-left: 44px;
    margin-top: -22px;
  }

  :deep(.arco-timeline-item) {
    padding-bottom: 36px;
  }

  .session-line {
    display: flex;
    flex-direction: column;

    .address-line {
      color: var(--color-text-1);
      font-size: 15px;
      font-weight: 600;
      margin-bottom: 2px;
    }

    .time-line, .ua-line {
      color: var(--color-text-3);
      font-size: 14px;
      margin-top: 2px;
    }
  }

</style>
