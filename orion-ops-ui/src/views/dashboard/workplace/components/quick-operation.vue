<template>
  <a-card class="general-card"
          title="快捷操作"
          :header-style="{ paddingBottom: '0' }"
          :body-style="{ padding: '20px 20px 0 20px' }">
    <a-row :gutter="8">
      <a-col v-for="link in links"
             :key="link.meta.locale as string"
             :span="8"
             class="wrapper"
             @click="openRoute($event, link)">
        <div class="icon">
          <component v-if="link.meta.icon" :is="link.meta.icon" />
        </div>
        <a-typography-paragraph class="text">
          {{ link.meta.locale }}
        </a-typography-paragraph>
      </a-col>
    </a-row>
  </a-card>
</template>

<script lang="ts" setup>
  import type { RouteRecordNormalized } from 'vue-router';
  import { useRouter } from 'vue-router';
  import { useMenuStore } from '@/store';
  import { openNewRoute } from '@/router';

  const router = useRouter();
  const { appMenus } = useMenuStore();

  const links = appMenus.map(s => s.children)
    .filter(s => s?.length)
    .flat(1)
    .filter(s => s.meta)
    .map(s => s as RouteRecordNormalized)
    .slice(0, 12);

  // 打开路由
  const openRoute = (e: any, route: RouteRecordNormalized) => {
    // 新页面打开
    if (route.meta.newWindow || e.ctrlKey) {
      openNewRoute({
        name: route.name as string,
      });
      return;
    }
    // 触发跳转
    router.push({
      name: route.name as string,
    });
  };

</script>

<style lang="less" scoped>
</style>
