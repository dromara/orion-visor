<template>
  <a-space>
    <a-tag v-for="item in GuacdKeyboardItems"
           :key="item.name"
           :default-checked="session.displayHandler.keyboardDownKeys.includes(item.key)"
           checkable
           bordered
           @check="(checked: boolean) => setChecked(item.key, checked)">
      {{ item.name }}
    </a-tag>
  </a-space>
</template>

<script lang="ts">
  export default {
    name: 'triggerKeyAction'
  };
</script>

<script lang="ts" setup>
  import type { IGuacdSession } from '@/views/terminal/interfaces';
  import { GuacdKeyboardItems } from '@/views/terminal/types/const';

  const props = defineProps<{
    session: IGuacdSession;
  }>();

  // 设置选中
  const setChecked = (key: number, checked: boolean) => {
    const keys = props.session.displayHandler.keyboardDownKeys;
    if (checked) {
      keys.push(key);
    } else {
      const index = keys.findIndex(s => s === key);
      if (index !== -1) {
        keys.splice(index, 1);
      }
    }
  };

</script>

<style lang="less" scoped>
</style>
