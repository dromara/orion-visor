<template>
  <a-spin :loading="loading" class="grant-container">
    <!-- 角色列表 -->
    <router-roles v-if="type === GrantType.ROLE"
                  outer-class="router-wrapper"
                  v-model="subjectId"
                  @change="fetchAuthorizedData" />
    <!-- 角色列表 -->
    <router-users v-else-if="type === GrantType.USER"
                  outer-class="router-wrapper"
                  v-model="subjectId"
                  @change="fetchAuthorizedData" />
    <!-- 数据列表 -->
    <div class="data-container">
      <!-- 顶部 -->
      <div class="data-header">
        <!-- 提示信息 -->
        <a-alert class="alert-wrapper" :show-icon="false">
          <span class="alert-message" v-if="currentSubject">
            <!-- 角色提示信息 -->
            <template v-if="type === GrantType.ROLE">
              当前选择的角色为 <span class="span-blue mr4">{{ currentSubject.text }}</span>
              <span class="span-blue ml4" v-if="currentSubject.code === AdminRoleCode">管理员拥有全部权限, 无需配置</span>
            </template>
            <!-- 用户提示信息 -->
            <template v-else-if="type === GrantType.USER">
              当前选择的用户为 <span class="span-blue mr4">{{ currentSubject.text }}</span>
              <span class="ml4">若当前选择的用户角色包含管理员则无需配置 (管理员拥有全部权限)</span>
            </template>
          </span>
        </a-alert>
        <!-- 授权 -->
        <a-button class="grant-button"
                  type="primary"
                  @click="doGrant">
          授权
          <template #icon>
            <icon-safe />
          </template>
        </a-button>
      </div>
      <!-- 主体部分 -->
      <div class="data-main">
        <slot />
      </div>
    </div>
  </a-spin>
</template>

<script lang="ts">
  export default {
    name: 'grantLayout'
  };
</script>

<script lang="ts" setup>
  import type { TabRouterItem } from '@/components/view/tab-router/types';
  import type { AssetAuthorizedDataQueryRequest, AssetDataGrantRequest } from '@/api/asset/asset-data-grant';
  import { ref } from 'vue';
  import { AdminRoleCode } from '@/types/const';
  import { GrantType } from '../types/const';
  import RouterRoles from './router-roles.vue';
  import RouterUsers from './router-users.vue';

  const props = defineProps<{
    type: string;
    loading: boolean;
  }>();
  const emits = defineEmits(['fetch', 'grant']);

  const subjectId = ref();
  const currentSubject = ref();

  // 获取授权列表
  const fetchAuthorizedData = async (id: number, subject: TabRouterItem) => {
    currentSubject.value = subject;
    if (props.type === GrantType.ROLE) {
      emits('fetch', { roleId: id } as AssetAuthorizedDataQueryRequest);
    } else if (props.type === GrantType.USER) {
      emits('fetch', { userId: id } as AssetAuthorizedDataQueryRequest);
    }
  };

  // 授权
  const doGrant = async () => {
    if (props.type === GrantType.ROLE) {
      emits('grant', { roleId: subjectId.value } as AssetDataGrantRequest);
    } else if (props.type === GrantType.USER) {
      emits('grant', { userId: subjectId.value } as AssetDataGrantRequest);
    }
  };

</script>

<style lang="less" scoped>
  .grant-container {
    width: 100%;
    height: 100%;
    display: flex;
    padding: 0 12px 12px 0;
    position: absolute;

    .router-wrapper {
      margin-right: 16px;
      border-right: 1px var(--color-neutral-3) solid;
    }

    .data-container {
      position: relative;
      width: 100%;
      height: 100%;

      .data-header {
        display: flex;
        justify-content: space-between;
        margin-bottom: 16px;
        align-items: center;

        .alert-wrapper {
          padding: 4px 16px;

          .alert-message {
            display: block;
            height: 22px;
          }
        }

        .grant-button {
          margin-left: 16px;
        }
      }

      .data-main {
        display: flex;
        position: absolute;
        width: 100%;
        height: calc(100% - 48px);
      }
    }
  }

</style>
