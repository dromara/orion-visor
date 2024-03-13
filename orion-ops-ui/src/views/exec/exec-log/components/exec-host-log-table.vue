<template>
  <!-- table -->
  <a-table row-key="id"
           ref="tableRef"
           :loading="loading"
           :columns="columns"
           :data="row.hosts"
           :expandable="expandable"
           :scroll="{ y: '100%' }"
           :pagination="false"
           :bordered="false">
    <!-- 执行主机 -->
    <template #hostName="{ record }">
      <span class="span-blue">
        {{ record.hostName }}
      </span>
    </template>
    <!-- 错误信息 -->
    <template #errorMessage="{ record }">
      <span class="span-red">
        {{ record.errorMessage }}
      </span>
    </template>
    <!-- 执行状态 -->
    <template #status="{ record }">
      <a-tag :color="getDictValue(execHostStatusKey, record.status, 'color')">
        {{ getDictValue(execHostStatusKey, record.status) }}
      </a-tag>
    </template>
    <!-- 执行时间 -->
    <template #startTime="{ record }">
      <span class="table-cell-value">
        {{ (record.startTime && dateFormat(new Date(record.startTime))) || '-' }}
      </span>
      <br>
      <span class="table-cell-sub-value usn" style="font-size: 12px;">
        持续时间: {{ formatDuration(record.startTime, record.finishTime) || '-' }}
      </span>
    </template>
    <!-- 操作 -->
    <template #handle="{ record }">
      <div class="table-handle-wrapper">
        <!-- 命令 -->
        <a-button type="text"
                  size="mini"
                  @click="emits('viewCommand', record.command)">
          命令
        </a-button>
        <!-- 参数 -->
        <a-button type="text"
                  size="mini"
                  @click="emits('viewParams', record.parameter)">
          参数
        </a-button>
        <!-- 日志 -->
        <a-button type="text"
                  size="mini">
          日志
        </a-button>
        <!-- 中断 -->
        <a-popconfirm content="确认要中断命令吗?"
                      position="left"
                      type="warning"
                      @ok="interruptedHost(record)">
          <a-button v-permission="['asset:exec:interrupt-exec']"
                    type="text"
                    size="mini"
                    status="danger"
                    :disabled="record.status !== execHostStatus.WAITING && record.status !== execHostStatus.RUNNING">
            中断
          </a-button>
        </a-popconfirm>
        <!-- 删除 -->
        <a-popconfirm content="确认删除这条记录吗?"
                      position="left"
                      type="warning"
                      @ok="deleteRow(record)">
          <a-button v-permission="['asset:exec-log:delete']"
                    type="text"
                    size="mini"
                    status="danger">
            删除
          </a-button>
        </a-popconfirm>
      </div>
    </template>
  </a-table>
</template>

<script lang="ts">
  export default {
    name: 'execHostLogTable'
  };
</script>

<script lang="ts" setup>
  import type { ExecLogQueryResponse, ExecHostLogQueryResponse } from '@/api/exec/exec-log';
  import { deleteExecHostLog } from '@/api/exec/exec-log';
  import { Message } from '@arco-design/web-vue';
  import useLoading from '@/hooks/loading';
  import columns from '../types/host-table.columns';
  import { execHostStatusKey, execHostStatus } from '../types/const';
  import { useDictStore } from '@/store';
  import { useExpandable } from '@/types/table';
  import { dateFormat, formatDuration } from '@/utils';
  import { interruptHostExec } from '@/api/exec/exec';

  const props = defineProps<{
    row: ExecLogQueryResponse;
  }>();

  const emits = defineEmits(['viewCommand', 'viewParams']);

  const expandable = useExpandable({ width: 90 });
  const { loading, setLoading } = useLoading();
  const { toOptions, getDictValue } = useDictStore();

  // 中断执行
  const interruptedHost = async (record: ExecHostLogQueryResponse) => {
    try {
      setLoading(true);
      // 调用中断接口
      await interruptHostExec({
        hostLogId: record.id
      });
      Message.success('已中断');
      record.status = execHostStatus.INTERRUPTED;
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 删除当前行
  const deleteRow = async ({ id }: {
    id: number
  }) => {
    try {
      setLoading(true);
      // 调用删除接口
      await deleteExecHostLog(id);
      Message.success('删除成功');
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

</script>

<style lang="less" scoped>

</style>
