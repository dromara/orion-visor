import type { TemplateParam } from '@/components/view/exec-editor/const';

// cron 下次执行次数
export const CronNextTimes = 5;

// 计划任务状态
export const ExecJobStatus = {
  // 禁用
  DISABLED: 0,
  // 启用
  ENABLED: 1,
};

// 任务内置参数
export const jobBuiltinParams: Array<TemplateParam> = [
  {
    name: 'sourceId',
    desc: '计划任务id'
  }, {
    name: 'seq',
    desc: '执行序列'
  },
];

// 计划任务状态 字典项
export const execJobStatusKey = 'execJobStatus';

// 执行状态 字典项
export const execStatusKey = 'execStatus';

// 加载的字典值
export const dictKeys = [execJobStatusKey, execStatusKey];
