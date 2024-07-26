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
        <!-- 左侧按钮 -->
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
                @click="fetchData(true)">
          <icon-refresh />
        </span>
        </a-space>
        <!-- 搜索框 -->
        <a-input-search class="snippet-header-input"
                        v-model="filterValue"
                        placeholder="名称/命令"
                        allow-clear />
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
      <a-empty v-else-if="!snippetValue || (snippetValue.groups.length === 0 && snippetValue.ungroupedItems.length === 0)"
               style="padding: 28px 0">
        <span>暂无数据</span><br>
        <span>点击上方 '<icon-plus />' 添加一条数据吧~</span>
      </a-empty>
      <!-- 命令片段 -->
      <div v-else class="snippet-list-container">
        <!-- 命令片段组 -->
        <command-snippet-list-group :value="snippetValue" />
        <!-- 未分组命令片段 -->
        <div class="ungrouped-snippet-container">
          <template v-for="item in snippetValue.ungroupedItems">
            <command-snippet-list-item v-if="item.visible"
                                       :key="item.id"
                                       :item="item" />
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
  import type { CommandSnippetWrapperResponse, CommandSnippetQueryResponse } from '@/api/asset/command-snippet';
  import { ref, watch, provide } from 'vue';
  import useVisible from '@/hooks/visible';
  import useLoading from '@/hooks/loading';
  import { deleteCommandSnippet, getCommandSnippetList } from '@/api/asset/command-snippet';
  import { useCacheStore, useTerminalStore } from '@/store';
  import { openUpdateSnippetKey, removeSnippetKey } from './types/const';
  import { PanelSessionType } from '../../types/const';
  import CommandSnippetListItem from './command-snippet-list-item.vue';
  import CommandSnippetListGroup from './command-snippet-list-group.vue';
  import CommandSnippetFormDrawer from './command-snippet-form-drawer.vue';

  const { loading, setLoading } = useLoading();
  const { visible, setVisible } = useVisible();
  const { getCurrentSession } = useTerminalStore();
  const cacheStore = useCacheStore();

  const formDrawer = ref();
  const filterValue = ref<string>();
  const snippetValue = ref<CommandSnippetWrapperResponse>();

  // 打开
  const open = async () => {
    setVisible(true);
    // 加载数据
    await fetchData();
  };

  defineExpose({ open });

  // 加载数据
  const fetchData = async (force: boolean = false) => {
    if (snippetValue.value && !force) {
      return;
    }
    setLoading(true);
    try {
      // 查询
      const { data } = await getCommandSnippetList();
      snippetValue.value = data;
      // 设置状态
      filterSnippet();
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 过滤
  const filterSnippet = () => {
    snippetValue.value?.groups.forEach(g => {
      g.items?.forEach(s => {
        s.visible = !filterValue.value
          || s.name.toLowerCase().includes(filterValue.value.toLowerCase())
          || s.command.toLowerCase().includes(filterValue.value.toLowerCase());
      });
    });
    snippetValue.value?.ungroupedItems.forEach(s => {
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

  // 暴露 修改抽屉
  provide(openUpdateSnippetKey, (e: CommandSnippetQueryResponse) => {
    formDrawer.value.openUpdate(e);
  });

  // 暴露 删除
  provide(removeSnippetKey, async (id: number) => {
    if (!snippetValue.value) {
      return;
    }
    // 删除
    await deleteCommandSnippet(id);
    // 查找并且删除未分组的数据
    if (findAndSplice(id, snippetValue.value.ungroupedItems)) {
      return;
    }
    // 查找并且删除分组内数据
    for (let group of snippetValue.value.groups) {
      if (findAndSplice(id, group.items)) {
        return;
      }
    }
  });

  // 添加回调
  const onAdded = async (item: CommandSnippetQueryResponse) => {
    if (item.groupId) {
      let group = snippetValue.value?.groups.find(g => g.id === item.groupId);
      if (group) {
        group?.items.push(item);
      } else {
        const cacheGroups = await cacheStore.loadCommandSnippetGroups();
        const findGroup = cacheGroups.find(s => s.id === item.groupId);
        if (findGroup) {
          snippetValue.value?.groups.push({
            id: item.groupId,
            name: findGroup.name,
            items: [item]
          });
        }
      }
    } else {
      snippetValue.value?.ungroupedItems.push(item);
    }
    // 重置过滤
    filterSnippet();
  };

  // 修改回调
  const onUpdated = async (item: CommandSnippetQueryResponse) => {
    if (!snippetValue.value) {
      return;
    }
    // 查找原始数据
    let originItem;
    const findInUngrouped = snippetValue.value.ungroupedItems.find(s => s.id === item.id);
    if (findInUngrouped) {
      originItem = findInUngrouped;
    } else {
      for (let group of snippetValue.value.groups) {
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
    const findGroup = snippetValue.value.groups.find(s => s.id === item.groupId);
    if (!findGroup) {
      const cacheGroups = await cacheStore.loadCommandSnippetGroups();
      const cacheGroup = cacheGroups.find(s => s.id === item.groupId);
      if (cacheGroup) {
        snippetValue.value.groups.push({
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
        const findGroup = snippetValue.value.groups.find(s => s.id === originGroupId);
        if (findGroup) {
          findAndSplice(item.id, findGroup.items);
        }
      } else {
        // 从未分组数据中移除
        findAndSplice(item.id, snippetValue.value.ungroupedItems);
      }
      // 添加到新分组
      if (item.groupId) {
        const findGroup = snippetValue.value.groups.find(s => s.id === item.groupId);
        if (findGroup) {
          findGroup.items.push(item);
        }
      } else {
        snippetValue.value.ungroupedItems.push(originItem);
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
  }

  .loading-skeleton {
    padding: 0 12px;

    :deep(.arco-skeleton-line-row) {
      border-radius: 4px;
    }
  }

</style>
