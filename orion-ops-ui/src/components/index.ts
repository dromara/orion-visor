import type { App } from 'vue';
import { use } from 'echarts/core';
import AQueryHeader from '@dangojs/a-query-header';
import { CanvasRenderer } from 'echarts/renderers';
import { BarChart, LineChart, PieChart, RadarChart } from 'echarts/charts';
import {
  GridComponent,
  TooltipComponent,
  LegendComponent,
  DataZoomComponent,
  GraphicComponent,
} from 'echarts/components';
import Breadcrumb from './app/breadcrumb/index.vue';
import Chart from './view/chart/index.vue';
import CardList from './view/card-list/index.vue';
import Editor from './view/editor/index.vue';

use([
  CanvasRenderer,
  BarChart,
  LineChart,
  PieChart,
  RadarChart,
  GridComponent,
  TooltipComponent,
  LegendComponent,
  DataZoomComponent,
  GraphicComponent,
]);

export default {
  install(Vue: App) {
    Vue.component('Chart', Chart);
    Vue.component('Breadcrumb', Breadcrumb);
    Vue.component('a-query-header', AQueryHeader);
    Vue.component('card-list', CardList);
    Vue.component('Editor', Editor);
  },
};
