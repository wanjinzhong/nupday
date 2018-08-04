<template>
  <div style="text-align: left">
    <Form label-position="left" label-width="80px">
      <FormItem label="用户">
        <Select v-model="selectedOwner" style="width: 300px" size="normal">
          <Option v-for="owner in allOwners"
                  :key="owner.id"
                  :label="owner.name"
                  :value="owner.id"></Option>
        </Select>
      </FormItem>
      <FormItem label="邮箱">
        <div style="display: inline-block; width: 70%">
          <Input v-model="email" size="normal" width="100px"/>
        </div>
        <Button type="primary" size="normal" width="30%" @click="getValidateCode" :disabled="getValidateCodeDisabled">
          {{getValidateCodeBtn}}
        </Button>
      </FormItem>
      <FormItem label="验证码">
        <Input v-model="code" size="normal"/>
      </FormItem>
      <FormItem label="新密码">
        <Input v-model="newPwd" type="password" size="normal"/>
      </FormItem>
      <FormItem label="确认密码">
        <Input v-model="confirmPwd" type="password" size="normal"/>
      </FormItem>
    </Form>
    <div style="text-align: right">
      <Button size="normal" @click="cleanData();$emit('close')">取消</Button>
      <Button type="primary" size="normal" @click="savePwd">保存</Button>
    </div>
  </div>
</template>

<script>
  import {Button, Input, Select, Option, Form, FormItem} from "element-ui";

  export default {
    name: "ChangePassword",
    components: {Input, Button, Select, Option, Form, FormItem},
    props: ["id"],
    data() {
      return {
        selectedOwner: 1,
        code: "",
        email: "",
        newPwd: "",
        confirmPwd: "",
        getValidateCodeDisabled: false,
        cutdownCount: 60,
        getValidateCodeBtn: "获取验证码",
        validationCodeTimeInterval: {}
      }
    },
    watch: {
      id(val, oldVal) {
        this.selectedOwner = val;
      }
    },
    computed: {
      allOwners() {
        return this.$store.getters.getOwners;
      }
    },
    methods: {
      getValidateCode() {
        if (this.email.trim() == "") {
          this.$message({message: "请输入邮箱", type: "warning"});
          return;
        }
        ;
        let params = {
          email: this.email,
          type: 'CHANGE_PASSWORD'
        };
        let that = this;
        this.axios.get("/api/validationCode/" + this.selectedOwner, {params: params}).then(res => {
          that.getValidateCodeDisabled = true;
          that.getValidateCodeBtn = that.cutdownCount;
          this.validationCodeTimeInterval = setInterval(that.cutdown, 1000);
        });
      },
      cutdown() {
        this.cutdownCount = this.cutdownCount - 1;
        if (this.cutdownCount <= 0) {
          this.getValidateCodeBtn = "获取验证码";
          this.cutdownCount = 60;
          this.getValidateCodeDisabled = false;
          window.clearInterval(this.validationCodeTimeInterval);
          return;
        }
        this.getValidateCodeBtn = this.cutdownCount;
      },
      savePwd() {
        if (this.code.trim() == "") {
          this.$message({message: "请输入验证码", type: "warning"});
          return;
        }
        if (this.newPwd == "" || this.newPwd.length < 6) {
          this.$message({message: "密码至少6位", type: "warning"});
          return;
        }
        if (this.confirmPwd != this.newPwd) {
          this.$message({message: "两次密码不一致", type: "warning"});
          return;
        }
        let param = {
          code: this.code,
          ownerId: this.selectedOwner,
          password: this.newPwd
        };
        this.axios.put("/api/password", param).then(res => {
          this.$message({message: "修改密码成功", type: "success"});
          this.cleanData();
          this.$emit("close");
        });
      },
      cleanData() {
        this.code = "";
        this.email = "";
        this.newPwd = "";
        this.confirmPwd = "";
        this.getValidateCodeBtn = "获取验证码";
        this.cutdownCount = 60;
        this.getValidateCodeDisabled = false;
        window.clearInterval(this.validationCodeTimeInterval);
      }
    },
    mounted() {
      this.selectedOwner = this.id;
    }
  }
</script>

<style scoped>
  .content {
    margin-top: 20px;
  }
</style>
