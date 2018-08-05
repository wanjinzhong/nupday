<template>
  <div class="album-item" @mouseenter.self="hover = true" @mouseleave.self="hover = false" @click="goToAlbum">
    <div class="container" :style="hover ? style: ''">
      <img :src="album.coverPic != null? album.coverPic:'/static/default_cover.png'" style="max-width: 125px; max-height: 188px;"/>
      <div class="border"></div>
      <div class="counter">{{album.count}}</div>
      <div class="opt" v-if="album.deleteUserId != null">
        <Tooltip content="恢复" effect="light" :open-delay="500" style="cursor: pointer">
          <svg class="icon" aria-hidden="true" @click="revert">
            <use xlink:href="#icon-chexiao"></use>
          </svg>
        </Tooltip>&nbsp;
        <Tooltip content="彻底删除" effect="light" :open-delay="500" style="cursor: pointer"
                 v-if="album.deleteUserId != currentUserId">
          <svg class="icon" aria-hidden="true" @click="forceDelete">
            <use xlink:href="#icon-shanchu"></use>
          </svg>
        </Tooltip>
      </div>
    </div>
    <div style=" text-overflow: ellipsis;text-align: center; margin-top: 5px; width: 150px">{{album.name}}</div>
  </div>
</template>

<script>
  import {Tooltip} from "element-ui"
  export default {
    name: "AlbumItem",
    props: ["album"],
    components: {Tooltip},
    data() {
      return {
        hover: false,
        style: {
          transform: 'scale(1.05)',
          boxShadow: '5px 5px 5px rgba(200,200,200,0.4)'
        }
      }
    },
    computed: {
      currentUserId() {
        return this.$store.getters.getUserId;
      }
    },
    methods: {
      goToAlbum() {
        if (this.album.deleteUserId == null){
          this.$router.push('/album/' + this.album.id)
        }
      },
      revert() {
        let that = this;
        this.$confirm("确定要恢复这个相册吗？", "恢复相册", {type: "warning"}).then(() => {
          this.axios.put("/api/album/revert/" + this.album.id).then(res => {
            that.$message({message: "恢复相册成功", type: "success"});
            that.$emit("reload");
          })
        });
      },
      forceDelete() {
        let that = this;
        this.$confirm("确定要彻底删除这个相册吗？删除后不可恢复", "删除相册", {type: "warning"}).then(() => {
          this.axios.delete("/api/album/" + this.album.id).then(res => {
            that.$message({message: "删除相册成功", type: "success"});
            that.$emit("reload");
          })
        });
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
    transition: all 0.3s;
    box-shadow: 3px 3px 3px rgba(200,200,200,0.6);
  }

  .opt {
    background-color: rgba(100, 100, 100, 0.5);
    font-size: 18px;
    color: white;
    position: absolute;
    top: 9px;
    left: 12px;
    padding: 3px;
    border-radius: 5px;
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
