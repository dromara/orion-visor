<template>
  <a-modal v-model:visible="visible"
           title-align="start"
           title="授权主机"
           :top="80"
           :width="900"
           :align-center="false"
           :draggable="true"
           :mask-closable="false"
           :unmount-on-close="true"
           :ok-button-props="{ disabled: loading }"
           :cancel-button-props="{ disabled: loading }"
           :on-before-ok="handlerOk"
           @close="handleClose">
    <!-- 加载中 -->
    <a-skeleton v-if="loading"
                style="padding: 0 12px"
                :animation="true">
      <a-skeleton-line :rows="6"
                       :line-height="48"
                       :line-spacing="12" />
    </a-skeleton>
    <!-- 主机列表 -->
    <div v-else class="layout-container">
      11
    </div>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'authorizedHostModal'
  };
</script>

<script lang="ts" setup>
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import { Message } from '@arco-design/web-vue';

  const { loading, setLoading } = useLoading();
  const { visible, setVisible } = useVisible(true);

  // 打开
  const open = () => {
    setVisible(true);
  };

  defineExpose({ open });

  // 确定
  const handlerOk = async () => {
    setLoading(true);
    try {
      Message.success('修改成功');
      // 清空
      handlerClear();
    } catch (e) {
      return false;
    } finally {
      setLoading(false);
    }
  };

  // 关闭
  const handleClose = () => {
    handlerClear();
  };

  // 清空
  const handlerClear = () => {
    setLoading(false);
  };

</script>

<style lang="less" scoped>
  .layout-container {
    width: 100%;
    max-height: calc(100vh - 300px);
  }
</style>
