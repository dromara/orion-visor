<template>
  <query-header :model="formModel"
                label-align="left"
                :itemOptions="{ [visibleUser ? 5 : 4]: { span: 2 } }"
                @submit="submit"
                @reset="submit"
                @keyup.enter="submit">
    <!-- 操作用户 -->
    <a-form-item v-if="visibleUser"
                 field="userId"
                 label="操作用户"
                 label-col-flex="50px">
      <user-selector v-model="formModel.userId"
                     placeholder="请选择操作用户"
                     allow-clear />
    </a-form-item>
    <!-- 操作模块 -->
    <a-form-item field="module" label="操作模块" label-col-flex="50px">
      <a-select v-model="formModel.module"
                :options="toOptions(operatorLogModuleKey)"
                :allow-search="true"
                :filter-option="labelFilter"
                placeholder="请选择操作模块"
                allow-clear />
    </a-form-item>
    <!-- 操作类型 -->
    <a-form-item field="type" label="操作类型" label-col-flex="50px">
      <a-select v-model="formModel.type"
                :options="typeOptions"
                :allow-search="true"
                :filter-option="labelFilter"
                placeholder="请选择操作类型"
                allow-clear />
    </a-form-item>
    <!-- 风险等级 -->
    <a-form-item field="riskLevel" label="风险等级" label-col-flex="50px">
      <a-select v-model="formModel.riskLevel"
                :options="toOptions(operatorRiskLevelKey)"
                placeholder="请选择风险等级"
                allow-clear />
    </a-form-item>
    <!-- 执行结果 -->
    <a-form-item field="result" label="执行结果" label-col-flex="50px">
      <a-select v-model="formModel.result"
                :options="toOptions(operatorLogResultKey)"
                placeholder="请选择执行结果"
                allow-clear />
    </a-form-item>
    <!-- 执行时间 -->
    <a-form-item field="startTimeRange" label="执行时间" label-col-flex="50px">
      <a-range-picker v-model="formModel.startTimeRange"
                      style="width: 100%"
                      :time-picker-props="{ defaultValue: ['00:00:00', '23:59:59'] }"
                      show-time
                      format="YYYY-MM-DD HH:mm:ss" />
    </a-form-item>
  </query-header>
</template>

<script lang="ts">
  export default {
    name: 'operatorLogQueryHeader'
  };
</script>

<script lang="ts" setup>
  import type { OperatorLogQueryRequest } from '@/api/user/operator-log';
  import type { SelectOptionData } from '@arco-design/web-vue/es/select/interface';
  import { reactive, ref, watch } from 'vue';
  import useLoading from '@/hooks/loading';
  import { useDictStore } from '@/store';
  import { operatorLogModuleKey, operatorLogTypeKey, operatorRiskLevelKey, operatorLogResultKey } from '../types/const';
  import { labelFilter } from '@/types/form';
  import UserSelector from '@/components/user/user/user-selector.vue';

  const emits = defineEmits(['submit']);
  const props = defineProps({
    visibleUser: {
      type: Boolean,
      default: true
    }
  });

  const { loading, setLoading } = useLoading();
  const { $state: dictState, toOptions } = useDictStore();

  const typeOptions = ref<SelectOptionData[]>(toOptions(operatorLogTypeKey));
  const formModel = reactive<OperatorLogQueryRequest>({
    module: undefined,
    type: undefined,
    riskLevel: undefined,
    result: undefined,
    startTimeRange: undefined,
  });

  // 监听类型变化
  watch(() => formModel.module, (module: string | undefined) => {
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
    if (formModel.type && !formModel.type.startsWith(modulePrefix)) {
      formModel.type = undefined;
    }
  });

  // 切换页码
  const submit = () => {
    emits('submit', { ...formModel });
  };

</script>

<style lang="less" scoped>

</style>
