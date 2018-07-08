<template>
  <div class="container">
    <div style="float: left; vertical-align: top; width: 40px"><img :src="comment.avatar"
                                                                    style="width: 40px; height: 40px; border-radius: 5px"/>
    </div>
    <div style="display: inline-block; margin-left: 10px; width:calc(100% - 50px)"
         @mouseenter.self="hover = true" @mouseleave.self="hover = false">
      <div class="header"><span style="font-size: 16px; font-weight: bold">{{this.comment.name}}</span> {{new
        Date(this.comment.dateTime) | formatDate("DATETIME")}}
        <div style="float: right;" v-if="hover">
          <span style="cursor: pointer" @click="dialogVisible = true">回复</span>&nbsp;
          <span style="cursor: pointer" v-if="$store.getters.getType == 'OWNER'" @click="readyToDelete">删除</span>
        </div>
      </div>
      <div class="detail">
        <div class="content" :style="{borderBottom: '1px solid #eee'}">{{this.comment.content}}
        </div>
      </div>
    </div>
    <CommentItem v-for="child in comment.replies" :key="child.id" :comment="child"
                 :level="level + 1" style="margin-left: 50px"
                 :root-type="rootType" :root-id="rootId"></CommentItem>
    <Dialog :title="'回复' + comment.name" :visible.sync="dialogVisible">
      <AddComment target-type="COMMENT" :target-id="comment.id" @refresh="refresh"></AddComment>
    </Dialog>
    <Dialog title="确认删除" :visible.sync="showDeleteDialog" width="520px">
      <span>确认要删除这个评论吗？{{comment.replies.length > 0 ? "包括这条评论的回复也会一起删除" :""}}</span>
      <span slot="footer" class="dialog-footer" v-loading="">
        <Button @click="showDeleteDialog = false">取消</Button>
        <Button type="danger" @click="deleteComment">删除</Button>
      </span>
    </Dialog>
  </div>
</template>

<script>
  import {Dialog, Button} from 'element-ui'
  import AddComment from "./AddComment";

  export default {
    name: "CommentItem",
    components: {AddComment, Dialog, Button},
    props: ["comment", "level", "rootType", "rootId"],
    data() {
      return {
        hover: false,
        dialogVisible: false,
        showDeleteDialog: false
      }
    },
    methods: {
      refresh() {
        this.$store.commit("setCommentsLoading", true);
        this.axios.get("/api/comment", {
          params: {targetType: this.rootType, targetId: this.rootId}
        }).then(res => {
          this.dialogVisible = false;
          this.$store.commit("setComments", res.data.data);
          this.$store.commit("setCommentsLoading", false);
        })
      },
      readyToDelete() {
        if (this.$store.getters.getType != 'OWNER') {
          this.$notify({
            title: '不可删除',
            message: '你没有权限删除这篇文章',
            type: 'error'
          });
          return;
        }
        this.showDeleteDialog = true;
      },
      deleteComment() {
        this.axios.delete("/api/comment/" + this.comment.id).then(res => {
          this.showDeleteDialog = false;
          this.$message({
            type: 'success',
            message: '评论删除成功'
          });
          this.refresh();
        });
      }
    }
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
