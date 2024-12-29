<template>
  <a-select v-model:model-value="value"
            :options="optionData"
            :loading="loading"
            :multiple="multiple"
            placeholder="请选择主机"
            allow-clear />
</template>

<script lang="ts">
  export default {
    name: 'hostSelector'
  };
</script>

<script lang="ts" setup>
  import type { SelectOptionData } from '@arco-design/web-vue';
  import type { HostType } from '@/api/asset/host';
  import { computed, onActivated, onMounted, ref } from 'vue';
  import { useCacheStore } from '@/store';
  import useLoading from '@/hooks/loading';

  const props = withDefaults(defineProps<Partial<{
    type: HostType;
    status: string | undefined;
    modelValue: number | Array<number>;
    multiple: boolean;
  }>>(), {
    type: undefined,
    status: undefined,
    multiple: false,
  });

  const emits = defineEmits(['update:modelValue']);

  const { loading, setLoading } = useLoading();
  const cacheStore = useCacheStore();

  const value = computed({
    get() {
      return props.modelValue;
    },
    set(e) {
      if (e) {
        emits('update:modelValue', e);
      } else {
        emits('update:modelValue', null);
      }
    }
  });
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
