<template>
  <div>
    <div class="content" v-for="(item, index) in newsList" >
      <NewsItem :item="item"></NewsItem>
      <div v-if="index != newsList.length -1" class="breaker"></div>
    </div>
  </div>
</template>

<script>
  import NewsItem from "./NewsItem"
  export default {
    name: "NewsList",
    components: {NewsItem},
    props: ["newsList"]
  }
</script>

<style scoped>

  .breaker {
    height: 1px;
    background-color: #eee;
  }
</style>
