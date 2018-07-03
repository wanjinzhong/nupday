<template>
    <div id="banner" :style="{background:'url(' + homeBackground + ')  0% 0% / cover  no-repeat'}">
      <span>Home</span>
    </div>
</template>

<script>
    export default {
        name: "Banner",
      computed: {
          homeBackground() {
            return this.$store.getters.getHomeBackground;
          }
      }
    }
</script>

<style scoped>
  #banner {
    /*margin-top: 40px;*/
    padding-top: 60px;
    width: 100%;
    height: 500px;
  }
</style>
