<template>
  <div style="width: 1000px">
    <div style="width: 120px; display: inline-block; vertical-align: top">
      <Menu mode="vertical" default-active="1" router>
        <MenuItem index="1" :route="{name: 'visitor'}">访问码</MenuItem>
      </Menu>
    </div>
    <div style="display: inline-block; vertical-align: top; margin-left: 30px">
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
  import {Menu, MenuItem} from "element-ui"

  export default {
    name: "Setting",
    components: {Menu, MenuItem}
  }
</script>

<style scoped>

</style>
