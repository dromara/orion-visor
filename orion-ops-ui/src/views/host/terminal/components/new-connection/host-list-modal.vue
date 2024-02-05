<template>
  <a-modal v-model:visible="visible"
           title-align="start"
           :top="128"
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
              :split="false"
              :bordered="false"
              :data="hostList">
        <template #item="{ item }">
          <a-list-item class="host-item-wrapper" @click="clickHost(item)">
            <div class="host-item">
              <!-- 图标 -->
              <span class="host-item-icon">
                <icon-desktop />
              </span>
              <!-- 名称 -->
              <span class="host-item-name">
                {{ `${item.alias || item.name} (${item.address})` }}
              </span>
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
  import type { HostQueryResponse } from '@/api/asset/host';
  import { computed, ref } from 'vue';
  import { useTerminalStore } from '@/store';
  import { PanelSessionType } from '../../types/terminal.const';
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
  const clickHost = (item: HostQueryResponse) => {
    openSession(item, PanelSessionType.SSH, panelIndex.value);
    setVisible(false);
  };

</script>

<style lang="less" scoped>
  .host-list-wrapper {
    padding: 12px;
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
    padding: 12px 16px !important;
    border-radius: 4px;

    &:hover {
      background: var(--color-fill-2);
    }
  }

  .host-item {
    display: flex;
    align-items: center;
    cursor: pointer;

    &-icon {
      font-size: 20px;
      margin-right: 12px;
    }

    &-text {
      font-size: 14px;
    }
  }
</style>
