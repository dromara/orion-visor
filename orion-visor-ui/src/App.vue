<template>
  <a-config-provider :locale="locale">
    <!-- 路由 -->
    <router-view />
    <!-- 应用设置 -->
    <app-setting ref="appSettingRef" />
  </a-config-provider>
</template>

<script lang="ts" setup>
  import { computed, provide, ref } from 'vue';
  import { openAppSettingKey } from '@/types/symbol';
  import useLocale from '@/hooks/locale';
  import zhCN from '@arco-design/web-vue/es/locale/lang/zh-cn';
  import AppSetting from '@/components/app/setting/index.vue';

  const { currentLocale } = useLocale();
  const locale = computed(() => {
    switch (currentLocale.value) {
      case 'zh-CN':
        return zhCN;
      default:
        return zhCN;
    }
  });

  // 对外暴露打开配置方法
  const appSettingRef = ref();
  provide(openAppSettingKey, () => {
    appSettingRef.value.open();
  });

</script>
