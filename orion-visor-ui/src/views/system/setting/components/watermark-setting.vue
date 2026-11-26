<template>
  <a-spin class="main-container" :loading="loading">
    <!-- 标题 -->
    <h3 class="setting-header">页面水印设置</h3>
    <!-- 表单 -->
    <a-form :model="setting"
            ref="formRef"
            class="setting-form"
            label-align="right"
            :auto-label-width="true">
      <!-- 页面水印开关 -->
      <a-form-item label="启用页面水印"
                   :rules="[{required: true, message: '请选择此项'}]"
                   hide-asterisk>
        <a-switch v-model="setting['watermark.page.enabled']"
                  type="round"
                  checked-value="true"
                  unchecked-value="false"
                  checked-text="开启"
                  unchecked-text="关闭" />
        <template #extra>
          开启后系统页面将显示当前登录用户名水印
        </template>
      </a-form-item>
      <!-- 页面水印字号 -->
      <a-form-item label="页面水印字号"
                   :rules="[{required: true, message: '请输入水印字号'}]"
                   hide-asterisk>
        <a-input-number v-model="setting['watermark.page.font-size']"
                        class="input-wrapper"
                        :min="8"
                        :max="48"
                        :step="1"
                        placeholder="请输入页面水印字号"
                        allow-clear
                        hide-button>
          <template #suffix>
            px
          </template>
        </a-input-number>
        <template #extra>
          页面水印文字大小
        </template>
      </a-form-item>
      <!-- 页面水印灰度 -->
      <a-form-item label="页面水印灰度"
                   :rules="[{required: true, message: '请选择此项'}]"
                   hide-asterisk>
        <a-switch v-model="setting['watermark.page.grayscale']"
                  type="round"
                  checked-value="true"
                  unchecked-value="false"
                  checked-text="开启"
                  unchecked-text="关闭" />
        <template #extra>
          开启后页面水印以灰度显示
        </template>
      </a-form-item>
      <!-- 页面水印深浅 -->
      <a-form-item label="页面水印深浅"
                   :rules="[{required: true, message: '请输入水印深浅'}]"
                   hide-asterisk>
        <a-input-number v-model="setting['watermark.page.alpha']"
                        class="input-wrapper"
                        :min="0.1"
                        :max="1"
                        :step="0.1"
                        placeholder="请输入页面水印深浅"
                        allow-clear
                        hide-button>
          <template #suffix>
            (0.1~1)
          </template>
        </a-input-number>
        <template #extra>
          页面水印透明度, 值越小越浅
        </template>
      </a-form-item>
    </a-form>

    <!-- 标题 -->
    <h3 class="setting-header">终端水印设置</h3>
    <!-- 表单 -->
    <a-form :model="setting"
            ref="formRef"
            class="setting-form"
            label-align="right"
            :auto-label-width="true">
      <!-- 终端水印开关 -->
      <a-form-item label="启用终端水印"
                   :rules="[{required: true, message: '请选择此项'}]"
                   hide-asterisk>
        <a-switch v-model="setting['watermark.terminal.enabled']"
                  type="round"
                  checked-value="true"
                  unchecked-value="false"
                  checked-text="开启"
                  unchecked-text="关闭" />
        <template #extra>
          开启后终端截屏将显示当前登录用户名水印
        </template>
      </a-form-item>
      <!-- 终端水印字号 -->
      <a-form-item label="终端水印字号"
                   :rules="[{required: true, message: '请输入水印字号'}]"
                   hide-asterisk>
        <a-input-number v-model="setting['watermark.terminal.font-size']"
                        class="input-wrapper"
                        :min="8"
                        :max="48"
                        :step="1"
                        placeholder="请输入终端水印字号"
                        allow-clear
                        hide-button>
          <template #suffix>
            px
          </template>
        </a-input-number>
        <template #extra>
          终端截屏水印文字大小
        </template>
      </a-form-item>
      <!-- 终端水印深浅 -->
      <a-form-item label="终端水印深浅"
                   :rules="[{required: true, message: '请输入水印深浅'}]"
                   hide-asterisk>
        <a-input-number v-model="setting['watermark.terminal.alpha']"
                        class="input-wrapper"
                        :min="0.1"
                        :max="1"
                        :step="0.1"
                        placeholder="请输入终端水印深浅"
                        allow-clear
                        hide-button>
          <template #suffix>
            (0.1~1)
          </template>
        </a-input-number>
        <template #extra>
          终端截屏水印透明度, 值越小越浅
        </template>
      </a-form-item>
    </a-form>

    <!-- 按钮 -->
    <a-form-item v-permission="['infra:system-setting:update']">
      <a-button type="primary"
                size="small"
                @click="save">
        保存
      </a-button>
    </a-form-item>
  </a-spin>
</template>

<script lang="ts">
  export default {
    name: 'watermarkSetting',
  };
</script>

<script lang="ts" setup>
  import type { WatermarkSetting } from '@/api/system/setting';
  import { onMounted, ref } from 'vue';
  import { getSystemSetting, updateSystemSettingBatch } from '@/api/system/setting';
  import useLoading from '@/hooks/loading';
  import { Message } from '@arco-design/web-vue';
  import { SystemSettingTypes } from '../types/const';

  const { loading, setLoading } = useLoading();

  const formRef = ref();
  const setting = ref<WatermarkSetting>({} as WatermarkSetting);

  // 保存
  const save = async () => {
    // 验证参数
    const error = await formRef.value.validate();
    if (error) {
      return false;
    }
    setLoading(true);
    try {
      await updateSystemSettingBatch({
        type: SystemSettingTypes.WATERMARK,
        settings: setting.value
      });
      Message.success('修改成功');
      // 重新加载页面使布局水印配置即时生效
      setTimeout(() => window.location.reload(), 600);
    } finally {
      setLoading(false);
    }
  };

  // 加载配置
  onMounted(async () => {
    setLoading(true);
    try {
      const { data } = await getSystemSetting(SystemSettingTypes.WATERMARK);
      setting.value = {
        'watermark.page.enabled': data['watermark.page.enabled'] || 'true',
        'watermark.page.font-size': Number(data['watermark.page.font-size']) || 16,
        'watermark.page.grayscale': data['watermark.page.grayscale'] || 'true',
        'watermark.page.alpha': Number(data['watermark.page.alpha']) || 0.6,
        'watermark.terminal.enabled': data['watermark.terminal.enabled'] || 'true',
        'watermark.terminal.font-size': Number(data['watermark.terminal.font-size']) || 14,
        'watermark.terminal.alpha': Number(data['watermark.terminal.alpha']) || 0.1,
      } as WatermarkSetting;
    } finally {
      setLoading(false);
    }
  });

</script>

<style lang="less" scoped>
</style>
