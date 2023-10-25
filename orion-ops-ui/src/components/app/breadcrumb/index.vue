<template>
  <a-breadcrumb class="container-breadcrumb">
    <a-breadcrumb-item>
      <icon-apps />
    </a-breadcrumb-item>
    <a-breadcrumb-item v-for="item in items" :key="item">
      {{ item }}
    </a-breadcrumb-item>
  </a-breadcrumb>
</template>

<script lang="ts" setup>
  import type { PropType } from 'vue';
  import { useRoute } from 'vue-router';

  defineProps({
    items: {
      type: Array as PropType<string[]>,
      default() {
        return useRoute().matched
          .map(s => s.meta?.locale)
          .filter(Boolean) || [];
      },
    },
  });
</script>

<style lang="less" scoped>
  .container-breadcrumb {

    :deep(.arco-breadcrumb-item) {
      color: rgb(var(--gray-6));

      &:last-child {
        color: rgb(var(--gray-8));
      }
    }
  }
</style>
