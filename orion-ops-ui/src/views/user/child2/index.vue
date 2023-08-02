<template>
  <div>
    <p>UserChild 2</p>
    <h1 v-permission="['admin']">123</h1>
    <button @click="red">red</button>
  </div>
</template>

<script lang="ts" setup>
  import router from '@/router';

  function red() {
    router.push({ name: 'workplace' });
  }
</script>

<style scoped>

</style>
