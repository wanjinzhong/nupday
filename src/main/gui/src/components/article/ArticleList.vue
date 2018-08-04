<template>
  <div v-loading="loading">
    <div v-for="item in articles" >
      <NewsItem :key="item.id" :item="item" @reload="articles= [];loadArticle"></NewsItem>
      <div class="breaker"></div>
    </div>
    <template v-if="articles.length == 0">
      <div v-if="dustbin" style="text-align: center">
        回收站里没有文章
      </div>
      <div v-else style="text-align: center">还没有文章哦</div>
    </template>
    <div v-if="hasNext" style="margin-top: 20px;cursor: pointer; text-align: center">
      <Button @click="loadArticle" :disabled="loading" v-loading="loading">点击加载更多</Button>
    </div>
  </div>
</template>

<script>
  import NewsItem from "./NewsItem"
  import {Button} from "element-ui"

  export default {
    name: "ArticleList",
    components: {NewsItem, Button},
    props: ["dustbin"],
    data() {
      return {
        loading: false,
        page: 0,
        size: 5,
        articles: [],
        hasNext: false
      }
    },
    methods: {
      loadArticle() {
        let params = {
          page: this.page + 1,
          size: this.size,
          dustbin: this.dustbin
        };
        this.loading = true;
        var that = this;
        this.axios.get("/api/articles", {params: params}).then(res => {
          var data = res.data.data;
          that.page = data.page.currentPage;
          that.hasNext = data.page.totalPages > data.page.currentPage;
          for (var i in data.articles) {
            that.articles.push(data.articles[i]);
          }
          that.loading = false;
        }).catch(res => {
          that.loading = false;
        })
      }
    },
    created() {
      this.loadArticle();
    }
  }
</script>

<style scoped>
  .breaker {
    height: 1px;
    background-color: #eee;
  }
</style>
