<template>
  <a-row class="layout-container">
    <a-col :span="8">
      <a-card class="general-card">
        <template #title>
          用户信息
        </template>
        <a-spin :loading="loading" style="width: 100%">
          <a-form :model="formModel"
                  ref="formRef"
                  label-align="right"
                  :label-col-props="{ span: 6 }"
                  :wrapper-col-props="{ span: 16 }"
                  :rules="formRules">
            <!-- 用户名 -->
            <a-form-item field="username" label="用户名">
              <a-input v-model="formModel.username" disabled />
            </a-form-item>
            <!-- 花名 -->
            <a-form-item field="nickname" label="花名">
              <a-input v-model="formModel.nickname" placeholder="请输入花名" />
            </a-form-item>
            <!-- 手机号 -->
            <a-form-item field="mobile" label="手机号">
              <a-input v-model="formModel.mobile" placeholder="请输入手机号" />
            </a-form-item>
            <!-- 邮箱 -->
            <a-form-item field="email" label="邮箱">
              <a-input v-model="formModel.email" placeholder="请输入邮箱" />
            </a-form-item>
          </a-form>
        </a-spin>
      </a-card>
    </a-col>
  </a-row>
</template>

<script lang="ts" setup>
  import type { UserUpdateRequest } from '@/api/user/user';
  import useLoading from '@/hooks/loading';
  import { ref } from 'vue';
  import formRules from '../user/types/form.rules';

  const { loading, setLoading } = useLoading();

  const formRef = ref();
  const formModel = ref<UserUpdateRequest>({});

</script>

<style lang="less" scoped>

</style>
