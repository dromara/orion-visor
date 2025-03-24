<template>
  <a-col :span="4">
    <div class="card full">
      <div class="card-title">
        <p class="card-title-left">快捷操作</p>
      </div>
      <a-row :gutter="[12, 18]" class="pb12">
        <a-col v-for="{ locale, icon, name, newWindow} in links"
               :key="locale as string"
               :span="8"
               :title="locale"
               class="wrapper"
               @click="openRoute($event, name, newWindow)">
          <div class="icon">
            <component v-if="icon" :is="icon" />
          </div>
          <div class="text usn">
            <span>{{ locale }}</span>
          </div>
        </a-col>
      </a-row>
    </div>
  </a-col>
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
    .filter(s => s.meta.hideInMenu !== true)
    .map(s => {
      return {
        name: s.name,
        locale: s.meta.locale,
        icon: s.meta.icon,
        newWindow: s.meta.newWindow,
      };
    })
    .slice(0, 10);
  links.unshift({
    locale: '个人中心', icon: 'icon-user', name: 'userInfo', newWindow: false
  }, {
    locale: '修改密码', icon: 'icon-safe', name: 'updatePassword', newWindow: false
  });

  // 打开路由
  const openRoute = (e: any, name: any, newWindow: any) => {
    // 新页面打开
    if (newWindow || e.ctrlKey) {
      openNewRoute({
        name: name as string,
      });
      return;
    }
    // 触发跳转
    router.push({
      name: name as string,
    });
  };

</script>

<style lang="less" scoped>
  .text {
    padding: 0 4px;

    span {
      height: 14px;
      display: block;
      font-size: 12px;
      text-align: center;
      color: rgb(var(--gray-8));
      word-wrap: break-word;
      overflow: hidden;
    }
  }

  .icon {
    display: inline-block;
    width: 32px;
    height: 32px;
    margin-bottom: 4px;
    color: var(--color-text-2);
    line-height: 32px;
    font-size: 16px;
    text-align: center;
    background: var(--color-fill-2);
    border-radius: 4px;
  }

  .wrapper {
    text-align: center;
    cursor: pointer;

    &:hover {
      .icon {
        color: rgb(var(--arcoblue-6));
        background-color: #E8F3FF;
      }

      .text {
        color: rgb(var(--arcoblue-6));
      }
    }
  }

</style>
