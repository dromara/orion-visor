<template>
  <a-modal v-model:visible="visible"
           title-align="start"
           :title="record.title"
           :top="80"
           :align-center="false"
           :unmount-on-close="true"
           ok-text="删除"
           :hide-cancel="true"
           :ok-button-props="{ status: 'danger', size: 'small' }"
           :body-style="{ padding: '20px' }"
           @ok="emits('delete', record)">
    <div class="content" v-html="record.contentHtml" />
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'messageBoxModal'
  };
</script>

<script lang="ts" setup>
  import type { MessageRecordResponse } from '@/api/system/message';
  import useVisible from '@/hooks/visible';
  import { ref } from 'vue';

  const emits = defineEmits(['delete']);

  const { visible, setVisible } = useVisible();

  const record = ref<MessageRecordResponse>({} as MessageRecordResponse);

  // 打开
  const open = (message: MessageRecordResponse) => {
    record.value = message;
    setVisible(true);
  };

  defineExpose({ open });

</script>

<style lang="less" scoped>
  .content {
    font-size: 16px;
    color: var(--color-text-2);
  }
</style>
