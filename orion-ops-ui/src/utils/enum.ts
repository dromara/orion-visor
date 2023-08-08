/**
 * 转为 select options
 */
export const toOptions = (enums: any) => {
  const arr = [];
  for (let k in enums) {
    arr.push(enums[k]);
  }
  return arr;
};

/**
 * 获取枚举值
 */
export const getEnumValue = (value: any,
                             enums: any,
                             key = 'label',
                             defaultValue = value) => {
  for (let k in enums) {
    if (enums[k].value === value) {
      return enums[k][key];
    }
  }
  return defaultValue;
};

/**
 * 获取枚举对象
 */
export const getEnum = (value: any, enums: any) => {
  for (let k in enums) {
    if (enums[k].value === value) {
      return enums[k];
    }
  }
  return {};
};
