<template>
  <div class="container">
    <div v-show="current === host.id"
         v-for="host in command.hosts"
         :key="host.id"
         class="log-view">
      <!-- 面板头部 -->
      <div class="log-header">
        <!-- 左侧信息 -->
        <div class="log-header-left">
          <a-space :size="12">
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
        </div>
        <!-- 右侧操作 -->
        <div class="log-header-right">TODO</div>
      </div>
      <!-- 日志面板 -->
      <div class="log-wrapper">
        <div class="log-appender"
             :ref="e => addRef(host.id, e) as unknown as VNodeRef" />
      </div>
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
  import { nextTick, ref } from 'vue';
  import { formatDuration } from '@/utils';
  import { execHostStatus, execHostStatusKey } from '@/views/exec/exec-log/types/const';
  import { useDictStore } from '@/store';
  import LogAppender from '@/components/xtrem/log-appender/log-appender';
  import 'xterm/css/xterm.css';

  const props = defineProps<{
    current: number;
    command: ExecCommandResponse;
  }>();

  const { getDictValue } = useDictStore();

  const logRefs = ref<Array<LogDomRef>>([]);
  const appender = ref<ILogAppender>();

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

</script>

<style lang="less" scoped>
  @header-height: 38px;
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

    &-right {

    }
  }

  .log-wrapper {
    width: 100%;
    height: calc(100% - @header-height);
    position: relative;
    background: #212529;
    padding: 4px 0 0 4px;

    .log-appender {
      width: 100%;
      height: 100%;

      ::-webkit-scrollbar-track {
        display: none;
      }
    }
  }
</style>
