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
        <a-button class="terminal-sidebar-icon"
                  :class="[
                    iconClass,
                    action.checked === true ? 'checked-item' : '',
                  ]"
                  :disabled="action.disabled === true"
                  @click="action.click()">
          <component :is="action.icon" :style="action?.iconStyle" />
        </a-button>
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
  import type { SidebarAction } from '../../types/define';

  defineProps<Partial<{
    actions: Array<SidebarAction>;
    position: string;
    wrapperClass: string;
    iconClass: string;
  }>>();

</script>

<style lang="less" scoped>
  .terminal-sidebar-icon[disabled]:hover {
    background: var(--color-secondary-disabled);
  }
</style>
