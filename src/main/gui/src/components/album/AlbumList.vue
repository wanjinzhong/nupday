<template>
  <div>
    <AlbumItem v-for="album in albums" :key="album.id" :album="album" @reload="$emit('reload')"></AlbumItem>
    <template v-if="albums.length == 0">
      <div style="text-align: center">还没有相册哦</div>
    </template>
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
