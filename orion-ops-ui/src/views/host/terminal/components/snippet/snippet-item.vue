<template>
  <div class="snippet-item-wrapper"
       :class="[!!item.expand ? 'snippet-item-wrapper-expand' : '']"
       @click="expandItem">
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
                   @click.stop="paste">
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
                   @click.stop="exec">
              <template #icon>
                <icon-thunderbolt />
              </template>
              执行
            </a-tag>
          </a-space>
        </div>
      </div>
      <!-- 命令 -->
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
  import { useTerminalStore } from '@/store';

  const props = defineProps<{
    item: CommandSnippetQueryResponse
  }>();

  const { getCurrentTerminalSession } = useTerminalStore();

  // TODO 右键菜单 复制 粘贴 删除 执行 修改

  // 展开命令
  const expandItem = () => {
    props.item.expand = !props.item.expand;
  };

  // 粘贴
  const paste = () => {
    write(props.item.command);
  };

  // 执行
  const exec = () => {
    write(props.item.command + '\n');
  };

  // 写入命令
  const write = (command: string) => {
    const handler = getCurrentTerminalSession()?.handler;
    if (handler && handler.enabledStatus('checkAppendMissing')) {
      handler.checkAppendMissing(command);
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
        white-space: pre;
        width: @item-inline-width;
      }
    }
  }
</style>
