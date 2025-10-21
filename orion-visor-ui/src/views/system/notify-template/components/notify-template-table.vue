<template>
  <!-- 搜索 -->
  <a-card class="general-card table-search-card">
    <query-header :model="formModel"
                  label-align="left"
                  @submit="fetchTableData"
                  @reset="fetchTableData"
                  @keyup.enter="() => fetchTableData()">
      <!-- id -->
      <a-form-item field="id" label="id">
        <a-input-number v-model="formModel.id"
                        placeholder="请输入id"
                        hide-button
                        allow-clear />
      </a-form-item>
      <!-- 通知名称 -->
      <a-form-item field="name" label="通知名称">
        <a-input v-model="formModel.name"
                 placeholder="请输入通知名称"
                 allow-clear />
      </a-form-item>
      <!-- 渠道类型 -->
      <a-form-item field="channelType" label="渠道类型">
        <a-select v-model="formModel.channelType"
                  :options="toOptions(ChannelTypeKey)"
                  placeholder="请选择渠道类型"
                  allow-clear />
      </a-form-item>
      <!-- 通知描述 -->
      <a-form-item field="name" label="通知描述">
        <a-input v-model="formModel.name"
                 placeholder="请输入通知描述"
                 allow-clear />
      </a-form-item>
    </query-header>
  </a-card>
  <!-- 内容部分 -->
  <div class="container-content">
    <!-- 业务类型 -->
    <a-card class="general-card table-search-card biz-card">
      <a-tabs v-model:active-key="bizType"
              direction="vertical"
              type="rounded"
              :hide-content="true">
        <a-tab-pane v-for="item in toOptions(BizTypeKey)"
                    :key="item.value as string"
                    :title="item.label" />
      </a-tabs>
    </a-card>
    <!-- 表格 -->
    <a-card class="general-card table-card">
      <template #title>
        <!-- 左侧操作 -->
        <div class="table-left-bar-handle">
          <!-- 标题 -->
          <div class="table-title">
            通知模板列表
          </div>
        </div>
        <!-- 右侧操作 -->
        <div class="table-right-bar-handle">
          <a-space>
            <!-- 新增 -->
            <a-button v-permission="['infra:notify-template:create']"
                      type="primary"
                      @click="emits('openAdd')">
              新增
              <template #icon>
                <icon-plus />
              </template>
            </a-button>
            <!-- 调整 -->
            <table-adjust :columns="columns"
                          :columns-hook="columnsHook"
                          :query-order="queryOrder"
                          @query="fetchTableData" />
          </a-space>
        </div>
      </template>
      <!-- table -->
      <a-table row-key="id"
               ref="tableRef"
               class="table-resize"
               :loading="loading"
               :columns="tableColumns"
               :data="tableRenderData"
               :pagination="pagination"
               :bordered="false"
               :column-resizable="true"
               @page-change="(page: number) => fetchTableData(page, pagination.pageSize)"
               @page-size-change="(size: number) => fetchTableData(1, size)">
        <!-- 渠道类型 -->
        <template #channelType="{ record }">
          <a-tag :color="getDictValue(ChannelTypeKey, record.channelType, 'color')">
            {{ getDictValue(ChannelTypeKey, record.channelType) }}
          </a-tag>
        </template>
        <!-- 消息标识 -->
        <template #messageTag="{ record }">
          <!-- webhook -->
          <span v-if="getDictValue(ChannelTypeKey, record.channelType, 'notifyType') === NotifyType.WEBHOOK"
                class="text-copy"
                @click="copy(extraWebhook(record), true)">
            {{ extraWebhook(record) }}
          </span>
          <!-- 站内信 -->
          <span v-else-if="getDictValue(ChannelTypeKey, record.channelType, 'notifyType') === NotifyType.WEBSITE">
            <component :is="extraWebsite(record)" />
          </span>
        </template>
        <!-- 操作 -->
        <template #handle="{ record }">
          <div class="table-handle-wrapper">
            <!-- 修改 -->
            <a-button v-permission="['infra:notify-template:update']"
                      type="text"
                      size="mini"
                      @click="emits('openUpdate', record)">
              修改
            </a-button>
            <!-- 复制 -->
            <a-button v-permission="['infra:notify-template:create']"
                      type="text"
                      size="mini"
                      @click="emits('openCopy', record)">
              复制
            </a-button>
            <!-- 删除 -->
            <a-popconfirm content="确认删除这条记录吗?"
                          position="left"
                          type="warning"
                          @ok="deleteRow(record)">
              <a-button v-permission="['infra:notify-template:delete']"
                        type="text"
                        size="mini"
                        status="danger">
                删除
              </a-button>
            </a-popconfirm>
          </div>
        </template>
      </a-table>
    </a-card>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'notifyTemplateTable'
  };
</script>

<script lang="ts" setup>
  import type { NotifyTemplateQueryRequest, NotifyTemplateQueryResponse } from '@/api/system/notify-template';
  import { h, reactive, ref, onMounted } from 'vue';
  import { deleteNotifyTemplate, getNotifyTemplatePage } from '@/api/system/notify-template';
  import { Message, Tag } from '@arco-design/web-vue';
  import useLoading from '@/hooks/loading';
  import columns from '../types/table.columns';
  import { copy } from '@/hooks/copy';
  import { TableName, BizTypeKey, ChannelTypeKey, BizType, NotifyType, messageClassifyKey, messageTypeKey } from '../types/const';
  import { useTablePagination, useTableColumns } from '@/hooks/table';
  import { useDictStore } from '@/store';
  import { useQueryOrder, ASC } from '@/hooks/query-order';
  import TableAdjust from '@/components/app/table-adjust/index.vue';

  const emits = defineEmits(['openAdd', 'openUpdate', 'openCopy']);

  const pagination = useTablePagination();
  const { loading, setLoading } = useLoading();
  const queryOrder = useQueryOrder(TableName, ASC);
  const { tableColumns, columnsHook } = useTableColumns(TableName, columns);
  const { toOptions, getDictValue } = useDictStore();

  const bizType = ref<string>(BizType.ALARM);
  const tableRenderData = ref<Array<NotifyTemplateQueryResponse>>([]);
  const formModel = reactive<NotifyTemplateQueryRequest>({
    id: undefined,
    name: undefined,
    channelType: undefined,
  });

  // 提取 webhook
  const extraWebhook = (record: NotifyTemplateQueryResponse) => {
    try {
      return JSON.parse(record.channelConfig).webhook;
    } catch (e) {
      return '';
    }
  };

  // 提取 website
  const extraWebsite = (record: NotifyTemplateQueryResponse) => {
    try {
      const parse = JSON.parse(record.channelConfig);
      return h('div', {}, [
        h(Tag, { style: { 'margin-right': '8px' }, color: 'green' }, { default: () => getDictValue(messageClassifyKey, parse.messageClassify) }),
        h(Tag, { style: { 'margin-right': '8px' }, color: 'green' }, { default: () => getDictValue(messageTypeKey, parse.messageType) }),
        h(Tag, { color: 'purple' }, { default: () => parse.title }),
      ]);
    } catch (e) {
      return h('span', {}, '');
    }
  };

  // 删除当前行
  const deleteRow = async (record: NotifyTemplateQueryResponse) => {
    try {
      setLoading(true);
      // 调用删除接口
      await deleteNotifyTemplate(record.id);
      Message.success('删除成功');
      // 重新加载
      reload();
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 重新加载
  const reload = () => {
    // 重新加载数据
    fetchTableData();
  };

  defineExpose({ reload });

  // 加载数据
  const doFetchTableData = async (request: NotifyTemplateQueryRequest) => {
    try {
      setLoading(true);
      const { data } = await getNotifyTemplatePage(queryOrder.markOrderly({ ...request, bizType: bizType.value }));
      tableRenderData.value = data.rows;
      pagination.total = data.total;
      pagination.current = request.page;
      pagination.pageSize = request.limit;
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 切换页码
  const fetchTableData = (page = 1, limit = pagination.pageSize, form = formModel) => {
    doFetchTableData({ page, limit, ...form });
  };

  onMounted(() => {
    fetchTableData();
  });

</script>

<style lang="less" scoped>
  @biz-card-width: 120px;
  .container-content {
    display: flex;
  }

  .biz-card {
    width: @biz-card-width;
    margin: 0 16px 0 0 !important;
    user-select: none;
  }

  .table-card {
    width: calc(100% - @biz-card-width - 16px);
  }
</style>
