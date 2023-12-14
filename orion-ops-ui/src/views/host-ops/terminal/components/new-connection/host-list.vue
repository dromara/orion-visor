<template>
  <a-list size="large"
          max-height="100%"
          :hoverable="true"
          :data="hosts.hostList">
    <!-- 空数据 -->
    <template #empty>
      <a-empty>
        <template #image>
          <icon-desktop />
        </template>
        当前分组内无授权主机!
      </a-empty>
    </template>
    <!-- 数据 -->
    <template #item="{ item }">
      <a-list-item class="host-item-wrapper">
        <div class="host-item">
          <!-- 左侧图标-名称 -->
          <div class="flex-center host-item-left">
            <!-- 图标 -->
            <span class="host-item-left-icon">
              <icon-desktop />
            </span>
            <!-- 名称 -->
            <a-tooltip position="top"
                       :mini="true"
                       content-class="terminal-tooltip-content"
                       arrow-class="terminal-tooltip-content"
                       :content="`${item.name} (${item.code})`">
              <span class="host-item-text host-item-left-name">
                {{ `${item.name} (${item.code})` }}
              </span>
            </a-tooltip>
          </div>
          <!-- 中间ip -->
          <div class="flex-center host-item-center">
            <!-- ip -->
            <a-tooltip position="top"
                       :mini="true"
                       content-class="terminal-tooltip-content"
                       arrow-class="terminal-tooltip-content"
                       :content="item.address">
              <span class="host-item-text host-item-center-address">
                {{ item.address }}
              </span>
            </a-tooltip>
          </div>
          <!-- flex-center 右侧tag-操作 -->
          <div class="flex-center host-item-right">
            <div v-if="item.code === 'main'">
              <a-tag v-for="i in 5"
                     class="host-item-text"
                     :style="{
                    width: `calc(${100/5}% - ${i !== 5 ? '8px' : '0px'})`,
                    maxWidth: `calc(${100/5}% - ${i !== 5 ? '8px' : '0px'})`,
                    marginRight: `${i !== 5 ? '8px' : '0'}`,
                   }"
                     color="arcoblue">
                <template v-for="j in i*5">
                  {{ j }}
                </template>
              </a-tag>
            </div>
            <div v-else>

            </div>
          </div>
        </div>
      </a-list-item>
    </template>
  </a-list>
</template>

<script lang="ts">
  export default {
    name: 'hostList'
  };
</script>

<script lang="ts" setup>
  import { AuthorizedHostQueryResponse } from '@/api/asset/asset-authorized-data';

  const props = defineProps<{
    hosts: AuthorizedHostQueryResponse
  }>();

</script>

<style lang="less" scoped>
  @host-item-height: 56px;

  :deep(.arco-list-bordered) {
    border: 1px solid var(--color-fill-3);

    .arco-empty {
      padding: 16px 0;
      flex-direction: column;

      .arco-empty-image {
        margin-bottom: 0;
      }
    }

    .arco-list-item:not(:last-child) {
      border-bottom: 1px solid var(--color-fill-3);
    }

    .arco-list-item:hover {
      background-color: var(--color-fill-2);
    }
  }

  .host-item-wrapper {
    padding: 0 !important;
    height: @host-item-height;
    cursor: pointer;
    font-size: 12px;
    color: var(--color-content-text-2);

    .host-item {
      width: 100%;
      padding: 0 18px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      height: @host-item-height;

      &-text {
        display: inline-block;
        white-space: pre;
        word-break: break-all;
        overflow: hidden;
        text-overflow: ellipsis;
      }
    }

    .host-item-left {
      width: 35%;

      &-icon {
        width: 32px;
        height: 32px;
        border-radius: 32px;
        margin-right: 10px;
        font-size: 16px;
        display: flex;
        justify-content: center;
        align-items: center;
        color: var(--color-text-3);
        background: var(--color-fill-3);
      }

      &-name {
        max-width: calc(100% - 32px - 12px - 8px);
      }
    }

    .host-item-center {
      width: 25%;

      &-address {
        max-width: calc(100% - 8px);
      }
    }

    .host-item-right {
      width: 40%;
    }

  }
</style>
