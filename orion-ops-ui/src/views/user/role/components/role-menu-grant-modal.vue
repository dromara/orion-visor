<template>
  <a-modal v-model:visible="visible"
           body-class="modal-form"
           title-align="start"
           title="分配菜单"
           width="80%"
           :top="40"
           :body-style="{padding: '16px 16px 0 16px', 'margin-bottom': '16px'}"
           :align-center="false"
           :draggable="true"
           :mask-closable="false"
           :unmount-on-close="true"
           :ok-button-props="{ disabled: loading }"
           :cancel-button-props="{ disabled: loading }"
           :on-before-ok="handlerOk"
           @close="handleClose">
    <div class="role-menu-wrapper">
      <a-alert class="usn mb8">
        <span>{{ roleRecord.name }} {{ roleRecord.code }}</span>
        <span class="mx8">-</span>
        <span>菜单分配后需要用户手动刷新页面才会生效</span>
      </a-alert>
      <div class="usn mb8">
        <a-space>
          <template v-for="opt of quickGrantMenuOperator" :key="opt.name">
            <a-button size="mini" type="text" @click="() => { table.checked(opt.rule) }">
              {{ '全选' + opt.name }}
            </a-button>
          </template>
        </a-space>
      </div>
      <div class="usn mb8">
        <a-space>
          <template v-for="opt of quickGrantMenuOperator" :key="opt.name">
            <a-button size="mini" type="text" @click="() => { table.unchecked(opt.rule) }">
              {{ '反选' + opt.name }}
            </a-button>
          </template>
        </a-space>
      </div>
      <!-- 菜单 -->
      <menu-grant-table ref="table" />
    </div>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'role-menu-grant-modal'
  };
</script>

<script lang="ts" setup>
  import type { RoleGrantMenuRequest, RoleQueryResponse } from '@/api/user/role';
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import { getRoleMenuId, grantRoleMenu } from '@/api/user/role';
  import { Message } from '@arco-design/web-vue';
  import { useCacheStore } from '@/store';
  import { getMenuList } from '@/api/system/menu';
  import MenuGrantTable from '@/components/system/menu/grant/menu-grant-table.vue';
  import { quickGrantMenuOperator } from '../types/const';

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const table = ref();
  const roleRecord = ref<RoleQueryResponse>({} as RoleQueryResponse);

  // 打开新增
  const open = async (record: any) => {
    renderRecord(record);
    setVisible(true);
    try {
      setLoading(true);
      // 获取菜单列表
      const cacheStore = useCacheStore();
      if (!cacheStore.menus?.length) {
        // 加载菜单
        const { data: menuData } = await getMenuList({});
        cacheStore.set('menus', menuData);
      }
      // 获取角色菜单
      const { data: roleMenuIdList } = await getRoleMenuId(record.id);
      table.value.init(roleMenuIdList);
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 渲染对象
  const renderRecord = (record: any) => {
    roleRecord.value = Object.assign({}, record);
  };

  defineExpose({ open });

  // 确定
  const handlerOk = async () => {
    setLoading(true);
    try {
      // 修改
      await grantRoleMenu({
        roleId: roleRecord.value.id,
        menuIdList: [...table.value.getValue()]
      } as RoleGrantMenuRequest);
      Message.success('分配成功');
      // 清空
      handlerClear();
    } catch (e) {
      return false;
    } finally {
      setLoading(false);
    }
  };

  // 关闭
  const handleClose = () => {
    handlerClear();
  };

  // 清空
  const handlerClear = () => {
    setLoading(false);
    setVisible(false);
  };

</script>

<style lang="less" scoped>
  .role-menu-wrapper {
    width: 100%;
    max-height: calc(100vh - 230px);
  }

</style>
