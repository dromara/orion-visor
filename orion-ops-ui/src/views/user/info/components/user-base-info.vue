<template>
  <a-spin :loading="loading" style="width: 400px;">
    <!-- 头像 -->
    <div class="avatar-container">
      <div class="avatar-wrapper">
        <a-avatar :size="88"
                  :style="{ backgroundColor: 'rgb(var(--primary-6))' }">
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
    name: 'user-base-info'
  };
</script>

<script lang="ts" setup>
  import type { UserUpdateRequest, UserQueryResponse } from '@/api/user/user';
  import type { PropType } from 'vue';
  import useLoading from '@/hooks/loading';
  import { ref, onMounted } from 'vue';
  import formRules from '../../user/types/form.rules';
  import { useUserStore } from '@/store';
  import { getCurrentUser, updateCurrentUser } from '@/api/user/mine';
  import { pick } from 'lodash';
  import { Message } from '@arco-design/web-vue';
  import { updateUser } from '@/api/user/user';

  const props = defineProps({
    user: Object as PropType<UserQueryResponse>,
  });

  const userStore = useUserStore();
  const { loading, setLoading } = useLoading();

  const nickname = ref('');
  const formRef = ref();
  const formModel = ref<UserUpdateRequest>({});

  // 修改用户信息
  const save = async () => {
    setLoading(true);
    try {
      if (props.user) {
        // 更新用户
        await updateUser(formModel.value);
      } else {
        // 更新自己
        await updateCurrentUser(formModel.value);
        userStore.nickname = formModel.value.nickname;
        nickname.value = formModel.value.nickname?.substring(0, 1) as string;
      }
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
      let u: UserQueryResponse;
      if (props.user) {
        // 从参数中获取
        u = props.user;
      } else {
        // 查询个人信息
        const { data } = await getCurrentUser();
        u = data;
      }
      formModel.value = pick(u, 'id', 'username', 'nickname', 'mobile', 'email');
      nickname.value = u.nickname?.substring(0, 1);
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
