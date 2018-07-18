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
        <Button type="primary" @click="update">修改</Button>
      </div>
    </Form>
  </div>
</template>

<script>
  import {Button, Form, FormItem, Input, InputNumber, DatePicker} from "element-ui"
  import {formatDate} from "../../../utils/TimeFormater"
  export default {
    name: "EditVisitor",
    props: ["id"],
    components: {Button, Form, FormItem, Input, InputNumber, DatePicker},
    data() {
      return {
        code: '',
        time: [],
        count: 1,
        loading: false
      }
    },
    watch: {
      id(val, oldVal) {
        this.load();
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
      load() {
        this.loading = true;
        var that = this;
        this.axios.get("/api/visitor/" + this.id).then(res => {
          that.loading = false;
          var data = res.data.data;
          that.code = data.code;
          that.count= data.maxCount;
          that.time = [];
          that.time.push(data.from);
          that.time.push(data.to);
        }).catch(res => {
          that.loading = false;
        });
      },
      update() {
        if (this.code == '') {
          this.$message({
            type: "error",
            message: "访问码不能为空"
          });
          return;
        }
        var data = {
          id: this.id,
          code: this.code,
          from: formatDate(new Date(this.time[0]), 'DATETIME'),
          to: formatDate(new Date(this.time[1]), 'DATETIME'),
          maxCount: this.count
        };
        this.loading = true;
        this.axios.put("/api/visitor", data).then(res => {
          this.loading = false;
          this.$emit("editComplete");
        }).catch(res => {
          this.loading = false;
        })
      }
    },
    mounted() {
      this.load();
    }
  }
</script>

<style scoped>

</style>
