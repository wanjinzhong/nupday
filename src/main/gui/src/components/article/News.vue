<template>
  <div style="width: 600px" v-loading="loading">
    <Steps direction="vertical" :active="news.length">
      <Step v-for="newsItem in news" :key="newsItem.date">
        <div slot="title">{{newsItem.date}}</div>
        <svg class="icon" aria-hidden="true" slot="icon">
          <use xlink:href="#icon-yuandianda"></use>
        </svg>
        <NewsList slot="description" :newsList="newsItem.newsItems"></NewsList>
      </Step>
    </Steps>
    <div v-if="hasNext" style="cursor: pointer; text-align: center">
      <Button @click="loadArticle" :disabled="loading" v-loading="loading">点击加载更多</Button>
    </div>
  </div>
</template>

<script>
  import {Steps, Step, Button} from "element-ui"
  import NewsList from "./NewsList"

  export default {
    name: "News",
    components: {Steps, Step, Button, NewsList},
    data() {
      return {
        loading: false,
        news: [],
        page: 0,
        size: 10,
        hasNext: false
      }
    },
    methods: {
      loadArticle() {
        var params = {
          page: this.page + 1,
          size: this.size
        };
        this.loading = true;
        var that = this;
        this.axios.get("/api/news", {params: params}).then(res => {
          var data = res.data.data;
          that.page = data.page.currentPage;
          that.hasNext = data.page.totalPages > data.page.currentPage;
          that.loading = false;
          for (var i in data.news) {
            that.news.push(data.news[i]);
          }
        }).catch(res => {
          that.loading = false;
        });
      },

    },
    created() {
      this.loadArticle();
    }
  }
</script>

<style>
  .el-step__head.is-finish {
    color: #de2070 !important;
    border-color: #de2070 !important;
  }

  .el-step__title.is-finish {
    color: #333;
  }
</style>
