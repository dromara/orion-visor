<template>
  <card-list v-model:searchValue="formModel.searchValue"
             search-input-placeholder="输入 id / 名称 / 编码 / 地址"
             create-card-position="head"
             :loading="loading"
             :fieldConfig="fieldConfig"
             :list="list"
             :pagination="pagination"
             :card-layout-cols="cardColLayout"
             :filter-count="filterCount"
             :add-permission="['asset:host:create']"
             @add="emits('openAdd')"
             @reset="reset"
             @search="fetchCardData"
             @page-change="fetchCardData">
    <!-- 左侧操作 -->
    <template #leftHandle>
      <!-- 主机分组 -->
      <a-button v-permission="['asset:host-group:update']"
                class="card-header-icon-wrapper"
                @click="emits('openHostGroup')">
        主机分组
        <template #icon>
          <icon-layers />
        </template>
      </a-button>
      <!-- 角色授权 -->
      <a-button v-permission="['asset:host-group:grant']"
                class="card-header-icon-wrapper"
                @click="$router.push({ name: GrantRouteName, query: { key: GrantKey.HOST_GROUP_ROLE }})">
        角色授权
        <template #icon>
          <icon-user-group />
        </template>
      </a-button>
      <!-- 用户授权 -->
      <a-button v-permission="['asset:host-group:grant']"
                class="card-header-icon-wrapper"
                @click="$router.push({ name: GrantRouteName, query: { key: GrantKey.HOST_GROUP_USER }})">
        用户授权
        <template #icon>
          <icon-user />
        </template>
      </a-button>
    </template>
    <!-- 过滤条件 -->
    <template #filterContent>
      <a-form :model="formModel"
              class="card-filter-form"
              size="small"
              ref="formRef"
              label-align="right"
              :style="{ width: '320px' }"
              :label-col-props="{ span: 6 }"
              :wrapper-col-props="{ span: 18 }"
              @keyup.enter="() => fetchCardData()">
        <!-- id -->
        <a-form-item field="id" label="主机id">
          <a-input-number v-model="formModel.id"
                          placeholder="请输入主机id"
                          allow-clear
                          hide-button />
        </a-form-item>
        <!-- 主机名称 -->
        <a-form-item field="name" label="主机名称">
          <a-input v-model="formModel.name" placeholder="请输入主机名称" allow-clear />
        </a-form-item>
        <!-- 主机编码 -->
        <a-form-item field="code" label="主机编码">
          <a-input v-model="formModel.code" placeholder="请输入主机编码" allow-clear />
        </a-form-item>
        <!-- 主机地址 -->
        <a-form-item field="address" label="主机地址">
          <a-input v-model="formModel.address" placeholder="请输入主机地址" allow-clear />
        </a-form-item>
        <!-- 主机标签 -->
        <a-form-item field="tags" label="主机标签">
          <tag-multi-selector v-model="formModel.tags"
                              :allowCreate="false"
                              :limit="0"
                              type="HOST"
                              tag-type="hostTags"
                              placeholder="请选择主机标签" />
        </a-form-item>
      </a-form>
    </template>
    <!-- 标题 -->
    <template #title="{ record }">
      {{ record.name }}
    </template>
    <!-- 编码 -->
    <template #code="{ record }">
      <a-tag>{{ record.code }}</a-tag>
    </template>
    <!-- 地址 -->
    <template #address="{ record }">
      <a-tooltip content="点击复制">
        <span class="pointer span-blue" @click="copy(record.address)">
          <icon-copy /> {{ record.address }}
        </span>
      </a-tooltip>
    </template>
    <!-- 标签 -->
    <template #tags="{ record }">
      <a-space v-if="record.tags" wrap style="margin-bottom: -8px;">
        <a-tag v-for="tag in record.tags"
               :key="tag.id"
               :color="dataColor(tag.name, tagColor)">
          {{ tag.name }}
        </a-tag>
      </a-space>
    </template>
    <!-- 拓展操作 -->
    <template #extra="{ record }">
      <a-space>
        <!-- 更多操作 -->
        <a-dropdown trigger="hover">
          <icon-more class="card-extra-icon" />
          <template #content>
            <!-- 修改 -->
            <a-doption v-permission="['asset:host:update']"
                       @click="emits('openUpdate', record)">
              <icon-edit />
              修改
            </a-doption>
            <!-- 配置 -->
            <a-doption v-permission="['asset:host:update-config']"
                       @click="emits('openUpdateConfig', record)">
              <icon-settings />
              配置
            </a-doption>
            <!-- 删除 -->
            <a-doption v-permission="['asset:host:delete']"
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
      <a-doption v-permission="['asset:host:update']"
                 @click="emits('openUpdate', record)">
        <icon-edit />
        修改
      </a-doption>
      <!-- 配置 -->
      <a-doption v-permission="['asset:host:update-config']"
                 @click="emits('openUpdateConfig', record)">
        <icon-settings />
        配置
      </a-doption>
      <!-- 删除 -->
      <a-doption v-permission="['asset:host:delete']"
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
    name: 'asset-host-card-list'
  };
</script>

<script lang="ts" setup>
  import type { HostQueryRequest, HostQueryResponse } from '@/api/asset/host';
  import { usePagination, useColLayout } from '@/types/card';
  import { computed, reactive, ref, onMounted } from 'vue';
  import useLoading from '@/hooks/loading';
  import { dataColor, objectTruthKeyCount, resetObject } from '@/utils';
  import fieldConfig from '../types/host.card.fields';
  import { deleteHost, getHostPage } from '@/api/asset/host';
  import { Message, Modal } from '@arco-design/web-vue';
  import { tagColor } from '../types/const';
  import TagMultiSelector from '@/components/meta/tag/tag-multi-selector.vue';
  import useCopy from '@/hooks/copy';
  import { GrantKey, GrantRouteName } from '@/views/asset/grant/types/const';

  const emits = defineEmits(['openAdd', 'openUpdate', 'openUpdateConfig', 'openHostGroup']);

  const list = ref<HostQueryResponse[]>([]);

  const cardColLayout = useColLayout();
  const pagination = usePagination();
  const { copy } = useCopy();
  const { loading, setLoading } = useLoading();

  const formRef = ref();
  const formModel = reactive<HostQueryRequest>({
    searchValue: undefined,
    id: undefined,
    name: undefined,
    code: undefined,
    address: undefined,
    favorite: undefined,
    tags: undefined,
    queryTag: true
  });

  // 条件数量
  const filterCount = computed(() => {
    return objectTruthKeyCount(formModel, ['searchValue', 'queryTag']);
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
          await deleteHost(id);
          Message.success('删除成功');
          // 重新加载数据
          fetchCardData();
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
    resetObject(formModel, ['extra']);
    fetchCardData();
  };

  // 加载数据
  const doFetchCardData = async (request: HostQueryRequest) => {
    try {
      setLoading(true);
      const { data } = await getHostPage(request);
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
