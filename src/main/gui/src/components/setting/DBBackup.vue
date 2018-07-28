<template>
  <div>
    <p><strong>注意：</strong>系统将在每天凌晨0点自动进行数据库备份，并在上午9点自动上传备份文件，并将备份文件发送至相关者邮箱。</p>
    <div v-loading="countLoading">
     最大文件数量：
     <template v-if="!editMode">
       {{this.count}} <i class="el-icon-edit" style="cursor: pointer" @click="editMode = true"></i>
     </template>
     <template v-else>
      <InputNumber controls-position="right" size='mini' :min="1" v-model="count"/> <i class="el-icon-check" style="cursor: pointer" @click="updateMaxCount"></i>
      <i class="el-icon-close" style="cursor: pointer" @click="count = countBak; editMode = false"></i>
     </template>
    </div>
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
  import {Button, Table, TableColumn, InputNumber} from 'element-ui'

  export default {
    name: "DBBackup",
    components: {Table, TableColumn, Button, InputNumber},
    data() {
      return {
        data: [],
        loading: false,
        countLoading: false,
        count: 0,
        countBak: 0,
        editMode: false
      }
    },
    created() {
      this.load();
      this.getMaxCount();
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
        });
      },
      getMaxCount() {
        this.countLoading = true;
        var that = this;
        this.axios.get("/api/maxDBBackupCount").then(res => {
          this.count = res.data.data;
          this.countBak = res.data.data;
          this.countLoading = false;
        }).catch(res => {
          this.countLoading = false;
        });
      },
      updateMaxCount() {
        if (this.data.length > this.count) {
          this.$confirm("设置的最大备份文件个数小于当前拥有的备份文件个数，如果确认更新，系统将会删除多余的备份文件，是否确认？", "警告", {
            confirmButtonText: "确认",
            cancelButtonText: "取消",
            type: 'warning'
          }).then(() => {
            this.sureUpdateMaxCount();
          })
        } else {
          this.sureUpdateMaxCount();
        }
      },
      sureUpdateMaxCount() {
        this.countLoading = true;
        var that = this;
        this.axios.put("/api/maxDBBackupCount/"+this.count).then(res => {
          this.countLoading = false;
          this.editMode = false;
          this.getMaxCount();
          this.load();
        }).catch(res => {
          this.countLoading = false;
        });
      },
      download(id) {
        window.location.href =  "/api/dbBackup/" + id;
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
