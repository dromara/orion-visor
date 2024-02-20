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
        <span class="click-icon-wrapper header-action-icon mr8"
              @click="backParentPath">
          <icon-left />
        </span>
      </a-tooltip>
      <!-- 当前路径 -->
      <div class="sftp-path-container"
           @click="setPathEditable(true)">
        <!-- 路径输入框 -->
        <div v-if="pathEditable">
          <a-input v-model="pathInput"
                   ref="pathInputRef"
                   size="mini"
                   placeholder="文件夹路径"
                   allow-clear
                   @press-enter="doChangePath" />
        </div>
        <!-- 路径视图 -->
        <a-breadcrumb class="sftp-path-wrapper" v-else>
          <!-- 根目录 -->
          <a-breadcrumb-item class="sftp-path-unit"
                             @click.stop="loadFileList('/')">
            <icon-storage />
          </a-breadcrumb-item>
          <!-- 子目录 -->
          <a-breadcrumb-item class="sftp-path-unit"
                             v-for="path in analysisPaths"
                             @click.stop="loadFileList(path.path)">
            {{ path.name }}
          </a-breadcrumb-item>
        </a-breadcrumb>
      </div>
    </div>
    <!-- 路径编辑模式-右侧操作 -->
    <a-space v-if="pathEditable" class="sftp-table-header-right">
      <!-- 进入 -->
      <a-tooltip position="top"
                 :mini="true"
                 :overlay-inverse="true"
                 :auto-fix-position="false"
                 content-class="terminal-tooltip-content"
                 arrow-class="terminal-tooltip-content"
                 content="进入">
        <span class="click-icon-wrapper header-action-icon"
              @click="doChangePath">
          <icon-right />
        </span>
      </a-tooltip>
      <!-- 取消 -->
      <a-tooltip position="top"
                 :mini="true"
                 :overlay-inverse="true"
                 :auto-fix-position="false"
                 content-class="terminal-tooltip-content"
                 arrow-class="terminal-tooltip-content"
                 content="取消">
        <span class="click-icon-wrapper header-action-icon"
              @click="setPathEditable(false)">
          <icon-stop />
        </span>
      </a-tooltip>
    </a-space>
    <!-- 非路径编辑模式-右侧操作 -->
    <a-space v-else class="sftp-table-header-right">
      <!-- 刷新 -->
      <a-tooltip position="top"
                 :mini="true"
                 :overlay-inverse="true"
                 :auto-fix-position="false"
                 content-class="terminal-tooltip-content"
                 arrow-class="terminal-tooltip-content"
                 content="刷新">
        <span class="click-icon-wrapper header-action-icon"
              @click="loadFileList()">
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
                 :content="showHiddenFile ? '不显示隐藏文件' : '显示隐藏文件'">
        <span class="click-icon-wrapper header-action-icon"
              @click="toggleShowHiddenFile">
          <icon-eye-invisible v-if="showHiddenFile" />
          <icon-eye v-else />
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
        <span class="click-icon-wrapper header-action-icon"
              @click="createFile">
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
        <span class="click-icon-wrapper header-action-icon"
              @click="createDir">
          <icon-folder-add />
        </span>
      </a-tooltip>
      <!-- 删除选中文件 -->
      <a-tooltip position="top"
                 :mini="true"
                 :overlay-inverse="true"
                 :auto-fix-position="false"
                 content-class="terminal-tooltip-content"
                 arrow-class="terminal-tooltip-content"
                 content="删除选中文件">
        <span class="click-icon-wrapper header-action-icon"
              @click="deleteSelectFiles">
          <icon-delete />
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
        <span class="click-icon-wrapper header-action-icon"
              @click="openSftpUploadModal">
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
        <span class="click-icon-wrapper header-action-icon"
              @click="downloadFile">
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
  import type { PathAnalysis } from '@/utils/file';
  import type { ISftpSession } from '../../types/terminal.type';
  import { inject, nextTick, ref, watch } from 'vue';
  import { getParentPath, getPathAnalysis } from '@/utils/file';
  import { openSftpCreateModalKey, openSftpUploadModalKey } from '../../types/terminal.const';

  const props = defineProps<{
    currentPath: string;
    session: ISftpSession | undefined,
    selectedFiles: Array<string>
  }>();

  const emits = defineEmits(['loadFile']);

  const showHiddenFile = ref(false);
  const analysisPaths = ref<Array<PathAnalysis>>([]);
  const pathEditable = ref(false);
  const pathInput = ref('');
  const pathInputRef = ref();

  const openSftpCreateModal = inject(openSftpCreateModalKey) as (sessionId: string, path: string, isTouch: boolean) => void;

  const openSftpUploadModal = inject(openSftpUploadModalKey) as () => void;

  // 监听路径变化
  watch(() => props.currentPath, (path) => {
    if (path) {
      analysisPaths.value = getPathAnalysis(path);
    } else {
      analysisPaths.value = [];
    }
  });

  // 返回上级目录
  const backParentPath = () => {
    loadFileList(getParentPath(props.currentPath));
  };

  // 设置命令编辑模式
  const setPathEditable = (editable: boolean) => {
    pathEditable.value = editable;
    pathInput.value = editable ? props.currentPath : '';
    // 自动聚焦
    nextTick(() => {
      if (editable) {
        pathInputRef.value?.focus();
      }
    });
  };

  // 执行修改目录
  const doChangePath = () => {
    loadFileList(pathInput.value);
    setPathEditable(false);
  };

  // 加载文件列表
  const loadFileList = (path: string = props.currentPath) => {
    emits('loadFile', path);
  };

  // 设置是否显示隐藏文件
  const toggleShowHiddenFile = () => {
    showHiddenFile.value = !showHiddenFile.value;
    // 设置显示状态并且刷新
    if (props.session) {
      props.session.setShowHiddenFile(showHiddenFile.value);
      loadFileList();
    }
  };

  // 创建文件
  const createFile = () => {
    openSftpCreateModal(props.session?.sessionId as string, props.currentPath + '/', true);
  };

  // 创建文件夹
  const createDir = () => {
    openSftpCreateModal(props.session?.sessionId as string, props.currentPath + '/', false);
  };

  // 删除选中文件
  const deleteSelectFiles = () => {
    if (props.selectedFiles?.length) {
      props.session?.remove(props.selectedFiles);
    }
  };

  // 下载文件
  const downloadFile = () => {
    // TODO
    console.log(props.selectedFiles);
  };

</script>

<style lang="less" scoped>
  @action-num: 7;
  @action-gap: 8px;
  @action-size: 26px;
  @actions-width: @action-num * (@action-size + @action-gap);

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
      width: calc(100% - @actions-width);
    }

    &-right {
      width: @actions-width;
      justify-content: flex-end;
    }
  }

  .sftp-path-container {
    background: var(--color-fill-2);
    width: 100%;
    border-radius: 2px;
    overflow: hidden;

    :deep(.sftp-path-unit) {
      cursor: pointer;
      font-size: 12px;

      &:hover {
        color: rgb(var(--arcoblue-6));
      }
    }

    .sftp-path-wrapper {
      padding: 0 6px 1px 6px;
    }
  }

  .header-action-icon {
    font-size: 16px;
    padding: 4px;
    width: @action-size;
    height: @action-size;
  }
</style>
