<template>
  <a-spin :loading="loading">
    <a-form :model="formModel"
            ref="formRef"
            label-align="right"
            :auto-label-width="true"
            :rules="hostFormRules">
      <!-- 主机协议 -->
      <a-form-item field="types" label="主机协议">
        <a-select v-model="formModel.types"
                  placeholder="请选择支持的主机协议"
                  :options="toOptions(hostTypeKey)"
                  multiple
                  allow-clear />
      </a-form-item>
      <!-- 系统类型 -->
      <a-form-item field="osType" label="系统类型">
        <a-select v-model="formModel.osType"
                  placeholder="请选择系统类型"
                  :options="toOptions(hostOsTypeKey)"
                  allow-clear />
      </a-form-item>
      <!-- 系统架构 -->
      <a-form-item field="archType" label="系统架构">
        <a-select v-model="formModel.archType"
                  placeholder="请选择系统架构"
                  :options="toOptions(hostArchTypeKey)"
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
                    :auto-size="{ minRows: 3, maxRows: 3}"
                    allow-clear />
      </a-form-item>
      <!-- 保存 -->
      <a-form-item style="margin-bottom: 0;">
        <a-button type="primary"
                  long
                  @click="saveHost">
          保存
        </a-button>
      </a-form-item>
    </a-form>
  </a-spin>
</template>

<script lang="ts">
  export default {
    name: 'hostFormInfo'
  };
</script>

<script lang="ts" setup>
  import type { HostUpdateRequest } from '@/api/asset/host';
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import { hostFormRules } from '../types/form.rules';
  import { createHost, getHost, updateHost } from '@/api/asset/host';
  import { Message } from '@arco-design/web-vue';
  import { pick } from 'lodash';
  import { tagColor, hostTypeKey, hostOsTypeKey, HostOsType, hostArchTypeKey } from '../types/const';
  import { useDictStore } from '@/store';
  import TagMultiSelector from '@/components/meta/tag/multi-selector/index.vue';
  import HostGroupTreeSelector from '@/components/asset/host-group/tree-selector/index.vue';

  const emits = defineEmits(['changeType', 'updated']);

  const { toOptions } = useDictStore();
  const { loading, setLoading } = useLoading();

  const defaultForm = (): HostUpdateRequest => {
    return {
      id: undefined,
      osType: HostOsType.LINUX.value,
      archType: 'AMD64',
      types: [],
      name: undefined,
      code: undefined,
      address: undefined,
      tags: undefined,
      groupIdList: undefined,
      description: undefined,
    };
  };

  const formRef = ref();
  const formModel = ref<HostUpdateRequest>({});

  // 打开新增
  const openAdd = () => {
    renderForm({ ...defaultForm() });
  };

  // 打开修改
  const openUpdate = async (id: number) => {
    renderForm({ ...defaultForm() });
    await fetchHostRender(id);
  };

  // 打开复制
  const openCopy = async (id: number) => {
    renderForm({ ...defaultForm() });
    await fetchHostRender(id);
  };

  defineExpose({ openAdd, openUpdate, openCopy });

  // 渲染主机
  const fetchHostRender = async (id: number) => {
    try {
      setLoading(true);
      const { data } = await getHost(id);
      const detail = Object.assign({} as Record<string, any>,
        pick(data, 'id', 'types', 'osType', 'archType', 'name', 'code', 'address', 'port', 'status', 'groupIdList', 'description'));
      // tag
      const tags = (data.tags || []).map(s => s.id);
      // 渲染
      renderForm({ ...detail, tags });
      // 响应类型
      emits('changeType', data.types);
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 渲染表单
  const renderForm = (record: any) => {
    // 渲染表单
    formModel.value = Object.assign({}, record);
  };

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

  // 保存
  const saveHost = async () => {
    setLoading(true);
    try {
      // 验证参数
      const error = await formRef.value.validate();
      if (error) {
        return;
      }
      if (!formModel.value.id) {
        // 新增
        const { data } = await createHost(formModel.value);
        Message.success('创建成功');
        emits('updated', data);
      } else {
        // 修改
        await updateHost(formModel.value);
        Message.success('修改成功');
        emits('updated', formModel.value.id);
      }
      emits('changeType', formModel.value.types);
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

</script>

<style lang="less" scoped>

</style>
