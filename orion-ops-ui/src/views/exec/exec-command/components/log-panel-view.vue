<template>
  <div class="container">
    <div v-show="current === host.id"
         v-for="host in command.hosts"
         :key="host.id"
         class="log-view">
      <!-- 面板头部 -->
      <div class="log-header">
        <!-- 左侧信息 -->
        <a-space class="log-header-left" :size="12">
          <!-- 状态 -->
          <a-tag :color="getDictValue(execHostStatusKey, host.status, 'color')">
            {{ getDictValue(execHostStatusKey, host.status) }}
          </a-tag>
          <!-- exitStatus -->
          <a-tag v-if="host.exitStatus || host.exitStatus === 0"
                 :color="host.exitStatus === 0 ? 'arcoblue' : 'orangered'"
                 title="exit status">
            <template #icon>
              <icon-check v-if="host.exitStatus === 0" />
              <icon-exclamation v-else />
            </template>
            <span class="tag-value">{{ host.exitStatus }}</span>
          </a-tag>
          <!-- 持续时间 -->
          <a-tag color="arcoblue" title="持续时间">
            <template #icon>
              <icon-loading v-if="host.status === execHostStatus.WAITING || host.status === execHostStatus.RUNNING" />
              <icon-clock-circle v-else />
            </template>
            <span class="tag-value">{{ formatDuration(host.startTime, host.finishTime) || '0s' }}</span>
          </a-tag>
        </a-space>
        <!-- 右侧操作 -->
        <a-space class="log-header-right" :size="12">
          <!-- 搜索 -->
          <span class="log-action click-icon-wrapper"
                title="搜索"
                @click="() => appender?.addFontSize(1)">
            <icon-find-replace />
          </span>
          <!-- 增大字号 -->
          <span class="log-action click-icon-wrapper"
                title="增大字号"
                @click="() => appender?.addFontSize(1)">
            <icon-zoom-in />
          </span>
          <!-- 减小字号 -->
          <span class="log-action click-icon-wrapper"
                title="减小字号"
                @click="() => appender?.addFontSize(-1)">
            <icon-zoom-out />
          </span>
          <!-- 去顶部 -->
          <span class="log-action click-icon-wrapper"
                title="去顶部"
                @click="() => appender?.toTop()">
            <icon-up />
          </span>
          <!-- 去底部 -->
          <span class="log-action click-icon-wrapper"
                title="去底部"
                @click="() => appender?.toBottom()">
            <icon-down />
          </span>
          <!-- 全选 -->
          <span class="log-action click-icon-wrapper"
                title="全选"
                @click="() => appender?.selectAll()">
            <icon-expand />
          </span>
          <!-- 复制 -->
          <span class="log-action click-icon-wrapper"
                title="复制"
                @click="() => appender?.copy()">
            <icon-copy />
          </span>
          <!-- 复制全部 -->
          <span class="log-action click-icon-wrapper"
                title="复制全部"
                @click="() => appender?.copyAll()">
            <icon-brush />
          </span>
          <!-- 清空 -->
          <span class="log-action click-icon-wrapper"
                title="清空"
                @click="() => appender?.clear()">
            <icon-delete />
          </span>
          <!-- 下载 -->
          <span class="log-action click-icon-wrapper"
                title="下载"
                @click="downloadLogFile(host.id)">
            <icon-download />
          </span>
          <!-- 设置固定 -->
          <a-switch type="round"
                    checked-text="固定"
                    unchecked-text="跟随"
                    @change="(e: any) => appender?.setFixed(e as boolean)" />
        </a-space>
      </div>
      <!-- 右键菜单 -->
      <a-dropdown :popup-max-height="false"
                  trigger="contextMenu"
                  position="bl"
                  alignPoint>
        <!-- 日志面板 -->
        <div class="log-wrapper">
          <!-- terminal -->
          <div class="log-appender"
               :ref="e => addRef(host.id, e as HTMLElement) as unknown as VNodeRef" />
          <!-- 搜索框 -->
          <xterm-search-modal ref="searchModal"
                              class="search-modal"
                              @find="searchWords"
                              @close="searchClose" />
        </div>
        <!-- 右键菜单 -->
        <template #content>
          <!-- 去顶部 -->
          <a-doption style="line-height: 30px; padding: 0 8px;"
                     @click="() => appender?.toTop()">
            <template #icon>
              <icon-up />
            </template>
            <span>去顶部</span>
          </a-doption>
          <!-- 去底部 -->
          <a-doption style="line-height: 30px; padding: 0 8px;"
                     @click="() => appender?.toBottom()">
            <template #icon>
              <icon-down />
            </template>
            <span>去底部</span>
          </a-doption>
          <!-- 全选 -->
          <a-doption style="line-height: 30px; padding: 0 8px;"
                     @click="() => appender?.selectAll()">
            <template #icon>
              <icon-expand />
            </template>
            <span>全选</span>
          </a-doption>
          <!-- 复制 -->
          <a-doption style="line-height: 30px; padding: 0 8px;"
                     @click="() => appender?.copy()">
            <template #icon>
              <icon-copy />
            </template>
            <span>复制</span>
          </a-doption>
          <!-- 清空 -->
          <a-doption style="line-height: 30px; padding: 0 8px;"
                     @click="() => appender?.clear()">
            <template #icon>
              <icon-delete />
            </template>
            <span>清空</span>
          </a-doption>
        </template>
      </a-dropdown>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'logPanelView'
  };
</script>

<script lang="ts" setup>
  import type { VNodeRef } from 'vue';
  import type { ExecCommandResponse } from '@/api/exec/exec';
  import type { LogDomRef, ILogAppender } from '@/components/xtrem/log-appender/appender.const';
  import { nextTick, ref, watch } from 'vue';
  import { downloadExecLogFile } from '@/api/exec/exec';
  import { downloadFile } from '@/utils/file';
  import { formatDuration } from '@/utils';
  import { execHostStatus, execHostStatusKey } from '@/views/exec/exec-log/types/const';
  import { useDictStore } from '@/store';
  import LogAppender from '@/components/xtrem/log-appender/log-appender';
  import XtermSearchModal from '@/components/xtrem/search-modal/index.vue';
  import 'xterm/css/xterm.css';

  const props = defineProps<{
    current: number;
    command: ExecCommandResponse;
  }>();

  const { getDictValue } = useDictStore();

  const logRefs = ref<Array<LogDomRef>>([]);
  const appender = ref<ILogAppender>();
  const searchModal = ref();

  // 切换标签
  watch(() => props.current, (val) => {
    nextTick(() => {
      appender.value?.setCurrent(val);
    });
  });

  // 打开
  const open = () => {
    nextTick(async () => {
      appender.value = new LogAppender({ execId: props.command.id });
      // 初始化
      await appender.value.init(logRefs.value);
    });
  };

  // 关闭客户端
  const closeClient = () => {
    appender.value?.closeClient();
  };

  // 关闭全部
  const closeAll = () => {
    appender.value?.close();
  };

  defineExpose({ open, closeClient, closeAll });

  // 添加 ref
  const addRef = (id: number, el: HTMLElement) => {
    nextTick(() => {
      logRefs.value.push({ id, el });
    });
  };

  // 搜索关键字
  const searchWords = (word: string, next: boolean, options: any) => {
    appender.value?.find(word, next, options);
  };

  // 关闭搜索框
  const searchClose = () => {
    appender.value?.focus();
  };

  // 下载文件
  const downloadLogFile = async (id: number) => {
    const data = await downloadExecLogFile(id);
    downloadFile(data);
  };

</script>

<style lang="less" scoped>
  @header-height: 40px;

  .log-view {
    width: 100%;
    height: 100%;
    position: relative;
  }

  .log-header {
    width: 100%;
    height: @header-height;
    padding: 8px;
    border-radius: 4px 4px 0 0;
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: var(--color-bg-1);
    color: var(--color-text-1);

    :deep(.arco-tag-icon) {
      font-size: 14px;
    }

    .tag-value {
      font-weight: 600;
    }

    .log-action {
      width: 24px;
      height: 24px;
      font-size: 16px;
    }
  }

  .log-wrapper {
    width: 100%;
    height: calc(100% - @header-height);
    position: relative;
    background: #202020;
    padding: 4px 0 0 4px;

    .log-appender {
      width: 100%;
      height: 100%;

      ::-webkit-scrollbar-track {
        display: none;
      }
    }
  }

  .search-modal {
    --bg-focus: rgba(255, 255, 255, .85);
    --bg: rgba(255, 255, 255, .95);
    --color-text: #0E0E0E;
    --color-text-focus: #0F0F0F;
    --bg-icon-hover: rgba(12, 12, 12, .04);
    --bg-icon-hover-focus: rgba(12, 12, 12, .08);
    --bg-icon-selected: rgba(12, 12, 12, .06);
    --bg-icon-selected-focus: rgba(12, 12, 12, .10);
  }

</style>
