import { computed, reactive, ref, unref, watch } from 'vue';

// 类型定义
export enum TypeEnum {
  unset = 'UNSET',
  every = 'EVERY',
  range = 'RANGE',
  loop = 'LOOP',
  work = 'WORK',
  last = 'LAST',
  specify = 'SPECIFY',
}

// 周定义
export const WEEK_MAP: any = {
  1: '周日',
  2: '周一',
  3: '周二',
  4: '周三',
  5: '周四',
  6: '周五',
  7: '周六',
};

// use 公共 props
export function useFormProps(options: any) {
  const defaultValue = options?.defaultValue ?? '?';
  return {
    modelValue: {
      type: String,
      default: defaultValue,
    },
    disabled: {
      type: Boolean,
      default: false,
    },
    ...options?.props,
  };
}

// use 公共 emits
export function useFromEmits() {
  return ['change', 'update:modelValue'];
}

// use 公共 setup
export function useFormSetup(props: any, context: any, options: any) {
  const { emit } = context;
  const defaultValue = ref(options?.defaultValue ?? '?');
  // 类型
  const type = ref(options.defaultType ?? TypeEnum.every);
  const valueList = ref<any[]>([]);
  // 对于不同的类型, 所定义的值也有所不同
  const valueRange = reactive(options.valueRange);
  const valueLoop = reactive(options.valueLoop);
  const valueWork = ref(options.valueWork);
  const maxValue = ref(options.maxValue);
  const minValue = ref(options.minValue);

  // 根据不同的类型计算出的 value
  const computeValue = computed(() => {
    const valueArray: any[] = [];
    switch (type.value) {
      case TypeEnum.unset:
        valueArray.push('?');
        break;
      case TypeEnum.every:
        valueArray.push('*');
        break;
      case TypeEnum.range:
        valueArray.push(`${valueRange.start}-${valueRange.end}`);
        break;
      case TypeEnum.loop:
        valueArray.push(`${valueLoop.start}/${valueLoop.interval}`);
        break;
      case TypeEnum.work:
        valueArray.push(`${valueWork.value}W`);
        break;
      case TypeEnum.last:
        valueArray.push('L');
        break;
      case TypeEnum.specify:
        if (valueList.value.length === 0) {
          valueList.value.push(minValue.value);
        }
        valueArray.push(valueList.value.join(','));
        break;
      default:
        valueArray.push(defaultValue.value);
        break;
    }
    return valueArray.length > 0 ? valueArray.join('') : defaultValue.value;
  });

  // 指定值范围区间, 介于最小值和最大值之间
  const specifyRange = computed(() => {
    const range: number[] = [];
    if (maxValue.value != null) {
      for (let i = minValue.value; i <= maxValue.value; i++) {
        range.push(i);
      }
    }
    return range;
  });

  // 更新值
  const updateValue = (value: any) => {
    emit('change', value);
    emit('update:modelValue', value);
  };

  // 解析值
  const parseValue = (value: any) => {
    if (value === computeValue.value) {
      return;
    }
    try {
      if (!value || value === defaultValue.value) {
        type.value = TypeEnum.every;
      } else if (value.indexOf('?') >= 0) {
        type.value = TypeEnum.unset;
      } else if (value.indexOf('-') >= 0) {
        type.value = TypeEnum.range;
        const values = value.split('-');
        if (values.length >= 2) {
          valueRange.start = parseInt(values[0]);
          valueRange.end = parseInt(values[1]);
        }
      } else if (value.indexOf('/') >= 0) {
        type.value = TypeEnum.loop;
        const values = value.split('/');
        if (values.length >= 2) {
          valueLoop.start = value[0] === '*' ? 0 : parseInt(values[0]);
          valueLoop.interval = parseInt(values[1]);
        }
      } else if (value.indexOf('W') >= 0) {
        type.value = TypeEnum.work;
        const values = value.split('W');
        if (!values[0] && !isNaN(values[0])) {
          valueWork.value = parseInt(values[0]);
        }
      } else if (value.indexOf('L') >= 0) {
        type.value = TypeEnum.last;
      } else if (value.indexOf(',') >= 0 || !isNaN(value)) {
        type.value = TypeEnum.specify;
        valueList.value = value.split(',').map((item: any) => parseInt(item));
      } else {
        type.value = TypeEnum.every;
      }
    } catch (e) {
      type.value = TypeEnum.every;
    }
  };

  // 更新值
  watch(() => props.modelValue, (val) => {
    if (val !== computeValue.value) {
      parseValue(val);
    }
  }, { immediate: true });

  // 更新值
  watch(computeValue, (v) => updateValue(v));

  // 单选框属性
  const beforeRadioAttrs = computed(() => ({
    class: ['choice'],
    disabled: props.disabled || unref(options.disabled),
    size: 'small',
  }));

  // 输入框属性
  const inputNumberAttrs = computed(() => ({
    max: maxValue.value,
    min: minValue.value,
    precision: 0,
    size: 'small',
    hideButton: true,
    class: 'w60'
  }));

  // 区间属性
  const typeRangeAttrs = computed(() => ({
    disabled: type.value !== TypeEnum.range || props.disabled || unref(options.disabled),
    ...inputNumberAttrs.value,
  }));

  // 间隔属性
  const typeLoopAttrs = computed(() => ({
    disabled: type.value !== TypeEnum.loop || props.disabled || unref(options.disabled),
    ...inputNumberAttrs.value,
  }));

  // 指定属性
  const typeSpecifyAttrs = computed(() => ({
    disabled: type.value !== TypeEnum.specify || props.disabled || unref(options.disabled),
    class: ['list-check-item'],
    size: 'small',
  }));

  return {
    type,
    TypeEnum,
    defaultValue,
    valueRange,
    valueLoop,
    valueList,
    valueWork,
    maxValue,
    minValue,
    computeValue,
    specifyRange,
    updateValue,
    // parseValue,
    beforeRadioAttrs,
    inputNumberAttrs,
    typeRangeAttrs,
    typeLoopAttrs,
    typeSpecifyAttrs,
  };

}
