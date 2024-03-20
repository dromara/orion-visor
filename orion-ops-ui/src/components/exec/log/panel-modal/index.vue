<template>
  <a-modal v-model:visible="visible"
           title-align="start"
           title="执行日志"
           width="94%"
           :top="80"
           :body-style="{ padding: '0' }"
           :align-center="false"
           :draggable="true"
           :mask-closable="false"
           :unmount-on-close="true"
           :footer="false"
           @close="handleClose">
    <a-spin class="modal-body" :loading="loading">
      <!-- 日志面板 -->
      <exec-log-panel ref="log" :visible-back="false" />
    </a-spin>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'execLogPanelModal'
  };
</script>

<script lang="ts" setup>
  import useVisible from '@/hooks/visible';
  import useLoading from '@/hooks/loading';
  import { nextTick, ref } from 'vue';
  import { getExecLog } from '@/api/exec/exec-log';
  import ExecLogPanel from '../panel/index.vue';

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const log = ref();

  // TODO 测试卸载

  // 打开
  const open = async (id: number) => {
    setVisible(true);
    setLoading(true);
    try {
      // 获取执行日志
      const { data } = await getExecLog(id);
      // 打开日志
      nextTick(() => {
        log.value.open(data);
      });
    } catch (e) {
    } finally {
      setVisible(false);
      setLoading(false);
    }
  };

  // 关闭回调
  const handleClose = () => {
    handleClear();
  };

  // 清空
  const handleClear = () => {
    setLoading(false);
    setVisible(false);
  };

</script>

<style lang="less" scoped>
  .modal-body {
    width: 100%;
    height: calc(100vh - 140px);
  }
</style>
