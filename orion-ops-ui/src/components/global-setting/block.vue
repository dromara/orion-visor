<template>
  <div class="block">
    <h5 class="title">{{ title }}</h5>
    <div v-for="option in options" :key="option.name" class="option-wrapper">
      <!-- 偏好项 -->
      <span>{{ option.name }}</span>
      <!-- input -->
      <form-wrapper :name="option.key"
                    :type="option.type"
                    :default-value="option.defaultVal"
                    :options="option.options"
                    @input-change="handleChange" />
    </div>
  </div>
</template>

<script lang="ts" setup>
  import { PropType } from 'vue';
  import { useAppStore } from '@/store';
  import FormWrapper from './form-wrapper.vue';
  import { RadioOption } from '@arco-design/web-vue/es/radio/interface';

  interface OptionsProps {
    name: string;
    key: string;
    type?: string;
    defaultVal?: boolean | string | number;
    options?: Array<RadioOption>;
  }

  defineProps({
    title: String,
    options: {
      type: Array as PropType<OptionsProps[]>,
      default() {
        return [];
      },
    },
  });
  const appStore = useAppStore();

  /**
   * 修改配置
   */
  const handleChange = async ({ key, value, }: {
    key: string;
    value: unknown;
  }) => {
    // 色弱模式
    if (key === 'colorWeak') {
      document.body.style.filter = value ? 'invert(80%)' : 'none';
    }
    // 顶部菜单
    if (key === 'topMenu') {
      appStore.updateSettings({
        menuCollapse: false,
      });
    }
    // 修改配置
    appStore.updateSettings({ [key]: value });
    // TODO 同步偏好

  };
</script>

<style scoped lang="less">
  .block {
    margin-bottom: 24px;
    user-select: none;
  }

  .title {
    margin: 10px 0;
    padding: 0;
    font-size: 14px;
  }

  .option-wrapper {
    display: flex;
    align-items: center;
    justify-content: space-between;
    height: 32px;
  }
</style>
