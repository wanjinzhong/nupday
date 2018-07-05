<template>
  <div id="newArticle" v-loading="pushing" :element-loading-text="loadingText">
    文章标题 <Input style="width: 100%; margin-top: 10px; margin-bottom: 20px" v-model="title"/><br/>
    <el-switch style="margin-bottom: 20px;" v-model="isOpen" active-text="开放" inactive-text="隐私"></el-switch>
    <div class="break"></div>
    <el-switch style="margin-bottom: 20px;" v-model="isNotDraft" active-text="发布" inactive-text="草稿"></el-switch>
    <div class="break"></div>
    <el-switch style="margin-bottom: 20px;" v-model="commentable" active-text="可评论" inactive-text="禁止评论"></el-switch>
    <vue-tinymce
      style="margin-top: 20px"
      ref="tinymce"
      v-model="content"
      :setting="setting">
    </vue-tinymce>
    <Button type="primary" @click="onSubmit" style="margin-top: 10px" :disabled="btnDisabled">
      {{isNotDraft?"发布文章":"保存为草稿"}}
    </Button>
    <Button type="danger" @click="readyToDelete" style="margin-top: 10px" :disabled="btnDisabled" v-if="id > -1">删除文章
    </Button>
    <Dialog title="确认删除" :visible.sync="showDeleteDialog" width="520px">
      <span>确认要删除这篇文章吗？删除之后不可恢复，但是你可以先移动到回收站。</span>
      <span slot="footer" class="dialog-footer" v-loading="">
        <Button @click="showDeleteDialog = false">取消</Button>
        <Button type="primary" @click="deleteArticle('true')">移到回收站</Button>
        <Button type="danger" @click="deleteArticle('false')">删除</Button>
      </span>
    </Dialog>
  </div>
</template>

<script>
  import {TinymceSetting, VueTinymce} from '@packy-tang/vue-tinymce';
  import {Button, Form, FormItem, Input, Switch, Dialog} from 'element-ui'

  export default {
    name: 'EditArticle',
    components: {VueTinymce, TinymceSetting, Button, Input, FormItem, Form, Switch, Dialog},
    data: function () {
      return {
        id: -1,
        type:'',
        content: '',
        title: '',
        pushing: false,
        btnDisabled: false,
        isOpen: true,
        isNotDraft: true,
        commentable: true,
        deletable: false,
        showDeleteDialog: false,
        loadingText: '正在发布文章',
        deleting: false,
        setting: {
          ...TinymceSetting,
          height: 200,
          language_url: "static/zh_CN.js",
          block_formats: "Paragraph=p;Heading 1=h1;Heading 2=h2;Heading 3=h3;Heading 4=h4;Heading 5=h5;Heading 6=h6;"
        }
      }
    },
    methods: {
      onSubmit() {
        if (this.title == "") {
          this.$message({
            type: "error",
            message: "文章标题不能为空"
          });
          return;
        }
        var data = {
          id: this.id,
          title: this.title,
          content: this.content,
          type: "ARTICLE",
          permission:{
            commentable: this.commentable
          },
          isDraft: !this.isNotDraft,
          isOpen: this.isOpen
        };
        var that = this;
        var message;
        if (this.isNotDraft) {
          this.loadingText = "正在发布文章";
          if (this.isOpen) {
            message = "文章公共发表成功";
          } else {
            message = "文章私密发表成功";
          }
        } else {
          this.loadingText = "正在保存到草稿箱";
          message = "文章已存到草稿箱";
        }
        this.pushing = true;
        this.btnDisabled = true;
        this.id > -1 ? this.updateArticle(data, message) : this.newArticle(data, message);
      },
      newArticle(data, message) {
        var that = this;
        this.axios.post("/api/article", data).then((res) => {
          var id = res.data.data;
          this.pushing = false;
          that.$message({
            type: "success",
            message: message,
            onClose: function () {
              that.$router.push("/article/" + id);
            }
          });
        }).catch((res) => {
          this.pushing = false
        });
      },
      updateArticle(data, message) {
        var that = this;
        this.axios.put("/api/article", data).then((res) => {
          var id = res.data.data;
          that.pushing = false;
          that.$message({
            type: "success",
            message: message,
            onClose: function () {
              that.$router.push("/article/" + id);
            }
          })
        }).catch((res) => {
          this.pushing = false
        });
      },
      readyToDelete() {
        if (this.type != 'ARTICLE') {
          this.$notify({
            title: '不可删除',
            message: '这不是一篇文章',
            type: 'error'
          });
          this.$router.push("/");
          return;
        }
        if (!this.deletable) {
          this.$notify({
            title: '不可删除',
            message: '你没有权限删除这篇文章',
            type: 'error'
          });
          this.$router.push("/");
          return;
        }
        this.showDeleteDialog = true;
      },
    deleteArticle(toDustbin) {
      var that = this;
      this.deleting = true;
      var params = "?id=" + this.id + "&toDustbin=" + toDustbin;
      this.axios.delete("/api/article" + params).then(res => {
        that.$message({
          type: "success",
          message: "文章删除成功",
          onClose: function () {
            that.showDeleteDialog = false;
            that.$router.push("/");
          }
        });
      });
    }},
    created() {
      if (this.$route.fullPath.indexOf("editArticle") >= 0) {
        var articleId = this.$route.params.articleId;
        var that = this;
        this.axios.get("/api/article/" + articleId).then((res) => {
          var data = res.data.data;
          if (data.type != 'ARTICLE') {
            that.$notify({
              title: '不可编辑',
              message: '这不是一篇文章',
              type: 'error'
            });
            that.$router.push("/");
            return;
          }
          if (!data.permission.editable) {
            that.$notify({
              title: '不可编辑',
              message: '你没有权限编辑这篇文章',
              type: 'error'
            });
            that.$router.push("/");
            return;
          }
          this.id = data.id;
          this.type = data.type;
          this.title = data.title;
          this.content = data.content;
          this.isOpen = data.isOpen;
          this.isNotDraft = !data.isDraft;
          this.commentable = data.permission.commentable;
          this.deletable = data.permission.deletable;
        });
      }
    }
  }
</script>
<style scoped>
  #newArticle {
    width: 1000px;
    margin: 0 auto;
  }

  .break {
    margin: 5px 10px;
    width: 1px;
    background-color: #ccc;
    height: 20px;
    display: inline-block;
  }
</style>
