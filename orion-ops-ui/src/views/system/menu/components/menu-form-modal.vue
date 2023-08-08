<template>
  <a-modal v-model:visible="visible"
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
              :style="{ width: '440px' }"
              :label-col-props="{ span: 5 }"
              :wrapper-col-props="{ span: 19 }"
              :rules="formRules">
        <!-- 上级菜单 -->
        <a-form-item field="parentId" label="上级菜单">
          <menu-tree-selector v-model="formModel.parentId"
                              :disabled="!!(isAddHandle && formModel.id)" />
        </a-form-item>
        <!-- 名称 -->
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
        <a-form-item field="icon" label="菜单图标">
          <icon-picker v-model:icon="formModel.icon">
            <template #iconSelect>
              <a-input v-model="formModel.icon" placeholder="请选择菜单图标" />
            </template>
          </icon-picker>
        </a-form-item>
        <!-- 菜单权限 -->
        <a-form-item field="permission" label="菜单权限">
          <a-input v-model="formModel.permission"
                   placeholder="菜单权限 infra:system-menu:query"
                   allow-clear />
        </a-form-item>
        <!-- 外链地址 -->
        <a-form-item field="path" label="外链地址">
          <a-input v-model="formModel.path"
                   placeholder="外链地址与组件名称二选一"
                   allow-clear />
        </a-form-item>
        <!-- 组件名称 -->
        <a-form-item field="component" label="组件名称">
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
        <!-- 可见状态 -->
        <a-form-item field="type" label="可见状态">
          <a-radio-group type="button"
                         v-model="formModel.visible"
                         :options="toOptions(MenuVisibleEnum)" />
        </a-form-item>
        <!-- 缓存状态 -->
        <a-form-item field="type" label="缓存状态">
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
  import { reactive, ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import formRules from '../type/form.rules';
  import { MenuTypeEnum, MenuVisibleEnum, MenuCacheEnum } from '../type/enum.types';
  import { toOptions } from '@/utils/enum';
  import IconPicker from '@sanqi377/arco-vue-icon-picker';
  import MenuTreeSelector from '@/views/system/menu/components/menu-tree-selector.vue';
  import { createMenu, updateMenu } from '@/api/system/menu';

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const title = ref<string>();
  const isAddHandle = ref<boolean>(true);

  const formRef = ref<any>();
  const formModel = reactive<Record<string, any>>({
    id: undefined,
    parentId: undefined,
    name: undefined,
    type: undefined,
    permission: undefined,
    sort: undefined,
    visible: undefined,
    cache: undefined,
    icon: undefined,
    path: undefined,
    component: undefined,
  });

  const emits = defineEmits(['added', 'updated']);

  // 打开新增
  const openAdd = (record: any) => {
    title.value = '添加菜单';
    isAddHandle.value = true;
    renderForm(record);
    setVisible(true);
  };

  // 打开修改
  const openUpdate = (record: any) => {
    title.value = '修改菜单';
    isAddHandle.value = false;
    renderForm(record);
    setVisible(true);
  };

  defineExpose({ openAdd, openUpdate });

  // 渲染表单
  const renderForm = (record: any) => {
    Object.keys(formModel).forEach(k => {
      formModel[k] = record[k];
    });
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
        await createMenu(formModel as any);
        emits('added', { ...formModel });
      } else {
        // 修改
        await updateMenu(formModel as any);
        emits('updated', { ...formModel });
      }
      // 清空
      // handlerClear();
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
    renderForm({});
  };

</script>

<style lang="less" scoped>
  :deep(.arco-form) {
    &-item {
      &:last-child {
        margin-bottom: 0 !important;
      }
    }
  }

</style>
