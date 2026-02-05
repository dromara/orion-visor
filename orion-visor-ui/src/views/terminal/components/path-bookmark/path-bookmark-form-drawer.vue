<template>
  <a-drawer v-model:visible="visible"
            :width="388"
            :title="title"
            :mask-closable="false"
            :unmount-on-close="true"
            :ok-button-props="{ disabled: loading }"
            :cancel-button-props="{ disabled: loading }"
            :on-before-ok="handleOk"
            @cancel="handleClose">
    <a-spin class="full form-container" :loading="loading">
      <a-form :model="formModel"
              ref="formRef"
              label-align="right"
              :auto-label-width="true"
              :rules="bookmarkFormRules">
        <!-- 类型 -->
        <a-form-item field="type" label="类型" hide-asterisk>
          <a-radio-group v-model="formModel.type"
                         type="button"
                         class="full-radio-group usn"
                         :options="toRadioOptions(pathBookmarkTypeKey)" />
        </a-form-item>
        <!-- 名称 -->
        <a-form-item field="name" label="名称">
          <a-input v-model="formModel.name"
                   placeholder="请输入名称"
                   allow-clear />
        </a-form-item>
        <!-- 分组 -->
        <a-form-item field="groupId" label="分组">
          <path-bookmark-group-selector v-model="formModel.groupId" />
        </a-form-item>
        <!-- 文件路径 -->
        <a-form-item field="path" label="路径">
          <a-textarea v-model="formModel.path"
                      placeholder="文件路径"
                      :auto-size="{ minRows: 8, maxRows: 8 }"
                      allow-clear />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-drawer>
</template>

<script lang="ts">
  export default {
    name: 'pathBookmarkFormDrawer'
  };
</script>

<script lang="ts" setup>
  import type { PathBookmarkUpdateRequest } from '@/api/terminal/path-bookmark';
  import type { FormHandle } from '@/types/form';
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import { assignOmitRecord } from '@/utils';
  import { createPathBookmark, updatePathBookmark } from '@/api/terminal/path-bookmark';
  import { useDictStore } from '@/store';
  import { Message } from '@arco-design/web-vue';
  import { pathBookmarkTypeKey, PathBookmarkType } from '../../types/const';
  import { bookmarkFormRules } from '../../types/form.rules';
  import PathBookmarkGroupSelector from '@/components/terminal/bookmark-path/group/selector/index.vue';

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();
  const { toRadioOptions } = useDictStore();

  const title = ref<string>();
  const formHandle = ref<FormHandle>('add');

  const defaultForm = (): PathBookmarkUpdateRequest => {
    return {
      id: undefined,
      groupId: undefined,
      name: undefined,
      type: PathBookmarkType.DIR,
      path: undefined,
    };
  };

  const formRef = ref<any>();
  const formModel = ref<PathBookmarkUpdateRequest>({});

  const emits = defineEmits(['added', 'updated']);

  // 打开新增
  const openAdd = () => {
    title.value = '添加路径书签';
    formHandle.value = 'add';
    formModel.value = assignOmitRecord({ ...defaultForm() });
    setVisible(true);
  };

  // 打开修改
  const openUpdate = (record: any) => {
    title.value = '修改路径书签';
    formHandle.value = 'update';
    formModel.value = assignOmitRecord({ ...defaultForm(), ...record });
    setVisible(true);
  };

  defineExpose({ openAdd, openUpdate });

  // 确定
  const handleOk = async () => {
    setLoading(true);
    try {
      // 验证参数
      const error = await formRef.value.validate();
      if (error) {
        return false;
      }
      if (formHandle.value === 'add') {
        // 新增
        const { data: id } = await createPathBookmark(formModel.value);
        formModel.value.id = id;
        Message.success('创建成功');
        emits('added', formModel.value);
      } else {
        // 修改
        await updatePathBookmark(formModel.value);
        Message.success('修改成功');
        emits('updated', formModel.value);
      }
      handleClose();
      return true;
    } catch (e) {
      return false;
    } finally {
      setLoading(false);
    }
  };

  // 关闭
  const handleClose = () => {
    handleClear();
    setVisible(false);
  };

  // 清空
  const handleClear = () => {
    setLoading(false);
  };

</script>

<style lang="less" scoped>
  .form-container {
    padding: 16px;
  }

</style>
