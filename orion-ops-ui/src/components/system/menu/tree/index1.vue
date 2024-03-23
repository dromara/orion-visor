<!--<script lang="tsx">-->
<!--  import type { RouteMeta, RouteRecordRaw } from 'vue-router';-->
<!--  import { useRoute, useRouter } from 'vue-router';-->
<!--  import { compile, computed, defineComponent, h, ref } from 'vue';-->
<!--  import { useAppStore } from '@/store';-->
<!--  import { listenerRouteChange } from '@/utils/route-listener';-->
<!--  import { openWindow, regexUrl } from '@/utils';-->
<!--  import { openNewRoute } from '@/router';-->
<!--  import useMenuTree from './use-menu-tree';-->

<!--  export default defineComponent({-->
<!--    name: 'menuTree',-->
<!--    emit: ['collapse'],-->
<!--    setup() {-->
<!--      const appStore = useAppStore();-->
<!--      const router = useRouter();-->
<!--      const route = useRoute();-->
<!--      const { menuTree } = useMenuTree();-->
<!--      const collapsed = computed({-->
<!--        get() {-->
<!--          if (appStore.device === 'desktop') return appStore.menuCollapse;-->
<!--          return false;-->
<!--        },-->
<!--        set(value: boolean) {-->
<!--          appStore.updateSettings({ menuCollapse: value });-->
<!--        },-->
<!--      });-->

<!--      const topMenu = computed(() => appStore.topMenu);-->
<!--      const openKeys = ref<string[]>([]);-->
<!--      const selectedKey = ref<string[]>([]);-->

<!--      // 跳转路由-->
<!--      const goto = (e: any, item: RouteRecordRaw) => {-->
<!--        // 打开外链-->
<!--        if (regexUrl.test(item.path)) {-->
<!--          openWindow(item.path);-->
<!--          return;-->
<!--        }-->
<!--        const { hideInMenu, activeMenu, newWindow } = item.meta as RouteMeta;-->
<!--        // 新页面打开-->
<!--        if (newWindow || e.ctrlKey) {-->
<!--          openNewRoute({-->
<!--            name: item.name,-->
<!--          });-->
<!--          return;-->
<!--        }-->
<!--        // 设置 selectedKey-->
<!--        if (route.name === item.name && !hideInMenu && !activeMenu) {-->
<!--          selectedKey.value = [item.name as string];-->
<!--          return;-->
<!--        }-->
<!--        // 触发跳转-->
<!--        router.push({-->
<!--          name: item.name,-->
<!--        });-->
<!--      };-->

<!--      const findMenuOpenKeys = (target: string) => {-->
<!--        const result: string[] = [];-->
<!--        let isFind = false;-->
<!--        const backtrack = (item: RouteRecordRaw, keys: string[]) => {-->
<!--          if (item.name === target) {-->
<!--            isFind = true;-->
<!--            result.push(...keys);-->
<!--            return;-->
<!--          }-->
<!--          if (item.children?.length) {-->
<!--            item.children.forEach((el) => {-->
<!--              backtrack(el, [...keys, el.name as string]);-->
<!--            });-->
<!--          }-->
<!--        };-->
<!--        menuTree.value.forEach((el: RouteRecordRaw) => {-->
<!--          if (isFind) return;-->
<!--          backtrack(el, [el.name as string]);-->
<!--        });-->
<!--        return result;-->
<!--      };-->

<!--      // 监听路由 设置打开的 key-->
<!--      listenerRouteChange((newRoute) => {-->
<!--        const { activeMenu, hideInMenu } = newRoute.meta;-->
<!--        if (!hideInMenu || activeMenu) {-->
<!--          const menuOpenKeys = findMenuOpenKeys(-->
<!--            (activeMenu || newRoute.name) as string-->
<!--          );-->

<!--          const keySet = new Set([...menuOpenKeys, ...openKeys.value]);-->
<!--          openKeys.value = [...keySet];-->

<!--          selectedKey.value = [-->
<!--            activeMenu || menuOpenKeys[menuOpenKeys.length - 1],-->
<!--          ];-->
<!--        }-->
<!--      }, true);-->

<!--      // 展开菜单-->
<!--      const setCollapse = (val: boolean) => {-->
<!--        if (appStore.device === 'desktop')-->
<!--          appStore.updateSettings({ menuCollapse: val });-->
<!--      };-->

<!--      // 渲染菜单-->
<!--      const renderSubMenu = () => {-->
<!--        function travel(_route: RouteRecordRaw[], nodes = []) {-->
<!--          if (_route) {-->
<!--            _route.forEach((element) => {-->
<!--              // This is demo, modify nodes as needed-->
<!--              const icon = element?.meta?.icon-->
<!--                ? () => h(compile(`<${element?.meta?.icon}/>`))-->
<!--                : null;-->
<!--              const node =-->
<!--                element?.children && element?.children.length !== 0 ? (-->
<!--                  <a-sub-menu-->
<!--                    key={element?.name}-->
<!--                    v-slots={{-->
<!--                      icon,-->
<!--                      // 去除国际化 title: () => h(compile(t(element?.meta?.locale || ''))),-->
<!--                      title: () => h(compile(element?.meta?.locale || '')),-->
<!--                    }}-->
<!--                  >-->
<!--                    {travel(element?.children)}-->
<!--                  </a-sub-menu>-->
<!--                ) : (-->
<!--                  <a-menu-item-->
<!--                    key={element?.name}-->
<!--                    v-slots={{ icon }}-->
<!--                    onClick={($event: any) => goto($event, element)}-->
<!--                  >-->
<!--                    {element?.meta?.locale || ''}-->
<!--                  </a-menu-item>-->
<!--                );-->
<!--              nodes.push(node as never);-->
<!--            });-->
<!--          }-->
<!--          return nodes;-->
<!--        }-->

<!--        return travel(menuTree.value);-->
<!--      };-->

<!--      return () => (-->
<!--        <a-menu-->
<!--          mode={topMenu.value ? 'horizontal' : 'vertical'}-->
<!--          v-model:collapsed={collapsed.value}-->
<!--          v-model:open-keys={openKeys.value}-->
<!--          show-collapse-button={appStore.device !== 'mobile'}-->
<!--          auto-open={false}-->
<!--          selected-keys={selectedKey.value}-->
<!--          auto-open-selected={true}-->
<!--          level-indent={34}-->
<!--          style="height: 100%; width:100%;"-->
<!--          onCollapse={setCollapse}-->
<!--        >-->
<!--          {renderSubMenu()}-->
<!--        </a-menu>-->
<!--      );-->
<!--    },-->
<!--  });-->
<!--</script>-->

<!--<style lang="less" scoped>-->
<!--  :deep(.arco-menu-inner) {-->
<!--    .arco-menu-inline-header {-->
<!--      display: flex;-->
<!--      align-items: center;-->
<!--    }-->

<!--    .arco-icon {-->
<!--      &:not(.arco-icon-down) {-->
<!--        font-size: 18px;-->
<!--      }-->
<!--    }-->

<!--    .arco-menu-icon {-->
<!--      margin-right: 10px !important;-->
<!--    }-->

<!--    .arco-menu-indent-list {-->
<!--      width: 28px;-->
<!--      display: inline-block;-->
<!--    }-->

<!--    .arco-menu-title {-->
<!--      user-select: none;-->
<!--    }-->
<!--  }-->
<!--</style>-->
