import { DictValueOptionsQueryResponse } from '@/api/system/dict-value';

export interface DictState {
  [key: string]: Array<DictValueOptionsQueryResponse>;
}
