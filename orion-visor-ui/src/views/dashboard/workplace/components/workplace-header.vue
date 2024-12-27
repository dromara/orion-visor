<template>
  <a-row class="card">
    <!-- 登录用户 -->
    <a-col :span="8">
      <a-typography-title :heading="5" style="padding: 4px 0; margin: 0;">
        欢迎回来! {{ data.infra?.nickname }}
      </a-typography-title>
    </a-col>
    <a-col :span="16" class="header-right flex-center flex-content-end">
      <!-- 未读消息数-->
      <a-tag v-if="data.infra?.unreadMessageCount"
             class="pointer"
             title="查看消息"
             color="arcoblue"
             @click="setVisibleMessageBox">
        <template #icon>
          <icon-message />
        </template>
        您有 {{ data.infra.unreadMessageCount }} 条未读消息
      </a-tag>
      <a-divider v-if="data.infra?.unreadMessageCount"
                 direction="vertical"
                 :margin="14"
                 style="height: 20px;" />
      <!-- 上次登录时间 -->
      <div v-if="data.infra?.lastLoginTime"
           class="last-login-wrapper">
        <span class="last-login-label usn">上次登录时间</span>
        <a-tag color="purple">
          <template #icon>
            <icon-schedule />
          </template>
          {{ dateFormat(new Date(data.infra.lastLoginTime)) }}
        </a-tag>
      </div>
    </a-col>
  </a-row>
</template>

<script lang="ts">
  export default {
    name: 'workplaceHeader'
  };
</script>

<script lang="ts" setup>
  import type { WorkplaceStatisticsData } from '@/views/dashboard/workplace/types/const';
  import { dateFormat } from '@/utils';
  import { openMessageBox } from '@/types/symbol';
  import { inject } from 'vue';

  const props = defineProps<{
    data: WorkplaceStatisticsData;
  }>();

  // 注入打开消息盒子
  const setVisibleMessageBox = inject(openMessageBox) as () => void;

</script>

<style lang="less" scoped>
  .header-right {
    height: 36px;
  }

  .last-login-wrapper {
    display: flex;
    align-items: center;
  }

  .last-login-label {
    color: var(--color-text-2);
    margin-right: 8px;
    font-size: 12px;
  }
</style>
