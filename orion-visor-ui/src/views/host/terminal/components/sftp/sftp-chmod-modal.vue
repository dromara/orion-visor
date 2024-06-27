<template>
  <a-modal v-model:visible="visible"
           modal-class="modal-form-large"
           title-align="start"
           title="修改权限"
           :align-center="false"
           :mask-closable="false"
           :unmount-on-close="true"
           :on-before-ok="handlerOk">
    <a-form :model="formModel"
            ref="formRef"
            label-align="right"
            :auto-label-width="true">
      <!-- 文件路径 -->
      <a-form-item field="path"
                   disabled
                   label="文件路径">
        <a-input v-model="formModel.path"
                 placeholder="原始路径" />
      </a-form-item>
      <!-- 文件权限 -->
      <a-form-item field="mod"
                   label="文件权限"
                   :rules="[{ required: true, message: '请输入文件权限' }]">
        <div class="mod-wrapper">
          <!-- 权限输入框 -->
          <a-input-number ref="modRef"
                          class="mod-input"
                          v-model="formModel.mod"
                          placeholder="请输入文件权限"
                          hide-button
                          @input="updatePreview" />
          <!-- 权限预览 -->
          <span class="mod-preview">{{ formModel.permission }}</span>
        </div>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'sftpChmodModal'
  };
</script>

<script lang="ts" setup>
  import type { ISftpSession } from '../../types/terminal.type';
  import useVisible from '@/hooks/visible';
  import { nextTick, ref } from 'vue';
  import { useTerminalStore } from '@/store';
  import { permission10toString } from '@/utils/file';
  import { PanelSessionType } from '../../types/terminal.const';

  const { visible, setVisible } = useVisible();
  const { sessionManager } = useTerminalStore();

  const sessionId = ref();
  const modRef = ref();
  const formRef = ref();
  const formModel = ref({
    path: '',
    mod: 0,
    permission: ''
  });

  // 修改预览
  const updatePreview = (v: number | undefined) => {
    formModel.value.permission = permission10toString(v as number);
  };

  // 打开新增
  const open = (session: string, path: string, permission: number) => {
    sessionId.value = session;
    formModel.value.path = path;
    formModel.value.mod = permission;
    formModel.value.permission = permission10toString(permission);
    setVisible(true);
    // 自动聚焦
    nextTick(() => {
      modRef.value?.focus();
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
        session.chmod(formModel.value.path, formModel.value.mod);
      }
    } catch (e) {
      return false;
    }
  };

</script>

<style lang="less" scoped>
  .mod-wrapper {
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;

    .mod-input {
      width: 65%;
    }

    .mod-preview {
      width: 30%;
      display: flex;
      justify-content: flex-end;
    }
  }
</style>
