<template>
  <a-dropdown class="terminal-context-menu"
              :popup-max-height="false"
              trigger="contextMenu"
              position="bl"
              alignPoint>
    <!-- 路径 -->
    <div class="path-item-wrapper"
         :class="[expand ? 'path-item-wrapper-expand' : '']"
         @click="clickItem">
      <div class="path-item">
        <div class="path-item-title">
          <!-- 名称 -->
          <span class="path-item-title-name">
            {{ item.name }}
          </span>
          <!-- 操作 -->
          <div class="path-item-title-actions">
            <a-space>
              <!-- 进入 -->
              <a-tag class="pointer usn"
                     size="small"
                     :checkable="true"
                     :checked="true"
                     @click.stop="changePath">
                <template #icon>
                  <icon-link />
                </template>
                进入
              </a-tag>
              <!-- 复制 -->
              <a-tag class="pointer usn"
                     size="small"
                     :checkable="true"
                     :checked="true"
                     @click.stop.prevent="emits('copy', item.path)">
                <template #icon>
                  <icon-copy />
                </template>
                复制
              </a-tag>
            </a-space>
          </div>
        </div>
        <!-- 路径 -->
        <span class="path-item-value" @click="clickPath">
          {{ item.path }}
        </span>
      </div>
    </div>
    <!-- 右键菜单 -->
    <template #content>
      <!-- 进入 -->
      <a-doption @click="changePath">
        <div class="terminal-context-menu-icon">
          <icon-link />
        </div>
        <div>进入</div>
      </a-doption>
      <!-- 复制 -->
      <a-doption @click="emits('copy', item.path)">
        <div class="terminal-context-menu-icon">
          <icon-copy />
        </div>
        <div>复制</div>
      </a-doption>
      <!-- 粘贴 -->
      <a-doption @click="emits('paste', item.path)">
        <div class="terminal-context-menu-icon">
          <icon-paste />
        </div>
        <div>粘贴</div>
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
    name: 'pathBookmarkItem'
  };
</script>

<script lang="ts" setup>
  import type { PathBookmarkQueryResponse } from '@/api/terminal/path-bookmark';
  import { ref } from 'vue';
  import { useTerminalStore } from '@/store';
  import { useDebounceFn } from '@vueuse/core';
  import { getParentPath } from '@/utils/file';
  import { TerminalSessionTypes, PathBookmarkType } from '../../types/const';

  const props = defineProps<{
    item: PathBookmarkQueryResponse;
  }>();

  const emits = defineEmits(['remove', 'update', 'copy', 'paste', 'exec', 'change']);

  const { getCurrentSessionType, checkTerminalPanelActive } = useTerminalStore();

  const expand = ref(false);

  let clickCount = 0;

  // 点击路径
  const clickItem = () => {
    if (++clickCount == 2) {
      clickCount = 0;
      // 双击
      changePath();
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

  // 点击路径
  const clickPath = (e: Event) => {
    if (expand.value) {
      // 获取选中的文本
      const selectedText = window.getSelection()?.toString();
      if (selectedText) {
        e.stopPropagation();
      }
    }
  };

  // 切换目录
  const changePath = () => {
    // 检查是否为终端页面
    if (!checkTerminalPanelActive()) {
      return;
    }
    // 获取当前终端类型
    const type = getCurrentSessionType();
    if (type === TerminalSessionTypes.SSH.type) {
      // SSH cd
      const path = props.item.type === PathBookmarkType.DIR
        ? props.item.path
        : getParentPath(props.item.path);
      emits('exec', `cd ${path}`);
    } else if (type === TerminalSessionTypes.SFTP.type) {
      // SFTP 切换目录
      const path = props.item.type === PathBookmarkType.DIR
        ? props.item.path
        : getParentPath(props.item.path);
      emits('change', path);
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

  .path-item-wrapper {
    padding: @item-wrapper-p-y 0;
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;

    &-expand {

      .path-item {
        width: @item-width-transform !important;
        background: var(--color-fill-3) !important;

        .path-item-value {
          color: var(--color-text-1);
          text-overflow: unset;
          word-break: break-all;
          white-space: pre-wrap;
          user-select: unset;
        }
      }
    }

    .path-item {
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

        .path-item-title {
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

      &-value {
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
