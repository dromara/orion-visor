<template>
  <a-drawer class="drawer-body-padding-0"
            :width="420"
            :visible="visible"
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
        主机配置 <span class="host-name-title-text">{{ record.name }}</span>
      </span>
    </template>
    <a-spin :loading="loading" class="host-config-container">
      <!-- SSH 配置 -->
      <host-config-ssh-form :content="config.SSH" @submitted="(e) => config.SSH.config = e" />
    </a-spin>
  </a-drawer>
</template>

<script lang="ts">
  export default {
    name: 'host-config-drawer'
  };
</script>

<script lang="ts" setup>
  import { ref } from 'vue';
  import useVisible from '@/hooks/visible';
  import useLoading from '@/hooks/loading';
  import { Message } from '@arco-design/web-vue';
  import { getHostConfigAll } from '@/api/asset/host';
  import HostConfigSshForm from './host-config-ssh-form.vue';

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const record = ref();
  const config = ref<Record<string, any>>({});

  // 打开
  const open = async (e: any) => {
    record.value = { ...e };
    try {
      setLoading(true);
      setVisible(true);
      // 加载配置
      const { data } = await getHostConfigAll({ hostId: record.value.id });
      data.forEach(s => {
        config.value[s.type] = s;
      });
    } catch ({ message }) {
      Message.error(`配置加载失败 ${message}`);
      setVisible(false);
    } finally {
      setLoading(false);
    }
  };

  const handleOk = () => {
    console.log('ok');
    setLoading(true);
    setTimeout(() => {
      setVisible(false);
      setLoading(false);
    }, 1000);
  };

  const handleCancel = () => {
    console.log('cancel');
    setLoading(false);
    setVisible(false);
  };

  defineExpose({ open });

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
    height: 100%;
    background-color: var(--color-fill-2);
  }

  .arco-card {
    margin: 18px 18px 0 18px;
  }
</style>
