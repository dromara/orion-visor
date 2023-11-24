<template>
  <a-spin :loading="loading" class="grant-container">
    <!-- 左侧用户列表 -->
    <div class="user-container">
      <!-- 用户列表 -->
      <tab-router v-if="usersRouter.length"
                  class="user-router"
                  v-model="userId"
                  :items="usersRouter" />
      <!-- 暂无数据 -->
      <a-empty v-else class="user-empty">
        <div slot="description">
          暂无用户数据
        </div>
      </a-empty>
    </div>
    <!-- 右侧菜单列表 -->
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
        <!-- 保存 -->
        <a-button class="save-button"
                  type="primary"
                  @click="save">
          保存
          <template #icon>
            <icon-check />
          </template>
        </a-button>
      </div>
      <!-- 主题部分 -->
      <div class="group-main">
        <!-- 菜单 -->
        <div class="group-main-tree">
          <host-group-tree :checkable="true"
                           :checked-keys="checkedGroups"
                           :draggable="false"
                           :loading="loading"
                           @loading="setLoading"
                           @select-node="e => selectedGroup = e"
                           @update:checked-keys="updateCheckedGroups" />
        </div>
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
  import type { TabRouterItem } from '@/components/view/tab-router/types';
  import type { TreeNodeData } from '@arco-design/web-vue';
  import { ref, onMounted, watch } from 'vue';
  import { useCacheStore } from '@/store';
  import useLoading from '@/hooks/loading';
  import { getAuthorizedHostGroup, grantHostGroup } from '@/api/asset/host-group';
  import { Message } from '@arco-design/web-vue';
  import HostGroupTree from './host-group-tree.vue';
  import HostList from './host-list.vue';

  const { loading, setLoading } = useLoading();
  const cacheStore = useCacheStore();

  const userId = ref();
  const currentUser = ref();
  const usersRouter = ref<Array<TabRouterItem>>([]);
  const authorizedGroups = ref<Array<number>>([]);
  const checkedGroups = ref<Array<number>>([]);
  const selectedGroup = ref<TreeNodeData>({});

  // 监听用户变更 获取授权列表
  watch(userId, async (value) => {
    if (!value) {
      return;
    }
    currentUser.value = usersRouter.value.find(s => s.key === value);
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
  });

  // 选择分组
  const updateCheckedGroups = (e: Array<number>) => {
    checkedGroups.value = e;
  };

  // 保存
  const save = async () => {
    setLoading(true);
    try {
      await grantHostGroup({
        userId: userId.value,
        groupIdList: checkedGroups.value
      });
      Message.success('保存成功');
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 加载主机
  onMounted(() => {
    usersRouter.value = cacheStore.users.map(s => {
      return {
        key: s.id,
        text: `${s.nickname} (${s.username})`
      };
    });
  });

</script>

<style lang="less" scoped>
  .grant-container {
    width: 100%;
    padding: 16px;
    display: flex;

    .user-container {
      margin-right: 16px;

      .user-router {
        height: 100%;
        min-width: max-content;
        border-right: 1px var(--color-neutral-3) solid;
      }

      .user-empty {
        width: 198px;
      }
    }

    .group-container {
      width: 100%;

      .group-header {
        display: flex;
        justify-content: space-between;
        margin-bottom: 12px;
        align-items: center;

        .alert-wrapper {
          padding: 4px 16px;
        }

        .save-button {
          margin-left: 16px;
        }
      }

      .group-main {
        display: flex;

        &-tree {
          width: calc(60% - 16px);
          margin-right: 16px;
        }

        &-hosts {
          width: 40%;
        }
      }
    }
  }

  :deep(.tab-item) {
    margin-left: 0;
  }

</style>
