<template>
  <a-textarea class="action-bar-clipboard"
              v-model="clipboardData"
              :ref="setAutoFocus"
              placeholder="远程剪切板"
              allow-clear />
  <!-- 按钮 -->
  <a-space class="action-bar-content-footer">
    <a-button size="small" @click="clearClipboardData">
      清空
    </a-button>
    <a-button type="primary"
              size="small"
              title="仅将文本发送到远程剪切板"
              :disabled="!clipboardData"
              @click="sendClipboardData(false)">
      发送
    </a-button>
    <a-button type="primary"
              size="small"
              title="将文本发送到远程剪切板并执行粘贴操作"
              :disabled="!clipboardData"
              @click="sendClipboardData(true)">
      粘贴
    </a-button>
  </a-space>
</template>

<script lang="ts">
  export default {
    name: 'clipboardAction'
  };
</script>

<script lang="ts" setup>
  import type { IGuacdSession } from '@/views/terminal/interfaces';
  import { ref, onMounted } from 'vue';
  import { setAutoFocus } from '@/utils/dom';
  import { readText } from '@/hooks/copy';

  const props = defineProps<{
    session: IGuacdSession;
  }>();
  const emits = defineEmits(['close']);

  const clipboardData = ref('');

  // 发送剪切板数据
  const sendClipboardData = (sendPaste: boolean) => {
    props.session.paste(clipboardData.value, sendPaste);
    emits('close');
  };

  // 清空剪切板数据
  const clearClipboardData = () => {
    clipboardData.value = '';
  };

  // 初始化
  onMounted(() => {
    readText(false)
      .then(s => clipboardData.value = s)
      .catch(() => clipboardData.value = '');
  });

</script>

<style lang="less" scoped>
</style>
