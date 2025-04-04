<template>
  <a-select v-model:model-value="value as any"
            :options="optionData"
            :allow-search="true"
            :multiple="multiple"
            :loading="loading"
            :disabled="loading"
            :filter-option="labelFilter"
            placeholder="请选择角色" />
</template>

<script lang="ts">
  export default {
    name: 'userRoleSelector'
  };
</script>

<script lang="ts" setup>
  import type { SelectOptionData } from '@arco-design/web-vue';
  import { computed, onActivated, onMounted, ref } from 'vue';
  import { useCacheStore } from '@/store';
  import { RoleStatus } from '@/views/user/role/types/const';
  import { labelFilter } from '@/types/form';
  import useLoading from '@/hooks/loading';

  const props = withDefaults(defineProps<Partial<{
    modelValue: number | Array<number>;
    multiple: boolean;
  }>>(), {
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
        if (props.multiple) {
          emits('update:modelValue', []);
        } else {
          emits('update:modelValue', null);
        }
      }
    }
  });
  const optionData = ref<Array<SelectOptionData>>([]);

  // 初始化选项
  const initOptions = async () => {
    setLoading(true);
    try {
      const roles = await cacheStore.loadRoles();
      optionData.value = roles.map(s => {
        return {
          label: `${s.name} (${s.code})`,
          disabled: s.status === RoleStatus.DISABLED,
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
