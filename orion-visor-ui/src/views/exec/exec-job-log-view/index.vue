<template>
  <div class="container">
    <div class="wrapper">
      <exec-log-panel ref="log"
                      type="JOB"
                      :visible-back="false" />
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'execJobLogView'
  };
</script>

<script lang="ts" setup>
  import { onMounted, ref, nextTick } from 'vue';
  import { useRoute } from 'vue-router';
  import { getExecJobLog } from '@/api/exec/exec-job-log';
  import ExecLogPanel from '@/components/exec/log/panel/index.vue';

  const route = useRoute();
  const log = ref();

  // 初始化
  const init = async (id: number) => {
    // 获取执行日志
    const { data } = await getExecJobLog(id);
    // 打开日志
    await nextTick(() => {
      setTimeout(() => {
        log.value.open(data);
      }, 50);
    });
  };

  onMounted(() => {
    const idParam = route.query.id;
    if (idParam) {
      init(Number.parseInt(idParam as string));
    }
  });

</script>

<style lang="less" scoped>
  .container {
    width: 100%;
    height: 100%;
    position: relative;
    padding: 16px;
    background: var(--color-fill-2);

    .wrapper {
      width: 100%;
      height: 100%;
      position: relative;
    }
  }
</style>
