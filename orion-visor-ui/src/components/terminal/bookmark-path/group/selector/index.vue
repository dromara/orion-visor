<template>
  <a-select v-model:model-value="value"
            placeholder="请选择路径分组"
            :options="optionData"
            :loading="loading"
            :allow-create="true"
            allow-clear />
</template>

<script lang="ts">
  export default {
    name: 'pathBookmarkGroupSelector'
  };
</script>

<script lang="ts" setup>
  import type { SelectOptionData } from '@arco-design/web-vue';
  import type { PathBookmarkGroupQueryResponse } from '@/api/terminal/path-bookmark-group';
  import { ref, computed, onBeforeMount } from 'vue';
  import { useCacheStore } from '@/store';
  import useLoading from '@/hooks/loading';
  import { createPathBookmarkGroup } from '@/api/terminal/path-bookmark-group';

  const props = defineProps<Partial<{
    modelValue: number;
  }>>();

  const emits = defineEmits(['update:modelValue']);

  const { loading, setLoading } = useLoading();
  const cacheStore = useCacheStore();

  const value = computed<number | string>({
    get() {
      return props.modelValue as number;
    },
    async set(e) {
      const val = await checkCreateGroup(e);
      emits('update:modelValue', val);
    }
  });
  const optionData = ref<SelectOptionData[]>([]);

  // 检查是否可以创建分组
  const checkCreateGroup = async (val: string | number): Promise<number> => {
    // 清空
    if (!val) {
      return undefined as unknown as number;
    }
    // 为 number 代表为 id 已存在
    if (typeof val === 'number') {
      return val;
    }
    // 已存在则跳过
    const find = optionData.value.find((o) => o.label === val);
    if (find) {
      return find.value as number;
    }
    // 不存在则创建
    setLoading(true);
    try {
      return await doCreateGroup(val);
    } catch (e) {
      return undefined as unknown as number;
    } finally {
      setLoading(false);
    }
  };

  // 创建分组
  const doCreateGroup = async (name: string) => {
    const { data: id } = await createPathBookmarkGroup({
      name,
    });
    // 插入缓存
    const groups = await cacheStore.loadPathBookmarkGroups();
    groups && groups.push({ id, name } as PathBookmarkGroupQueryResponse);
    // 插入 options
    optionData.value.push({
      label: name,
      value: id,
    });
    return id;
  };

  // 初始化选项
  onBeforeMount(async () => {
    setLoading(true);
    try {
      const tags = await cacheStore.loadPathBookmarkGroups();
      optionData.value = tags.map(s => {
        return {
          label: s.name,
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
