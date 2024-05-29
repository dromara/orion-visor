<template>
  <div class="login-form-wrapper">
    <!-- 标题 -->
    <div class="login-form-title usn">{{ $t('login.form.title') }}</div>
    <!-- 子标题 -->
    <div v-if="!isDemoMode" class="login-form-sub-title">{{ $t('login.form.sub.title') }}</div>
    <!-- 演示模式 -->
    <div v-else class="login-form-sub-title ">演示模式账号: admin/admin</div>
    <!-- 错误信息 -->
    <div class="login-form-error-msg">{{ errorMessage }}</div>
    <!-- 登录表单 -->
    <a-form ref="loginForm"
            :model="userInfo"
            class="login-form"
            layout="vertical"
            @submit="handleSubmit">
      <a-form-item field="username"
                   :rules="[{ required: true, message: $t('login.form.userName.errMsg') }]"
                   :validate-trigger="['change', 'blur']"
                   hide-label>
        <a-input v-model="userInfo.username"
                 :placeholder="$t('login.form.userName.placeholder')">
          <template #prefix>
            <icon-user />
          </template>
        </a-input>
      </a-form-item>
      <a-form-item field="password"
                   :rules="[{ required: true, message: $t('login.form.password.errMsg') }]"
                   :validate-trigger="['change', 'blur']"
                   hide-label>
        <a-input-password v-model="userInfo.password"
                          :placeholder="$t('login.form.password.placeholder')"
                          allow-clear>
          <template #prefix>
            <icon-lock />
          </template>
        </a-input-password>
      </a-form-item>
      <!-- 登录按钮 -->
      <a-space :size="16" direction="vertical">
        <a-button type="primary" html-type="submit" long :loading="loading">
          {{ $t('login.form.login') }}
        </a-button>
      </a-space>
    </a-form>
  </div>
</template>

<script lang="ts" setup>
  import type { ValidatedError } from '@arco-design/web-vue/es/form/interface';
  import type { LoginRequest } from '@/api/user/auth';
  import { reactive, ref } from 'vue';
  import { useRouter } from 'vue-router';
  import { Message } from '@arco-design/web-vue';
  import { useI18n } from 'vue-i18n';
  import { useUserStore } from '@/store';
  import useLoading from '@/hooks/loading';
  import { isDemoMode } from '@/utils/env';

  const router = useRouter();
  const { t } = useI18n();
  const { loading, setLoading } = useLoading();
  const userStore = useUserStore();

  const errorMessage = ref('');

  const userInfo = reactive<LoginRequest>({
    username: undefined,
    password: undefined,
  });

  const handleSubmit = async ({ errors, values }: {
    errors: Record<string, ValidatedError> | undefined;
    values: LoginRequest;
  }) => {
    if (loading.value) return;
    if (!errors) {
      setLoading(true);
      try {
        // 执行登录
        await userStore.login(values);
        // 跳转路由
        const { redirect, ...othersQuery } = router.currentRoute.value.query;
        router.push({
          name: (redirect as string) || 'workplace',
          query: {
            ...othersQuery,
          },
        });
        Message.success(t('login.form.login.success'));
      } catch (err) {
        errorMessage.value = (err as Error).message;
      } finally {
        setLoading(false);
      }
    }
  };
</script>

<style lang="less" scoped>
  .login-form {
    &-wrapper {
      width: 320px;
    }

    &-title {
      color: var(--color-text-1);
      font-weight: 500;
      font-size: 24px;
      line-height: 32px;
    }

    &-sub-title {
      color: var(--color-text-3);
      font-size: 16px;
      line-height: 24px;
    }

    &-error-msg {
      height: 32px;
      color: rgb(var(--red-6));
      line-height: 32px;
    }
  }
</style>
