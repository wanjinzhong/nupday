<template>
  <div v-loading="loading">
    <Form label-position="left" label-width="80px">
      <FormItem label="访问码">
        <Input size="small" v-model="code" style="width: 90%"/>
        <svg class="icon" aria-hidden="true" style="cursor: pointer" @click="generate">
          <use xlink:href="#icon-zhongxinshengchengchushimima"></use>
        </svg>
      </FormItem>
      <FormItem label="起止时间">
        <DatePicker size="small" type="datetimerange"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期" v-model="time"></DatePicker>
      </FormItem>
      <FormItem label="最大访问">
        <InputNumber size="small" v-model="count" :min="1"/>
      </FormItem>
      <div style="text-align: right">
        <Button type="primary" @click="add">添加</Button>
      </div>
    </Form>
  </div>
</template>

<script>
  import {Button, DatePicker, Form, FormItem, Input, InputNumber} from "element-ui"
  import {formatDate} from "../../../utils/TimeFormater"
  export default {
    name: "NewVisitor",
    components: {Button, Form, FormItem, Input, InputNumber, DatePicker},
    data() {
      return {
        code: '',
        time: [],
        count: 1,
        loading: false
      }
    },
    methods: {
      generate() {
        var s = [];
        var hexDigits = "0123456789abcdef";
        for (var i = 0; i < 6; i++) {
          s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
        }
        var uuid = s.join("");
        this.code = uuid;
      },
      add() {
        if (this.code == '') {
          this.$message({
            type: "error",
            message: "访问码不能为空"
          });
          return;
        }
        var data = {
          code: this.code,
          from: formatDate(new Date(this.time[0]), 'DATETIME'),
          to: formatDate(new Date(this.time[1]), 'DATETIME'),
          maxCount: this.count
        };
        this.loading = true;
        this.axios.post("/api/visitor", data).then(res => {
          this.loading = false;
          this.$emit("addComplete");
        }).catch(res => {
          this.loading = false;
        })
      }
    }
  }
</script>

<style scoped>

</style>
