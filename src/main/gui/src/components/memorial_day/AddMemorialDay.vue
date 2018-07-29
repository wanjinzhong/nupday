<template>
  <div>
    <Form v-loading="loading" label-position="left" label-width="80px">
      <FormItem label="标题">
        <Input v-model="title" size="small"/>
      </FormItem>
      <FormItem label="描述">
        <Input v-model="description" size="small"/>
      </FormItem>
      <FormItem label="时间">
        <DatePicker v-model="time" size="small"/>
      </FormItem>
      <FormItem label="开放">
        <el-switch v-model="open" size="small"></el-switch>
      </FormItem>
      <FormItem label="主页">
        <el-switch v-model="home" size="small"></el-switch>
      </FormItem>
      <FormItem v-if="home" label="预览">
        我们{{title}}XXX天啦！
      </FormItem>
      <div style="text-align: right">
        <Button @click="saveMemorialDay()" type="primary">保存</Button>
        <Button @click="closeMe()">取消</Button>
      </div>
    </Form>
  </div>
</template>

<script>
  import {Form, FormItem, Button, Input, Switch, DatePicker} from 'element-ui';
  import {formatDate} from "../../utils/TimeFormater"

  export default {
    name: "AddMemorialDay",
    components: {Form, FormItem, Button, Input, Switch, DatePicker},
    data() {
      return {
        title: '',
        description: '',
        open: true,
        home: false,
        time: '',
        loading: false
      }
    },
    methods: {
      saveMemorialDay() {
        if (this.title.trim() == "") {
          this.$message({
            type: 'error',
            message: '标题不能为空'
          });
          return;
        }
        if (formatDate(new Date(this.time), 'DATE') == "") {
          this.$message({
            type: 'error',
            message: '时间不能为空'
          });
          return;
        }
        var data = {
          title: this.title,
          description: this.description,
          home: this.home,
          open: this.open,
          time: formatDate(new Date(this.time), 'DATE')
        };
        this.loading = true;
        this.axios.post("/api/memorialDay", data).then(res => {
          this.loading = false;
          this.title = "";
          this.description = "";
          this.home = false;
          this.open = true;
          this.time = "";
          this.$message({
            type: "success",
            message: "纪念日保存成功"
          });
          this.$emit("closeMe", true);
        }).catch(res => {
          this.loading = false;
        })
      },
      closeMe() {
        this.$emit("closeMe", false);
      }
    }
  }
</script>

<style scoped>

</style>
