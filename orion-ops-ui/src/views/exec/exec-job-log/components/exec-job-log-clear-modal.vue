<template>
  <a-modal v-model:visible="visible"
           body-class="modal-form"
           title-align="start"
           title="清空批量执行日志"
           :align-center="false"
           :draggable="true"
           :mask-closable="false"
           :unmount-on-close="true"
           ok-text="清空"
           :ok-button-props="{ disabled: loading }"
           :cancel-button-props="{ disabled: loading }"
           :on-before-ok="handlerOk"
           @close="handleClose">
    <a-spin class="full" :loading="loading">
      <a-form :model="formModel"
              ref="formRef"
              label-align="right"
              :label-col-props="{ span: 5 }"
              :wrapper-col-props="{ span: 18 }">
        <!-- 执行时间 -->
        <a-form-item field="startTimeRange" label="执行时间">
          <a-range-picker v-model="formModel.startTimeRange"
                          :time-picker-props="{ defaultValue: ['00:00:00', '23:59:59'] }"
                          show-time
                          format="YYYY-MM-DD HH:mm:ss" />
        </a-form-item>
        <!-- 执行用户 -->
        <a-form-item field="userId" label="执行用户">
          <user-selector v-model="formModel.userId"
                         placeholder="请选择执行用户"
                         allow-clear />
        </a-form-item>
        <!-- 执行描述 -->
        <a-form-item field="description" label="执行描述">
          <a-input v-model="formModel.description"
                   placeholder="请输入执行描述"
                   allow-clear />
        </a-form-item>
        <!-- 执行命令 -->
        <a-form-item field="command" label="执行命令">
          <a-input v-model="formModel.command"
                   placeholder="请输入执行命令"
                   allow-clear />
        </a-form-item>
        <!-- 执行状态 -->
        <a-form-item field="status" label="执行状态">
          <a-select v-model="formModel.status"
                    :options="toOptions(execStatusKey)"
                    placeholder="请选择执行状态" />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'execJobLogClearModal'
  };
</script>

<script lang="ts" setup>
  import type { ExecLogQueryRequest } from '@/api/exec/exec-log';
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import { execStatusKey } from '@/components/exec/log/const';
  import { getExecJobLogCount, clearExecJobLog } from '@/api/exec/exec-job-log';
  import { Message, Modal } from '@arco-design/web-vue';
  import { useDictStore } from '@/store';
  import UserSelector from '@/components/user/user/selector/index.vue';

  const emits = defineEmits(['clear']);

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();
  const { toOptions } = useDictStore();

  const formRef = ref<any>();
  const formModel = ref<ExecLogQueryRequest>({});

  const defaultForm = (): ExecLogQueryRequest => {
    return {
      id: undefined,
      userId: undefined,
      description: undefined,
      command: undefined,
      status: undefined,
      startTimeRange: undefined
    };
  };

  // 打开
  const open = (record: any) => {
    renderForm({ ...defaultForm(), ...record });
    setVisible(true);
  };

  // 渲染表单
  const renderForm = (record: any) => {
    formModel.value = Object.assign({}, record);
  };

  defineExpose({ open });
  // 确定
  const handlerOk = async () => {
    setLoading(true);
    try {
      // 获取总数量
      const { data } = await getExecJobLogCount(formModel.value);
      if (data) {
        // 清空
        doClear(data);
      } else {
        // 无数据
        Message.warning('当前条件未查询到数据');
      }
    } catch (e) {
    } finally {
      setLoading(false);
    }
    return false;
  };

  // 执行删除
  const doClear = (count: number) => {
    Modal.confirm({
      title: '删除清空',
      content: `确定要删除 ${count} 条数据吗? 确定后将立即删除且无法恢复!`,
      onOk: async () => {
        setLoading(true);
        try {
          // 调用删除
          await clearExecJobLog(formModel.value);
          emits('clear');
          // 清空
          setVisible(false);
          handlerClear();
        } catch (e) {
        } finally {
          setLoading(false);
        }
      }
    });
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
