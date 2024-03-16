<template>
  <!-- 命令执行 -->
  <a-spin class="exec-container" :loading="loading">
    <!-- 执行参数 -->
    <exec-panel-form class="exec-form-container"
                     :schema-count="parameterSchema.length"
                     @exec="execCommand"
                     @reset="resetForm">
      <!-- 命令表单 -->
      <template #form>
        <a-form :model="formModel"
                ref="formRef"
                label-align="right"
                :rules="formRules">
          <!-- 执行主机 -->
          <a-form-item field="hostIdList"
                       label="执行主机"
                       label-col-flex="72px">
            <div class="selected-host">
              <!-- 已选择数量 -->
              <span class="usn" v-if="formModel.hostIdList?.length">
                已选择<span class="selected-host-count span-blue">{{ formModel.hostIdList?.length }}</span>台主机
              </span>
              <span class="usn pointer span-blue" @click="openSelectHost">
                {{ formModel.hostIdList?.length ? '重新选择' : '选择主机' }}
              </span>
            </div>
          </a-form-item>
          <!-- 执行描述 -->
          <a-form-item field="description"
                       label="执行描述"
                       label-col-flex="72px">
            <a-input v-model="formModel.description"
                     placeholder="请输入执行描述"
                     allow-clear />
          </a-form-item>
          <!-- 超时时间 -->
          <a-form-item field="timeout"
                       label="超时时间"
                       label-col-flex="72px">
            <a-input-number v-model="formModel.timeout"
                            placeholder="为0则不超时"
                            :min="0"
                            :max="100000"
                            hide-button>
              <template #suffix>
                秒
              </template>
            </a-input-number>
          </a-form-item>
        </a-form>
      </template>
      <!-- 参数表单 -->
      <template #params>
        <a-form :model="parameterFormModel"
                ref="parameterFormRef"
                label-align="right">
          <a-form-item v-for="item in parameterSchema"
                       :key="item.name"
                       :field="item.name as string"
                       :label="item.name"
                       label-col-flex="72px"
                       required>
            <a-input v-model="parameterFormModel[item.name as string]"
                     :placeholder="item.desc"
                     allow-clear />
          </a-form-item>
        </a-form>
      </template>
    </exec-panel-form>
    <!-- 执行命令 -->
    <exec-panel-editor class="exec-command-container"
                       @selected="setWithTemplate">
      <exec-editor v-model="formModel.command"
                   theme="vs-dark"
                   :parameter="parameterSchema" />
    </exec-panel-editor>
    <!-- 执行历史 -->
    <exec-panel-history class="exec-history-container"
                        @selected="setWithExecLog" />
    <!-- 主机模态框 -->
    <authorized-host-modal ref="hostModal"
                           @selected="setSelectedHost" />
  </a-spin>
</template>

<script lang="ts">
  export default {
    name: 'execPanel'
  };
</script>

<script lang="ts" setup>
  import type { ExecCommandRequest } from '@/api/exec/exec';
  import type { TemplateParam } from '@/components/view/exec-editor/const';
  import type { ExecTemplateQueryResponse } from '@/api/exec/exec-template';
  import type { ExecLogQueryResponse } from '@/api/exec/exec-log';
  import { ref } from 'vue';
  import formRules from '../types/form.rules';
  import useLoading from '@/hooks/loading';
  import { batchExecCommand } from '@/api/exec/exec';
  import { Message } from '@arco-design/web-vue';
  import ExecEditor from '@/components/view/exec-editor/index.vue';
  import AuthorizedHostModal from '@/components/asset/host/authorized-host-modal/index.vue';
  import ExecPanelForm from './exec-panel-form.vue';
  import ExecPanelHistory from './exec-panel-history.vue';
  import ExecPanelEditor from './exec-panel-editor.vue';

  const defaultForm = (): ExecCommandRequest => {
    return {
      timeout: 0,
      command: ''
    };
  };

  const { loading, setLoading } = useLoading();

  const hostModal = ref<any>();
  const formRef = ref<any>();
  const parameterFormRef = ref<any>();
  const formModel = ref<ExecCommandRequest>({ ...defaultForm() });
  const parameterFormModel = ref<Record<string, any>>({});
  const parameterSchema = ref<Array<TemplateParam>>([]);

  // 打开选择主机
  const openSelectHost = () => {
    hostModal.value.open(formModel.value.hostIdList);
  };

  // 设置选中主机
  const setSelectedHost = (hosts: Array<number>) => {
    formModel.value.hostIdList = hosts;
  };

  // 从执行模板设置
  const setWithTemplate = (record: ExecTemplateQueryResponse) => {
    formModel.value = {
      ...formModel.value,
      command: record.command,
      description: record.name,
      timeout: record.timeout,
    };
    parameterSchema.value = record.parameter ? JSON.parse(record.parameter) : [];
    parameterFormModel.value = {};
  };

  // 从执行日志设置
  const setWithExecLog = (record: ExecLogQueryResponse) => {
    formModel.value = {
      ...formModel.value,
      command: record.command,
      description: record.description,
      timeout: record.timeout,
      hostIdList: record.hostIdList
    };
    parameterSchema.value = record.parameterSchema ? JSON.parse(record.parameterSchema) : [];
    if (parameterSchema.value.length) {
      parameterFormModel.value = parameterSchema.value.reduce((acc, cur) => ({
        ...acc,
        [cur.name as string]: cur.value
      }), {});
    } else {
      parameterFormModel.value = {};
    }
  };

  // 执行
  const execCommand = async () => {
    setLoading(true);
    try {
      // 验证参数
      let error = await formRef.value.validate();
      if (error) {
        return false;
      }
      error = await parameterFormRef.value?.validate();
      if (error) {
        return false;
      }
      if (!formModel.value.command) {
        Message.error('请输入命令');
        return false;
      }
      // 设置 schema
      for (let ps of parameterSchema.value) {
        ps.value = parameterFormModel.value[ps.name as string];
      }
      // 执行命令
      const { data } = await batchExecCommand({
        ...formModel.value,
        parameterSchema: JSON.stringify(parameterSchema.value),
      });
      // TODO log history
    } catch (e) {
      return false;
    } finally {
      setLoading(false);
    }
  };

  // 重置
  const resetForm = () => {
    formModel.value = Object.assign({}, { ...defaultForm() });
    parameterFormModel.value = {};
    parameterSchema.value = [];
  };

</script>

<style lang="less" scoped>
  @form-width: 420px;
  @history-width: 320px;
  @command-gap: @form-width + @history-width + 32px;

  .exec-container {
    display: flex;
    justify-content: space-between;
    width: 100%;
    height: 100%;

    .exec-form-container {
      width: @form-width;
    }

    .exec-command-container {
      width: calc(100% - @command-gap);
    }

    .exec-history-container {
      width: @history-width;
    }

    .exec-form-container, .exec-command-container, .exec-history-container {
      background: var(--color-bg-2);
      border-radius: 4px;
      height: 100%;
      padding: 16px;
      position: relative;
    }
  }

  :deep(.exec-header) {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    height: 28px;
    margin-bottom: 4px;

    h3, > span {
      margin: 0;
      overflow: hidden;
      white-space: nowrap;
    }

    h3 {
      color: var(--color-text-1);
    }
  }

  .exec-form-container {
    .selected-host {
      width: 100%;
      display: flex;
      align-items: center;
      justify-content: space-between;

      &-count {
        font-size: 16px;
        font-weight: 600;
        display: inline-block;
        margin: 0 6px;
      }
    }
  }

</style>
