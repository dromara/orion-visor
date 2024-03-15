<template>
  <a-drawer v-model:visible="visible"
            :title="title"
            :width="470"
            :mask-closable="false"
            :unmount-on-close="true"
            :ok-button-props="{ disabled: loading }"
            :cancel-button-props="{ disabled: loading }"
            :on-before-ok="handlerOk"
            @cancel="handleClose">
    <a-spin class="full modal-form" :loading="loading">
      <a-form :model="formModel"
              ref="formRef"
              :rules="formRules">
        <!-- 模板名称 -->
        <a-form-item field="name" label="模板名称">
          <a-input v-model="formModel.name"
                   placeholder="请输入模板名称"
                   allow-clear />
        </a-form-item>
        <!-- 超时时间 -->
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
        <!-- 模板命令 -->
        <a-form-item field="command"
                     label="模板命令"
                     :wrapper-col-props="{ span: 24 }"
                     :help="'使用 @{{ xxx }} 来替换参数, 输入_可以获取全部变量'">
          <exec-editor v-model="formModel.command"
                       containerClass="command-editor"
                       theme="vs-dark"
                       :parameter="parameter" />
        </a-form-item>
        <!-- 命令参数 -->
        <a-form-item field="parameter"
                     class="parameter-form-item"
                     label="命令参数"
                     :label-col-props="{ span: 24 }"
                     :wrapper-col-props="{ span: 24 }">
          <!-- label -->
          <template #label>
            <div class="parameter-label-wrapper">
              <span>命令参数</span>
              <span class="span-blue pointer" @click="addParameter">添加参数</span>
            </div>
          </template>
          <!-- 参数 -->
          <template v-if="parameter.length">
            <a-input-group v-for="(item, i) in parameter"
                           :key="i"
                           class="parameter-item"
                           :class="[ i === parameter.length - 1 ? 'parameter-item-last' : '' ]">
              <a-input class="parameter-item-name"
                       v-model="item.name"
                       placeholder="参数名称 (必填)"
                       allow-clear />
              <a-input class="parameter-item-default"
                       v-model="item.defaultValue"
                       placeholder="默认值 (非必填)"
                       allow-clear />
              <a-input class="parameter-item-description"
                       v-model="item.desc"
                       placeholder="描述 (非必填)"
                       allow-clear />
              <span class="parameter-item-close click-icon-wrapper"
                    title="移除"
                    @click="removeParameter(i)">
               <icon-close />
              </span>
            </a-input-group>
          </template>
          <!-- 无参数 -->
          <template v-else>
            <span class="no-parameter">
              <icon-empty class="mr4" />无参数
            </span>
          </template>
        </a-form-item>
      </a-form>
    </a-spin>
  </a-drawer>
</template>

<script lang="ts">
  export default {
    name: 'execTemplateFormDrawer'
  };
</script>

<script lang="ts" setup>
  import type { TemplateParam } from '@/components/view/exec-editor/const';
  import type { ExecTemplateUpdateRequest } from '@/api/exec/exec-template';
  import { onUnmounted, ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import formRules from '../types/form.rules';
  import { createExecTemplate, updateExecTemplate } from '@/api/exec/exec-template';
  import { Message } from '@arco-design/web-vue';
  import ExecEditor from '@/components/view/exec-editor/index.vue';

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const title = ref<string>();
  const isAddHandle = ref<boolean>(true);

  const defaultForm = (): ExecTemplateUpdateRequest => {
    return {
      id: undefined,
      name: undefined,
      command: undefined,
      timeout: 0,
      parameter: undefined,
    };
  };

  const formRef = ref<any>();
  const formModel = ref<ExecTemplateUpdateRequest>({});
  const parameter = ref<Array<TemplateParam>>([]);

  const emits = defineEmits(['added', 'updated']);

  // 打开新增
  const openAdd = () => {
    title.value = '添加执行模板';
    isAddHandle.value = true;
    renderForm({ ...defaultForm() });
    setVisible(true);
  };

  // 打开修改
  const openUpdate = (record: any) => {
    title.value = '修改执行模板';
    isAddHandle.value = false;
    renderForm({ ...defaultForm(), ...record });
    setVisible(true);
  };

  // 渲染表单
  const renderForm = (record: any) => {
    formModel.value = Object.assign({}, record);
    if (record.parameter) {
      parameter.value = JSON.parse(record.parameter);
    } else {
      parameter.value = [];
    }
  };

  defineExpose({ openAdd, openUpdate });

  // 添加参数
  const addParameter = () => {
    parameter.value.push({});
  };

  // 移除参数
  const removeParameter = (index: number) => {
    parameter.value.splice(index, 1);
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
      // 验证并设置命令参数
      for (const p of parameter.value) {
        if (!p.name) {
          Message.warning('请补全命令参数');
          return false;
        }
      }
      if (parameter.value.length) {
        formModel.value.parameter = JSON.stringify(parameter.value);
      }
      if (isAddHandle.value) {
        // 新增
        await createExecTemplate(formModel.value);
        Message.success('创建成功');
        emits('added');
      } else {
        // 修改
        await updateExecTemplate(formModel.value);
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
  .parameter-form-item {
    user-select: none;

    :deep(.arco-form-item-label) {
      width: 100%;
    }

    .parameter-label-wrapper {
      display: flex;
      align-items: center;
      justify-content: space-between;
    }

    :deep(.arco-form-item-content) {
      flex-direction: column;
    }

    .parameter-item-last {
      margin-bottom: 0 !important;
    }

    .parameter-item {
      margin-bottom: 12px;
      display: flex;
      justify-content: space-between;

      & > span {
        border-radius: 2px;
        border-right-color: transparent;
      }

      &-name {
        width: 29%;
      }

      &-default {
        width: 29%;
      }

      &-description {
        width: calc(39% - 44px);
      }

      &-close {
        cursor: pointer;
        width: 32px;
        height: 32px;
        font-size: 16px;
        background: var(--color-fill-2);
        display: flex;
        align-items: center;
        justify-content: center;

        &:hover {
          background: var(--color-fill-3);
        }
      }
    }

    .no-parameter {
      background: var(--color-fill-2);
      width: 100%;
      height: 32px;
      border-radius: 2px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: var(--color-text-2);
    }
  }

  .command-editor {
    width: 100%;
    height: 50vh;
  }

</style>
