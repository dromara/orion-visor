<template>
  <card-list v-model:searchValue="formModel.searchValue"
             search-input-placeholder="输入 id / 名称 / 用户名"
             create-card-position="head"
             :loading="loading"
             :fieldConfig="fieldConfig"
             :list="list"
             :pagination="pagination"
             :card-layout-cols="cardColLayout"
             :filter-count="filterCount"
             :add-permission="['asset:host-identity:create']"
             @add="emits('openAdd')"
             @reset="reset"
             @search="fetchCardData"
             @page-change="fetchCardData">
    <!-- 标题 -->
    <template #title="{ record }">
      {{ record.name }}
    </template>
    <!-- 用户名 -->
    <template #username="{ record }">
      <a-tooltip content="点击复制">
          <span class="pointer span-blue" @click="copy(record.username)">
            <icon-copy /> {{ record.username }}
          </span>
      </a-tooltip>
    </template>
    <!-- 秘钥名称 -->
    <template #keyId="{ record }">
      <template v-if="record.keyId">
        <!-- 可查看详情 -->
        <a-tooltip v-if="hasAnyPermission(['asset:host-key:detail', 'asset:host-key:update'])"
                   content="点击查看详情">
          <a-tag :checked="true"
                 checkable
                 @click="emits('openKeyView',{id: record.keyId})">
            {{ record.keyName }}
          </a-tag>
        </a-tooltip>
        <!-- 不可查看详情 -->
        <a-tag v-else>
          {{ record.keyName }}
        </a-tag>
      </template>
    </template>
    <!-- 拓展操作 -->
    <template #extra="{ record }">
      <a-space>
        <!-- 更多操作 -->
        <a-dropdown trigger="hover">
          <icon-more class="card-extra-icon" />
          <template #content>
            <!-- 修改 -->
            <a-doption v-permission="['asset:host-identity:update']"
                       @click="emits('openUpdate', record)">
              <icon-edit />
              修改
            </a-doption>
            <!-- 删除 -->
            <a-doption v-permission="['asset:host-identity:delete']"
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
      <a-doption v-permission="['asset:host-identity:update']"
                 @click="emits('openUpdate', record)">
        <icon-edit />
        修改
      </a-doption>
      <!-- 删除 -->
      <a-doption v-permission="['asset:host-identity:delete']"
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
              :wrapper-col-props="{ span: 18 }"
              @keyup.enter="() => fetchCardData()">
        <!-- id -->
        <a-form-item field="id" label="id">
          <a-input-number v-model="formModel.id"
                          placeholder="请输入id"
                          allow-clear
                          hide-button />
        </a-form-item>
        <!-- 名称 -->
        <a-form-item field="name" label="名称">
          <a-input v-model="formModel.name" placeholder="请输入名称" allow-clear />
        </a-form-item>
        <!-- 用户名 -->
        <a-form-item field="username" label="用户名">
          <a-input v-model="formModel.username" placeholder="请输入用户名" allow-clear />
        </a-form-item>
        <!-- 主机秘钥 -->
        <a-form-item field="keyId" label="主机秘钥">
          <host-key-selector v-model="formModel.keyId" allow-clear />
        </a-form-item>
      </a-form>
    </template>
  </card-list>
</template>

<script lang="ts">
  export default {
    name: 'asset-host-identity-card-list'
  };
</script>

<script lang="ts" setup>
  import type { HostIdentityQueryRequest, HostIdentityQueryResponse } from '@/api/asset/host-identity';
  import { usePagination, useColLayout } from '@/types/card';
  import { computed, reactive, ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import { objectTruthKeyCount, resetObject } from '@/utils';
  import fieldConfig from '../types/card.fields';
  import { deleteHostIdentity, getHostIdentityPage } from '@/api/asset/host-identity';
  import { Message, Modal } from '@arco-design/web-vue';
  import usePermission from '@/hooks/permission';
  import useCopy from '@/hooks/copy';
  import HostKeySelector from '@/components/asset/host-key/host-key-selector.vue';

  const { copy } = useCopy();
  const { hasAnyPermission } = usePermission();
  const { loading, setLoading } = useLoading();

  const cardColLayout = useColLayout();
  const pagination = usePagination();
  const list = ref<HostIdentityQueryResponse[]>([]);
  const emits = defineEmits(['openAdd', 'openUpdate', 'openKeyView']);

  const formRef = ref();
  const formModel = reactive<HostIdentityQueryRequest>({
    searchValue: undefined,
    id: undefined,
    name: undefined,
    username: undefined,
    keyId: undefined,
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
          await deleteHostIdentity(id);
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
  const doFetchCardData = async (request: HostIdentityQueryRequest) => {
    try {
      setLoading(true);
      const { data } = await getHostIdentityPage(request);
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

<style lang="less" scoped>

</style>
