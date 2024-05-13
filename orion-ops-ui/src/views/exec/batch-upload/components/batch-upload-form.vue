<template>
  <div class="container">
    <!-- 表头 -->
    <div class="panel-header">
      <h3>批量上传</h3>
      <!-- 操作 -->
      <a-button-group size="mini">
        <!-- 重置 -->
        <a-button v-if="status.value === UploadTaskStepStatus.WAITING.value"
                  @click="emits('clear')">
          重置
        </a-button>
        <!-- 取消上传 -->
        <a-button v-if="status.value === UploadTaskStepStatus.REQUESTING.value"
                  type="primary"
                  status="warning"
                  @click="emits('abort')">
          取消上传
        </a-button>
        <!-- 开始上传 -->
        <a-button v-if="status.value === UploadTaskStepStatus.WAITING.value"
                  type="primary"
                  @click="submit">
          开始上传
        </a-button>
      </a-button-group>
    </div>
    <!-- 表单 -->
    <a-form :model="formModel"
            ref="formRef"
            class="form-wrapper"
            label-align="right"
            :auto-label-width="true"
            :rules="formRules">
      <!-- 上传描述 -->
      <a-form-item field="description" label="上传描述">
        <a-input v-model="formModel.description"
                 placeholder="请输入上传描述"
                 allow-clear />
      </a-form-item>
      <!-- 上传路径 -->
      <a-form-item field="remotePath"
                   style="margin-bottom: 4px;"
                   label="上传路径"
                   help="${username} 用户名 ${home} 用户家目录">
        <a-input v-model="formModel.remotePath"
                 placeholder="请输入上传路径"
                 allow-clear />
      </a-form-item>
      <!-- 上传主机 -->
      <a-form-item field="hostIdList" label="上传主机">
        <div class="selected-host">
          <!-- 已选择数量 -->
          <span class="usn" v-if="formModel.hostIdList?.length">
            已选择<span class="selected-host-count span-blue">{{ formModel.hostIdList?.length }}</span>台主机
          </span>
          <span class="usn pointer span-blue" @click="emits('openHost')">
            {{ formModel.hostIdList?.length ? '重新选择' : '选择主机' }}
          </span>
        </div>
      </a-form-item>
    </a-form>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'batchUploadForm'
  };
</script>

<script lang="ts" setup>
  import type { UploadTaskCreateRequest } from '@/api/exec/upload-task';
  import type { UploadTaskStatusType } from '../types/const';
  import { ref } from 'vue';
  import formRules from '../types/form.rules';
  import { UploadTaskStepStatus } from '../types/const';

  const emits = defineEmits(['upload', 'openHost', 'abort', 'clear']);
  const props = defineProps<{
    status: UploadTaskStatusType;
    formModel: UploadTaskCreateRequest;
  }>();

  const formRef = ref<any>();

  // 提交表单
  const submit = async () => {
    // 验证参数
    let error = await formRef.value.validate();
    if (error) {
      return false;
    }
    emits('upload');
  };

</script>

<style lang="less" scoped>

  .selected-host {
    width: 100%;
    height: 32px;
    padding: 0 12px;
    border-radius: 2px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    color: var(--color-text-2);
    background: var(--color-fill-2);
    transition: all 0.3s;

    &-count {
      font-size: 16px;
      font-weight: 600;
      display: inline-block;
      margin: 0 6px;
    }

    &:hover {
      background: var(--color-fill-3);
    }
  }

</style>
