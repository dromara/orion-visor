<template>
  <a-drawer v-model:visible="visible"
            :width="388"
            :footer="false"
            @close="onClose">
    <!-- 标题 -->
    <template #title>
      <span class="snippet-drawer-title usn">
        命令片段
      </span>
    </template>
    <!-- 命令容器 -->
    <div class="snippet-container">
      <!-- 命令头部 -->
      <div class="snippet-header">
        <!-- 搜索框 -->
        <a-input-search class="snippet-header-input"
                        v-model="filterValue"
                        placeholder="请输入名称/命令"
                        allow-clear />
        <!-- 右侧按钮 -->
        <a-space size="small">
          <!-- 创建命令 -->
          <span class="click-icon-wrapper snippet-header-icon"
                title="创建命令"
                @click="openAdd">
            <icon-plus />
          </span>
          <!-- 刷新 -->
          <span class="click-icon-wrapper snippet-header-icon"
                title="刷新"
                @click="fetchData">
            <icon-refresh />
          </span>
        </a-space>
      </div>
      <!-- 加载中 -->
      <a-skeleton v-if="loading"
                  class="loading-skeleton"
                  :animation="true">
        <a-skeleton-line :rows="4"
                         :line-height="66"
                         :line-spacing="12" />
      </a-skeleton>
      <!-- 无数据 -->
      <a-empty v-else-if="snippetGroups.length === 0 && ungroupedItems.length === 0"
               style="padding: 28px 0">
        <span>暂无数据</span><br>
        <span>点击上方 '<icon-plus />' 添加一条数据吧~</span>
      </a-empty>
      <!-- 命令片段 -->
      <div v-else class="snippet-list-container">
        <!-- 命令片段组 -->
        <a-collapse :bordered="false">
          <template v-for="group in snippetGroups">
            <a-collapse-item v-if="calcGroupTotal(group) > 0"
                             :key="group.id"
                             :header="group.name">
              <!-- 总量 -->
              <template #extra>
                {{ calcGroupTotal(group) }} 条
              </template>
              <!-- 代码片段 -->
              <template v-for="item in group.items">
                <command-snippet-item v-if="item.visible"
                                      :key="item.id"
                                      :item="item"
                                      @copy="(s: string) => copy(s, true)"
                                      @paste="(s: string) => appendCommandToCurrentSession(s)"
                                      @exec="(s: string) => appendCommandToCurrentSession(s, true)"
                                      @remove="remoteSnippet"
                                      @update="(s: CommandSnippetQueryResponse) => formDrawer.openUpdate(s)" />
              </template>
            </a-collapse-item>
          </template>
        </a-collapse>
        <!-- 未分组命令片段 -->
        <div class="ungrouped-snippet-container">
          <template v-for="item in ungroupedItems">
            <command-snippet-item v-if="item.visible"
                                  :key="item.id"
                                  :item="item"
                                  @copy="(s: string) => copy(s, true)"
                                  @paste="(s: string) => appendCommandToCurrentSession(s)"
                                  @exec="(s: string) => appendCommandToCurrentSession(s, true)"
                                  @remove="remoteSnippet"
                                  @update="(s: CommandSnippetQueryResponse) => formDrawer.openUpdate(s)" />
          </template>
        </div>
      </div>
    </div>
    <!-- 命令编辑抽屉 -->
    <command-snippet-form-drawer ref="formDrawer"
                                 @added="onAdded"
                                 @updated="onUpdated" />
  </a-drawer>
</template>

<script lang="ts">
  export default {
    name: 'commandSnippetDrawer'
  };
</script>

<script lang="ts" setup>
  import type { ISshSession } from '../../types/define';
  import type { CommandSnippetQueryResponse } from '@/api/asset/command-snippet';
  import type { CommandSnippetGroupQueryResponse } from '@/api/asset/command-snippet-group';
  import { ref, watch } from 'vue';
  import useVisible from '@/hooks/visible';
  import useLoading from '@/hooks/loading';
  import { deleteCommandSnippet } from '@/api/asset/command-snippet';
  import { useCacheStore, useTerminalStore } from '@/store';
  import { PanelSessionType } from '../../types/const';
  import { copy } from '@/hooks/copy';
  import CommandSnippetItem from './command-snippet-item.vue';
  import CommandSnippetFormDrawer from './command-snippet-form-drawer.vue';

  const { loading, setLoading } = useLoading();
  const { visible, setVisible } = useVisible();
  const { getCurrentSession, appendCommandToCurrentSession } = useTerminalStore();

  const cacheStore = useCacheStore();

  const formDrawer = ref();
  const filterValue = ref<string>('');

  const snippetGroups = ref<Array<CommandSnippetGroupQueryResponse>>([]);
  const ungroupedItems = ref<Array<CommandSnippetQueryResponse>>([]);

  // 打开
  const open = async () => {
    setVisible(true);
    // 加载数据
    await fetchData();
  };

  defineExpose({ open });

  // 加载数据
  const fetchData = async () => {
    setLoading(true);
    try {
      // 查询
      const data = await cacheStore.loadCommandSnippets(true);
      snippetGroups.value = data.groups;
      ungroupedItems.value = data.ungroupedItems;
      // 设置状态
      filterSnippet();
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 计算总量
  const calcGroupTotal = (group: CommandSnippetGroupQueryResponse) => {
    return group.items.filter(s => s.visible).length;
  };

  // 过滤
  const filterSnippet = () => {
    snippetGroups.value.forEach(g => {
      g.items?.forEach(s => {
        s.visible = !filterValue.value
          || s.name.toLowerCase().includes(filterValue.value.toLowerCase())
          || s.command.toLowerCase().includes(filterValue.value.toLowerCase());
      });
    });
    ungroupedItems.value.forEach(s => {
      s.visible = !filterValue.value
        || s.name.toLowerCase().includes(filterValue.value.toLowerCase())
        || s.command.toLowerCase().includes(filterValue.value.toLowerCase());
    });
  };

  // 过滤列表
  watch(filterValue, filterSnippet);

  // 新建
  const openAdd = () => {
    formDrawer.value.openAdd();
  };

  // 查找并且删除
  const findAndSplice = (id: number, items: Array<CommandSnippetQueryResponse>) => {
    if (items) {
      const index = items.findIndex(s => s.id === id);
      if (index !== -1) {
        items.splice(index, 1);
        return true;
      }
      return false;
    }
  };

  // 删除代码片段
  const remoteSnippet = async (id: number) => {
    // 删除
    await deleteCommandSnippet(id);
    // 查找并且删除未分组的数据
    if (findAndSplice(id, ungroupedItems.value)) {
      return;
    }
    // 查找并且删除分组内数据
    for (let group of snippetGroups.value) {
      if (findAndSplice(id, group.items)) {
        return;
      }
    }
  };

  // 添加回调
  const onAdded = async (item: CommandSnippetQueryResponse) => {
    if (item.groupId) {
      let group = snippetGroups.value.find(g => g.id === item.groupId);
      if (group) {
        group?.items.push(item);
      } else {
        const cacheGroups = await cacheStore.loadCommandSnippetGroups();
        const findGroup = cacheGroups.find(s => s.id === item.groupId);
        if (findGroup) {
          snippetGroups.value.push({
            id: item.groupId,
            name: findGroup.name,
            items: [item]
          });
        }
      }
    } else {
      ungroupedItems.value.push(item);
    }
    // 重置过滤
    filterSnippet();
  };

  // 修改回调
  const onUpdated = async (item: CommandSnippetQueryResponse) => {
    // 查找原始数据
    let originItem;
    const findInUngrouped = ungroupedItems.value.find(s => s.id === item.id);
    if (findInUngrouped) {
      originItem = findInUngrouped;
    } else {
      for (let group of snippetGroups.value) {
        const find = group.items.find(s => s.id === item.id);
        if (find) {
          originItem = find;
          break;
        }
      }
    }
    if (!originItem) {
      return;
    }
    // 检查分组是否存在
    const findGroup = snippetGroups.value.find(s => s.id === item.groupId);
    if (!findGroup) {
      const cacheGroups = await cacheStore.loadCommandSnippetGroups();
      const cacheGroup = cacheGroups.find(s => s.id === item.groupId);
      if (cacheGroup) {
        snippetGroups.value.push({
          id: item.groupId,
          name: cacheGroup.name,
          items: []
        });
      }
    }
    // 设置数据
    const originGroupId = originItem.groupId;
    originItem.groupId = item.groupId;
    originItem.name = item.name;
    originItem.command = item.command;
    // 移动分组
    if (item.groupId !== originGroupId) {
      // 从原始分组移除
      if (originGroupId) {
        const findGroup = snippetGroups.value.find(s => s.id === originGroupId);
        if (findGroup) {
          findAndSplice(item.id, findGroup.items);
        }
      } else {
        // 从未分组数据中移除
        findAndSplice(item.id, ungroupedItems.value);
      }
      // 添加到新分组
      if (item.groupId) {
        const findGroup = snippetGroups.value.find(s => s.id === item.groupId);
        if (findGroup) {
          findGroup.items.push(item);
        }
      } else {
        ungroupedItems.value.push(originItem);
      }
    }
    // 重置过滤
    filterSnippet();
  };

  // 关闭回调
  const onClose = () => {
    // 关闭时候如果打开的是终端 则聚焦终端
    getCurrentSession<ISshSession>(PanelSessionType.SSH.type)?.focus();
  };

</script>

<style lang="less" scoped>
  .snippet-drawer-title {
    font-size: 16px;
  }

  .snippet-container {
    position: relative;
    background: var(--color-bg-2);
    height: 100%;
    width: 100%;
    display: block;

    .snippet-header {
      padding: 12px;
      height: 56px;
      display: flex;
      align-items: center;
      justify-content: space-between;

      &-icon {
        width: 32px;
        height: 32px;
        font-size: 16px;
      }

      &-input {
        width: 248px;
        user-select: none;
      }
    }
  }

  .snippet-list-container {
    position: relative;
    height: calc(100% - 56px);
    overflow: auto;
    padding-bottom: 4px;

    &::-webkit-scrollbar-track {
      display: none;
    }

    &::-webkit-scrollbar-thumb {
      background: transparent;
    }

    &:hover::-webkit-scrollbar-thumb {
      background: var(--color-fill-4);
    }

    :deep(.arco-collapse-item) {
      border: none;

      &-header {
        border: none;

        &-title {
          user-select: none;
        }

        &-extra {
          user-select: none;
        }
      }

      &-content {
        background-color: unset;
        padding: 0;
      }

      &-content-box {
        padding: 0;
      }
    }

  }

  .loading-skeleton {
    padding: 0 12px;

    :deep(.arco-skeleton-line-row) {
      border-radius: 4px;
    }
  }

</style>
