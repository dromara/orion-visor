<template>
  <a-spin :loading="loading" class="grant-container">
    <!-- 角色列表 -->
    <router-roles outer-class="roles-router-wrapper"
                  v-model="roleId"
                  @change="fetchAuthorizedHostKey" />
    <!-- 分组列表 -->
    <div class="host-key-container">
      <!-- 顶部 -->
      <div class="host-key-header">
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
      <div class="host-key-main">
        <a-table row-key="id"
                 class="host-key-main-table"
                 label-align="left"
                 :loading="loading"
                 :columns="hostKeyColumns"
                 v-model:selected-keys="selectedKeys"
                 :row-selection="rowSelection"
                 :sticky-header="true"
                 :data="hostKeys"
                 :pagination="false"
                 :bordered="false" />
      </div>
    </div>
  </a-spin>
</template>

<script lang="ts">
  export default {
    name: 'host-key-role-grant'
  };
</script>

<script lang="ts" setup>
  import type { HostKeyQueryResponse } from '@/api/asset/host-key';
  import { onMounted, ref } from 'vue';
  import { getAuthorizedHostKey, grantHostKey } from '@/api/asset/asset-data-grant';
  import { AdminRoleCode } from '@/types/const';
  import { Message } from '@arco-design/web-vue';
  import { hostKeyColumns } from '../types/table.columns';
  import useLoading from '@/hooks/loading';
  import { useRowSelection } from '@/types/table';
  import { useCacheStore } from '@/store';
  import RouterRoles from './router-roles.vue';

  const rowSelection = useRowSelection();
  const cacheStore = useCacheStore();
  const { loading, setLoading } = useLoading();

  const roleId = ref();
  const currentRole = ref();
  const hostKeys = ref<Array<HostKeyQueryResponse>>([]);
  const selectedKeys = ref<Array<number>>([]);

  // 获取授权列表
  const fetchAuthorizedHostKey = async (id: number, role: any) => {
    roleId.value = id;
    currentRole.value = role;
    setLoading(true);
    try {
      const { data } = await getAuthorizedHostKey({
        roleId: roleId.value
      });
      selectedKeys.value = data;
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 授权
  const doGrant = async () => {
    setLoading(true);
    try {
      await grantHostKey({
        roleId: roleId.value,
        idList: selectedKeys.value
      });
      Message.success('授权成功');
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 初始化数据
  onMounted(async () => {
    setLoading(true);
    try {
      hostKeys.value = await cacheStore.loadHostKeys();
    } catch (e) {
    } finally {
      setLoading(false);
    }
  });

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

    .host-key-container {
      position: relative;
      width: 100%;
      height: 100%;

      .host-key-header {
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

      .host-key-main {
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
