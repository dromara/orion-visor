<template>
  <div>
    <a-tooltip v-for="(action, index) in actions"
               :key="index"
               :position="position as any"
               :mini="true"
               :show-arrow="false"
               content-class="host-space-tooltip-content"
               :auto-fix-position="false"
               :content="action.content">
      <div class="host-space-sidebar-icon-wrapper" v-if="action.visible !== false">
        <div class="host-space-sidebar-icon"
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
  import type { SidebarAction } from '../../types/type';
  import type { PropType } from 'vue';

  defineProps({
    actions: Array as PropType<Array<SidebarAction>>,
    position: String,
    iconClass: String,
  });

</script>

<style lang="less" scoped>

</style>
