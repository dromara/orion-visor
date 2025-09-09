import type { MonitorHostQueryResponse, } from '@/api/monitor/monitor-host';
import { getMonitorHostMetrics, updateMonitorHostAlarmSwitch } from '@/api/monitor/monitor-host';
import type { HostAgentLogResponse } from '@/api/asset/host-agent';
import { getAgentInstallLogStatus, getHostAgentStatus, installHostAgent, updateAgentInstallStatus } from '@/api/asset/host-agent';
import type { Ref } from 'vue';
import { onActivated, onDeactivated, onMounted, onUnmounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useDictStore } from '@/store';
import { Message, Modal } from '@arco-design/web-vue';
import { AgentInstallStatus, HostOsType } from '@/views/asset/host-list/types/const';
import { AgentLogStatus, AlarmSwitchKey } from '@/views/monitor/monitor-host/types/const';

// 监控主机列表配置
export interface UseMonitorHostListOptions {
  // 主机信息
  hosts: Ref<Array<MonitorHostQueryResponse>>;
  // 设置加载中
  setLoading: (loading: boolean) => void;
  // 重新加载
  reload: () => void;
}

// 使用监控主机列表
export default function useMonitorHostList(options: UseMonitorHostListOptions) {
  const autoRefresh = ref(true);
  const autoRefreshId = ref();
  const fetchInstallStatusId = ref();
  const lastRefreshTime = ref(0);

  const router = useRouter();
  const { toggleDict } = useDictStore();
  const { hosts, setLoading, reload } = options;

  // 打开详情
  const openDetail = (hostId: number, name: string) => {
    router.push({ name: 'monitorDetail', query: { hostId, name } });
  };

  // 安装探针
  const installAgent = async (hostIdList: Array<number>) => {
    try {
      setLoading(true);
      // 获取全部数据
      const installHosts = hosts.value.filter(s => hostIdList.includes(s.hostId));
      let hasWindows = false;
      // 安装前检查
      for (let host of installHosts) {
        // 检查状态
        if (host?.installLog?.status === AgentLogStatus.WAIT || host?.installLog?.status === AgentLogStatus.RUNNING) {
          Message.error('主机' + host.name + '正在安装中, 请勿重复操作');
          return;
        }
        // 检查系统类型
        if (host.osType === HostOsType.WINDOWS.value) {
          hasWindows = true;
        }
      }
      // 二次确认
      Modal.confirm({
        title: '安装提示',
        titleAlign: 'start',
        bodyStyle: { 'white-space': 'pre-wrap' },
        content: `请确保探针已关闭\n请确认文件夹是否有权限${hasWindows ? '\nWindows 系统仅支持探针上传, 请手动进行安装' : ''}`,
        okText: '确定',
        onOk: async () => {
          try {
            // 调用安装
            await installHostAgent({ idList: hostIdList });
            Message.success('开始安装');
            // 重新加载
            reload();
          } catch (e) {
          } finally {
            setLoading(false);
          }
        }
      });
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 手动安装成功
  const setInstallSuccess = (log: HostAgentLogResponse) => {
    Modal.confirm({
      title: '修正状态',
      titleAlign: 'start',
      content: `确定要手动将安装记录修正为完成吗?`,
      okText: '确定',
      onOk: async () => {
        try {
          setLoading(true);
          // 调用修改接口
          await updateAgentInstallStatus({
            id: log.id,
            status: AgentLogStatus.SUCCESS,
            message: '手动修正',
          });
          log.status = AgentLogStatus.SUCCESS;
          Message.success('状态已修正');
        } catch (e) {
        } finally {
          setLoading(false);
        }
      }
    });
  };

  // 更新报警开关
  const toggleAlarmSwitch = async (record: MonitorHostQueryResponse) => {
    const dict = toggleDict(AlarmSwitchKey, record.alarmSwitch);
    Modal.confirm({
      title: `${dict.label}确认`,
      titleAlign: 'start',
      content: `确定要${dict.label}报警功能吗?`,
      okText: '确定',
      onOk: async () => {
        try {
          setLoading(true);
          const newSwitch = dict.value as number;
          // 调用修改接口
          await updateMonitorHostAlarmSwitch({
            id: record.id,
            alarmSwitch: newSwitch,
          });
          record.alarmSwitch = newSwitch;
          Message.success(`已${dict.label}`);
        } catch (e) {
        } finally {
          setLoading(false);
        }
      }
    });
  };

  // 获取探针安装状态
  const pullInstallLogStatus = async () => {
    // 获取安装中的记录
    const runningIdList = hosts.value.filter(s => s.installLog?.status === AgentLogStatus.WAIT || s.installLog?.status === AgentLogStatus.RUNNING)
      .map(s => s.installLog?.id)
      .filter(Boolean);
    if (!runningIdList.length) {
      return;
    }
    // 查询状态
    const { data } = await getAgentInstallLogStatus(runningIdList);
    data.forEach(item => {
      hosts.value.filter(s => s.installLog?.id === item.id).forEach(s => {
        s.installLog.status = item.status;
        s.installLog.message = item.message;
        // 若安装成功则修改探针信息
        if (item.status === AgentLogStatus.SUCCESS && item.agentStatus) {
          s.agentVersion = item.agentStatus.agentVersion;
          s.latestVersion = item.agentStatus.latestVersion;
          s.agentInstallStatus = item.agentStatus.agentInstallStatus;
          s.agentOnlineStatus = item.agentStatus.agentOnlineStatus;
        }
      });
    });
  };

  // 获取探针状态
  const getAgentStatus = async () => {
    // 获取全部 hostId
    const hostIds = hosts.value.map(s => s.hostId);
    if (hostIds.length) {
      try {
        // 查询状态
        const { data } = await getHostAgentStatus(hostIds);
        data.forEach(item => {
          hosts.value.filter(s => s.hostId === item.id).forEach(s => {
            s.agentVersion = item.agentVersion;
            s.latestVersion = item.latestVersion;
            s.agentInstallStatus = item.agentInstallStatus;
            s.agentOnlineStatus = item.agentOnlineStatus;
          });
        });
      } catch (e) {
      }
    }
  };

  // 获取指标信息
  const getHostMetrics = async () => {
    // 获取全部已安装 agentKey
    const agentKeys = hosts.value
      .filter(s => s.agentInstallStatus === AgentInstallStatus.INSTALLED)
      .map(s => s.agentKey)
      .filter(Boolean);
    if (agentKeys.length) {
      try {
        // 查询指标
        const { data } = await getMonitorHostMetrics(agentKeys);
        data.forEach(item => {
          hosts.value.filter(s => s.agentKey === item.agentKey).forEach(s => {
            s.metricsData = item;
          });
        });
      } catch (e) {
      }
    }
  };

  // 刷新指标
  const refreshMetrics = async () => {
    // 加载状态信息
    await getAgentStatus();
    // 加载指标数据
    await getHostMetrics();
    // 设置刷新时间
    lastRefreshTime.value = Date.now();
  };

  // 切换自动刷新
  const toggleAutoRefresh = async () => {
    autoRefresh.value = !autoRefresh.value;
    if (autoRefresh.value) {
      // 开启自动刷新
      await openAutoRefresh();
    } else {
      // 关闭自动刷新
      closeAutoRefresh();
    }
  };

  // 开启自动刷新
  const openAutoRefresh = async () => {
    window.clearInterval(autoRefreshId.value);
    if (!autoRefresh.value) {
      return;
    }
    if (lastRefreshTime.value === 0) {
      // 防止首次就刷新
      lastRefreshTime.value = 1;
    } else if (Date.now() - lastRefreshTime.value > 60000) {
      // 超过刷新的时间
      await refreshMetrics();
    }
    // 设置自动刷新
    autoRefreshId.value = window.setInterval(refreshMetrics, 60000);
  };

  // 关闭自动刷新
  const closeAutoRefresh = () => {
    window.clearInterval(autoRefreshId.value);
    autoRefreshId.value = undefined;
  };

  onMounted(openAutoRefresh);
  onActivated(openAutoRefresh);
  onDeactivated(closeAutoRefresh);
  onUnmounted(closeAutoRefresh);

  onMounted(() => {
    window.clearInterval(fetchInstallStatusId.value);
    fetchInstallStatusId.value = window.setInterval(pullInstallLogStatus, 5000);
  });

  onUnmounted(() => {
    window.clearInterval(fetchInstallStatusId.value);
  });

  return {
    autoRefresh,
    openDetail,
    installAgent,
    setInstallSuccess,
    toggleAlarmSwitch,
    toggleAutoRefresh,
  };

};
