<template>
  <a-modal v-model:visible="visible"
           body-class="modal-form"
           title-align="start"
           :title="title"
           :top="30"
           :align-center="false"
           :draggable="true"
           :mask-closable="false"
           :unmount-on-close="true"
           :ok-button-props="{ disabled: loading }"
           :cancel-button-props="{ disabled: loading }"
           :on-before-ok="handlerOk"
           @close="handleClose">
    <a-spin class="full" :loading="loading">
      <a-form :model="formModel"
              ref="formRef"
              label-align="right"
              :style="{ width: '460px' }"
              :label-col-props="{ span: 7 }"
              :wrapper-col-props="{ span: 17 }"
              :rules="formRules">
        <!-- 上级菜单 -->
        <a-form-item field="parentId" label="上级菜单">
          <menu-tree-selector v-model:model-value="formModel.parentId as number"
                              :disabled="formModel.type === MenuType.PARENT_MENU" />
        </a-form-item>
        <!-- 菜单名称 -->
        <a-form-item field="name" label="菜单名称">
          <a-input v-model="formModel.name" placeholder="请输入菜单名称" />
        </a-form-item>
        <!-- 菜单类型 -->
        <a-form-item v-if="isAddHandle"
                     field="type"
                     label="菜单类型">
          <a-radio-group type="button"
                         v-model="formModel.type"
                         :options="toRadioOptions(menuTypeKey)" />
        </a-form-item>
        <!-- 菜单图标 -->
        <a-form-item v-if="formModel.type !== MenuType.FUNCTION"
                     field="icon"
                     label="菜单图标">
          <icon-picker v-model:icon="formModel.icon as string">
            <template #iconSelect>
              <a-input v-model="formModel.icon" placeholder="请选择菜单图标" />
            </template>
          </icon-picker>
        </a-form-item>
        <!-- 菜单权限 -->
        <a-form-item v-if="formModel.type === MenuType.FUNCTION"
                     field="permission"
                     label="菜单权限">
          <a-input v-model="formModel.permission"
                   placeholder="菜单权限 infra:system-menu:query"
                   allow-clear />
        </a-form-item>
        <!-- 外链地址 -->
        <a-form-item v-if="formModel.type !== MenuType.FUNCTION"
                     field="path"
                     label="外链地址"
                     tooltip="输入组件名称后则不会生效">
          <a-input v-model="formModel.path"
                   placeholder="外链地址与组件名称二选一"
                   allow-clear />
        </a-form-item>
        <!-- 组件名称 -->
        <a-form-item v-if="formModel.type !== MenuType.FUNCTION"
                     field="component"
                     label="组件名称">
          <a-input v-model="formModel.component"
                   placeholder="路由组件名称"
                   allow-clear />
        </a-form-item>
        <!-- 菜单排序 -->
        <a-form-item field="sort" label="菜单排序">
          <a-input-number v-model="formModel.sort"
                          placeholder="排序"
                          :style="{ width: '120px' }"
                          allow-clear
                          hide-button />
        </a-form-item>
        <!-- 是否可见 -->
        <a-form-item v-if="formModel.type !== MenuType.FUNCTION"
                     field="type"
                     label="是否可见"
                     tooltip="选择隐藏后不会在菜单以及 tab 中显示 但是可以访问">
          <a-switch type="round"
                    v-model="formModel.visible"
                    :checked-text="getDictValue(menuVisibleKey, EnabledStatus.ENABLED)"
                    :unchecked-text="getDictValue(menuVisibleKey, EnabledStatus.DISABLED)"
                    :checked-value="EnabledStatus.ENABLED"
                    :unchecked-value="EnabledStatus.DISABLED" />
        </a-form-item>
        <!-- 是否新窗口打开 -->
        <a-form-item v-if="formModel.type !== MenuType.FUNCTION"
                     field="type"
                     label="新窗口打开"
                     tooltip="选择后点击菜单会使用新页面打开">
          <a-switch type="round"
                    v-model="formModel.newWindow"
                    :checked-text="getDictValue(menuNewWindowKey, EnabledStatus.ENABLED)"
                    :unchecked-text="getDictValue(menuNewWindowKey, EnabledStatus.DISABLED)"
                    :checked-value="EnabledStatus.ENABLED"
                    :unchecked-value="EnabledStatus.DISABLED" />
        </a-form-item>
        <!-- 是否缓存 -->
        <a-form-item v-if="formModel.type !== MenuType.FUNCTION"
                     field="type"
                     label="是否缓存"
                     tooltip="选择缓存后则会使用 keep-alive 缓存组件">
          <a-switch type="round"
                    v-model="formModel.cache"
                    :checked-text="getDictValue(menuCacheKey, EnabledStatus.ENABLED)"
                    :unchecked-text="getDictValue(menuCacheKey, EnabledStatus.DISABLED)"
                    :checked-value="EnabledStatus.ENABLED"
                    :unchecked-value="EnabledStatus.DISABLED" />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'menuFormModal'
  };
</script>

<script lang="ts" setup>
  import type { MenuUpdateRequest } from '@/api/system/menu';
  import { ref, watch } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import formRules from '../types/form.rules';
  import { menuCacheKey, menuNewWindowKey, sortStep } from '../types/const';
  import { menuVisibleKey, menuTypeKey, MenuType } from '../types/const';
  import { EnabledStatus } from '@/types/const';
  import { createMenu, updateMenu } from '@/api/system/menu';
  import { Message } from '@arco-design/web-vue';
  import { useDictStore } from '@/store';
  import IconPicker from '@sanqi377/arco-vue-icon-picker';
  import MenuTreeSelector from '@/components/system/menu/selector/menu-tree-selector.vue';

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();
  const { toRadioOptions, getDictValue } = useDictStore();

  const title = ref<string>();
  const isAddHandle = ref<boolean>(true);

  const defaultForm = (): MenuUpdateRequest => {
    return {
      id: undefined,
      parentId: 0,
      name: undefined,
      type: MenuType.PARENT_MENU,
      permission: undefined,
      sort: undefined,
      visible: EnabledStatus.ENABLED,
      cache: EnabledStatus.ENABLED,
      newWindow: EnabledStatus.DISABLED,
      icon: undefined,
      path: undefined,
      component: undefined,
    };
  };

  const formRef = ref();
  const formModel = ref<MenuUpdateRequest>({});

  const emits = defineEmits(['added', 'updated']);

  // 选择根目录 parentId 设置为 0
  watch(() => formModel.value.type, () => {
    if (formModel.value.type === MenuType.PARENT_MENU) {
      formModel.value.parentId = 0;
    }
  });

  // 打开新增
  const openAdd = (record: any) => {
    title.value = '添加菜单';
    isAddHandle.value = true;
    renderForm({ ...defaultForm(), parentId: record.parentId, sort: (record.sort || 0) + sortStep });
    // 如果是父菜单默认选中子菜单 如果是子菜单默认选中功能
    if (record.type === MenuType.PARENT_MENU || record.type === MenuType.SUB_MENU) {
      formModel.value.type = record.type + 1;
    }
    setVisible(true);
  };

  // 打开修改
  const openUpdate = (record: any) => {
    title.value = '修改菜单';
    isAddHandle.value = false;
    renderForm({ ...defaultForm(), ...record });
    setVisible(true);
  };

  // 渲染表单
  const renderForm = (record: any) => {
    formModel.value = Object.assign({}, record);
  };

  defineExpose({ openAdd, openUpdate });

  // 确定
  const handlerOk = async () => {
    setLoading(true);
    try {
      // 验证参数
      const error = await formRef.value.validate();
      if (error) {
        return false;
      }
      if (formModel.value.parentId === 0
        && (formModel.value.type === MenuType.SUB_MENU || formModel.value.type === MenuType.FUNCTION)) {
        formRef.value.setFields({
          parentId: {
            status: 'error',
            message: '创建子目录或功能时 父菜单不能为根目录'
          }
        });
        return false;
      }
      if (isAddHandle.value) {
        // 新增
        await createMenu(formModel.value);
        Message.success('创建成功');
        emits('added');
      } else {
        // 修改
        await updateMenu(formModel.value);
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
    // 触发 watch 防止第二次加载变成根目录
    renderForm(defaultForm());
  };

</script>

<style lang="less" scoped>

</style>
