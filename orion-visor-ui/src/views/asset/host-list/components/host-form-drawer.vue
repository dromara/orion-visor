<template>
  <a-drawer v-model:visible="visible"
            :title="title"
            :width="600"
            :esc-to-close="false"
            :mask-closable="false"
            :unmount-on-close="true"
            :footer="false"
            @close="handleClose">
    <div class="host-from-container">
      <a-tabs v-model:active-key="activeTab"
              class=""
              type="rounded"
              direction="vertical"
              :justify="true"
              :lazy-load="true">
        <!-- 主机信息 -->
        <a-tab-pane v-permission="['asset:host:update']"
                    key="info"
                    title="主机信息">
          <host-form-info ref="infoRef"
                          class="form-panel"
                          @change-type="(ts: string[]) => types = ts"
                          @updated="onUpdateHostInfo" />
        </a-tab-pane>
        <!-- 规格配置 -->
        <a-tab-pane v-permission="['asset:host:update']"
                    key="spec"
                    title="规格配置"
                    :disabled="!hostId">
          <host-form-spec v-if="hostId"
                          class="form-panel"
                          :hostId="hostId"
                          @updated="onUpdateHostSpec" />
        </a-tab-pane>
        <!-- SSH 配置 -->
        <a-tab-pane v-permission="['asset:host:update-config']"
                    key="ssh"
                    title="SSH"
                    :disabled="!hostId || !types.includes(HostType.SSH.value)">
          <host-form-ssh v-if="hostId"
                         class="form-panel"
                         :hostId="hostId" />
        </a-tab-pane>
        <!-- RDP 配置 -->
        <a-tab-pane v-permission="['asset:host:update-config']"
                    key="rdp"
                    title="RDP"
                    :disabled="!hostId || !types.includes(HostType.RDP.value)">
          <host-form-rdp v-if="hostId"
                         class="form-panel"
                         :hostId="hostId" />
        </a-tab-pane>
      </a-tabs>
    </div>
  </a-drawer>
</template>

<script lang="ts">
  export default {
    name: 'hostFormDrawer'
  };
</script>

<script lang="ts" setup>
  import { ref, nextTick } from 'vue';
  import useVisible from '@/hooks/visible';
  import { Message } from '@arco-design/web-vue';
  import { useCacheStore } from '@/store';
  import { HostType } from '../types/const';
  import HostFormInfo from './host-form-info.vue';
  import HostFormSpec from './host-form-spec.vue';
  import HostFormSsh from './host-form-ssh.vue';
  import HostFormRdp from './host-form-rdp.vue';

  const { visible, setVisible } = useVisible();

  const activeTab = ref<string>('info');
  const title = ref<string>();
  const hostId = ref<number>();
  const types = ref<string[]>([]);
  const infoRef = ref();
  const hostViewUpdated = ref(false);

  const emits = defineEmits(['reload']);

  // 打开新增
  const openAdd = () => {
    init('添加主机', undefined);
    nextTick(() => {
      infoRef.value.openAdd();
    });
  };

  // 打开修改
  const openUpdate = (id: number) => {
    init('修改主机', id);
    nextTick(() => {
      infoRef.value.openUpdate(id);
    });
  };

  // 打开复制
  const openCopy = (id: number) => {
    init('复制主机', undefined);
    nextTick(() => {
      infoRef.value.openCopy(id);
    });
  };

  // 初始化
  const init = (_title: string, id: number | undefined) => {
    title.value = _title;
    activeTab.value = 'info';
    hostId.value = id;
    hostViewUpdated.value = false;
    types.value = [];
    checkHostGroup();
    setVisible(true);
  };

  // 检查是否有主机分组
  const checkHostGroup = () => {
    useCacheStore().loadHostGroupTree().then(s => {
      if (!s?.length) {
        Message.warning('请先配置主机分组');
        setVisible(false);
      }
    });
  };

  defineExpose({ openAdd, openUpdate, openCopy });

  // 更新主机信息
  const onUpdateHostInfo = (id: number) => {
    title.value = '修改主机';
    hostId.value = id;
    hostViewUpdated.value = true;
  };

  // 更新主机信息
  const onUpdateHostSpec = () => {
    hostViewUpdated.value = true;
  };

  // 处理关闭
  const handleClose = () => {
    // 修改主机视图信息后刷新列表
    if (hostViewUpdated.value) {
      emits('reload');
    }
  };

</script>

<style lang="less" scoped>
  @drawer-width: 600px;
  @nav-width: 104px;

  .host-from-container {
    height: 100%;
    width: 100%;
    padding: 16px 0 16px 16px;
    display: flex;
    overflow: hidden;

    :deep(.arco-tabs-pane) {
      border-left: 1px var(--color-neutral-3) solid;
    }

    :deep(.arco-tabs-nav) {
      user-select: none;
    }

    :deep(.arco-tabs-content) {
      width: @drawer-width - @nav-width;
      overflow: auto;
    }

    :deep(.extra-button) {
      margin-left: 8px;
      width: 168px;
    }
  }

  .form-panel {
    width: 100%;
    height: 100%;
    padding: 0 24px;
  }
</style>
