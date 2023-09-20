<template>
  <a-card class="general-card"
          :body-style="{padding: config.status === 1 ? '' : '0'}">
    <!-- 标题 -->
    <template #title>
      <div class="config-title-wrapper">
        <span>SSH 配置</span>
        <a-switch v-model="config.status"
                  :disabled="loading"
                  type="round"
                  :checked-value="1"
                  :unchecked-value="0"
                  :beforeChange="updateStatus" />
      </div>
    </template>
    <a-spin v-show="config.status" :loading="loading" class="config-form-wrapper">
      <!-- 表单 -->
      <a-form :model="formModel"
              ref="formRef"
              label-align="right"
              :label-col-props="{ span: 6 }"
              :wrapper-col-props="{ span: 18 }"
              :rules="{}">
        <!-- 用户名 -->
        <a-form-item field="username" label="用户名" label-col-flex="60px">
          <a-input v-model="formModel.username" placeholder="请输入用户名" />
        </a-form-item>
        <!-- SSH 端口 -->
        <a-form-item field="port" label="SSH端口" label-col-flex="60px">
          <a-input-number v-model="formModel.port" placeholder="请输入SSH端口" />
        </a-form-item>
        <!-- 主机密码 -->
        <a-form-item field="password" label="主机密码" label-col-flex="60px">
          <a-input-password v-model="formModel.password" placeholder="主机密码 / 身份二选一" />
          <a-button type="text" class="password-choose-text">选择</a-button>
        </a-form-item>
        <!-- 身份验证 -->
        <a-form-item field="identityId" label="身份验证" label-col-flex="60px">
          <a-input v-model="formModel.identityId" placeholder="" />
        </a-form-item>
        <!-- 用户名 -->
        <a-form-item field="connectTimeout" label="连接超时时间" label-col-flex="86px">
          <a-input-number v-model="formModel.connectTimeout" placeholder="请输入连接超时时间">
            <template #suffix>
              ms
            </template>
          </a-input-number>
        </a-form-item>
        <!-- 用户名 -->
        <a-form-item field="charset" label="SSH输出编码" label-col-flex="86px">
          <a-input v-model="formModel.charset" placeholder="请输入 SSH 输出编码" />
        </a-form-item>
        <!-- 文件名称编码 -->
        <a-form-item field="fileNameCharset" label="文件名称编码" label-col-flex="86px">
          <a-input v-model="formModel.fileNameCharset" placeholder="请输入 SFTP 文件名称编码" />
        </a-form-item>
        <!-- 文件内容编码 -->
        <a-form-item field="fileContentCharset" label="文件内容编码" label-col-flex="86px">
          <a-input v-model="formModel.fileContentCharset" placeholder="请输入 SFTP 文件内容编码" />
        </a-form-item>
      </a-form>
      <!-- 操作按钮 -->
      <div class="config-button-group">
        <a-space>
          <a-button @click="resetConfig">重置</a-button>
          <a-button type="primary" @click="saveConfig">保存</a-button>
        </a-space>
      </div>
    </a-spin>
  </a-card>
</template>

<script lang="ts">
  export default {
    name: 'host-config-ssh-form'
  };
</script>

<script lang="ts" setup>
  import { reactive, ref, watch } from 'vue';
  import useLoading from '@/hooks/loading';
  import { updateHostConfigStatus, updateHostConfig } from '@/api/asset/host';

  const { loading, setLoading } = useLoading();

  const props = defineProps({
    content: Object
  });

  const emits = defineEmits(['submitted']);

  const config = ref({
    status: undefined,
    version: undefined,
  });

  const formRef = ref<any>();
  const formModel = reactive<Record<string, any>>({
    username: undefined,
    port: undefined,
    password: undefined,
    identityId: undefined,
    connectTimeout: undefined,
    charset: undefined,
    fileNameCharset: undefined,
    fileContentCharset: undefined,
  });

  // 监听数据变化
  watch(() => props.content, (v: any) => {
    config.value.status = v?.status;
    config.value.version = v?.version;
    resetConfig();
  });

  // 修改状态
  const updateStatus = (e: number) => {
    setLoading(true);
    return updateHostConfigStatus({
      id: props?.content?.id,
      status: e,
      version: config.value.version
    }).then(({ data }) => {
      config.value.version = data;
      setLoading(false);
      return true;
    }).catch(() => {
      setLoading(false);
      return false;
    });
  };

  // 重置配置
  const resetConfig = () => {
    Object.keys(formModel).forEach(k => {
      formModel[k] = props.content?.config[k];
    });
  };

  // 保存配置
  const saveConfig = async () => {
    try {
      // 验证参数
      const error = await formRef.value.validate();
      if (error) {
        return false;
      }
      setLoading(true);
      const { data } = await updateHostConfig({
        id: props?.content?.id,
        version: config.value.version,
        config: JSON.stringify(formModel)
      });
      config.value.version = data;
      setLoading(false);
      // 回调 props
      emits('submitted', { ...formModel });
    } finally {
      setLoading(false);
    }
  };

</script>

<style lang="less" scoped>
  .config-title-wrapper {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .config-form-wrapper {
    width: 100%;
  }

  .password-choose-text {
    margin-left: 6px;
    padding: 0 4px 0 4px;
  }

  .config-button-group {
    display: flex;
    align-items: center;
    justify-content: flex-end;
  }
</style>
