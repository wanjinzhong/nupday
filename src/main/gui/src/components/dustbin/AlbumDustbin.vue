<template>
  <AlbumList v-loading="loading" :albums="albums" @reload="refresh"></AlbumList>
</template>

<script>
  import AlbumList from "../album/AlbumList"

  export default {
    name: "AlbumDustbin",
    components: {AlbumList},
    data() {
      return {
        albums: [],
        loading: false,
      }
    },
    methods: {
      refresh() {
        this.loading = true;
        let that = this;
        let param = {
          dustbin: true
        };
        this.axios.get("/api/albums", {params: param}).then(res => {
          that.loading = false;
          that.albums = res.data.data;
        });
      }
    },
    mounted() {
      this.refresh();
    }
  }
</script>

<style scoped>

</style>
