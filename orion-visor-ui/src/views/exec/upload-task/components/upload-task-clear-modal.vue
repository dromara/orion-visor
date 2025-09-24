<template>
  <a-modal v-model:visible="visible"
           modal-class="modal-form-large"
           title-align="start"
           title="清理上传任务"
           :top="80"
           :align-center="false"
           :draggable="true"
           :mask-closable="false"
           :unmount-on-close="true"
           :ok-button-props="{ disabled: loading }"
           :cancel-button-props="{ disabled: loading }"
           :on-before-ok="handlerOk"
           @close="handleClose">
    <a-spin class="full" :loading="loading">
      <a-form :model="formModel"
              label-align="right"
              :auto-label-width="true">
        <!-- 上传时间 -->
        <a-form-item field="createTimeRange" label="上传时间">
          <a-range-picker v-model="formModel.createTimeRange"
                          :time-picker-props="{ defaultValue: ['00:00:00', '23:59:59'] }"
                          show-time
                          format="YYYY-MM-DD HH:mm:ss" />
        </a-form-item>
        <!-- 上传用户 -->
        <a-form-item field="userId" label="上传用户">
          <user-selector v-model="formModel.userId"
                         placeholder="请选择上传用户"
                         allow-clear />
        </a-form-item>
        <!-- 远程路径 -->
        <a-form-item field="remotePath" label="远程路径">
          <a-input v-model="formModel.remotePath"
                   placeholder="请输入远程路径"
                   allow-clear />
        </a-form-item>
        <!-- 上传描述 -->
        <a-form-item field="description" label="上传描述">
          <a-input v-model="formModel.description"
                   placeholder="请输入上传描述"
                   allow-clear />
        </a-form-item>
        <!-- 上传状态 -->
        <a-form-item field="status" label="上传状态">
          <a-select v-model="formModel.status"
                    :options="toOptions(uploadTaskStatusKey)"
                    placeholder="请选择状态"
                    allow-clear />
        </a-form-item>
        <!-- 数量限制 -->
        <a-form-item field="limit" label="数量限制">
          <a-input-number v-model="formModel.limit"
                          :min="1"
                          :max="maxClearLimit"
                          :placeholder="`请输入数量限制 最大: ${maxClearLimit}`"
                          hide-button
                          allow-clear />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'uploadTaskFormModal'
  };
</script>

<script lang="ts" setup>
  import type { UploadTaskQueryRequest } from '@/api/exec/upload-task';
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import { maxClearLimit, uploadTaskStatusKey } from '../types/const';
  import { getUploadTaskCount, clearUploadTask } from '@/api/exec/upload-task';
  import { Message, Modal } from '@arco-design/web-vue';
  import { useDictStore } from '@/store';
  import UserSelector from '@/components/user/user/selector/index.vue';

  const emits = defineEmits(['clear']);

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();
  const { toOptions } = useDictStore();

  const formModel = ref<UploadTaskQueryRequest>({});

  const defaultForm = (): UploadTaskQueryRequest => {
    return {
      userId: undefined,
      remotePath: undefined,
      description: undefined,
      status: undefined,
      createTimeRange: undefined,
      limit: maxClearLimit,
    };
  };

  // 打开
  const open = (record: any) => {
    renderForm({ ...defaultForm(), ...record });
    setVisible(true);
  };

  // 渲染表单
  const renderForm = (record: any) => {
    formModel.value = Object.assign({}, record);
  };

  defineExpose({ open });

  // 确定
  const handlerOk = async () => {
    if (!formModel.value.limit) {
      Message.error('请输入数量限制');
      return false;
    }
    setLoading(true);
    try {
      // 获取总数量
      const { data } = await getUploadTaskCount(formModel.value);
      if (data) {
        // 清理
        doClear(data);
      } else {
        // 无数据
        Message.warning('当前条件未查询到数据');
      }
    } catch (e) {
    } finally {
      setLoading(false);
    }
    return false;
  };

  // 执行删除
  const doClear = (count: number) => {
    Modal.confirm({
      title: '清理确认',
      content: `确定要删除 ${count} 条数据吗? 确定后将立即删除且无法恢复!`,
      onOk: async () => {
        setLoading(true);
        try {
          // 调用清理
          const { data } = await clearUploadTask(formModel.value);
          Message.success(`已成功清理 ${data} 条数据`);
          emits('clear');
          // 清空
          setVisible(false);
          handlerClear();
        } catch (e) {
        } finally {
          setLoading(false);
        }
      }
    });
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
