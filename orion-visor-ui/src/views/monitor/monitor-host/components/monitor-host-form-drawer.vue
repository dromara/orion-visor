<template>
  <a-drawer v-model:visible="visible"
            title="修改监控主机配置"
            :width="470"
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
              :label-width="120"
              :rules="formRules">
        <!-- 主机名称 -->
        <a-form-item field="name" label="主机名称">
          <a-input v-model="hostRecord.name" readonly />
        </a-form-item>
        <!-- 主机地址 -->
        <a-form-item field="address" label="主机地址">
          <a-input v-model="hostRecord.address" readonly />
        </a-form-item>
        <!-- 负责人 -->
        <a-form-item field="ownerUserId" label="负责人">
          <user-selector v-model="formModel.ownerUserId"
                         placeholder="请选择负责人"
                         allow-clear />
        </a-form-item>
        <!-- 告警策略 -->
        <a-form-item field="policyId" label="告警策略">
          <alarm-policy-selector v-model="formModel.policyId"
                                 type="HOST"
                                 placeholder="请选择告警策略"
                                 allow-clear />
        </a-form-item>
        <!-- 告警开关 -->
        <a-form-item field="alarmSwitch"
                     label="告警开关"
                     style="margin-bottom: 0;"
                     hide-asterisk>
          <a-switch v-model="formModel.alarmSwitch"
                    type="round"
                    :checked-value="AlarmSwitch.ON"
                    :unchecked-value="AlarmSwitch.OFF"
                    checked-text="开"
                    unchecked-text="关"
                    allow-clear />
        </a-form-item>
        <!-- 展示设置 -->
        <a-divider direction="horizontal">
          展示设置
        </a-divider>
        <!-- CPU -->
        <!--        <a-form-item field="cpuName" label="CPU">-->
        <!--          <a-select v-model="formModel.cpuName"-->
        <!--                    :options="hostRecord.meta?.cpus || []"-->
        <!--                    placeholder="请选择展示的CPU" />-->
        <!--        </a-form-item>-->
        <!-- 磁盘 -->
        <a-form-item field="diskName" label="磁盘">
          <a-select v-model="formModel.diskName"
                    :options="hostRecord.meta?.disks || []"
                    placeholder="请选择展示的磁盘名称" />
        </a-form-item>
        <!-- 网卡 -->
        <a-form-item field="networkName" label="网卡">
          <a-select v-model="formModel.networkName"
                    :options="hostRecord.meta?.nets || []"
                    placeholder="请选择展示的网卡名称" />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-drawer>
</template>

<script lang="ts">
  export default {
    name: 'monitorHostFormDrawer'
  };
</script>

<script lang="ts" setup>
  import type { MonitorHostQueryResponse, MonitorHostUpdateRequest } from '@/api/monitor/monitor-host';
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import formRules from '../types/form.rules';
  import { AlarmSwitch, } from '../types/const';
  import { updateMonitorHost } from '@/api/monitor/monitor-host';
  import { Message } from '@arco-design/web-vue';
  import UserSelector from '@/components/user/user/selector/index.vue';
  import AlarmPolicySelector from '@/components/monitor/alarm-policy/selector/index.vue';

  const emits = defineEmits(['updated']);

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const formRef = ref<any>();
  const formModel = ref<MonitorHostUpdateRequest>({});
  const hostRecord = ref<MonitorHostQueryResponse>({} as MonitorHostQueryResponse);

  // 打开修改
  const openUpdate = (record: MonitorHostQueryResponse) => {
    hostRecord.value = record;
    formModel.value = {
      id: record.id,
      policyId: record.policyId,
      alarmSwitch: record.alarmSwitch,
      ownerUserId: record.ownerUserId,
      cpuName: record.config?.cpuName,
      diskName: record.config?.diskName,
      networkName: record.config?.networkName,
    };
    setVisible(true);
  };

  defineExpose({ openUpdate });

  // 确定
  const handlerOk = async () => {
    setLoading(true);
    try {
      // 验证参数
      const error = await formRef.value.validate();
      if (error) {
        return false;
      }
      // 修改
      await updateMonitorHost(formModel.value);
      Message.success('修改成功');
      emits('updated');
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
