<template>
  <a-dropdown trigger="contextMenu"
              :popup-max-height="false"
              @select="actionSelect">
    <span class="arco-tag arco-tag-size-medium arco-tag-checked"
          :class="{ 'link-activated': itemData?.path === $route.path }"
          @click="goto(itemData as TagProps)">
      <span class="tag-link">
        {{ itemData.title }}
      </span>
      <span class="arco-icon-hover arco-tag-icon-hover arco-icon-hover-size-medium arco-tag-close-btn"
            @click.stop="tagClose(itemData as TagProps, index)">
        <icon-close />
      </span>
    </span>
    <template #content>
      <a-doption :disabled="disabledReload" :value="Option.reload">
        <icon-refresh />
        <span>重新加载</span>
      </a-doption>
      <a-doption
        class="group-line"
        :disabled="disabledCurrent"
        :value="Option.current">
        <icon-close />
        <span>关闭当前标签页</span>
      </a-doption>
      <a-doption :disabled="disabledLeft" :value="Option.left">
        <icon-to-left />
        <span>关闭左侧标签页</span>
      </a-doption>
      <a-doption
        class="group-line"
        :disabled="disabledRight"
        :value="Option.right">
        <icon-to-right />
        <span>关闭右侧标签页</span>
      </a-doption>
      <a-doption :value="Option.others">
        <icon-swap />
        <span>关闭其它标签页</span>
      </a-doption>
      <a-doption :value="Option.all">
        <icon-folder-delete />
        <span>关闭全部标签页</span>
      </a-doption>
    </template>
  </a-dropdown>
</template>

<script lang="ts" setup>
  import type { TagProps } from '@/store/modules/tab-bar/types';
  import type { PropType } from 'vue';
  import { computed } from 'vue';
  import { useRouter, useRoute } from 'vue-router';
  import { useTabBarStore } from '@/store';
  import { DEFAULT_ROUTE_NAME, REDIRECT_ROUTE_NAME } from '@/router/constants';

  enum Option {
    reload = 'reload',
    current = 'current',
    left = 'left',
    right = 'right',
    others = 'others',
    all = 'all',
  }

  const props = defineProps({
    itemData: {
      type: Object as PropType<TagProps>,
      default: () => {
        return {};
      }
    },
    index: {
      type: Number,
      default: 0,
    },
  });

  const router = useRouter();
  const route = useRoute();
  const tabBarStore = useTabBarStore();

  const goto = (tag: TagProps) => {
    router.push({ ...tag });
  };
  const tagList = computed(() => {
    return tabBarStore.getTabList;
  });

  const disabledReload = computed(() => {
    return props.itemData.path !== route.path;
  });

  const disabledCurrent = computed(() => {
    return props.index === 0;
  });

  const disabledLeft = computed(() => {
    return [0, 1].includes(props.index);
  });

  const disabledRight = computed(() => {
    return props.index === tagList.value.length - 1;
  });

  // 关闭 tag
  const tagClose = (tag: TagProps, idx: number) => {
    tabBarStore.deleteTab(idx, tag);
    if (props.itemData.path === route.path) {
      // 获取队列的前一个 tab
      const latest = tagList.value[idx - 1];
      router.push({ name: latest.name });
    }
  };

  // 获取当前路由索引
  const findCurrentRouteIndex = () => {
    return tagList.value.findIndex((el) => el.path === route.path);
  };

  // 选择操作
  const actionSelect = async (value: any) => {
    const { itemData, index } = props;
    const copyTagList = [...tagList.value];
    if (value === Option.current) {
      // 关闭当前
      tagClose(itemData, index);
    } else if (value === Option.left) {
      // 关闭左侧
      const currentRouteIdx = findCurrentRouteIndex();
      copyTagList.splice(1, props.index - 1);

      tabBarStore.freshTabList(copyTagList);
      if (currentRouteIdx < index) {
        await router.push({ name: itemData.name });
      }
    } else if (value === Option.right) {
      // 关闭右侧
      const currentRouteIdx = findCurrentRouteIndex();
      copyTagList.splice(props.index + 1);

      tabBarStore.freshTabList(copyTagList);
      if (currentRouteIdx > index) {
        await router.push({ name: itemData.name });
      }
    } else if (value === Option.others) {
      // 关闭其他
      const filterList = tagList.value.filter((el, idx) => {
        return idx === 0 || idx === props.index;
      });
      tabBarStore.freshTabList(filterList);
      await router.push({ name: itemData.name });
    } else if (value === Option.reload) {
      // 重新加载
      tabBarStore.deleteCache(itemData);
      await router.push({
        name: REDIRECT_ROUTE_NAME,
        params: { path: route.fullPath },
      });
      tabBarStore.addCache(itemData.name);
    } else {
      // 关闭全部
      tabBarStore.resetTabList();
      await router.push({ name: DEFAULT_ROUTE_NAME });
    }
  };
</script>

<style lang="less" scoped>
  .tag-link {
    color: var(--color-text-2);
    text-decoration: none;
  }

  .link-activated {
    color: rgb(var(--link-6));

    .tag-link {
      color: rgb(var(--link-6));
    }

    & + .arco-tag-close-btn {
      color: rgb(var(--link-6));
    }
  }

  :deep(.arco-dropdown-option-content) {
    span {
      margin-left: 10px;
    }
  }

  .arco-dropdown-open {
    .tag-link {
      color: rgb(var(--danger-6));
    }

    .arco-tag-close-btn {
      color: rgb(var(--danger-6));
    }
  }

  .group-line {
    border-bottom: 1px solid var(--color-neutral-3);
  }
</style>
