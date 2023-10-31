<template>
  <a-spin :loading="loading" class="main-container">
    <span class="extra-message">只展示最近登录的 30 条历史记录</span>
    <a-timeline>
      <a-timeline-item v-for="item in list"
                       :key="item.id">
        <!-- 图标 -->
        <template #dot>
          <div class="icon-container">
            <icon-desktop />
          </div>
        </template>
        <!-- 日志行 -->
        <div class="log-line">
          <!-- 地址行 -->
          <span class="address-line">
            <span class="mr8">{{ item.address }}</span>
            <span>{{ item.location }}</span>
          </span>
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
  </a-spin>
</template>

<script lang="ts">
  export default {
    name: 'login-history'
  };
</script>

<script lang="ts" setup>
  import type { LoginHistoryQueryResponse } from '@/api/user/operator-log';
  import useLoading from '@/hooks/loading';
  import { ref, onMounted } from 'vue';
  import { useUserStore } from '@/store';
  import { ResultStatus } from '../types/const';
  import { getCurrentLoginHistory } from '@/api/user/mine';
  import { dateFormat } from '@/utils';

  const list = ref<LoginHistoryQueryResponse[]>([]);

  const userStore = useUserStore();
  const { loading, setLoading } = useLoading();

  // 查询操作日志
  onMounted(async () => {
    try {
      setLoading(true);
      const { data } = await getCurrentLoginHistory();
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
    margin-left: -20px;
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

  .log-line {
    display: flex;
    flex-direction: column;

    .address-line {
      color: var(--color-text-1);
      font-size: 16px;
      font-weight: 600;
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
