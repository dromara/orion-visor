import type { GetParams } from '@/types/global';
import Mock from 'mockjs';
import qs from 'query-string';
import dayjs from 'dayjs';
import setupMock, { successResponseWrap } from '@/utils/setup-mock';

const textList = [
  {
    key: 1,
    clickNumber: '1w+',
    title: 'text...',
    increases: 35,
  },
  {
    key: 2,
    clickNumber: '2w+',
    title: 'text...',
    increases: 22,
  },
];

setupMock({
  setup() {
    Mock.mock(new RegExp('/api/content-data'), () => {
      const presetData = [58, 81, 53, 90, 64, 88, 49, 79];
      const getLineData = () => {
        const count = 8;
        return new Array(count).fill(0).map((el, idx) => ({
          x: dayjs()
            .day(idx - 2)
            .format('YYYY-MM-DD'),
          y: presetData[idx],
        }));
      };
      return successResponseWrap([...getLineData()]);
    });
    Mock.mock(new RegExp('/api/popular/list'), (params: GetParams) => {
      const { type = 'text' } = qs.parseUrl(params.url).query;
      if (type === 'text') {
        return successResponseWrap([...textList]);
      } else {
        return successResponseWrap([]);
      }
    });
  },
});
