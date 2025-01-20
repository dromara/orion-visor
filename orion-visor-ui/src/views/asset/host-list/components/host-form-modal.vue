<template>
  <a-modal v-model:visible="visible"
           modal-class="modal-form-large"
           title-align="start"
           :title="title"
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
              ref="formRef"
              label-align="right"
              :auto-label-width="true"
              :rules="formRules">
        <!-- 主机类型 -->
        <a-form-item v-if="isAddHandle"
                     field="type"
                     label="主机类型"
                     help="主机创建后, 类型则无法修改">
          <a-select v-model="formModel.type"
                    placeholder="请选择主机类型"
                    :options="toOptions(hostTypeKey)"
                    allow-clear />
        </a-form-item>
        <!-- 系统类型 -->
        <a-form-item field="osType" label="系统类型">
          <a-select v-model="formModel.osType"
                    placeholder="请选择系统类型"
                    :options="toOptions(hostOsTypeKey)"
                    allow-clear />
        </a-form-item>
        <!-- 主机名称 -->
        <a-form-item field="name" label="主机名称">
          <a-input v-model="formModel.name"
                   placeholder="请输入主机名称"
                   allow-clear />
        </a-form-item>
        <!-- 主机编码 -->
        <a-form-item field="code" label="主机编码">
          <a-input v-model="formModel.code"
                   placeholder="请输入主机编码 (定义主机唯一值)"
                   allow-clear />
        </a-form-item>
        <!-- 主机地址 -->
        <a-form-item field="address" label="主机地址">
          <a-input v-model="formModel.address"
                   placeholder="请输入主机地址"
                   allow-clear />
        </a-form-item>
        <!-- 主机端口 -->
        <a-form-item field="port" label="主机端口">
          <a-input-number v-model="formModel.port"
                          placeholder="请输入主机端口"
                          hide-button
                          allow-clear />
        </a-form-item>
        <!-- 主机分组 -->
        <a-form-item field="groupIdList" label="主机分组">
          <host-group-tree-selector v-model="formModel.groupIdList"
                                    placeholder="请选择主机分组" />
        </a-form-item>
        <!-- 主机标签 -->
        <a-form-item field="tags" label="主机标签">
          <tag-multi-selector v-model="formModel.tags"
                              type="HOST"
                              :limit="5"
                              :tag-color="tagColor"
                              :allow-create="true"
                              placeholder="请选择主机标签"
                              @on-limited="onLimitedTag" />
        </a-form-item>
        <!-- 主机描述 -->
        <a-form-item field="description" label="主机描述">
          <a-textarea v-model="formModel.description"
                      placeholder="请输入主机描述"
                      allow-clear />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'hostFormModal'
  };
</script>

<script lang="ts" setup>
  import type { HostUpdateRequest } from '@/api/asset/host';
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import formRules from '../types/form.rules';
  import { createHost, getHost, updateHost } from '@/api/asset/host';
  import { Message } from '@arco-design/web-vue';
  import { pick } from 'lodash';
  import { tagColor, HostType, hostTypeKey, hostOsTypeKey, HostOsType } from '../types/const';
  import { useDictStore } from '@/store';
  import TagMultiSelector from '@/components/meta/tag/multi-selector/index.vue';
  import HostGroupTreeSelector from '@/components/asset/host-group/tree-selector/index.vue';

  const { toOptions } = useDictStore();
  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const title = ref<string>();
  const isAddHandle = ref<boolean>(true);

  const defaultForm = (): HostUpdateRequest => {
    return {
      id: undefined,
      type: HostType.SSH.value,
      osType: HostOsType.LINUX.value,
      name: undefined,
      code: undefined,
      address: undefined,
      port: HostType.SSH.port,
      tags: undefined,
      groupIdList: undefined,
      description: undefined,
    };
  };

  const formRef = ref();
  const formModel = ref<HostUpdateRequest>({});

  const emits = defineEmits(['added', 'updated']);

  // 打开新增
  const openAdd = () => {
    title.value = '添加主机';
    isAddHandle.value = true;
    renderForm({ ...defaultForm() });
    setVisible(true);
  };

  // 打开修改
  const openUpdate = async (id: number) => {
    title.value = '修改主机';
    isAddHandle.value = false;
    renderForm({ ...defaultForm() });
    setVisible(true);
    await fetchHostRender(id);
  };

  // 打开复制
  const openCopy = async (id: number) => {
    title.value = '复制主机';
    isAddHandle.value = true;
    renderForm({ ...defaultForm() });
    setVisible(true);
    await fetchHostRender(id);
  };

  // 渲染主机
  const fetchHostRender = async (id: number) => {
    try {
      setLoading(true);
      const { data } = await getHost(id);
      const detail = Object.assign({} as Record<string, any>,
        pick(data, 'id', 'type', 'osType', 'name', 'code', 'address', 'port', 'status', 'groupIdList', 'description'));
      // tag
      const tags = (data.tags || []).map(s => s.id);
      // 渲染
      renderForm({ ...detail, tags });
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 渲染表单
  const renderForm = (record: any) => {
    formModel.value = Object.assign({}, record);
  };

  defineExpose({ openAdd, openUpdate, openCopy });

  // tag 超出所选限制
  const onLimitedTag = (count: number, message: string) => {
    formRef.value.setFields({
      tags: {
        status: 'error',
        message
      }
    });
    // 因为输入框已经限制数量 这里只做提示
    setTimeout(() => {
      formRef.value.clearValidate('tags');
    }, 3000);
  };

  // 确定
  const handlerOk = async () => {
    setLoading(true);
    try {
      // 验证参数
      const error = await formRef.value.validate();
      if (error) {
        return false;
      }
      if (isAddHandle.value) {
        // 新增
        await createHost(formModel.value);
        Message.success('创建成功');
        emits('added');
      } else {
        // 修改
        await updateHost(formModel.value);
        Message.success('修改成功');
        emits('updated');
      }
      // 清空
      handlerClear();
    } catch (e) {
      return false;
    } finally {
      setLoading(false);
    }
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
