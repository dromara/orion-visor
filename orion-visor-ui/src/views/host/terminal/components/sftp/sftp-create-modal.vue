<template>
  <a-modal v-model:visible="visible"
           modal-class="modal-form-large"
           title-align="start"
           :title="touch ? '创建文件' : '创建文件夹'"
           :align-center="false"
           :mask-closable="false"
           :unmount-on-close="true"
           :on-before-ok="handlerOk">
    <a-form :model="formModel"
            ref="formRef"
            label-align="right"
            :auto-label-width="true">
      <!-- 路径 -->
      <a-form-item field="path"
                   :label="`${touch ? '文件' : '文件夹'}路径`"
                   :rules="[{ required: true, message: `请输入${touch ? '文件' : '文件夹'}路径` }]">
        <a-input ref="pathRef"
                 v-model="formModel.path"
                 :placeholder="`请输入${touch ? '文件' : '文件夹'}路径`" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'sftpCreateModal'
  };
</script>

<script lang="ts" setup>
  import type { ISftpSession } from '../../types/terminal.type';
  import useVisible from '@/hooks/visible';
  import { nextTick, ref } from 'vue';
  import { useTerminalStore } from '@/store';
  import { PanelSessionType } from '../../types/terminal.const';

  const { visible, setVisible } = useVisible();
  const { sessionManager } = useTerminalStore();

  const sessionId = ref();
  const touch = ref(false);
  const pathRef = ref();
  const formRef = ref();
  const formModel = ref({
    path: ''
  });

  // 打开新增
  const open = (session: string, path: string, isTouch: boolean) => {
    sessionId.value = session;
    formModel.value.path = path;
    touch.value = isTouch;
    setVisible(true);
    // 自动聚焦
    nextTick(() => {
      pathRef.value?.focus();
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
      const session = sessionManager.getSession<ISftpSession>(sessionId.value);
      if (session.type === PanelSessionType.SFTP.type) {
        if (touch.value) {
          // 创建文件
          session.touch(formModel.value.path);
        } else {
          // 创建文件夹
          session.mkdir(formModel.value.path);
        }
      }
    } catch (e) {
      return false;
    }
  };

</script>

<style lang="less" scoped>

</style>
