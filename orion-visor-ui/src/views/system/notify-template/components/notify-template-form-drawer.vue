<template>
  <a-drawer v-model:visible="visible"
            :title="title"
            :width="540"
            :mask-closable="false"
            :unmount-on-close="true"
            :ok-button-props="{ disabled: loading }"
            :cancel-button-props="{ disabled: loading }"
            :on-before-ok="handlerOk"
            @cancel="handleClose">
    <a-spin class="full drawer-form-small" :loading="loading">
      <a-form :model="formModel"
              ref="formRef"
              label-align="right"
              :auto-label-width="true"
              :rules="formRules">
        <!-- 通知名称 -->
        <a-form-item field="name" label="通知名称">
          <a-input v-model="formModel.name"
                   placeholder="请输入通知名称"
                   allow-clear />
        </a-form-item>
        <!-- 业务类型 -->
        <a-form-item field="bizType"
                     label="业务类型"
                     :disabled="formHandle === 'add'">
          <a-select v-model="formModel.bizType"
                    :options="toOptions(BizTypeKey)"
                    placeholder="请选择业务类型"
                    allow-clear />
        </a-form-item>
        <!-- 渠道类型 -->
        <a-form-item field="channelType" label="渠道类型">
          <a-select v-model="formModel.channelType"
                    :options="toOptions(ChannelTypeKey)"
                    placeholder="请选择渠道类型"
                    allow-clear />
        </a-form-item>
        <!-- 消息分类 -->
        <a-form-item v-if="formModel.channelType === ChannelType.WEBSITE"
                     field="messageClassify"
                     label="消息分类">
          <a-select v-model="formModel.messageClassify"
                    :options="toOptions(messageClassifyKey)"
                    placeholder="请选择消息分类"
                    allow-clear />
        </a-form-item>
        <!-- 消息类型 -->
        <a-form-item v-if="formModel.channelType === ChannelType.WEBSITE"
                     field="messageType"
                     label="消息类型">
          <a-select v-model="formModel.messageType"
                    :options="toOptions(messageTypeKey)"
                    placeholder="请选择消息分类"
                    allow-clear />
        </a-form-item>
        <!-- webhook -->
        <a-form-item v-if="formModel.channelType && getDictValue(ChannelTypeKey, formModel.channelType, 'notifyType') === NotifyType.WEBHOOK"
                     field="webhook"
                     label="webhook">
          <a-textarea v-model="formModel.webhook"
                      :auto-size="{ minRows: 3, maxRows: 3}"
                      placeholder="请输入 webhook 地址"
                      allow-clear />
        </a-form-item>
        <!-- 签名密钥 -->
        <a-form-item v-if="formModel.channelType === ChannelType.DING || formModel.channelType === ChannelType.FEI_SHU"
                     field="secret"
                     label="签名密钥">
          <a-input v-model="formModel.secret"
                   placeholder="请输入签名密钥"
                   allow-clear />
        </a-form-item>
        <!-- 消息标题 -->
        <a-form-item v-if="formModel.channelType === ChannelType.WEBSITE
                            || formModel.channelType === ChannelType.DING"
                     field="title"
                     label="消息标题"
                     extra="标题同样支持变量">
          <a-input v-model="formModel.title"
                   placeholder="请输入消息标题"
                   allow-clear />
        </a-form-item>
        <!-- 消息模板 -->
        <a-form-item field="messageTemplate"
                     label="消息模板"
                     :extra="formModel.channelType ? getDictValue(ChannelTypeKey, formModel.channelType, 'templateTips') : ''">
          <a-textarea v-model="formModel.messageTemplate"
                      :auto-size="{ minRows: 10, maxRows: 10 }"
                      placeholder="请输入消息模板"
                      allow-clear />
        </a-form-item>
        <!-- 通知描述 -->
        <a-form-item field="description" label="通知描述">
          <a-textarea v-model="formModel.description"
                      :auto-size="{ minRows: 3, maxRows: 3}"
                      placeholder="请输入通知描述"
                      allow-clear />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-drawer>
</template>

<script lang="ts">
  export default {
    name: 'notifyTemplateFormDrawer'
  };
</script>

<script lang="ts" setup>
  import type { FormHandle } from '@/types/form';
  import type { NotifyTemplateUpdateRequest } from '@/api/system/notify-template';
  import type { NotifyTemplateConfig } from '../types/const';
  import { messageClassifyKey, messageTypeKey } from '../types/const';
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import formRules from '../types/form.rules';
  import { assignOmitRecord } from '@/utils';
  import { BizTypeKey, ChannelTypeKey, BizType, ChannelType } from '../types/const';
  import { createNotifyTemplate, updateNotifyTemplate } from '@/api/system/notify-template';
  import { NotifyType } from '../types/const';
  import { Message } from '@arco-design/web-vue';
  import { useDictStore } from '@/store';

  const emits = defineEmits(['added', 'updated']);

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();
  const { toOptions, getDictValue } = useDictStore();

  const title = ref<string>();
  const formHandle = ref<FormHandle>('add');
  const formRef = ref<any>();
  const formModel = ref<Partial<NotifyTemplateUpdateRequest & NotifyTemplateConfig>>({});

  const defaultForm = (): Partial<NotifyTemplateUpdateRequest & NotifyTemplateConfig> => {
    return {
      id: undefined,
      name: undefined,
      bizType: BizType.ALARM,
      channelType: ChannelType.WEBSITE,
      channelConfig: undefined,
      messageTemplate: undefined,
      messageClassify: 'ALARM',
      messageType: 'ALARM',
    };
  };

  // 打开新增
  const openAdd = () => {
    title.value = '添加通知模板';
    formHandle.value = 'add';
    renderForm({ ...defaultForm() });
    setVisible(true);
  };

  // 打开复制
  const openCopy = (record: any) => {
    title.value = '添加通知模板';
    formHandle.value = 'add';
    renderForm({ ...defaultForm(), ...record, id: undefined });
    setVisible(true);
  };

  // 打开修改
  const openUpdate = (record: any) => {
    title.value = '修改通知模板';
    formHandle.value = 'update';
    renderForm({ ...defaultForm(), ...record });
    setVisible(true);
  };

  // 渲染表单
  const renderForm = (record: any) => {
    const model = assignOmitRecord(record, 'channelConfig');
    const channelConfig = record.channelConfig ? JSON.parse(record.channelConfig) : {};
    formModel.value = { ...model, ...channelConfig };
  };

  defineExpose({ openAdd, openCopy, openUpdate });

  // 确定
  const handlerOk = async () => {
    setLoading(true);
    try {
      // 验证参数
      const error = await formRef.value.validate();
      if (error) {
        return false;
      }
      // 修改渠道配置
      const channelConfig = {
        webhook: formModel.value.webhook,
        secret: formModel.value.secret,
        title: formModel.value.title,
        messageClassify: formModel.value.messageClassify,
        messageType: formModel.value.messageType,
      };
      if (formHandle.value === 'add') {
        // 新增
        await createNotifyTemplate({ ...formModel.value, channelConfig: JSON.stringify(channelConfig) });
        Message.success('创建成功');
        emits('added');
      } else {
        // 修改
        await updateNotifyTemplate({ ...formModel.value, channelConfig: JSON.stringify(channelConfig) });
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

</style>
