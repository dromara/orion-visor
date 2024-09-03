<template>
  <a-modal v-model:visible="visible"
           modal-class="modal-form-large"
           title-align="start"
           title="清空操作日志"
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
        <!-- 操作用户 -->
        <a-form-item field="userId" label="操作用户">
          <user-selector v-model="formModel.userId"
                         placeholder="请选择操作用户"
                         allow-clear />
        </a-form-item>
        <!-- 操作模块 -->
        <a-form-item field="module" label="操作模块">
          <a-select v-model="formModel.module"
                    :options="toOptions(operatorLogModuleKey)"
                    :allow-search="true"
                    :filter-option="labelFilter"
                    placeholder="请选择操作模块"
                    allow-clear />
        </a-form-item>
        <!-- 操作类型 -->
        <a-form-item field="type" label="操作类型">
          <a-select v-model="formModel.type"
                    :options="typeOptions"
                    :allow-search="true"
                    :filter-option="labelFilter"
                    placeholder="请选择操作类型"
                    allow-clear />
        </a-form-item>
        <!-- 风险等级 -->
        <a-form-item field="riskLevel" label="风险等级">
          <a-select v-model="formModel.riskLevel"
                    :options="toOptions(operatorRiskLevelKey)"
                    placeholder="请选择风险等级"
                    allow-clear />
        </a-form-item>
        <!-- 执行结果 -->
        <a-form-item field="result" label="执行结果">
          <a-select v-model="formModel.result"
                    :options="toOptions(operatorLogResultKey)"
                    placeholder="请选择执行结果"
                    allow-clear />
        </a-form-item>
        <!-- 数量限制 -->
        <a-form-item field="limit" label="数量限制">
          <a-input-number v-model="formModel.limit"
                          :min="1"
                          :max="maxClearLimit"
                          :placeholder="`请输入数量限制 最大: ${maxClearLimit}`"
                          hide-button
                          allow-clear />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'operatorLogClearModal'
  };
</script>

<script lang="ts" setup>
  import type { SelectOptionData } from '@arco-design/web-vue/es/select/interface';
  import type { OperatorLogQueryRequest } from '@/api/user/operator-log';
  import { ref, watch } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import { getOperatorLogCount, clearOperatorLog } from '@/api/user/operator-log';
  import { Message, Modal } from '@arco-design/web-vue';
  import { useDictStore } from '@/store';
  import { operatorLogModuleKey, operatorLogResultKey, operatorLogTypeKey, operatorRiskLevelKey, maxClearLimit } from '../types/const';
  import { labelFilter } from '@/types/form';
  import UserSelector from '@/components/user/user/selector/index.vue';

  const { $state: dictState, toOptions } = useDictStore();
  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const defaultForm = (): OperatorLogQueryRequest => {
    return {
      module: undefined,
      type: undefined,
      riskLevel: undefined,
      result: undefined,
      startTimeRange: undefined,
      limit: maxClearLimit,
    };
  };

  const typeOptions = ref<SelectOptionData[]>(toOptions(operatorLogTypeKey));
  const formModel = ref<OperatorLogQueryRequest>({});

  const emits = defineEmits(['clear']);

  // 打开
  const open = (record: OperatorLogQueryRequest) => {
    renderForm({ ...defaultForm(), ...record });
    setVisible(true);
  };

  // 渲染表单
  const renderForm = (record: any) => {
    formModel.value = Object.assign({}, record);
  };

  defineExpose({ open });

  // 监听类型变化
  watch(() => formModel.value.module, (module: string | undefined) => {
    if (!module) {
      // 不选择则重置 options
      typeOptions.value = toOptions(operatorLogTypeKey);
      return;
    }
    const moduleArr = module.split(':');
    const modulePrefix = moduleArr[moduleArr.length - 1] + ':';
    // 渲染 options
    typeOptions.value = dictState[operatorLogTypeKey].filter(s => (s.value as string).startsWith(modulePrefix));
    // 渲染输入框
    if (formModel.value.type && !formModel.value.type.startsWith(modulePrefix)) {
      formModel.value.type = undefined;
    }
  });

  // 确定
  const handlerOk = async () => {
    if (!formModel.value.limit) {
      Message.error('请输入数量限制');
      return false;
    }
    setLoading(true);
    try {
      // 获取总数量
      const { data } = await getOperatorLogCount(formModel.value);
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
          const { data } = await clearOperatorLog(formModel.value);
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
