<template>
  <!-- 表头 -->
  <div class="sftp-editor-header">
    <!-- 左侧操作 -->
    <div class="sftp-editor-header-left">
      <a-tooltip position="top"
                 :mini="true"
                 :overlay-inverse="true"
                 :auto-fix-position="false"
                 content-class="terminal-tooltip-content"
                 arrow-class="terminal-tooltip-content"
                 content="点击复制">
        <a-tag class="sftp-path-container pointer"
               color="arcoblue"
               @click="copy(path, true)">
          <span>{{ name }}</span>
        </a-tag>
      </a-tooltip>
    </div>
    <!-- 右侧操作 -->
    <a-space class="sftp-editor-header-right">
      <!-- 保存 -->
      <a-tooltip position="top"
                 :mini="true"
                 :overlay-inverse="true"
                 :auto-fix-position="false"
                 content-class="terminal-tooltip-content"
                 arrow-class="terminal-tooltip-content"
                 content="保存">
        <span class="click-icon-wrapper header-action-icon"
              @click="emits('save')">
          <icon-save />
        </span>
      </a-tooltip>
      <!-- 关闭 -->
      <a-tooltip position="top"
                 :mini="true"
                 :overlay-inverse="true"
                 :auto-fix-position="false"
                 content-class="terminal-tooltip-content"
                 arrow-class="terminal-tooltip-content"
                 content="关闭">
        <span class="click-icon-wrapper header-action-icon"
              @click="emits('close')">
          <icon-close />
        </span>
      </a-tooltip>
    </a-space>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'sftpEditorHeader'
  };
</script>

<script lang="ts" setup>
  import { copy } from '@/hooks/copy';

  const props = defineProps<{
    name: string;
    path: string;
  }>();

  const emits = defineEmits(['save', 'close']);

</script>

<style lang="less" scoped>
  @action-num: 2;
  @action-gap: 8px;
  @action-size: 26px;
  @actions-width: @action-num * (@action-size + @action-gap);

  .sftp-editor-header {
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;

    &-left, &-right {
      display: flex;
      align-items: center;
      height: 100%;
    }

    &-left {
      width: calc(100% - @actions-width);
    }

    &-right {
      width: @actions-width;
      justify-content: flex-end;
    }
  }

  .header-action-icon {
    font-size: 16px;
    padding: 4px;
    width: @action-size;
    height: @action-size;
  }
</style>
