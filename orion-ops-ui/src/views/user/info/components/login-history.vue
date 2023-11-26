<template>
  <a-spin :loading="loading" class="main-container">
    <span class="extra-message">
      <template v-if="user">
        只展示用户 <span class="user-info">{{ user.nickname }}({{ user.username }})</span> 最近登录的 30 条历史记录
      </template>
      <template v-else>
        只展示最近登录的 30 条历史记录
      </template>
    </span>
    <!-- 登录历史时间线 -->
    <a-timeline v-if="list.length">
      <a-timeline-item v-for="item in list"
                       :key="item.id">
        <!-- 图标 -->
        <template #dot>
          <div class="icon-container">
            <icon-mobile v-if="isMobile(item.userAgent)" />
            <icon-desktop v-else />
          </div>
        </template>
        <!-- 日志行 -->
        <div class="log-line">
          <!-- 地址行 -->
          <a-space class="address-line">
            <span class="mr8">{{ item.address }}</span>
            <span>{{ item.location }}</span>
          </a-space>
          <!-- 错误信息行 -->
          <span class="error-line" v-if="item.result === ResultStatus.FAILED">
            登录失败: {{ item.errorMessage }}
          </span>
          <!-- 时间行 -->
          <span class="time-line">
            {{ dateFormat(new Date(item.createTime)) }}
          </span>
          <!-- ua -->
          <span class="ua-line">
            {{ item.userAgent }}
          </span>
        </div>
      </a-timeline-item>
    </a-timeline>
    <!-- 加载中 -->
    <a-space direction="vertical"
             v-else-if="loading"
             :style="{width: '70%'}"
             size="large">
      <a-skeleton-line :rows="4" />
    </a-space>
    <!-- 空 -->
    <a-empty v-else />
  </a-spin>
</template>

<script lang="ts">
  export default {
    name: 'login-history'
  };
</script>

<script lang="ts" setup>
  import type { UserQueryResponse } from '@/api/user/user';
  import type { LoginHistoryQueryResponse } from '@/api/user/operator-log';
  import type { PropType } from 'vue';
  import useLoading from '@/hooks/loading';
  import { ref, onMounted } from 'vue';
  import { ResultStatus } from '../types/const';
  import { getCurrentLoginHistory } from '@/api/user/mine';
  import { getLoginHistory } from '@/api/user/operator-log';
  import { dateFormat } from '@/utils';
  import { isMobile } from '@/utils/is';

  const props = defineProps({
    user: Object as PropType<UserQueryResponse>,
  });

  const list = ref<LoginHistoryQueryResponse[]>([]);

  const { loading, setLoading } = useLoading();

  // 查询操作日志
  onMounted(async () => {
    try {
      setLoading(true);
      if (props.user) {
        // 查询其他用户
        const { data } = await getLoginHistory(props.user.username);
        list.value = data;
      } else {
        // 查询当前用户
        const { data } = await getCurrentLoginHistory();
        list.value = data;
      }
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

  .log-line {
    display: flex;
    flex-direction: column;

    .address-line {
      color: var(--color-text-1);
      font-size: 15px;
      font-weight: 600;
      margin-bottom: 2px;
    }

    .time-line, .ua-line, .error-line {
      color: var(--color-text-3);
      font-size: 14px;
      margin-top: 2px;
    }

    .error-line {
      color: rgb(var(--danger-6));
      font-weight: 600;
    }
  }

</style>
