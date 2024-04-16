<template>
  <a-drawer v-model:visible="visible"
            title="执行命令"
            width="60%"
            :mask-closable="false"
            :unmount-on-close="true"
            :ok-button-props="{ disabled: loading }"
            :cancel-button-props="{ disabled: loading }"
            :on-before-ok="handlerOk"
            @cancel="handleClose">
    <a-spin class="full spin-wrapper" :loading="loading">
      <!-- 命令表单 -->
      <a-form :model="formModel"
              ref="formRef"
              label-align="right"
              :auto-label-width="true"
              :rules="formRules">
        <a-row :gutter="16">
          <!-- 执行描述 -->
          <a-col :span="16">
            <a-form-item field="description" label="执行描述">
              <a-input v-model="formModel.description"
                       placeholder="请输入执行描述"
                       allow-clear />
            </a-form-item>
          </a-col>
          <!-- 超时时间 -->
          <a-col :span="7">
            <a-form-item field="timeout"
                         label="超时时间">
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
          <!-- 执行主机 -->
          <a-col :span="16">
            <a-form-item field="hostIdList" label="执行主机">
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
          <!-- 脚本执行 -->
          <a-col :span="8">
            <a-form-item field="scriptExec" label="脚本执行">
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
            <a-form-item field="command"
                         label="执行命令"
                         :hide-label="true"
                         :wrapper-col-props="{ span: 24 }"
                         :help="'使用 @{{ xxx }} 来替换参数, 输入_可以获取全部变量'">
              <exec-editor v-model="formModel.command"
                           container-class="command-editor"
                           theme="vs-dark"
                           :parameter="parameterSchema" />
            </a-form-item>
          </a-col>
        </a-row>
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
              label-align="left"
              :auto-label-width="true">
        <a-row :gutter="16">
          <a-col v-for="item in parameterSchema"
                 :key="item.name"
                 :span="12">
            <a-form-item :field="item.name as string"
                         :label="item.name"
                         required>
              <a-input v-model="parameterFormModel[item.name as string]"
                       :placeholder="item.desc"
                       allow-clear />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-spin>
  </a-drawer>
</template>

<script lang="ts">
  export default {
    name: 'execTemplateExecDrawer'
  };
</script>

<script lang="ts" setup>
  import type { TemplateParam } from '@/components/view/exec-editor/const';
  import type { ExecTemplateQueryResponse } from '@/api/exec/exec-template';
  import type { ExecCommandRequest } from '@/api/exec/exec-command';
  import { onUnmounted, ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import formRules from '../../exec-command/types/form.rules';
  import { Message } from '@arco-design/web-vue';
  import { EnabledStatus } from '@/types/const';
  import { batchExecCommand } from '@/api/exec/exec-command';
  import ExecEditor from '@/components/view/exec-editor/index.vue';

  const emits = defineEmits(['openHost']);

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const formRef = ref<any>();
  const parameterFormRef = ref<any>();
  const formModel = ref<ExecCommandRequest>({});
  const parameterFormModel = ref<Record<string, any>>({});
  const parameterSchema = ref<Array<TemplateParam>>([]);

  // 打开
  const open = (record: ExecTemplateQueryResponse) => {
    renderForm({ ...record });
    setVisible(true);
  };

  // 渲染表单
  const renderForm = (record: ExecTemplateQueryResponse) => {
    formModel.value = {
      description: record.name,
      timeout: record.timeout,
      scriptExec: record.scriptExec,
      command: record.command,
      hostIdList: []
    };
    if (record.parameterSchema) {
      parameterSchema.value = JSON.parse(record.parameterSchema);
      const params = {} as any;
      for (let param of parameterSchema.value) {
        params[param.name as keyof any] = param.defaultValue;
      }
      parameterFormModel.value = params;

    } else {
      parameterSchema.value = [];
      parameterFormModel.value = {};
    }
  };

  // 设置选中主机
  const setSelectedHost = (hosts: Array<number>) => {
    formModel.value.hostIdList = hosts;
  };

  defineExpose({ open, setSelectedHost });

  // 打开选择主机
  const openSelectHost = () => {
    emits('openHost', formModel.value.hostIdList);
  };

  // 确定
  const handlerOk = async () => {
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
      // 设置 schema
      for (let ps of parameterSchema.value) {
        ps.value = parameterFormModel.value[ps.name as string];
      }
      // 执行命令
      await batchExecCommand({
        ...formModel.value,
        parameterSchema: JSON.stringify(parameterSchema.value),
      });
      Message.success('已开始执行');
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
  .spin-wrapper {
    padding: 16px 20px;
  }

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

  .command-editor {
    width: 100%;
    height: 65vh;
  }

</style>
