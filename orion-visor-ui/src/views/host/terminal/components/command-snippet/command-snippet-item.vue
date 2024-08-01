<template>
  <a-dropdown class="terminal-context-menu"
              :popup-max-height="false"
              trigger="contextMenu"
              position="bl"
              alignPoint>
    <!-- 命令 -->
    <div class="snippet-item-wrapper"
         :class="[expand ? 'snippet-item-wrapper-expand' : '']"
         @click="clickItem">
      <div class="snippet-item">
        <div class="snippet-item-title">
          <!-- 名称 -->
          <span class="snippet-item-title-name">
            {{ item.name }}
          </span>
          <!-- 操作 -->
          <div class="snippet-item-title-actions">
            <a-space>
              <!-- 粘贴 -->
              <a-tag class="pointer usn"
                     size="small"
                     :checkable="true"
                     :checked="true"
                     @click.stop.prevent="emits('paste', item.command)">
                <template #icon>
                  <icon-paste />
                </template>
                粘贴
              </a-tag>
              <!-- 执行 -->
              <a-tag class="pointer usn"
                     size="small"
                     :checkable="true"
                     :checked="true"
                     @click.stop="emits('exec', item.command)">
                <template #icon>
                  <icon-thunderbolt />
                </template>
                执行
              </a-tag>
            </a-space>
          </div>
        </div>
        <!-- 命令 -->
        <span class="snippet-item-command"
              @click="clickCommand">
          {{ item.command }}
        </span>
      </div>
    </div>
    <!-- 右键菜单 -->
    <template #content>
      <!-- 复制 -->
      <a-doption @click="emits('copy', item.command)">
        <div class="terminal-context-menu-icon">
          <icon-copy />
        </div>
        <div>复制</div>
      </a-doption>
      <!-- 粘贴 -->
      <a-doption @click="emits('paste', item.command)">
        <div class="terminal-context-menu-icon">
          <icon-paste />
        </div>
        <div>粘贴</div>
      </a-doption>
      <!-- 执行 -->
      <a-doption @click="emits('exec', item.command)">
        <div class="terminal-context-menu-icon">
          <icon-thunderbolt />
        </div>
        <div>执行</div>
      </a-doption>
      <!-- 修改 -->
      <a-doption @click="emits('update', item)">
        <div class="terminal-context-menu-icon">
          <icon-edit />
        </div>
        <div>修改</div>
      </a-doption>
      <!-- 删除 -->
      <a-doption @click="emits('remove', item.id)">
        <div class="terminal-context-menu-icon">
          <icon-delete />
        </div>
        <div>删除</div>
      </a-doption>
      <!-- 展开 -->
      <a-doption v-if="!expand"
                 @click="() => expand = true">
        <div class="terminal-context-menu-icon">
          <icon-expand />
        </div>
        <div>展开</div>
      </a-doption>
      <!-- 收起 -->
      <a-doption v-else
                 @click="() => expand = false">
        <div class="terminal-context-menu-icon">
          <icon-shrink />
        </div>
        <div>收起</div>
      </a-doption>
    </template>
  </a-dropdown>
</template>

<script lang="ts">
  export default {
    name: 'commandSnippetItem'
  };
</script>

<script lang="ts" setup>
  import type { CommandSnippetQueryResponse } from '@/api/asset/command-snippet';
  import { ref } from 'vue';
  import { useDebounceFn } from '@vueuse/core';

  const props = defineProps<{
    item: CommandSnippetQueryResponse;
  }>();

  const emits = defineEmits(['remove', 'update', 'copy', 'exec', 'paste']);

  const expand = ref(false);

  let clickCount = 0;

  // 点击命令
  const clickItem = () => {
    if (++clickCount == 2) {
      // 双击执行
      clickCount = 0;
      emits('exec', props.item.command);
    } else {
      // 单击展开
      expandItem();
    }
  };

  // 展开
  const expandItem = useDebounceFn(() => {
    setTimeout(() => {
      // 为 0 则代表为双击
      if (clickCount !== 0) {
        expand.value = !expand.value;
        clickCount = 0;
      }
    }, 50);
  });

  // 点击命令
  const clickCommand = (e: Event) => {
    if (expand.value) {
      // 获取选中的文本
      const selectedText = window.getSelection()?.toString();
      if (selectedText) {
        e.stopPropagation();
      }
    }
  };

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
  @item-actions-width: 124px;

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
          white-space: pre-wrap;
          user-select: unset;
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

        .snippet-item-title {
          &-name {
            width: calc(@item-inline-width - @item-actions-width);
          }

          &-actions {
            width: @item-actions-width;
            display: inline-block;
          }
        }
      }

      &-title {
        color: var(--color-text-1);
        margin-bottom: 8px;
        width: @item-inline-width;
        height: 24px;
        display: flex;
        align-items: center;
        user-select: none;

        &-name {
          width: @item-inline-width;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: pre;
        }

        &-actions {
          display: none;
        }
      }

      &-command {
        color: var(--color-text-2);
        font-size: 12px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        width: @item-inline-width;
        user-select: none;
      }
    }
  }

</style>
