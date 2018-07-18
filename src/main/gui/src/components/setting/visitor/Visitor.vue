<template>
  <div v-loading="loading">
    <div>
      <Button type="primary" size="small" @click="addDialogShow = true">添加访问码</Button>
    </div>
    <Table :data="visitors" :row-class-name="tableRowClassName">
      <TableColumn prop="code" label="访问码" width="100px"></TableColumn>
      <TableColumn label="开始时间" width="180px">
        <template slot-scope="scope">
          {{new Date(scope.row.from) | formatDate('DATETIME')}}
        </template>
      </TableColumn>
      <TableColumn prop="to" label="结束时间" width="180px">
        <template slot-scope="scope">
          {{new Date(scope.row.to) | formatDate('DATETIME')}}
        </template>
      </TableColumn>
      <TableColumn prop="maxCount" label="最大访问次数" width="120px"></TableColumn>
      <TableColumn prop="loginCount" label="已访问次数" width="120px"></TableColumn>
      <TableColumn label="操作">
        <template slot-scope="scope">
          <svg class="icon" aria-hidden="true" style="cursor: pointer" @click="onEdit(scope.row.id)">
            <use xlink:href="#icon-ai-edit"></use>
          </svg>&nbsp;
          <svg class="icon" aria-hidden="true" style="cursor: pointer" @click="onDelete(scope.row.id)">
            <use xlink:href="#icon-shanchu"></use>
          </svg>
        </template>
      </TableColumn>
    </Table>
    <Dialog :visible.sync="addDialogShow" width="530px" title="添加访问码">
      <NewVisitor @addComplete="optComplete"></NewVisitor>
    </Dialog>
    <Dialog :visible.sync="editDialogShow" width="530px" title="修改访问码">
      <EditVisitor ref="editComponent" :id="editId" @editComplete="optComplete"></EditVisitor>
    </Dialog>
  </div>
</template>

<script>
  import {Button, Dialog, Form, FormItem, Table, TableColumn} from "element-ui"
  import NewVisitor from "./NewVisitor"
  import EditVisitor from "./EditVisitor"

  export default {
    name: "Visitor",
    components: {Button, Table, TableColumn, Form, FormItem, Dialog, NewVisitor, EditVisitor},
    data() {
      return {
        visitors: [],
        loading: false,
        addDialogShow: false,
        editDialogShow: false,
        editId: -1
      }
    },
    methods: {
      loadVisitors() {
        this.loading = true;
        var that = this;
        this.axios.get("/api/visitors").then(res => {
          that.visitors = res.data.data;
          that.loading = false;
        }).catch(res => {
          that.loading = false;
        });
      },
      tableRowClassName({row, rowIndex}) {
        if (this.visitors[rowIndex].isCountout || this.visitors[rowIndex].isTimeout) {
          return 'warning-row';
        } else {
          return '';
        }
      },
      onDelete(id) {
        this.$confirm("确定要删除这个访问码吗？删除之后访客将不能用这个访问码登陆。", "删除访问码", {
          type: "warning"
        }).then(() => {
          this.loading = true;
          this.axios.delete("/api/visitor/" + id).then(res => {
            this.loading = false;
            this.$message({
              type: "success",
              message: "删除访问码成功"
            });
            this.loadVisitors();
          }).catch(res => {
            this.loading = false;
          })
        })
      },
      onEdit(id) {
        this.editId = id;
        this.editDialogShow = true;
      },
      optComplete() {
        this.addDialogShow = false;
        this.editDialogShow = false;
        this.loadVisitors();
      }
    },
    created() {
      this.loadVisitors();
    }
  }
</script>

<style>
  .warning-row {
    background-color: #fbe2e6 !important;
  }
</style>
