<template>
  <a-list class="hosts-list-container"
          size="large"
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
      <a-list-item class="host-item-wrapper">
        <div class="host-item">
          <!-- 左侧图标-名称 -->
          <div class="flex-center host-item-left">
            <!-- 图标 -->
            <span class="host-item-left-icon">
              <icon-desktop />
            </span>
            <!-- 名称 -->
            <span class="host-item-left-name">
              <!-- 名称文本 -->
              <template v-if="!item.editable">
                <!-- 文本 -->
                <a-tooltip position="top"
                           :mini="true"
                           content-class="terminal-tooltip-content"
                           arrow-class="terminal-tooltip-content"
                           :content="item.alias || `${item.name} (${item.code})`">
                  <span class="host-item-text host-item-left-name-text">
                    <template v-if="item.alias">
                      {{ item.alias }}
                    </template>
                    <template v-else>
                      {{ `${item.name} (${item.code})` }}
                    </template>
                  </span>
                </a-tooltip>
                <!-- 修改别名 -->
                <a-tooltip position="top"
                           :mini="true"
                           :auto-fix-position="false"
                           content-class="terminal-tooltip-content"
                           arrow-class="terminal-tooltip-content"
                           content="修改别名">
                  <icon-edit class="host-item-left-name-edit"
                             @click="clickEditAlias(item)" />
                </a-tooltip>
              </template>
              <!-- 名称输入框 -->
              <template v-else>
                <a-input v-model="item.alias"
                         ref="aliasNameInput"
                         class="host-item-left-name-input"
                         :max-length="32"
                         :disabled="item.loading"
                         size="mini"
                         :placeholder="item.name"
                         @blur="saveAlias(item)"
                         @press-enter="saveAlias(item)"
                         @change="saveAlias(item)">
                  <template #suffix>
                    <!-- 加载中 -->
                    <icon-loading v-if="item.loading" />
                    <!-- 保存 -->
                    <icon-check v-else
                                class="pointer"
                                title="保存"
                                @click="saveAlias(item)" />
                  </template>
                </a-input>
              </template>
            </span>
          </div>
          <!-- 中间ip -->
          <div class="flex-center host-item-center">
            <!-- ip -->
            <a-tooltip position="top"
                       :mini="true"
                       :auto-fix-position="false"
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
              <!-- 打开 SSH -->
              <a-tooltip position="top"
                         :mini="true"
                         :auto-fix-position="false"
                         content-class="terminal-tooltip-content"
                         arrow-class="terminal-tooltip-content"
                         content="打开 SSH">
                <div class="terminal-sidebar-icon-wrapper">
                  <div class="terminal-sidebar-icon" @click="openSession(item, PanelSessionType.SSH)">
                    <icon-thunderbolt />
                  </div>
                </div>
              </a-tooltip>
              <!-- 打开 SFTP -->
              <a-tooltip position="top"
                         :mini="true"
                         :auto-fix-position="false"
                         content-class="terminal-tooltip-content"
                         arrow-class="terminal-tooltip-content"
                         content="打开 SFTP">
                <div class="terminal-sidebar-icon-wrapper">
                  <div class="terminal-sidebar-icon" @click="openSession(item, PanelSessionType.SFTP)">
                    <icon-folder />
                  </div>
                </div>
              </a-tooltip>
              <!-- 主机设置 -->
              <a-tooltip position="top"
                         :mini="true"
                         :auto-fix-position="false"
                         content-class="terminal-tooltip-content"
                         arrow-class="terminal-tooltip-content"
                         content="主机设置">
                <div class="terminal-sidebar-icon-wrapper">
                  <div class="terminal-sidebar-icon" @click="openSetting(item)">
                    <icon-settings />
                  </div>
                </div>
              </a-tooltip>
              <!-- 收藏 -->
              <a-tooltip position="top"
                         :mini="true"
                         :auto-fix-position="false"
                         content-class="terminal-tooltip-content"
                         arrow-class="terminal-tooltip-content"
                         content="收藏">
                <div class="terminal-sidebar-icon-wrapper">
                  <div class="terminal-sidebar-icon" @click="setFavorite(item)">
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
</template>

<script lang="ts">
  export default {
    name: 'hostListView'
  };
</script>

<script lang="ts" setup>
  import type { HostQueryResponse } from '@/api/asset/host';
  import { ref, nextTick, inject } from 'vue';
  import useFavorite from '@/hooks/favorite';
  import { dataColor } from '@/utils';
  import { tagColor } from '@/views/asset/host-list/types/const';
  import { updateHostExtra } from '@/api/asset/host-extra';
  import { openSettingModalKey, PanelSessionType, ExtraSettingItems } from '../../types/terminal.const';
  import { useTerminalStore } from '@/store';

  const props = defineProps<{
    hostList: Array<HostQueryResponse>;
    emptyValue: string;
  }>();

  const { openSession } = useTerminalStore();
  const { toggle: toggleFavorite, loading: favoriteLoading } = useFavorite('HOST');

  const aliasNameInput = ref();

  // 点击修改别名
  const clickEditAlias = (item: HostQueryResponse) => {
    item.editable = true;
    if (!item.alias) {
      item.alias = '';
    }
    nextTick(() => {
      aliasNameInput.value?.focus();
    });
  };

  // 保存别名
  const saveAlias = async (item: HostQueryResponse) => {
    item.loading = true;
    item.modCount = (item.modCount || 0) + 1;
    if (item.modCount != 1) {
      return;
    }
    try {
      // 修改别名
      await updateHostExtra({
        hostId: item.id,
        item: ExtraSettingItems.LABEL,
        extra: JSON.stringify({ alias: item.alias })
      });
      item.editable = false;
    } catch (e) {
    } finally {
      item.loading = false;
      item.modCount = 0;
    }
  };

  // 打开配置
  const openSetting = inject(openSettingModalKey) as (record: HostQueryResponse) => void;

  // 设置收藏
  const setFavorite = async (item: HostQueryResponse) => {
    if (favoriteLoading.value) {
      return;
    }
    await toggleFavorite(item, item.id);
  };

</script>

<style lang="less" scoped>
  @host-item-height: 56px;

  .hosts-list-container {
    height: 100%;
  }

  :deep(.arco-scrollbar) {
    height: 100%;

    .arco-list-bordered {
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
  }

  .host-item-wrapper {
    padding: 0 !important;
    height: @host-item-height;
    font-size: 12px;
    color: var(--color-content-text-2);

    .host-item {
      width: 100%;
      height: @host-item-height;
      display: flex;
      justify-content: space-between;
      align-items: center;
      position: relative;

      &-text {
        display: inline-block;
        white-space: pre;
        word-break: break-all;
        overflow: hidden;
        text-overflow: ellipsis;
      }

      &:hover {
        .host-item-left-name-edit {
          display: inline;
        }

        .host-item-right-tags {
          display: none;
        }

        .host-item-right-actions {
          display: flex;
        }
      }
    }

    .host-item-left {
      width: 35%;
      height: 100%;
      padding-left: 18px;
      position: absolute;

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
        // 100% - icon-width - icon-margin-right
        width: calc(100% - 42px);
        display: flex;
        align-items: center;

        &-text {
          // 100% - edit-margin-left - edit-font-size
          max-width: calc(100% - 18px);
        }

        &-edit {
          display: none;
          margin-left: 4px;
          cursor: pointer;
          color: rgb(var(--blue-6));
          font-size: 14px;
        }

        &-input {
          width: 80%;
        }
      }
    }

    .host-item-center {
      width: 25%;
      height: 100%;
      left: 35%;
      padding: 0 8px;
      position: absolute;

      &-address {
        max-width: 100%;
      }
    }

    .host-item-right {
      width: 40%;
      height: 100%;
      left: 60%;
      padding-right: 18px;
      flex-direction: column;
      justify-content: center;
      position: absolute;

      &-tags {
        width: 100%;
      }

      &-actions {
        display: none;
        width: 100%;
        justify-content: flex-end;
      }
    }
  }

  .favorite {
    color: rgb(var(--yellow-6));
  }
</style>
