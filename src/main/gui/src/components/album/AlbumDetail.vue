<template>
  <div v-loading="loading">
    <div @mouseover="hover=true" @mouseleave="hover = false" style="position: relative">
      <div class="title-content">{{album.name}}</div>&nbsp;&nbsp;
      <template v-if="$store.getters.getType == 'OWNER'">
        <svg class="icon editBtn" aria-hidden="true" v-if="hover" @click="showEditDialog">
          <use xlink:href="#icon-ai-edit"></use>
        </svg>
        <svg class="icon editBtn" aria-hidden="true" v-if="hover" @click="deleteAlbum">
          <use xlink:href="#icon-shanchu"></use>
        </svg>
      </template>
      <div style="margin-left: 20px; margin-top: 10px; ">{{album.description}}</div>
      <div style="position: absolute; right: 10px; top: 10px; cursor: pointer">
        <Button type="primary" @click="showUploadDialog = true" size="small" v-if="$store.getters.getType == 'OWNER'">
          <i class="el-icon-upload el-icon--right"></i>
          上传照片
        </Button>
        <svg class="icon zan" aria-hidden="true">
          <use xlink:href="#icon-fanhui"></use>
        </svg>
        <span @click="$router.push({name: 'album'})">返回相册</span>
      </div>
    </div>
    <div class="breaker"></div>
    <div v-loading="photosLoading">
      <div v-if="photos.length > 0" style="position: relative;">
        <div @mouseover="optBtnId = photo.id" @mouseout="optBtnId = 0" class="photoContainer" v-for="photo in photos"
             style="display: inline-block;">
          <div class="optHeader" v-if="optBtnId == photo.id && $store.getters.getType == 'OWNER'">
            <svg class="icon" aria-hidden="true" @click="setCover(photo.id)">
              <use :xlink:href="photo.isCover?'#icon-msnui-home-block':'#icon-zhuye'"></use>
            </svg>&nbsp;
            <svg class="icon" aria-hidden="true" @click="deletePhoto(photo.id)">
              <use xlink:href="#icon-shanchu"></use>
            </svg>
          </div>
          <div>
            <img :key="photo.id" :src="photo.smallKey" class="photo"/>
          </div>
        </div>
      </div>
      <div v-else style="text-align: center;margin-top: 20px">暂时还没有照片哦~</div>
      <div v-if="hasNext" style="cursor: pointer; text-align: center">
        <Button @click="loadPic" :disabled="photosLoading" v-loading="photosLoading">点击加载更多</Button>
      </div>
    </div>
    <Comment target-type="ALBUM" :target-id="$route.params.albumId" :commentable="album.commentable"
             style="margin-top: 30px"></Comment>
    <Dialog title="修改相册" :visible.sync="isShowEditDialog" width="500px" v-loading="saving">
      <Form @submit.native.prevent lebel-position="left" label-width="50px">
        <FormItem label="标题">
          <Input v-model="name"/>
        </FormItem>
        <FormItem label="描述">
          <Input v-model="description" type="textarea"/>
        </FormItem>
        <FormItem label="权限">
          <el-switch style="margin-bottom: 20px;" v-model="isOpen" active-text="开放" inactive-text="隐私"></el-switch>
          <div
            style="  margin: 5px 10px;width: 1px; background-color: #ccc; height: 20px; display: inline-block;"></div>
          <el-switch style="margin-bottom: 20px;" v-model="commentable" active-text="可评论"
                     inactive-text="禁止评论"></el-switch>
        </FormItem>
        <div style="text-align: right">
          <Button @click="isShowEditDialog = false">关闭</Button>
          <Button @click="saveAlbum" type="primary">保存</Button>
        </div>
      </Form>
    </Dialog>
    <Dialog title="上传照片" :visible.sync="showUploadDialog" @close="closeDialog">
      <Upload :action="'/api/album/' + $route.params.albumId + '/photo'" ref="upload"
              multiple with-credentials list-type="picture-card" accept="image/*" :on-success="uploadSuccess">
      </Upload>
      <div style="text-align: right">
        <Button type="primary" style="margin-top: 20px;" @click="closeDialog">完成</Button>
      </div>
    </Dialog>
  </div>
</template>

<script>
  import {Button, Dialog, Form, FormItem, Input, Switch, Upload} from 'element-ui'
  import Comment from "../comment/Comment"

  export default {
    name: "AlbumDetail",
    components: {Dialog, Form, FormItem, Input, Button, Switch, Comment, Upload},
    data() {
      return {
        album: {},
        loading: false,
        photos: [],
        photosLoading: false,
        currentPage: 0,
        pageSize: 20,
        hasNext: true,
        hover: false,
        isShowEditDialog: false,
        saving: false,
        isOpen: true,
        commentable: true,
        name: '',
        description: '',
        showDeleteDialog: false,
        deleting: false,
        showUploadDialog: false,
        uploaded: false,
        optBtnId: 0,
        showDeletePhotoDialog: false
      }
    },
    methods: {
      saveAlbum() {
        if (this.name.trim() == '') {
          this.$message({
            type: 'error',
            message: '给相册取个名字吧'
          });
          return;
        }
        var data = {
          id: this.album.id,
          name: this.name,
          description: this.description,
          isOpen: this.isOpen,
          commentable: this.commentable
        };
        this.saving = true;
        var that = this;
        this.axios.put("/api/album", data).then(res => {
          that.saving = false;
          that.isShowEditDialog = false;
          that.$message({
            type: 'success',
            message: "更新相册成功"
          });
          that.refreshAlbumInfo(that.album.id);
        }).catch(res => {
          that.saving = false;
        })
      },
      refreshAlbumInfo(id) {
        var that = this;
        this.loading = true;
        this.axios.get("/api/album/" + id).then(res => {
          that.loading = false;
          that.album = res.data.data;
        }).catch(res => {
          that.loading = false;
        });
      },
      showEditDialog() {
        this.name = this.album.name;
        this.description = this.album.description;
        this.isOpen = this.album.isOpen;
        this.commentable = this.album.commentable;
        this.isShowEditDialog = true;
      },
      deleteAlbum() {
        var that = this;
        this.$confirm("系统将暂时把这个相册和其中的所有照片放入回收站，等对方确认后彻底删除。确定放入回收站吗？", "确认删除",{type: "warning"})
          .then(() => {
            this.deleting = true;
            this.axios.delete("/api/album/" + this.album.id).then(res => {
              that.deleting = false;
              that.$message({
                type: "success",
                message: "删除相册成功"
              });
              that.$router.push("/albums");
            }).catch(res => {
              that.deleting = false;
            })
          });
      },
      loadPic() {
        var that = this;
        this.photosLoading = true;
        this.axios.get("/api/album/" + this.$route.params.albumId + "/photos", {
          params: {
            page: that.currentPage + 1,
            size: that.pageSize
          }
        }).then(res => {
          that.photosLoading = false;
          var data = res.data.data;
          that.currentPage = data.page.currentPage;
          that.hasNext = data.page.totalPages > that.currentPage;
          for (var i in data.photos) {
            that.photos.push(data.photos[i]);
          }
        }).catch(res => {
          that.photosLoading = false;
        })
      },
      closeDialog() {
        this.currentPage = 0;
        if (this.uploaded) {
          this.photos = [];
          this.loadPic();
          this.uploaded = false;
        }
        this.showUploadDialog = false;
        this.$refs.upload.clearFiles();
      },
      uploadSuccess() {
        this.uploaded = true;
      },
      setCover(id) {
        var that = this;
        this.axios.put("/api/album/" + this.$route.params.albumId + "/cover/photo/" + id).then(res => {
          for (var i in that.photos) {
            that.photos[i].isCover = that.photos[i].id == id;
          }
        })
      },
      readyToDelete(id) {
        this.showDeletePhotoDialog = true;
        this.deletePhotoId = id;
      },
      deletePhoto(id) {
        let that = this;
        this.$confirm("系统将会暂时把这张照片放到回收站，等对方确认后彻底删除。确定放入回收站吗？", "确认删除",{type: "warning"})
          .then(() => {
            this.deleting = true;
            this.axios.delete("/api/photo/" + id).then(res => {
              that.$message({
                type: "success",
                message: "删除照片成功"
              });
              that.deleting = false;
              this.currentPage = 0;
              this.photos = [];
              that.loadPic();
              this.showDeletePhotoDialog = false;
            }).catch(res => {
              that.deleting = false;
              this.showDeletePhotoDialog = false;
            });
          });
      }
    },
    created() {
      var id = this.$route.params.albumId;
      this.photosLoading = true;
      this.refreshAlbumInfo(id);
      this.loadPic();
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
  }

  .photoContainer {
    margin: 20px;
    box-shadow: 3px 3px 3px rgba(200, 200, 200, 0.6);
    transition: all 0.3s;
    cursor: pointer;
  }

  .photoContainer:hover {
    transform: scale(1.05);
    box-shadow: 5px 5px 5px rgba(200, 200, 200, 0.4);
  }

  .breaker {
    width: 100%;
    height: 2px;
    background-color: #eee;
    margin-top: 20px;
  }

  .editBtn {
    cursor: pointer;
  }

  .optHeader {
    position: absolute;
    background-color: rgba(170, 170, 170, 0.5);
    height: 20px;
    color: white;
    padding: 5px;
    font-size: 18px;
    border-radius: 3px;
  }
</style>
