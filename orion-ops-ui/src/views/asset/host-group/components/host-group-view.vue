<template>
  <div class="view-container">
    <!-- 左侧导航 -->
    <tab-router class="left-tabs"
                v-model="key"
                :items="tabItems" />
    <!-- 右侧内容 -->
    <div class="view-main">
      <!-- 分组配置 -->
      <template v-if="key === tabItemKeys.SETTING">
        <host-group-view-setting />
      </template>
      <!-- 角色分配 -->
      <template v-if="key === tabItemKeys.ROLE_GRANT">
        <host-group-view-role-grant />
      </template>
      <!-- 用户分配 -->
      <template v-if="key === tabItemKeys.USER_GRANT">
        <host-group-view-user-grant />
      </template>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'host-group-view'
  };
</script>

<script lang="ts" setup>
  import { ref } from 'vue';
  import { tabItems, tabItemKeys } from '../types/const';
  import HostGroupViewSetting from './host-group-view-setting.vue';
  import HostGroupViewRoleGrant from './host-group-view-role-grant.vue';
  import HostGroupViewUserGrant from './host-group-view-user-grant.vue';

  const key = ref();


</script>

<style lang="less" scoped>
  @tab-width: 138px;

  .view-container {
    display: flex;
    width: 100%;
    height: 100%;
    position: relative;
  }

  .left-tabs {
    display: flex;
    flex-direction: column;
    width: @tab-width;
    height: 100%;
    margin-right: 16px;
  }

  .view-main {
    width: calc(100% - @tab-width - 16px);
    height: 100%;
    position: relative;
  }

</style>
