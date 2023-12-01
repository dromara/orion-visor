<template>
  <a-spin :loading="loading" class="grant-container">
    <!-- 用户列表 -->
    <router-users v-model="userId" @change="fetchAuthorizedGroup" />
    <!-- 分组列表 -->
    <div class="group-container">
      <!-- 顶部 -->
      <div class="group-header">
        <!-- 提示信息 -->
        <a-alert class="alert-wrapper" :show-icon="false">
          <span v-if="currentUser">
            当前选择的用户为 <span class="span-blue mr4">{{ currentUser?.text }}</span>
            <span class="ml4">若当前选择的用户角色包含管理员则无需配置 (管理员拥有全部权限)</span>
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
      <!-- 主题部分 -->
      <div class="group-main">
        <!-- 分组 -->
        <host-group-tree outer-class="group-main-tree"
                         :checked-keys="checkedGroups"
                         :draggable="false"
                         :loading="loading"
                         @loading="setLoading"
                         @select-node="e => selectedGroup = e"
                         @update:checked-keys="updateCheckedGroups" />
        <!-- 主机列表 -->
        <host-list class="group-main-hosts"
                   :group="selectedGroup" />
      </div>
    </div>
  </a-spin>
</template>

<script lang="ts">
  export default {
    name: 'host-group-view-user-grant'
  };
</script>

<script lang="ts" setup>
  import type { TreeNodeData } from '@arco-design/web-vue';
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import { getAuthorizedHostGroup, grantHostGroup } from '@/api/asset/asset-data-grant';
  import { Message } from '@arco-design/web-vue';
  import HostGroupTree from '@/components/asset/host-group/host-group-tree.vue';
  import HostList from './host-list.vue';
  import RouterUsers from './router-users.vue';

  const { loading, setLoading } = useLoading();

  const userId = ref();
  const currentUser = ref();
  const authorizedGroups = ref<Array<number>>([]);
  const checkedGroups = ref<Array<number>>([]);
  const selectedGroup = ref<TreeNodeData>({});

  // 获取授权列表
  const fetchAuthorizedGroup = async (id: number, user: any) => {
    userId.value = id;
    currentUser.value = user;
    setLoading(true);
    try {
      const { data } = await getAuthorizedHostGroup({
        userId: userId.value
      });
      authorizedGroups.value = data;
      checkedGroups.value = data;
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 选择分组
  const updateCheckedGroups = (e: Array<number>) => {
    checkedGroups.value = e;
  };

  // 授权
  const doGrant = async () => {
    setLoading(true);
    try {
      await grantHostGroup({
        userId: userId.value,
        idList: checkedGroups.value
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

    .group-container {
      position: relative;
      width: 100%;
      height: 100%;

      .group-header {
        display: flex;
        justify-content: space-between;
        margin-bottom: 16px;
        align-items: center;

        .alert-wrapper {
          padding: 4px 16px;
        }

        .grant-button {
          margin-left: 16px;
        }
      }

      .group-main {
        display: flex;
        position: absolute;
        width: 100%;
        height: calc(100% - 48px);

        &-tree {
          width: calc(60% - 16px);
          height: 100%;
          margin-right: 16px;
        }

        &-hosts {
          width: 40%;
        }
      }
    }
  }

</style>
