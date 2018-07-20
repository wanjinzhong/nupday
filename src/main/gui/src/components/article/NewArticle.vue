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
        type:'',
        content: '',
        title: '',
        loading: false,
        btnDisabled: false,
        isOpen: true,
        isNotDraft: true,
        commentable: true,
        loadingText: '正在发布文章',
        pushing: false,
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
          title: this.title,
          content: this.content,
          commentable: this.commentable,
          isDraft: !this.isNotDraft,
          isOpen: this.isOpen
        };
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
