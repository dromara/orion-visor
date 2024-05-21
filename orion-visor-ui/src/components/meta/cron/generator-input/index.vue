<template>
  <div class="cron-inner">
    <div class="content">
      <!-- 设置表单 -->
      <a-tabs v-model:active-key="activeKey" size="small">
        <!-- 秒 -->
        <a-tab-pane title="秒" key="second" v-if="!hideSecond">
          <second-form v-model="second" :disabled="disabled" />
        </a-tab-pane>
        <!-- 分 -->
        <a-tab-pane title="分" key="minute">
          <minute-form v-model="minute" :disabled="disabled" />
        </a-tab-pane>
        <!-- 时 -->
        <a-tab-pane title="时" key="hour">
          <hour-form v-model="hour" :disabled="disabled" />
        </a-tab-pane>
        <!-- 日 -->
        <a-tab-pane title="日" key="day">
          <day-form v-model="day" :week="week" :disabled="disabled" />
        </a-tab-pane>
        <!-- 月 -->
        <a-tab-pane title="月" key="month">
          <month-form v-model="month" :disabled="disabled" />
        </a-tab-pane>
        <!-- 周 -->
        <a-tab-pane title="周" key="week">
          <week-form v-model="week" :day="day" :disabled="disabled" />
        </a-tab-pane>
        <!-- 年 -->
        <a-tab-pane title="年" key="year" v-if="!hideYear && !hideSecond">
          <year-form v-model="year" :disabled="disabled" />
        </a-tab-pane>
      </a-tabs>
      <!-- 执行时间预览 -->
      <a-row :gutter="8">
        <!-- 快捷修改 -->
        <a-col :span="18" style="margin-top: 28px">
          <a-row :gutter="[12, 12]">
            <!-- 秒 -->
            <a-col :span="8">
              <a-input v-model="inputValues.second" @change="onInputChange">
                <template #prepend>
                  <span class="allow-click" @click="activeKey = 'second'">秒</span>
                </template>
              </a-input>
            </a-col>
            <!-- 分 -->
            <a-col :span="8">
              <a-input v-model="inputValues.minute" @change="onInputChange">
                <template #prepend>
                  <span class="allow-click" @click="activeKey = 'minute'">分</span>
                </template>
              </a-input>
            </a-col>
            <!-- 时 -->
            <a-col :span="8">
              <a-input v-model="inputValues.hour" @change="onInputChange">
                <template #prepend>
                  <span class="allow-click" @click="activeKey = 'hour'">时</span>
                </template>
              </a-input>
            </a-col>
            <!-- 日 -->
            <a-col :span="8">
              <a-input v-model="inputValues.day" @change="onInputChange">
                <template #prepend>
                  <span class="allow-click" @click="activeKey = 'day'">日</span>
                </template>
              </a-input>
            </a-col>
            <!-- 月 -->
            <a-col :span="8">
              <a-input v-model="inputValues.month" @change="onInputChange">
                <template #prepend>
                  <span class="allow-click" @click="activeKey = 'month'">月</span>
                </template>
              </a-input>
            </a-col>
            <!-- 周 -->
            <a-col :span="8">
              <a-input v-model="inputValues.week" @change="onInputChange">
                <template #prepend>
                  <span class="allow-click" @click="activeKey = 'week'">周</span>
                </template>
              </a-input>
            </a-col>
            <!-- 年 -->
            <a-col :span="8">
              <a-input v-model="inputValues.year" @change="onInputChange">
                <template #prepend>
                  <span class="allow-click" @click="activeKey = 'year'">年</span>
                </template>
              </a-input>
            </a-col>
            <!-- 表达式 -->
            <a-col :span="16">
              <a-input v-model="inputValues.cron"
                       :placeholder="placeholder"
                       @change="onInputCronChange">
                <template #prepend>
                  <span class="allow-click">表达式</span>
                </template>
                <template #append>
                  <span class="allow-click span-blue"
                        title="点击复制"
                        @click="copy(inputValues.cron,'已复制')">
                    <icon-copy />
                  </span>
                </template>
              </a-input>
            </a-col>
          </a-row>
        </a-col>
        <!-- 执行时间 -->
        <a-col :span="6">
          <div class="preview-times usn">近五次执行时间 (不解析年)</div>
          <a-textarea v-model="previewTimes" :auto-size="{ minRows: 5, maxRows: 5 }" />
        </a-col>
      </a-row>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'cronGeneratorInput'
  };
</script>

<script lang="ts" setup>
  import type { CronPropType } from './const.types';
  import { computed, onMounted, reactive, ref, watch } from 'vue';
  import { cronEmits, cronDefaultProps, convertWeekToQuartz } from './const.types';
  import { useDebounceFn } from '@vueuse/core';
  import { dateFormat } from '@/utils';
  import { copy } from '@/hooks/copy';
  import CronParser from 'cron-parser';
  import SecondForm from './form/second-form.vue';
  import MinuteForm from './form/minute-form.vue';
  import HourForm from './form/hour-form.vue';
  import DayForm from './form/day-form.vue';
  import MonthForm from './form/month-form.vue';
  import WeekForm from './form/week-form.vue';
  import YearForm from './form/year-form.vue';

  const emit = defineEmits([...cronEmits]);
  const props = withDefaults(defineProps<Partial<CronPropType>>(), { ...cronDefaultProps });

  const activeKey = ref(props.hideSecond ? 'minute' : 'second');
  const second = ref('*');
  const minute = ref('*');
  const hour = ref('*');
  const day = ref('*');
  const month = ref('*');
  const week = ref('?');
  const year = ref('*');
  const inputValues = reactive({
    second: '',
    minute: '',
    hour: '',
    day: '',
    month: '',
    week: '',
    year: '',
    cron: '',
  });
  const previewTimes = ref('执行预览');

  // cron 表达式
  const expression = computed(() => {
    const result: string[] = [];
    if (!props.hideSecond) {
      result.push(second.value ? second.value : '*');
    }
    result.push(minute.value ? minute.value : '*');
    result.push(hour.value ? hour.value : '*');
    result.push(day.value ? day.value : '*');
    result.push(month.value ? month.value : '*');
    result.push(week.value ? week.value : '?');
    if (!props.hideYear && !props.hideSecond) {
      result.push(year.value ? year.value : '*');
    }
    return result.join(' ');
  });

  // 不含年的 cron 表达式
  const expressionNoYear = computed(() => {
    const v = expression.value;
    if (props.hideYear || props.hideSecond) return v;
    const vs = v.split(' ');
    if (vs.length >= 6) {
      // 转成 quartz 周
      vs[5] = convertWeekToQuartz(vs[5]);
    }
    return vs.slice(0, vs.length - 1).join(' ');
  });

  // 计算触发时间
  const calcTriggerTime = () => {
    try {
      // 解析表达式
      const iter = CronParser.parseExpression(expressionNoYear.value, {
        currentDate: dateFormat(new Date()),
      });
      const result: string[] = [];
      for (let i = 1; i <= 5; i++) {
        result.push(dateFormat(new Date(iter.next() as any)));
      }
      previewTimes.value = result.length > 0 ? result.join('\n') : '无执行时间';
      // 回调
      if (props.callback) {
        props.callback(expression.value, +new Date(), true);
      }
    } catch (e) {
      previewTimes.value = '表达式错误';
      // 回调
      if (props.callback) {
        props.callback(expression.value, +new Date(), false);
      }
    }
  };

  const calcTriggerTimeList = useDebounceFn(calcTriggerTime, 500);

  // 监听 cron 修改
  watch(() => props.modelValue, (newVal) => {
    if (newVal === expression.value) {
      return;
    }
    parseCron();
  });

  // 监听 cron 修改
  watch(expression, (newValue) => {
    calcTriggerTimeList();
    emitValue(newValue);
    assignInput();
  });

  // 根据 cron 解析
  const parseCron = () => {
    // 计算执行时间
    calcTriggerTimeList();
    if (!props.modelValue) {
      return;
    }
    const values = props.modelValue.split(' ').filter((item) => !!item);
    if (!values || values.length <= 0) {
      return;
    }
    let i = 0;
    if (!props.hideSecond) second.value = values[i++];
    if (values.length > i) minute.value = values[i++];
    if (values.length > i) hour.value = values[i++];
    if (values.length > i) day.value = values[i++];
    if (values.length > i) month.value = values[i++];
    if (values.length > i) week.value = values[i++];
    if (values.length > i) year.value = values[i];
    // 重新分配
    assignInput();
  };

  // 重新分配
  const assignInput = () => {
    inputValues.second = second.value;
    inputValues.minute = minute.value;
    inputValues.hour = hour.value;
    inputValues.day = day.value;
    inputValues.month = month.value;
    inputValues.week = week.value;
    inputValues.year = year.value;
    inputValues.cron = expression.value;
  };

  // 修改 cron 解析内容
  const onInputChange = () => {
    second.value = inputValues.second;
    minute.value = inputValues.minute;
    hour.value = inputValues.hour;
    day.value = inputValues.day;
    month.value = inputValues.month;
    week.value = inputValues.week;
    year.value = inputValues.year;
  };

  // 修改 cron 输入框
  const onInputCronChange = (value: string) => {
    emitValue(value);
  };

  // 修改 cron
  const emitValue = (value: string) => {
    emit('change', value);
    emit('update:modelValue', value);
  };

  onMounted(() => {
    assignInput();
    parseCron();
    // 如果 modelValue 没有值则更新为 expression
    if (!props.modelValue) {
      emitValue(expression.value);
    }
  });

</script>

<style lang="less" scoped>
  .cron-inner {
    user-select: none;

    :deep(.arco-tabs-content) {
      padding-top: 6px;
    }

    :deep(.cron-inner-config-list) {
      text-align: left;
      margin: 0 12px 4px 12px;

      .item {
        margin-top: 6px;
        font-size: 14px;
        width: 100%;
      }

      .choice {
        padding: 4px 8px 4px 0;
      }

      .w60 {
        margin: 0 8px !important;
        padding: 0 8px !important;
        width: 60px !important;
      }

      .w80 {
        margin: 0 8px !important;
        padding: 0 8px !important;
        width: 80px !important;
      }

      .list {
        margin: 0 20px;
      }

      .list-check-item {
        padding: 1px 3px;
        width: 4em;
      }

      .list-cn .list-check-item {
        width: 5em;
      }

      .tip-info {
        color: var(--color-text-3);
      }
    }
  }

  :deep(.arco-input-prepend) {
    padding: 0 !important;
  }

  :deep(.arco-input-append) {
    padding: 0 !important;
  }

  .preview-times {
    color: var(--color-text-3);
    margin: 2px 0 4px 0;
  }

  .allow-click {
    width: 100%;
    height: 100%;
    padding: 0 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    user-select: none;
  }

</style>
