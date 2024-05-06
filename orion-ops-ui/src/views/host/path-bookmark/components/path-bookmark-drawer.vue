<template>
  <a-drawer v-model:visible="visible"
            :width="388"
            :footer="false"
            @close="onClose">
    <!-- 标题 -->
    <template #title>
      <span class="path-drawer-title usn">
        路径书签
      </span>
    </template>
    <!-- 路径容器 -->
    <div class="path-container">
      <!-- 路径头部 -->
      <div class="path-header">
        <!-- 左侧按钮 -->
        <a-space size="small">
          <!-- 创建路径 -->
          <span class="click-icon-wrapper path-header-icon"
                title="创建路径"
                @click="openAdd">
          <icon-plus />
        </span>
          <!-- 刷新 -->
          <span class="click-icon-wrapper path-header-icon"
                title="刷新"
                @click="fetchData(true)">
          <icon-refresh />
        </span>
        </a-space>
        <!-- 搜索框 -->
        <a-input-search class="path-header-input"
                        v-model="filterValue"
                        placeholder="名称/路径"
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
      <a-empty v-else-if="!pathBookmarkData || (pathBookmarkData.groups.length === 0 && pathBookmarkData.ungroupedItems.length === 0)"
               style="padding: 28px 0">
        <span>暂无数据</span><br>
        <span>点击上方 '<icon-plus />' 添加一条数据吧~</span>
      </a-empty>
      <!-- 路径书签 -->
      <div v-else class="path-list-container">
        <!-- 路径书签组 -->
        <path-bookmark-list-group :value="pathBookmarkData" />
        <!-- 未分组路径书签 -->
        <div class="ungrouped-path-container">
          <template v-for="item in pathBookmarkData.ungroupedItems">
            <path-bookmark-list-item v-if="item.visible"
                                     :key="item.id"
                                     :item="item" />
          </template>
        </div>
      </div>
    </div>
    <!-- 路径编辑抽屉 -->
    <path-bookmark-form-drawer ref="formDrawer"
                               @added="onAdded"
                               @updated="onUpdated" />
  </a-drawer>
</template>

<script lang="ts">
  export default {
    name: 'pathBookmarkDrawer'
  };
</script>

<script lang="ts" setup>
  import { ISshSession } from '@/views/host/terminal/types/terminal.type';
  import type { PathBookmarkWrapperResponse, PathBookmarkQueryResponse } from '@/api/asset/path-bookmark';
  import { ref, provide, onMounted, watch } from 'vue';
  import useVisible from '@/hooks/visible';
  import useLoading from '@/hooks/loading';
  import { deletePathBookmark, getPathBookmarkList } from '@/api/asset/path-bookmark';
  import { useCacheStore, useDictStore, useTerminalStore } from '@/store';
  import { PanelSessionType } from '@/views/host/terminal/types/terminal.const';
  import { dictKeys, openUpdatePathKey, removePathKey } from '../types/const';
  import PathBookmarkListItem from './path-bookmark-list-item.vue';
  import PathBookmarkListGroup from './path-bookmark-list-group.vue';
  import PathBookmarkFormDrawer from './path-bookmark-form-drawer.vue';

  const { loading, setLoading } = useLoading();
  const { visible, setVisible } = useVisible();
  const { getCurrentSession } = useTerminalStore();
  const cacheStore = useCacheStore();

  const formDrawer = ref();
  const filterValue = ref<string>();
  const pathBookmarkData = ref<PathBookmarkWrapperResponse>();

  // 打开
  const open = async () => {
    setVisible(true);
    // 加载数据
    await fetchData();
  };

  defineExpose({ open });

  // 加载数据
  const fetchData = async (force: boolean = false) => {
    if (pathBookmarkData.value && !force) {
      return;
    }
    setLoading(true);
    try {
      // 查询
      const { data } = await getPathBookmarkList();
      pathBookmarkData.value = data;
      // 设置状态
      filterPath();
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 过滤
  const filterPath = () => {
    pathBookmarkData.value?.groups.forEach(g => {
      g.items?.forEach(s => {
        s.visible = !filterValue.value
          || s.name.toLowerCase().includes(filterValue.value.toLowerCase())
          || s.path.toLowerCase().includes(filterValue.value.toLowerCase());
      });
    });
    pathBookmarkData.value?.ungroupedItems.forEach(s => {
      s.visible = !filterValue.value
        || s.name.toLowerCase().includes(filterValue.value.toLowerCase())
        || s.path.toLowerCase().includes(filterValue.value.toLowerCase());
    });
  };

  // 过滤列表
  watch(filterValue, filterPath);

  // 新建
  const openAdd = () => {
    formDrawer.value.openAdd();
  };

  // 查找并且删除
  const findAndSplice = (id: number, items: Array<PathBookmarkQueryResponse>) => {
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
  provide(openUpdatePathKey, (e: PathBookmarkQueryResponse) => {
    formDrawer.value.openUpdate(e);
  });

  // 暴露 删除
  provide(removePathKey, async (id: number) => {
    if (!pathBookmarkData.value) {
      return;
    }
    // 删除
    await deletePathBookmark(id);
    // 查找并且删除未分组的数据
    if (findAndSplice(id, pathBookmarkData.value.ungroupedItems)) {
      return;
    }
    // 查找并且删除分组内数据
    for (let group of pathBookmarkData.value.groups) {
      if (findAndSplice(id, group.items)) {
        return;
      }
    }
  });

  // 添加回调
  const onAdded = async (item: PathBookmarkQueryResponse) => {
    if (item.groupId) {
      let group = pathBookmarkData.value?.groups.find(g => g.id === item.groupId);
      if (group) {
        group?.items.push(item);
      } else {
        const cacheGroups = await cacheStore.loadPathBookmarkGroups();
        const findGroup = cacheGroups.find(s => s.id === item.groupId);
        if (findGroup) {
          pathBookmarkData.value?.groups.push({
            id: item.groupId,
            name: findGroup.name,
            items: [item]
          });
        }
      }
    } else {
      pathBookmarkData.value?.ungroupedItems.push(item);
    }
    // 重置过滤
    filterPath();
  };

  // 修改回调
  const onUpdated = async (item: PathBookmarkQueryResponse) => {
    if (!pathBookmarkData.value) {
      return;
    }
    // 查找原始数据
    let originItem;
    const findInUngrouped = pathBookmarkData.value.ungroupedItems.find(s => s.id === item.id);
    if (findInUngrouped) {
      originItem = findInUngrouped;
    } else {
      for (let group of pathBookmarkData.value.groups) {
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
    const findGroup = pathBookmarkData.value.groups.find(s => s.id === item.groupId);
    if (!findGroup) {
      const cacheGroups = await cacheStore.loadPathBookmarkGroups();
      const cacheGroup = cacheGroups.find(s => s.id === item.groupId);
      if (cacheGroup) {
        pathBookmarkData.value.groups.push({
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
    originItem.path = item.path;
    // 移动分组
    if (item.groupId !== originGroupId) {
      // 从原始分组移除
      if (originGroupId) {
        const findGroup = pathBookmarkData.value.groups.find(s => s.id === originGroupId);
        if (findGroup) {
          findAndSplice(item.id, findGroup.items);
        }
      } else {
        // 从未分组数据中移除
        findAndSplice(item.id, pathBookmarkData.value.ungroupedItems);
      }
      // 添加到新分组
      if (item.groupId) {
        const findGroup = pathBookmarkData.value.groups.find(s => s.id === item.groupId);
        if (findGroup) {
          findGroup.items.push(item);
        }
      } else {
        pathBookmarkData.value.ungroupedItems.push(originItem);
      }
    }
    // 重置过滤
    filterPath();
  };

  // 关闭回调
  const onClose = () => {
    // 关闭时候如果打开的是终端 则聚焦终端
    getCurrentSession<ISshSession>(PanelSessionType.SSH.type)?.focus();
  };

  // 加载字典值
  onMounted(() => {
    useDictStore().loadKeys(dictKeys);
  });

</script>

<style lang="less" scoped>
  .path-drawer-title {
    font-size: 16px;
  }

  .path-container {
    position: relative;
    background: var(--color-bg-2);
    height: 100%;
    width: 100%;
    display: block;

    .path-header {
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

  .path-list-container {
    position: relative;
    height: calc(100% - 56px);
    overflow: auto;
    padding-bottom: 4px;
  }

  .loading-skeleton {
    padding: 0 12px;

    :deep(.arco-skeleton-line-row) {
      border-radius: 4px;
    }
  }

</style>
