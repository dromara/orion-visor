// eslint-disable-next-line @typescript-eslint/no-unused-vars
import type { TreeNodeData } from '@arco-design/web-vue';
import type { NodeData } from './global';

declare module '@arco-design/web-vue' {
  interface TreeNodeData extends NodeData {
  }
}
