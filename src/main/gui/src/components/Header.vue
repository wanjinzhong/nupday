<template>
    <div id="container"
         :style="{background:'url(' + homeBackground + ')  0% 0% / cover  no-repeat'}">
      <div id="sub" :style="{background:'url(' + homeBackground + ')  0% 0% / cover  no-repeat'}"></div>
      <div id="content">
        菜单
      </div>
    </div>
</template>

<script>
  export default {
    name: "Header",
    computed: {
      homeBackground() {
        return this.$store.getters.getHomeBackground;
      }
    }
  }
</script>

<style scoped>

  #container {
    width: 100%;
    height: 50px;
    line-height: 50px;
    vertical-align: center;
    position: fixed;
    /*background: url(../assets/bg3.jpg) center top;*/
    /*background-size: cover;*/
    top: -8px;
    left: 0;
    border-radius: 8px;
    box-shadow: 0 3px 2px rgba(0, 0, 0, 0.2);
    overflow: hidden;
    z-index: 2;
    box-sizing: border-box;
    margin-top: 5px;
  }

  #sub {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    -webkit-filter: blur(20px);
    -moz-filter: blur(20px);
    -ms-filter: blur(20px);
    -o-filter: blur(20px);
    filter: blur(20px);
    z-index: -3;
    margin: -30px;
    /*background: url(../assets/bg3.jpg) center top;*/
    /*background-size: cover;*/
    /*background-attachment: fixed;*/
  }
  #content {
    margin-left: 10px;
  }
</style>
