<template>
  <a-drawer v-model:visible="visible"
            :width="388"
            :footer="false"
            @close="onClose">
    <!-- 表头 -->
    <template #title>
      <span class="snippet-drawer-title usn">
        <icon-code />
        命令片段
      </span>
    </template>
    <!-- 命令容器 -->
    <div class="snippet-container">
      <!-- 命令头部 -->
      <div class="snippet-header">
        <!-- 创建命令 -->
        <span class="click-icon-wrapper snippet-header-icon"
              title="创建命令"
              @click="openAdd">
          <icon-plus />
        </span>
        <!-- 搜索框 -->
        <a-input-search class="snippet-header-input"
                        v-model="filterValue"
                        placeholder="名称"
                        allow-clear
                        @search="filterSnippet"
                        @keyup.enter="filterSnippet" />
      </div>
      <!-- 加载中 -->
      <a-skeleton v-if="loading"
                  style="padding: 0 12px"
                  :animation="true">
        <a-skeleton-line :rows="4"
                         :line-height="66"
                         :line-spacing="12" />
      </a-skeleton>
      <!-- 无数据 -->
      <a-empty v-else-if="!snippet || (snippet.groups.length === 0 && snippet.ungroupedItems.length === 0)"
               style="padding: 28px 0">
        <span>暂无数据</span><br>
        <span>点击上方 '<icon-plus />' 添加一条数据吧~</span>
      </a-empty>
      <!-- 命令片段 -->
      <div v-else class="snippet-list-container">
        <!-- 命令片段组 -->
        <command-snippet-group :snippet="snippet" />
        <!-- 未分组命令片段 -->
        <div class="ungrouped-snippet-container">
          <template v-for="item in snippet.ungroupedItems">
            <command-snippet-item v-if="item.visible"
                                  :key="item.id"
                                  :item="item" />
          </template>
        </div>
      </div>
    </div>
    <!-- 命令编辑抽屉 -->
    <command-snippet-form-drawer ref="formDrawer" />
  </a-drawer>
</template>

<script lang="ts">
  export default {
    name: 'commandSnippetListDrawer'
  };
</script>

<script lang="ts" setup>
  import type { CommandSnippetWrapperResponse, CommandSnippetQueryResponse } from '@/api/asset/command-snippet';
  import { onMounted, ref, provide } from 'vue';
  import useVisible from '@/hooks/visible';
  import useLoading from '@/hooks/loading';
  import { deleteCommandSnippet, getCommandSnippetList } from '@/api/asset/command-snippet';
  import { useTerminalStore } from '@/store';
  import { openUpdateSnippetKey, removeSnippetKey } from '../types/const';
  import CommandSnippetItem from './command-snippet-item.vue';
  import CommandSnippetGroup from './command-snippet-group.vue';
  import CommandSnippetFormDrawer from './command-snippet-form-drawer.vue';

  const { loading, setLoading } = useLoading();
  const { visible, setVisible } = useVisible();
  const { getCurrentTerminalSession } = useTerminalStore();

  const formDrawer = ref();
  const filterValue = ref<string>();
  const snippet = ref<CommandSnippetWrapperResponse>();

  // 打开
  const open = async () => {
    setVisible(true);
    // 加载数据
    await fetchData();
  };

  defineExpose({ open });

  // 加载数据
  const fetchData = async () => {
    if (snippet.value) {
      return;
    }
    setLoading(true);
    try {
      // 查询
      const { data } = await getCommandSnippetList();
      snippet.value = data;
      // 设置状态
      filterSnippet();
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 过滤
  const filterSnippet = () => {
    snippet.value?.groups.forEach(g => {
      g.items?.forEach(s => {
        s.visible = !filterValue.value
          || s.name.toLowerCase().includes(filterValue.value.toLowerCase())
          || s.command.toLowerCase().includes(filterValue.value.toLowerCase());
      });
    });
    snippet.value?.ungroupedItems.forEach(s => {
      s.visible = !filterValue.value
        || s.name.toLowerCase().includes(filterValue.value.toLowerCase())
        || s.command.toLowerCase().includes(filterValue.value.toLowerCase());
    });
  };

  // 新建
  const openAdd = () => {
    formDrawer.value.openAdd();
  };

  // 打开修改
  provide(openUpdateSnippetKey, (e: CommandSnippetQueryResponse) => {
    formDrawer.value.openUpdate(e);
  });

  // 删除
  provide(removeSnippetKey, async (id: number) => {
    if (!snippet.value) {
      return;
    }

    // 查找并且删除
    function findAndSplice(items: Array<CommandSnippetQueryResponse>) {
      if (items) {
        const index = items.findIndex(s => s.id === id);
        if (index !== -1) {
          items.splice(index, 1);
          return true;
        }
        return false;
      }
    }

    // 删除
    await deleteCommandSnippet(id);

    // 查找并且删除未分组的数据
    if (findAndSplice(snippet.value.ungroupedItems)) {
      return;
    }
    // 查找并且删除分组内数据
    for (let group of snippet.value.groups) {
      if (findAndSplice(group.items)) {
        return;
      }
    }
  });

  // 关闭
  const onClose = () => {
    // 聚焦终端
    getCurrentTerminalSession(false)?.focus();
  };

  // fixme
  onMounted(open);

</script>

<style lang="less" scoped>
  .snippet-drawer-title {
    font-size: 14px;
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
        width: 220px;
        user-select: none;
      }
    }
  }

  .snippet-list-container {
    position: relative;
    height: calc(100% - 56px);
    overflow: auto;
    padding-bottom: 4px;
  }

</style>
