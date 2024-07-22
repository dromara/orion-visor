<template>
  <div class="main-container">
    <h3>关于 Orion Visor</h3>
    <!-- 不一致提示 -->
    <a-alert v-if="app.version && webVersion !== app.version"
             class="alert-wrapper">
      当前前端版本与后端版本不一致, 请使用 Ctrl + F5 刷新页面
    </a-alert>
    <!-- 升级提示 -->
    <a v-if="app.version && repo.tag_name && ('v' + app.version) !== repo.tag_name"
       class="alert-href"
       target="_blank"
       :href="`https://github.com/dromara/orion-visor/releases/tag/${repo.tag_name}`">
      <a-alert class="alert-wrapper">
        新版本已发布, 请及时升级版本
      </a-alert>
    </a>
    <!-- 系统信息 -->
    <a-descriptions class="detail-container"
                    size="large"
                    :align="{ label: 'right', value: 'left' }"
                    :label-style="{ width: '134px' }"
                    :column="1">
      <!-- 机器码 -->
      <a-descriptions-item label="机器码">
        <span class="text-copy span-blue uuid-wrapper" @click="copy(app.uuid, '已复制')">
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
        {{ repo.tag_name }}
      </a-descriptions-item>
      <!-- 当前后端版本 -->
      <a-descriptions-item label="最新更新日志">
        <a-textarea class="release-node"
                    v-model="repo.body"
                    :auto-size="{ minRows: 3, maxRows: 16 }"
                    readonly>
        </a-textarea>
      </a-descriptions-item>
    </a-descriptions>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'systemSettingAbout',
  };
</script>

<script lang="ts" setup>
  import type { AppInfoResponse, RepoReleaseResponse } from '@/api/system/setting';
  import { onMounted, reactive } from 'vue';
  import { getRepoLatestRelease, getSystemAppInfo } from '@/api/system/setting';
  import { copy } from '@/hooks/copy';
  import { Message } from '@arco-design/web-vue';

  const webVersion = import.meta.env.VITE_APP_VERSION;

  const app = reactive<AppInfoResponse>({
    version: '',
    uuid: '',
  });

  const repo = reactive<RepoReleaseResponse>({
    tag_name: '',
    body: '',
  });

  // 加载应用信息
  onMounted(async () => {
    try {
      const { data } = await getSystemAppInfo();
      app.version = data.version;
      app.uuid = data.uuid;
    } catch (e) {
    }
  });

  // 加载仓库信息
  onMounted(async () => {
    try {
      const { data } = await getRepoLatestRelease();
      repo.tag_name = data.tag_name;
      repo.body = data.body;
    } catch (e) {
      Message.error('获取仓库信息失败, 请等待后重试');
    }
  });

</script>

<style lang="less" scoped>
  @release-node-width: 528px;
  @label-width: 134px;

  .main-container {
    padding-left: 16px;

    .alert-href {
      text-decoration: none;
    }

    .alert-wrapper {
      width: @release-node-width + @label-width;
      margin-bottom: 12px;
    }

    .uuid-wrapper {
      font-weight: 600;
    }

    .release-node {
      width: @release-node-width;
    }
  }
</style>
