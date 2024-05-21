<template>
  <a-modal v-model:visible="visible"
           modal-class="modal-form-small"
           title-align="start"
           title="cron 生成器"
           :top="32"
           :width="780"
           :align-center="false"
           :draggable="true"
           :mask-closable="false"
           :unmount-on-close="true"
           :body-style="{ padding: '4px 16px 8px 16px' }">
    <!-- cron 输入框 -->
    <cron-generator-input v-model="cronExpression" />
    <!-- 页脚-->
    <template #footer>
      <a-button size="small" @click="handlerClose">关闭</a-button>
      <a-button size="small"
                type="primary"
                @click="handlerOk">
        确定
      </a-button>
    </template>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'cronGeneratorModal'
  };
</script>

<script lang="ts" setup>
  import { ref } from 'vue';
  import useVisible from '@/hooks/visible';
  import CronGeneratorInput from '../generator-input/index.vue';

  const { visible, setVisible } = useVisible();

  const cronExpression = ref('');

  const emits = defineEmits(['ok']);

  // 打开新增
  const open = (cron: string = '') => {
    cronExpression.value = cron;
    setVisible(true);
  };

  defineExpose({ open });

  // 确定
  const handlerOk = () => {
    setVisible(false);
    console.log(cronExpression.value);
    console.log('ok', cronExpression.value);
    emits('ok', cronExpression.value);
  };

  // 关闭
  const handlerClose = () => {
    setVisible(false);
  };

</script>

<style lang="less" scoped>
</style>
