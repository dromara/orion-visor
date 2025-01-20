import { JSEncrypt } from 'jsencrypt';
import { useCacheStore } from '@/store';
import { Message } from '@arco-design/web-vue';

// 加密
export const encrypt = async (data: string | undefined): Promise<string | undefined> => {
  // 为空直接返回
  if (!data) {
    return data;
  }
  // 获取公钥
  const publicKey = (await useCacheStore().loadSystemSetting()).encrypt_publicKey;
  const encryptor = new JSEncrypt();
  encryptor.setPublicKey(publicKey);
  // 加密
  const value = encryptor.encrypt(data);
  if (value === false) {
    Message.error('数据加密失败');
    throw new Error('数据加密失败');
  }
  return value;
};
