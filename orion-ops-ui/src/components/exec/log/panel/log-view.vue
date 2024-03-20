<template>
  <div class="container">
    <template v-if="appender">
      <log-item class="log-item"
                v-show="current === host.id"
                v-for="host in command.hosts"
                :key="host.id"
                :ref="addRef as unknown as VNodeRef"
                :host="host"
                :appender="appender as ILogAppender" />
    </template>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'logView'
  };
</script>

<script lang="ts" setup>
  import type { VNodeRef } from 'vue';
  import type { ExecCommandResponse } from '@/api/exec/exec';
  import type { LogDomRef, ILogAppender } from './const';
  import { nextTick, onBeforeMount, ref, watch } from 'vue';
  import LogAppender from './log-appender';
  import LogItem from './log-item.vue';

  const props = defineProps<{
    current: number;
    command: ExecCommandResponse;
  }>();

  const logRefs = ref<Array<LogDomRef>>([]);
  const appender = ref<ILogAppender>();

  // 切换标签
  watch(() => props.current, (val) => {
    nextTick(() => {
      setTimeout(() => {
        appender.value?.setCurrent(val);
      }, 50);
    });
  });

  // 打开
  const open = () => {
    nextTick(async () => {
      if (appender.value) {
        // 初始化
        await appender.value.init(logRefs.value);
      }
    });
  };

  // 关闭客户端
  const closeClient = () => {
    appender.value?.closeClient();
  };

  // 关闭全部
  const closeAll = () => {
    appender.value?.close();
    logRefs.value = [];
    appender.value = undefined;
  };

  defineExpose({ open, closeClient, closeAll });

  // 添加 ref
  const addRef = (ref: any) => {
    if (!ref) {
      return;
    }
    nextTick(() => {
      logRefs.value.push({
        id: ref.id,
        el: ref.appenderRef,
        openSearch: ref.openSearch
      });
    });
  };

  onBeforeMount(() => {
    appender.value = new LogAppender({ execId: props.command.id });
  });

</script>

<style lang="less" scoped>
  .log-item {
    width: 100%;
    height: 100%;
    position: relative;
  }

</style>
