<template>
  <a-layout class="full-layout">
    <!-- 页面 -->
    <a-layout-content>
      <!-- 水印 -->
      <a-watermark v-if="watermarkEnabled"
                   :grayscale="watermarkGrayscale"
                   :alpha="watermarkAlpha"
                   :font-size="watermarkFontSize"
                   :z-index="9999"
                   style="width: 100%; height: 100%;"
                   :content="userStore.username || ''">
        <page-layout />
      </a-watermark>
      <page-layout v-else />
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
  export default {
    name: 'fullLayout'
  };
</script>

<script lang="ts" setup>
  import { onMounted, ref } from 'vue';
  import { useUserStore } from '@/store';
  import { getSystemSetting } from '@/api/system/setting';
  import { SystemSettingTypes } from '@/views/system/setting/types/const';
  import PageLayout from './page-layout.vue';

  const userStore = useUserStore();

  // 页面水印配置
  const watermarkEnabled = ref(true);
  const watermarkGrayscale = ref(true);
  const watermarkAlpha = ref(0.6);
  const watermarkFontSize = ref(16);

  // 加载页面水印配置
  const loadWatermarkSetting = async () => {
    try {
      const { data } = await getSystemSetting(SystemSettingTypes.WATERMARK);
      watermarkEnabled.value = data['watermark.page.enabled'] !== 'false';
      watermarkGrayscale.value = data['watermark.page.grayscale'] === 'true';
      const alpha = Number(data['watermark.page.alpha']);
      watermarkAlpha.value = Number.isNaN(alpha) ? 0.6 : alpha;
      const fontSize = Number(data['watermark.page.font-size']);
      watermarkFontSize.value = Number.isNaN(fontSize) ? 16 : fontSize;
    } catch (e) {
      // 配置获取失败时使用默认开启
    }
  };

  onMounted(() => {
    loadWatermarkSetting();
  });

</script>

<style lang="less" scoped>
  .full-layout {
    height: 100vh;
    width: 100%;

    .arco-layout-content {
      height: 100%;
      width: 100%;
    }
  }
</style>
