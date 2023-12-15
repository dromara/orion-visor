<template>
  <div class="list-view-container">
    <!-- 主机列表 -->
    <host-list :hosts="hosts" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'hostListView'
  };
</script>

<script lang="ts" setup>
  import type { AuthorizedHostQueryResponse } from '@/api/asset/asset-authorized-data';
  import HostList from './host-list.vue';

  const props = defineProps<{
    hosts: AuthorizedHostQueryResponse
  }>();

</script>

<style lang="less" scoped>
  .list-view-container {
    max-height: 100%;
    width: 100%;
    overflow: auto;
    position: relative;
  }
</style>
