// 头部事件
export const HeaderEmitter = {
  ADD: 'add',
  UPDATE_SEARCH_VALUE: 'update:searchValue',
  SEARCH: 'search',
  RESET: 'reset',
  PAGE_CHANGE: 'pageChange'
};

// 卡片事件
export const CardEmitter = {
  CLICK: 'click',
  DBL_CLICK: 'dblclick'
};

// 事件
export const Emitter = [
  ...Object.values(HeaderEmitter),
  ...Object.values(CardEmitter),
];
