<template>
  <div style="margin-top: 20px;margin-bottom: 20px" v-loading="loading">
    <Form>
      <FormItem label-width="0px">
        <Input v-model="accessCode" style="width: 300px;" placeholder="访问码"/>
      </FormItem>
      <Button native-type="submit" type="primary" style="width:300px" @click.prevent="login" :disabled="loading" :loading="loading">登陆
      </Button>
    </Form>
  </div>
</template>

<script>
  import {Button, Form, FormItem, Input, Tooltip} from "element-ui"

  export default {
    name: "VisitorLogin",
    components: {Form, FormItem, Input, Button, Tooltip},
    props: ["to"],
    data() {
      return {
        loading: false,
        accessCode: "",
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
        this.loading = true;
        const data = {
          type: "VISITOR",
          accessCode: this.accessCode
        };
        this.axios.post("/api/login", data).then((response) => {
          self.$message({
            type: "success",
            message: "欢迎光临！"
          });
          self.loading = false;
          if (self.to != undefined && self.to.length > 0) {
            self.$router.push(self.to);
          } else {
            self.$router.push("/");
          }
        }).catch(res=>{
          self.loading = false;
        });
      }
    }
  }
</script>

<style scoped>

</style>
