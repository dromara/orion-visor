<template>
  <div class="cron-inner-config-list">
    <a-radio-group v-model="type">
      <div class="item">
        <a-radio :value="TypeEnum.every" v-bind="beforeRadioAttrs">每年</a-radio>
      </div>
      <div class="item">
        <a-radio :value="TypeEnum.range" v-bind="beforeRadioAttrs">区间</a-radio>
        <span>从</span>
        <a-input-number v-model="valueRange.start" v-bind="typeRangeAttrs" />
        <span>年 至</span>
        <a-input-number v-model="valueRange.end" v-bind="typeRangeAttrs" />
        <span>年</span>
      </div>
      <div class="item">
        <a-radio :value="TypeEnum.loop" v-bind="beforeRadioAttrs">循环</a-radio>
        <span>从</span>
        <a-input-number v-model="valueLoop.start" v-bind="typeLoopAttrs" />
        <span>年开始, 间隔</span>
        <a-input-number v-model="valueLoop.interval" v-bind="typeLoopAttrs" />
        <span>年</span>
      </div>
    </a-radio-group>
  </div>
</template>

<script lang="ts">
  import { defineComponent } from 'vue';
  import { useFormProps, useFormSetup, useFromEmits } from './use-mixin';

  export default defineComponent({
    name: 'YearForm',
    props: useFormProps({
      defaultValue: '*',
    }),
    emits: useFromEmits(),
    setup(props, context) {
      const nowYear = new Date().getFullYear();
      return useFormSetup(props, context, {
        defaultValue: '*',
        minValue: 0,
        valueRange: { start: nowYear, end: nowYear + 100 },
        valueLoop: { start: nowYear, interval: 1 },
      });
    },
  });
</script>
