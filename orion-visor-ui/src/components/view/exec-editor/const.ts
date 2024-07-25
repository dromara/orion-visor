// 触发提示前缀
export const triggerPrefix = '_';

// 触发提示前缀
export const templatePrefix = '@{{ ';

// 触发提示后缀
export const templateSuffix = ' }}';

// 模板参数
export interface TemplateParam {
  name?: string;
  desc?: string;
  value?: any;
}

// 内置参数
export const builtinParams: Array<TemplateParam> = [
  {
    name: 'source',
    desc: '执行来源'
  }, {
    name: 'userId',
    desc: '执行用户id'
  }, {
    name: 'username',
    desc: '执行用户名'
  }, {
    name: 'execId',
    desc: '执行记录id'
  }, {
    name: 'hostId',
    desc: '执行主机id'
  }, {
    name: 'hostName',
    desc: '执行主机名称'
  }, {
    name: 'hostCode',
    desc: '执行主机编码'
  }, {
    name: 'hostAddress',
    desc: '执行主机地址'
  }, {
    name: 'hostPort',
    desc: '主机端口'
  }, {
    name: 'hostUsername',
    desc: '执行主机用户名'
  }, {
    name: 'osType',
    desc: '执行主机系统版本'
  }, {
    name: 'charset',
    desc: 'SSH 编码集'
  }, {
    name: 'scriptExec',
    desc: '是否使用脚本执行'
  }, {
    name: 'scriptPath',
    desc: '脚本文件路径'
  }, {
    name: 'uuid',
    desc: '生成任务维度 uuid'
  }, {
    name: 'uuidShort',
    desc: '生成任务维度 uuid 无 \'-\''
  }, {
    name: 'hostUuid',
    desc: '生成机器维度 uuid'
  }, {
    name: 'hostUuidShort',
    desc: '生成机器维度 uuid 无 \'-\''
  }, {
    name: 'timestampMillis',
    desc: '时间戳毫秒'
  }, {
    name: 'timestamp',
    desc: '时间戳'
  }, {
    name: 'date',
    desc: '执行时间 yyyy-MM-dd'
  }, {
    name: 'datetime',
    desc: '执行时间 yyyy-MM-dd HH:mm:ss'
  },
];
