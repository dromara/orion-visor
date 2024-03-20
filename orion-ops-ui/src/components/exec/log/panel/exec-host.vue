<template>
  <!-- 执行主机 -->
  <div class="container">
    <!-- 表头 -->
    <div class="panel-header">
      <h3>执行主机</h3>
      <!-- 操作 -->
      <a-button v-if="visibleBack"
                size="small"
                @click="emits('back')">
        返回
      </a-button>
    </div>
    <!-- 主机列表 -->
    <div class="exec-host-container">
      <div v-for="item in hosts"
           :key="item.id"
           class="exec-host-item"
           :class="[ current === item.id ? 'exec-host-item-selected' : '' ]"
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
  import { execHostStatusKey } from './const';

  const props = defineProps<{
    visibleBack: boolean;
    current: number;
    hosts: Array<ExecHostLogQueryResponse>;
  }>();
  const emits = defineEmits(['back', 'selected']);

  const { getDictValue } = useDictStore();

</script>

<style lang="less" scoped>
  .exec-host-container {
    margin-top: 4px;
    position: absolute;
    width: calc(100% - 32px);
    height: calc(100% - 68px);
    overflow: auto;

    &::-webkit-scrollbar-track {
      display: none;
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
    margin-bottom: 8px;
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
