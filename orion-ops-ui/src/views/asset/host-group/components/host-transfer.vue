<template>
  <div class="transfer-container">
    <!-- 头部 -->
    <div class="transfer-header">
      <!-- 提示 -->
      <a-alert class="alert-wrapper">123123</a-alert>
      <!-- 保存按钮 -->
      <a-button class="save-button"
                type="primary"
                @click="save">
        保存
        <template #icon>
          <icon-check />
        </template>
      </a-button>
    </div>
    <!-- 传输框 -->
    <a-transfer v-model="value"
                :data="data"
                :source-input-search-props="{ placeholder:'请输入主机名称/编码/IP' }"
                :target-input-search-props="{ placeholder:'请输入主机名称/编码/IP' }"
                show-search
                one-way>
      <!-- 主机列表 -->
      <template #source-title="{ countTotal, countSelected, checked, indeterminate, onSelectAllChange}">
        <!-- 左侧标题 -->
        <div class="source-title-container">
          <a-checkbox style="margin-right: 8px;"
                      :model-value="checked"
                      :indeterminate="indeterminate"
                      @change="onSelectAllChange" />
          <span>
            主机列表 {{ countSelected }} / {{ countTotal }}
          </span>
        </div>
      </template>
      <!-- 右侧标题 -->
      <template #target-title="{ countTotal, countSelected, onClear }">
        <div class="target-title-container">
          <span>已选择 <span class="span-blue">{{ countTotal }}</span> 个</span>
          <span class="pointer"
                @click="onClear"
                title="清空">
            <icon-delete />
          </span>
        </div>
      </template>
    </a-transfer>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'host-transfer'
  };
</script>

<script lang="ts" setup>
  import type { TransferItem } from '@arco-design/web-vue/es/transfer/interface';
  import { onMounted, ref } from 'vue';
  import { useCacheStore } from '@/store';

  const data = ref<Array<TransferItem>>([]);
  const value = ref([]);

  // 保存
  const save = () => {
    console.log(value.value);
  };

  // 加载主机
  onMounted(() => {
    const cacheStore = useCacheStore();
    data.value = Array(200).fill(undefined).map((_, index) => ({
      value: `option${index + 1}`,
      label: `Option ${index + 1}`,
      disabled: false
    }));
  });

</script>

<style lang="less" scoped>
  .transfer-container {
    width: 100%;
    height: 100%;

    .transfer-header {
      margin-bottom: 12px;
      display: flex;
      align-items: center;

      .alert-wrapper {
        height: 32px;
      }

      .save-button {
        margin-left: 16px;
      }
    }
  }

  :deep(.arco-transfer) {
    height: calc(100% - 44px);

    .arco-transfer-view {
      width: 100%;
      height: 100%;
      user-select: none;
    }

    .arco-transfer-view-target {

      .arco-transfer-list-item-content {
        margin-left: 4px;
      }

      .arco-transfer-list-item-remove-btn {
        margin-right: 8px;
      }
    }
  }

  .source-title-container {
    display: flex;
    align-items: center;
  }

  .target-title-container {
    display: flex;
    justify-content: space-between;
    align-items: center;

    svg {
      font-size: 16px;
    }
  }

</style>
