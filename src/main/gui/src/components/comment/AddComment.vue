<template>
  <div v-loading="loading">
    <Form label-width="80px" @submit.native.prevent>
      <FormItem label="选个头像" style="margin-bottom: 0px" v-if="$store.getters.getType != 'OWNER'">
        <Tooltip placement="right" effect="light">
          <img :src="'/static/avatar/' + avatar + '.png'" style="width: 48px; cursor: pointer"/>
          <div slot="content"><AvatarPicker v-model="avatar"></AvatarPicker></div>
        </Tooltip>
      </FormItem>
      <FormItem label="怎么称呼" v-if="$store.getters.getType != 'OWNER'">
        <Input v-model="name" style="width: 400px" :size="targetType == 'COMMENT'? 'mini': 'normal'"/>
      </FormItem>
      <FormItem :label="targetType == 'COMMENT'? '回复': '祝福的话'">
        <Input type="textarea" v-model="content" :size="targetType == 'COMMENT'? 'mini': 'normal'"/>
      </FormItem>
      <div style="width: 100%; text-align: right">
        <Button type="primary" @click="onSubmit">{{targetType == 'COMMENT'? '回复': '祝福'}}</Button>
      </div>
    </Form>
  </div>
</template>

<script>
  import {Button, Form, FormItem, Input, Tooltip} from 'element-ui'
  import AvatarPicker from '../util/AvatarPicker'
  export default {
    name: "AddComment",
    components: {Form, FormItem, Button, Input, Tooltip, AvatarPicker},
    props: ["targetType", "targetId"],
    data() {
      return {
        name: '',
        content: '',
        loading: false,
        avatar: 1
      }
    },
    methods: {
      onSubmit() {
        if (this.$store.getters.getType=="VISITOR" && this.name.trim() == '') {
          this.$message({
            type: 'error',
            message: '您怎么称呼？'
          });
          return;
        }
        if (this.content.trim() == '') {
          this.$message({
            type: 'error',
            message: "写一些祝福内容吧"
          });
          return;
        }
        this.loading = true;
        var data = {
          name: this.name,
          content:this.content,
          targetType: this.targetType,
          targetId: this.targetId,
          avatar: this.avatar
        };
        var successMsg = this.targetType == "ARTICLE" ? "祝福":"回复";
        successMsg += "成功";
        this.axios.post("/api/comment", data).then(res => {
          this.loading = false;
          this.name = '';
          this.content = '';
          this.$message({
            type: "success",
            message: successMsg
          });
          this.$emit("refresh");
        });
      }
    }
  }
</script>

<style scoped>
</style>
