<template>
  <div id="banner" :style="{background:'url(' + $store.getters.getHomeBackground + ')  0% 0% / cover  no-repeat'}">
    <div id="content" style="width: 1400px; text-align: center; margin: 0 auto">
      <div class="owner-container">
        <div class="owner-container-left">
          <img :src="owners[0].avatar" class="avatar avatar-left"/>
          <span class="name name-left">{{owners[0].name}}</span>
        </div>
        <div style="display: inline-block">
          <svg class="icon xin" aria-hidden="true">
            <use xlink:href="#icon-xin"></use>
          </svg>
        </div>
        <div class="owner-container-right">
          <span class="name name-left">{{owners[1].name}}</span>
          <img :src="owners[1].avatar" class="avatar avatar-right"/>
        </div>
        <div class="loveDaysContent">我们{{memorialDay.title}}
          <Popover
          placement="bottom"
          :content="memorialDay.detailDays"
          trigger="hover"
          :open-delay="500">
            <span class="loveDays" slot="reference">{{memorialDay.days}}</span>
          </Popover>
          天啦！
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {Button, Menu, MenuItem, Submenu, Popover} from "element-ui"

  export default {
    name: "Banner",
    components: {Menu, MenuItem, Button, Submenu, Popover},
    data() {
      return {
        memorialDay: {},
        owners: [
          {id: 0, name: "", avatar: ""},
          {id: 0, name: "", avatar: ""}
        ],
      }
    },
    created() {
      this.axios.get("/api/allOwners").then(res => {
        this.owners = res.data.data;
        this.$store.commit("setOwners", res.data.data);
      });
      this.axios.get("/api/homeMemorialDay").then(res => {
        this.memorialDay = res.data.data;
      });
    }
  }
</script>

<style scoped>
  #banner {
    /*margin-top: 40px;*/
    padding-top: 60px;
    width: 100%;
    height: 400px;
    text-align: center;
  }

  .loveDaysContent {
    font-size: 50px;
    margin-top: 30px;
  }

  .loveDays {
    font-size: 70px;
    color: #f00;
  }

  .name {
    font-size: 60px;
    font-weight: bold;
    line-height: 200px;
  }

  .avatar {
    height: 190px;
    width: 190px;
    border-radius: 95px;
    margin-top: 5px;
  }

  .avatar-left {
    float: left;
    margin-left: 5px;
  }

  .avatar-right {
    margin-right: 5px;
    float: right;
  }

  .owner-container {
    margin: 20px auto 0;
  }

  .xin {
    font-size: 200px;
    color: red;
    line-height: 200px;
    padding-top: 50px;
    margin-bottom: -50px;
    -webkit-animation-name: scaleDraw; /*关键帧名称*/
    -webkit-animation-timing-function: ease-in-out; /*动画的速度曲线*/
    -webkit-animation-iteration-count: infinite; /*动画播放的次数*/
    -webkit-animation-duration: 3s;
  }

  .owner-container-left, .owner-container-right {
    width: 480px;
    height: 200px;
    background-color: rgba(255, 255, 255, 0.5);
    border-radius: 100px;
    display: inline-block;
  }

  @keyframes scaleDraw { /*定义关键帧、scaleDrew是需要绑定到选择器的关键帧名称*/
    0% {
      transform: scale(1); /*开始为原始大小*/
    }
    25% {
      transform: scale(1.1); /*放大1.1倍*/
    }
    50% {
      transform: scale(1);
    }
    75% {
      transform: scale(1.1);
    }
  }
</style>
