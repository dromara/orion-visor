<template>
  <a-drawer v-model:visible="visible"
            :width="460"
            :esc-to-close="false"
            :mask-closable="false"
            :unmount-on-close="true"
            :ok-button-props="{ disabled: loading }"
            :cancel-button-props="{ disabled: loading }"
            :footer="false"
            @cancel="handleCancel">
    <!-- 标题 -->
    <template #title>
      <span class="host-title-text">
        主机配置 <span class="host-name-title-text">{{ record?.name }}</span>
      </span>
    </template>
    <a-spin :loading="loading" class="host-config-container">
      <!-- SSH 配置 -->
      <ssh-config-form class="host-config-wrapper"
                       :host-config="hostConfig"
                       @save="save"
                       @reset="reset" />
    </a-spin>
  </a-drawer>
</template>

<script lang="ts">
  export default {
    name: 'hostConfigDrawer'
  };
</script>

<script lang="ts" setup>
  import type { HostConfigQueryResponse } from '@/api/asset/host';
  import { ref } from 'vue';
  import useVisible from '@/hooks/visible';
  import useLoading from '@/hooks/loading';
  import { Message } from '@arco-design/web-vue';
  import { useCacheStore, useDictStore } from '@/store';
  import { dictKeys } from '../types/const';
  import { getHostConfig, updateHostConfig } from '@/api/asset/host';
  import SshConfigForm from '../components/ssh-config-form.vue';

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();
  const cacheStore = useCacheStore();

  const record = ref({} as any);
  const hostConfig = ref<HostConfigQueryResponse>({} as HostConfigQueryResponse);

  // 打开
  const open = async (e: any) => {
    record.value = { ...e };
    hostConfig.value = { config: {} } as HostConfigQueryResponse;
    try {
      setLoading(true);
      setVisible(true);
      // 加载字典值
      const dictStore = useDictStore();
      await dictStore.loadKeys(dictKeys);
      // 加载配置
      const { data } = await getHostConfig(record.value.id);
      data.current = Date.now();
      hostConfig.value = data;
    } catch ({ message }) {
      Message.error(`配置加载失败 ${message}`);
      setVisible(false);
    } finally {
      setLoading(false);
    }
  };

  defineExpose({ open });

  // 保存
  const save = async (conf: Record<string, any>) => {
    try {
      setLoading(true);
      // 更新
      await updateHostConfig({
        id: hostConfig.value.id,
        config: JSON.stringify(conf)
      });
      // 设置参数
      hostConfig.value.config = conf;
      Message.success('修改成功');
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 重置
  const reset = () => {
    // 修改 current 让子组件重新渲染
    hostConfig.value.current = Date.now();
  };

  // 关闭
  const handleCancel = () => {
    setLoading(false);
    setVisible(false);
  };

</script>

<style lang="less" scoped>
  .host-title-text {
    width: 368px;
    display: flex;
    font-size: 16px;
    line-height: 16px;
    font-weight: 600;

    .host-name-title-text {
      max-width: 288px;
      margin-left: 8px;
      font-size: 14px;
      display: inline-block;
      white-space: nowrap;
      text-overflow: ellipsis;
      overflow: hidden;
      color: rgb(var(--arcoblue-6));
    }

    .host-name-title-text::before {
      content: '(';
    }

    .host-name-title-text::after {
      content: ')';
    }
  }

  .host-config-container {
    width: 100%;
    min-height: 100%;
    background-color: var(--color-fill-2);
  }

  .host-config-wrapper {
    margin: 18px;
  }

  :deep(.config-title-wrapper) {
    display: flex;
    align-items: center;
    justify-content: space-between;

    .title{
      font-weight: 600;
      user-select: none;
    }
  }

  :deep(.config-button-group) {
    display: flex;
    align-items: center;
    justify-content: flex-end;
  }

</style>
