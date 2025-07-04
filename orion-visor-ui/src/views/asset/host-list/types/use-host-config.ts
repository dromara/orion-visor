import type { Ref } from 'vue';
import { ref } from 'vue';
import type { HostBaseConfig } from '@/api/asset/host-config';
import { getHostConfig, updateHostConfig } from '@/api/asset/host-config';
import type { FieldRule } from '@arco-design/web-vue';
import { Message } from '@arco-design/web-vue';
import { baseFormRules } from './form.rules';
import { encrypt } from '@/utils/rsa';
import { testHostConnect } from '@/api/asset/host';

// 主机配置表单信息
export interface UseHostConfigFormOptions<T extends HostBaseConfig> {
  type: string;
  hostId: number;
  formModel: Ref<T>;
  rules?: Record<string, FieldRule | FieldRule[]>;
  setLoading: (loading: boolean) => void;
}

// 使用主机配置表单
export default function useHostConfigForm<T extends HostBaseConfig>(options: UseHostConfigFormOptions<T>) {
  const { type, hostId, formModel, rules, setLoading } = options;

  const formRef = ref();
  const formRules = ref({ ...rules, username: baseFormRules.username(formModel), password: baseFormRules.password(formModel) });

  // 加载配置
  const fetchHostConfig = async () => {
    try {
      setLoading(true);
      const { data } = await getHostConfig<T>({ hostId, type });
      data.useNewPassword = !data.hasPassword;
      formModel.value = data;
    } catch (err: any) {
      Message.error('配置加载失败');
    } finally {
      setLoading(false);
    }
  };

  // 测试连接
  const testConnect = async () => {
    if (!formRef?.value) {
      return;
    }
    const error = await formRef.value.validate();
    if (error) {
      return;
    }
    try {
      setLoading(true);
      // 测试连接
      await testHostConnect({ id: hostId, type });
      Message.success('连接成功');
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 保存配置
  const saveConfig = async () => {
    if (!formRef?.value) {
      return;
    }
    const error = await formRef.value.validate();
    if (error) {
      return;
    }
    // 加密密码
    const data = { ...formModel.value };
    try {
      data.password = await encrypt(data.password);
    } catch (e) {
      return;
    }
    try {
      setLoading(true);
      // 更新
      await updateHostConfig({
        hostId,
        type,
        config: JSON.stringify(data),
      });
      Message.success('修改成功');
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  return {
    formRef,
    formRules,
    fetchHostConfig,
    testConnect,
    saveConfig,
  };
}
