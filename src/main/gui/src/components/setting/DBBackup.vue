<template>
  <div>
    <p><strong>注意：</strong>系统将在每天凌晨0点自动进行数据库备份，并在上午9点自动上传备份文件，并将备份文件发送至相关者邮箱。</p>
    <Table v-loading="loading" :data="data">
      <TableColumn type="index" width="50">
      </TableColumn>
      <TableColumn label="上传时间">
        <template slot-scope="scope">
          {{new Date(scope.row.time) | formatDate('DATETIME')}}
        </template>
      </TableColumn>
      <TableColumn label="操作">
        <template slot-scope="scope">
          <Button type="primary" size="mini" @click="download(scope.row.id)">下载文件</Button>
          <Button type="danger" size="mini" @click="deleteDBBackup(scope.row.id)">删除</Button>
        </template>
      </TableColumn>
    </Table>
  </div>
</template>

<script>
  import {Button, Table, TableColumn} from 'element-ui'

  export default {
    name: "DBBackup",
    components: {Table, TableColumn, Button},
    data() {
      return {
        data: [],
        loading: false
      }
    },
    created() {
      this.load();
    },
    methods: {
      load() {
        this.loading = true;
        var that = this;
        this.axios.get("/api/dbBackups").then(res => {
          this.data = res.data.data;
          this.loading = false;
        }).catch(res => {
          this.loading = false;
        })
      },
      download(id) {
        window.location.href = this.apiUrl + "/api/dbBackup/" + id;
      },
      deleteDBBackup(id) {
        let that = this;
        this.$confirm("确定要删除这个备份文件吗？删除后不可恢复", "删除文件", {
          type: "warning"
        }).then(() => {
          that.axios.delete("/api/dbBackup/" + id).then(res => {
           that.$message({
             type: "success",
             message: "删除成功"
           });
           that.load();
          })
        });
        this.load();
      }
    }
  }
</script>

<style scoped>

</style>
