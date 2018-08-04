<template>
  <div>
    <div style="position: relative;margin: 10px 0;"><span class="title" @click="goToArticle">{{item.title}}</span>
      <span style="position: absolute; right: 20px" v-if="item.inDustbin">
        <Tooltip content="恢复" effect="light" :open-delay="500" style="cursor: pointer">
              <svg class="icon" aria-hidden="true" @click="revert">
                <use xlink:href="#icon-chexiao"></use>
              </svg>
            </Tooltip>&nbsp;
        <Tooltip content="彻底删除" effect="light" :open-delay="500" style="cursor: pointer"
                 v-if="item.deleteUserId != currentUserId">
              <svg class="icon" aria-hidden="true" @click="forceDelete">
                <use xlink:href="#icon-shanchu"></use>
              </svg>
            </Tooltip>
      </span>
    </div>
    <div class="info">{{item.owner}}&nbsp;&nbsp;&nbsp;{{new Date(item.dateTime) | formatDate('DATETIME')}}
      &nbsp;&nbsp;
      <svg class="icon" aria-hidden="true">
        <use xlink:href="#icon-zan2"></use>
      </svg>&nbsp;{{item.likes}}
    </div>
    <div class="content" v-if="item.type=='ARTICLE'">{{item.content}}...</div>
    <div v-if="item.type=='PHOTO'">
      <img v-for="img in item.photos" :src="img" class="photo"/>
    </div>
  </div>
</template>

<script>
  import {Tooltip} from "element-ui"

  export default {
    name: "NewsItem",
    components: {Tooltip},
    props: ["item"],
    computed: {
      currentUserId() {
        return this.$store.getters.getUserId;
      }
    },
    methods: {
      goToArticle() {
        if (!this.item.inDustbin) {
          this.$router.push('/article/' + this.item.id);
        }
      },
      revert() {
        let that = this;
        this.$confirm("确定要恢复这篇文章吗？", "恢复文章", {type: "warning"}).then(() => {
          this.axios.put("/api/article/revert/" + this.item.id).then(res => {
            that.$message({message: "恢复文章成功", type: "success"});
            that.$emit("reload");
          })
        });
      },
      forceDelete() {
        let that = this;
        this.$confirm("确定要彻底删除这篇文章吗？删除后不可恢复", "删除文章", {type: "warning"}).then(() => {
          this.axios.delete("/api/article/" + this.item.id).then(res => {
            that.$message({message: "删除文章成功", type: "success"});
            that.$emit("reload");
          })
        });
      }
    }
  }
</script>

<style scoped>
  .photo {
    max-width: 120px;
    max-height: 120px;
    margin: 10px 10px;
  }

  .title {
    font-size: 20px;
    color: #555;
    font-weight: bold;
    cursor: pointer;
  }

  .title:hover {
    color: #de2070;
  }

  .content {
    margin: 20px;
    font-size: 15px;
    color: #555;
  }

  .info {
    margin-bottom: 10px;
    margin-left: 30px;
  }

</style>
