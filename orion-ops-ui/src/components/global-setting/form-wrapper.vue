<template>
  <!-- 数字框 -->
  <a-input-number v-if="type === 'number'"
                  :style="{ width: '80px' }"
                  size="small"
                  :default-value="defaultValue as number"
                  @change="handleChange"
                  hide-button />
  <!-- 开关 -->
  <a-switch v-else-if="type === 'switch'"
            type="round"
            :default-checked="defaultValue"
            size="small"
            @change="handleChange" />
  <!-- 单选按钮 -->
  <a-radio-group v-else-if="type === 'radio-group'"
                 type="button"
                 size="mini"
                 :default-value="defaultValue"
                 :options="options"
                 @change="handleChange" />
</template>

<script lang="ts" setup>
  import { PropType } from 'vue';
  import { RadioOption } from '@arco-design/web-vue/es/radio/interface';

  const props = defineProps({
    type: {
      type: String,
      default: 'switch',
    },
    name: {
      type: String,
      default: '',
    },
    defaultValue: {
      type: [String, Boolean, Number],
      default: '',
    },
    options: {
      type: Array as PropType<Array<RadioOption>>,
      default: []
    }
  });
  const emit = defineEmits(['inputChange']);
  const handleChange = (value: unknown) => {
    emit('inputChange', {
      value,
      key: props.name,
    });
  };
</script>
