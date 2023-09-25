<template>
  <div v-if="!appStore.navbar" class="fixed-settings" @click="open">
    <a-button type="primary">
      <template #icon>
        <icon-settings />
      </template>
    </a-button>
  </div>
  <a-drawer title="偏好设置"
            :width="300"
            :visible="visible"
            :footer="false"
            :unmount-on-close="true"
            @cancel="() => setVisible(false)">
    <Block :options="layoutOpts" title="布局设置" />
    <Block :options="viewsOpts" title="页面视图" />
  </a-drawer>
</template>

<script lang="ts" setup>
  import { computed } from 'vue';
  import { useAppStore } from '@/store';
  import Block from './block.vue';
  import useVisible from '@/hooks/visible';

  const appStore = useAppStore();
  const { visible, setVisible } = useVisible();

  // 打开
  const open = () => {
    setVisible(true);
  };
  defineExpose({ open });

  // 布局设置
  const layoutOpts = computed(() => [
    {
      name: '导航栏',
      key: 'navbar',
      defaultVal: appStore.navbar
    },
    {
      name: '菜单栏',
      key: 'menu',
      defaultVal: appStore.menu,
    },
    {
      name: '顶部菜单栏',
      key: 'topMenu',
      defaultVal: appStore.topMenu,
    },
    {
      name: '底部',
      key: 'footer',
      defaultVal: appStore.footer
    },
    {
      name: '多页签',
      key: 'tabBar',
      defaultVal: appStore.tabBar
    },
    {
      name: '色弱模式',
      key: 'colorWeak',
      defaultVal: appStore.colorWeak,
    },
    {
      name: '菜单宽度 (px)',
      key: 'menuWidth',
      type: 'number',
      defaultVal: appStore.menuWidth,
    },
  ]);

  // 页面视图配置
  const viewsOpts = computed(() => [
    {
      name: '主机列表',
      key: 'host',
      type: 'radio-group',
      defaultVal: appStore.host,
      options: [{ value: 'table', label: '表格' }, { value: 'card', label: '卡片' }]
    },
    {
      name: '主机秘钥',
      key: 'hostKeys',
      type: 'radio-group',
      defaultVal: appStore.hostKeys,
      options: [{ value: 'table', label: '表格' }, { value: 'card', label: '卡片' }]
    },
    {
      name: '主机身份',
      key: 'hostIdentity',
      type: 'radio-group',
      defaultVal: appStore.hostIdentity,
      options: [{ value: 'table', label: '表格' }, { value: 'card', label: '卡片' }]
    },
  ]);

</script>

<style scoped lang="less">
  .fixed-settings {
    position: fixed;
    top: 280px;
    right: 0;

    svg {
      font-size: 18px;
      vertical-align: -4px;
    }
  }
</style>
