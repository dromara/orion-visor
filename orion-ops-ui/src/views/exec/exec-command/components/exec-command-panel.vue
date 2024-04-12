<template>
  <!-- 命令执行 -->
  <a-spin class="exec-container" :loading="loading">
    <!-- 执行参数 -->
    <exec-command-panel-form class="exec-form-container"
                             :schema-count="parameterSchema.length"
                             @exec="execCommand"
                             @reset="resetForm">
      <!-- 命令表单 -->
      <template #form>
        <a-form :model="formModel"
                ref="formRef"
                class="form-wrapper"
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
    </exec-command-panel-form>
    <!-- 执行命令 -->
    <exec-command-panel-editor class="exec-command-container"
                               @open-template="() => templateModal.open()">
      <exec-editor v-model="formModel.command"
                   theme="vs-dark"
                   :parameter="parameterSchema" />
    </exec-command-panel-editor>
    <!-- 执行历史 -->
    <exec-command-panel-history class="exec-history-container"
                                ref="historyRef"
                                @selected="setWithExecLog" />
    <!-- 命令模板模态框 -->
    <exec-template-modal ref="templateModal"
                         @selected="setWithTemplate" />
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
  import type { ExecCommandRequest } from '@/api/exec/exec-command';
  import type { TemplateParam } from '@/components/view/exec-editor/const';
  import type { ExecTemplateQueryResponse } from '@/api/exec/exec-template';
  import type { ExecCommandLogQueryResponse } from '@/api/exec/exec-command-log';
  import { ref } from 'vue';
  import formRules from '../types/form.rules';
  import useLoading from '@/hooks/loading';
  import { batchExecCommand } from '@/api/exec/exec-command';
  import { Message } from '@arco-design/web-vue';
  import ExecEditor from '@/components/view/exec-editor/index.vue';
  import AuthorizedHostModal from '@/components/asset/host/authorized-host-modal/index.vue';
  import ExecCommandPanelForm from './exec-command-panel-form.vue';
  import ExecCommandPanelHistory from './exec-command-panel-history.vue';
  import ExecCommandPanelEditor from './exec-command-panel-editor.vue';
  import ExecTemplateModal from '@/components/exec/template/modal/index.vue';

  const emits = defineEmits(['submit']);

  const defaultForm = (): ExecCommandRequest => {
    return {
      command: '',
      timeout: 0,
    };
  };

  const { loading, setLoading } = useLoading();

  const hostModal = ref<any>();
  const historyRef = ref<any>();
  const formRef = ref<any>();
  const templateModal = ref<any>();
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

  // 从执行日志设置
  const setWithExecLog = (record: ExecCommandLogQueryResponse) => {
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
      const request = {
        ...formModel.value,
        parameterSchema: JSON.stringify(parameterSchema.value),
      };
      const { data } = await batchExecCommand(request);
      // 设置历史命令
      historyRef.value.add(request);
      Message.success('已开始执行');
      emits('submit', data);
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
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: space-between;
    position: absolute;

    .exec-form-container {
      width: @form-width;
    }

    .exec-command-container {
      width: calc(100% - @command-gap);
      margin: 0 16px;
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

  .exec-form-container {

    .form-wrapper {
      margin-top: 8px;
    }

    .selected-host {
      width: 100%;
      height: 32px;
      padding: 0 8px;
      border-radius: 2px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      color: var(--color-text-2);
      background: var(--color-fill-2);
      transition: all 0.3s;

      &-count {
        font-size: 16px;
        font-weight: 600;
        display: inline-block;
        margin: 0 6px;
      }

      &:hover {
        background: var(--color-fill-3);
      }
    }
  }

  :deep(.panel-header) {
    width: 100%;
    height: 28px;
    margin-bottom: 4px;
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

</style>
