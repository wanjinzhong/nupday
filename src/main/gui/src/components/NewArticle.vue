<template>
  <div id="newArticle">
    文章标题        <Input style="width: 100%; margin-top: 10px; margin-bottom: 20px"/>
      <vue-tinymce
        style="margin-top: 10px"
        ref="tinymce"
        v-model="content"
        :setting="setting">
      </vue-tinymce>
      <Button type="primary" @click="onSubmit" style="margin-top: 10px">发布</Button>
  </div>
</template>

<script>
  import {VueTinymce, TinymceSetting} from '@packy-tang/vue-tinymce';
  import {Button, Input, FormItem, Form} from 'element-ui'

  export default {
    name: 'NewArticle',
    components: {VueTinymce, TinymceSetting, Button, Input, FormItem, Form},
    data: function () {
      return {
        content: '<p>html content</p>',
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
        console.log(this.content);
      }
    }
  }
</script>
<style scoped>
  #newArticle {
    width: 1400px;
    margin: 0 auto;
  }
</style>
