<template>
  <a-modal v-model:visible="visible"
           body-class="modal-form"
           title-align="start"
           title="移动文件"
           :align-center="false"
           :mask-closable="false"
           :unmount-on-close="true"
           :on-before-ok="handlerOk">
    <a-form :model="formModel"
            ref="formRef"
            label-align="right"
            :style="{ width: '460px' }"
            :label-col-props="{ span: 6 }"
            :wrapper-col-props="{ span: 18 }">
      <!-- 原始路径 -->
      <a-form-item field="path"
                   disabled
                   label="原始路径">
        <a-input v-model="formModel.path"
                 placeholder="原始路径" />
      </a-form-item>
      <!-- 目标路径 -->
      <a-form-item field="target"
                   label="目标路径"
                   extra="目标路径可以是绝对路径/相对路径/名称 (可以包含 ./ ../)"
                   :rules="[{ required: true, message: '请输入目标路径' }]">
        <a-input ref="targetRef"
                 v-model="formModel.target"
                 placeholder="请输入目标路径" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'sftpMoveModal'
  };
</script>

<script lang="ts" setup>
  import useVisible from '@/hooks/visible';
  import { nextTick, ref } from 'vue';
  import { useTerminalStore } from '@/store';
  import SftpSession from '../../handler/sftp-session';

  const { visible, setVisible } = useVisible();
  const { sessionManager } = useTerminalStore();

  const sessionId = ref();
  const targetRef = ref();
  const formRef = ref();
  const formModel = ref({
    path: '',
    target: ''
  });

  // 打开新增
  const open = (session: string, path: string) => {
    sessionId.value = session;
    formModel.value.path = path;
    formModel.value.target = path;
    setVisible(true);
    // 自动聚焦
    nextTick(() => {
      targetRef.value?.focus();
    });
  };

  defineExpose({ open });

  // 确定
  const handlerOk = async () => {
    try {
      // 验证参数
      const error = await formRef.value.validate();
      if (error) {
        return false;
      }
      // 获取会话
      const session = sessionManager.getSession(sessionId.value);
      if (session instanceof SftpSession) {
        session.move(formModel.value.path, formModel.value.target);
      }
    } catch (e) {
      return false;
    }
  };

</script>

<style lang="less" scoped>

</style>
