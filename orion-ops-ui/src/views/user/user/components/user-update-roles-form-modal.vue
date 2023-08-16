<template>
  <a-modal v-model:visible="visible"
           body-class="modal-form"
           title-align="start"
           title="分配角色"
           :top="120"
           :align-center="false"
           :draggable="true"
           :mask-closable="false"
           :unmount-on-close="true"
           :ok-button-props="{ disabled: saveLoading }"
           :cancel-button-props="{ disabled: saveLoading }"
           :on-before-ok="handlerOk"
           @close="handleClose">
    <a-spin :loading="saveLoading">
      <a-form :model="formModel"
              ref="formRef"
              label-align="right"
              :style="{ width: '460px' }"
              :label-col-props="{ span: 6 }"
              :wrapper-col-props="{ span: 18 }">
        <!-- 用户名 -->
        <a-form-item field="username" label="用户名">
          <a-input v-model="formModel.username" :disabled="true" />
        </a-form-item>
        <!-- 花名 -->
        <a-form-item field="nickname" label="花名">
          <a-input v-model="formModel.nickname" :disabled="true" />
        </a-form-item>
        <!-- 角色 -->
        <a-form-item field="roles" label="角色">
          <user-role-selector v-model="formModel.roleIdList"
                              :loading="roleLoading"
                              :multiple="true" />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'user-user-update-roles-form-modal'
  };
</script>

<script lang="ts" setup>
  import { reactive, ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import { Message } from '@arco-design/web-vue';
  import UserRoleSelector from '@/components/user/role/user-role-selector.vue';
  import { getRoleList } from '@/api/user/role';
  import { useCacheStore } from '@/store';
  import { getUserRoleIdList, updateUserRole } from '@/api/user/user';

  const { visible, setVisible } = useVisible();
  const { loading: saveLoading, setLoading: setSaveLoading } = useLoading();
  const { loading: roleLoading, setLoading: setRoleLoading } = useLoading();

  const defaultForm = () => {
    return {
      id: undefined,
      username: undefined,
      nickname: undefined,
      roleIdList: undefined,
    };
  };

  const formRef = ref<any>();
  const formModel = reactive<Record<string, any>>(defaultForm());
  const cacheStore = useCacheStore();

  // 打开
  const open = (record: any) => {
    renderForm({ ...defaultForm(), ...record });
    setVisible(true);
    loadRoles();
  };

  // 渲染表单
  const renderForm = (record: any) => {
    Object.keys(formModel).forEach(k => {
      formModel[k] = record[k];
    });
  };

  // 加载角色
  const loadRoles = async () => {
    try {
      setRoleLoading(true);
      // 获取全部角色
      if (!cacheStore.roles?.length) {
        const { data } = await getRoleList();
        cacheStore.updateRoles(data);
      }
      // 加载用户角色
      const { data: roleIdList } = await getUserRoleIdList(formModel.id);
      formModel.roleIdList = roleIdList;
    } finally {
      setRoleLoading(false);
    }
  };

  defineExpose({ open });

  // 确定
  const handlerOk = async () => {
    setSaveLoading(true);
    try {
      await updateUserRole({
        id: formModel.id,
        roleIdList: formModel.roleIdList
      });
      Message.success('修改成功');
      // 清空
      handlerClear();
    } catch (e) {
      return false;
    } finally {
      setSaveLoading(false);
    }
  };

  // 关闭
  const handleClose = () => {
    handlerClear();
  };

  // 清空
  const handlerClear = () => {
    setSaveLoading(false);
    setRoleLoading(false);
    setVisible(false);
  };

</script>

<style lang="less" scoped>

</style>
