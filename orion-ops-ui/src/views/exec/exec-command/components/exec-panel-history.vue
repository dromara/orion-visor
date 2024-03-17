<template>
  <div class="container">
    <!-- 表头 -->
    <div class="panel-header">
      <h3>执行历史</h3>
      <span class="history-help">
        展示最近 {{ historyCount }} 条执行记录
      </span>
    </div>
    <div v-if="!historyLogs.length" class="flex-center mt16">
      <a-empty description="无执行记录" />
    </div>
    <!-- 执行记录 -->
    <div v-else class="exec-history-rows">
      <div v-for="record in historyLogs"
           :key="record.id"
           class="exec-history"
           @click="emits('selected', record)">
        <!-- 机器数量 -->
        <span class="exec-history-count">
          {{ record.hostIdList?.length || 0 }}
        </span>
        <!-- 执行描述 -->
        <span class="exec-history-desc">
          {{ record.description }}
        </span>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'execPanelHistory'
  };
</script>

<script lang="ts" setup>
  import type { ExecLogQueryResponse } from '@/api/exec/exec-log';
  import type { ExecCommandRequest } from '@/api/exec/exec';
  import { onMounted, ref } from 'vue';
  import { getExecLogHistory } from '@/api/exec/exec-log';
  import { historyCount } from '../types/const';

  const emits = defineEmits(['selected']);

  const historyLogs = ref<Array<ExecLogQueryResponse>>([]);

  // 添加
  const add = (record: ExecCommandRequest) => {
    const index = historyLogs.value.findIndex(s => s.description === record.description);
    if (index === -1) {
      // 不存在
      historyLogs.value.unshift({
        description: record.description,
        command: record.command,
        parameterSchema: record.parameterSchema,
        timeout: record.timeout,
        hostIdList: record.hostIdList
      } as ExecLogQueryResponse);
    } else {
      // 存在
      const his = historyLogs.value[index];
      historyLogs.value.splice(index, 1);
      historyLogs.value.unshift({
        ...his,
        command: record.command,
        parameterSchema: record.parameterSchema,
        timeout: record.timeout,
        hostIdList: record.hostIdList
      } as ExecLogQueryResponse);
    }
  };

  defineExpose({ add });

  // 加载执行记录
  const fetchExecHistory = async () => {
    const { data } = await getExecLogHistory(historyCount);
    historyLogs.value = data;
  };

  // 加载执行记录
  onMounted(fetchExecHistory);

</script>

<style lang="less" scoped>
  .exec-history-rows {
    position: absolute;
    width: calc(100% - 32px);
    height: calc(100% - 64px);
    overflow: auto;

    &::-webkit-scrollbar-track {
      display: none;
    }
  }

  .exec-history {
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

    &-count {
      width: 24px;
      height: 24px;
      border-radius: 2px;
      margin-right: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #FFF;
      background: rgb(var(--arcoblue-6));
    }

    &-desc {
      color: var(--color-text-2);
      width: calc(100% - 36px);
      overflow: hidden;
      white-space: nowrap;
      text-align: end;
      text-overflow: ellipsis;
    }
  }

  .history-help {
    color: var(--color-text-3);
  }

</style>
