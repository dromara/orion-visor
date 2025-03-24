// 表名称
export const TableName = 'sftp-log';

// sftp 操作类型
export const SftpOperatorType = {
  SFTP_MOVE: 'terminal:sftp-move',
  SFTP_CHMOD: 'terminal:sftp-chmod',
};

// 最大展示数量
export const showPathMaxCount = 5;

// sftp 操作类型 字典项
export const sftpOperatorTypeKey = 'sftpOperatorType';

// sftp 操作结果 字典项
export const sftpOperatorResultKey = 'operatorLogResult';

// 加载的字典值
export const dictKeys = [sftpOperatorTypeKey, sftpOperatorResultKey];
