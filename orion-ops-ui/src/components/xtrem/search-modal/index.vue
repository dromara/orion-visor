<template>
  <div class="search-modal" v-show="visible">
    <!-- 输入框-->
    <input class="search-input"
           ref="inputRef"
           v-model="inputValue"
           placeholder="搜索关键字"
           @keyup.enter="find(true)"
           @keyup.esc="close" />
    <div class="options-wrapper">
      <!-- 上一个-->
      <div class="icon-wrapper"
           title="上一个"
           @click="find(false)">
        <icon-up />
      </div>
      <!-- 下一个 -->
      <div class="icon-wrapper"
           title="下一个"
           @click="find(true)">
        <icon-down />
      </div>
      <!-- 区分大小写 -->
      <div class="icon-wrapper"
           :class="{ selected: searchOptions.caseSensitive }"
           title="区分大小写"
           @click="toggleOption('caseSensitive')">
        <icon-font-colors />
      </div>
      <!-- 单词匹配 -->
      <div class="icon-wrapper word-option"
           :class="{ selected: searchOptions.wholeWord }"
           title="单词匹配"
           @click="toggleOption('wholeWord')">
        <icon-formula />
      </div>
      <!-- 正则匹配 -->
      <div class="icon-wrapper"
           :class="{ selected: searchOptions.regex }"
           title="正则匹配"
           @click="toggleOption('regex')">
        <icon-italic />
      </div>
      <!-- 关闭 -->
      <div class="icon-wrapper"
           title="关闭"
           @click="close">
        <icon-close />
      </div>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'xtermSearchModal'
  };
</script>

<script lang="ts" setup>
  import type { ISearchOptions } from 'xterm-addon-search';
  import useVisible from '@/hooks/visible';
  import { nextTick, ref } from 'vue';

  const emits = defineEmits(['find', 'close']);

  const { visible, setVisible } = useVisible();

  const inputRef = ref();
  const inputValue = ref();
  const searchOptions = ref<ISearchOptions>({
    caseSensitive: false,
    wholeWord: false,
    regex: false
  });

  // 打开
  const open = () => {
    setVisible(true);
    nextTick(() => {
      inputRef.value.focus();
    });
  };

  // 关闭
  const close = () => {
    setVisible(false);
    inputValue.value = undefined;
    emits('close');
  };

  // 切换状态
  const toggle = () => {
    if (visible.value) {
      close();
    } else {
      open();
    }
  };

  // 查找
  const find = (next: boolean) => {
    inputRef.value.focus();
    if (inputValue.value) {
      emits('find', inputValue.value, next, searchOptions.value);
    }
  };

  // 切换选项
  const toggleOption = (key: string) => {
    searchOptions.value[key as keyof ISearchOptions] =
      !searchOptions.value[key as keyof ISearchOptions] as any;
    inputRef.value.focus();
  };

  defineExpose({ open, close, toggle });

</script>

<style lang="less" scoped>
  .search-modal {
    position: absolute;
    top: 6px;
    right: 6px;
    width: 272px;
    height: 32px;
    padding: 4px;
    z-index: 30;
    border-radius: 2px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    background: var(--bg);
    transition: background-color .2s;

    &:focus-within, &:hover {
      background: var(--bg-focus);

      .search-input {
        color: var(--color-text-focus);
      }

      .icon-wrapper {
        color: var(--color-text-focus);
        transition: background-color .2s;

        &:hover {
          background: var(--bg-icon-hover-focus);
        }

        &.selected {
          background: var(--bg-icon-selected-focus);
        }
      }
    }
  }

  .search-input {
    border: none;
    background: red;
    background: none;
    width: 130px;
    outline: none;
    height: 18px;
    font-size: 12px;
    color: var(--color-text);
  }

  .word-option {
    transform: rotate(-90deg)
  }

  .options-wrapper {
    display: flex;
    align-items: center;

    .icon-wrapper {
      font-size: 12px;
      cursor: pointer;
      border-radius: 4px;
      width: 20px;
      height: 20px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: var(--color-text);

      &:not(:first-child) {
        margin-left: 2px;
      }

      &:hover {
        background: var(--bg-icon-hover);
      }

      &.selected {
        background: var(--bg-icon-selected);
      }
    }
  }

</style>
