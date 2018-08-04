<template>
  <div>
    <Menu mode="horizontal" :default-active="defaultActive" active-text-color="#de2070"
          class="mainMenu" router text-color="#333">
      <MenuItem index="article" :route="{name:'articleDustbin'}" class="menuItems"><span>文章</span></MenuItem>
      <MenuItem index="album" :route="{name:'albumDustbin'}" class="menuItems"><span>相冊</span></MenuItem>
      <MenuItem index="photo" :route="{name:'photoDustbin'}" class="menuItems"><span>照片</span></MenuItem>
    </Menu>
    <router-view style="margin-top: 10px"></router-view>
  </div>
</template>

<script>
  import {Menu, MenuItem} from "element-ui"

  export default {
    name: "Dustbin",
    components: {Menu, MenuItem},
    data() {
      return {
        defaultActive: "article"
      }
    },

  }
</script>

<style scoped>
  .menuItems {
    font-size: 18px;
  }
</style>
