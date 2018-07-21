<template>
  <div class="container">
    <div style="float: left; vertical-align: top; width: 40px"><img :src="guestBook.avatar" style="width: 40px; height: 40px; border-radius: 5px"/>
    </div>
    <div style="display: inline-block; margin-left: 10px; width:calc(100% - 50px)"
         @mouseenter.self="hover = true" @mouseleave.self="hover = false">
      <div class="header"><span style="font-size: 16px; font-weight: bold">{{this.guestBook.name}}</span>
        {{new Date(this.guestBook.dateTime) | formatDate("DATETIME")}}
        <div style="float: right;" v-if="hover">
          <span style="cursor: pointer" @click="dialogVisible = true">回复</span>&nbsp;
          <span style="cursor: pointer" v-if="$store.getters.getType == 'OWNER'" @click="readyToDelete">删除</span>
        </div>
      </div>
      <div class="detail">
        <div class="content" :style="{borderBottom: '1px solid #eee'}">{{this.guestBook.content}}
        </div>
      </div>
    </div>
    <GuestBookItem v-for="child in guestBook.replies" :key="child.id" :guestBook="child"
                 :level="level + 1" style="margin-left: 50px"></GuestBookItem>
    <Dialog width="500px" :title="'回复' + guestBook.name" :visible.sync="dialogVisible">
      <AddComment target-type="COMMENT" :target-id="guestBook.id" @refresh="refresh"></AddComment>
    </Dialog>
    <Dialog title="确认删除" :visible.sync="showDeleteDialog" width="520px" v-loading="deleting">
      <span>确认要删除这个留言吗？{{guestBook.replies.length > 0 ? "包括这条留言的回复也会一起删除" :""}}</span>
      <span slot="footer" class="dialog-footer" v-loading="">
        <Button @click="showDeleteDialog = false">取消</Button>
        <Button type="danger" @click="deleteGuestBook">删除</Button>
      </span>
    </Dialog>
  </div>
</template>

<script>
  import {Dialog, Button} from 'element-ui'
  import AddComment from "./AddGuestBook";

  export default {
    name: "GuestBookItem",
    components: {AddComment, Dialog, Button},
    props: ["guestBook", "level"],
    data() {
      return {
        hover: false,
        dialogVisible: false,
        showDeleteDialog: false,
        deleting: false,
      }
    },
    methods: {
      refresh() {
        this.dialogVisible = false;
        this.$emit("refresh");
      },
      readyToDelete() {
        if (this.$store.getters.getType != 'OWNER') {
          this.$notify({
            title: '不可删除',
            message: '你没有权限删除评论',
            type: 'error'
          });
          return;
        }
        this.showDeleteDialog = true;
      },
      deleteGuestBook() {
        this.deleting = true;
        this.axios.delete("/api/comment/" + this.guestBook.id).then(res => {
          this.deleting = false;
          this.showDeleteDialog = false;
          this.$message({
            type: 'success',
            message: '评论删除留言'
          });
          this.refresh();
        });
      }
    },
  }
</script>

<style scoped>
  .detail {
    margin-left: 20px;
    margin-top: 5px;
  }

  .container {
    margin: 5px 0px;
    padding: 10px 0px;
  }
</style>
