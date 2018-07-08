<template>
  <div class="album-item" @mouseenter.self="hover = true" @mouseleave.self="hover = false" @click="$router.push('/album/' + album.id)">
    <div class="container" :style="hover ? style: ''">
      <img :src="album.coverPic != null? album.coverPic:'/static/default_cover.png'" style="max-width: 125px; max-height: 188px;"/>
      <div class="border"></div>
      <div class="counter">{{album.count}}</div>
    </div>
    <div style=" text-overflow: ellipsis;text-align: center; margin-top: 5px; width: 150px">{{album.name}}</div>
  </div>
</template>

<script>
  export default {
    name: "AlbumItem",
    props: ["album"],
    data() {
      return {
        hover: false,
        style: {
          boxShadow: '2px 2px 5px #aaa'
        }
      }
    }
  }
</script>

<style scoped>
  .album-item {
    width: 150px;
    display: inline-block;
    margin: 20px;
    cursor: pointer;
  }

  .container {
    width: 150px;
    height: 200px;
    vertical-align: middle;
    text-align: center;
    display: table-cell;
    position: relative;
  }

  .border {
    width: 150px;
    height: 200px;
    background: url('../../assets/album_border.png') no-repeat;
    background-size: 150px 200px;
    position: absolute;
    top: 0px;
    left: 0px;
  }

  .counter {
    background-color: rgba(100, 100, 100, 0.5);
    font-size: 20px;
    color: white;
    position: absolute;
    top: 9px;
    right: 12px;
    padding: 3px;
    border-radius: 5px;
  }
</style>
