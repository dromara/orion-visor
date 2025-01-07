import type { EChartsOption } from 'echarts';
import { computed } from 'vue';
import useThemes from '@/hooks/themes';

// 配置生成
interface OptionsFn {
  (isDark: boolean, themeTextColor: string, themeLineColor: string): EChartsOption;
}

// 亮色文本色
const lightTextColor = '#4E5969';

// 暗色文本色
const darkTextColor = 'rgba(255, 255, 255, 0.7)';

// 亮色线色
const lightLineColor = '#F2F3F5';

// 暗色线色
const darkLineColor = '#2E2E30';

export default function useChartOption(sourceOption: OptionsFn) {
  const { isDark } = useThemes();

  // 配置
  const chartOption = computed<EChartsOption>(() => {
    return sourceOption(isDark.value,
      isDark.value ? darkTextColor : lightTextColor,
      isDark.value ? darkLineColor : lightLineColor);
  });

  return {
    chartOption,
  };
}
