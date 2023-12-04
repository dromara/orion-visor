<template>
  <!-- 搜索 -->
  <a-card class="general-card table-search-card">
    <a-row>
      <!-- 搜索框 -->
      <a-col :span="12">
        <a-form :model="formModel"
                ref="formRef"
                label-align="left"
                @keyup.enter="loadMenuData">
          <a-row :gutter="32">
            <!-- 菜单名称 -->
            <a-col :span="12">
              <a-form-item field="name" label="菜单名称" label-col-flex="60px">
                <a-input v-model="formModel.name" placeholder="请输入菜单名称" allow-clear />
              </a-form-item>
            </a-col>
            <!-- 菜单状态 -->
            <a-col :span="12">
              <a-form-item field="status" label="菜单状态" label-col-flex="60px">
                <a-select v-model="formModel.status"
                          :options="toOptions(menuStatusKey)"
                          placeholder="请选择菜单状态"
                          allow-clear />
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </a-col>
      <!-- 操作 -->
      <a-col :span="12" class="table-right-bar-handle">
        <a-space>
          <!-- 新增 -->
          <a-button type="primary"
                    @click="emits('openAdd',{ parentId: 0, sort: getMaxSort(tableRenderData) })"
                    v-permission="['infra:system-menu:create']">
            新增
            <template #icon>
              <icon-plus />
            </template>
          </a-button>
          <!-- 查询 -->
          <a-button type="primary" :loading="fetchLoading" @click="loadMenuData">
            查询
            <template #icon>
              <icon-search />
            </template>
          </a-button>
          <!-- 重置 -->
          <a-button @click="resetForm" :loading="fetchLoading">
            重置
            <template #icon>
              <icon-refresh />
            </template>
          </a-button>
          <!-- 折叠/展开 -->
          <a-button @click="toggleExpand">
            {{ expandStatus ? '折叠' : '展开' }}
            <template #icon>
              <icon-shrink v-if="expandStatus" />
              <icon-expand v-else />
            </template>
          </a-button>
          <!-- 刷新缓存 -->
          <a-popconfirm content="确定要刷新全局菜单缓存吗?"
                        position="left"
                        type="warning"
                        @ok="doRefreshCache">
            <a-button type="primary" status="warning"
                      v-permission="['infra:system-menu:refresh-cache']">
              刷新缓存
              <template #icon>
                <icon-sync />
              </template>
            </a-button>
          </a-popconfirm>
        </a-space>
      </a-col>
    </a-row>
  </a-card>
  <!-- 表格 -->
  <a-card class="general-card table-card">
    <a-table row-key="id"
             class="table-wrapper-16"
             ref="tableRef"
             label-align="left"
             :loading="fetchLoading"
             :pagination="false"
             :columns="columns"
             :data="tableRenderData"
             :bordered="false">
      <!-- 菜单名称 -->
      <template #menuName="{ record }">
        <span class="ml8">{{ record.name }}</span>
      </template>
      <!-- 图标 -->
      <template #icon="{ record }">
        <component v-if="record.icon" :is="record.icon" />
      </template>
      <!-- 类型 -->
      <template #type="{ record }">
        <a-tag>{{ getDictValue(menuTypeKey, record.type) }}</a-tag>
      </template>
      <!-- 状态 -->
      <template #status="{ record }">
        <a-space>
          <!-- 菜单状态 -->
          <a-popconfirm v-if="hasPermission('infra:system-menu:update-status')"
                        position="top"
                        type="warning"
                        :content="`确定要将当前节点以及所有子节点改为${toggleDictValue(menuStatusKey, record.status, 'label')}?`"
                        @ok="updateStatus(record.id, toggleDictValue(menuStatusKey, record.status))">
            <a-tooltip content="点击切换状态">
              <a-tag :color="getDictValue(menuStatusKey, record.status, 'color')" class="pointer">
                {{ getDictValue(menuStatusKey, record.status) }}
              </a-tag>
            </a-tooltip>
          </a-popconfirm>
          <a-tag v-else :color="getDictValue(menuStatusKey, record.status, 'color')">
            {{ getDictValue(menuStatusKey, record.status) }}
          </a-tag>
          <!-- 显示状态 -->
          <a-popconfirm v-if="hasPermission('infra:system-menu:update-status')"
                        position="top"
                        type="warning"
                        :content="`确定要将当前节点以及所有子节点改为${toggleDictValue(menuVisibleKey, record.visible, 'label')}?`"
                        @ok="updateVisible(record.id, toggleDictValue(menuVisibleKey, record.visible))">
            <a-tooltip content="点击切换状态">
              <a-tag v-if="(record.visible || record.visible === 0) && record.type !== MenuType.FUNCTION"
                     :color="getDictValue(menuVisibleKey, record.visible, 'color')"
                     class="pointer">
                {{ getDictValue(menuVisibleKey, record.visible) }}
              </a-tag>
            </a-tooltip>
          </a-popconfirm>
          <a-tag v-else-if="(record.visible || record.visible === 0) && record.type !== MenuType.FUNCTION"
                 :color="getDictValue(menuVisibleKey, record.visible, 'color')">
            {{ getDictValue(menuVisibleKey, record.visible) }}
          </a-tag>
        </a-space>
      </template>
      <!-- 操作 -->
      <template #handle="{ record }">
        <div class="table-handle-wrapper">
          <!-- 新增 -->
          <a-button type="text"
                    size="mini"
                    v-if="record.type !== MenuType.FUNCTION"
                    v-permission="['infra:system-menu:create']"
                    @click="emits('openAdd', { parentId: record.id, type: record.type, sort: getMaxSort(record.children) })">
            新增
          </a-button>
          <!-- 修改 -->
          <a-button type="text"
                    size="mini"
                    v-permission="['infra:system-menu:update']"
                    @click="emits('openUpdate', record)">
            修改
          </a-button>
          <!-- 删除 -->
          <a-popconfirm content="确认删除这条记录吗?"
                        position="left"
                        type="warning"
                        @ok="doDeleteMenu(record)">
            <a-button type="text" size="mini"
                      status="danger"
                      v-permission="['infra:system-menu:delete']">
              删除
            </a-button>
          </a-popconfirm>
        </div>
      </template>
    </a-table>
  </a-card>
</template>

<script lang="ts">
  export default {
    name: 'menu-table'
  };
</script>

<script lang="ts" setup>
  import type { MenuQueryRequest, MenuQueryResponse } from '@/api/system/menu';
  import { reactive, ref, onMounted } from 'vue';
  import useLoading from '@/hooks/loading';
  import { getMenuList, deleteMenu, updateMenuStatus, refreshCache } from '@/api/system/menu';
  import { menuStatusKey, menuVisibleKey, menuTypeKey, MenuType } from '../types/const';
  import columns from '../types/table.columns';
  import { Message } from '@arco-design/web-vue';
  import { useCacheStore, useDictStore } from '@/store';
  import usePermission from '@/hooks/permission';
  import { findParentNode } from '@/utils/tree';

  const { toOptions, getDictValue, toggleDictValue } = useDictStore();
  const cacheStore = useCacheStore();
  const { hasPermission } = usePermission();

  const formRef = ref();
  const formModel = reactive<MenuQueryRequest>({
    name: undefined,
    status: undefined
  });

  const tableRef = ref();
  const expandStatus = ref<boolean>(false);

  const tableRenderData = ref<MenuQueryResponse[]>([]);
  const { loading: fetchLoading, setLoading: setFetchLoading } = useLoading();

  const emits = defineEmits(['openAdd', 'openUpdate']);

  // 删除菜单
  const doDeleteMenu = async ({ id }: any) => {
    try {
      setFetchLoading(true);
      // 调用删除接口
      await deleteMenu(id);
      // 获取父级容器
      const parent = findParentNode(id, tableRenderData.value, 'id');
      if (parent) {
        // 页面删除 不重新调用接口
        let children;
        if (parent.root) {
          children = tableRenderData.value;
        } else {
          children = parent.children;
        }
        if (children) {
          // 删除
          for (let i = 0; i < children.length; i++) {
            if (children[i].id === id) {
              children.splice(i, 1);
              break;
            }
          }
        }
      }
      Message.success('删除成功');
    } catch (e) {
    } finally {
      setFetchLoading(false);
    }
  };

  // 添加后回调
  const addedCallback = () => {
    loadMenuData();
  };

  // 更新后回调
  const updatedCallback = () => {
    loadMenuData();
  };

  defineExpose({
    addedCallback, updatedCallback
  });

  // 加载菜单
  const loadMenuData = async () => {
    try {
      setFetchLoading(true);
      const { data } = await getMenuList(formModel);
      tableRenderData.value = data as MenuQueryResponse[];
    } catch (e) {
    } finally {
      setFetchLoading(false);
    }
  };

  onMounted(() => {
    loadMenuData();
  });

  // 重置菜单
  const resetForm = () => {
    formRef.value.resetFields();
    loadMenuData();
  };

  // 切换展开/折叠
  const toggleExpand = () => {
    tableRef.value.expandAll(expandStatus.value = !expandStatus.value);
  };

  // 刷新缓存
  const doRefreshCache = async () => {
    try {
      setFetchLoading(true);
      await refreshCache();
      Message.success('刷新成功 页面缓存刷新后生效');
    } catch (e) {
    } finally {
      setFetchLoading(false);
    }
  };

  // 修改状态
  const updateStatus = async (id: number, status: number) => {
    await updateMenuStatus({ id, status });
    await loadMenuData();
  };

  // 修改可见状态
  const updateVisible = async (id: number, visible: number) => {
    await updateMenuStatus({ id, visible });
    await loadMenuData();
  };

  // 获取最大排序
  const getMaxSort = (array: MenuQueryResponse[]) => {
    if (array?.length) {
      return array[array.length - 1].sort;
    } else {
      return 0;
    }
  };

</script>

<style lang="less" scoped>

</style>
