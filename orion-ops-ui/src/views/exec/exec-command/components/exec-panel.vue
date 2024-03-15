<template>
  <!-- 命令执行 -->
  <a-spin class="exec-container" :loading="loading">
    <!-- 执行参数 -->
    <div class="exec-form-container">
      <!-- 表头 -->
      <div class="exec-form-header">
        <h3>执行参数</h3>
        <!-- 操作 -->
        <a-button-group size="small">
          <a-button @click="reset">重置</a-button>
          <a-button type="primary" @click="exec">执行</a-button>
        </a-button-group>
      </div>
      <!-- 命令表单 -->
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
      <!-- 命令参数 -->
      <a-divider v-if="parameterSchema.length"
                 orientation="center"
                 style="margin: 12px 0 26px 0;">
        命令参数
      </a-divider>
      <!-- 参数表单 -->
      <a-form v-if="parameterSchema.length"
              :model="parameterFormModel"
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
    </div>
    <!-- 执行命令 -->
    <div class="exec-command-container">
      <!-- 表头 -->
      <div class="exec-form-header">
        <h3>执行命令</h3>
        <span class="span-blue usn pointer" @click="openTemplate">从模板中选择</span>
      </div>
      <!-- 命令编辑器 -->
      <div class="command-editor-wrapper">
        <exec-editor v-model="formModel.command"
                     theme="vs-dark"
                     :parameter="parameterSchema" />
      </div>
      <!-- 命名提示信息 -->
      <div v-pre class="command-editor-help">
        使用 @{{ xxx }} 来替换参数, 输入_可以获取全部变量
      </div>
    </div>
    <!-- 执行历史 -->
    <div class="exec-history-container">
      <div v-if="!historyLogs.length" class="flex-center mt16">
        <a-empty description="无执行记录" />
      </div>
      <div v-else class="exec-history-rows">
        <div v-for="record in historyLogs"
             :key="record.id"
             class="exec-history">
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
    <!-- 主机模态框 -->
    <authorized-host-modal ref="hostModal"
                           @selected="setSelectedHost" />
    <!-- 命令模板模态框 -->
    <exec-template-modal ref="templateModal"
                         @selected="setWithTemplate" />
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
  import { onMounted, ref } from 'vue';
  import formRules from '../types/form.rules';
  import useLoading from '@/hooks/loading';
  import { batchExecCommand } from '@/api/exec/exec';
  import { historyCount } from '../types/const';
  import { getExecLogHistory } from '@/api/exec/exec-log';
  import { Message } from '@arco-design/web-vue';
  import ExecEditor from '@/components/view/exec-editor/index.vue';
  import AuthorizedHostModal from '@/components/asset/host/authorized-host-modal/index.vue';
  import ExecTemplateModal from '@/components/exec/template/modal/index.vue';

  const defaultForm = (): ExecCommandRequest => {
    return {
      timeout: 0
    };
  };

  const { loading, setLoading } = useLoading();

  const hostModal = ref<any>();
  const templateModal = ref<any>();
  const formRef = ref<any>();
  const parameterFormRef = ref<any>();
  const formModel = ref<ExecCommandRequest>({ ...defaultForm() });
  const parameterFormModel = ref<Record<string, any>>({});
  const parameterSchema = ref<Array<TemplateParam>>([]);
  const historyLogs = ref<Array<ExecLogQueryResponse>>([]);

  // 加载执行记录
  const fetchExecHistory = async () => {
    const { data } = await getExecLogHistory(historyCount);
    historyLogs.value = data;
  };

  // 打开选择主机
  const openSelectHost = () => {
    hostModal.value.open(formModel.value.hostIdList);
  };

  // 打开模板
  const openTemplate = () => {
    templateModal.value.open();
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
  const setWithExecLog = () => {
    // TODO
  };

  // 执行
  const exec = async () => {
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
  const reset = () => {
    formModel.value = Object.assign({}, { ...defaultForm() });
    parameterFormModel.value = {};
    parameterSchema.value = [];
  };


  // 加载执行记录
  onMounted(fetchExecHistory);

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

    .exec-form-header {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      height: 28px;
      margin-bottom: 12px;

      h3 {
        margin: 0;
      }
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

  .exec-command-container {
    background: red;

    .command-editor-wrapper {
      width: 100%;
      height: calc(100% - 66px);
      position: relative;
    }

    .command-editor-help {
      user-select: none;
      display: flex;
      margin-top: 8px;
      height: 18px;
      color: var(--color-text-3);
    }
  }

  .exec-history-container {

    .exec-history-rows {
      overflow-y: auto;
    }

    .exec-history {
      padding: 6px 8px;
      display: flex;
      justify-content: space-between;
      margin-bottom: 8px;
      background: var(--color-fill-2);
      transition: all .2s;
      user-select: none;

      &:hover {
        background: var(--color-fill-3);
      }

      &-count {
        width: 24px;
        height: 24px;
        border-radius: 2px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: var(--color-bg-2);
        background: rgb(var(--arcoblue-6));
      }

      &-desc {

      }
    }
  }

</style>
