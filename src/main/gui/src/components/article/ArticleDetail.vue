<template>
  <div style="width: 1100px">
    <div class="article_container" v-loading="loading">
      <div class="title">{{title}}</div>
      <div style="width: 100%">
        <div style="float: left; display: inline-block; width: 100px">&nbsp;</div>
        <div class="info">
          {{entryUser}}&nbsp;&nbsp;
          {{entryDatetime | formatDate("DATETIME")}}&nbsp;&nbsp;
          <svg class="icon zan" aria-hidden="true" :style="{color: liked?'#f00':'#555'}" @click="zan">
            <use :xlink:href="liked?'#icon-zan1' : '#icon-zan2'"></use>
          </svg>
          {{likes}}
        </div>
        <div class="operation" v-if="this.operatable">
          <Tooltip content="编辑" effect="light" :open-delay="500">
            <svg class="icon delete" aria-hidden="true" @click="toEdit">
              <use xlink:href="#icon-ai-edit"></use>
            </svg>
          </Tooltip>&nbsp;
          <Tooltip :content="isOpen?'锁定文章':'解锁文章'" effect="light" :open-delay="500">
            <svg class="icon lock" aria-hidden="true" @click="changeLock">
              <use :xlink:href="isOpen?'#icon-jiesuo' : '#icon-suoding'"></use>
            </svg>
          </Tooltip>&nbsp;
          <Tooltip content="删除文章" effect="light" :open-delay="500">
            <svg class="icon delete" aria-hidden="true" @click="readyToDelete">
              <use xlink:href="#icon-shanchu"></use>
            </svg>
          </Tooltip>
        </div>
      </div>
      <div class="break"></div>
      <div class="content">
        <div v-html="content"></div>
      </div>
      <Dialog title="确认删除" :visible.sync="showDeleteDialog" width="520px">
        <span>确认要删除这篇文章吗？删除之后不可恢复，但是你可以先移动到回收站。</span>
        <span slot="footer" class="dialog-footer" v-loading="">
        <Button @click="showDeleteDialog = false">取消</Button>
        <Button type="primary" @click="deleteArticle('true')">移到回收站</Button>
        <Button type="danger" @click="deleteArticle('false')">删除</Button>
      </span>
      </Dialog>
    </div>
    <Comment style="margin: 20px auto" :target-id="articleId" target-type="ARTICLE" :commentable="commentable"></Comment>
  </div>
</template>

<script>
  import {Button, Dialog, Tooltip} from "element-ui"
  import Comment from "../comment/Comment"

  export default {
    name: "ArticleDetail",
    components: {Dialog, Button, Tooltip, Comment},
    data() {
      return {
        loading: false,
        articleId: 0,
        title: '',
        content: '',
        likes: 0,
        entryUser: '',
        entryDatetime: '',
        operatable: false,
        deletable: false,
        commentable: false,
        liked: false,
        isOpen: false,
        showDeleteDialog: false,
        deleting: false
      }
    },
    created() {
      var articleId = this.$route.params.articleId;
      this.articleId = articleId;
      this.loading = true;
      this.axios.get("/api/article/" + articleId).then((res) => {
        this.loading = false;
        var data = res.data.data;
        this.title = data.title;
        this.content = data.content;
        this.likes = data.likes;
        this.entryUser = data.entryUser;
        this.entryDatetime = new Date(data.entryDatetime);
        this.isOpen = data.isOpen;
        this.deletable = data.permission.deletable;
        this.operatable = data.operatable;
        this.commentable = data.permission.commentable;
      }).catch(res=>{
        this.loading = false;
        this.$router.push("/")
      });
    },
    methods: {
      zan() {
        if (this.liked) {
          return;
        }
        this.liked = true;
        this.axios.put("/api/article/" + this.articleId + "/like").then((res) => {
          this.likes = res.data.data;
        })
      },
      changeLock() {
        var message;
        var changeStatus;
        var confirmTitle;
        if (this.isOpen) {
          message = "确定要锁定吗？锁定之后这篇文章对访客将不可见";
          changeStatus = "CLOSE";
          confirmTitle = "锁定文章";
        } else {
          message = "确定要解锁吗？解锁之后这篇文章将对访客可见";
          changeStatus = "OPEN";
          confirmTitle = "文章解锁";
        }
        var that = this;
        this.$confirm(message, confirmTitle, {type: 'warning'}).then(() => {
          that.axios.put("/api/article/" + this.articleId + "/" + changeStatus).then(() => {
            that.isOpen = !that.isOpen;
            that.$message({
              message: confirmTitle + "成功",
              type: "success",
            });
          })
        })
      },
      readyToDelete() {
        if (!this.deletable) {
          this.$notify({
            title: '不可删除',
            message: '你没有权限删除这篇文章',
            type: 'error'
          });
          return;
        }
        this.showDeleteDialog = true;
      },
      toEdit() {
        this.$router.push("/editArticle/" + this.articleId);
      },
      deleteArticle(toDustbin) {
        var that = this;
        this.deleting = true;
        var params = "?id=" + this.articleId + "&toDustbin=" + toDustbin;
        this.axios.delete("/api/article" + params).then(res => {
          that.showDeleteDialog = false;
          that.$message({
            type: "success",
            message: "文章删除成功"
          });
          that.$router.push("/");
        });
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

  .article_container {
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
    display: inline-block;
    width: 800px;
  }

  .zan {
    font-size: 30px;
    padding-top: 5px;
    margin-bottom: -5px;
    cursor: pointer;
  }

  .operation {
    float: right;
    display: inline-block;
    line-height: 40px;
    width: 100px;
  }

  .lock, .delete {
    font-size: 20px;
    cursor: pointer;
  }
</style>
