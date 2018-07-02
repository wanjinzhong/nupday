<template>
  <div>
    <Menu mode="horizontal"
          background-color="#f8f8f8"
          router
          active-text-color="#f699b4"
          default-active="1">
      <MenuItem index="1" :route="{name: 'ownerLogin'}">主人</MenuItem>
      <MenuItem index="2" :route="{name: 'visitorLogin'}">访客</MenuItem>
    </Menu>
    <router-view></router-view>
  </div>
</template>

<script>
  import {Menu, MenuItem} from "element-ui";
  export default {
    name: "Login",
    components: {Menu, MenuItem},
    created() {
      this.axios.get("/api/getAllOwners").then((response) => {
        this.$store.commit("setOwners", response.data.data);
      })
    }
  }
</script>

<style scoped>

</style>
