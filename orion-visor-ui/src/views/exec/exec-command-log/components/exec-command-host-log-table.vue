<template>
  <!-- table -->
  <a-table row-key="id"
           ref="tableRef"
           class="table-resize"
           :loading="loading"
           :columns="hostColumns"
           :data="row.hosts"
           :expandable="expandable"
           :scroll="{ y: '100%' }"
           :pagination="false"
           :bordered="false"
           :column-resizable="true">
    <!-- 执行主机 -->
    <template #hostName="{ record }">
      <span class="table-cell-value span-blue">
        {{ record.hostName }}
      </span>
      <br>
      <span class="table-cell-sub-value usn text-copy"
            style="font-size: 12px;"
            @click="copy(record.hostAddress)">
        {{ record.hostAddress }}
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
        <a-button v-permission="['exec:exec-command-log:query']"
                  type="text"
                  size="mini"
                  :disabled="record.status === ExecHostStatus.WAITING"
                  :title="record.status === ExecHostStatus.WAITING ? '命令正在等待执行' : '查看命令'"
                  @click="doViewCommand(record)">
          命令
        </a-button>
        <!-- 参数 -->
        <a-button v-permission="['exec:exec-command-log:query']"
                  type="text"
                  size="mini"
                  :disabled="record.status === ExecHostStatus.WAITING"
                  :title="record.status === ExecHostStatus.WAITING ? '命令正在等待执行' : '查看参数'"
                  @click="doViewParams(record)">
          参数
        </a-button>
        <!-- 下载 -->
        <a-button type="text"
                  size="mini"
                  @click="downloadLogFile(record.id)">
          下载
        </a-button>
        <!-- 中断 -->
        <a-popconfirm content="确认要中断命令吗, 删除后会中断执行?"
                      position="left"
                      type="warning"
                      @ok="interruptedHost(record)">
          <a-button v-permission="['exec:exec-command-log:interrupt']"
                    type="text"
                    size="mini"
                    status="danger"
                    :disabled="record.status !== ExecHostStatus.WAITING && record.status !== ExecHostStatus.RUNNING">
            中断
          </a-button>
        </a-popconfirm>
        <!-- 删除 -->
        <a-popconfirm content="确认删除这条记录吗?"
                      position="left"
                      type="warning"
                      @ok="deleteRow(record)">
          <a-button v-permission="['exec:exec-command-log:delete']"
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
    name: 'execCommandHostLogTable'
  };
</script>

<script lang="ts" setup>
  import type { ExecLogQueryResponse, ExecHostLogQueryResponse } from '@/api/exec/exec-log';
  import { deleteExecCommandHostLog, getExecCommandHostLog } from '@/api/exec/exec-command-log';
  import { Message } from '@arco-design/web-vue';
  import useLoading from '@/hooks/loading';
  import { hostColumns } from '../types/table.columns';
  import { execHostStatusKey, ExecHostStatus } from '@/components/exec/log/const';
  import { useDictStore } from '@/store';
  import { useExpandable } from '@/hooks/table';
  import { dateFormat, formatDuration } from '@/utils';
  import { downloadExecCommandLogFile, interruptHostExecCommand } from '@/api/exec/exec-command-log';
  import { copy } from '@/hooks/copy';
  import { downloadFile } from '@/utils/file';

  const props = defineProps<{
    row: ExecLogQueryResponse;
  }>();

  const emits = defineEmits(['viewCommand', 'viewParams', 'refreshHost']);

  const expandable = useExpandable({ width: 90 });
  const { loading, setLoading } = useLoading();
  const { toOptions, getDictValue } = useDictStore();

  // 查看命令
  const doViewCommand = async (record: ExecHostLogQueryResponse) => {
    // 刷新记录
    if (!record.refreshed) {
      await refreshRecord(record);
    }
    emits('viewCommand', record.command);
  };

  // 查看参数
  const doViewParams = async (record: ExecHostLogQueryResponse) => {
    // 刷新记录
    if (!record.refreshed) {
      await refreshRecord(record);
    }
    emits('viewParams', record.parameter);
  };

  // 刷新记录
  const refreshRecord = async (record: ExecHostLogQueryResponse) => {
    try {
      setLoading(true);
      const { data } = await getExecCommandHostLog(record.id);
      record.status = data.status;
      record.command = data.command;
      record.parameter = data.parameter;
      record.exitCode = data.exitCode;
      record.errorMessage = data.errorMessage;
      record.startTime = data.startTime;
      record.finishTime = data.finishTime;
      record.refreshed = true;
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 下载文件
  const downloadLogFile = async (id: number) => {
    const data = await downloadExecCommandLogFile(id);
    downloadFile(data);
  };

  // 中断执行
  const interruptedHost = async (record: ExecHostLogQueryResponse) => {
    try {
      setLoading(true);
      // 调用中断接口
      await interruptHostExecCommand({
        hostLogId: record.id
      });
      Message.success('已中断');
      record.status = ExecHostStatus.INTERRUPTED;
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 删除当前行
  const deleteRow = async ({ id, logId }: ExecHostLogQueryResponse) => {
    try {
      setLoading(true);
      // 调用删除接口
      await deleteExecCommandHostLog(id);
      Message.success('删除成功');
      emits('refreshHost', logId);
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

</script>

<style lang="less" scoped>

</style>
