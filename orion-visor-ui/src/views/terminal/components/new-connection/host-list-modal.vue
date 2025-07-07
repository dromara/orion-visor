<template>
  <a-modal v-model:visible="visible"
           title-align="start"
           :top="136"
           :width="520"
           :align-center="false"
           :draggable="true"
           :unmount-on-close="true"
           :body-style="{ padding: 0 }"
           :hide-title="true"
           :footer="false">
    <div class="host-list-wrapper">
      <!-- 主机搜索框 -->
      <div class="host-filter-wrapper">
        <span class="host-label">主机列表</span>
        <a-input v-model="filterValue"
                 class="host-filter"
                 placeholder="别名/名称/编码/IP" />
      </div>
      <!-- 主机列表 -->
      <a-list class="host-list"
              max-height="50vh"
              :hoverable="true"
              :bordered="false"
              :data="hostList">
        <template #item="{ item }">
          <a-list-item class="host-item-wrapper">
            <div class="host-item">
              <!-- 左侧 -->
              <div class="host-item-left">
                <!-- 图标 -->
                <span class="host-item-icon">
                  <icon-desktop />
                </span>
                <!-- 名称 -->
                <span class="host-item-name"
                      :title="`${item.alias || item.name} (${item.address})`">
                  {{ `${item.alias || item.name} (${item.address})` }}
                </span>
              </div>
              <!-- 操作 -->
              <div class="host-item-actions">
                <!-- 打开会话 -->
                <template v-for="type in TerminalSessionTypes">
                  <template v-if="item.types?.includes(type.protocol)">
                    <a-tooltip position="top"
                               :mini="true"
                               :auto-fix-position="false"
                               :content="`打开 ${type.type}`"
                               content-class="terminal-tooltip-content"
                               arrow-class="terminal-tooltip-content">
                      <a-button class="host-item-action icon-button" @click="openHostSession(item, type)">
                        <component :is="type.connectIcon || type.icon" />
                      </a-button>
                    </a-tooltip>
                  </template>
                </template>
              </div>
            </div>
          </a-list-item>
        </template>
      </a-list>
    </div>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'hostListModal'
  };
</script>

<script lang="ts" setup>
  import type { TerminalSessionType } from '@/views/terminal/interfaces';
  import type { HostQueryResponse } from '@/api/asset/host';
  import { computed, ref } from 'vue';
  import { useTerminalStore } from '@/store';
  import { TerminalSessionTypes } from '../../types/const';
  import useVisible from '@/hooks/visible';

  const { hosts, openSession } = useTerminalStore();
  const { visible, setVisible } = useVisible();

  const panelIndex = ref();
  const filterValue = ref('');

  const hostList = computed(() => {
    const filterVal = filterValue.value.toLowerCase();
    let list;
    // 过滤
    if (filterVal) {
      list = hosts.hostList.filter(item => {
        return (item.name as string)?.toLowerCase().indexOf(filterVal) > -1
          || (item.code as string)?.toLowerCase().indexOf(filterVal) > -1
          || (item.alias as string)?.toLowerCase().indexOf(filterVal) > -1
          || (item.address as string)?.toLowerCase().indexOf(filterVal) > -1;
      });
    } else {
      list = hosts.hostList;
    }
    // 排序
    return list.sort((o1, o2) => {
      if (o1.favorite || o2.favorite) {
        if (o1.favorite && o2.favorite) {
          return o2.id < o1.id ? 1 : -1;
        }
        return o2.favorite ? 1 : -1;
      } else {
        return o2.id < o1.id ? 1 : -1;
      }
    });
  });

  // 打开配置
  const open = (index: number) => {
    panelIndex.value = index;
    setVisible(true);
  };

  defineExpose({ open });

  // 打开终端
  const openHostSession = (item: HostQueryResponse, tab: TerminalSessionType) => {
    openSession(item, tab, panelIndex.value);
    setVisible(false);
  };

</script>

<style lang="less" scoped>
  @modal-width: 520px;
  @item-height: 54px;
  @item-padding-y: 4px;
  @item-padding-x: 12px;
  @action-size: 32px;

  .host-list-wrapper {
    padding: 12px;

    :deep(.arco-list-item-main) {
      width: 100%;

      .arco-list-item-content {
        width: 100%;
      }
    }
  }

  .host-filter-wrapper {
    height: 32px;
    margin-bottom: 12px;
    display: flex;
    justify-content: space-between;
    align-items: center;

    .host-label {
      margin-left: 8px;
      user-select: none;
      font-size: 18px;
      font-weight: 600;
      letter-spacing: 2px;
    }

    .host-filter {
      width: 60%;
    }
  }

  .host-item-wrapper {
    padding: @item-padding-y @item-padding-x !important;
    height: @item-height;
    display: flex;
    align-items: center;

    &:hover {
      background: var(--color-fill-2);
      border-radius: 4px;

      .host-item-left {
        width: auto;
      }

      .host-item-actions {
        display: flex;
      }
    }
  }

  .host-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    height: @item-height - @item-padding-y * 2;

    &-left {
      width: @modal-width - 48px;
      display: flex;
      flex: 1;
      align-items: center;
      overflow: hidden;
    }

    &-icon {
      font-size: 20px;
      margin-right: 12px;
    }

    &-name {
      font-size: 14px;
      overflow: hidden;
      white-space: pre;
      text-overflow: ellipsis;
    }

    &-actions {
      height: 100%;
      padding-left: 8px;
      display: none;
      flex-shrink: 0;
      align-items: center;
      justify-content: flex-end;
    }

    &-action {
      width: @action-size;
      height: @action-size;
      margin: 0 4px;
      border-radius: 4px;
      font-size: 22px;

      &:hover {
        background: var(--color-neutral-4);
      }
    }
  }

</style>
