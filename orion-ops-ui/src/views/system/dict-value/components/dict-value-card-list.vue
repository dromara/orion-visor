<template>
  <card-list v-model:searchValue="formModel.searchValue"
             search-input-placeholder="输入xxx"
             create-card-position="head"
             :loading="loading"
             :fieldConfig="fieldConfig"
             :list="list"
             :pagination="pagination"
             :card-layout-cols="cardColLayout"
             :filter-count="filterCount"
             :add-permission="['infra:dict-value:create']"
             @add="emits('openAdd')"
             @reset="reset"
             @search="fetchCardData"
             @page-change="fetchCardData">
    <!-- 标题 -->
    <template #title="{ record }">
      {{ record.id }}
    </template>
    <!-- 拓展操作 -->
    <template #extra="{ record }">
      <a-space>
        <!-- 更多操作 -->
        <a-dropdown trigger="hover">
          <icon-more class="card-extra-icon" />
          <template #content>
            <!-- 修改 -->
            <a-doption v-permission="['infra:dict-value:update']"
                       @click="emits('openUpdate', record)">
              <icon-edit />
              修改
            </a-doption>
            <!-- 删除 -->
            <a-doption v-permission="['infra:dict-value:delete']"
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
      <!-- 修改 -->
      <a-doption v-permission="['infra:dict-value:update']"
                 @click="emits('openUpdate', record)">
        <icon-edit />
        修改
      </a-doption>
      <!-- 删除 -->
      <a-doption v-permission="['infra:dict-value:delete']"
                 class="span-red"
                 @click="deleteRow(record.id)">
        <icon-delete />
        删除
      </a-doption>
    </template>
    <!-- 过滤条件 -->
    <template #filterContent>
      <a-form :model="formModel"
              class="modal-form"
              size="small"
              ref="formRef"
              label-align="right"
              :style="{ width: '320px' }"
              :label-col-props="{ span: 6 }"
              :wrapper-col-props="{ span: 18 }">
        <!-- id -->
        <a-form-item field="id" label="id">
          <a-input-number v-model="formModel.id"
                          placeholder="请输入id"
                          allow-clear
                          hide-button />
        </a-form-item>
        <!-- 配置项id -->
        <a-form-item field="keyId" label="配置项id">
          <a-input-number v-model="formModel.keyId"
                          placeholder="请输入配置项id"
                          allow-clear
                          hide-button />
        </a-form-item>
        <!-- 配置项 -->
        <a-form-item field="keyName" label="配置项">
          <a-input v-model="formModel.keyName" placeholder="请输入配置项" allow-clear />
        </a-form-item>
        <!-- 配置名称 -->
        <a-form-item field="name" label="配置名称">
          <a-input v-model="formModel.name" placeholder="请输入配置名称" allow-clear />
        </a-form-item>
        <!-- 配置值 -->
        <a-form-item field="value" label="配置值">
          <a-input v-model="formModel.value" placeholder="请输入配置值" allow-clear />
        </a-form-item>
        <!-- 配置描述 -->
        <a-form-item field="label" label="配置描述">
          <a-input v-model="formModel.label" placeholder="请输入配置描述" allow-clear />
        </a-form-item>
        <!-- 额外参数 -->
        <a-form-item field="extra" label="额外参数">
          <a-input v-model="formModel.extra" placeholder="请输入额外参数" allow-clear />
        </a-form-item>
        <!-- 排序 -->
        <a-form-item field="sort" label="排序">
          <a-input-number v-model="formModel.sort"
                          placeholder="请输入排序"
                          allow-clear
                          hide-button />
        </a-form-item>
      </a-form>
    </template>
  </card-list>
</template>

<script lang="ts">
  export default {
    name: 'system-dict-value-card-list'
  };
</script>

<script lang="ts" setup>
  import { usePagination, useColLayout } from '@/types/card';
  import { computed, reactive, ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import { objectTruthKeyCount, resetObject } from '@/utils';
  import fieldConfig from '../types/card.fields';
  import { deleteDictValue, getDictValuePage, DictValueQueryRequest, DictValueQueryResponse } from '@/api/system/dict-value';
  import { Message, Modal } from '@arco-design/web-vue';

  const { loading, setLoading } = useLoading();
  const cardColLayout = useColLayout();
  const pagination = usePagination();
  const list = ref<DictValueQueryResponse[]>([]);
  const emits = defineEmits(['openAdd', 'openUpdate']);

  const formRef = ref();
  const formModel = reactive<DictValueQueryRequest>({
    searchValue: undefined,
    id: undefined,
    keyId: undefined,
    keyName: undefined,
    name: undefined,
    value: undefined,
    label: undefined,
    extra: undefined,
    sort: undefined,
  });

  // 条件数量
  const filterCount = computed(() => {
    return objectTruthKeyCount(formModel, ['searchValue']);
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
          await deleteDictValue(id);
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
  const doFetchCardData = async (request: DictValueQueryRequest) => {
    try {
      setLoading(true);
      const { data } = await getDictValuePage(request);
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
