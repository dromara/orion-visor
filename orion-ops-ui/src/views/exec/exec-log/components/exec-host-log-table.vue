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
    <!-- 执行状态 -->
    <template #status="{ record }">
      {{ getDictValue(execHostStatusKey, record.status) }}
    </template>
    <!-- 操作 -->
    <template #handle="{ record }">
      <div class="table-handle-wrapper">
        <!-- 删除 -->
        <a-popconfirm content="确认删除这条记录吗?"
                      position="left"
                      type="warning"
                      @ok="deleteRow(record)">
          <a-button v-permission="['asset:exec-host-log:delete']"
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
  import type { ExecLogQueryResponse } from '@/api/exec/exec-log';
  import { reactive, ref, onMounted } from 'vue';
  import { deleteExecHostLog } from '@/api/exec/exec-host-log';
  import { Message } from '@arco-design/web-vue';
  import useLoading from '@/hooks/loading';
  import columns from '../types/table.columns';
  import { execHostStatusKey } from '../types/const';
  import { useDictStore } from '@/store';
  import { useExpandable } from '@/types/table';

  const props = defineProps<{
    row: ExecLogQueryResponse;
  }>();

  const expandable = useExpandable();
  const { loading, setLoading } = useLoading();
  const { toOptions, getDictValue } = useDictStore();

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
