<template>
  <a-spin class="main-container" :loading="loading">
    <h3 class="setting-header">关于</h3>
    <!-- 不一致提示 -->
    <a-alert v-if="app.version && webVersion !== app.version"
             type="warning"
             class="alert-wrapper">
      当前前端版本与后端版本不一致, 请使用 Ctrl + F5 强制刷新页面
    </a-alert>
    <!-- 升级提示 -->
    <a v-if="app.version && repo.tagName && ('v' + app.version) !== repo.tagName"
       class="alert-href"
       target="_blank"
       :href="`https://github.com/dromara/orion-visor/releases/tag/${repo.tagName}`">
      <a-alert class="alert-wrapper">
        新版本已发布, 请及时升级版本
      </a-alert>
    </a>
    <!-- 系统信息 -->
    <a-descriptions class="detail-container"
                    size="large"
                    :align="{ label: 'right', value: 'left' }"
                    :label-style="{ width: '138px' }"
                    :column="1">
      <!-- 机器码 -->
      <a-descriptions-item label="机器码">
        <span class="text-copy uuid-wrapper" @click="copy(app.uuid, true)">
          {{ app.uuid }}
        </span>
      </a-descriptions-item>
      <!-- 当前前端版本 -->
      <a-descriptions-item label="当前前端版本">
        {{ 'v' + webVersion }}
      </a-descriptions-item>
      <!-- 当前后端版本 -->
      <a-descriptions-item label="当前后端版本">
        {{ 'v' + app.version }}
      </a-descriptions-item>
      <!-- 当前后端版本 -->
      <a-descriptions-item label="最新发布版本">
        {{ repo.tagName || '-' }}
      </a-descriptions-item>
      <!-- 当前后端版本 -->
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
  import { onMounted, reactive } from 'vue';
  import { getAppLatestRelease, getSystemAppInfo } from '@/api/system/setting';
  import { copy } from '@/hooks/copy';
  import useLoading from '@/hooks/loading';

  const { loading, setLoading } = useLoading();

  const webVersion = import.meta.env.VITE_APP_VERSION;

  const app = reactive<AppInfoResponse>({
    version: '',
    uuid: '',
  });

  const repo = reactive<AppReleaseResponse>({
    tagName: '',
    body: '',
  });

  // 加载应用信息
  onMounted(async () => {
    setLoading(true);
    try {
      const { data } = await getSystemAppInfo();
      app.version = data.version;
      app.uuid = data.uuid;
    } catch (e) {
    } finally {
      setLoading(false);
    }
  });

  // 加载版本信息
  onMounted(async () => {
    try {
      const { data } = await getAppLatestRelease();
      repo.tagName = data.tagName;
      repo.body = data.body;
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
