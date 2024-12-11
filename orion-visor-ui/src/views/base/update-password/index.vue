<template>
  <a-spin class="content" :loading="loading">
    <!-- 原因 -->
    <p class="reason">{{ reason }}</p>
    <!-- 表单 -->
    <a-form :model="formModel"
            ref="formRef"
            label-align="right"
            :rules="rules">
      <!-- 原始密码 -->
      <a-form-item field="beforePassword"
                   label="原始密码"
                   hide-label>
        <a-input-password v-model="formModel.beforePassword" placeholder="请输入原始密码" />
      </a-form-item>
      <!-- 新密码 -->
      <a-form-item field="password"
                   label="新密码"
                   hide-label>
        <a-input-password v-model="formModel.password" placeholder="请输入新密码" />
      </a-form-item>
      <!-- 确认密码 -->
      <a-form-item field="checkPassword"
                   label="确认密码"
                   hide-label>
        <a-input-password v-model="formModel.checkPassword" placeholder="请再次输入新密码" />
      </a-form-item>
    </a-form>
    <!-- 按钮 -->
    <a-space>
      <!-- 确认修改 -->
      <a-button class="action"
                type="primary"
                @click="doUpdate">
        确认修改
      </a-button>
      <!-- 退出登录 -->
      <a-button class="action"
                type="primary"
                @click="() => logout()">
        退出登录
      </a-button>
    </a-space>
  </a-spin>
</template>

<script lang="ts">
  export default {
    name: 'updatePassword',
  };
</script>

<script lang="ts" setup>
  import type { UserUpdatePasswordRequest } from '@/api/user/mine';
  import type { FieldRule } from '@arco-design/web-vue';
  import { onMounted, ref } from 'vue';
  import { useRoute } from 'vue-router';
  import { md5 } from '@/utils';
  import useUser from '@/hooks/user';
  import useLoading from '@/hooks/loading';
  import { useDictStore } from '@/store';
  import { dictKeys, updatePasswordReasonKey } from './types/const';
  import { updateCurrentUserPassword } from '@/api/user/mine';

  const { logout } = useUser();
  const { loading, setLoading } = useLoading();

  const rules = {
    beforePassword: [{
      required: true,
      message: '请输入原始密码'
    }],
    password: [{
      required: true,
      message: '请输入新密码'
    }, {
      minLength: 8,
      maxLength: 32,
      message: '新密码长度需要在 8-32 位之间'
    }, {
      validator: (value, cb) => {
        if (formModel.value.beforePassword === value) {
          cb('新密码不能和原始密码相同');
          return;
        }
      }
    }],
    checkPassword: [{
      required: true,
      message: '请再次输入新密码'
    }, {
      validator: (value, cb) => {
        if (formModel.value.password !== value) {
          cb('两次输入的密码不一致');
          return;
        }
      }
    }],
  } as Record<string, FieldRule | FieldRule[]>;

  const reason = ref();
  const formRef = ref();
  const formModel = ref<UserUpdatePasswordRequest>({});

  // 确认修改
  const doUpdate = async () => {
    setLoading(true);
    try {
      // 验证参数
      const error = await formRef.value.validate();
      if (error) {
        return;
      }
      // 修改密码
      await updateCurrentUserPassword({
        beforePassword: md5(formModel.value.beforePassword as string),
        password: md5(formModel.value.password as string)
      });
      // 退出登录
      await logout('修改成功');
    } catch (e) {
      return;
    } finally {
      setLoading(false);
    }
  };

  onMounted(async () => {
    const reasonKey = useRoute().query?.reason;
    if (reasonKey) {
      const { loadKeys, getDictValue } = useDictStore();
      // 加载字典值
      await loadKeys(dictKeys);
      // 获取原因
      reason.value = getDictValue(updatePasswordReasonKey, reasonKey);
    } else {
      reason.value = '修改密码';
    }
  });

</script>

<style lang="less" scoped>
  .content {
    position: absolute;
    top: 50%;
    left: 50%;
    margin-left: -96px;
    margin-top: -168px;
    text-align: center;

    .reason {
      text-align: left;
      margin-bottom: 16px;
      height: 16px;
      line-height: 16px;
      font-size: 16px;
      color: var(--color-text-2);
    }

    .action {
      width: 148px;
    }
  }
</style>
