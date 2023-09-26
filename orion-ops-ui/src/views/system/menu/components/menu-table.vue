<template>
  <!-- 搜索 -->
  <a-card class="general-card table-search-card">
    <a-row>
      <!-- 搜索框 -->
      <a-col :span="12">
        <a-form :model="formModel"
                ref="formRef"
                label-align="left"
                @keydown.enter="loadMenuData">
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
                          :options="toOptions(MenuStatusEnum)"
                          placeholder="请选择菜单状态"
                          allow-clear />
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </a-col>
      <!-- 操作 -->
      <a-col :span="12" class="table-bar-handle">
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
                        @ok="doInitCache">
            <a-button type="primary" status="warning"
                      v-permission="['infra:system-menu:init-cache']">
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
        <a-tag>{{ getEnumValue(record.type, MenuTypeEnum) }}</a-tag>
      </template>
      <!-- 状态 -->
      <template #status="{ record }">
        <a-space>
          <!-- 菜单状态 -->
          <a-popconfirm position="top"
                        type="warning"
                        :content="`确定要将当前节点以及所有子节点改为${toggleEnumValue(record.status, MenuStatusEnum, 'label')}?`"
                        @ok="updateStatus(record.id, toggleEnumValue(record.status, MenuStatusEnum))">
            <a-tag :color="getEnumValue(record.status, MenuStatusEnum,'color')" class="pointer">
              {{ getEnumValue(record.status, MenuStatusEnum) }}
            </a-tag>
          </a-popconfirm>
          <!-- 显示状态 -->
          <a-popconfirm position="top"
                        type="warning"
                        :content="`确定要将当前节点以及所有子节点改为${toggleEnumValue(record.visible, MenuVisibleEnum, 'label')}?`"
                        @ok="updateVisible(record.id, toggleEnumValue(record.visible, MenuVisibleEnum))">
            <a-tag v-if="(record.visible || record.visible === 0) && record.type !== MenuTypeEnum.FUNCTION.value"
                   :color="getEnumValue(record.visible, MenuVisibleEnum,'color')"
                   class="pointer">
              {{ getEnumValue(record.visible, MenuVisibleEnum) }}
            </a-tag>
          </a-popconfirm>
        </a-space>
      </template>
      <!-- 操作 -->
      <template #handle="{ record }">
        <div class="table-handle-wrapper">
          <!-- 新增 -->
          <a-button type="text"
                    size="mini"
                    v-if="record.type !== MenuTypeEnum.FUNCTION.value"
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
  import { reactive, ref, onUnmounted } from 'vue';
  import useLoading from '@/hooks/loading';
  import { getMenuList, deleteMenu, updateMenuStatus, initCache, MenuQueryRequest, MenuQueryResponse } from '@/api/system/menu';
  import { toOptions, getEnumValue, toggleEnumValue } from '@/utils/enum';
  import { MenuStatusEnum, MenuVisibleEnum, MenuTypeEnum } from '../types/enum.types';
  import columns from '../types/table.columns';
  import { Message } from '@arco-design/web-vue';
  import { useCacheStore } from '@/store';

  const cacheStore = useCacheStore();

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

      // 获取父菜单
      const findParentMenu = (arr: any, id: number): any => {
        if (!arr || !arr.length) {
          return null;
        }
        // 当前级
        for (let e of arr) {
          if (e.id === id) {
            return arr;
          }
        }
        // 子级
        for (let e of arr) {
          if (e.children && e.children.length) {
            if (findParentMenu(e.children, id)) {
              return e.children;
            }
          }
        }
        return null;
      };

      // 获取父级容器
      const parent = findParentMenu(tableRenderData.value, id) as unknown as MenuQueryResponse[];
      if (parent) {
        // 删除
        for (let i = 0; i < parent.length; i++) {
          if (parent[i].id === id) {
            parent.splice(i, 1);
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
      cacheStore.set('menus', tableRenderData.value);
    } catch (e) {
    } finally {
      setFetchLoading(false);
    }
  };
  loadMenuData();

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
  const doInitCache = async () => {
    try {
      setFetchLoading(true);
      await initCache();
      Message.success('刷新成功');
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
