<template>
  <!-- 搜索 -->
  <a-card class="general-card table-search-card">
    <a-row style="margin-top: 16px">
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
                <a-select
                  v-model="formModel.status"
                  :options="toOptions(MenuStatusEnum)"
                  placeholder="请选择菜单状态"
                  allow-clear
                />
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </a-col>
      <!-- 操作 -->
      <a-col :span="12" class="form-option">
        <a-space>
          <!-- 新增 -->
          <a-button type="primary"
                    @click="emits('openAdd',{ parentId: 0 })"
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
          <!-- 展开 -->
          <a-button @click="toggleExpand">
            折叠/展开
            <template #icon>
              <icon-expand />
            </template>
          </a-button>
        </a-space>
      </a-col>
    </a-row>
  </a-card>
  <!-- 表格 -->
  <a-card class="general-card table-card">
    <a-table row-key="id"
             class="table-wrapper"
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
          <a-tag :color="getEnumValue(record.status, MenuStatusEnum,'color')">
            {{ getEnumValue(record.status, MenuStatusEnum) }}
          </a-tag>
          <!-- 显示状态 -->
          <a-tag v-if="(record.visible || record.visible === 0) && record.type !== MenuTypeEnum.FUNCTION.value"
                 :color="getEnumValue(record.visible, MenuVisibleEnum,'color')">
            {{ getEnumValue(record.visible, MenuVisibleEnum) }}
          </a-tag>
        </a-space>
      </template>
      <!-- 操作 -->
      <template #option="{ record }">
        <div class="table-option-wrapper">
          <!-- 新增 -->
          <a-button type="text"
                    size="mini"
                    v-if="record.type !== MenuTypeEnum.FUNCTION.value"
                    v-permission="['infra:system-menu:create']"
                    @click="emits('openAdd', { parentId: record.id })">
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
  import { getMenuList, deleteMenu, MenuQueryResponse } from '@/api/system/menu';
  import { toOptions, getEnumValue } from '@/utils/enum';
  import { MenuStatusEnum, MenuVisibleEnum, MenuTypeEnum } from '../type/enum.types';
  import columns from '../type/table.columns';
  import { Message } from '@arco-design/web-vue';
  import { useMenuStore } from '@/store';

  const menuStore = useMenuStore();

  const formRef = ref<any>();
  const formModel = reactive({
    name: undefined,
    status: undefined
  });

  const tableRef = ref<any>();
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
      const parent = menuStore.findParentMenu(tableRenderData.value, id) as unknown as MenuQueryResponse[];
      if (parent) {
        // 删除
        for (let i = 0; i < parent.length; i++) {
          if (parent[i].id === id) {
            parent.splice(i, 1);
          }
        }
      }
      Message.success({ content: '删除成功' });
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
      menuStore.updateMenu(tableRenderData.value);
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

  // 卸载时清除 store
  onUnmounted(() => {
    menuStore.reset();
  });

</script>

<style lang="less" scoped>
  .form-option {
    display: flex;
    align-items: center;
    justify-content: end
  }

  :deep(.arco-form-item) {
    margin-bottom: 0 !important;
  }
</style>
