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
    desc: '主机id'
  }, {
    name: 'hostName',
    desc: '主机名称'
  }, {
    name: 'hostCode',
    desc: '主机编码'
  }, {
    name: 'userId',
    desc: '执行用户id'
  }, {
    name: 'username',
    desc: '执行用户名称'
  }, {
    name: 'execId',
    desc: '执行id'
  }, {
    name: 'uuid',
    desc: 'uuid'
  }, {
    name: 'uuidShort',
    desc: 'uuid 无 \'-\''
  }, {
    name: 'timeMillis',
    desc: '时间戳毫秒'
  }, {
    name: 'timestamp',
    desc: '时间戳'
  }, {
    name: 'date',
    desc: '时间 yyyy-MM-dd'
  }, {
    name: 'datetime',
    desc: '时间 yyyy-MM-dd HH:mm:ss'
  },
];
