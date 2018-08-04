<template>
  <div>
    <ArticleList :dustbin="true"></ArticleList>
  </div>
</template>

<script>
  import ArticleList from "../article/ArticleList"

  export default {
    name: "ArticleDustbin",
    components: {ArticleList}
  }
</script>

<style scoped>

</style>
