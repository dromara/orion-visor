<template>
  <a-scrollbar>
    <!-- 分组树 -->
    <a-tree v-if="treeData.length"
            v-model:checked-keys="checkedKeys"
            ref="tree"
            class="tree-container block-tree"
            :class="[ editable ? 'editable-tree' : '' ]"
            :blockNode="true"
            :data="treeData"
            :draggable="editable"
            :checkable="checkable"
            :check-strictly="true"
            @drop="moveGroup"
            @select="(s: any) => emits('onSelected', s)">
      <!-- 标题 -->
      <template #title="node">
        <!-- 修改名称输入框 -->
        <template v-if="node.editable">
          <a-input size="mini"
                   ref="renameInput"
                   v-model="node.title"
                   style="width: 138px;"
                   placeholder="名称"
                   :max-length="32"
                   :disabled="node.loading"
                   @blur="saveNode(node)"
                   @press-enter="saveNode(node)"
                   @change="saveNode(node)">
            <template #suffix>
              <!-- 加载中 -->
              <icon-loading v-if="node.loading" />
              <!-- 保存 -->
              <icon-check v-else
                          class="pointer"
                          title="保存"
                          @click="saveNode(node)" />
            </template>
          </a-input>
        </template>
        <!-- 名称 -->
        <span v-else
              class="node-title-wrapper"
              @click="() => emits('selectedNode', node)">
          {{ node.title }}
        </span>
      </template>
      <!-- 操作图标 -->
      <template #drag-icon="{ node }">
        <a-space v-if="!node.editable">
          <!-- 重命名 -->
          <span v-permission="['asset:host-group:update']"
                class="tree-icon"
                title="重命名"
                @click="rename(node.title, node.key)">
            <icon-edit />
          </span>
          <!-- 删除 -->
          <a-popconfirm content="确认删除这条记录吗?"
                        position="left"
                        type="warning"
                        @ok="deleteNode(node.key)">
            <span v-permission="['asset:host-group:delete']"
                  class="tree-icon"
                  title="删除">
              <icon-delete />
            </span>
          </a-popconfirm>
          <!-- 新增 -->
          <span v-permission="['asset:host-group:create']"
                class="tree-icon"
                title="新增"
                @click="addChildren(node)">
            <icon-plus />
          </span>
        </a-space>
      </template>
    </a-tree>
    <!-- 无数据 -->
    <a-empty v-else-if="!loading" class="empty-container">
      <span>暂无数据</span><br>
      <span v-if="editable">点击上方 '<icon-plus />' 添加一个分组吧~</span>
    </a-empty>
  </a-scrollbar>
</template>

<script lang="ts">
  export default {
    name: 'hostGroupTree'
  };
</script>

<script lang="ts" setup>
  import type { TreeNodeData } from '@arco-design/web-vue';
  import { computed, nextTick, onMounted, ref } from 'vue';
  import { createGroupGroupPrefix, rootId } from '../const';
  import { findNode, findParentNode, moveNode } from '@/utils/tree';
  import { createHostGroup, deleteHostGroup, updateHostGroupName, moveHostGroup } from '@/api/asset/host-group';
  import { isString } from '@/utils/is';
  import { useCacheStore } from '@/store';

  const props = withDefaults(defineProps<Partial<{
    loading: boolean;
    editable: boolean;
    checkable: boolean;
    checkedKeys: Array<number>;
  }>>(), {
    editable: false,
    checkable: false,
  });
  const emits = defineEmits(['setLoading', 'onSelected', 'selectedNode', 'update:checkedKeys']);

  const cacheStore = useCacheStore();

  const tree = ref();
  const renameInput = ref();
  const treeData = ref<Array<TreeNodeData>>([]);

  const checkedKeys = computed<Array<number>>({
    get() {
      return props.checkedKeys as Array<number>;
    },
    set(e) {
      if (e) {
        emits('update:checkedKeys', e);
      } else {
        emits('update:checkedKeys', []);
      }
    }
  });

  // 重命名
  const rename = (title: number, key: number) => {
    const node = findNode<TreeNodeData>(key, treeData.value);
    if (!node) {
      return;
    }
    node.editable = true;
    node.originTitle = node.title;
    nextTick(() => {
      renameInput.value?.focus();
    });
  };

  // 删除节点
  const deleteNode = async (key: number) => {
    try {
      emits('setLoading', true);
      // 删除
      await deleteHostGroup(key);
      // 页面删除
      const parentNode = findParentNode<TreeNodeData>(key, treeData.value);
      if (!parentNode) {
        return;
      }
      const children = parentNode.root ? treeData.value : parentNode.children;
      if (children) {
        // 删除
        for (let i = 0; i < children.length; i++) {
          if (children[i].key === key) {
            children.splice(i, 1);
            break;
          }
        }
      }
    } catch (e) {
    } finally {
      emits('setLoading', false);
    }
  };

  // 新增根节点
  const addRootNode = () => {
    const newKey = `${createGroupGroupPrefix}${Date.now()}`;
    treeData.value.push({
      title: '',
      key: newKey
    });
    // 编辑子节点
    const newNode = findNode<TreeNodeData>(newKey, treeData.value);
    if (newNode) {
      newNode.editable = true;
      nextTick(() => {
        renameInput.value?.focus();
      });
    }
  };

  // 新增子节点
  const addChildren = (parentNode: TreeNodeData) => {
    const newKey = `${createGroupGroupPrefix}${Date.now()}`;
    const children = parentNode.children || [];
    children.push({
      title: '',
      key: newKey
    });
    parentNode.children = children;
    treeData.value = [...treeData.value];
    nextTick(() => {
      // 展开
      tree.value.expandNode(parentNode.key);
      // 编辑子节点
      const newNode = findNode<TreeNodeData>(newKey, treeData.value);
      if (newNode) {
        newNode.editable = true;
        nextTick(() => {
          renameInput.value?.focus();
        });
      }
    });
  };

  // 保存节点
  const saveNode = async (node: TreeNodeData) => {
    const key = node.key;
    const newTitle = node.title;
    node.modCount = (node.modCount || 0) + 1;
    if (node.modCount != 1) {
      return;
    }
    if (newTitle) {
      node.loading = true;
      try {
        // 创建节点
        if (isString(key) && key.startsWith(createGroupGroupPrefix)) {
          const parent = findParentNode<TreeNodeData>(key, treeData.value);
          if (parent.root) {
            parent.key = rootId;
          }
          // 创建
          const { data } = await createHostGroup({
            parentId: parent.key as number,
            name: newTitle
          });
          node.key = data;
        } else {
          // 重命名节点
          await updateHostGroupName({
            id: key as unknown as number,
            name: newTitle
          });
        }
        node.editable = false;
      } catch (e) {
        // 重复 重新聚焦
        setTimeout(() => {
          renameInput.value?.focus();
        }, 100);
      } finally {
        node.loading = false;
      }
    } else {
      // 未输入数据 并且为创建则移除节点
      if (isString(key) && key.startsWith(createGroupGroupPrefix)) {
        // 寻找父节点
        const parent = findParentNode(key, treeData.value);
        if (parent) {
          // 根节点
          if (parent.root) {
            parent.children = treeData.value;
          }
          // 移除子节点
          if (parent.children) {
            for (let i = 0; i < parent.children.length; i++) {
              if (parent.children[i].key === key) {
                parent.children.splice(i, 1);
              }
            }
          }
        }
      } else {
        // 修改为空则设置为之前的值
        node.title = node.originTitle;
      }
      node.editable = false;
    }
    // 重置 modCount
    setTimeout(() => {
      node.modCount = 0;
      node.originTitle = undefined;
    }, 50);
  };

  // 移动分组
  const moveGroup = async (
    {
      dragNode, dropNode, dropPosition
    }: {
      dragNode: TreeNodeData,
      dropNode: TreeNodeData,
      dropPosition: number
    }) => {
    try {
      emits('setLoading', true);
      // 移动
      await moveHostGroup({
        id: dragNode.key as number,
        targetId: dropNode.key as number,
        position: dropPosition
      });
      // 移动分组
      moveNode(treeData.value, dragNode, dropNode, dropPosition);
    } catch (e) {
    } finally {
      emits('setLoading', false);
    }
  };

  // 加载数据
  const fetchTreeData = async (force = false) => {
    try {
      emits('setLoading', true);
      const groups = await cacheStore.loadHostGroups(force);
      treeData.value = groups || [];
    } catch (e) {
    } finally {
      emits('setLoading', false);
    }
    // 未选择则选择首个
    if (!tree.value?.getSelectedNodes()?.length && treeData.value.length) {
      await nextTick(() => {
        tree.value?.selectNode(treeData.value[0].key);
        emits('selectedNode', treeData.value[0]);
      });
    }
  };

  defineExpose({ addRootNode, fetchTreeData });

  onMounted(() => {
    fetchTreeData();
  });

</script>

<style lang="less" scoped>
  :deep(.arco-scrollbar-container) {
    height: 100%;
    overflow-y: auto;
  }

  .tree-container {
    min-width: 100%;
    width: max-content;
    user-select: none;
    overflow: hidden;

    :deep(.arco-tree-node-title) {
      padding: 0;
    }

    .node-title-wrapper {
      width: 100%;
      height: 100%;
      display: flex;
      align-items: center;
      padding-left: 8px;
    }

    .tree-icon {
      font-size: 12px;
      color: rgb(var(--primary-6));
    }
  }

  .editable-tree {
    :deep(.arco-tree-node-title) {
      padding: 0 80px 0 0;
    }
  }

  .empty-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    height: 100%;
    padding-top: 25px;
    color: var(--color-text-3);
  }

</style>
