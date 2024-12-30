<template>
  <div />
</template>

<script lang="ts">
  export default {
    name: 'redirect',
  };
</script>

<script lang="ts" setup>
  import { useRoute, useRouter } from 'vue-router';
  import qs from 'query-string';

  const route = useRoute();
  const router = useRouter();

  // 解析路径
  const { url, query } = qs.parseUrl(route.params.path as string);

  // 重定向
  router.replace({
    path: url,
    query: query,
  });

</script>

<style lang="less" scoped>
</style>
