<template>
  <div class="terminal-setting-container">
    <div class="terminal-setting-wrapper">
      <!-- 主标题 -->
      <h2 class="terminal-setting-title">新建连接</h2>
      <!-- 操作栏 -->
      <div class="terminal-setting-block header-actions">
        <a-radio-group type="button">
          <a-radio value="1">分组</a-radio>
          <a-radio value="2">列表</a-radio>
          <a-radio value="3">最近连接</a-radio>
        </a-radio-group>
        <a-input-search class="host-filter"
                        placeholder="输入名称/编码/IP 进行过滤" />
      </div>
      <!-- 授权主机 -->
      <div class="terminal-setting-block">
        <!-- 顶部 -->
        <div class="terminal-setting-subtitle-wrapper">
          <h3 class="terminal-setting-subtitle">
            授权主机
          </h3>
        </div>
        <!-- 内容区域 -->
        <div class="terminal-setting-body hosts-container">
          <div class="host-tree">
            <a-tree :data="hosts.groupTree"
                    :blockNode="true"
            >

            </a-tree>
            {{ }}
          </div>
          <div class="host-list">
            {{ hosts.treeNodes }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'newConnectionView'
  };
</script>

<script lang="ts" setup>
  import type { AuthorizedHostQueryResponse } from '@/api/asset/asset-authorized-data';
  import { getCurrentAuthorizedHost } from '@/api/asset/asset-authorized-data';
  import { onMounted, ref } from 'vue';

  const hosts = ref<AuthorizedHostQueryResponse>({} as AuthorizedHostQueryResponse);

  onMounted(async () => {
    const { data } = await getCurrentAuthorizedHost();
    hosts.value = data;
  });
</script>

<style lang="less" scoped>
  .header-actions {
    display: flex;
    align-items: center;
    justify-content: space-between;

    .host-filter {
      width: 40%;
    }
  }

  .hosts-container {
    justify-content: space-between;

    .host-tree {
      margin-right: 16px;
      width: 350px;
    }

    .host-list {
      width: 490px;
    }
  }

</style>
