<template>
  <div class="container">
    <!-- 表头 -->
    <div class="panel-header">
      <h3>执行历史</h3>
      <span class="history-help">
        展示最近 {{ historyCount }} 条执行记录
      </span>
    </div>
    <!-- 加载中 -->
    <a-skeleton v-if="loading" :animation="true">
      <a-skeleton-line :rows="4"
                       :line-height="40"
                       :line-spacing="8" />
    </a-skeleton>
    <!-- 无数据 -->
    <div v-else-if="!historyLogs.length" class="flex-center mt16">
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
    name: 'execCommandPanelHistory'
  };
</script>

<script lang="ts" setup>
  import type { ExecCommandLogQueryResponse } from '@/api/exec/exec-command-log';
  import type { ExecCommandRequest } from '@/api/exec/exec-command';
  import { onMounted, ref } from 'vue';
  import { getExecCommandLogHistory } from '@/api/exec/exec-command-log';
  import { historyCount } from '../types/const';
  import useLoading from '@/hooks/loading';

  const emits = defineEmits(['selected']);

  const { loading, setLoading } = useLoading(true);

  const historyLogs = ref<Array<ExecCommandLogQueryResponse>>([]);

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
      } as ExecCommandLogQueryResponse);
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
      } as ExecCommandLogQueryResponse);
    }
  };

  defineExpose({ add });

  // 加载执行记录
  const fetchExecHistory = async () => {
    setLoading(true);
    try {
      const { data } = await getExecCommandLogHistory(historyCount);
      historyLogs.value = data;
    } catch (e) {
    } finally {
      setLoading(false);
    }
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
