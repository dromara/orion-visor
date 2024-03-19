<template>
  <div class="container">
    <div v-show="current === host.id"
         v-for="host in command.hosts"
         :key="host.id"
         class="log-view">
      <!-- 面板头部 -->
      <div class="log-header">
        header
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
  import type { ExecCommandResponse, ExecCommandHostResponse } from '@/api/exec/exec';
  import type { LogDomRef, ILogAppender } from '@/components/view/log-appender/appender.const';
  import { nextTick, ref } from 'vue';
  import LogAppender from '@/components/view/log-appender/log-appender';
  import 'xterm/css/xterm.css';

  const props = defineProps<{
    current: number;
    command: ExecCommandResponse;
  }>();

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
    background: green;
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
