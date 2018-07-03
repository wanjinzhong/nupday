<template>
  <div style="margin-top: 20px;margin-bottom: 20px">
    <Form>
      <FormItem label-width="0px">
        <Input v-model="accessCode" style="width: 280px;" placeholder="访问码"/>
        <Tooltip placement="top" :content="tipMessage" effect="light">
          <svg class="icon" aria-hidden="true" font-size="20px" color="#555">
            <use xlink:href="#icon-bangzhu"></use>
          </svg>
        </Tooltip>
      </FormItem>
      <Button native-type="submit" type="primary" style="width:300px" @click.prevent="login" :disabled="btnLoading" :loading="btnLoading">登陆
      </Button>
    </Form>
  </div>
</template>

<script>
  import {Button, Form, FormItem, Input, Tooltip} from "element-ui"

  export default {
    name: "VisitorLogin",
    components: {Form, FormItem, Input, Button, Tooltip},
    data() {
      return {
        btnLoading: false,
        accessCode: "",
        tipMessage: "如果没有访问码，请向代佳琪或者万进忠申请访问码"
      }
    },
    methods: {
      login() {
        if (this.accessCode == "") {
          this.$message({
            type: "warning",
            message: "访问码不能为空"
          });
          return;
        }
        var self = this;
        this.btnLoading = true;
        const data = {
          type: "VISITOR",
          accessCode: this.accessCode
        };
        this.axios.post("/api/login", data).then((response) => {
          self.$message({
            type: "success",
            message: "欢迎光临！"
          });
          self.$router.push("/");
        });
        this.btnLoading = false;
      }
    }
  }
</script>

<style scoped>

</style>
