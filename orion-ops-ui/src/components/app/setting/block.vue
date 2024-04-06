<template>
  <div class="block">
    <h5 class="title">{{ title }}</h5>
    <template v-for="option in options" :key="option.name">
      <div class="option-wrapper"
           v-permission="option.permission || []"
           :style="{ 'margin': option.margin || '' }">
        <!-- 偏好项 -->
        <span>{{ option.name }}</span>
        <!-- 偏好值 -->
        <form-wrapper :name="option.key"
                      :type="option.type as string"
                      :default-value="option.defaultVal"
                      :options="option.options"
                      @input-change="handleChange" />
      </div>
    </template>
  </div>
</template>

<script lang="ts" setup>
  import type { RadioOption } from '@arco-design/web-vue/es/radio/interface';
  import type { SelectOption } from '@arco-design/web-vue/es/select/interface';
  import { useAppStore } from '@/store';
  import { updatePreference } from '@/api/user/preference';
  import FormWrapper from './form-wrapper.vue';

  interface OptionsProps {
    name: string;
    key: string;
    type?: string;
    permission?: string[];
    defaultVal?: boolean | string | number;
    options?: Array<RadioOption | SelectOption>;
    margin?: string;
  }

  defineProps<Partial<{
    title: string;
    options: Array<OptionsProps>;
  }>>();

  const appStore = useAppStore();

  /**
   * 修改配置
   */
  const handleChange = async ({ key, value, }: {
    key: string;
    value: unknown;
  }) => {
    // 顶部菜单
    if (key === 'topMenu') {
      appStore.updateSettings({
        menuCollapse: false,
      });
    }
    // 修改配置
    appStore.updateSettings({ [key]: value });
    // 同步偏好
    try {
      await updatePreference({
        type: 'SYSTEM',
        item: key,
        value
      });
    } catch (e) {
    }
  };
</script>

<style lang="less" scoped>
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
