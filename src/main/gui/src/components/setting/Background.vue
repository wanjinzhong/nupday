<template>
  <div>
    <span>登陆背景</span>
    <Upload action="/api/loginBackground"
            multiple with-credentials accept="image/*"
            list-type="picture"
            :show-file-list="false"
            :on-success="reloadLoginBg"
            :auto-upload="true" class="upload">
      <Button type="primary" size="mini" icon="el-icon-upload">点击上传</Button>

    </Upload>
    <div v-loading="loginLoading">
      <img :src="loginBg" class="bgImg"/>

    </div>
    <span>主页背景</span>
    <Upload action="/api/homeBackground" ref="upload"
            list-type="text"
            :show-file-list="false"
            multiple with-credentials accept="image/*"
            :on-success="reloadHomeBg"
            class="upload">
      <Button type="primary" size="mini" icon="el-icon-upload">点击上传</Button>
    </Upload>
    <div v-loading="homeLoading">
      <img :src="homeBg" class="bgImg"/>
    </div>
  </div>
</template>

<script>
  import {Upload, Button} from "element-ui"

  export default {
    name: "Background",
    components: {Upload, Button},
    data() {
      return {
        homeBg: '',
        loginBg: '',
        loading: false,
        loginLoading: false,
        homeLoading: false
      }
    },
    created() {
      this.homeLoading = true;
      this.reloadLoginBg();
      this.reloadHomeBg();
    },
    methods: {
      reloadLoginBg() {
        var that = this;
        this.loginLoading = true;
        this.axios.get("/api/loginBackground").then(res => {
          that.loginLoading = false;
          that.loginBg = res.data.data;
        }).catch(res => {
          that.loginLoading = false;
        });
      },
      reloadHomeBg() {
        var that = this;
        this.axios.get("/api/homeBackground").then(res => {
          that.homeLoading = false;
          that.homeBg = res.data.data;
        }).catch(res => {
          that.homeLoading = false;
        });
      },

    }
  }
</script>

<style scoped>
  .bgImg {
    max-width: 500px;
    max-height: 300px;
    margin-bottom: 20px;
  }
  .upload {
    display: inline-block;
    margin-bottom: 10px;
  }
</style>
