<template>
  <div v-loading="loading">
    <Form label-position="left" label-width="80px" @submit.native.prevent>
      <FormItem label="姓名">
        <Input v-model="name" :disabled="!edit"/>
      </FormItem>
      <FormItem label="头像">
        <Upload
          action="/api/avatar"
          :show-file-list="false"
          :on-success="reloadAvatar"
          with-credentials
          :disabled="!edit" v-loading="avatarLoading">
          <img v-if="avatar" :src="avatar" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </Upload>
      </FormItem>
      <FormItem label="生日">
        <DatePicker :disabled="!edit" type="date" v-model="birthday"></DatePicker>
      </FormItem>
      <FormItem label="手机">
        <Input :disabled="!edit" v-model="phone"/>
      </FormItem>
      <FormItem label="邮箱">
        <Input :disabled="!edit" v-model="email"/>
      </FormItem>
      <FormItem label="性别">
        <Radio :disabled="!edit" border v-model="male" :label="true">男</Radio>
        <Radio :disabled="!edit" border v-model="male" :label="false">女</Radio>
      </FormItem>
      <div>
        <Button v-if="!edit" @click="edit = true" type="primary">编辑</Button>
        <Button v-if="edit" @click="save" type="primary">保存</Button>
      </div>
    </Form>

  </div>
</template>

<script>
  import {Form, FormItem, Button, Input, DatePicker, Upload, Radio} from 'element-ui'
  import {formatDate} from "../../utils/TimeFormater"

  export default {
    name: "MyInfo",
    components: {Form, FormItem, Button, Input, DatePicker, Upload, Radio},
    data() {
      return {
        name: '',
        avatar: '',
        birthday: '',
        phone: '',
        email: '',
        male: true,
        edit: false,
        loading: false,
        avatarLoading: false
      }
    },
    created() {
      this.reload();
    },
    methods: {
      reload() {
        this.loading = true;
        var that = this;
        this.axios.get("/api/myInfo").then(res => {
          var data = res.data.data;
          that.name = data.name;
          that.avatar = data.avatar;
          that.birthday = data.birthday;
          that.phone = data.phone;
          that.email = data.email;
          that.male = data.male;
          that.loading = false;
        }).catch(res => {
          that.loading = false;
        })
      },
      reloadAvatar() {
        var that = this;
        this.avatarLoading = true;
        this.axios.get("/api/myAvatar").then(res => {
          that.avatar = res.data.data;
          this.avatarLoading = false;
          this.reloadOwners();
        }).catch(res => {
          this.avatarLoading = false;
        })
      },
      reloadOwners() {
        this.axios.get("/api/allOwners").then(res => {
          this.$store.commit("setOwners", res.data.data);
        });
      },
      save() {
        var data = {
          name: this.name,
          avatar: this.avatar,
          birthday: formatDate(new Date(this.birthday), 'DATE'),
          phone: this.phone,
          email: this.email,
          male: this.male
        };
        this.loading = true;
        var that = this;
        this.axios.put("/api/myInfo", data).then(res => {
          that.edit = false;
          that.loading = false;
          that.reload();
          this.reloadOwners();
        }).catch(res => {
          that.loading = false;
        })
      }
    }
  }
</script>

<style scoped>
  .avatar {
    width: 200px;
    height: 200px;
    border-radius: 100px;
  }
</style>
