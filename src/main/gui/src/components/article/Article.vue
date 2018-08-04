<template>
  <ArticleList :dustbin="false"></ArticleList>
</template>

<script>
  import ArticleList from "./ArticleList"

  export default {
    name: "Article",
    components: {ArticleList}
  }
</script>

<style scoped>

</style>
