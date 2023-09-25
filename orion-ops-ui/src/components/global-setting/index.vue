<template>
  <div v-if="!appStore.navbar" class="fixed-settings" @click="setVisible">
    <a-button type="primary">
      <template #icon>
        <icon-settings />
      </template>
    </a-button>
  </div>
  <a-drawer title="偏好设置"
            :width="300"
            unmount-on-close
            :visible="visible"
            ok-text="保存"
            cancel-text="关闭"
            @ok="saveConfig"
            @cancel="cancel">
    <Block :options="contentOpts" title="内容区域" />
    <Block :options="othersOpts" title="其他设置" />
  </a-drawer>
</template>

<script lang="ts" setup>
  import { computed } from 'vue';
  import { useAppStore } from '@/store';
  import Block from './block.vue';

  const emit = defineEmits(['cancel']);

  const appStore = useAppStore();
  const visible = computed(() => appStore.globalSettings);

  /**
   * 内容配置
   */
  const contentOpts = computed(() => [
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
      name: '菜单宽度 (px)',
      key: 'menuWidth',
      defaultVal: appStore.menuWidth,
      type: 'number',
    },
  ]);

  /**
   * 其他配置
   */
  const othersOpts = computed(() => [
    {
      name: '色弱模式',
      key: 'colorWeak',
      defaultVal: appStore.colorWeak,
    },
  ]);

  /**
   * 取消配置
   */
  const cancel = () => {
    appStore.updateSettings({ globalSettings: false });
    emit('cancel');
  };

  /**
   * 复制配置
   */
  const saveConfig = async () => {
  };

  /**
   * 显示菜单
   */
  const setVisible = () => {
    appStore.updateSettings({ globalSettings: true });
  };
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
