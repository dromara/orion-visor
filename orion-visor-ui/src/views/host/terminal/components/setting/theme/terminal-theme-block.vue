<template>
  <div class="terminal-setting-block">
    <!-- 顶部 -->
    <div class="terminal-setting-subtitle-wrapper">
      <h3 class="terminal-setting-subtitle">
        主题设置
      </h3>
    </div>
    <!-- 加载中 -->
    <a-skeleton v-if="loading"
                class="skeleton-wrapper"
                :animation="true">
      <a-skeleton-line :rows="6"
                       :line-height="64"
                       :line-spacing="24" />
    </a-skeleton>
    <!-- 内容区域 -->
    <div v-else class="terminal-setting-body terminal-theme-container">
      <!-- 终端主题 -->
      <div class="theme-row"
           v-for="(themeArr, index) in themes"
           :key="index">
        <a-card v-for="(theme, colIndex) in themeArr"
                :key="theme.name"
                class="terminal-theme-card simple-card"
                :class="{
                  'terminal-theme-card-check': theme.name === currentThemeName
                }"
                :title="theme.name"
                :style="{
                  background: theme.schema.background,
                  marginRight: colIndex === 0 ? '16px' : 0
                }"
                :header-style="{
                  color: theme.dark ? 'rgba(255, 255, 255, .8)' : 'rgba(0, 0, 0, .8)',
                  userSelect: 'none'
                }"
                @click="selectTheme(theme)">
          <!-- 样例 -->
          <terminal-example :schema="theme.schema" />
          <!-- 选中按钮 -->
          <icon-check class="theme-check-icon"
                      v-show="theme.name === currentThemeName" />
        </a-card>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'terminalThemeBlock'
  };
</script>

<script lang="ts" setup>
  import type { TerminalTheme } from '@/api/asset/terminal';
  import type { ISshSession } from '../../../types/define';
  import { useTerminalStore } from '@/store';
  import { TerminalPreferenceItem } from '@/store/modules/terminal';
  import { PanelSessionType } from '../../../types/const';
  import { onMounted, ref } from 'vue';
  import { getTerminalThemes } from '@/api/asset/terminal';
  import { getPreference } from '@/api/user/preference';
  import useLoading from '@/hooks/loading';
  import TerminalExample from '../terminal-example.vue';

  const { updateTerminalPreference, sessionManager } = useTerminalStore();
  const { loading, setLoading } = useLoading();

  const currentThemeName = ref();
  const themes = ref<Array<Array<TerminalTheme>>>([]);

  // 选择主题
  const selectTheme = async (theme: TerminalTheme) => {
    // 修改主题色
    document.body.setAttribute('terminal-theme', theme.dark ? 'dark' : 'light');
    // 修改终端主题
    Object.values(sessionManager.sessions)
      .filter(Boolean)
      .filter(s => s.type === PanelSessionType.SSH.type)
      .map(s => s as ISshSession)
      .forEach(s => {
        s.inst.options.theme = theme.schema;
        // 自适应
        s.blur();
      });
    // 同步
    currentThemeName.value = theme.name;
    await updateTerminalPreference(TerminalPreferenceItem.THEME, theme, true);
  };

  // 加载用户主题
  onMounted(async () => {
    try {
      const { data } = await getPreference<Record<string, any>>('TERMINAL', [TerminalPreferenceItem.THEME]);
      currentThemeName.value = data[TerminalPreferenceItem.THEME]?.name;
    } catch (e) {
    }
  });

  // 加载主题列表
  onMounted(async () => {
    setLoading(true);
    try {
      // 加载全部主题
      const { data } = await getTerminalThemes();
      const result = [];
      for (let i = 0; i < data.length; i += 2) {
        const subArray = data.slice(i, i + 2);
        result.push(subArray);
      }
      themes.value = result;
    } catch (e) {
    } finally {
      setLoading(false);
    }
  });

</script>

<style lang="less" scoped>
  @terminal-width: 458px;
  @terminal-height: 138px;

  .terminal-theme-container {
    flex-direction: column;

    .theme-row {
      display: flex;
      margin-bottom: 16px;
    }
  }

  .terminal-theme-card {
    width: @terminal-width;
    height: calc(@terminal-height + 44px);
    border: 2px solid var(--color-border);
    cursor: pointer;

    :deep(.arco-card-header) {
      padding: 4px 16px;
      height: 40px;
      border-bottom: .5px solid var(--color-border-2);

      &-title {
        color: unset;
        font-size: 16px;
        font-weight: 600;
      }
    }

    :deep(.arco-card-body) {
      height: @terminal-height;
      padding: 0;
      display: flex;
      position: relative;

      .theme-check-icon {
        display: flex;
        position: absolute;
        color: #FFF;
        right: 0;
        bottom: 0;
        z-index: 10;
      }
    }

    &-check, &:hover {
      border: 2px solid rgb(var(--blue-6));
    }

    &-check::after {
      content: '';
      position: absolute;
      right: 0;
      bottom: 0;
      width: 0;
      height: 0;
      border-bottom: 28px solid rgb(var(--blue-6));
      border-left: 28px solid transparent;
    }
  }

</style>
