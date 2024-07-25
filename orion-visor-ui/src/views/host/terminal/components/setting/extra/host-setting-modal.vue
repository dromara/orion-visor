<template>
  <a-modal v-model:visible="visible"
           title-align="start"
           :title="title"
           :top="180"
           :width="600"
           :align-center="false"
           :draggable="true"
           :mask-closable="false"
           :unmount-on-close="true"
           ok-text="保存当前页"
           cancel-text="关闭"
           :ok-button-props="{ disabled: loading }"
           :cancel-button-props="{ disabled: loading }"
           :on-before-ok="handlerOk"
           @close="handleClose">
    <a-spin class="full" :loading="loading">
      <a-tabs v-model:active-key="activeItem"
              position="left"
              type="rounded"
              :lazy-load="true">
        <!-- SSH 配置 -->
        <a-tab-pane :key="ExtraSettingItems.SSH" title="SSH 配置">
          <ssh-setting-form ref="sshForm"
                            :host-id="hostId as number"
                            :item="ExtraSettingItems.SSH" />
        </a-tab-pane>
        <!-- 标签配置 -->
        <a-tab-pane :key="ExtraSettingItems.LABEL" title="标签配置">
          <label-setting-form ref="labelForm"
                              :host-id="hostId as number"
                              :item="ExtraSettingItems.LABEL" />
        </a-tab-pane>
      </a-tabs>
    </a-spin>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'hostSettingModal'
  };
</script>

<script lang="ts" setup>
  import type { HostQueryResponse } from '@/api/asset/host';
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import { ExtraSettingItems } from '../../../types/const';
  import { updateHostExtra } from '@/api/asset/host-extra';
  import { Message } from '@arco-design/web-vue';
  import SshSettingForm from './ssh-setting-form.vue';
  import LabelSettingForm from './label-setting-form.vue';

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const activeItem = ref<string>(ExtraSettingItems.SSH);
  const title = ref<string>();
  const hostId = ref<number>();
  const sshForm = ref();
  const labelForm = ref();

  // 打开配置
  const open = (record: HostQueryResponse) => {
    hostId.value = record.id;
    title.value = record.alias || `${record.name} (${record.code})`;
    setVisible(true);
  };

  defineExpose({ open });

  // 确定
  const handlerOk = async () => {
    setLoading(true);
    try {
      let value;
      if (activeItem.value === ExtraSettingItems.SSH) {
        // SSH 配置
        value = await sshForm.value.getValue();
      } else if (activeItem.value === ExtraSettingItems.LABEL) {
        // 标签配置
        value = await labelForm.value.getValue();
      }
      if (!value) {
        return false;
      }
      // 保存
      await updateHostExtra({
        hostId: hostId.value,
        item: activeItem.value,
        extra: value as string
      });
      Message.success('保存成功');
    } catch (e) {
    } finally {
      setLoading(false);
    }
    return false;
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
  :deep(.arco-tabs-pane) {
    border-left: 1px var(--color-neutral-3) solid;
    padding-left: 16px;
  }
</style>
