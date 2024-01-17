<template>
  <div class="terminal-setting-block">
    <!-- 顶部 -->
    <div class="terminal-setting-subtitle-wrapper">
      <h3 class="terminal-setting-subtitle">
        右键菜单设置
      </h3>
    </div>
    <!-- 提示 -->
    <a-alert class="mb16">修改后会立刻保存, 重新打开终端后生效 (无需刷新页面)</a-alert>
    <!-- 内容区域 -->
    <div class="terminal-setting-body block-body setting-body">
      <!-- 功能项 -->
      <div class="actions-container">
        <div class="vertical-form-label">功能</div>
        <!-- 功能项列表 -->
        <div class="actions-wrapper">
          <a-row :gutter="[8, 8]">
            <a-col :span="12"
                   v-for="(action, index) in ActionBarItems"
                   :key="index">
              <div class="action-item" @click="clickAction(action.item)">
                <!-- 图标 -->
                <div class="action-icon">
                  <component :is="action.icon" />
                </div>
                <!-- 描述 -->
                <div class="action-desc">
                  {{ action.content }}
                </div>
              </div>
            </a-col>
          </a-row>
        </div>
      </div>
      <!-- 菜单预览容器 -->
      <div class="preview-container">
        <div class="vertical-form-label">菜单预览</div>
        <div ref="popupContainer" />
      </div>
      <!-- 预览下拉菜单 -->
      <a-dropdown v-if="popupContainer"
                  :popup-visible="true"
                  :popup-container="popupContainer"
                  :popup-max-height="false">
        <template #content v-if="rightActions.length">
          <a-doption v-for="(action, index) in rightActions"
                     :key="index">
            <div class="preview-action">
              <!-- 图标 -->
              <div class="preview-icon">
                <component :is="action.icon" />
              </div>
              <!-- 文本 -->
              <div>{{ action.content }}</div>
            </div>
            <!-- 关闭按钮 -->
            <div class="close-icon" @click="clickAction(action.item)">
              <icon-close />
            </div>
          </a-doption>
        </template>
        <!-- 空数据 -->
        <template #content v-else>
          <a-doption>
            点击左侧功能添加
          </a-doption>
        </template>
      </a-dropdown>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'terminalRightMenuBlock'
  };
</script>

<script lang="ts" setup>
  import type { ContextMenuItem } from '../../types/terminal.type';
  import { computed, ref, watch } from 'vue';
  import { useTerminalStore } from '@/store';
  import { TerminalPreferenceItem } from '@/store/modules/terminal';
  import { ActionBarItems } from '../../types/terminal.const';

  const { preference, updateTerminalPreference } = useTerminalStore();

  const popupContainer = ref();
  const rightActionItems = ref<Array<string>>([...preference.rightMenuSetting]);

  // // 监听同步
  watch(rightActionItems, (v) => {
    // 同步
    updateTerminalPreference(TerminalPreferenceItem.RIGHT_MENU_SETTING, v, true);
  }, { deep: true });

  // 实际操作项
  const rightActions = computed<Array<ContextMenuItem>>(() => {
    return rightActionItems.value
      .map(s => ActionBarItems.find(i => i.item === s) as ContextMenuItem)
      .filter(Boolean);
  });

  // 添加操作项
  const clickAction = (item: string) => {
    if (rightActionItems.value.includes(item)) {
      // 移除
      rightActionItems.value.splice(rightActionItems.value.indexOf(item), 1);
    } else {
      // 添加
      rightActionItems.value.push(item);
    }
  };

</script>

<style lang="less" scoped>

  .setting-body {
    display: flex;
  }

  .actions-container {
    width: 418px;
    height: auto;

    .actions-wrapper {
      padding-right: 8px;
      margin-right: 32px;
    }

    .action-item {
      display: flex;
      padding: 6px;
      align-items: center;
      cursor: pointer;
      border-radius: 4px;
      background-color: var(--color-fill-2);
      transition: transform 0.3s ease;
      will-change: transform;

      &:hover {
        transform: scale(1.04);
      }
    }

    .action-icon {
      width: 24px;
      height: 24px;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 16px;
      border-radius: 4px;
      margin-right: 8px;
      background-color: var(--color-fill-3);
    }

    .action-desc {
      display: flex;
      align-items: center;
      font-size: 14px;
      user-select: none;
    }
  }

  .preview-container {
    width: 242px;
    height: auto;

    :deep(.arco-dropdown-option-content) {
      width: 100%;
      display: flex;
      justify-content: space-between;
      align-items: center;

      &:hover {
        .close-icon {
          display: flex;
        }
      }
    }

    .preview-action {
      display: flex;
      align-items: center;
      justify-content: space-between;
    }

    .preview-icon {
      font-size: 18px;
      margin-right: 6px;
    }

    .close-icon {
      display: none;
      padding: 3px;
      border-radius: 12px;
      align-items: center;
      justify-content: center;
      transition: .2s;
      font-size: 15px;

      &:hover {
        background-color: var(--color-fill-3);
      }
    }
  }

  :deep(.arco-trigger-popup) {
    position: relative;
  }

</style>
