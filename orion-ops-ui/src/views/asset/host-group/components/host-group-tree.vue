<template>
  <div class="tree-container">
    <a-tree
      :blockNode="true"
      :draggable="props.editMode"
      :data="treeData">

      <template #title="node">
        <template v-if="node.editable">
          <a-input size="mini"
                   v-model="currName"
                   :max-length="32"
                   :disabled="node.loading"
                   autofocus
                   @change="() => saveNode(node.key)">
            <template #suffix>
              <!-- 加载中 -->
              <icon-loading v-if="node.loading" />
              <!-- 保存 -->
              <icon-check v-else
                          class="pointer"
                          title="保存"
                          @click="saveNode(node.key)" />
            </template>
          </a-input>
        </template>

        <span class="node-title" v-else>
          {{ node.title }}
        </span>
      </template>
      <!-- 操作图标 -->
      <template #drag-icon="{ node }">
        <a-space v-if="!node.editable">
          <icon-edit class="tree-icon"
                     title="重命名"
                     @click="rename(node.title, node.key)" />
          <icon-delete class="tree-icon"
                       title="删除"
                       @click="rename(node.title, node.key)" />
          <icon-plus class="tree-icon"
                     title="新增"
                     @click="rename(node.title, node.key)" />
        </a-space>
      </template>
    </a-tree>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'host-group-tree'
  };
</script>

<script lang="ts" setup>
  import { nextTick, ref } from 'vue';
  import { TagProps } from '@/store/modules/tab-bar/types';
  import { TreeNodeData } from '@arco-design/web-vue';

  const props = defineProps({
    editMode: Boolean
  });

  const currName = ref();


  function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

  // 保存节点
  const saveNode = async (key: string) => {
    // 寻找节点
    const node = findNode(key, treeData.value);
    if (currName.value) {
      node.loading = true;
      try {
        if (key.startsWith('create')) {
          // 调用创建 api
          await sleep(340);
          node.key = 'await id';
        } else {
          // 调用重命名 api
          await sleep(340);
        }
        node.title = currName.value;
      } catch (e) {
      } finally {
        node.loading = false;
      }
    } else {
      if (key.startsWith('create')) {
        // 寻找父节点
        // 移除子节点
      }
    }
    node.editable = false;
  };

  // 重命名
  const rename = (title: string, key: string) => {
    const node = findNode(key, treeData.value);
    currName.value = title;
    node.editable = true;
  };

  // 寻找当前节点
  const findNode = (id: string, arr: Array<TreeNodeData>): TreeNodeData | undefined => {
    for (let node of arr) {
      if (node.key === id) {
        return node;
      }
    }
    // 寻找子级
    for (let node of arr) {
      if (node?.children?.length) {
        const inChildNode = findNode(id, node.children);
        if (inChildNode) {
          return inChildNode;
        }
      }
    }
    return undefined;
  };

  function onIconClick(node: any) {
    const children = node.children || [];
    children.push({
      title: 'new tree node',
      key: node.key + '-' + (children.length + 1)
    });
    node.children = children;

    treeData.value = [...treeData.value];
  }

  const treeData = ref(
    [
      {
        title: 'Trunk',
        key: '0-0',
        children: [
          {
            title: 'Leaf',
            key: '0-0-1',
          },
          {
            title: 'Branch',
            key: '0-0-2',
            children: [
              {
                title: 'Leaf',
                key: '0-0-2-1'
              }
            ]
          },
        ],
      },
      {
        title: 'Trunk',
        key: '0-1',
        children: [
          {
            title: 'Branch',
            key: '0-1-1',
            children: [
              {
                title: 'Leaf',
                key: '0-1-1-11',
              },
              {
                title: 'Leaf',
                key: '0-1-1-12',
              },
              {
                title: 'Leaf',
                key: '0-1-1-13',
              },
              {
                title: 'Leaf',
                key: '0-1-1-41',
              },
              {
                title: 'Leaf',
                key: '0-1-1-51',
              },
              {
                title: 'Leaf',
                key: '0-1-1-61',
              },
              {
                title: 'Leaf',
                key: '0-1-17-1',
              },
              {
                title: 'Leaf',
                key: '0-1-81-1',
              },
              {
                title: 'Leaf',
                key: '0-19-1-1',
              },
              {
                title: 'Leaf',
                key: '0-10-1-1',
              },
              {
                title: 'Leaf',
                key: '0-1-111-1',
              },
              {
                title: 'Leaf',
                key: '0-21-1-1',
              },
              {
                title: 'Leaf',
                key: '0-31-1-1',
              },
              {
                title: 'Leaf',
                key: '40-1-1-2',
              },
            ]
          },
          {
            title: 'Leaf',
            key: '0-1-2',
          },
        ],
      },
    ]
  );
</script>

<style lang="less" scoped>
  .tree-container {
    min-width: 100%;
    width: max-content;
    user-select: none;
  }

  :deep(.arco-tree-node-title) {
    padding-right: 48px;

    &:hover {
      background-color: var(--color-fill-1);
    }

    .arco-tree-node-title-text {
      width: 100%;
      display: flex;
      align-items: center;
    }
  }

  .node-title {

  }

  .node-handler {

  }

  :deep(.arco-tree-node-selected) {
    background-color: var(--color-fill-2);
  }

  .tree-icon {
    font-size: 12px;
    color: rgb(var(--primary-6));
  }

  .drag-icon {
    padding-left: -8px;
  }

</style>
