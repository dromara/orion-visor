import type { RouteRecordNormalized } from 'vue-router';

// 应用模块
const modules = import.meta.glob('./modules/*.ts', { eager: true });

// 应用路由
export const appRoutes: RouteRecordNormalized[] = formatModules(modules, []);

// 格式化模块
function formatModules(_modules: any, result: RouteRecordNormalized[]) {
  Object.keys(_modules).forEach((key) => {
    const defaultModule = _modules[key].default;
    if (!defaultModule) return;
    const moduleList = Array.isArray(defaultModule)
      ? [...defaultModule]
      : [defaultModule];
    result.push(...moduleList);
  });
  return result;
}
