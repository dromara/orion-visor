import { JSEncrypt } from 'jsencrypt';
import { useCacheStore } from '@/store';
import { Message } from '@arco-design/web-vue';

// 分块大小
const BLOCK_SIZE = 100;

// 加密
export const encrypt = async (data: string | undefined): Promise<string | undefined> => {
  // 为空直接返回
  if (!data) {
    return data;
  }
  // 获取公钥
  const publicKey = (await useCacheStore().loadSystemSetting())['encrypt.public-key'];
  const encryptor = new JSEncrypt();
  encryptor.setPublicKey(publicKey);

  try {
    // 分块加密
    const chunks = splitString(data);
    const encryptedChunks = chunks.map(chunk => {
      const encrypted = encryptor.encrypt(chunk);
      if (encrypted === false) {
        throw new Error();
      }
      return encrypted;
    });
    return encryptedChunks.join('|');
  } catch (error) {
    Message.error('数据加密失败');
    throw new Error('数据加密失败');
  }
};

// 分割字符串
const splitString = (str: string): string[] => {
  const chunks: string[] = [];
  for (let i = 0; i < str.length; i += BLOCK_SIZE) {
    chunks.push(str.slice(i, i + BLOCK_SIZE));
  }
  return chunks;
};
