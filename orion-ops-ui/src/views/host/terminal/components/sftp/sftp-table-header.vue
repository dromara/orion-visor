<template>
  <!-- 表头 -->
  <div class="sftp-table-header">
    <!-- 左侧操作 -->
    <div class="sftp-table-header-left">
      <!-- 返回上级 -->
      <a-tooltip position="top"
                 :mini="true"
                 :overlay-inverse="true"
                 :auto-fix-position="false"
                 content-class="terminal-tooltip-content"
                 arrow-class="terminal-tooltip-content"
                 content="返回上级">
        <span class="click-icon-wrapper header-action-icon mr4">
          <icon-left />
        </span>
      </a-tooltip>
      <!-- 当前路径 -->
      <div class="sftp-path-wrapper">
        <a-breadcrumb>
          <!-- 分隔符 -->
          <template #separator>
            <icon-right />
          </template>
          <a-breadcrumb-item class="sftp-path-unit">/</a-breadcrumb-item>
          <a-breadcrumb-item class="sftp-path-unit">root</a-breadcrumb-item>
          <a-breadcrumb-item class="sftp-path-unit">orion</a-breadcrumb-item>
          <a-breadcrumb-item class="sftp-path-unit">space</a-breadcrumb-item>
          <a-breadcrumb-item class="sftp-path-unit">logs</a-breadcrumb-item>
        </a-breadcrumb>
      </div>
    </div>
    <!-- 右侧操作 -->
    <a-space class="sftp-table-header-right">
      <!-- 刷新 -->
      <a-tooltip position="top"
                 :mini="true"
                 :overlay-inverse="true"
                 :auto-fix-position="false"
                 content-class="terminal-tooltip-content"
                 arrow-class="terminal-tooltip-content"
                 content="刷新">
        <span class="click-icon-wrapper header-action-icon">
          <icon-refresh />
        </span>
      </a-tooltip>
      <!-- 显示隐藏文件 -->
      <a-tooltip position="top"
                 :mini="true"
                 :overlay-inverse="true"
                 :auto-fix-position="false"
                 content-class="terminal-tooltip-content"
                 arrow-class="terminal-tooltip-content"
                 content="显示隐藏文件">
        <span class="click-icon-wrapper header-action-icon">
          <icon-eye />
        </span>
      </a-tooltip>
      <!-- 创建文件 -->
      <a-tooltip position="top"
                 :mini="true"
                 :overlay-inverse="true"
                 :auto-fix-position="false"
                 content-class="terminal-tooltip-content"
                 arrow-class="terminal-tooltip-content"
                 content="创建文件">
        <span class="click-icon-wrapper header-action-icon">
          <icon-drive-file />
        </span>
      </a-tooltip>
      <!-- 创建文件夹 -->
      <a-tooltip position="top"
                 :mini="true"
                 :overlay-inverse="true"
                 :auto-fix-position="false"
                 content-class="terminal-tooltip-content"
                 arrow-class="terminal-tooltip-content"
                 content="创建文件夹">
        <span class="click-icon-wrapper header-action-icon">
          <icon-folder-add />
        </span>
      </a-tooltip>
      <!-- 删除 -->
      <a-tooltip position="top"
                 :mini="true"
                 :overlay-inverse="true"
                 :auto-fix-position="false"
                 content-class="terminal-tooltip-content"
                 arrow-class="terminal-tooltip-content"
                 content="删除">
        <span class="click-icon-wrapper header-action-icon">
          <icon-delete />
        </span>
      </a-tooltip>
      <!-- 复制 -->
      <a-tooltip position="top"
                 :mini="true"
                 :overlay-inverse="true"
                 :auto-fix-position="false"
                 content-class="terminal-tooltip-content"
                 arrow-class="terminal-tooltip-content"
                 content="复制">
        <span class="click-icon-wrapper header-action-icon">
          <icon-copy />
        </span>
      </a-tooltip>
      <!-- 移动 -->
      <a-tooltip position="top"
                 :mini="true"
                 :overlay-inverse="true"
                 :auto-fix-position="false"
                 content-class="terminal-tooltip-content"
                 arrow-class="terminal-tooltip-content"
                 content="移动">
        <span class="click-icon-wrapper header-action-icon">
          <icon-paste />
        </span>
      </a-tooltip>
      <!-- 上传 -->
      <a-tooltip position="top"
                 :mini="true"
                 :overlay-inverse="true"
                 :auto-fix-position="false"
                 content-class="terminal-tooltip-content"
                 arrow-class="terminal-tooltip-content"
                 content="上传">
        <span class="click-icon-wrapper header-action-icon">
          <icon-upload />
        </span>
      </a-tooltip>
      <!-- 下载 -->
      <a-tooltip position="top"
                 :mini="true"
                 :overlay-inverse="true"
                 :auto-fix-position="false"
                 content-class="terminal-tooltip-content"
                 arrow-class="terminal-tooltip-content"
                 content="下载">
        <span class="click-icon-wrapper header-action-icon">
          <icon-download />
        </span>
      </a-tooltip>
    </a-space>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'sftpTableHeader'
  };
</script>

<script lang="ts" setup>

</script>

<style lang="less" scoped>

  .sftp-table-header {
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
      width: calc(100% - 302px);
    }

    &-right {
      width: 302px;
      justify-content: flex-end;
    }
  }

  .sftp-path-wrapper {
    background: var(--color-fill-2);
    width: 100%;
    border-radius: 2px;
    padding: 1px 6px;
    overflow: hidden;

    :deep(.sftp-path-unit) {
      cursor: pointer;

      &:hover {
        color: rgb(var(--arcoblue-6));
      }
    }
  }

  .header-action-icon {
    font-size: 16px;
    padding: 4px;
  }
</style>
