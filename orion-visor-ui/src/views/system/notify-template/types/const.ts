export const TableName = 'notify_template';

// 通知模板表单
export interface NotifyTemplateConfig {
  webhook: string;
  secret: string;
  title: string;
  messageClassify: string;
  messageType: string;
}

// 通知业务类型
export const BizType = {
  ALARM: 'ALARM',
};

// 通知渠道类型
export const ChannelType = {
  WEBSITE: 'WEBSITE',
  DING: 'DING',
  FEI_SHU: 'FEI_SHU',
  WE_COM: 'WE_COM',
};

// 通知类型
export const NotifyType = {
  WEBHOOK: 'webhook',
  WEBSITE: 'website',
};

// 通知业务类型 字典项
export const BizTypeKey = 'notifyBizType';

// 通知渠道类型 字典项
export const ChannelTypeKey = 'notifyChannelType';

// 消息分类 字典项
export const messageClassifyKey = 'messageClassify';

// 消息类型 字典项
export const messageTypeKey = 'messageType';

// 加载的字典值
export const dictKeys = [BizTypeKey, ChannelTypeKey, messageClassifyKey, messageTypeKey];
