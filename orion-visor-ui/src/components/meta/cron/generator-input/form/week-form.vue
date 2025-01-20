<template>
  <div class="cron-inner-config-list">
    <a-radio-group v-model="type">
      <div class="item">
        <a-radio :value="TypeEnum.unset" v-bind="beforeRadioAttrs">不设置</a-radio>
        <span class="tip-info">日和周只能设置其中之一</span>
      </div>
      <div class="item">
        <a-radio :value="TypeEnum.range" v-bind="beforeRadioAttrs">区间</a-radio>
        <span>从</span>
        <a-select v-model="valueRange.start" v-bind="typeRangeSelectAttrs">
          <a-option v-for="item in weekOptions" :key="item.value" :label="item.label" :value="item.value" />
        </a-select>
        <span>至</span>
        <a-select v-model="valueRange.end" v-bind="typeRangeSelectAttrs">
          <a-option v-for="item in weekOptions" :key="item.value" :label="item.label" :value="item.value" />
        </a-select>
      </div>
      <div class="item">
        <a-radio :value="TypeEnum.loop" v-bind="beforeRadioAttrs">循环</a-radio>
        <span>从</span>
        <a-select v-model="valueLoop.start" v-bind="typeLoopSelectAttrs">
          <a-option v-for="item in weekOptions" :key="item.value" :label="item.label" :value="item.value" />
        </a-select>
        <span>开始, 间隔</span>
        <a-input-number v-model="valueLoop.interval" v-bind="typeLoopAttrs" />
        <span>天</span>
      </div>
      <div class="item">
        <a-radio :value="TypeEnum.specify" v-bind="beforeRadioAttrs">指定</a-radio>
        <div class="list list-cn">
          <a-checkbox-group v-model="valueList">
            <template v-for="opt in weekOptions" :key="opt">
              <a-checkbox :value="opt.value" v-bind="typeSpecifyAttrs">
                {{ opt.label }}
              </a-checkbox>
            </template>
          </a-checkbox-group>
        </div>
      </div>
    </a-radio-group>
  </div>
</template>

<script lang="ts">
  import { computed, defineComponent, watch } from 'vue';
  import { TypeEnum, useFormProps, useFormSetup, useFromEmits, WEEK_MAP } from './use-mixin';

  export default defineComponent({
    name: 'WeekForm',
    props: useFormProps({
      defaultValue: '?',
      props: {
        day: { type: String, default: '*' },
      },
    }),
    emits: useFromEmits(),
    setup(props: any, context) {
      const disabledChoice = computed(() => {
        return (props.day && props.day !== '?') || props.disabled;
      });
      const setup = useFormSetup(props, context, {
        defaultType: TypeEnum.unset,
        defaultValue: '?',
        minValue: 1,
        maxValue: 7,
        // 0,7表示周日 1表示周一
        valueRange: { start: 1, end: 7 },
        valueLoop: { start: 2, interval: 1 },
        disabled: disabledChoice,
      });
      const weekOptions = computed(() => {
        const options: { label: string; value: number }[] = [];
        for (const weekKey of Object.keys(WEEK_MAP)) {
          const weekName: string = WEEK_MAP[weekKey];
          options.push({
            value: Number.parseInt(weekKey),
            label: weekName,
          });
        }
        return options;
      });

      const typeRangeSelectAttrs = computed<any>(() => ({
        disabled: setup.typeRangeAttrs.value.disabled,
        size: 'small',
        class: ['w80'],
      }));

      const typeLoopSelectAttrs = computed<any>(() => ({
        disabled: setup.typeLoopAttrs.value.disabled,
        size: 'small',
        class: ['w80'],
      }));

      watch(() => props.day, () => {
        setup.updateValue(disabledChoice.value ? '?' : setup.computeValue.value);
      });

      return {
        ...setup,
        weekOptions,
        typeLoopSelectAttrs,
        typeRangeSelectAttrs,
        WEEK_MAP,
      };
    },
  });
</script>
