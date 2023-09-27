<template>
  <div class="card-list-layout">
    <div class="card-list-header">
      <!-- title slot -->
      <!-- plusIcon |  field field field searchFieldInput searchIcon resetIcon  -->
      <!-- margin -->
      <!-- cardList / empty -->
      <!-- margin -->
      <!-- page -->

      <div>
        title
      </div>
      <div class="card-list-header-left">
      </div>
      <div class="card-list-header-right">
        right search/add
      </div>
    </div>
    <div>
      card 区域
      <a-row :gutter="cardLayoutGutter">
        <a-col v-for="item in list"
               :key="item.id"
               class="wrapper"
               v-bind="cardLayoutCols">
          <a-card class="general-card"
                  :bordered="false"
                  :hoverable="true"
                  style="width: 98%; height: 120px;">
            <template #title>
              {{ item.name }}
            </template>
            <template #extra>
              <icon-more />
            </template>
          </a-card>
        </a-col>
      </a-row>
    </div>
    <div>
      page 区域
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'host-card-list'
  };
</script>

<script setup lang="ts">
  import { ref } from 'vue';

  const cardLayoutGutter = [
    { xs: 16, sm: 16, md: 16 },
    { xs: 16, sm: 16, md: 16 }
  ];

  const cardLayoutCols = {
    xs: 24,
    sm: 12,
    md: 8,
    lg: 6,
    xl: 4,
    xxl: 4,
  };

  const list = ref<Array<any>>([]);

  for (let i = 0; i < 20; i++) {
    list.value.push({
      id: i + 1,
      name: `名称 ${i + 1}`,
      host: `192.168.1.${i}`
    });
  }

</script>

<style scoped lang="less">
  .card-list-layout {
    .card-list-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      background: var(--color-bg-4);

      &-left {
        color: red;
      }

      &-right {
        color: blue;
      }
    }

  }

  .general-card {
    transition-property: all;
  }

  .general-card:hover {
    transform: translateY(-4px);
  }
</style>
