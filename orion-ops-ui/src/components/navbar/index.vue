<template>
  <div class="navbar">
    <!-- 左侧按钮 -->
    <div class="left-side">
      <a-space>
        <!-- FIXME -->
        <!-- LOGO -->
        <img alt="logo"
             src="//p3-armor.byteimg.com/tos-cn-i-49unhts6dw/dfdba5317c0c20ce20e64fac803d52bc.svg~tplv-49unhts6dw-image.image" />
        <!-- 标头 -->
        <a-typography-title :heading="5" :style="{ margin: 0, fontSize: '18px' }">
          Orion Ops Pro
        </a-typography-title>
        <!-- 收缩菜单 -->
        <icon-menu-fold v-if="!topMenu && appStore.device === 'mobile'"
                        style="font-size: 22px; cursor: pointer"
                        @click="toggleDrawerMenu" />
      </a-space>
    </div>
    <!-- 顶部菜单 -->
    <div class="center-side">
      <Menu v-if="topMenu" />
    </div>
    <!-- 右侧操作 -->
    <ul class="right-side">
      <!-- 搜索 -->
      <li v-if="false">
        <a-tooltip content="搜索">
          <a-button class="nav-btn" type="outline" shape="circle">
            <template #icon>
              <icon-search />
            </template>
          </a-button>
        </a-tooltip>
      </li>
      <!-- 切换语言 -->
      <li v-if="false">
        <a-tooltip content="语言">
          <a-button class="nav-btn"
                    type="outline"
                    :shape="'circle'"
                    @click="setUserInfoVisible">
            <template #icon>
              <icon-language />
            </template>
          </a-button>
        </a-tooltip>
        <a-dropdown trigger="click" @select="changeLocale">
          <div ref="refUserInfoTrigger" class="trigger-btn" />
          <template #content>
            <a-doption v-for="item in locales"
                       :key="item.value"
                       :value="item.value">
              <template #icon>
                <icon-check v-show="item.value === currentLocale" />
              </template>
              {{ item.label }}
            </a-doption>
          </template>
        </a-dropdown>
      </li>
      <!-- 暗色模式 -->
      <li>
        <a-tooltip :content="theme === 'light'
              ? '点击切换为暗黑模式'
              : '点击切换为亮色模式'">
          <a-button class="nav-btn"
                    type="outline"
                    :shape="'circle'"
                    @click="handleToggleTheme">
            <template #icon>
              <icon-moon-fill v-if="theme === 'dark'" />
              <icon-sun-fill v-else />
            </template>
          </a-button>
        </a-tooltip>
      </li>
      <!-- 消息列表 -->
      <li v-if="false">
        <a-tooltip content="消息通知">
          <div class="message-box-trigger">
            <a-badge :count="9" dot>
              <a-button class="nav-btn"
                        type="outline"
                        :shape="'circle'"
                        @click="setMessageBoxVisible">
                <icon-notification />
              </a-button>
            </a-badge>
          </div>
        </a-tooltip>
        <a-popover trigger="click"
                   :arrow-style="{ display: 'none' }"
                   :content-style="{ padding: 0, minWidth: '400px' }"
                   content-class="message-popover">
          <div ref="refMessageBoxTrigger" class="ref-btn" />
          <template #content>
            <message-box />
          </template>
        </a-popover>
      </li>
      <!-- 全屏模式 -->
      <li>
        <a-tooltip :content="isFullscreen
              ? '点击退出全屏模式'
              : '点击切换全屏模式'">
          <a-button class="nav-btn"
                    type="outline"
                    shape="circle"
                    @click="toggleFullScreen">
            <template #icon>
              <icon-fullscreen-exit v-if="isFullscreen" />
              <icon-fullscreen v-else />
            </template>
          </a-button>
        </a-tooltip>
      </li>
      <!-- 偏好设置 -->
      <li>
        <a-popover :popup-visible="tippedPreference" position="br">
          <template #title>
            💡 点击这里可以修改系统偏好~
          </template>
          <template #content>
           <span style="line-height: 1.8">
             ◾ 可以修改页面布局<br>
             ◾ 可以切换显示视图
           </span>
            <div class="tips-buttons">
              <a-button size="mini" class="mr8" @click="closePreferenceTip(false)">关闭</a-button>
              <a-button size="mini" type="primary" @click="closePreferenceTip(true)">不在提醒</a-button>
            </div>
          </template>
          <a-tooltip content="偏好设置">
            <a-button class="nav-btn"
                      type="outline"
                      shape="circle"
                      @click="openGlobalSetting">
              <template #icon>
                <icon-settings />
              </template>
            </a-button>
          </a-tooltip>
        </a-popover>
      </li>
      <!-- 用户信息 -->
      <li>
        <a-dropdown trigger="click">
          <!-- 头像 -->
          <a-avatar :size="32"
                    :style="{ cursor: 'pointer', backgroundColor: '#3370ff' }">
            {{ nickname }}
          </a-avatar>
          <template #content>
            <!-- 个人中心 -->
            <a-doption>
              <a-space @click="$router.push({ name: 'userMine' })">
                <icon-user />
                <span>个人中心</span>
              </a-space>
            </a-doption>
            <!-- 修改密码 -->
            <a-doption>
              <a-space @click="$router.push({ name: 'userMine' })">
                <icon-lock />
                <span>修改密码</span>
              </a-space>
            </a-doption>
            <!-- 退出登录 -->
            <a-doption>
              <a-space @click="handleLogout">
                <icon-export />
                <span>退出登录</span>
              </a-space>
            </a-doption>
          </template>
        </a-dropdown>
      </li>
    </ul>
  </div>
</template>

<script lang="ts" setup>
  import { computed, inject, ref } from 'vue';
  import { useDark, useFullscreen, useToggle } from '@vueuse/core';
  import { useAppStore, useTipsStore, useUserStore } from '@/store';
  import { LOCALE_OPTIONS } from '@/locale';
  import useLocale from '@/hooks/locale';
  import useUser from '@/hooks/user';
  import { triggerMouseEvent } from '@/utils';
  import Menu from '@/components/menu/tree/index.vue';
  import MessageBox from '../message-box/index.vue';
  import { openGlobalSettingKey, toggleDrawerMenuKey } from '@/types/symbol';
  import { preferenceTipsKey } from './const';

  const tipsStore = useTipsStore();
  const tippedPreference = ref(tipsStore.isNotTipped(preferenceTipsKey));
  const appStore = useAppStore();
  const userStore = useUserStore();
  const { logout } = useUser();
  const { changeLocale, currentLocale } = useLocale();
  const { isFullscreen, toggle: toggleFullScreen } = useFullscreen();
  const locales = [...LOCALE_OPTIONS];
  const nickname = computed(() => {
    return userStore.nickname?.substring(0, 1);
  });
  const topMenu = computed(() => appStore.topMenu && appStore.menu);

  // 当前主题
  const theme = computed(() => {
    return appStore.theme;
  });

  // 主题
  const darkTheme = useDark({
    selector: 'body',
    attribute: 'arco-theme',
    valueDark: 'dark',
    valueLight: 'light',
    storageKey: 'arco-theme',
    onChanged(dark: boolean) {
      appStore.updateSettings({
        theme: dark ? 'dark' : 'light'
      });
    },
  });

  // 切换主题
  const handleToggleTheme = () => {
    useToggle(darkTheme)();
  };

  // 打开系统设置
  const openGlobalSetting = inject(openGlobalSettingKey) as () => void;

  // 消息触发器 ref
  const refMessageBoxTrigger = ref();
  const setMessageBoxVisible = () => {
    triggerMouseEvent(refMessageBoxTrigger);
  };

  // 个人信息触发器 ref
  const refUserInfoTrigger = ref();
  const setUserInfoVisible = () => {
    triggerMouseEvent(refUserInfoTrigger);
  };

  // 退出登录
  const handleLogout = () => {
    logout();
  };

  // 注入收缩菜单
  const toggleDrawerMenu = inject(toggleDrawerMenuKey) as () => void;

  // 关闭偏好提示
  const closePreferenceTip = (ack: boolean) => {
    tippedPreference.value = false;
    if (ack) {
      tipsStore.setTipped(preferenceTipsKey);
    }
  };

</script>

<style scoped lang="less">
  .navbar {
    display: flex;
    justify-content: space-between;
    height: 100%;
    background-color: var(--color-bg-2);
    border-bottom: 1px solid var(--color-border);
  }

  .left-side {
    display: flex;
    align-items: center;
    padding-left: 20px;
  }

  .center-side {
    flex: 1;
  }

  .right-side {
    display: flex;
    list-style: none;

    :deep(.locale-select) {
      border-radius: 20px;
    }

    li {
      display: flex;
      align-items: center;
      padding: 0 10px;
    }

    a {
      color: var(--color-text-1);
      text-decoration: none;
    }

    .nav-btn {
      border-color: rgb(var(--gray-2));
      color: rgb(var(--gray-8));
      font-size: 16px;
    }

    .trigger-btn,
    .ref-btn {
      position: absolute;
      bottom: 14px;
    }

    .trigger-btn {
      margin-left: 14px;
    }
  }
</style>

<style lang="less">
  .message-popover {
    .arco-popover-content {
      margin-top: 0;
    }
  }

  .tips-buttons {
    margin-top: 12px;
    display: flex;
    justify-content: flex-end;
  }

</style>
