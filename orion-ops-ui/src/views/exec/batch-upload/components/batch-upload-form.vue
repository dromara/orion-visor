<template>
  <div class="container">
    <!-- 表头 -->
    <div class="panel-header">
      <h3>批量上传</h3>
      <!-- 操作 -->
      <a-button-group size="small">
        <a-button>重置</a-button>
        <a-button type="primary">上传</a-button>
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
      <a-form-item field="remotePath" label="上传路径">
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
          <span class="usn pointer span-blue" @click="openSelectHost">
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
  import { ref } from 'vue';
  import formRules from '../types/form.rules';
  import useLoading from '@/hooks/loading';

  const defaultForm = (): UploadTaskCreateRequest => {
    return {
      description: '',
      remotePath: '',
      hostIdList: [],
      files: []
    };
  };

  const { loading, setLoading } = useLoading();

  const formRef = ref<any>();
  const formModel = ref<UploadTaskCreateRequest>({ ...defaultForm() });
  const hostModal = ref<any>();

  // 打开选择主机
  const openSelectHost = () => {
    hostModal.value.open(formModel.value.hostIdList);
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
