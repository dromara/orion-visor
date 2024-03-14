// 模板参数
export interface TemplateParam {
  name?: string;
  default?: string;
  desc?: string;
}

// 内置参数
export const builtinsParams: Array<TemplateParam> = [
  {
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
    name: 'userId',
    desc: '执行用户id'
  }, {
    name: 'username',
    desc: '执行用户名'
  }, {
    name: 'execId',
    desc: '执行记录id'
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
