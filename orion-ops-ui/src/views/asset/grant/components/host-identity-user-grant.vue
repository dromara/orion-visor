<template>
  <a-spin :loading="loading" class="grant-container">
    <!-- 用户列表 -->
    <router-users outer-class="users-router-wrapper"
                  v-model="userId"
                  @change="fetchAuthorizedHostIdentity" />
    <!-- 分组列表 -->
    <div class="host-identity-container">
      <!-- 顶部 -->
      <div class="host-identity-header">
        <!-- 提示信息 -->
        <a-alert class="alert-wrapper" :show-icon="false">
          <span v-if="currentUser" class="alert-message">
            当前选择的用户为 <span class="span-blue mr4">{{ currentUser?.text }}</span>
            <span class="ml4">若当前选择的用户用户包含管理员则无需配置 (管理员拥有全部权限)</span>
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
    name: 'host-identity-user-grant'
  };
</script>

<script lang="ts" setup>
  import { ref } from 'vue';
  import { getAuthorizedHostIdentity, grantHostIdentity } from '@/api/asset/asset-data-grant';
  import { Message } from '@arco-design/web-vue';
  import useLoading from '@/hooks/loading';
  import { useCacheStore } from '@/store';
  import RouterUsers from './router-users.vue';
  import HostIdentityGrantTable from '@/views/asset/grant/components/host-identity-grant-table.vue';

  const cacheStore = useCacheStore();
  const { loading, setLoading } = useLoading();

  const userId = ref();
  const currentUser = ref();
  const selectedIdentities = ref<Array<number>>([]);

  // 获取授权列表
  const fetchAuthorizedHostIdentity = async (id: number, user: any) => {
    userId.value = id;
    currentUser.value = user;
    setLoading(true);
    try {
      const { data } = await getAuthorizedHostIdentity({
        userId: userId.value
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
        userId: userId.value,
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

    .users-router-wrapper {
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
