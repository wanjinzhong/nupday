<template>
  <div>
    <AlbumItem v-for="album in albums" :key="album.id" :album="album"></AlbumItem>
  </div>
</template>

<script>
  import AlbumItem from './AlbumItem'
  export default {
    name: "AlbumList",
    components:{AlbumItem},
    props: ["albums"]
  }
</script>

<style scoped>

</style>
