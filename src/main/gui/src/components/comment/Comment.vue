<template>
  <div>
    <div style="padding: 20px 50px; margin: 0 auto; border: 2px solid #eee; border-radius: 30px"
    v-if="commentable">
      <div class="comment-title">献上祝福</div>
      <AddComment :target-type="targetType" :target-id="targetId" @refresh="refresh"></AddComment>
    </div>
    <CommentList :target-type="targetType" :target-id="targetId"
                 style="width: 1000px; margin: 20px auto;"></CommentList>
  </div>
</template>

<script>
  import AddComment from './AddComment'
  import CommentList from './CommentList'

  export default {
    name: "Comment",
    components: {AddComment, CommentList},
    props: ["targetId", "targetType", "commentable"],
    created() {
      this.$store.commit("setComments", []);
      this.refresh();
    },
    methods: {
      refresh() {
        this.$store.commit("setCommentsLoading", true);
        this.axios.get("/api/comment", {
          params: {targetType: this.targetType, targetId: this.targetId}
        }).then(res => {
          this.$store.commit("setComments", res.data.data);
          this.$store.commit("setCommentsLoading", false);
        })
      }
    }
  }
</script>

<style scoped>

  .comment-title {
    font-size: 25px;
    color: #de2070;
    margin-bottom: 20px;
  }
</style>
