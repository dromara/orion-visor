<template>
  <div class="layout-container">
    <!-- 顶部 -->
    <div class="top-side">
      <!-- 提示 -->
      <div class="panel">
        <Banner />
      </div>
    </div>
    <div class="row-wrapper">
      <div class="left-side">
        <!-- 操作日志 -->
        <a-card class="general-card"
                title="操作日志"
                :header-style="{ paddingBottom: '0' }"
                :body-style="{ padding: '8px 20px 8px 20px' }">
          <operator-log-simple-table :current="true"
                                     :handle-column="false" />
        </a-card>
      </div>
      <a-grid class="right-side"
              :cols="24"
              :row-gap="16">
        <!-- 快捷操作 -->
        <a-grid-item class="card-wrapper" :span="24">
          <quick-operation />
        </a-grid-item>
        <!-- 文档 -->
        <a-grid-item class="panel" :span="24">
          <docs />
        </a-grid-item>
      </a-grid>
    </div>
  </div>
</template>

<script lang="ts" setup>
  import Banner from './components/banner.vue';
  import QuickOperation from './components/quick-operation.vue';
  import Docs from './components/docs.vue';
  import OperatorLogSimpleTable from '@/views/user/operator-log/components/operator-log-simple-table.vue';
</script>

<script lang="ts">
  export default {
    name: 'workplace',
  };
</script>

<style lang="less" scoped>
  .top-side {
    flex: 1;
    overflow: auto;
  }

  .row-wrapper {
    margin-top: 16px;
    width: 100%;
    display: flex;

    .left-side {
      width: calc(100% - 296px);
      margin-right: 16px;
    }

    .right-side {
      width: 280px;
    }
  }

  .panel {
    background-color: var(--color-bg-2);
    border-radius: 4px;
    overflow: auto;
  }

  :deep(.panel-border) {
    margin-bottom: 0;
    border-bottom: 1px solid rgb(var(--gray-2));
  }

  .card-wrapper {
    border-radius: 4px;
    background-color: var(--color-bg-2);

    :deep(.text) {
      font-size: 12px;
      text-align: center;
      color: rgb(var(--gray-8));
    }

    :deep(.wrapper) {
      margin-bottom: 8px;
      text-align: center;
      cursor: pointer;

      &:last-child {
        .text {
          margin-bottom: 0;
        }
      }

      &:hover {
        .icon {
          color: rgb(var(--arcoblue-6));
          background-color: #E8F3FF;
        }

        .text {
          color: rgb(var(--arcoblue-6));
        }
      }
    }

    :deep(.icon) {
      display: inline-block;
      width: 32px;
      height: 32px;
      margin-bottom: 4px;
      color: rgb(var(--dark-gray-1));
      line-height: 32px;
      font-size: 16px;
      text-align: center;
      background-color: rgb(var(--gray-1));
      border-radius: 4px;
    }
  }
</style>
