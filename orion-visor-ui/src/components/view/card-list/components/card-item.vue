<template>
  <!-- 卡片 -->
  <a-card :class="[
            'card-list-item',
            item.disabled === true ? 'card-list-item-disabled' : undefined,
            !!cardClass ? cardClass : undefined
          ]"
          :style="{ height: cardHeight }"
          :bordered="false"
          :hoverable="true"
          :body-style="cardBodyStyle as Record<string, any>"
          @click="bubblesEmitter(CardEmitter.CLICK, item, index)"
          @dblclick="bubblesEmitter(CardEmitter.DBL_CLICK, item, index)">
    <!-- 标题 -->
    <template #title>
      <slot name="title" />
    </template>
    <!-- 拓展部分 -->
    <template #extra>
      <slot name="extra" />
    </template>
    <!-- 内容-字段 -->
    <template v-if="fieldConfig && fieldConfig.fields">
      <div :class="['fields-container', fieldConfig.bodyClass]">
        <template v-for="(field, index) in fieldConfig.fields">
          <a-row :align="fieldConfig.rowAlign || field.rowAlign || 'center'"
                 :style="{
                   'margin-bottom': index !== fieldConfig.fields.length - 1 ? (fieldConfig.rowGap || '12px') : false,
                   'height': fieldConfig.height || field.height || 'unset',
                   'min-height': fieldConfig.minHeight || field.minHeight || 'unset'
                 }">
            <!-- label -->
            <a-col :span="fieldConfig.labelSpan || 8" :offset="fieldConfig.labelOffset || 0"
                   :style="{ 'text-align': fieldConfig.labelAlign || 'left' }"
                   :class="[
                     fieldConfig.labelClass,
                     field.labelClass,
                     'field-label'
                   ]">
              <span>{{ field.label + (fieldConfig.showColon ? ' :' : '') }}</span>
            </a-col>
            <!-- value -->
            <a-col :span="24 - (fieldConfig.labelSpan || 8) + (fieldConfig.labelOffset || 0)"
                   :style="{ 'text-align': fieldConfig.valueAlign || 'left' }"
                   :class="[
                     fieldConfig.valueClass,
                     field.valueClass,
                     'field-value',
                     field.ellipsis ? 'field-value-ellipsis' : ''
                   ]">
              <slot :name="field.slotName"
                    :record="item"
                    :index="index"
                    :rowKey="item[rowKey as string]">
                <a-tooltip v-if="field.tooltip" :content="item[field.dataIndex]">
                  <span v-if="field.render" v-html="field.render({ record: item, index })" />
                  <span v-else>{{ item[field.dataIndex] }}</span>
                </a-tooltip>
                <template v-else>
                  <span v-if="field.render" v-html="field.render({ record: item, index })" />
                  <span v-else>{{ item[field.dataIndex] }}</span>
                </template>
              </slot>
            </a-col>
          </a-row>
        </template>
      </div>
    </template>
    <!-- 内容-自定义槽 -->
    <template v-else>
      <slot name="card" />
    </template>
    <!-- 卡片底部 -->
    <slot name="cardFooter" />
  </a-card>
</template>

<script lang="ts">
  export default {
    name: 'cardItem'
  };
</script>

<script lang="ts" setup>
  import type { CardRecord } from '@/types/card';
  import type { CardProps } from '../types/props';
  import { CardEmitter } from '../types/emits';
  import useEmitter from '@/hooks/emitter';

  const props = defineProps<CardProps & {
    index: number;
    item: CardRecord;
  }>();
  const emits = defineEmits(['emitter']);

  const { bubblesEmitter } = useEmitter(emits);

</script>

<style lang="less" scoped>
  .fields-container {

    .field-label {
      color: var(--color-text-3);
      word-break: break-all;
      white-space: pre-wrap;
      padding-right: 8px;
      font-size: 12px;
      font-weight: 500;
      user-select: none;
    }

    .field-value {
      color: var(--gray-8);
      word-break: break-all;
      white-space: pre-wrap;
      padding-right: 8px;
      font-size: 13px;
      font-weight: 400;
    }

    .field-value-ellipsis {
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
      word-break: keep-all;
    }
  }

</style>
