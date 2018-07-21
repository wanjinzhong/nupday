<template>
  <div>
    <div style="padding: 20px 50px; margin: 0 auto; border: 2px solid #eee; border-radius: 30px">
      <div class="comment-title">留言</div>
      <AddGuestBook target-type="GUEST_BOOK" :target-id="0" @refresh="refresh"></AddGuestBook>
    </div>
    <GuestBookList style="width: 1000px; margin: 20px auto; " @loadMore="loadMore" @refresh="refresh"></GuestBookList>
  </div>
</template>

<script>
  import AddGuestBook from './AddGuestBook'
  import GuestBookList from './GuestBookList'

  export default {
    name: "GuestBook",
    components: {AddGuestBook, GuestBookList},
    data() {
      return {
        currentPage: 0,
        pageSize: 20,
        hasNext: true,
      }
    },
    created() {
      this.$store.commit("setGuestBooks", []);
      this.refresh();
    },
    methods: {
      loadMore() {
        this.currentPage ++;
        this.load();
      },
      load() {
        this.$store.commit("setGuestBooksLoading", true);
        this.axios.get("/api/guestBook", {
          params: {page: this.currentPage, size: this.pageSize}
        }).then(res => {
          var data = res.data.data;
          var guestBooks = this.$store.getters.getGuestBooks;
          for (var i in data.data) {
            guestBooks.push(data.data[i]);
          }
          this.$store.commit("setGuestBooks", guestBooks);
          this.$store.commit("setGuestBooksLoading", false);
          this.$store.commit("setHasNext", data.page.totalPages > this.currentPage)
        })
      },
      refresh() {
        this.$store.commit("setGuestBooks", []);
        this.currentPage = 1;
        this.load();
      }
    }
  }
</script>

<style scoped>

</style>
