<template>
  <div v-loading="loading">
    <div style="position: relative">
      <div class="title-content">{{album.name}}</div>
      <div style="margin-left: 20px; margin-top: 10px; ">{{album.description}}</div>
      <div style="position: absolute; right: 10px; top: 10px; cursor: pointer" @click="$router.push({name: 'album'})">
        <svg class="icon zan" aria-hidden="true">
          <use xlink:href="#icon-fanhui"></use>
        </svg>返回相册</div>
    </div>
    <div class="breaker"></div>
    <div v-loading="photosLoading">
      <div v-if="photos.length > 0">
        <img v-for="photo in photos" :key="photo.id" :src="photo.smallKey" class="photo"/>
      </div>
      <div v-else style="text-align: center">暂时还没有照片哦~</div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "AlbumDetail",
    data() {
      return {
        album: {},
        loading: false,
        photos: [],
        photosLoading: false,
        currentPage: 0,
        pageSize: 12,
        hasNext: true
      }
    },
    created() {
      var id = this.$route.params.albumId;
      this.loading = true;
      var that = this;
      this.axios.get("/api/album/" + id).then(res => {
        that.loading = false;
        that.album = res.data.data;
      }).catch(res => {
        that.loading = false;
      });
      this.photosLoading = true;
      this.axios.get("/api/album/" + id + "/photos", {
        params: {
          page: this.currentPage + 1,
          size: this.pageSize
        }
      }).then(res => {
        that.photosLoading = false;
        var data = res.data.data;
        that.currentPage = data.page.currentPage;
        that.hasNext = data.page.totalPages > that.currentPage;
        that.photos = data.photos;
      }).catch(res => {
        that.photosLoading = false;
      })
    }
  }
</script>

<style scoped>
  .title-content {
    vertical-align: middle;
    line-height: 32px;
    display: inline-block;
    font-size: 25px;
    color: #de2070;
    height: 32px;
  }

  .photo {
    max-width: 180px;
    max-height: 280px;
    margin: 20px;
  }

  .breaker {
    width: 100%;
    height: 2px;
    background-color: #eee;
    margin-top: 20px;
  }
</style>
