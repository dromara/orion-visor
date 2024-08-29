<template>
  <a-modal v-model:visible="visible"
           modal-class="modal-form-large"
           title-align="start"
           title="清空计划任务日志"
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
              label-align="right"
              :auto-label-width="true">
        <!-- 执行时间 -->
        <a-form-item field="startTimeRange" label="执行时间">
          <a-range-picker v-model="formModel.startTimeRange"
                          :time-picker-props="{ defaultValue: ['00:00:00', '23:59:59'] }"
                          show-time
                          format="YYYY-MM-DD HH:mm:ss" />
        </a-form-item>
        <!-- 计划任务 -->
        <a-form-item field="sourceId" label="计划任务">
          <exec-job-selector v-model:model-value="formModel.sourceId"
                             v-model:name="formModel.description"
                             allow-create
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
        <!-- 数量限制 -->
        <a-form-item field="limit" label="数量限制">
          <a-input-number v-model="formModel.limit"
                          :min="1"
                          :max="maxLimit"
                          :placeholder="`请输入数量限制 最大: ${maxLimit}`"
                          hide-button
                          allow-clear />
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
  import { getExecJobLogCount, clearExecJobLog } from '@/api/job/exec-job-log';
  import { Message, Modal } from '@arco-design/web-vue';
  import { useDictStore } from '@/store';
  import { maxClearLimit } from '../types/const';
  import ExecJobSelector from '@/components/exec/job/selector/index.vue';

  const emits = defineEmits(['clear']);

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();
  const { toOptions } = useDictStore();

  const maxLimit = ref<number>(0);
  const formModel = ref<ExecLogQueryRequest>({});

  const defaultForm = (): ExecLogQueryRequest => {
    return {
      id: undefined,
      sourceId: undefined,
      description: undefined,
      command: undefined,
      status: undefined,
      startTimeRange: undefined,
      limit: maxLimit.value,
    };
  };

  // 打开
  const open = (record: any) => {
    maxLimit.value = maxClearLimit;
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
    if (!formModel.value.limit) {
      Message.error('请输入数量限制');
      return false;
    }
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
          // 调用清空
          const { data } = await clearExecJobLog(formModel.value);
          Message.success(`已成功清空 ${data} 条数据`);
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
