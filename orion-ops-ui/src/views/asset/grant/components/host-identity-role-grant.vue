<template>
  <a-spin :loading="loading" class="grant-container">
    <!-- 角色列表 -->
    <router-roles outer-class="roles-router-wrapper"
                  v-model="roleId"
                  @change="fetchAuthorizedHostIdentity" />
    <!-- 分组列表 -->
    <div class="host-identity-container">
      <!-- 顶部 -->
      <div class="host-identity-header">
        <!-- 提示信息 -->
        <a-alert class="alert-wrapper" :show-icon="false">
          <span v-if="currentRole" class="alert-message">
            当前选择的角色为 <span class="span-blue mr4">{{ currentRole?.text }}</span>
            <span class="span-blue ml4" v-if="currentRole.code === AdminRoleCode">管理员拥有全部权限, 无需配置</span>
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
      <div class="host-identity-main">
        <host-identity-grant-table v-model="selectedIdentities"
                                   @loading="setLoading" />
      </div>
    </div>
  </a-spin>
</template>

<script lang="ts">
  export default {
    name: 'host-identity-role-grant'
  };
</script>

<script lang="ts" setup>
  import { ref } from 'vue';
  import { getAuthorizedHostIdentity, grantHostIdentity } from '@/api/asset/asset-data-grant';
  import { AdminRoleCode } from '@/types/const';
  import { Message } from '@arco-design/web-vue';
  import useLoading from '@/hooks/loading';
  import { useCacheStore } from '@/store';
  import RouterRoles from './router-roles.vue';
  import HostIdentityGrantTable from './host-identity-grant-table.vue';

  const cacheStore = useCacheStore();
  const { loading, setLoading } = useLoading();

  const roleId = ref();
  const currentRole = ref();
  const selectedIdentities = ref<Array<number>>([]);

  // 获取授权列表
  const fetchAuthorizedHostIdentity = async (id: number, role: any) => {
    roleId.value = id;
    currentRole.value = role;
    setLoading(true);
    try {
      const { data } = await getAuthorizedHostIdentity({
        roleId: roleId.value
      });
      selectedIdentities.value = data;
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 授权
  const doGrant = async () => {
    setLoading(true);
    try {
      await grantHostIdentity({
        roleId: roleId.value,
        idList: selectedIdentities.value
      });
      Message.success('授权成功');
    } catch (e) {
    } finally {
      setLoading(false);
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

    .roles-router-wrapper {
      margin-right: 16px;
      border-right: 1px var(--color-neutral-3) solid;
    }

    .host-identity-container {
      position: relative;
      width: 100%;
      height: 100%;

      .host-identity-header {
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

      .host-identity-main {
        display: flex;
        position: absolute;
        width: 100%;
        height: calc(100% - 48px);

        &-table {
          width: 100%;
          height: 100%;
        }
      }
    }
  }

</style>
