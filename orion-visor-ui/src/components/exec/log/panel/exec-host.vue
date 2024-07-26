<template>
  <!-- 执行主机 -->
  <div class="container">
    <!-- 表头 -->
    <div class="host-header">
      <h3 class="usn">执行主机</h3>
      <!-- 操作 -->
      <a-button v-if="visibleBack"
                size="small"
                @click="emits('back')">
        返回
      </a-button>
    </div>
    <!-- 主机列表 -->
    <div class="exec-host-items">
      <a-scrollbar>
        <div v-for="(item, index) in hosts"
             :key="item.id"
             class="exec-host-item"
             :class="[ current === item.id ? 'exec-host-item-selected' : '' ]"
             :style="{ marginBottom: index === hosts.length -1 ? 0 : '8px' }"
             @click="emits('selected', item.id)">
          <!-- 主机名称 -->
          <div class="exec-host-item-name">
            <span class="host-name">{{ item.hostName }}</span>
            <span class="host-address">{{ item.hostAddress }}</span>
          </div>
          <!-- 状态 -->
          <div class="exec-host-item-status">
            <a-tag :color="getDictValue(execHostStatusKey, item.status, 'execColor')">
              {{ getDictValue(execHostStatusKey, item.status) }}
            </a-tag>
          </div>
        </div>
      </a-scrollbar>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'execHost'
  };
</script>

<script lang="ts" setup>
  import type { ExecHostLogQueryResponse } from '@/api/exec/exec-log';
  import { useDictStore } from '@/store';
  import { execHostStatusKey } from '../const';

  const props = defineProps<{
    visibleBack: boolean;
    current: number;
    hosts: Array<ExecHostLogQueryResponse>;
  }>();
  const emits = defineEmits(['back', 'selected']);

  const { getDictValue } = useDictStore();

</script>

<style lang="less" scoped>

  .host-header {
    width: 100%;
    height: 28px;
    margin-bottom: 8px;
    display: flex;
    justify-content: space-between;
    align-items: flex-start;

    h3, > span {
      margin: 0;
      overflow: hidden;
      white-space: nowrap;
    }

    h3 {
      color: var(--color-text-1);
    }
  }

  .exec-host-items {
    position: relative;
    width: 100%;
    height: calc(100% - 38px);

    :deep(.arco-scrollbar) {
      position: absolute;
      width: 100%;
      height: 100%;

      &-container {
        height: 100%;
        overflow-y: auto;
      }
    }
  }

  .exec-host-item-selected {
    background: var(--color-fill-3) !important;
  }

  .exec-host-item {
    height: 46px;
    padding: 8px;
    border-radius: 2px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: var(--color-fill-2);
    transition: all .2s;
    user-select: none;
    cursor: pointer;

    &:hover {
      background: var(--color-fill-3);
    }

    &-name {
      width: calc(100% - 72px);
      margin-right: 12px;
      display: flex;
      flex-direction: column;
      color: var(--color-text-2);

      .host-name, .host-address {
        display: block;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
      }

      .host-address {
        margin-top: 2px;
        font-size: 10px;
        color: var(--color-text-3);
      }
    }

    &-status {
      display: flex;
      justify-content: flex-end;
    }

  }

</style>
