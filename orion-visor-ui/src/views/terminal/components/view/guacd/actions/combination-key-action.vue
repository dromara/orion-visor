<template>
  <a-row :gutter="[12, 12]" wrap>
    <a-col v-for="item in GuacdCombinationKeyItems"
           :key="item.name"
           :span="12"
           class="combination-key-item"
           @click="triggerCombinationKey(item.keys)">
      <span>{{ item.name }}</span>
    </a-col>
  </a-row>
</template>

<script lang="ts">
  export default {
    name: 'combinationKeyAction'
  };
</script>

<script lang="ts" setup>
  import type { IGuacdSession } from '@/views/terminal/interfaces';
  import { GuacdCombinationKeyItems } from '@/views/terminal/types/const';

  const props = defineProps<{
    session: IGuacdSession;
  }>();
  const emits = defineEmits(['close']);

  // 触发组合键
  const triggerCombinationKey = (keys: Array<number>) => {
    props.session.sendKeys(keys);
    emits('close');
  };

</script>

<style lang="less" scoped>
</style>
