<template>
  <div class="cron-inner-config-list">
    <a-radio-group v-model="type">
      <div class="item">
        <a-radio :value="TypeEnum.unset" v-bind="beforeRadioAttrs">不设置</a-radio>
        <span class="tip-info">日和周只能设置其中之一</span>
      </div>
      <div class="item">
        <a-radio :value="TypeEnum.every" v-bind="beforeRadioAttrs">每日</a-radio>
      </div>
      <div class="item">
        <a-radio :value="TypeEnum.range" v-bind="beforeRadioAttrs">区间</a-radio>
        <span>从</span>
        <a-input-number v-model="valueRange.start" v-bind="inputNumberAttrs" />
        <span>日 至</span>
        <a-input-number v-model="valueRange.end" v-bind="inputNumberAttrs" />
        <span>日</span>
      </div>
      <div class="item">
        <a-radio :value="TypeEnum.loop" v-bind="beforeRadioAttrs">循环</a-radio>
        <span>从</span>
        <a-input-number v-model="valueLoop.start" v-bind="typeLoopAttrs" />
        <span>日开始, 间隔</span>
        <a-input-number v-model="valueLoop.interval" v-bind="typeLoopAttrs" />
        <span>日</span>
      </div>
      <div class="item">
        <a-radio :value="TypeEnum.last" v-bind="beforeRadioAttrs">最后一日</a-radio>
      </div>
      <div class="item">
        <a-radio :value="TypeEnum.specify" v-bind="beforeRadioAttrs">指定</a-radio>
        <div class="list">
          <a-checkbox-group v-model="valueList">
            <a-grid :cols="11">
              <a-grid-item v-for="i in specifyRange" :key="i">
                <a-checkbox :value="i" v-bind="typeSpecifyAttrs">
                  {{ i }}
                </a-checkbox>
              </a-grid-item>
            </a-grid>
          </a-checkbox-group>
        </div>
      </div>
    </a-radio-group>
  </div>
</template>

<script lang="ts">
  import { computed, defineComponent, watch } from 'vue';
  import { TypeEnum, useFormProps, useFormSetup, useFromEmits } from './use-mixin';

  export default defineComponent({
    name: 'DayForm',
    props: useFormProps({
      defaultValue: '*',
      props: {
        week: { type: String, default: '?' },
      },
    }),
    emits: useFromEmits(),
    setup(props: any, context) {
      const disabledChoice = computed(() => {
        return (props.week && props.week !== '?') || props.disabled;
      });
      const setup = useFormSetup(props, context, {
        defaultValue: '*',
        valueWork: 1,
        minValue: 1,
        maxValue: 31,
        valueRange: { start: 1, end: 31 },
        valueLoop: { start: 1, interval: 1 },
        disabled: disabledChoice,
      });
      const typeWorkAttrs = computed(() => ({
        disabled: setup.type.value !== TypeEnum.work || props.disabled || disabledChoice.value,
        ...setup.inputNumberAttrs.value,
      }));

      watch(
        () => props.week,
        () => {
          setup.updateValue(disabledChoice.value ? '?' : setup.computeValue.value);
        },
      );

      return { ...setup, typeWorkAttrs };
    },
  });
</script>
