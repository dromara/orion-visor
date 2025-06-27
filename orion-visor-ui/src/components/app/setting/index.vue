<template>
  <!-- 左侧固定配置按钮 -->
  <div v-if="!appStore.navbar" class="fixed-settings" @click="open">
    <a-button type="primary">
      <template #icon>
        <icon-settings />
      </template>
    </a-button>
  </div>
  <!-- 偏好配置抽屉 -->
  <a-drawer v-model:visible="visible"
            title="偏好设置"
            :width="300"
            :footer="false"
            :unmount-on-close="true"
            @cancel="() => setVisible(false)">
    <div class="preference-containers">
      <!-- 布局设置 -->
      <block :options="layoutOpts" title="布局设置" />
      <!-- 数据设置 -->
      <block :options="dataOpts" title="数据设置" />
      <!-- 页面视图 -->
      <block :options="viewsOpts" title="页面视图" />
      <!-- 保存为桌面程序 -->
      <a-button v-if="visibleCreatePwaApp()"
                class="mb16"
                type="primary"
                @click="createPwaApp"
                long>
        保存为桌面程序
      </a-button>
    </div>
  </a-drawer>
</template>

<script lang="ts" setup>
  import { computed } from 'vue';
  import { useAppStore } from '@/store';
  import useVisible from '@/hooks/visible';
  import { CardPageSizeOptions, TablePageSizeOptions } from '@/types/const';
  import { Message } from '@arco-design/web-vue';
  import { isStandaloneMode } from '@/utils/env';
  import Block from './block.vue';

  const cardOptions = [{ value: 'table', label: '表格' }, { value: 'card', label: '卡片' }];

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
      name: '底部页脚',
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

  // 布局设置
  const dataOpts = computed(() => [
    {
      name: '表格默认页数',
      key: 'defaultTablePageSize',
      type: 'select',
      margin: '0 0 4px 0',
      defaultVal: appStore.defaultTablePageSize,
      options: TablePageSizeOptions.map(s => {
        return {
          value: s,
          label: `${s} 条/页`
        };
      })
    },
    {
      name: '卡片默认页数',
      key: 'defaultCardPageSize',
      type: 'select',
      defaultVal: appStore.defaultCardPageSize,
      options: CardPageSizeOptions.map(s => {
        return {
          value: s,
          label: `${s} 条/页`
        };
      })
    },
  ]);

  // 页面视图配置
  const viewsOpts = computed(() => [
    {
      name: '主机列表',
      key: 'hostView',
      type: 'radio-group',
      margin: '0 0 4px 0',
      permission: ['asset:host:query'],
      defaultVal: appStore.hostView,
      options: cardOptions,
    },
    {
      name: '主机密钥',
      key: 'hostKeyView',
      type: 'radio-group',
      margin: '0 0 4px 0',
      permission: ['asset:host-key:query'],
      defaultVal: appStore.hostKeyView,
      options: cardOptions,
    },
    {
      name: '主机身份',
      key: 'hostIdentityView',
      type: 'radio-group',
      margin: '0 0 4px 0',
      permission: ['asset:host-identity:query'],
      defaultVal: appStore.hostIdentityView,
      options: cardOptions,
    },
  ]);

  // 是否展示创建 PWA 应用
  const visibleCreatePwaApp = () => {
    return !isStandaloneMode && !!(window as CustomWindow).deferredPrompt;
  };

  // 创建 PWA 应用
  const createPwaApp = () => {
    const win = window as CustomWindow;
    try {
      win.deferredPrompt.prompt();
      win.deferredPrompt.userChoice.then((choiceResult: any) => {
        if (choiceResult.outcome === 'accepted') {
          win.deferredPrompt = null;
        }
      });
    } catch (e) {
      Message.error('无法安装 PWA 应用');
    }
  };

</script>

<style lang="less" scoped>
  .fixed-settings {
    position: fixed;
    top: 280px;
    right: 0;

    svg {
      font-size: 18px;
      vertical-align: -4px;
    }
  }

  .preference-containers {
    padding: 0 16px;
  }

</style>
