<template>
  <a-tabs class="view-container"
          :default-active-key="1"
          :justify="true"
          :destroy-on-hide="true"
          :lazy-load="true">
    <!-- 左侧导航 -->
    <a-tab-pane :key="1">
      <host-group-view-setting />
      <template #title>
        <icon-unordered-list />
        分组配置
      </template>
    </a-tab-pane>
    <!-- 角色分配 -->
    <a-tab-pane :key="2">
      <host-group-view-role-grant />
      <template #title>
        <icon-safe />
        角色授权
      </template>
    </a-tab-pane>
    <!-- 用户分配 -->
    <a-tab-pane :key="3">
      <host-group-view-user-grant />
      <template #title>
        <icon-user />
        用户授权
      </template>
    </a-tab-pane>
  </a-tabs>
</template>

<script lang="ts">
  export default {
    name: 'host-group-view'
  };
</script>

<script lang="ts" setup>
  import HostGroupViewSetting from './host-group-view-setting.vue';
  import HostGroupViewRoleGrant from './host-group-view-role-grant.vue';
  import HostGroupViewUserGrant from './host-group-view-user-grant.vue';

</script>

<style lang="less" scoped>


</style>
