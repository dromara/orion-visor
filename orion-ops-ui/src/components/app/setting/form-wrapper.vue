<template>
  <!-- 数字框 -->
  <a-input-number v-if="type === 'number'"
                  :style="{ width: '80px' }"
                  size="small"
                  :precision="0"
                  :default-value="defaultValue as number"
                  @change="handleChange"
                  hide-button />
  <!-- 开关 -->
  <a-switch v-else-if="type === 'switch'"
            type="round"
            :default-checked="defaultValue as boolean"
            size="small"
            @change="handleChange" />
  <!-- 单选按钮 -->
  <a-radio-group v-else-if="type === 'radio-group'"
                 type="button"
                 size="small"
                 :default-value="defaultValue"
                 :options="options as Array<RadioOption>"
                 @change="handleChange" />
  <!-- 选择框 -->
  <a-select v-else-if="type === 'select'"
            size="small"
            style="width: 128px;"
            :default-value="defaultValue"
            :options="options as Array<SelectOption>"
            @change="handleChange" />
</template>

<script lang="ts" setup>
  import type { PropType } from 'vue';
  import type { RadioOption } from '@arco-design/web-vue/es/radio/interface';
  import type { SelectOption } from '@arco-design/web-vue/es/select/interface';

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
      type: Array as PropType<Array<RadioOption | SelectOption>>,
      default: () => []
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
