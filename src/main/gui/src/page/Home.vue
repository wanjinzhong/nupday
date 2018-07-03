<template>
<div style="height: 100%;">
  <Header></Header>
  <Banner></Banner>
</div>
</template>

<script>
  import Header from "../components/Header"
  import Banner from "../components/Banner"
    export default {
        name: "Home",
      components: {Header, Banner},
      created() {
        this.axios.get("/api/currentUser").then((response) => {
          this.$store.commit("setLoginInfo", response.data.data);
        });
        this.axios.get("/api/homeBackground").then((response) => {
          this.$store.commit("setHomeBackground", response.data.data);
        })
      }
    }
</script>

<style scoped>

</style>
