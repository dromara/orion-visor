<template>
  <a-select v-model:model-value="value"
            :placeholder="placeholder"
            :options="optionData"
            :loading="loading"
            :limit="limit as number"
            :allow-create="allowCreate"
            @exceed-limit="() => { emits('onLimited', limit, `最多选择${limit}个tag`) }"
            multiple
            allow-clear>
    <!-- 选项 -->
    <template #option="{ data: { label } }">
      <a-tag :color="dataColor(label, tagColor)">
        {{ label }}
      </a-tag>
    </template>
  </a-select>
</template>

<script lang="ts">
  export default {
    name: 'tagMultiSelector'
  };
</script>

<script lang="ts" setup>
  import type { SelectOptionData } from '@arco-design/web-vue';
  import type { TagCreateRequest } from '@/api/meta/tag';
  import { ref, computed, onMounted, onActivated } from 'vue';
  import { useCacheStore } from '@/store';
  import { dataColor } from '@/utils';
  import { createTag } from '@/api/meta/tag';
  import useLoading from '@/hooks/loading';

  const props = withDefaults(defineProps<Partial<{
    modelValue: Array<number>;
    placeholder: string;
    limit: number;
    type: string;
    allowCreate: boolean;
    tagColor: Array<string>;
  }>>(), {
    allowCreate: false,
    tagColor: () => [],
  });

  const emits = defineEmits(['update:modelValue', 'onLimited']);

  const { loading, setLoading } = useLoading();
  const cacheStore = useCacheStore();

  const value = computed<Array<number>>({
    get() {
      return props.modelValue as Array<number>;
    },
    async set(e) {
      await checkCreateTag(e as Array<any>);
      emits('update:modelValue', e);
    }
  });
  const optionData = ref<SelectOptionData[]>([]);

  // 检查是否可以创建tag
  const checkCreateTag = async (tags: Array<any>) => {
    if (!tags.length) {
      return;
    }
    for (let i = 0; i < tags.length; i++) {
      const tag = tags[i];
      // 为 number 代表为 id 已存在
      if (typeof tag === 'number') {
        continue;
      }
      // 已存在则跳过
      const find = optionData.value.find((o) => o.label === tag);
      if (find) {
        // 删除并且跳出循环
        tags.splice(i, 1);
        return;
      }
      // 不存在则创建 tag
      setLoading(true);
      try {
        // 创建 tag
        tags[i] = await doCreateTag(tag);
      } catch (e) {
        // 失败删除
        tags.splice(i, 1);
      } finally {
        setLoading(false);
      }
    }
  };

  // 创建 tag
  const doCreateTag = async (name: string) => {
    const { data: id } = await createTag({
      name,
      type: props.type
    } as unknown as TagCreateRequest);
    // 插入缓存
    const tagCache = await cacheStore.loadTags(props.type as string);
    tagCache && tagCache.push({ id, name });
    // 插入 options
    optionData.value.push({
      label: name,
      value: id,
      tagProps: {
        color: dataColor(name, props.tagColor)
      }
    });
    return id;
  };

  // 初始化选项
  const initOptions = async () => {
    setLoading(true);
    try {
      const tags = await cacheStore.loadTags(props.type as string);
      optionData.value = tags.map(s => {
        return {
          label: s.name,
          value: s.id,
          tagProps: {
            color: dataColor(s.name, props.tagColor)
          }
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
