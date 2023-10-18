<template>
  <card-list v-model:searchValue="formModel.searchValue"
             search-input-placeholder="输入 id / 名称"
             create-card-position="head"
             :loading="loading"
             :fieldConfig="fieldConfig"
             :list="list"
             :pagination="pagination"
             :card-layout-cols="cardColLayout"
             :handle-visible="{disableFilter: true}"
             :add-permission="['asset:host-key:create']"
             @add="emits('openAdd')"
             @reset="reset"
             @search="fetchCardData"
             @page-change="fetchCardData">
    <!-- 标题 -->
    <template #title="{ record }">
      {{ record.name }}
    </template>
    <!-- 拓展操作 -->
    <template #extra="{ record }">
      <a-space>
        <!-- 更多操作 -->
        <a-dropdown trigger="hover">
          <icon-more class="card-extra-icon" />
          <template #content>
            <!-- 详情 -->
            <a-doption v-permission="['asset:host-key:detail', 'asset:host-key:update']"
                       @click="emits('openView', record)">
              <icon-list />
              详情
            </a-doption>
            <!-- 修改 -->
            <a-doption v-permission="['asset:host-key:update']"
                       @click="emits('openUpdate', record)">
              <icon-edit />
              修改
            </a-doption>
            <!-- 删除 -->
            <a-doption v-permission="['asset:host-key:delete']"
                       class="span-red"
                       @click="deleteRow(record.id)">
              <icon-delete />
              删除
            </a-doption>
          </template>
        </a-dropdown>
      </a-space>
    </template>
    <!-- 右键菜单 -->
    <template #contextMenu="{ record }">
      <!-- 详情 -->
      <a-doption v-permission="['asset:host-key:detail', 'asset:host-key:update']"
                 @click="emits('openView', record)">
        <icon-list />
        详情
      </a-doption>
      <!-- 修改 -->
      <a-doption v-permission="['asset:host-key:update']"
                 @click="emits('openUpdate', record)">
        <icon-edit />
        修改
      </a-doption>
      <!-- 删除 -->
      <a-doption v-permission="['asset:host-key:delete']"
                 class="span-red"
                 @click="deleteRow(record.id)">
        <icon-delete />
        删除
      </a-doption>
    </template>
  </card-list>
</template>

<script lang="ts">
  export default {
    name: 'asset-host-key-card-list'
  };
</script>

<script setup lang="ts">
  import { usePagination, useColLayout } from '@/types/card';
  import { reactive, ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import { resetObject } from '@/utils';
  import fieldConfig from '../types/card.fields';
  import { deleteHostKey, getHostKeyPage, HostKeyQueryRequest, HostKeyQueryResponse } from '@/api/asset/host-key';
  import { Message, Modal } from '@arco-design/web-vue';

  const { loading, setLoading } = useLoading();
  const cardColLayout = useColLayout();
  const pagination = usePagination();
  const list = ref<HostKeyQueryResponse[]>([]);
  const emits = defineEmits(['openAdd', 'openUpdate', 'openView']);

  const formModel = reactive<HostKeyQueryRequest>({
    searchValue: undefined,
  });

  // 删除当前行
  const deleteRow = (id: number) => {
    Modal.confirm({
      title: '删除前确认!',
      titleAlign: 'start',
      content: '确定要删除这条记录吗?',
      okText: '删除',
      onOk: async () => {
        try {
          setLoading(true);
          // 调用删除接口
          await deleteHostKey(id);
          Message.success('删除成功');
          // 重新加载数据
          await fetchCardData();
        } catch (e) {
        } finally {
          setLoading(false);
        }
      }
    });
  };

  // 添加后回调
  const addedCallback = () => {
    fetchCardData();
  };

  // 更新后回调
  const updatedCallback = () => {
    fetchCardData();
  };

  defineExpose({
    addedCallback, updatedCallback
  });

  // 重置条件
  const reset = () => {
    resetObject(formModel);
    fetchCardData();
  };

  // 加载数据
  const doFetchCardData = async (request: HostKeyQueryRequest) => {
    try {
      setLoading(true);
      const { data } = await getHostKeyPage(request);
      list.value = data.rows;
      pagination.total = data.total;
      pagination.current = request.page;
      pagination.pageSize = request.limit;
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 切换页码
  const fetchCardData = (page = 1, limit = pagination.pageSize, form = formModel) => {
    doFetchCardData({ page, limit, ...form });
  };
  fetchCardData();

</script>

<style scoped lang="less">

</style>
