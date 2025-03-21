<template>
  <card-list v-model:searchValue="formModel.searchValue"
             search-input-placeholder="输入 id / 名称"
             :create-card-position="false"
             :loading="loading"
             :field-config="cardFieldConfig"
             :list="list"
             :pagination="pagination"
             :card-layout-cols="cardColLayout"
             :handle-visible="{disableFilter: true}"
             :add-permission="['asset:host-key:create']"
             :query-order="queryOrder"
             :fields-hook="fieldsHook"
             @add="emits('openAdd')"
             @reset="reset"
             @search="fetchCardData"
             @page-change="fetchCardData">
    <!-- 左侧操作 -->
    <template #leftHandle>
      <!-- 角色授权 -->
      <a-button v-permission="['asset:host-key:grant']"
                class="card-header-button"
                @click="router.push({ name: GrantRouteName, query: { key: GrantKey.HOST_KEY_ROLE }})">
        角色授权
        <template #icon>
          <icon-user-group />
        </template>
      </a-button>
      <!-- 用户授权 -->
      <a-button v-permission="['asset:host-key:grant']"
                class="card-header-button"
                @click="router.push({ name: GrantRouteName, query: { key: GrantKey.HOST_KEY_USER }})">
        用户授权
        <template #icon>
          <icon-user />
        </template>
      </a-button>
    </template>
    <!-- 标题 -->
    <template #title="{ record }">
      {{ record.name }}
    </template>
    <!-- 描述 -->
    <template #description="{ record }">
      <span :title="record.description">
        {{ record.description || '-' }}
      </span>
    </template>
    <!-- 拓展操作 -->
    <template #extra="{ record }">
      <a-space>
        <!-- 更多操作 -->
        <a-dropdown trigger="hover" :popup-max-height="false">
          <icon-more class="card-extra-icon" />
          <template #content>
            <!-- 详情 -->
            <a-doption v-permission="['asset:host-key:detail', 'asset:host-key:update']"
                       @click="emits('openView', record)">
              <span class="more-doption normal">详情</span>
            </a-doption>
            <!-- 修改 -->
            <a-doption v-permission="['asset:host-key:update']"
                       @click="emits('openUpdate', record)">
              <span class="more-doption normal">修改</span>
            </a-doption>
            <!-- 删除 -->
            <a-doption v-permission="['asset:host-key:delete']"
                       class="span-red"
                       @click="deleteRow(record.id)">
              <span class="more-doption error">删除</span>
            </a-doption>
          </template>
        </a-dropdown>
      </a-space>
    </template>
  </card-list>
</template>

<script lang="ts">
  export default {
    name: 'hostKeyCardList'
  };
</script>

<script lang="ts" setup>
  import type { HostKeyQueryRequest, HostKeyQueryResponse } from '@/api/asset/host-key';
  import { useCardPagination, useCardColLayout, useCardFieldConfig } from '@/hooks/card';
  import { reactive, ref, onMounted } from 'vue';
  import { useCacheStore } from '@/store';
  import { useRouter } from 'vue-router';
  import useLoading from '@/hooks/loading';
  import { resetObject } from '@/utils';
  import fieldConfig from '../types/card.fields';
  import { deleteHostKey, getHostKeyPage } from '@/api/asset/host-key';
  import { Message, Modal } from '@arco-design/web-vue';
  import { GrantKey, GrantRouteName } from '@/views/asset/grant/types/const';
  import { TableName } from '../types/const';
  import { useQueryOrder, ASC } from '@/hooks/query-order';

  const emits = defineEmits(['openAdd', 'openUpdate', 'openView']);

  const router = useRouter();
  const cacheStore = useCacheStore();
  const cardColLayout = useCardColLayout();
  const pagination = useCardPagination();
  const queryOrder = useQueryOrder(TableName, ASC);
  const { cardFieldConfig, fieldsHook } = useCardFieldConfig(TableName, fieldConfig);
  const { loading, setLoading } = useLoading();

  const list = ref<HostKeyQueryResponse[]>([]);
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
          // 重新加载
          reload();
        } catch (e) {
        } finally {
          setLoading(false);
        }
      }
    });
  };

  // 重新加载
  const reload = () => {
    // 重新加载数据
    fetchCardData();
    // 清空缓存
    cacheStore.reset('hostKeys', 'authorizedHostKeys');
  };

  defineExpose({ reload });

  // 重置条件
  const reset = () => {
    resetObject(formModel);
    fetchCardData();
  };

  // 加载数据
  const doFetchCardData = async (request: HostKeyQueryRequest) => {
    try {
      setLoading(true);
      const { data } = await getHostKeyPage(queryOrder.markOrderly(request));
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

  onMounted(() => {
    fetchCardData();
  });

</script>

<style lang="less" scoped>

</style>
