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
              :wrapper-col-props="{ span: 18 }">
        <!-- 用户名 -->
        <a-form-item field="username" label="用户名">
          <a-input v-model="updateUser.username" :disabled="true" />
        </a-form-item>
        <!-- 花名 -->
        <a-form-item field="nickname" label="花名">
          <a-input v-model="updateUser.nickname" :disabled="true" />
        </a-form-item>
        <!-- 角色 -->
        <a-form-item field="roles" label="角色">
          <role-selector v-model="formModel.roleIdList"
                         :multiple="true" />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'user-user-grant-roles-form-modal'
  };
</script>

<script lang="ts" setup>
  import type { UserQueryResponse, UserUpdateRequest } from '@/api/user/user';
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import { Message } from '@arco-design/web-vue';
  import RoleSelector from '@/components/user/role/role-selector.vue';
  import { useCacheStore } from '@/store';
  import { getUserRoleIdList, grantUserRole } from '@/api/user/user';

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const formRef = ref();
  const formModel = ref<UserUpdateRequest>({});
  const updateUser = ref<UserQueryResponse>({} as UserQueryResponse);
  const cacheStore = useCacheStore();

  // 打开
  const open = (record: any) => {
    renderForm(record);
    setVisible(true);
    loadRoles();
  };

  // 渲染表单
  const renderForm = (record: any) => {
    updateUser.value = Object.assign({}, record);
    formModel.value = {
      id: record.id,
      roleIdList: []
    };
  };

  // 加载角色
  const loadRoles = async () => {
    try {
      // 加载用户角色
      const { data: roleIdList } = await getUserRoleIdList(formModel.value.id as number);
      formModel.value.roleIdList = roleIdList;
    } catch (e) {
    }
  };

  defineExpose({ open });

  // 确定
  const handlerOk = async () => {
    setLoading(true);
    try {
      await grantUserRole(formModel.value);
      Message.success('修改成功');
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

</script>

<style lang="less" scoped>

</style>
