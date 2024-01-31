<template>
  <div class="host-space-setting-container">
    <div class="host-space-setting-wrapper loading-skeleton">
      <!-- 加载骨架 -->
      <a-skeleton class="full"
                  :animation="true">
        <a-skeleton-line :rows="6"
                         :line-height="42"
                         :line-spacing="24" />
      </a-skeleton>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'loadingSkeleton'
  };
</script>

<script lang="ts" setup>

</script>

<style lang="less" scoped>
  .loading-skeleton {
    margin: 32px auto;
  }
</style>
