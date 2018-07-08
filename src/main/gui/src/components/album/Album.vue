<template>
  <div style="width: 1100px">
    <div class="title">
      <div class="title-content">相册</div>
      <Button plain v-if="$store.getters.getType == 'OWNER'" type="danger" size="small" round
              style="margin-left: 10px" @click="showAddDialog = true">新建相册
      </Button>
    </div>
    <Dialog title="添加相册" :visible.sync="showAddDialog" width="500px" v-loading="saving">
      <Form @submit.native.prevent lebel-position="left" label-width="50px">
        <FormItem label="标题">
          <Input v-model="name"/>
        </FormItem>
        <FormItem label="描述">
          <Input v-model="description" type="textarea"/>
        </FormItem>
        <FormItem label="权限">
          <el-switch style="margin-bottom: 20px;" v-model="isOpen" active-text="开放" inactive-text="隐私"></el-switch>
          <div class="break"></div>
          <el-switch style="margin-bottom: 20px;" v-model="commentable" active-text="可评论"
                     inactive-text="禁止评论"></el-switch>
        </FormItem>
        <div style="text-align: right">
          <Button @click="showAddDialog = false">关闭</Button>
          <Button @click="createAlbum" type="primary">创建</Button>
        </div>
      </Form>
    </Dialog>
    <AlbumList v-loading="loading" :albums="albums"></AlbumList>
  </div>
</template>

<script>
  import AlbumList from './AlbumList'
  import {Button, Dialog, Form, FormItem, Input} from "element-ui"

  export default {
    name: "Album",
    components: {AlbumList, Button, Input, Dialog, Form, FormItem},
    data() {
      return {
        albums: [],
        loading: false,
        showAddDialog: false,
        name: '',
        description: '',
        isOpen: true,
        commentable: true,
        saving: false
      }
    },
    methods: {
      createAlbum() {
        if (this.name.trim() == '') {
          this.$message({
            type: 'error',
            message: '给相册取个名字吧'
          });
          return;
        }
        var data = {
          name: this.name,
          description: this.description,
          isOpen: this.isOpen,
          commentable: this.commentable
        };
        this.saving = true;
        var that = this;
        this.axios.post("/api/album", data).then(res => {
          that.saving = false;
          that.showAddDialog = false;
          that.refresh();
        }).catch(res => {
          that.saving = false;
        })
      },
      refresh() {
        this.loading = true;
        var that = this;
        this.axios.get("/api/albums").then(res => {
          that.loading = false;
          that.name = '';
          that.description = '';
          that.isOpen = true;
          that.commentable = true;
          that.albums = res.data.data;
        });
      }
    },
    created() {
      this.refresh();
    }
  }
</script>
<style scoped>
  .title {
    margin-bottom: 20px;
  }

  .title-content {
    vertical-align: middle;
    line-height: 32px;
    display: inline-block;
    font-size: 25px;
    color: #de2070;
    height: 32px;
  }

  .break {
    margin: 5px 10px;
    width: 1px;
    background-color: #ccc;
    height: 20px;
    display: inline-block;
  }
</style>
