<template>
  <a-card class="card-list-item create-card"
          :style="{ height: cardHeight }"
          :body-style="{ height: '100%' }"
          :bordered="false"
          :hoverable="true">
    <div class="create-card-body">
      <icon-plus class="create-card-body-icon" />
      <span class="create-card-body-text">{{ createCardDescription }}</span>
    </div>
  </a-card>
</template>

<script lang="ts">
  export default {
    name: 'create-card'
  };
</script>

<script lang="ts" setup>
  import type { CardProps } from '../types/props';

  const props = defineProps<CardProps>();
</script>

<style lang="less" scoped>
  .create-card-body {
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    flex-direction: column;
    justify-content: center;
    cursor: pointer;

    &-icon {
      font-size: 18px;
      margin-bottom: 4px;
    }

    &-text {
      user-select: none;
    }
  }
</style>
