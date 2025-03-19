<template>
  <!-- 调整字段 -->
  <a-popover class="table-adjust-popover"
             position="br"
             trigger="click"
             :content-style="{ padding: 0 }"
             @popup-visible-change="visibleChanged">
    <!-- 触发按钮 -->
    <a-button class="icon-button card-header-icon-button" title="调整字段">
      <icon-list />
    </a-button>
    <!-- 调整内容 -->
    <template #content>
      <!-- 顶部按钮 -->
      <div class="table-adjust-popover-header">
        <span class="table-adjust-title">
          表格展示字段
        </span>
        <a-button type="text"
                  size="mini"
                  @click="resetFields">
          重置
        </a-button>
      </div>
      <!-- 分隔符 -->
      <a-divider :margin="4" />
      <!-- 列信息 -->
      <a-checkbox-group v-model="fieldsValue"
                        direction="vertical"
                        @change="fieldsChanged">
        <a-checkbox v-for="field in fieldsHook.originFields"
                    :key="field.dataIndex || field.slotName"
                    :value="field.dataIndex || field.slotName">
          {{ field.label }}
        </a-checkbox>
      </a-checkbox-group>
    </template>
  </a-popover>
</template>

<script lang="ts">
  export default {
    name: 'cardFieldAdjust'
  };
</script>

<script lang="ts" setup>
  import type { CardFieldsHook } from '@/hooks/card';
  import { ref } from 'vue';

  const props = defineProps<{
    fieldsHook: CardFieldsHook;
  }>();

  const fieldsValue = ref<Array<string>>([]);

  // 列显示切换
  const visibleChanged = (show: boolean) => {
    if (show) {
      fieldsValue.value = props.fieldsHook.getConfig();
    }
  };

  // 重置字段
  const resetFields = () => {
    props.fieldsHook.saveConfig(undefined);
    fieldsValue.value = props.fieldsHook.getConfig();
  };

  // 修改字段
  const fieldsChanged = () => {
    props.fieldsHook.saveConfig(fieldsValue.value);
  };

</script>

<style lang="less" scoped>
</style>
