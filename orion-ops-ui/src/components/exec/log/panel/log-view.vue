<template>
  <div class="container">
    <log-item class="log-item"
              v-show="current === host.id"
              v-for="host in hosts"
              :key="host.id"
              :ref="addRef as unknown as VNodeRef"
              :host="host"
              :appender="appender" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'logView'
  };
</script>

<script lang="ts" setup>
  import type { VNodeRef } from 'vue';
  import type { ExecCommandHostLogQueryResponse } from '@/api/exec/exec-command-log';
  import type { LogDomRef, ILogAppender } from './const';
  import { nextTick, ref, watch } from 'vue';
  import LogItem from './log-item.vue';

  const props = defineProps<{
    current: number;
    hosts: Array<ExecCommandHostLogQueryResponse>;
    appender: ILogAppender;
  }>();

  const logRefs = ref<Array<LogDomRef>>([]);

  // 切换标签
  watch(() => props.current, (val) => {
    nextTick(() => {
      setTimeout(() => {
        props.appender?.setCurrent(val);
      }, 50);
    });
  });

  // 打开
  const open = () => {
    nextTick(async () => {
      if (props.appender) {
        // 初始化
        await props.appender.init(logRefs.value);
      }
    });
  };

  defineExpose({ open });

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

</script>

<style lang="less" scoped>
  .log-item {
    width: 100%;
    height: 100%;
    position: relative;
  }

</style>
