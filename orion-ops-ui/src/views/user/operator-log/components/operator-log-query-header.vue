<template>
  <a-query-header :model="formModel"
                  label-align="left"
                  @submit="submit"
                  @reset="reset"
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
                placeholder="请选择操作模块"
                @change="selectedModule"
                allow-clear />
    </a-form-item>
    <!-- 操作类型 -->
    <a-form-item field="type" label="操作类型" label-col-flex="50px">
      <a-select v-model="formModel.type"
                :options="typeOptions"
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
    <a-form-item field="startTime" label="执行时间" label-col-flex="50px">
      <a-range-picker v-model="timeRange"
                      :time-picker-props="{ defaultValue: ['00:00:00', '23:59:59'] }"
                      show-time
                      format="YYYY-MM-DD HH:mm:ss"
                      @ok="timeRangePicked" />
    </a-form-item>
  </a-query-header>
</template>

<script lang="ts">
  export default {
    name: 'operator-log-query-header'
  };
</script>

<script lang="ts" setup>
  import type { OperatorLogQueryRequest } from '@/api/user/operator-log';
  import type { SelectOptionData } from '@arco-design/web-vue/es/select/interface';
  import { reactive, ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import { useDictStore } from '@/store';
  import UserSelector from '@/components/user/role/user-selector.vue';
  import { operatorLogModuleKey, operatorLogTypeKey, operatorRiskLevelKey, operatorLogResultKey } from '../types/const';

  const emits = defineEmits(['submit']);
  const props = defineProps({
    visibleUser: {
      type: Boolean,
      default: true
    }
  });

  const { loading, setLoading } = useLoading();
  const { $state: dictState, toOptions } = useDictStore();

  const timeRange = ref<string[]>([]);
  const typeOptions = ref<SelectOptionData[]>(toOptions(operatorLogTypeKey));
  const formModel = reactive<OperatorLogQueryRequest>({
    userId: undefined,
    module: undefined,
    type: undefined,
    riskLevel: undefined,
    result: undefined,
    startTimeStart: undefined,
    startTimeEnd: undefined,
  });

  // 选择时间
  const timeRangePicked = (e: string[]) => {
    formModel.startTimeStart = e[0];
    formModel.startTimeEnd = e[1];
  };

  // 选择类型
  const selectedModule = (module: string) => {
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
  };

  // 重置
  const reset = () => {
    timeRange.value = [];
    formModel.startTimeStart = undefined;
    formModel.startTimeEnd = undefined;
    submit();
  };

  // 切换页码
  const submit = () => {
    emits('submit', { ...formModel });
  };

</script>

<style lang="less" scoped>

</style>
