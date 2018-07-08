<template>
  <div id="container"
       :style="{background:'url(' + homeBackground + ')  0% 0% / cover  no-repeat'}">
    <div id="sub" :style="{background:'url(' + homeBackground + ')  0% 0% / cover  no-repeat'}"></div>
    <div id="content">
      <Menu mode="horizontal" :default-active="defaultActive" background-color="transparent" active-text-color="#de2070"
            class="mainMenu" router text-color="#333">
        <MenuItem index="home" :route="{name:'home'}" class="menuItems"><span>主页</span></MenuItem>
        <MenuItem index="article" class="menuItems"><span>文章</span></MenuItem>
        <MenuItem index="album" class="menuItems" :route="{name:'album'}"><span>相册</span></MenuItem>
        <MenuItem index="guestBook" class="menuItems"><span>留言</span></MenuItem>
        <MenuItem index="memorialDay" class="menuItems"><span>纪念日</span></MenuItem>
        <Button plain v-if="$store.getters.getType == 'OWNER'" type="danger" size="small" round
                style="margin-left: 10px" @click="$router.push({name: 'newArticle'})">发文章
        </Button>
      </Menu>
      <Menu mode="horizontal" background-color="transparent" active-text-color="#de2070"
            class="mainMenu" text-color="#333" style="position: absolute; top: 0; right: 10px"
            v-if="$store.getters.getType == 'OWNER'">
        <Submenu index="owner" style="position: absolute; top: 0px; right:10px">
          <template slot="title"><span class="menuItems">{{$store.getters.getName}}</span></template>
          <MenuItem index="personal" class="secondLevelMenuItem">
            <svg class="icon" aria-hidden="true">
              <use xlink:href="#icon-geren2"></use>
            </svg>
            个人中心
          </MenuItem>
          <MenuItem index="setting" class="secondLevelMenuItem">
            <svg class="icon" aria-hidden="true">
              <use xlink:href="#icon-shezhi"></use>
            </svg>
            设置
          </MenuItem>
          <MenuItem index="logout" class="secondLevelMenuItem">
            <div @click="logout">
              <svg class="icon" aria-hidden="true">
                <use xlink:href="#icon-084tuichu"></use>
              </svg>
              退出登陆
            </div>
          </MenuItem>
        </Submenu>
      </Menu>

    </div>
  </div>
</template>

<script>
  import {Button, Menu, MenuItem, Submenu} from "element-ui"

  export default {
    name: "Header",
    components: {Menu, Submenu, MenuItem, Button},
    data() {
      return {
        defaultActive: 'home'
      }
    },
    computed: {
      homeBackground() {
        return this.$store.getters.getHomeBackground;
      }
    },
    methods: {
      logout() {
        var that = this;
        this.axios.get("/api/logout").then(res => {
          that.$router.push("/login");
        })
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

  .menuItems {
    font-size: 18px;
    height: 50px;
  }

  .secondLevelMenuItem {
    font-size: 16px;
  }

  .el-menu-item:not(.is-disabled):hover, .is-opened, .el-submenu__title:hover {
    background-color: rgba(255, 255, 255, 0.2) !important;
  }
</style>
