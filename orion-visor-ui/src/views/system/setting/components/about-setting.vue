<template>
  <a-spin class="main-container" :loading="loading">
    <a-descriptions title="关于"
                    class="detail-container"
                    size="large"
                    :align="{ label: 'right', value: 'left' }"
                    :label-style="{ paddingTop: '2px', paddingLeft: '32px', verticalAlign: 'top' }"
                    :column="1">
      <!-- 当前前端版本 -->
      <a-descriptions-item label="当前前端版本">
        <!-- 前端版本 -->
        <span>{{ 'v' + webVersion }}</span>
        <!-- 不一致提示 -->
        <b v-if="app.version && webVersion !== app.version"
           class="span-red ml8">当前前端版本与后端版本不一致, 请使用 Ctrl + F5 强制刷新页面</b>
      </a-descriptions-item>
      <!-- 当前后端版本 -->
      <a-descriptions-item label="当前后端版本">
        <span>{{ 'v' + app.version }}</span>
      </a-descriptions-item>
      <!-- 最新发布版本 -->
      <a-descriptions-item label="最新发布版本">
        <!-- 最新版本 -->
        <span>{{ repo.tagName || '-' }}</span>
        <!-- 升级提示 -->
        <b v-if="app.version && repo.tagName && ('v' + app.version) !== repo.tagName"
           class="span-green ml8">新版本已发布, 请及时升级版本</b>
      </a-descriptions-item>
      <!-- 最近更新时间 -->
      <a-descriptions-item label="最近更新时间">
        <span>{{ repo.releaseTime }}</span>
      </a-descriptions-item>
      <!-- 最新更新日志 -->
      <a-descriptions-item label="最新更新日志">
        <a-textarea v-model="repo.body"
                    :auto-size="{ minRows: 3, maxRows: 16 }"
                    readonly>
        </a-textarea>
      </a-descriptions-item>
    </a-descriptions>
  </a-spin>
</template>

<script lang="ts">
  export default {
    name: 'aboutSetting',
  };
</script>

<script lang="ts" setup>
  import type { AppInfoResponse, AppReleaseResponse } from '@/api/system/setting';
  import { onMounted, ref } from 'vue';
  import { copy } from '@/hooks/copy';
  import useLoading from '@/hooks/loading';
  import { getAppLatestRelease, getSystemAppInfo } from '@/api/system/setting';

  const { loading, setLoading } = useLoading();

  const webVersion = import.meta.env.VITE_APP_VERSION;

  const app = ref<AppInfoResponse>({
    version: '',
  });

  const repo = ref<AppReleaseResponse>({
    tagName: '',
    releaseTime: '',
    body: '',
  });

  // 加载应用信息
  onMounted(async () => {
    setLoading(true);
    try {
      const { data } = await getSystemAppInfo();
      app.value = data;
    } catch (e) {
    } finally {
      setLoading(false);
    }
  });

  // 加载版本信息
  onMounted(async () => {
    try {
      const { data } = await getAppLatestRelease();
      repo.value = data;
    } catch (e) {
    }
  });

</script>

<style lang="less" scoped>
  .main-container {
    width: 664px !important;

    .uuid-wrapper {
      color: rgb(var(--arcoblue-6));
      font-weight: 600;
    }
  }
</style>
