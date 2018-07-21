<template>
  <div style="margin-top: 40px">
    <div class="guestBook-title">你们的祝福</div>
    <div v-loading="guestBooksLoading">
      <GuestBookItem v-if="guestBooks.length > 0" v-for="guestBook in guestBooks" :key="guestBook.id" :guestBook="guestBook"
                    level=1 @refresh="$emit('refresh')"></GuestBookItem>
    </div>
    <div v-if="$store.getters.getHasNext" style="cursor: pointer; text-align: center">
      <Button @click="$emit('loadMore')" :disabled="guestBooksLoading">点击加载更多</Button>
    </div>
    <div v-if="guestBooks.length == 0">还没有评论哦~去评论一个吧</div>
  </div>
</template>

<script>
  import GuestBookItem from "./GuestBookItem"
  import {Button} from "element-ui"

  export default {
    name: "GuestBookList",
    components: {GuestBookItem, Button},
    computed: {
      guestBooks() {
        return this.$store.getters.getGuestBooks;
      },
      guestBooksLoading() {
        return this.$store.getters.getGuestBooksLoading;
      }
    },
  }
</script>

<style scoped>
  .guestBook-title {
    font-size: 25px;
    color: #de2070;
    margin-bottom: 20px;
  }
</style>
