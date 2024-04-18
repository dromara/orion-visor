<template>
  <div>
    <a-tooltip v-for="(action, index) in actions"
               :key="index"
               :position="position as any"
               :mini="true"
               :show-arrow="false"
               content-class="terminal-tooltip-content"
               :auto-fix-position="false"
               :content="action.content">
      <div v-if="action.visible !== false"
           class="terminal-sidebar-icon-wrapper"
           :class="[ wrapperClass ]">
        <div class="terminal-sidebar-icon"
             :class="[
               iconClass,
               action.disabled === true ? 'disabled-item' : '',
               action.checked === true ? 'checked-item' : '',
             ]"
             @click="action.disabled === true ? false : action.click()">
          <component :is="action.icon" :style="action?.iconStyle" />
        </div>
      </div>
    </a-tooltip>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'iconActions'
  };
</script>

<script lang="ts" setup>
  import type { SidebarAction } from '../../types/terminal.type';

  defineProps<Partial<{
    actions: Array<SidebarAction>;
    position: string;
    wrapperClass: string;
    iconClass: string;
  }>>();

</script>

<style lang="less" scoped>

</style>
