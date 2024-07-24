<template>
  <div>
    <a-alert class="mb16">刷新页面后生效</a-alert>
    <!-- 颜色选择 -->
    <div class="color-setting-container">
      <!-- 透明色 -->
      <div class="color-wrapper"
           :style="{ '--color': 'transparent' }"
           @click="clickColor('')">
        <div class="color-item">
        <span class="color-item-cancel">
          <icon-stop />
        </span>
        </div>
      </div>
      <!-- 其他颜色 -->
      <template v-for="color in toOptions(tabColorKey)">
        <div class="color-wrapper"
             :class="[formModel.color === color.value ? 'selected-color' : '']"
             :style="{ '--color': `${color.value}` }"
             @click="clickColor(color.value as string)">
          <div class="color-item">
            <div class="color-item-dot" />
          </div>
        </div>
      </template>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'labelSettingForm'
  };
</script>

<script lang="ts" setup>
  import type { LabelExtraSettingModel } from '../../../types/define';
  import { onMounted, ref } from 'vue';
  import { tabColorKey } from '../../../types/const';
  import { getHostExtraItem } from '@/api/asset/host-extra';
  import { useDictStore } from '@/store';

  const props = defineProps<{
    hostId: number,
    item: string
  }>();

  const { toOptions } = useDictStore();

  const formModel = ref<LabelExtraSettingModel>({
    color: '',
  });

  // 渲染表单
  const renderForm = async () => {
    const { data } = await getHostExtraItem<LabelExtraSettingModel>({ hostId: props.hostId, item: props.item });
    formModel.value = data;
  };

  // 设置颜色
  const clickColor = (color: string) => {
    formModel.value.color = color;
  };

  // 获取值
  const getValue = async () => {
    return JSON.stringify(formModel.value);
  };

  defineExpose({ getValue });

  onMounted(renderForm);

</script>

<style lang="less" scoped>
  .color-setting-container {
    display: flex;
  }

  .color-wrapper {
    margin-right: 8px;
    width: 32px;
    height: 32px;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all .3s ease-in-out;
    cursor: pointer;
    border-radius: 4px;

    &:hover {
      background: var(--color-fill-3);
    }

    .color-item {
      width: 20px;
      height: 20px;
      border-radius: 100%;
      display: flex;
      align-items: center;
      justify-content: center;

      &-dot {
        width: 100%;
        height: 100%;
        border-radius: 100%;
        background: var(--color);
      }

      &-cancel {
        color: var(--color-content-text-1);
        font-size: 22px;
        line-height: 1;
      }
    }
  }

  .color-wrapper.selected-color {

    .color-item {
      border: 2px solid var(--color);

      &-dot {
        width: 12px;
        height: 12px;
        box-sizing: border-box;
        background: var(--color);
        padding: 4px;
        position: relative;
      }
    }
  }

</style>
