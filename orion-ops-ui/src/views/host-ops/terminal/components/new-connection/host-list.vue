<template>
  <div class="hosts-list-container">
    <a-list size="large"
            max-height="100%"
            :hoverable="true"
            :data="hostList">
      <!-- 空数据 -->
      <template #empty>
        <a-empty>
          <template #image>
            <icon-desktop />
          </template>
          {{ emptyValue }}
        </a-empty>
      </template>
      <!-- 数据 -->
      <template #item="{ item }">
        <a-list-item class="host-item-wrapper" @click="openTerminal(item)">
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
            <!-- 右侧tag-操作 -->
            <div class="flex-center host-item-right">
              <!-- tags -->
              <div class="host-item-right-tags">
                <template v-if="item.tags?.length">
                  <a-tag v-for="(tag, i) in item.tags"
                         class="host-item-text"
                         :key="tag.id"
                         :style="{
                           maxWidth: `calc(${100 / item.tags.length}% - ${i !== item.tags.length - 1 ? '8px' : '0px'})`,
                           marginRight: `${i !== item.tags.length - 1 ? '8px' : '0'}`,
                         }"
                         :color="dataColor(tag.name, tagColor)">
                    {{ tag.name }}
                  </a-tag>
                </template>
              </div>
              <!-- 操作 -->
              <div class="host-item-right-actions">
                <!-- 连接主机 -->
                <a-tooltip position="top"
                           :mini="true"
                           :popup-visible="true"
                           content-class="terminal-tooltip-content"
                           arrow-class="terminal-tooltip-content"
                           content="连接主机">
                  <div class="terminal-sidebar-icon-wrapper">
                    <div class="terminal-sidebar-icon" @click.stop="openTerminal(item)">
                      <icon-thunderbolt />
                    </div>
                  </div>
                </a-tooltip>
                <!-- 连接设置 -->
                <a-tooltip position="top"
                           :mini="true"
                           content-class="terminal-tooltip-content"
                           arrow-class="terminal-tooltip-content"
                           content="连接设置">
                  <div class="terminal-sidebar-icon-wrapper">
                    <div class="terminal-sidebar-icon" @click.stop="openSetting(item)">
                      <icon-settings />
                    </div>
                  </div>
                </a-tooltip>
                <!-- 收藏 -->
                <a-tooltip position="top"
                           :mini="true"
                           content-class="terminal-tooltip-content"
                           arrow-class="terminal-tooltip-content"
                           content="收藏">
                  <div class="terminal-sidebar-icon-wrapper">
                    <div class="terminal-sidebar-icon" @click.stop="setFavorite(item)">
                      <icon-star-fill class="favorite" v-if="item.favorite" />
                      <icon-star v-else />
                    </div>
                  </div>
                </a-tooltip>
              </div>
            </div>
          </div>
        </a-list-item>
      </template>
    </a-list>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'hostList'
  };
</script>

<script lang="ts" setup>
  import type { HostQueryResponse } from '@/api/asset/host';
  import useFavorite from '@/hooks/favorite';
  import { dataColor } from '@/utils';
  import { tagColor } from '@/views/asset/host-list/types/const';

  const props = defineProps<{
    hostList: Array<HostQueryResponse>,
    emptyValue: string
  }>();

  const { toggle: toggleFavorite, loading: favoriteLoading } = useFavorite('HOST');

  // 打开终端
  const openTerminal = (item: any) => {
    console.log('ter', item);
  };

  // 打开配置
  const openSetting = (item: any) => {
    console.log('set', item);
  };

  // 设置收藏
  const setFavorite = async (item: any) => {
    if (favoriteLoading.value) {
      return;
    }
    await toggleFavorite(item, item.id);
  };

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
        max-width: 100%;
      }
    }

    .host-item-right {
      width: 40%;
      height: 100%;
      flex-direction: column;
      justify-content: center;
      position: relative;

      &-tags {
        // 必须设置 最外层用的是 min-width
        position: absolute;
        width: 100%;
      }

      &-actions {
        position: absolute;
        display: none;
        width: 100%;
        justify-content: flex-end;
      }
    }

    &:hover {
      .host-item-right-tags {
        display: none;
      }

      .host-item-right-actions {
        display: flex;
      }
    }

  }

  .favorite {
    color: rgb(var(--yellow-6));
  }
</style>
