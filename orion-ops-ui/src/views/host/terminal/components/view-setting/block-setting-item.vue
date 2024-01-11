<template>
  <a-col :span="12">
    <div class="block-form-item-wrapper">
      <div class="block-form-item-header">
        <!-- label -->
        <div class="block-form-item-label">
          {{ label }}
        </div>
        <!-- item -->
        <div class="block-form-item-value">
          <slot />
        </div>
      </div>
      <!-- 描述 -->
      <div class="block-form-item-desc">
        {{ desc }}
      </div>
    </div>
  </a-col>

</template>

<script lang="ts">
  export default {
    name: 'blockSettingItem'
  };
</script>

<script lang="ts" setup>

  defineProps<{
    label: string,
    desc: string,
  }>();

</script>

<style lang="less" scoped>
  .block-form-item-wrapper {
    height: 100%;
    min-height: 64px;
    border-radius: 4px;
    background: var(--color-fill-2);
    padding: 16px;
    display: flex;
    flex-direction: column;

    .block-form-item-header {
      display: flex;
      justify-content: space-between;
      margin-bottom: 14px;
    }

    .block-form-item-label {
      color: var(--color-content-text-3);
      font-size: 14px;
    }

    .block-form-item-desc {
      color: var(--color-text-2);
      font-size: 12px;
    }

    :deep(.arco-input-wrapper) {
      background-color: var(--color-fill-3)
    }
    :deep(.arco-select) {
      background-color: var(--color-fill-3)
    }
  }
</style>
