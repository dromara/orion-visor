import { FieldRule } from '@arco-design/web-vue';

export const name = [{
  required: true,

}] as FieldRule[];


export default {
  name
} as Record<string, FieldRule | FieldRule[]>;
