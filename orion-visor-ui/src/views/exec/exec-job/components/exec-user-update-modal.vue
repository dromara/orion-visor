<template>
  <a-modal v-model:visible="visible"
           modal-class="modal-form-large"
           title-align="start"
           title="修改执行用户"
           :align-center="false"
           :draggable="true"
           :mask-closable="false"
           :unmount-on-close="true"
           :ok-button-props="{ disabled: loading }"
           :cancel-button-props="{ disabled: loading }"
           :on-before-ok="handlerOk"
           @close="handleClose">
    <a-spin class="full" :loading="loading">
      <a-form :model="formModel"
              label-align="right"
              :auto-label-width="true">
        <!-- 执行用户 -->
        <a-form-item field="userId" label="执行用户">
          <user-selector v-model="formModel.userId"
                         placeholder="请选择执行用户"
                         allow-clear />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'execUserUpdateModal'
  };
</script>

<script lang="ts" setup>
  import type { ExecJobUpdateExecUserRequest, ExecJobQueryResponse } from '@/api/exec/exec-job';
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import { Message } from '@arco-design/web-vue';
  import { useCacheStore, useDictStore } from '@/store';
  import { updateExecJobExecUser } from '@/api/exec/exec-job';
  import UserSelector from '@/components/user/user/selector/index.vue';

  const { toOptions } = useDictStore();
  const { loadUsers } = useCacheStore();
  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const formModel = ref<ExecJobUpdateExecUserRequest>({});
  const job = ref<ExecJobQueryResponse>();

  // 打开
  const open = (record: ExecJobQueryResponse) => {
    formModel.value = { id: record.id, userId: record.execUserId };
    job.value = record;
    setVisible(true);
  };

  defineExpose({ open });

  // 确定
  const handlerOk = async () => {
    const execUserId = formModel.value.userId;
    if (!execUserId) {
      Message.error('请选择执行用户');
      return false;
    }
    setLoading(true);
    try {
      // 更新
      await updateExecJobExecUser(formModel.value);
      if (job.value) {
        job.value.execUserId = execUserId;
        job.value.execUsername = (await loadUsers()).find(s => s.id === execUserId)?.username || '';
      }
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

</style>
