<template>
  <a-modal v-model:visible="visible"
           title-align="start"
           :top="120"
           :width="298"
           :body-style="{ padding: '16px' }"
           :align-center="false"
           :mask-closable="true"
           :unmount-on-close="true"
           :footer="false"
           @close="handleClose">
    <!-- title -->
    <template #title>
      <span class="modal-title">
        未来 <span class="span-blue times">{{ request?.times }}</span> 次执行时间
      </span>
    </template>
    <a-spin class="full container" :loading="loading">
      <!-- cron 表达式 -->
      <div class="cron-value span-blue">
        {{ request?.expression }}
      </div>
      <!-- 执行时间 -->
      <div v-for="time in next"
           :key="time"
           class="next-time">
        {{ time }}
      </div>
    </a-spin>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'cronNextModal'
  };
</script>

<script lang="ts" setup>
  import type { CronNextRequest } from '@/api/meta/expression';
  import { ref } from 'vue';
  import { getCronNextTime } from '@/api/meta/expression';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import { Message } from '@arco-design/web-vue';

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const request = ref<CronNextRequest>();
  const next = ref<Array<string>>([]);

  // 打开
  const open = async (record: CronNextRequest) => {
    request.value = record;
    try {
      // 获取执行时间
      setLoading(true);
      const { data } = await getCronNextTime(request.value);
      if (data.valid) {
        // 表达式正确
        next.value = data.next;
        setVisible(true);
      } else {
        // 表达式错误
        setVisible(false);
        Message.error('cron 表达式错误');
      }
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  defineExpose({ open });

  // 关闭回调
  const handleClose = () => {
    handleClear();
  };

  // 清空
  const handleClear = () => {
    setLoading(false);
    setVisible(false);
  };

</script>

<style lang="less" scoped>
  .modal-title {
    font-size: 14px;
  }

  .container {
    display: flex;
    flex-direction: column;
  }

  .times {
    font-weight: 600;
  }

  .cron-value {
    margin-bottom: 8px;
    font-weight: 600;
  }

  .next-time {
    margin-bottom: 4px;
    color: var(--color-text-2);
  }

</style>
