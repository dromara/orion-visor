<template>
  <!-- 主机列表 -->
  <host-list class="list-view-container"
             :hostList="hostList"
             :empty-value="emptyValue" />
</template>

<script lang="ts">
  export default {
    name: 'hostListView'
  };
</script>

<script lang="ts" setup>
  import type { HostQueryResponse } from '@/api/asset/host';
  import HostList from './host-list.vue';

  const props = defineProps<{
    hostList: Array<HostQueryResponse>,
    emptyValue: string
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
