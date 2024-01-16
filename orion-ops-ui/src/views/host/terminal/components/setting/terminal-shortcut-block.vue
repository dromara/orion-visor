<template>
  <div class="terminal-setting-block">
    <!-- 顶部 -->
    <div class="terminal-setting-subtitle-wrapper">
      <h3 class="terminal-setting-subtitle">
        终端快捷键
      </h3>
    </div>
    <!-- 加载中 -->
    <a-skeleton v-if="loading"
                class="skeleton-wrapper"
                :animation="true">
      <a-skeleton-line :rows="8" />
    </a-skeleton>
    <!-- 内容区域 -->
    <div v-else class="terminal-setting-body terminal-shortcut-container">
      <!-- 提示 -->
      <a-alert class="mb16">选择后会立刻保存, 刷新页面后生效 (设置时需要避免浏览器内置快捷键)</a-alert>
      <div class="shortcut-row">
        <a-space>
          <a-tag checkable :default-checked="true">ctrl</a-tag>
          <a-tag checkable :default-checked="true">shift</a-tag>
          <a-tag checkable :default-checked="true">alt</a-tag>
          <icon-plus />
          <a-tag :default-checked="true">alt</a-tag>
        </a-space>
      </div>
      <div class="shortcut-row">2</div>
      <div class="shortcut-row">3</div>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'TerminalShortcutBlock'
  };
</script>

<script lang="ts" setup>
  import { useTerminalStore } from '@/store';
  import { TerminalPreferenceItem } from '@/store/modules/terminal';
  import { onMounted, ref } from 'vue';
  import { getPreference } from '@/api/user/preference';
  import useLoading from '@/hooks/loading';

  const { updateTerminalPreference } = useTerminalStore();
  const { loading, setLoading } = useLoading();


  // 加载用户快捷键
  onMounted(async () => {
    try {
      const { data } = await getPreference<Record<string, any>>('TERMINAL', [TerminalPreferenceItem.SHORTCUT_SETTING]);
    } catch (e) {
    }
  });

</script>

<style lang="less" scoped>
  @terminal-width: 458px;
  @terminal-height: 138px;

  .terminal-shortcut-container {
    flex-direction: column;

    .shortcut-row {
      display: flex;
      align-items: center;
      margin-bottom: 8px;
      background: var(--color-fill-2);
      height: 48px;
      padding: 8px;
      border-radius: 4px;
      transition: .3s;

      &:hover {
        background: var(--color-fill-3);
      }
    }

    :deep(.arco-tag) {
      background: #FFFFFF;
      width: 44px;
      display: flex;
      justify-content: center;
      height: 26px;
      font-size: 14px;
      user-select: none;
    }

    :deep(.arco-tag-checked) {
      color: #FFFFFF;
      background: rgb(var(--arcoblue-5));
    }
  }

</style>
