<template>
  <div class="article_container">
    <div class="title">{{title}}</div>
    <div class="info">
      {{entryUser}}&nbsp;&nbsp;
      {{entryDatetime | formatDate("DATETIME")}}&nbsp;&nbsp;
      <svg class="icon zan" aria-hidden="true" :style="{color: liked?'#f00':'#555'}" @click="zan">
        <use :xlink:href="liked?'#icon-zan1' : '#icon-zan2'"></use>
      </svg>
      {{likes}}
    </div>
    <div class="break"></div>
    <div class="content">
      <div v-html="content"></div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "ArticleDetail",
    data() {
      return {
        articleId: 0,
        title: '',
        content: '',
        likes: 0,
        entryUser: '',
        entryDatetime: '',
        liked: false
      }
    },
    created() {
      var articleId = this.$route.params.articleId;
      this.articleId = articleId;
      this.axios.get("/api/article/" + articleId).then((res) => {
        var data = res.data.data;
        this.title = data.title;
        this.content = data.content;
        this.likes = data.likes;
        this.entryUser = data.entryUser;
        this.entryDatetime = new Date(data.entryDatetime);
      });
    },
    methods: {
      zan() {
        if (this.liked) {
          return;
        }
        this.liked = true;
        this.axios.put("/api/article/" + this.articleId + "/like").then((res) => {
          console.log(res.data.data);
          this.likes = res.data.data;
        })
      }
    }
  }
</script>

<style scoped>
  .title {
    font-size: 30px;
    font-weight: bold;
    color: black;
    text-align: center;
    margin: 10px auto;
  }
  .article_container{
    width: 1000px;
    margin: 0 auto;
    padding: 20px 50px;
    background: url("../../assets/article_bg.jpg") repeat;
    box-shadow: 2px 2px 5px #aaa;
  }
  .break {
    width: 100%;
    height: 2px;
    background-color: #ccc;
    margin-top: 15px;
  }
  .info {
    margin: 0 auto;
    text-align: center;
  }
  .zan {
    font-size: 30px;
    padding-top: 5px;
    margin-bottom: -5px;
    cursor: pointer;
  }
</style>
