<template>
  <card-list ref="cardList"
             v-model:searchValue="formModel.name"
             create-card-position="head"
             :card-height="180"
             :list="list"
             :pagination="pagination"
             :card-layout-cols="cardColLayout"
             @add="add"
             @search="search"
             @reset="reset">
    <!-- 标题 -->
    <template #title="{ record }">
      {{ record.name }}
    </template>
    <!-- 标题拓展 -->
    <template #extra="{ index }">
      {{ index }}
    </template>
    <!-- 卡片 -->
    <template #card="{ record }">
      {{ record }}
    </template>
  </card-list>
  {{ formModel }}
</template>

<script lang="ts">
  export default {
    name: 'host-card-list'
  };
</script>

<script setup lang="ts">
  import { useCardPagination, useCardColLayout } from '@/types/table';
  import { reactive, ref } from 'vue';

  const formModel = reactive({
    name: undefined
  });

  const cardColLayout = useCardColLayout();
  const pagination = useCardPagination();
  const list = ref<Array<any>>([]);

  for (let i = 0; i < 5; i++) {
    list.value.push({
      id: i + 1,
      name: `名称 ${i + 1}`,
      host: `192.168.1.${i}`,
      disabled: i === 0
    });
  }
  pagination.total = 270;
  const add = () => {
    console.log('add');
  };
  const search = () => {
    console.log('search');
  };
  const reset = () => {
    console.log('reset');
  };

</script>

<style scoped lang="less">

</style>
