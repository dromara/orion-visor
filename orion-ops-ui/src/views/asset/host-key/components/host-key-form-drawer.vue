<template>
  <a-drawer :visible="visible"
            :title="title"
            :width="470"
            :mask-closable="false"
            :unmount-on-close="true"
            :on-before-ok="handlerOk"
            @cancel="handleClose">
    <a-spin :loading="loading">
      <a-alert style="margin-bottom: 18px;">请使用 ssh-keygen -m PEM -t rsa 生成秘钥</a-alert>
      <a-form :model="formModel"
              ref="formRef"
              label-align="right"
              :style="{ width: '420px' }"
              :label-col-props="{ span: 4 }"
              :wrapper-col-props="{ span: 20 }"
              :rules="formRules">
        <!-- 名称 -->
        <a-form-item field="name" label="名称">
          <a-input v-model="formModel.name" placeholder="请输入名称" />
        </a-form-item>
        <!-- 公钥文本 -->
        <a-form-item field="publicKey" label="公钥">
          <a-upload :auto-upload="false"
                    :show-file-list="false"
                    draggable
                    @change="selectPublicFile"
                    @click.prevent="() => {}">
            <template #upload-button>
              <a-textarea v-model="formModel.publicKey"
                          placeholder="请输入公钥文本或将文件拖拽到此处"
                          :auto-size="{ minRows: 7, maxRows: 7}" />
            </template>
          </a-upload>
        </a-form-item>
        <!-- 私钥文本 -->
        <a-form-item field="privateKey" label="私钥">
          <a-upload :auto-upload="false"
                    :show-file-list="false"
                    draggable
                    @change="selectPrivateFile"
                    @click.prevent="() => {}">
            <template #upload-button>
              <a-textarea v-model="formModel.privateKey"
                          placeholder="请输入私钥文本或将文件拖拽到此处"
                          :auto-size="{ minRows: 8, maxRows: 8}" />
            </template>
          </a-upload>
        </a-form-item>
        <!-- 密码 -->
        <a-form-item field="password" label="密码">
          <a-input-password v-model="formModel.password" placeholder="请输入私钥密码" />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-drawer>
</template>

<script lang="ts">
  export default {
    name: 'asset-host-key-form-drawer'
  };
</script>

<script lang="ts" setup>
  import { reactive, ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import formRules from '../types/form.rules';
  import { createHostKey, updateHostKey, getHostKey } from '@/api/asset/host-key';
  import { FileItem, Message } from '@arco-design/web-vue';
  import {} from '../types/enum.types';
  import {} from '../types/const';
  import { readFileText } from '@/utils/file';

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const title = ref<string>();
  const isAddHandle = ref<boolean>(true);

  const defaultForm = () => {
    return {
      id: undefined,
      name: undefined,
      publicKey: undefined,
      privateKey: undefined,
      password: undefined,
    };
  };

  const formRef = ref<any>();
  const formModel = reactive<Record<string, any>>(defaultForm());

  const emits = defineEmits(['added', 'updated']);

  // 打开新增
  const openAdd = () => {
    title.value = '添加主机秘钥';
    isAddHandle.value = true;
    renderForm({ ...defaultForm() });
    setVisible(true);
  };

  // 打开修改
  const openUpdate = async (record: any) => {
    title.value = '修改主机秘钥';
    isAddHandle.value = false;
    setVisible(true);
    setLoading(true);
    try {
      const { data } = await getHostKey(record.id);
      renderForm({ ...data });
    } catch (e) {
      setVisible(false);
    } finally {
      setLoading(false);
    }
  };

  // 渲染表单
  const renderForm = (record: any) => {
    Object.keys(formModel).forEach(k => {
      formModel[k] = record[k];
    });
  };

  defineExpose({ openAdd, openUpdate });

  // 选择公钥文件
  const selectPublicFile = async (fileList: FileItem[]) => {
    formModel.publicKey = await readFileText(fileList[0].file as File);
    formRef.value.clearValidate('publicKey');
  };

  // 选择私钥文件
  const selectPrivateFile = async (fileList: FileItem[]) => {
    formModel.privateKey = await readFileText(fileList[0].file as File);
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
        await createHostKey(formModel as any);
        Message.success('创建成功');
        emits('added');
      } else {
        // 修改
        await updateHostKey(formModel as any);
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
    setVisible(false);
  };

</script>

<style lang="less" scoped>

</style>
