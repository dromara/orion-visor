<template>
  <div>
    <p>UserChild 2</p>
    <h1 v-permission="['admin']">123</h1>
  </div>
</template>

<script>
  export default {
    name: 'UserChild2',
  };
</script>

<style scoped>

</style>
