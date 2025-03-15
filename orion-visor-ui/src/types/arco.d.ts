import type { TreeNodeData } from '@arco-design/web-vue';
import type { NodeData } from './global';

declare module '@arco-design/web-vue' {
  export interface TreeNodeData extends TreeNodeData, NodeData {
  }
}
