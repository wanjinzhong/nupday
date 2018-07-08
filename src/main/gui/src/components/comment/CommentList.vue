<template>
  <div style="margin-top: 40px">
    <div class="comment-title">你们的祝福</div>
    <div v-loading="commentsLoading">
      <CommentItem v-if="comments.length > 0" v-for="comment in comments" :key="comment.id" :comment="comment"
                   :root-type="targetType" :root-id="targetId" level=1></CommentItem>
    </div>
    <div v-if="comments.length == 0">还没有评论哦~去评论一个吧</div>
  </div>
</template>

<script>
  import CommentItem from "./CommentItem"

  export default {
    name: "CommentList",
    components: {CommentItem},
    props: ["targetId", "targetType"],
    computed: {
      comments() {
        return this.$store.getters.getComments;
      },
      commentsLoading() {
        return this.$store.getters.getCommentsLoading;
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
