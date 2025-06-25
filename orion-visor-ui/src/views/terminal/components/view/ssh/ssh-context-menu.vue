<template>
  <!-- 终端右键菜单 -->
  <a-dropdown class="terminal-context-menu"
              :popup-max-height="false"
              trigger="contextMenu"
              position="bl"
              alignPoint>
    <!-- 终端插槽 -->
    <slot />
    <!-- 右键菜单 -->
    <template v-if="session && preference.interactSetting.enableRightClickMenu" #content>
      <a-doption v-for="(action, index) in actions"
                 :key="index"
                 :disabled="!session || !session.handler.enabledStatus(action.item)"
                 @click="emits('handle', action.item)">
        <!-- 图标 -->
        <div class="terminal-context-menu-icon">
          <component :is="action.icon" />
        </div>
        <!-- 文本 -->
        <div>{{ action.content }}</div>
      </a-doption>
    </template>
  </a-dropdown>
</template>

<script lang="ts">
  export default {
    name: 'sshContextMenu'
  };
</script>

<script lang="ts" setup>
  import type { ISshSession } from '@/views/terminal/interfaces';
  import type { ContextMenuItem } from '@/views/terminal/types/define';
  import { SshActionBarItems } from '@/views/terminal/types/const';
  import { useTerminalStore } from '@/store';

  defineProps<{
    session?: ISshSession;
  }>();

  const emits = defineEmits(['handle']);

  const { preference } = useTerminalStore();

  const actions: Array<ContextMenuItem> = !preference.interactSetting.enableRightClickMenu
    ? []
    : preference.rightMenuSetting
      .map(s => SshActionBarItems.find(i => i.item === s) as ContextMenuItem)
      .filter(Boolean);

</script>

<style lang="less" scoped>

</style>
