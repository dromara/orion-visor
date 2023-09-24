<template>
  <a-modal v-model:visible="visible"
           body-class="modal-form"
           title-align="start"
           title="分配菜单"
           :top="80"
           :width="480"
           :body-style="{padding: '16px 16px 0 16px'}"
           :align-center="false"
           :draggable="true"
           :mask-closable="false"
           :unmount-on-close="true"
           :ok-button-props="{ disabled: loading }"
           :cancel-button-props="{ disabled: loading }"
           :on-before-ok="handlerOk"
           @close="handleClose">
    <a-spin :loading="loading">
      <div class="role-menu-wrapper">
        <!-- 角色名称 -->
        <div class="item-wrapper">
          <span class="item-label">角色名称</span>
          <span class="item-value ml4">{{ roleRecord.name }}</span>
        </div>
        <!-- 角色编码 -->
        <div class="item-wrapper">
          <span class="item-label">角色编码</span>
          <a-tag class="item-value">{{ roleRecord.code }}</a-tag>
        </div>
        <!-- 菜单 -->
        <div class="menu-tree-wrapper item-wrapper">
          <span class="item-label">菜单选择</span>
          <menu-selector-tree ref="tree" class="item-value" />
        </div>
      </div>
    </a-spin>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'role-menu-grant-modal'
  };
</script>

<script lang="ts" setup>
  import MenuSelectorTree from '@/components/menu/selector/menu-selector-tree.vue';

  import { reactive, ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import { getRoleMenuId, grantRoleMenu, RoleGrantMenuRequest } from '@/api/user/role';
  import { Message } from '@arco-design/web-vue';
  import { useCacheStore } from '@/store';
  import { getMenuList } from '@/api/system/menu';

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const tree = ref<any>();
  const roleRecord = reactive<Record<string, any>>({
    id: undefined,
    name: undefined,
    code: undefined,
  });

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
      tree.value.init(roleMenuIdList);
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 渲染对象
  const renderRecord = (record: any) => {
    Object.keys(roleRecord).forEach(k => {
      if (record.hasOwnProperty(k)) {
        roleRecord[k] = record[k];
      }
    });
  };

  defineExpose({ open });

  // 确定
  const handlerOk = async () => {
    setLoading(true);
    try {
      // 修改
      await grantRoleMenu({
        roleId: roleRecord.id,
        menuIdList: [...tree.value.getValue()]
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
    min-width: 400px;
    max-height: calc(100vh - 285px);

    .item-wrapper {
      display: flex;
      margin-bottom: 16px;
      align-items: baseline;

      .item-label {
        display: inline-block;
        text-align: right;
        width: 80px;
        height: 32px;
        margin-right: 12px;
        line-height: 32px;
        color: var(--color-text-2);
      }

    }
  }

</style>
