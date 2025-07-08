<template>
  <a-descriptions size="large"
                  :label-style="{ display: 'flex', width: '48px' }"
                  :column="1">
    <!-- 主机id -->
    <a-descriptions-item label="主机id">
      {{ session.info.hostId }}
    </a-descriptions-item>
    <!-- 主机名称 -->
    <a-descriptions-item label="主机名称">
      {{ session.info.name }}
    </a-descriptions-item>
    <!-- 主机地址 -->
    <a-descriptions-item label="主机地址">
      <span class="text-copy" @click="copy(session.info.address, true)">
        {{ session.info.address }}
      </span>
    </a-descriptions-item>
    <!-- 主机端口 -->
    <a-descriptions-item label="主机端口">
      {{ session.info.port }}
    </a-descriptions-item>
    <!-- 用户名 -->
    <a-descriptions-item v-if="session.info.username" label="用户名">
      {{ session.info.username }}
    </a-descriptions-item>
  </a-descriptions>
</template>

<script lang="ts">
  export default {
    name: 'infoAction'
  };
</script>

<script lang="ts" setup>
  import type { IGuacdSession } from '@/views/terminal/interfaces';
  import { copy } from '@/hooks/copy';

  const props = defineProps<{
    session: IGuacdSession;
  }>();

</script>

<style lang="less" scoped>

</style>
