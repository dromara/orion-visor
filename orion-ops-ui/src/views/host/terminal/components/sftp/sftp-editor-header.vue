<template>
  <!-- 表头 -->
  <div class="sftp-editor-header">
    <!-- 左侧操作 -->
    <div class="sftp-editor-header-left">
      <div class="sftp-path-container">
        <!-- 当前路径 -->
        <a-tooltip position="top"
                   :mini="true"
                   :overlay-inverse="true"
                   :auto-fix-position="false"
                   content-class="terminal-tooltip-content"
                   arrow-class="terminal-tooltip-content"
                   :content="path">
          <span>{{ name }}</span>
        </a-tooltip>
      </div>
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
  import type { ISftpSession } from '../../types/terminal.type';

  const props = defineProps<{
    name: string;
    path: string;
    session: ISftpSession | undefined,
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

  .sftp-path-container {
    width: 100%;
    height: @action-size;
    background: var(--color-fill-2);
    border-radius: 2px;
    overflow: hidden;
    display: flex;
    align-items: center;
    padding: 0 8px;

    span {
      font-size: 14px;
      line-height: 1.2;
      text-overflow: ellipsis;
      overflow: hidden;
    }
  }

  .header-action-icon {
    font-size: 16px;
    padding: 4px;
    width: @action-size;
    height: @action-size;
  }
</style>
