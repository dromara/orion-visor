<template>
  <div class="snippet-item-wrapper"
       :class="[!!item.expand ? 'snippet-item-wrapper-expand' : '']">
    <div class="snippet-item">
      <span class="snippet-item-title">
        {{ item.name }}
      </span>
      <span class="snippet-item-command">
        {{ item.command }}
      </span>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'snippetItem'
  };
</script>

<script lang="ts" setup>
  import type { CommandSnippetQueryResponse } from '@/api/asset/command-snippet';

  defineProps<{
    item: CommandSnippetQueryResponse
  }>();

</script>

<style lang="less" scoped>
  @transform-x: 8px;
  @drawer-width: 388px;
  @item-wrapper-p-y: 4px;
  @item-wrapper-p-x: 12px;
  @item-p: 8px;
  @item-width: @drawer-width - @item-wrapper-p-x * 2;
  @item-width-transform: @item-width + @transform-x;
  @item-inline-width: @item-width - @item-p * 2;

  .snippet-item-wrapper {
    padding: @item-wrapper-p-y 0;
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;

    &-expand {

      .snippet-item {
        width: @item-width-transform !important;
        background: var(--color-fill-3) !important;

        .snippet-item-command {
          color: var(--color-text-1);
          text-overflow: unset;
          word-break: break-all;
          white-space: unset;
        }
      }
    }

    .snippet-item {
      display: flex;
      flex-direction: column;
      padding: @item-p;
      background: var(--color-fill-2);
      border-radius: 4px;
      cursor: pointer;
      transition: all 0.2s;
      width: @item-width;

      &:hover {
        width: @item-width-transform;
        background: var(--color-fill-3);
      }

      &-title {
        color: var(--color-text-1);
        margin-bottom: 8px;
        overflow: hidden;
        text-overflow: ellipsis;
        width: @item-inline-width;
      }

      &-command {
        color: var(--color-text-2);
        font-size: 12px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: pre;
        width: @item-inline-width;
      }
    }
  }
</style>
