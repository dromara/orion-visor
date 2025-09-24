<template>
  <a-select v-model:model-value="modelValue"
            :options="optionData"
            :allow-search="true"
            :multiple="multiple"
            :loading="loading"
            :disabled="loading"
            placeholder="请选择通知模板" />
</template>

<script lang="ts">
  export default {
    name: 'notifyTemplateSelector'
  };
</script>

<script lang="ts" setup>
  import type { SelectOptionData } from '@arco-design/web-vue';
  import { onActivated, onMounted, ref } from 'vue';
  import { useCacheStore } from '@/store';
  import useLoading from '@/hooks/loading';

  const props = withDefaults(defineProps<{
    multiple?: boolean;
    bizType: string;
  }>(), {
    multiple: false,
  });

  const modelValue = defineModel({ type: Array<number> });

  const { loading, setLoading } = useLoading();
  const cacheStore = useCacheStore();

  const optionData = ref<Array<SelectOptionData>>([]);

  // 初始化选项
  const initOptions = async () => {
    setLoading(true);
    try {
      const values = await cacheStore.loadNotifyTemplate(props.bizType);
      optionData.value = values.map(s => {
        return {
          label: s.name,
          value: s.id,
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
