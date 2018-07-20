<template>
  <div>
    <Switch v-model="article" v-loading="articleLoading" @change="update('ARTICLE')"></Switch>
    <p class="title">邮件通知</p>
    <div class="switcherContainer">文章发布
      <el-switch v-model="article" v-loading="articleLoading" @change="update('ARTICLE')">c</el-switch>
    </div>
    <div class="switcherContainer">照片上传
      <el-switch v-model="photo" v-loading="photoLoading" @change="update('PHOTO')">c</el-switch>
    </div>
    <div class="switcherContainer">评论或回复
      <el-switch v-model="comment" v-loading="commentLoading" @change="update('COMMENT')">c</el-switch>
    </div>
  </div>
</template>

<script>
  import {Switch} from 'element-ui'

  export default {
    name: "Notification",
    components: {Switch},
    data() {
      return {
        article: false,
        articleLoading: false,
        photo: false,
        photoLoading: false,
        comment: false,
        commentLoading: false
      }
    },
    methods: {
      update(type) {
        this.setLoading(this, type, true);
        var that = this;
        var data;
        switch (type) {
          case "ARTICLE":
            data = this.article;
            break;
          case "PHOTO":
            data = this.photo;
            break;
          case "COMMENT":
            data = this.comment;
            break;
        }
        this.axios.put("/api/emailNotification/" + type + "/" + data).then(res => {
          that.setLoading(that, type, false);
        }).catch(res => {
          that.setLoading(that, type, false);
        })
      },
      setLoading(that, type, isLoading) {
        switch (type) {
          case "ARTICLE":
            that.articleLoading = isLoading;
            break;
          case "PHOTO":
            that.photoLoading = isLoading;
            break;
          case "COMMENT":
            that.commentLoading = isLoading;
            break;
        }
      }
    },
    created() {
      var that = this;
      that.setLoading(that, "ARTICLE", true);
      that.setLoading(that, "PHOTO", true);
      that.setLoading(that, "COMMENT", true);
      this.axios.get("/api/emailNotification/ARTICLE").then(res => {
        that.article = res.data.data;
        that.setLoading(that, "ARTICLE", false);
      }).catch(res => {
        that.setLoading(that, "ARTICLE", false);
      });
      this.axios.get("/api/emailNotification/PHOTO").then(res => {
        that.photo = res.data.data;
        that.setLoading(that, "PHOTO", false);
      }).catch(res => {
        that.setLoading(that, "PHOTO", false);
      });
      this.axios.get("/api/emailNotification/COMMENT").then(res => {
        that.comment = res.data.data;
        that.setLoading(that, "COMMENT", false);
      }).catch(res => {
        that.setLoading(that, "COMMENT", false);
      });
    }
  }
</script>

<style scoped>
  .switcherContainer {
    margin-top: 15px;
    margin-bottom: 15px;
  }

  .title {
    font-weight: bold;
  }
</style>
