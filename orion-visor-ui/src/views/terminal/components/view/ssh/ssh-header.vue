<template>
  <!-- 头部 -->
  <div class="ssh-header">
    <!-- 左侧操作 -->
    <div class="ssh-header-left">
      <!-- 主机地址 -->
      <span class="address-wrapper">
          <span class="text-copy"
                :title="session?.info?.address"
                @click="copy(session?.info?.address as string, true)">
            {{ session?.info?.address }}
        </span>
      </span>
    </div>
    <!-- 右侧操作 -->
    <div class="ssh-header-right">
      <!-- 操作按钮 -->
      <icon-actions class="ssh-header-right-action-bar"
                    wrapper-class="ssh-header-icon-wrapper"
                    icon-class="ssh-header-icon"
                    :actions="rightActions"
                    position="bottom" />
      <!-- 连接状态 -->
      <a-badge v-if="preference.sshActionBarSetting.connectStatus !== false"
               class="status-bridge"
               :status="getDictValue(connectStatusKey, session ? session.state.connectStatus : TerminalStatus.CONNECTING, 'status')"
               :text="getDictValue(connectStatusKey, session ? session.state.connectStatus : TerminalStatus.CONNECTING)" />
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'sshHeader'
  };
</script>

<script lang="ts" setup>
  import type { SidebarAction } from '@/views/terminal/types/define';
  import type { ISshSession } from '@/views/terminal/interfaces';
  import { computed } from 'vue';
  import { useDictStore, useTerminalStore } from '@/store';
  import { SshActionBarItems, connectStatusKey, TerminalStatus } from '@/views/terminal/types/const';
  import { copy } from '@/hooks/copy';
  import IconActions from '../../layout/icon-actions.vue';

  const emits = defineEmits(['handle']);

  const props = defineProps<{
    session?: ISshSession;
  }>();

  const { getDictValue } = useDictStore();
  const { preference } = useTerminalStore();

  // 右侧操作
  const rightActions = computed<Array<SidebarAction>>(() => {
    return SshActionBarItems.map(s => {
      return {
        icon: s.icon,
        content: s.content,
        visible: preference.sshActionBarSetting[s.item] !== false,
        disabled: props.session?.handler?.enabledStatus(s.item) === false,
        click: () => emits('handle', s.item)
      };
    });
  });

</script>

<style lang="less" scoped>
  @ssh-header-height: 36px;

  .ssh-header {
    width: 100%;
    height: @ssh-header-height;
    padding: 0 8px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    background: var(--color-bg-panel-bar);

    &-left, &-right {
      display: flex;
      align-items: center;
      height: 100%;
    }

    &-left {
      .address-wrapper {
        height: 100%;
        display: inline-flex;
        align-items: center;
        user-select: none;

        &:before {
          content: 'IP:';
          padding-right: 4px;
        }
      }
    }

    &-right {
      justify-content: flex-end;
    }

    &-right-action-bar {
      display: flex;

      :deep(.ssh-header-icon-wrapper) {
        width: 28px;
        height: 28px;
        margin: 0 2px;
      }

      :deep(.ssh-header-icon) {
        width: 28px;
        height: 28px;
        font-size: 20px;
      }
    }

    .status-bridge {
      height: 100%;
      margin: 0 2px 0 8px;
      display: flex;
      align-items: center;
      user-select: none;

      :deep(.arco-badge-status-text) {
        width: 36px;
      }

      &::before {
        content: "";
        height: 56%;
        margin: 0 12px 0 6px;
        border-left: 2px solid var(--color-fill-4);
        border-radius: 2px;
      }
    }
  }

</style>
