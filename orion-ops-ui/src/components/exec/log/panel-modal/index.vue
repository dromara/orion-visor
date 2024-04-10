<template>
  <a-modal v-model:visible="visible"
           title-align="start"
           title="执行日志"
           width="94%"
           :top="44"
           :body-style="{ padding: '0', height: 'calc(100vh - 138px)', overflow: 'hidden' }"
           :align-center="false"
           :draggable="true"
           :mask-closable="false"
           :unmount-on-close="true"
           :footer="false"
           @close="handleClose">
    <a-spin v-if="visible"
            class="modal-body"
            :loading="loading">
      <div class="panel-wrapper">
        <!-- 日志面板 -->
        <exec-log-panel ref="log" :visible-back="false" />
      </div>
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
  import { getExecCommandLog } from '@/api/exec/exec-command-log';
  import ExecLogPanel from '../panel/index.vue';

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const log = ref();

  // 打开
  const open = async (id: number) => {
    setVisible(true);
    setLoading(true);
    try {
      // 获取执行日志
      const { data } = await getExecCommandLog(id);
      // 打开日志
      await nextTick(() => {
        setTimeout(() => {
          log.value.open(data);
        }, 50);
      });
    } catch (e) {
      setVisible(false);
    } finally {
      setLoading(false);
    }
  };

  defineExpose({ open });

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
    height: 100%;
    position: relative;
    padding: 0 12px 12px 12px;
  }

  .panel-wrapper {
    width: 100%;
    height: 100%;
    position: relative;
  }

  :deep(.exec-host-container) {
    padding: 0;

    .host-header {
      height: 38px;
      margin: 0;

      h3 {
        height: 100%;
        display: flex;
        align-items: center;
      }
    }

    .exec-host-items {
      width: 100%;
      height: calc(100% - 38px);
    }
  }

  :deep(.log-header) {
    padding: 0;
  }

</style>
