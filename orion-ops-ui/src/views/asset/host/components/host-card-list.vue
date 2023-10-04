<template>
  <card-list ref="cardList"
             v-model:searchValue="formModel.name"
             create-card-position="head"
             :card-height="180"
             :loading="loading"
             :list="list"
             :pagination="pagination"
             :card-layout-cols="cardColLayout"
             @add="add"
             @reset="reset"
             @search="fetchTableData"
             @page-change="fetchTableData">
    <!-- 标题 -->
    <template #title="{ record }">
      {{ record.id }}
    </template>
    <!-- 标题拓展 -->
    <template #extra="{ index }">
      {{ index }}
    </template>
    <!-- 卡片 -->
    <template #card="{ record }">
      <a-descriptions class="card-descriptions"
                      :data="convert(record)"
                      :column="1">
        <template #value="{ data }">
          {{ data.field }}
          <template v-if="data.field === 'id'">
            <a-tag>{{ data.value }}</a-tag>
          </template>
          <template v-else>
            {{ data.value }}
          </template>
        </template>
      </a-descriptions>
    </template>
    <template #leftHandle>
      <span>1</span>
    </template>
  </card-list>
</template>

<script lang="ts">
  export default {
    name: 'host-card-list'
  };
</script>

<script setup lang="ts">
  import { usePagination, useColLayout } from '@/types/card';
  import { reactive, ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import { resetObject } from '@/utils';
  import { convert } from '../types/card.fields';

  const formModel = reactive({
    name: undefined
  });

  const { loading, setLoading } = useLoading();
  const cardColLayout = useColLayout();
  const pagination = usePagination();
  const list = ref<Array<any>>([]);

  // 切换页码
  const fetchTableData = (page = 1, limit = pagination.pageSize, form = formModel) => {
    console.log(page, limit, form);
    setLoading(true);
    setTimeout(() => {
      try {
        const t = 90;
        for (let i = 0; i < t; i++) {
          list.value.push({
            id: i + 1,
            name: `名称 ${i + 1}`,
            address: `192.168.1.${i}`,
            disabled: i === 0
          });
        }
        pagination.total = t;
        pagination.current = page;
        pagination.pageSize = limit;
        setLoading(false);
      } catch (e) {
      } finally {
        setLoading(false);
      }
    }, 300);
  };
  fetchTableData();

  const add = () => {
    console.log('add');
  };

  const reset = () => {
    console.log('reset');
    resetObject(formModel);
    fetchTableData();
  };


</script>

<style scoped lang="less">

</style>
