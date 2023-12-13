<template>
  <div>
    <a-tooltip v-for="(action, index) in actions"
               :key="index"
               :position="position as any"
               :mini="true"
               content-class="terminal-tooltip-content"
               arrow-class="terminal-tooltip-arrow"
               :content="action.content">
      <div class="terminal-sidebar-icon-wrapper"
           v-if="action.visible !== false">
        <div class="terminal-sidebar-icon"
             :class="iconClass"
             @click="action.click">
          <component :is="action.icon" :style="action?.iconStyle" />
        </div>
      </div>
    </a-tooltip>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'icon-actions'
  };
</script>

<script lang="ts" setup>
  import type { SidebarAction } from '../../types/terminal.const';
  import type { PropType } from 'vue';

  defineProps({
    actions: Array as PropType<Array<SidebarAction>>,
    position: String,
    iconClass: String,
  });

</script>

<style lang="less" scoped>

</style>
