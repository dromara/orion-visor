<template>
  <!-- 终端右键菜单 -->
  <a-dropdown class="terminal-context-menu"
              trigger="contextMenu"
              :popup-max-height="false"
              position="bl"
              alignPoint>
    <!-- 终端插槽 -->
    <slot />
    <!-- 右键菜单 -->
    <template v-if="session && preference.interactSetting.enableRightClickMenu" #content>
      <a-doption v-for="(action, index) in actions"
                 :key="index"
                 :disabled="!session.handler.enabledStatus(action.item)"
                 @click="emits('click', action.item)">
        <!-- 图标 -->
        <div class="action-icon">
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
    name: 'terminalContextMenu'
  };
</script>

<script lang="ts" setup>
  import type { ContextMenuItem, ITerminalSession } from '../../types/terminal.type';
  import { ActionBarItems } from '../../types/terminal.const';
  import { useTerminalStore } from '@/store';

  defineProps<{
    session: ITerminalSession | undefined
  }>();

  const emits = defineEmits(['click']);

  const { preference } = useTerminalStore();

  const actions: Array<ContextMenuItem> = !preference.interactSetting.enableRightClickMenu
    ? []
    : preference.rightMenuSetting
      .map(s => ActionBarItems.find(i => i.item === s) as ContextMenuItem)
      .filter(Boolean);

</script>

<style lang="less" scoped>

  .action-icon {
    font-size: 16px;
    margin: 0 8px 0 4px;
  }

</style>
