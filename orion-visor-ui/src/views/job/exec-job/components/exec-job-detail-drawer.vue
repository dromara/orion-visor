<template>
  <a-drawer v-model:visible="visible"
            title="计划任务详情"
            width="70%"
            :mask-closable="false"
            :unmount-on-close="true"
            ok-text="关闭"
            :hide-cancel="true"
            @cancel="handleClose">
    <a-descriptions class="detail-container"
                    size="large"
                    table-layout="fixed"
                    :label-style="{ width: '90px' }"
                    :column="2">
      <!-- 任务id -->
      <a-descriptions-item label="任务id">
        <span class="text-copy"
              title="复制"
              @click="copy(record.id+'')">
          {{ record.id }}
        </span>
      </a-descriptions-item>
      <!-- 任务名称 -->
      <a-descriptions-item label="任务名称">
        {{ record.name }}
      </a-descriptions-item>
      <!-- cron -->
      <a-descriptions-item label="cron">
        <span class="text-copy"
              title="复制"
              @click="copy(record.expression)">
         {{ record.expression }}
        </span>
      </a-descriptions-item>
      <!-- 超时时间 -->
      <a-descriptions-item label="超时时间">
        {{ record.timeout }} 秒
      </a-descriptions-item>
      <!-- 任务状态 -->
      <a-descriptions-item label="任务状态">
        <a-tag :color="getDictValue(execJobStatusKey, record.status, 'color')">
          {{ getDictValue(execJobStatusKey, record.status) }}
        </a-tag>
      </a-descriptions-item>
      <!-- 脚本执行 -->
      <a-descriptions-item label="脚本执行">
        {{ record.scriptExec === EnabledStatus.ENABLED ? '是' : '否' }}
      </a-descriptions-item>
      <!-- 创建时间 -->
      <a-descriptions-item label="创建时间">
        {{ dateFormat(new Date(record.createTime)) }}
      </a-descriptions-item>
      <!-- 修改时间 -->
      <a-descriptions-item label="修改时间">
        {{ dateFormat(new Date(record.updateTime)) }}
      </a-descriptions-item>
      <!-- 执行主机 -->
      <a-descriptions-item :span="3">
        <template #label>
          <span class="host-label">执行主机</span>
        </template>
        <a-space wrap>
          <a-tag v-for="host in record.hostList"
                 :key="host.id">
            {{ host.name }}
          </a-tag>
        </a-space>
      </a-descriptions-item>
      <!-- 执行命令 -->
      <a-descriptions-item label="执行命令" :span="3">
        <editor v-model="record.command"
                language="shell"
                theme="vs-dark"
                container-class="command-editor"
                :readonly="true"
                :suggestions="false" />
      </a-descriptions-item>
      <!-- 执行参数 -->
      <a-descriptions-item v-if="record.parameterSchema"
                           label="执行参数"
                           :span="3">
        <editor v-model="record.parameterSchema"
                language="json"
                theme="vs-dark"
                container-class="json-editor"
                :readonly="true"
                :suggestions="false" />
      </a-descriptions-item>
    </a-descriptions>
  </a-drawer>
</template>

<script lang="ts">
  export default {
    name: 'execJobDetailDrawer'
  };
</script>

<script lang="ts" setup>
  import type { ExecJobQueryResponse } from '@/api/job/exec-job';
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import { useDictStore } from '@/store';
  import { dateFormat } from '@/utils';
  import { copy } from '@/hooks/copy';
  import { getExecJob } from '@/api/job/exec-job';
  import { EnabledStatus } from '@/types/const';
  import { execJobStatusKey } from '../types/const';

  const { getDictValue, toOptions } = useDictStore();
  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const record = ref<ExecJobQueryResponse>({} as ExecJobQueryResponse);

  // 打开
  const open = async (id: any) => {
    try {
      // 查询计划任务
      setLoading(true);
      const { data } = await getExecJob(id);
      record.value = data;
      // 设置参数值
      if (data.parameterSchema) {
        const value = JSON.parse(data.parameterSchema);
        if (value?.length) {
          data.parameterSchema = JSON.stringify(value, undefined, 4);
        } else {
          data.parameterSchema = undefined as unknown as string;
        }
      }
      setVisible(true);
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  defineExpose({ open });

  // 关闭
  const handleClose = () => {
    handlerClear();
  };

  // 清空
  const handlerClear = () => {
    setLoading(false);
  };

</script>

<style lang="less" scoped>
  .detail-container {
    padding: 24px 8px 24px 24px;
  }

  :deep(.arco-descriptions-item-value) {
    color: var(--color-text-1);
  }

  .host-label {
    margin-top: -8px;
    display: block;
  }

  .command-editor {
    width: 100%;
    height: calc(100vh - 396px);
  }

  .json-editor {
    width: 100%;
    height: 328px;
  }

</style>
