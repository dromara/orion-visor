<template>
  <a-drawer v-model:visible="visible"
            :width="388"
            :footer="false"
            @close="emits('closed')">
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
        <!-- 搜索框 -->
        <a-input-search class="path-header-input"
                        v-model="filterValue"
                        placeholder="请输入名称/路径"
                        allow-clear />
        <!-- 右侧侧按钮 -->
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
      <a-empty v-else-if="bookmarkGroups.length === 0 && ungroupedItems.length === 0"
               style="padding: 28px 0">
        <span>暂无数据</span><br>
        <span>点击上方 '<icon-plus />' 添加一条数据吧~</span>
      </a-empty>
      <!-- 路径书签 -->
      <div v-else class="path-list-container">
        <!-- 路径书签组 -->
        <a-collapse :bordered="false">
          <template v-for="group in bookmarkGroups">
            <a-collapse-item v-if="calcGroupTotal(group) > 0"
                             :key="group.id"
                             :header="group.name">
              <!-- 总量 -->
              <template #extra>
                {{ calcGroupTotal(group) }} 条
              </template>
              <!-- 路径 -->
              <template v-for="item in group.items">
                <path-bookmark-item v-if="item.visible"
                                    :key="item.id"
                                    :item="item"
                                    @copy="(s: string) => copy(s, true)"
                                    @paste="(s: string) => appendCommandToCurrentSession(s)"
                                    @exec="(s: string) => appendCommandToCurrentSession(s, true)"
                                    @change="(s: string) => changePath(s)"
                                    @update="(s: PathBookmarkQueryResponse) => formDrawer.openUpdate(s)"
                                    @remove="removeBookmark" />
              </template>
            </a-collapse-item>
          </template>
        </a-collapse>
        <!-- 未分组路径书签 -->
        <div class="ungrouped-path-container">
          <template v-for="item in ungroupedItems">
            <path-bookmark-item v-if="item.visible"
                                :key="item.id"
                                :item="item"
                                @copy="(s: string) => copy(s, true)"
                                @paste="(s: string) => appendCommandToCurrentSession(s)"
                                @exec="(s: string) => appendCommandToCurrentSession(s, true)"
                                @change="(s: string) => changePath(s)"
                                @update="(s: PathBookmarkQueryResponse) => formDrawer.openUpdate(s)"
                                @remove="removeBookmark" />
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
  import type { ISftpSession } from '../../types/define';
  import type { PathBookmarkQueryResponse } from '@/api/asset/path-bookmark';
  import type { PathBookmarkGroupQueryResponse } from '@/api/asset/path-bookmark-group';
  import { ref, watch } from 'vue';
  import useVisible from '@/hooks/visible';
  import useLoading from '@/hooks/loading';
  import { deletePathBookmark } from '@/api/asset/path-bookmark';
  import { useCacheStore, useTerminalStore } from '@/store';
  import { PanelSessionType } from '../../types/const';
  import { copy } from '@/hooks/copy';
  import PathBookmarkItem from './path-bookmark-item.vue';
  import PathBookmarkFormDrawer from './path-bookmark-form-drawer.vue';

  const emits = defineEmits(['closed']);

  const { loading, setLoading } = useLoading();
  const { visible, setVisible } = useVisible();
  const { getCurrentSession, appendCommandToCurrentSession } = useTerminalStore();
  const cacheStore = useCacheStore();

  const formDrawer = ref();
  const filterValue = ref<string>();
  const bookmarkGroups = ref<Array<PathBookmarkGroupQueryResponse>>([]);
  const ungroupedItems = ref<Array<PathBookmarkQueryResponse>>([]);

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
      const data = await cacheStore.loadPathBookmarks(true);
      bookmarkGroups.value = data.groups;
      ungroupedItems.value = data.ungroupedItems;
      // 设置状态
      filterPath();
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 过滤
  const filterPath = () => {
    bookmarkGroups.value.forEach(g => {
      g.items?.forEach(s => {
        s.visible = !filterValue.value
          || s.name.toLowerCase().includes(filterValue.value.toLowerCase())
          || s.path.toLowerCase().includes(filterValue.value.toLowerCase());
      });
    });
    ungroupedItems.value.forEach(s => {
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

  // 计算总量
  const calcGroupTotal = (group: PathBookmarkGroupQueryResponse) => {
    return group.items.filter(s => s.visible).length;
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

  // 查询 sftp 文件列表
  const changePath = (path: string) => {
    getCurrentSession<ISftpSession>(PanelSessionType.SFTP.type, true)?.list(path);
  };

  // 删除书签路径
  const removeBookmark = async (id: number) => {
    // 删除
    await deletePathBookmark(id);
    // 查找并且删除未分组的数据
    if (findAndSplice(id, ungroupedItems.value)) {
      return;
    }
    // 查找并且删除分组内数据
    for (let group of bookmarkGroups.value) {
      if (findAndSplice(id, group.items)) {
        return;
      }
    }
  };

  // 添加回调
  const onAdded = async (item: PathBookmarkQueryResponse) => {
    if (item.groupId) {
      let group = bookmarkGroups.value.find(g => g.id === item.groupId);
      if (group) {
        group?.items.push(item);
      } else {
        const cacheGroups = await cacheStore.loadPathBookmarkGroups();
        const findGroup = cacheGroups.find(s => s.id === item.groupId);
        if (findGroup) {
          bookmarkGroups.value.push({
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
    filterPath();
  };

  // 修改回调
  const onUpdated = async (item: PathBookmarkQueryResponse) => {
    // 查找原始数据
    let originItem;
    const findInUngrouped = ungroupedItems.value.find(s => s.id === item.id);
    if (findInUngrouped) {
      originItem = findInUngrouped;
    } else {
      for (let group of bookmarkGroups.value) {
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
    const findGroup = bookmarkGroups.value.find(s => s.id === item.groupId);
    if (!findGroup) {
      const cacheGroups = await cacheStore.loadPathBookmarkGroups();
      const cacheGroup = cacheGroups.find(s => s.id === item.groupId);
      if (cacheGroup) {
        bookmarkGroups.value.push({
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
        const findGroup = bookmarkGroups.value.find(s => s.id === originGroupId);
        if (findGroup) {
          findAndSplice(item.id, findGroup.items);
        }
      } else {
        // 从未分组数据中移除
        findAndSplice(item.id, ungroupedItems.value);
      }
      // 添加到新分组
      if (item.groupId) {
        const findGroup = bookmarkGroups.value.find(s => s.id === item.groupId);
        if (findGroup) {
          findGroup.items.push(item);
        }
      } else {
        ungroupedItems.value.push(originItem);
      }
    }
    // 重置过滤
    filterPath();
  };

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
