<template>
  <a-select v-model:model-value="value"
            :options="optionData"
            :loading="loading"
            placeholder="请选择主机身份"
            allow-clear>
    <!-- label -->
    <template #label="{ data }">
      <span class="option-wrapper">
        <span class="label">{{ data.label }}</span>
        <span class="username">{{ data.username }}</span>
      </span>
    </template>
    <!-- 选项 -->
    <template #option="{ data }">
      <span class="option-wrapper">
        <span class="label">{{ data.label }}</span>
        <span class="username">{{ data.username }}</span>
      </span>
    </template>
  </a-select>
</template>

<script lang="ts">
  export default {
    name: 'hostIdentitySelector'
  };
</script>

<script lang="ts" setup>
  import type { SelectOptionData } from '@arco-design/web-vue';
  import { computed, onActivated, onMounted, ref } from 'vue';
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
  const initOptions = async () => {
    setLoading(true);
    try {
      const hostIdentities = props.authorized
        ? await cacheStore.loadAuthorizedHostIdentities()
        : await cacheStore.loadHostIdentities();
      optionData.value = hostIdentities.map(s => {
        return {
          label: s.name,
          value: s.id,
          username: s.username,
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
  .option-wrapper {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .label {
      margin-right: 8px;
    }

    .username {
      font-size: 12px;
      color: var(--color-text-3);
    }
  }
</style>
