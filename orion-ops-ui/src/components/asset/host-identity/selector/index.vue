<template>
  <a-select v-model:model-value="value"
            :options="optionData"
            :loading="loading"
            placeholder="请选择主机身份"
            allow-clear />
</template>

<script lang="ts">
  export default {
    name: 'hostIdentitySelector'
  };
</script>

<script lang="ts" setup>
  import type { SelectOptionData } from '@arco-design/web-vue';
  import { computed, onBeforeMount, ref } from 'vue';
  import { useCacheStore } from '@/store';
  import useLoading from '@/hooks/loading';

  const props = withDefaults(defineProps<Partial<{
    modelValue: number;
    authorized: boolean;
  }>>(), {
    authorized: false
  });

  const emits = defineEmits(['update:modelValue']);

  const { loading, setLoading } = useLoading();
  const cacheStore = useCacheStore();

  const value = computed<number>({
    get() {
      return props.modelValue as number;
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
  onBeforeMount(async () => {
    setLoading(true);
    try {
      const hostIdentities = props.authorized
        ? await cacheStore.loadAuthorizedHostIdentities()
        : await cacheStore.loadHostIdentities();
      optionData.value = hostIdentities.map(s => {
        return {
          label: `${s.name} (${s.username})`,
          value: s.id,
        };
      });
    } catch (e) {
    } finally {
      setLoading(false);
    }
  });

</script>

<style lang="less" scoped>

</style>
