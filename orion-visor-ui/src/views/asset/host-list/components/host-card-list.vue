<template>
  <card-list v-model:searchValue="formModel.searchValue"
             search-input-placeholder="输入 id / 名称 / 编码 / 地址"
             :create-card-position="false"
             :loading="loading"
             :field-config="fieldConfig"
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
                class="card-header-button"
                @click="emits('openHostGroup')">
        主机分组
        <template #icon>
          <icon-layers />
        </template>
      </a-button>
      <!-- 角色授权 -->
      <a-button v-permission="['asset:host-group:grant']"
                class="card-header-button"
                @click="router.push({ name: GrantRouteName, query: { key: GrantKey.HOST_GROUP_ROLE }})">
        角色授权
        <template #icon>
          <icon-user-group />
        </template>
      </a-button>
      <!-- 用户授权 -->
      <a-button v-permission="['asset:host-group:grant']"
                class="card-header-button"
                @click="router.push({ name: GrantRouteName, query: { key: GrantKey.HOST_GROUP_USER }})">
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
              :auto-label-width="true"
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
        <!-- 主机类型 -->
        <a-form-item field="type" label="主机类型">
          <a-select v-model="formModel.type"
                    :options="toOptions(hostTypeKey)"
                    placeholder="请选择主机类型"
                    allow-clear />
        </a-form-item>
        <!-- 系统类型 -->
        <a-form-item field="osType" label="系统类型">
          <a-select v-model="formModel.osType"
                    :options="toOptions(hostOsTypeKey)"
                    placeholder="请选择系统类型"
                    allow-clear />
        </a-form-item>
        <!-- 主机状态 -->
        <a-form-item field="status" label="主机状态">
          <a-select v-model="formModel.status"
                    :options="toOptions(hostStatusKey)"
                    placeholder="请选择主机状态"
                    allow-clear />
        </a-form-item>
        <!-- 主机标签 -->
        <a-form-item field="tags" label="主机标签">
          <tag-multi-selector v-model="formModel.tags"
                              type="HOST"
                              :limit="0"
                              :tag-color="tagColor"
                              placeholder="请选择主机标签" />
        </a-form-item>
        <!-- 描述 -->
        <a-form-item field="description" label="描述">
          <a-input v-model="formModel.description"
                   placeholder="请输入描述"
                   allow-clear />
        </a-form-item>
      </a-form>
    </template>
    <!-- 标题 -->
    <template #title="{ record }">
      <div class="host-title">
        <!-- 系统类型图标 -->
        <component v-if="getHostOsIcon(record.osType)"
                   :is="getHostOsIcon(record.osType)"
                   class="os-icon" />
        <!-- 主机名称 -->
        <span>{{ record.name }}</span>
      </div>
    </template>
    <!-- 编码 -->
    <template #code="{ record }">
      {{ record.code }}
    </template>
    <!-- 描述 -->
    <template #description="{ record }">
      <span :title="record.description">
        {{ record.description || '-' }}
      </span>
    </template>
    <!-- 主机类型 -->
    <template #type="{ record }">
      <a-tag :color="getDictValue(hostTypeKey, record.type, 'color')">
        {{ getDictValue(hostTypeKey, record.type) }}
      </a-tag>
    </template>
    <!-- 地址 -->
    <template #address="{ record }">
       <span class="span-blue text-copy host-address"
             title="复制"
             @click="copy(record.address)">
          {{ record.address }}
        </span>
      <span class="span-blue text-copy"
            title="复制"
            @click="copy(record.port)">
          {{ record.port }}
        </span>
    </template>
    <!-- 主机状态 -->
    <template #status="{ record }">
      <a-tag :color="getDictValue(hostStatusKey, record.status, 'color')">
        {{ getDictValue(hostStatusKey, record.status) }}
      </a-tag>
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
        <a-dropdown trigger="hover" :popup-max-height="false">
          <icon-more class="card-extra-icon" />
          <template #content>
            <!-- 修改 -->
            <a-doption v-permission="['asset:host:update']"
                       @click="emits('openUpdate', record)">
              <span class="more-doption normal">
                <icon-edit /> 修改
              </span>
            </a-doption>
            <!-- 配置 -->
            <a-doption v-permission="['asset:host:update-config']"
                       @click="emits('openUpdateConfig', record)">
              <span class="more-doption normal">
                <icon-settings /> 配置
              </span>
            </a-doption>
            <!-- 修改状态 -->
            <a-doption v-permission="['asset:host:update-status']"
                       @click="updateStatus(record as HostQueryResponse)">
              <span class="more-doption"
                    :class="[toggleDictValue(hostStatusKey, record.status, 'status')]">
                <icon-sync /> {{ toggleDictValue(hostStatusKey, record.status, 'label') }}
              </span>
            </a-doption>
            <!-- 复制 -->
            <a-doption v-permission="['asset:host:create']"
                       @click="emits('openCopy', record)">
              <span class="more-doption normal">
                <icon-copy /> 复制
              </span>
            </a-doption>
            <!-- 删除 -->
            <a-doption v-permission="['asset:host:delete']"
                       class="span-red"
                       @click="deleteRow(record.id)">
              <span class="more-doption error">
                <icon-delete /> 删除
              </span>
            </a-doption>
            <!-- SSH -->
            <a-doption v-if="record.type === HostType.SSH.value"
                       v-permission="['asset:terminal:access']"
                       @click="openNewRoute({ name: 'terminal', query: { connect: record.id, type: 'SSH' } })">
              <span class="more-doption normal">
                <icon-thunderbolt /> SSH
              </span>
            </a-doption>
            <!-- SFTP -->
            <a-doption v-if="record.type === HostType.SSH.value"
                       v-permission="['asset:terminal:access']"
                       @click="openNewRoute({ name: 'terminal', query: { connect: record.id, type: 'SFTP' } })">
              <span class="more-doption normal">
               <icon-folder /> SFTP
              </span>
            </a-doption>
          </template>
        </a-dropdown>
      </a-space>
    </template>
  </card-list>
</template>

<script lang="ts">
  export default {
    name: 'hostCardList'
  };
</script>

<script lang="ts" setup>
  import type { HostQueryRequest, HostQueryResponse } from '@/api/asset/host';
  import { useCardPagination, useCardColLayout } from '@/hooks/card';
  import { computed, reactive, ref, onMounted } from 'vue';
  import { dataColor, objectTruthKeyCount, resetObject } from '@/utils';
  import { deleteHost, getHostPage, updateHostStatus } from '@/api/asset/host';
  import { Message, Modal } from '@arco-design/web-vue';
  import { getHostOsIcon, hostOsTypeKey, hostStatusKey, HostType, hostTypeKey, tagColor } from '../types/const';
  import { copy } from '@/hooks/copy';
  import { useCacheStore, useDictStore } from '@/store';
  import { GrantKey, GrantRouteName } from '@/views/asset/grant/types/const';
  import { useRouter } from 'vue-router';
  import useLoading from '@/hooks/loading';
  import fieldConfig from '../types/card.fields';
  import { openNewRoute } from '@/router';
  import TagMultiSelector from '@/components/meta/tag/multi-selector/index.vue';

  const emits = defineEmits(['openAdd', 'openUpdate', 'openUpdateConfig', 'openHostGroup', 'openCopy']);

  const router = useRouter();
  const cacheStore = useCacheStore();
  const cardColLayout = useCardColLayout();
  const pagination = useCardPagination();
  const { loading, setLoading } = useLoading();
  const { toOptions, getDictValue, toggleDictValue, toggleDict } = useDictStore();

  const list = ref<HostQueryResponse[]>([]);
  const formRef = ref();
  const formModel = reactive<HostQueryRequest>({
    searchValue: undefined,
    id: undefined,
    name: undefined,
    code: undefined,
    address: undefined,
    type: undefined,
    osType: undefined,
    status: undefined,
    tags: undefined,
    description: undefined,
    queryTag: true,
  });

  // 条件数量
  const filterCount = computed(() => {
    return objectTruthKeyCount(formModel, ['searchValue', 'queryTag']);
  });

  // 更新状态
  const updateStatus = async (record: HostQueryResponse) => {
    const dict = toggleDict(hostStatusKey, record.status);
    Modal.confirm({
      title: `${dict.label}确认`,
      titleAlign: 'start',
      content: `确定要${dict.label}该主机吗?`,
      okText: '确定',
      onOk: async () => {
        try {
          setLoading(true);
          const newStatus = dict.value as string;
          // 调用修改接口
          await updateHostStatus({
            id: record.id,
            status: newStatus,
          });
          record.status = newStatus;
          Message.success(`已${dict.label}`);
        } catch (e) {
        } finally {
          setLoading(false);
        }
      }
    });
  };

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
    cacheStore.reset('host_', 'host_SSH');
  };

  defineExpose({ reload });

  // 重置条件
  const reset = () => {
    resetObject(formModel, ['queryTag']);
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
  .host-title {
    display: flex;
    align-items: center;
    width: 100%;

    .os-icon {
      width: 20px;
      height: 20px;
      margin-right: 6px;
    }
  }

  .host-address::after {
    content: ':';
    user-select: text;
  }
</style>
