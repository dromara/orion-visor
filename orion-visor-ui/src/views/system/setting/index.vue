<template>
  <div class="tabs-container">
    <a-tabs v-model:active-key="activeKey"
            type="rounded"
            size="medium"
            position="left"
            :lazy-load="true">
      <!-- SFTP -->
      <a-tab-pane key="sftp" title="SFTP">
        <sftp-setting />
      </a-tab-pane>
      <!-- 关于 -->
      <a-tab-pane key="about" title="关于">
        <about-setting />
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'systemSetting',
  };
</script>

<script lang="ts" setup>
  import { onBeforeMount, ref } from 'vue';
  import { useRoute } from 'vue-router';
  import SftpSetting from './components/sftp-setting.vue';
  import AboutSetting from './components/about-setting.vue';

  const route = useRoute();

  const activeKey = ref('sftp');

  // 跳转到指定页
  onBeforeMount(() => {
    const key = route.query.key;
    if (key) {
      activeKey.value = key as string;
    }
  });

</script>

<style lang="less" scoped>
  .tabs-container {
    background: var(--color-bg-2);
    margin: 16px;
    padding: 16px;
    display: flex;
    flex-direction: column;
    border-radius: 4px;
  }

  :deep(.arco-tabs-nav-tab-list) {
    width: 88px;
  }

  :deep(.arco-tabs-pane) {
    border-left: 1px var(--color-neutral-3) solid;
  }

  :deep(.arco-tabs-tab) {
    user-select: none;
    white-space: nowrap;
    display: flex;
    justify-content: center;
  }

</style>
