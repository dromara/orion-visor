<template>
  <a-select v-model:model-value="value"
            :placeholder="placeholder"
            :options="optionData"
            :limit="limit as number"
            @exceed-limit="onLimited"
            multiple
            :allow-create="allowCreate"
            allow-clear>
  </a-select>
</template>

<script lang="ts">
  export default {
    name: 'tag-multi-selector'
  };
</script>

<script lang="ts" setup>
  import { ref, computed, PropType } from 'vue';
  import { useCacheStore } from '@/store';
  import { Message, SelectOptionData } from '@arco-design/web-vue';
  import { createTag, TagCreateRequest } from '@/api/meta/tag';

  const props = defineProps({
    modelValue: Array as PropType<Array<number>>,
    placeholder: String,
    limit: Number,
    type: String,
    allowCreate: Boolean
  });

  const emits = defineEmits(['update:modelValue']);

  const value = computed<Array<number>>({
    get() {
      return props.modelValue as Array<number>;
    },
    async set(e) {
      await checkCreateTag(e as Array<any>);
      emits('update:modelValue', e);
    }
  });

  // 选项数据
  const cacheStore = useCacheStore();
  const optionData = ref<SelectOptionData[]>([]);
  const initOptionData = () => {
    optionData.value = cacheStore.tags.map(s => {
      return {
        label: s.name,
        value: s.id,
      };
    }) as SelectOptionData[];
  };
  initOptionData();

  defineExpose({ initOptionData });

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
      try {
        // 创建 tag
        tags[i] = await doCreateTag(tag);
      } catch (e) {
        // 失败删除
        tags.splice(i, 1);
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
    cacheStore.tags.push({ id, name });
    // 插入 options
    optionData.value.push({
      label: name,
      value: id
    });
    return id;
  };

  // 超出限制
  const onLimited = () => {
    Message.warning(`最多选择${ props.limit }个tag`);
  };

</script>

<style lang="less" scoped>

</style>
