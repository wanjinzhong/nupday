<template>
  <div class="body" :style="{background:'url(' + backgroundUrl + ')  0% 0% / cover  no-repeat'}">
    <div class="container">
      <div style="width: 165px; height: 96px; position: absolute; margin-left: 20px">
        <div class="tou"></div>
        <div class="initial_left_hand" id="left_hand"></div>
        <div class="initial_right_hand" id="right_hand"></div>
      </div>
      <section id="content">
        <Menu mode="horizontal"
              background-color="#f8f8f8"
              active-text-color="#f699b4"
              default-active="1"
              @select="changeMenu">
          <MenuItem index="1">主人</MenuItem>
          <MenuItem index="2">访客</MenuItem>
        </Menu>
        <OwnerLogin v-if="isOwnerLogin" :to="originUrl"></OwnerLogin>
        <VisitorLogin v-else :to="originUrl"></VisitorLogin>
      </section><!-- content -->
    </div>
  </div>
</template>

<script>
  import {Menu, MenuItem} from "element-ui";
  import OwnerLogin from "../components/login/OwnerLogin"
  import VisitorLogin from "../components/login/VisitorLogin"

  export default {
    name: "LoginPage",
    components: {Menu, MenuItem, OwnerLogin, VisitorLogin},
    data() {
      return {
        isOwnerLogin: true,
        originUrl: ''
      }
    },
    created() {
      this.originUrl = this.$route.query.origin;
      this.axios.get("/api/loginBackground").then((res) => {
        this.$store.commit("setLoginBackground", res.data.data);
      });
      this.axios.get("/api/allSimpleOwners").then((response) => {
        this.$store.commit("setOwners", response.data.data);
      });
    },
    computed: {
      backgroundUrl() {
        return this.$store.getters.getLoginBackground;
      }
    },
    methods: {
      changeMenu(index) {
          this.isOwnerLogin = index == 1;
      }
    }
  }
</script>

<style scoped>

  .body {
    width: 100%;
    height: 100%;
    overflow: hidden;
    /*  -webkit-animation: animate-cloud 20s linear infinite;
      -moz-animation: animate-cloud 20s linear infinite;
      -ms-animation: animate-cloud 20s linear infinite;
      -o-animation: animate-cloud 20s linear infinite;
      animation: animate-cloud 20s linear infinite;*/
  }

  .clearfix:after, form:after {
    content: ".";
    display: block;
    height: 0;
    clear: both;
    visibility: hidden;
  }

  .container {
    margin: 200px auto 0 auto;
    position: relative;
    width: 400px;
  }

  #content {
    background: #f9f9f9;
    background: -moz-linear-gradient(top, rgba(248, 248, 248, 1) 0%, rgba(249, 249, 249, 1) 100%);
    background: -webkit-linear-gradient(top, rgba(248, 248, 248, 1) 0%, rgba(249, 249, 249, 1) 100%);
    background: -o-linear-gradient(top, rgba(248, 248, 248, 1) 0%, rgba(249, 249, 249, 1) 100%);
    background: -ms-linear-gradient(top, rgba(248, 248, 248, 1) 0%, rgba(249, 249, 249, 1) 100%);
    background: linear-gradient(top, rgba(248, 248, 248, 1) 0%, rgba(249, 249, 249, 1) 100%);
    /* filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#f8f8f8', endColorstr='#f9f9f9',GradientType=0 ); */
    -webkit-box-shadow: 0 1px 0 #fff inset;
    -moz-box-shadow: 0 1px 0 #fff inset;
    -ms-box-shadow: 0 1px 0 #fff inset;
    -o-box-shadow: 0 1px 0 #fff inset;
    box-shadow: 0 1px 0 #fff inset;
    border: 1px solid #c4c6ca;
    margin: 0 auto;
    padding: 25px 0 0;
    position: relative;
    text-align: center;
    text-shadow: 0 1px 0 #fff;
    width: 400px;
  }

  #content h1 {
    color: #7E7E7E;
    font: bold 25px Helvetica, Arial, sans-serif;
    letter-spacing: -0.05em;
    line-height: 20px;
    margin: 10px 0 30px;
  }

  #content h1:before,
  #content h1:after {
    content: "";
    height: 1px;
    position: absolute;
    top: 10px;
    width: 27%;
  }

  #content h1:after {
    background: rgb(126, 126, 126);
    background: -moz-linear-gradient(left, rgba(126, 126, 126, 1) 0%, rgba(255, 255, 255, 1) 100%);
    background: -webkit-linear-gradient(left, rgba(126, 126, 126, 1) 0%, rgba(255, 255, 255, 1) 100%);
    background: -o-linear-gradient(left, rgba(126, 126, 126, 1) 0%, rgba(255, 255, 255, 1) 100%);
    background: -ms-linear-gradient(left, rgba(126, 126, 126, 1) 0%, rgba(255, 255, 255, 1) 100%);
    background: linear-gradient(left, rgba(126, 126, 126, 1) 0%, rgba(255, 255, 255, 1) 100%);
    right: 0;
  }

  #content h1:before {
    background: rgb(126, 126, 126);
    background: -moz-linear-gradient(right, rgba(126, 126, 126, 1) 0%, rgba(255, 255, 255, 1) 100%);
    background: -webkit-linear-gradient(right, rgba(126, 126, 126, 1) 0%, rgba(255, 255, 255, 1) 100%);
    background: -o-linear-gradient(right, rgba(126, 126, 126, 1) 0%, rgba(255, 255, 255, 1) 100%);
    background: -ms-linear-gradient(right, rgba(126, 126, 126, 1) 0%, rgba(255, 255, 255, 1) 100%);
    background: linear-gradient(right, rgba(126, 126, 126, 1) 0%, rgba(255, 255, 255, 1) 100%);
    left: 0;
  }

  #content:after,
  #content:before {
    background: #f9f9f9;
    background: -moz-linear-gradient(top, rgba(248, 248, 248, 1) 0%, rgba(249, 249, 249, 1) 100%);
    background: -webkit-linear-gradient(top, rgba(248, 248, 248, 1) 0%, rgba(249, 249, 249, 1) 100%);
    background: -o-linear-gradient(top, rgba(248, 248, 248, 1) 0%, rgba(249, 249, 249, 1) 100%);
    background: -ms-linear-gradient(top, rgba(248, 248, 248, 1) 0%, rgba(249, 249, 249, 1) 100%);
    background: linear-gradient(top, rgba(248, 248, 248, 1) 0%, rgba(249, 249, 249, 1) 100%);
    /* filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#f8f8f8', endColorstr='#f9f9f9',GradientType=0 ); */
    border: 1px solid #c4c6ca;
    content: "";
    display: block;
    height: 100%;
    left: -1px;
    position: absolute;
    width: 100%;
  }

  #content:after {
    -webkit-transform: rotate(2deg);
    -moz-transform: rotate(2deg);
    -ms-transform: rotate(2deg);
    -o-transform: rotate(2deg);
    transform: rotate(2deg);
    top: 0;
    z-index: -1;
  }

  #content:before {
    -webkit-transform: rotate(-3deg);
    -moz-transform: rotate(-3deg);
    -ms-transform: rotate(-3deg);
    -o-transform: rotate(-3deg);
    transform: rotate(-3deg);
    top: 0;
    z-index: -2;
  }

  #content form {
    margin: 0 20px;
    position: relative
  }

  #content form input[type="text"],
  #content form input[type="password"] {
    -webkit-border-radius: 3px;
    -moz-border-radius: 3px;
    -ms-border-radius: 3px;
    -o-border-radius: 3px;
    border-radius: 3px;
    -webkit-box-shadow: 0 1px 0 #fff, 0 -2px 5px rgba(0, 0, 0, 0.08) inset;
    -moz-box-shadow: 0 1px 0 #fff, 0 -2px 5px rgba(0, 0, 0, 0.08) inset;
    -ms-box-shadow: 0 1px 0 #fff, 0 -2px 5px rgba(0, 0, 0, 0.08) inset;
    -o-box-shadow: 0 1px 0 #fff, 0 -2px 5px rgba(0, 0, 0, 0.08) inset;
    box-shadow: 0 1px 0 #fff, 0 -2px 5px rgba(0, 0, 0, 0.08) inset;
    -webkit-transition: all 0.5s ease;
    -moz-transition: all 0.5s ease;
    -ms-transition: all 0.5s ease;
    -o-transition: all 0.5s ease;
    transition: all 0.5s ease;
    /*background: #eae7e7 url(../assets/8bcLQqF.png) no-repeat;*/
    border: 1px solid #c8c8c8;
    color: #777;
    font: 13px Helvetica, Arial, sans-serif;
    margin: 0 0 10px;
    padding: 15px 10px 15px 40px;
    width: 80%;
  }

  #content form input[type="text"]:focus,
  #content form input[type="password"]:focus {
    -webkit-box-shadow: 0 0 2px #ed1c24 inset;
    -moz-box-shadow: 0 0 2px #ed1c24 inset;
    -ms-box-shadow: 0 0 2px #ed1c24 inset;
    -o-box-shadow: 0 0 2px #ed1c24 inset;
    box-shadow: 0 0 2px #ed1c24 inset;
    background-color: #fff;
    border: 1px solid #ed1c24;
    outline: none;
  }

  #username {
    background-position: 10px 10px !important
  }

  #password {
    background-position: 10px -53px !important
  }

  #content form input[type="submit"] {
    background: rgb(254, 231, 154);
    background: -moz-linear-gradient(top, rgba(254, 231, 154, 1) 0%, rgba(254, 193, 81, 1) 100%);
    background: -webkit-linear-gradient(top, rgba(254, 231, 154, 1) 0%, rgba(254, 193, 81, 1) 100%);
    background: -o-linear-gradient(top, rgba(254, 231, 154, 1) 0%, rgba(254, 193, 81, 1) 100%);
    background: -ms-linear-gradient(top, rgba(254, 231, 154, 1) 0%, rgba(254, 193, 81, 1) 100%);
    background: linear-gradient(top, rgba(254, 231, 154, 1) 0%, rgba(254, 193, 81, 1) 100%);
    /* filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#fee79a', endColorstr='#fec151',GradientType=0 ); */
    -webkit-border-radius: 30px;
    -moz-border-radius: 30px;
    -ms-border-radius: 30px;
    -o-border-radius: 30px;
    border-radius: 30px;
    -webkit-box-shadow: 0 1px 0 rgba(255, 255, 255, 0.8) inset;
    -moz-box-shadow: 0 1px 0 rgba(255, 255, 255, 0.8) inset;
    -ms-box-shadow: 0 1px 0 rgba(255, 255, 255, 0.8) inset;
    -o-box-shadow: 0 1px 0 rgba(255, 255, 255, 0.8) inset;
    box-shadow: 0 1px 0 rgba(255, 255, 255, 0.8) inset;
    border: 1px solid #D69E31;
    color: #85592e;
    cursor: pointer;
    float: left;
    font: bold 15px Helvetica, Arial, sans-serif;
    height: 35px;
    margin: 20px 0 35px 15px;
    position: relative;
    text-shadow: 0 1px 0 rgba(255, 255, 255, 0.5);
    width: 120px;
  }

  #content form input[type="submit"]:hover {
    background: rgb(254, 193, 81);
    background: -moz-linear-gradient(top, rgba(254, 193, 81, 1) 0%, rgba(254, 231, 154, 1) 100%);
    background: -webkit-linear-gradient(top, rgba(254, 193, 81, 1) 0%, rgba(254, 231, 154, 1) 100%);
    background: -o-linear-gradient(top, rgba(254, 193, 81, 1) 0%, rgba(254, 231, 154, 1) 100%);
    background: -ms-linear-gradient(top, rgba(254, 193, 81, 1) 0%, rgba(254, 231, 154, 1) 100%);
    background: linear-gradient(top, rgba(254, 193, 81, 1) 0%, rgba(254, 231, 154, 1) 100%);
    /* 	filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#fec151', endColorstr='#fee79a',GradientType=0 ); */
  }

  #content form div a {
    color: #004a80;
    float: right;
    font-size: 12px;
    margin: 30px 15px 0 0;
    text-decoration: underline;
  }

  .tou {
    background: url("../assets/tou.png") no-repeat;
    width: 97px;
    height: 92px;
    position: absolute;
    top: -87px;
    left: 140px;
  }

  .left_hand {
    background: url("../assets/left_hand.png") no-repeat;
    width: 32px;
    height: 37px;
    position: absolute;
    top: -38px;
    left: 150px;
  }

  .right_hand {
    background: url("../assets/right_hand.png") no-repeat;
    width: 32px;
    height: 37px;
    position: absolute;
    top: -38px;
    right: -64px;
  }

  .initial_left_hand {
    background: url("../assets/hand.png") no-repeat;
    width: 30px;
    height: 20px;
    position: absolute;
    top: -12px;
    left: 100px;
  }

  .initial_right_hand {
    background: url("../assets/hand.png") no-repeat;
    width: 30px;
    height: 20px;
    position: absolute;
    top: -12px;
    right: -112px;
  }

  .left_handing {
    background: url("../assets/left-handing.png") no-repeat;
    width: 30px;
    height: 20px;
    position: absolute;
    top: -24px;
    left: 139px;
  }

  .right_handinging {
    background: url("../assets/right_handing.png") no-repeat;
    width: 30px;
    height: 20px;
    position: absolute;
    top: -21px;
    left: 210px;
  }


</style>
