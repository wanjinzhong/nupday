<template>
  <div v-loading="loading">
    <Form>
      <FormItem label-width="0px">
        <Select v-model="selectedOwner" style="width: 300px">
          <Option v-for="owner in allOwners"
                  :key="owner.id"
                  :label="owner.name"
                  :value="owner.id"></Option>
        </Select>
      </FormItem>
      <FormItem label-width="0px">
        <Input type="password" v-model="password" placeholder="密码" class="password" @focus="onFocus" @blur="onBlur"
               style="width: 300px;"/>
      </FormItem>
      <div class="forget" @click="showChangePassword = true">忘记密码</div>
      <Button native-type="submit" type="primary" style="width:300px" @click.prevent="login" :disabled="loading" :loading="loading">登陆
      </Button>
    </Form>
    <Dialog :visible.sync="showChangePassword" title="修改密码" width="600px">
      <ChangePassword :id="selectedOwner" @close="showChangePassword = false"></ChangePassword>
    </Dialog>
  </div>
</template>

<script>
  import {Form, FormItem, Select, Option, Input, Button, Dialog} from "element-ui"
  import $ from "jquery"
  import ChangePassword from "./ChangePassword"

  export default {
    name: "OwnerLogin",
    components: {Form, FormItem, Select, Option, Input, Button, ChangePassword, Dialog},
    props: ["to"],
    data() {
      return {
        selectedOwner: 1,
        password: "",
        loading: false,
        showChangePassword: false
      }
    },
    computed: {
      allOwners() {
        return this.$store.getters.getOwners;
      }
    },
    methods: {
      login() {
        if (this.password == "") {
          this.$message({
            type: "warning",
            message: "密码不能为空"
          });
          return;
        }
        var self = this;
        this.loading = true;
        const data = {
          type: "OWNER",
          userId: this.selectedOwner,
          password: this.password
        }
        this.axios.post("/api/login", data).then((response) => {
          var name = '';
          for (var i in self.allOwners) {
            if (self.allOwners[i].id == this.selectedOwner) {
              name = self.allOwners[i].name;
            }
          }
          self.loading = false;
          self.$message({
            type: "success",
            message: name + "，欢迎回家！"
          });
          if (self.to != undefined && self.to.length > 0) {
            self.$router.push(self.to);
          } else {
            self.$router.push("/");
          }
        }).catch(res => {
          self.loading = false;
        });
      },
      onFocus() {
        $("#left_hand").animate({
          left: "150",
          top: " -38"
        }, {
          step: function () {
            if (parseInt($("#left_hand").css("left")) > 140) {
              $("#left_hand").attr("class", "left_hand");
            }
          }
        }, 2000);
        $("#right_hand").animate({
          right: "-64",
          top: "-38px"
        }, {
          step: function () {
            if (parseInt($("#right_hand").css("right")) > -70) {
              $("#right_hand").attr("class", "right_hand");
            }
          }
        }, 2000);
      },
      onBlur() {
        $("#left_hand").animate({
          left: "100px",
          top: "-12px"
        }, {
          step: function () {
            if (parseInt($("#left_hand").css("left")) > 100) {
              $("#left_hand").attr("class", "left_hand");
            }
          }
        }, 2000);
        $("#right_hand").animate({
          right: "-112px",
          top: "-12px"
        }, {
          step: function () {
            if (parseInt($("#right_hand").css("right")) > -70) {
              $("#right_hand").attr("class", "right_hand");
            }
          }
        }, 2000);
      }
    }
  }
</script>

<style scoped>
  * {
    margin: 10px;
  }
  .forget {
    font-size: 14px;
    color: blue;
    cursor: pointer;
    text-align: right;
    margin-right: 30px;
  }
  .el-dialog__body {
    padding: 0px;
    padding-bottom: 10px;
  }
</style>
