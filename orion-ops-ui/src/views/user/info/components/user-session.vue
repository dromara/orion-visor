<template>
  <div class="main-container">
    <span class="extra-message">
      <template v-if="user">
        用户 <span class="user-info">{{ user.nickname }}({{ user.username }})</span> 所有登录设备的会话列表
      </template>
      <template v-else>
        所有登录设备的会话列表
      </template>
    </span>
    <!-- 加载中 -->
    <a-skeleton v-if="loading"
                style="width: 70%;"
                :animation="true">
      <a-skeleton-line :rows="4" />
    </a-skeleton>
    <!-- 登录会话时间线 -->
    <a-timeline v-else-if="list.length">
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
              <a-button v-else-if="!user || hasPermission('infra:system-user:management:offline-session')"
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
    <!-- 空 -->
    <a-empty v-else />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'user-session'
  };
</script>

<script lang="ts" setup>
  import type { UserQueryResponse } from '@/api/user/user';
  import type { UserSessionQueryResponse } from '@/api/user/user';
  import type { PropType } from 'vue';
  import useLoading from '@/hooks/loading';
  import { ref, onBeforeMount } from 'vue';
  import { getCurrentUserSessionList, offlineCurrentUserSession } from '@/api/user/mine';
  import { dateFormat } from '@/utils';
  import { isMobile } from '@/utils/is';
  import { Message } from '@arco-design/web-vue';
  import usePermission from '@/hooks/permission';
  import { getUserSessionList, offlineUserSession } from '@/api/user/user';

  const props = defineProps({
    user: Object as PropType<UserQueryResponse>,
  });

  const list = ref<UserSessionQueryResponse[]>([]);

  const { loading, setLoading } = useLoading();
  const { hasPermission } = usePermission();

  // 下线
  const offline = async (item: UserSessionQueryResponse) => {
    try {
      setLoading(true);
      if (props.user) {
        // 下线其他用户
        await offlineUserSession({
          userId: props.user?.id,
          timestamp: item.loginTime
        });
      } else {
        // 下线当前用户
        await offlineCurrentUserSession({
          timestamp: item.loginTime
        });
      }
      Message.success('操作成功');
      item.visible = false;
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 查询登录会话
  onBeforeMount(async () => {
    try {
      setLoading(true);
      let sessions: UserSessionQueryResponse[];
      if (props.user) {
        // 查询其他用户
        const { data } = await getUserSessionList(props.user.id);
        sessions = data;
      } else {
        // 查询当前用户
        const { data } = await getCurrentUserSessionList();
        sessions = data;
      }
      sessions.forEach(s => s.visible = true);
      list.value = sessions;
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
    margin-bottom: 42px;
    margin-left: -24px;
    display: block;
    color: var(--color-text-3);
    user-select: none;

    .user-info {
      color: rgb(var(--primary-6));
      font-weight: 600;
    }
  }

  .icon-container {
    border-radius: 50%;
    width: 56px;
    height: 56px;
    color: var(--color-white);
    background: var(--color-fill-4);
    font-size: 28px;
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

    .arco-timeline-item-dot-custom {
      background: unset;
    }
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
