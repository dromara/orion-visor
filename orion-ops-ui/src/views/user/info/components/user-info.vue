<template>
  <a-spin :loading="loading" style="width: 400px;">
    <!-- 头像 -->
    <div class="avatar-container">
      <div class="avatar-wrapper">
        <a-avatar :size="88"
                  :style="{ backgroundColor: '#3370ff' }">
          {{ nickname }}
        </a-avatar>
      </div>
    </div>
    <a-form :model="formModel"
            ref="formRef"
            label-align="right"
            size="medium"
            :style="{ width: '100%' }"
            :label-col-props="{ span: 6 }"
            :wrapper-col-props="{ span: 18 }"
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
    <!-- 操作 -->
    <div class="handler-container">
      <a-button type="primary" @click="save">保存</a-button>
    </div>
  </a-spin>
</template>

<script lang="ts">
  export default {
    name: 'user-info'
  };
</script>

<script lang="ts" setup>
  import type { UserUpdateRequest } from '@/api/user/user';
  import useLoading from '@/hooks/loading';
  import { computed, ref, onMounted } from 'vue';
  import formRules from '../../user/types/form.rules';
  import { useUserStore } from '@/store';
  import { getCurrentUser, updateCurrentUser } from '@/api/user/mine';
  import { pick } from 'lodash';
  import { Message } from '@arco-design/web-vue';

  const userStore = useUserStore();
  const { loading, setLoading } = useLoading();

  const formRef = ref();
  const formModel = ref<UserUpdateRequest>({});

  // 用户名
  const nickname = computed(() => userStore.nickname?.substring(0, 1));

  // 保存
  const save = async () => {
    setLoading(true);
    try {
      await updateCurrentUser(formModel.value);
      userStore.nickname = formModel.value.nickname;
      Message.success('保存成功');
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 加载用户信息
  onMounted(async () => {
    setLoading(true);
    try {
      const { data } = await getCurrentUser();
      formModel.value = pick(data, 'username', 'nickname', 'mobile', 'email');
    } catch (e) {
    } finally {
      setLoading(false);
    }
  });

</script>

<style lang="less" scoped>
  .avatar-container {
    display: flex;
    justify-content: flex-end;
    padding: 4px 0;

    .avatar-wrapper {
      display: flex;
      justify-content: center;
      margin-bottom: 16px;
      width: calc(100% / 24 * 18);
    }
  }

  .handler-container {
    display: flex;
    margin-left: calc(100% / 24 * 6);
  }
</style>
