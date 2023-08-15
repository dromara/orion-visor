<template>
  <a-modal v-model:visible="visible"
           body-class="modal-form"
           title-align="start"
           :title="title"
           :top="80"
           :align-center="false"
           :draggable="true"
           :mask-closable="false"
           :unmount-on-close="true"
           :ok-button-props="{ disabled: loading }"
           :cancel-button-props="{ disabled: loading }"
           :on-before-ok="handlerOk"
           @close="handleClose">
    <a-spin :loading="loading">
      <a-form :model="formModel"
              ref="formRef"
              label-align="right"
              :style="{ width: '460px' }"
              :label-col-props="{ span: 6 }"
              :wrapper-col-props="{ span: 18 }"
              :rules="formRules">
        <!-- 上级菜单 -->
        <a-form-item field="parentId" label="上级菜单">
          <menu-tree-selector v-model:model-value="formModel.parentId"
                              :disabled="formModel.type === MenuTypeEnum.PARENT_MENU.value" />
        </a-form-item>
        <!-- 菜单名称 -->
        <a-form-item field="name" label="菜单名称">
          <a-input v-model="formModel.name" placeholder="请输入菜单名称" />
        </a-form-item>
        <!-- 菜单类型 -->
        <a-form-item field="type" label="菜单类型">
          <a-radio-group type="button"
                         v-model="formModel.type"
                         :options="toOptions(MenuTypeEnum)" />
        </a-form-item>
        <!-- 菜单图标 -->
        <a-form-item v-if="formModel.type !== MenuTypeEnum.FUNCTION.value"
                     field="icon"
                     label="菜单图标">
          <icon-picker v-model:icon="formModel.icon">
            <template #iconSelect>
              <a-input v-model="formModel.icon" placeholder="请选择菜单图标" />
            </template>
          </icon-picker>
        </a-form-item>
        <!-- 菜单权限 -->
        <a-form-item v-if="formModel.type === MenuTypeEnum.FUNCTION.value"
                     field="permission"
                     label="菜单权限">
          <a-input v-model="formModel.permission"
                   placeholder="菜单权限 infra:system-menu:query"
                   allow-clear />
        </a-form-item>
        <!-- 外链地址 -->
        <a-form-item v-if="formModel.type !== MenuTypeEnum.FUNCTION.value"
                     field="path"
                     label="外链地址"
                     tooltip="输入组件名称后则不会生效">
          <a-input v-model="formModel.path"
                   placeholder="外链地址与组件名称二选一"
                   allow-clear />
        </a-form-item>
        <!-- 组件名称 -->
        <a-form-item v-if="formModel.type !== MenuTypeEnum.FUNCTION.value"
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
                          allow-clear />
        </a-form-item>
        <!-- 是否可见 -->
        <a-form-item v-if="formModel.type !== MenuTypeEnum.FUNCTION.value"
                     field="type"
                     label="是否可见"
                     tooltip="选择隐藏后不会在菜单以及 tab 中显示 但是可以访问">
          <a-radio-group type="button"
                         v-model="formModel.visible"
                         :options="toOptions(MenuVisibleEnum)" />
        </a-form-item>
        <!-- 是否缓存 -->
        <a-form-item v-if="formModel.type !== MenuTypeEnum.FUNCTION.value"
                     field="type"
                     label="是否缓存"
                     tooltip="选择缓存后则会使用 keep-alive 缓存组件">
          <a-radio-group type="button"
                         v-model="formModel.cache"
                         :options="toOptions(MenuCacheEnum)" />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'menu-form-modal'
  };
</script>

<script lang="ts" setup>
  import { reactive, ref, watch } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import formRules from '../types/form.rules';
  import { MenuTypeEnum, MenuVisibleEnum, MenuCacheEnum } from '../types/enum.types';
  import { toOptions } from '@/utils/enum';
  import IconPicker from '@sanqi377/arco-vue-icon-picker';
  import MenuTreeSelector from './menu-tree-selector.vue';
  import { createMenu, updateMenu } from '@/api/system/menu';
  import { Message } from '@arco-design/web-vue';

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const title = ref<string>();
  const isAddHandle = ref<boolean>(true);

  const defaultForm = () => {
    return {
      id: undefined,
      parentId: 0,
      name: undefined,
      type: MenuTypeEnum.PARENT_MENU.value,
      permission: undefined,
      sort: 10,
      visible: MenuVisibleEnum.SHOW.value,
      cache: MenuCacheEnum.SHOW.value,
      icon: undefined,
      path: undefined,
      component: undefined,
    };
  };

  const formRef = ref<any>();
  const formModel = reactive<Record<string, any>>(defaultForm());

  const emits = defineEmits(['added', 'updated']);

  // 选择根目录 parentId就是 0
  watch(() => formModel.type, () => {
    if (formModel.type === MenuTypeEnum.PARENT_MENU.value) {
      formModel.parentId = 0;
    }
  });

  // 打开新增
  const openAdd = (record: any) => {
    title.value = '添加菜单';
    isAddHandle.value = true;
    renderForm({ ...defaultForm(), parentId: record.parentId });
    // 如果是父菜单默认选中子菜单 如果是子菜单默认选中功能
    if (record.type === 1 || record.type === 2) {
      formModel.type = record.type + 1;
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
    Object.keys(formModel).forEach(k => {
      formModel[k] = record[k];
    });
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
      if (formModel.parentId === 0
        && (formModel.type === MenuTypeEnum.SUB_MENU.value || formModel.type === MenuTypeEnum.FUNCTION.value)) {
        Message.error('创建子目录或功能时 父菜单不能为根目录');
        return false;
      }
      if (isAddHandle.value) {
        // 新增
        await createMenu(formModel as any);
        Message.success('创建成功');
        emits('added');
      } else {
        // 修改
        await updateMenu(formModel as any);
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
    setVisible(false);
    // 触发 watch 防止第二次加载变成根目录
    renderForm(defaultForm());
  };

</script>

<style lang="less" scoped>

</style>
