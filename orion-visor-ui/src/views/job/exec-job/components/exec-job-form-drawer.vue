<template>
  <a-drawer v-model:visible="visible"
            :title="title"
            width="66%"
            :esc-to-close="false"
            :mask-closable="false"
            :unmount-on-close="true"
            :ok-button-props="{ disabled: loading }"
            :cancel-button-props="{ disabled: loading }"
            :on-before-ok="handlerOk"
            @cancel="handleClose">
    <a-spin class="full modal-form-small" :loading="loading">
      <a-form :model="formModel"
              ref="formRef"
              label-align="right"
              :auto-label-width="true"
              :rules="formRules">
        <a-row :gutter="16">
          <!-- 任务名称 -->
          <a-col :span="13">
            <a-form-item field="name"
                         label="任务名称"
                         :hide-asterisk="true">
              <a-input v-model="formModel.name"
                       placeholder="请输入任务名称"
                       allow-clear />
            </a-form-item>
          </a-col>
          <!-- 执行主机 -->
          <a-col :span="11">
            <a-form-item field="hostIdList"
                         label="执行主机"
                         :hide-asterisk="true">
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
          </a-col>
          <!-- cron -->
          <a-col :span="13">
            <a-form-item field="expression"
                         label="cron"
                         :hide-asterisk="true">
              <a-input v-model="formModel.expression"
                       placeholder="请输入 cron 表达式"
                       allow-clear>
                <template #append>
                  <span class="span-blue pointer usn"
                        title="获取 cron 下次执行时间"
                        @click="emits('testCron', formModel.expression)">
                    测试
                  </span>
                </template>
              </a-input>
            </a-form-item>
          </a-col>
          <!-- 超时时间 -->
          <a-col :span="6">
            <a-form-item field="timeout"
                         label="超时时间"
                         :hide-asterisk="true">
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
          </a-col>
          <!-- 脚本执行 -->
          <a-col :span="5">
            <a-form-item field="scriptExec"
                         label="脚本执行"
                         :hide-asterisk="true">
              <div class="flex-center">
                <a-switch v-model="formModel.scriptExec"
                          type="round"
                          :checked-value="EnabledStatus.ENABLED"
                          :unchecked-value="EnabledStatus.DISABLED" />
                <div class="question-right ml8">
                  <a-tooltip position="tr" content="启用后会将命令写入脚本文件 传输到主机后执行">
                    <icon-question-circle />
                  </a-tooltip>
                </div>
              </div>
            </a-form-item>
          </a-col>
          <!-- 执行命令 -->
          <a-col :span="24">
            <a-form-item class="command-item"
                         field="command"
                         label="执行命令"
                         :hide-label="true"
                         :help="'使用 @{{ xxx }} 来替换参数, 输入_可以获取全部变量'">
              <template #extra>
                <span v-permission="['asset:exec-template:query']"
                      class="span-blue usn pointer"
                      @click="emits('openTemplate')">
                  从模板中选择
                </span>
              </template>
              <!-- 命令框 -->
              <exec-editor v-model="formModel.command"
                           container-class="command-editor"
                           theme="vs-dark"
                           :parameter="jobBuiltinsParams" />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-spin>
  </a-drawer>
</template>

<script lang="ts">
  export default {
    name: 'execJobFormDrawer'
  };
</script>

<script lang="ts" setup>
  import type { ExecJobUpdateRequest } from '@/api/job/exec-job';
  import type { ExecTemplateQueryResponse } from '@/api/exec/exec-template';
  import { onUnmounted, ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import formRules from '../types/form.rules';
  import { jobBuiltinsParams } from '../types/const';
  import { createExecJob, getExecJob, updateExecJob } from '@/api/job/exec-job';
  import { getExecTemplateWithAuthorized } from '@/api/exec/exec-template';
  import { Message } from '@arco-design/web-vue';
  import { EnabledStatus } from '@/types/const';
  import { useDictStore } from '@/store';
  import ExecEditor from '@/components/view/exec-editor/index.vue';

  const emits = defineEmits(['added', 'updated', 'openHost', 'openTemplate', 'testCron']);

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();
  const { toOptions } = useDictStore();

  const title = ref<string>();
  const isAddHandle = ref<boolean>(true);
  const formRef = ref<any>();
  const formModel = ref<ExecJobUpdateRequest>({});

  const defaultForm = (): ExecJobUpdateRequest => {
    return {
      id: undefined,
      name: undefined,
      expression: undefined,
      timeout: 0,
      scriptExec: EnabledStatus.DISABLED,
      command: undefined,
      parameterSchema: '[]',
      hostIdList: []
    };
  };

  // 打开新增
  const openAdd = () => {
    title.value = '添加计划任务';
    isAddHandle.value = true;
    renderForm({ ...defaultForm() });
    setVisible(true);
  };

  // 打开修改
  const openUpdate = async (id: any) => {
    title.value = '修改计划任务';
    isAddHandle.value = false;
    renderForm({});
    setVisible(true);
    // 查询计划任务
    try {
      setLoading(true);
      const { data } = await getExecJob(id);
      renderForm({ ...defaultForm(), ...data });
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 渲染表单
  const renderForm = (record: ExecJobUpdateRequest) => {
    formModel.value = {
      id: record.id,
      name: record.name,
      expression: record.expression,
      command: record.command,
      timeout: record.timeout,
      scriptExec: record.scriptExec,
      parameterSchema: record.parameterSchema,
      hostIdList: record.hostIdList,
    };
  };

  // 设置选中主机
  const setSelectedHost = (hosts: Array<number>) => {
    formModel.value.hostIdList = hosts;
  };

  // 通过模板设置
  const setWithTemplate = async ({ id }: ExecTemplateQueryResponse) => {
    setLoading(true);
    try {
      // 查询模板信息
      const { data } = await getExecTemplateWithAuthorized(id);
      formModel.value = {
        ...formModel.value,
        name: data.name,
        command: data.command,
        timeout: data.timeout,
        scriptExec: data.scriptExec,
        parameterSchema: data.parameterSchema,
        hostIdList: data.hostIdList,
      };
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  defineExpose({ openAdd, openUpdate, setSelectedHost, setWithTemplate });

  // 打开选择主机
  const openSelectHost = () => {
    emits('openHost', formModel.value.hostIdList);
  };

  // 确定
  const handlerOk = async () => {
    setLoading(true);
    try {
      // 验证参数
      const error = await formRef.value.validate();
      if (error) {
        return false;
      }
      if (isAddHandle.value) {
        // 新增
        await createExecJob(formModel.value);
        Message.success('创建成功');
        emits('added');
      } else {
        // 修改
        await updateExecJob(formModel.value);
        Message.success('修改成功');
        emits('updated');
      }
      // 清空
      handlerClear();
    } catch (e) {
      return false;
    } finally {
      setLoading(false);
    }
  };

  // 关闭
  const handleClose = () => {
    handlerClear();
  };

  // 清空
  const handlerClear = () => {
    setLoading(false);
  };

  // 卸载关闭
  onUnmounted(handlerClear);

</script>

<style lang="less" scoped>

  .selected-host {
    width: 100%;
    height: 32px;
    padding: 0 8px;
    border-radius: 2px;
    display: flex;
    align-items: center;
    justify-content: space-between;
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

  .command-item {
    :deep(.arco-form-item-extra) {
      margin-top: -18px;
      width: 100%;
      text-align: end;
    }
  }

  .command-editor {
    width: 100%;
    height: calc(100vh - 264px);
  }

</style>
