<template>
  <a-config-provider :locale="locale">
    <router-view />
    <global-setting ref="globalSettingRef" />
  </a-config-provider>
</template>

<script lang="ts" setup>
  import { computed, provide, ref } from 'vue';
  import zhCN from '@arco-design/web-vue/es/locale/lang/zh-cn';
  import GlobalSetting from '@/components/global-setting/index.vue';
  import useLocale from '@/hooks/locale';
  import { openGlobalSettingKey } from '@/types/symbol';

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
  const globalSettingRef = ref();
  provide(openGlobalSettingKey, () => {
    globalSettingRef.value.open();
  });

</script>
