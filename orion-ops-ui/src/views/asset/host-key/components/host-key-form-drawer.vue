<template>
  <a-drawer v-model:visible="visible"
            :title="title"
            :width="470"
            :mask-closable="false"
            :unmount-on-close="true"
            :ok-button-props="{ disabled: loading || isViewHandler }"
            :cancel-button-props="{ disabled: loading }"
            :on-before-ok="handlerOk"
            @cancel="handleClose">
    <a-spin class="form-wrapper" :loading="loading">
      <a-alert class="keygen-alert">
        请使用 ssh-keygen -m PEM -t rsa 生成秘钥
      </a-alert>
      <a-form :model="formModel"
              ref="formRef"
              label-align="right"
              :style="{ width: '420px' }"
              :label-col-props="{ span: 4 }"
              :wrapper-col-props="{ span: 20 }"
              :rules="formRules">
        <!-- 名称 -->
        <a-form-item field="name" label="名称">
          <a-input v-model="formModel.name"
                   :disabled="isViewHandler"
                   placeholder="请输入名称" />
        </a-form-item>
        <!-- 公钥文本 -->
        <a-form-item field="publicKey" label="公钥">
          <a-upload :auto-upload="false"
                    :show-file-list="false"
                    :draggable="true"
                    :disabled="isViewHandler"
                    @change="selectPublicFile"
                    @click.prevent="() => {}">
            <template #upload-button>
              <a-textarea v-model="formModel.publicKey"
                          :disabled="isViewHandler"
                          placeholder="请输入公钥文本或将文件拖拽到此处"
                          :auto-size="{ minRows: 8, maxRows: 8}" />
            </template>
          </a-upload>
        </a-form-item>
        <!-- 私钥文本 -->
        <a-form-item field="privateKey" label="私钥">
          <a-upload :auto-upload="false"
                    :show-file-list="false"
                    :draggable="true"
                    :disabled="isViewHandler"
                    @change="selectPrivateFile"
                    @click.prevent="() => {}">
            <template #upload-button>
              <a-textarea v-model="formModel.privateKey"
                          :disabled="isViewHandler"
                          placeholder="请输入私钥文本或将文件拖拽到此处"
                          :auto-size="{ minRows: 8, maxRows: 8}" />
            </template>
          </a-upload>
        </a-form-item>
        <!-- 密码 -->
        <a-form-item v-if="!isViewHandler"
                     field="password"
                     label="密码"
                     style="justify-content: space-between;"
                     :rules="passwordRules">
          <a-input-password v-model="formModel.password"
                            :disabled="!isAddHandle && !formModel.useNewPassword"
                            :class="[isAddHandle ? 'password-input-full' : 'password-input']"
                            class="password-input"
                            placeholder="请输入私钥密码" />
          <a-switch v-model="formModel.useNewPassword"
                    v-if="!isAddHandle"
                    class="password-switch"
                    type="round"
                    checked-text="使用新密码"
                    unchecked-text="使用原密码" />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-drawer>
</template>

<script lang="ts">
  export default {
    name: 'hostKeyFormDrawer'
  };
</script>

<script lang="ts" setup>
  import type { HostKeyUpdateRequest } from '@/api/asset/host-key';
  import type { FieldRule, FileItem } from '@arco-design/web-vue';
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import formRules from '../types/form.rules';
  import { createHostKey, updateHostKey, getHostKey } from '@/api/asset/host-key';
  import { Message } from '@arco-design/web-vue';
  import { readFileText } from '@/utils/file';

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const title = ref<string>();
  const isAddHandle = ref<boolean>(true);
  const isViewHandler = ref<boolean>(false);

  const defaultForm = (): HostKeyUpdateRequest => {
    return {
      id: undefined,
      name: undefined,
      publicKey: undefined,
      privateKey: undefined,
      password: undefined,
      useNewPassword: false
    };
  };

  const formRef = ref();
  const formModel = ref<HostKeyUpdateRequest>({});

  const emits = defineEmits(['added', 'updated']);

  // 打开新增
  const openAdd = () => {
    title.value = '添加主机秘钥';
    isAddHandle.value = true;
    isViewHandler.value = false;
    renderForm({ ...defaultForm() });
    setVisible(true);
  };

  // 打开修改
  const openUpdate = async (record: any) => {
    title.value = '修改主机秘钥';
    isAddHandle.value = false;
    isViewHandler.value = false;
    await render(record.id);
  };

  // 打开查看
  const openView = async (record: any) => {
    title.value = '主机秘钥';
    isAddHandle.value = false;
    isViewHandler.value = true;
    await render(record.id);
  };

  // 渲染数据
  const render = async (id: number) => {
    setVisible(true);
    setLoading(true);
    try {
      const { data } = await getHostKey(id);
      renderForm({ ...data });
    } catch (e) {
      setVisible(false);
    } finally {
      setLoading(false);
    }
  };

  // 渲染表单
  const renderForm = (record: any) => {
    formModel.value = Object.assign({}, record);
  };

  defineExpose({ openAdd, openUpdate, openView });

  // 密码验证
  const passwordRules = [{
    validator: (value, cb) => {
      if (value && value.length > 512) {
        cb('密码长度不能大于512位');
        return;
      }
      if (formModel.value.useNewPassword && !value) {
        cb('请输入密码');
        return;
      }
    }
  }] as FieldRule[];

  // 选择公钥文件
  const selectPublicFile = async (fileList: FileItem[]) => {
    formModel.value.publicKey = await readFileText(fileList[0].file as File);
    formRef.value.clearValidate('publicKey');
  };

  // 选择私钥文件
  const selectPrivateFile = async (fileList: FileItem[]) => {
    formModel.value.privateKey = await readFileText(fileList[0].file as File);
    formRef.value.clearValidate('privateKey');
  };

  // 确定
  const handlerOk = async () => {
    setLoading(true);
    try {
      // 验证参数
      const error = await formRef.value.validate();
      if (error) {
        return false;
      }
      if (isAddHandle.value) {
        // 新增
        await createHostKey(formModel.value);
        Message.success('创建成功');
        emits('added');
      } else {
        // 修改
        await updateHostKey(formModel.value);
        Message.success('修改成功');
        emits('updated');
      }
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
  .form-wrapper {
    width: 100%;
    padding: 12px 12px 0 12px;
  }

  .keygen-alert {
    margin: 0 0 12px 16px;
    width: calc(100% - 42px);
  }

  .password-input {
    width: 240px;
  }

  .password-input-full {
    width: 100%;
  }

  .password-switch {
    margin-left: 16px;
  }
</style>
