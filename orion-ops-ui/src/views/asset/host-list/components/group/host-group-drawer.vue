<template>
  <a-drawer v-model:visible="visible"
            class="host-group-drawer"
            width="70%"
            title="主机分组配置"
            :esc-to-close="false"
            :mask-closable="false"
            :unmount-on-close="true"
            ok-text="保存"
            cancel-text="关闭"
            :ok-button-props="{ disabled: loading }"
            :cancel-button-props="{ disabled: loading }"
            :on-before-ok="saveHost"
            @cancel="handleCancel">
    <a-spin :loading="loading"
            class="host-group-container">
      <div class="host-group-wrapper">
        <!-- 左侧菜单 -->
        <div class="simple-card tree-card">
          <!-- 主机分组头部 -->
          <div class="tree-card-header">
            <!-- 标题 -->
            <div class="tree-card-title">
              主机分组
            </div>
            <!-- 操作 -->
            <div class="tree-card-handler">
              <div v-permission="['asset:host-group:create']"
                   class="click-icon-wrapper handler-icon-wrapper"
                   title="根节点添加"
                   @click="addRootNode">
                <icon-plus />
              </div>
              <div class="click-icon-wrapper handler-icon-wrapper"
                   title="刷新"
                   @click="refreshTree">
                <icon-refresh />
              </div>
            </div>
          </div>
          <!-- 主机分组树 -->
          <host-group-tree outer-class="tree-card-main"
                           ref="tree"
                           :loading="loading"
                           @loading="setLoading"
                           @select-node="selectGroup" />
        </div>
        <!-- 身体部分 -->
        <a-spin class="simple-card transfer-body"
                :loading="hostLoading">
          <host-transfer v-model="currentGroupHost"
                         :group="currentGroup"
                         @loading="setHostLoading" />
        </a-spin>
      </div>
    </a-spin>
  </a-drawer>
</template>

<script lang="ts">
  export default {
    name: 'host-group-drawer'
  };
</script>

<script lang="ts" setup>
  import type { TreeNodeData } from '@arco-design/web-vue';
  import { ref } from 'vue';
  import useVisible from '@/hooks/visible';
  import useLoading from '@/hooks/loading';
  import { useCacheStore } from '@/store';
  import { Message } from '@arco-design/web-vue';
  import { updateHostGroupRel } from '@/api/asset/host-group';
  import HostTransfer from './host-transfer.vue';
  import HostGroupTree from '@/components/asset/host-group/host-group-tree.vue';

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();
  const { loading: hostLoading, setLoading: setHostLoading } = useLoading();

  const cacheStore = useCacheStore();

  const tree = ref();
  const currentGroup = ref();
  const currentGroupHost = ref<Array<string>>([]);

  // 打开
  const open = async () => {
    setVisible(true);
  };

  defineExpose({ open });

  // 添加根节点
  const addRootNode = () => {
    tree.value.addRootNode();
  };

  // 刷新树
  const refreshTree = () => {
    tree.value.fetchTreeData(true);
  };

  // 选中分组
  const selectGroup = (group: TreeNodeData) => {
    currentGroup.value = group;
  };

  // 保存主机
  const saveHost = async () => {
    setLoading(true);
    try {
      await updateHostGroupRel({
        groupId: currentGroup.value?.key as number,
        hostIdList: currentGroupHost.value
      });
      Message.success('保存成功');
    } catch (e) {
    } finally {
      setLoading(false);
    }
    return false;
  };

  // 关闭
  const handleCancel = () => {
    setLoading(false);
    setVisible(false);
  };

</script>

<style lang="less" scoped>
  @tree-width: 30%;

  .host-group-container {
    width: 100%;
    height: 100%;
    background-color: var(--color-fill-2);
    padding: 16px;

    .host-group-wrapper {
      position: relative;
      width: 100%;
      height: 100%;
    }
  }

  .tree-card {
    width: @tree-width;
    height: 100%;
    position: absolute;

    &-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 0 8px 0 16px;
      position: relative;
      width: 100%;
      height: 44px;
      border-bottom: 1px var(--color-border-2) solid;
      user-select: none;
    }

    &-title {
      color: rgba(var(--gray-10), .95);
      font-size: 16px;
      font-weight: 600;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
    }

    &-handler {
      display: flex;

      .handler-icon-wrapper {
        margin-left: 2px;
        padding: 4px;
        font-size: 16px;
        background: unset;

        &:hover {
          background: var(--color-fill-3);
        }
      }
    }

    &-main {
      padding: 8px;
      position: relative;
      width: 100%;
      height: calc(100% - 44px);
      overflow: auto;
    }
  }

  .transfer-body {
    display: flex;
    height: 100%;
    padding: 12px 16px 16px 16px;
    width: calc(100% - @tree-width - 16px);
    position: absolute;
    left: calc(@tree-width + 16px);
  }
</style>

<style lang="less">
  .host-group-drawer {
    .arco-drawer-footer {
      padding: 8px;
    }

    .arco-drawer-body {
      padding: 0;
      overflow: hidden;
    }
  }
</style>
