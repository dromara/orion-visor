import { defineStore } from 'pinia';
import { MenuState } from './types';
import { MenuQueryResponse } from '@/api/system/menu';
import { TreeNodeData } from '@arco-design/web-vue';

const useMenuStore = defineStore('menu', {
  state: (): MenuState => ({
    menus: []
  }),

  getters: {
    menuState(state: MenuState): MenuState {
      return { ...state };
    },
    treeData(state: MenuState): TreeNodeData[] {
      let render = (arr: any[]): TreeNodeData[] => {
        return arr.map((s) => {
          // 非 function
          if (s.type === 3) {
            return null as unknown as TreeNodeData;
          }
          // 当前节点
          const node = {
            key: s.id,
            title: s.name,
            children: undefined as unknown
          } as TreeNodeData;
          // 子节点
          if (s.children && s.children.length) {
            node.children = render(s.children);
          }
          return node;
        }).filter(Boolean);
      };
      return [{
        key: 0,
        title: '根目录',
        children: render([...state.menus])
      }];
    }
  },

  actions: {
    /**
     * 更新菜单
     */
    updateMenu(menus: MenuQueryResponse[]) {
      this.menus = menus;
    },

    /**
     * 获取父菜单
     */
    findParentMenu(arr: any, id: number): any {
      if (!arr || !arr.length) {
        return null;
      }
      // 当前级
      for (let e of arr) {
        if (e.id === id) {
          return arr;
        }
      }
      // 子级
      for (let e of arr) {
        if (e.children && e.children.length) {
          if (this.findParentMenu(e.children, id)) {
            return e.children;
          }
        }
      }
      return null;
    },

    /**
     * 清空菜单
     */
    reset() {
      this.menus = [];
    },
  },
});

export default useMenuStore;
