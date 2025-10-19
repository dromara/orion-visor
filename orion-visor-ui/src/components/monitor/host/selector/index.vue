<template>
  <a-select v-model:model-value="modelValue"
            :options="optionData"
            :loading="loading"
            :multiple="multiple"
            placeholder="请选择监控项"
            allow-clear />
</template>

<script lang="ts">
  export default {
    name: 'monitorHostSelector'
  };
</script>

<script lang="ts" setup>
  import type { SelectOptionData } from '@arco-design/web-vue';
  import type { HostType } from '@/api/asset/host';
  import { onActivated, onMounted, ref } from 'vue';
  import { useCacheStore } from '@/store';
  import useLoading from '@/hooks/loading';

  const props = withDefaults(defineProps<Partial<{
    type: HostType;
    status: string | undefined;
    multiple: boolean;
  }>>(), {
    type: undefined,
    status: undefined,
    multiple: false,
  });

  const modelValue = defineModel<string | Array<string>>();

  const { loading, setLoading } = useLoading();
  const cacheStore = useCacheStore();
  const optionData = ref<Array<SelectOptionData>>([]);

  // 初始化选项
  const initOptions = async () => {
   setLoading(true);
    try {
      const hosts = await cacheStore.loadHosts(props.type);
      optionData.value = hosts.filter(s => !props.status || s.status === props.status)
        .map(s => {
          return {
            label: `${s.name} - ${s.address}`,
            value: s.agentKey,
          };
        });
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 初始化选项
  onMounted(initOptions);
  onActivated(initOptions);

</script>

<style lang="less" scoped>

</style>
