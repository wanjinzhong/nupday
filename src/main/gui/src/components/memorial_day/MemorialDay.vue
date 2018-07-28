<template>
  <div>
    <Steps direction="vertical" :active="memorialDays.length" v-loading="memorialDaysLoading">
      <Step v-for="item in memorialDays" :key="item.id" :style="{backgroundColor: item.open? 'transparent': '#eee'}">
        <div slot="title" @mouseover="item.hover = true" @mouseleave="item.hover = false">{{item.time}} -
          {{item.title}}
          <template v-if="item.hover">
            <Tooltip :content="item.open?'锁定':'解锁'" effect="light" :open-delay="500">
              <svg class="icon" aria-hidden="true" @click="changeLock(item)">
                <use :xlink:href="item.open?'#icon-jiesuo' : '#icon-suoding'"></use>
              </svg>
            </Tooltip>&nbsp;
            <Tooltip content="编辑" effect="light" :open-delay="500">
              <svg class="icon" aria-hidden="true" @click="toEdit(item.id)">
                <use xlink:href="#icon-ai-edit"></use>
              </svg>
            </Tooltip>&nbsp;
            <Tooltip content="删除" effect="light" :open-delay="500">
              <svg class="icon" aria-hidden="true" @click="deleteMemorialDay(item.id)">
                <use xlink:href="#icon-shanchu"></use>
              </svg>
            </Tooltip>
          </template>
        </div>
        <svg class="icon" aria-hidden="true" slot="icon" style="cursor: pointer" @click="setHome(item.home, item.id)">
          <use :xlink:href="item.home? '#icon-msnui-home-block' : '#icon-yuandianda'"></use>
        </svg>
        <div slot="description" style="color: #555; margin-left: 20px; margin-bottom: 30px">
          {{item.description}}
        </div>
      </Step>
    </Steps>
  </div>
</template>

<script>
  import {Steps, Step, Tooltip} from 'element-ui';

  export default {
    name: "MemorialDay",
    components: {Steps, Step, Tooltip},
    data() {
      return {
        memorialDays: [],
        memorialDaysLoading: false
      }
    },
    methods: {
      load() {
        this.memorialDaysLoading = true;
        this.axios.get("/api/memorialDays").then(res => {
          var data = res.data.data;
          for (var i in data) {
            data[i]["hover"] = false;
          }
          this.memorialDays = data;
          this.memorialDaysLoading = false;
        }).catch(res => {
          this.memorialDaysLoading = false;
        })
      },
      setHome(isHome, id) {
        if (isHome) {
          return;
        }
        this.axios.put("/api/memorialDay/home/" + id).then(res => {
          this.load();
        });
      },
      toEdit(item) {
      },
      changeLock(item) {
        this.axios.put("/api/memorialDay/" + item.id + "/open/" + !item.open).then(res => {
          item.open = !item.open;
        })
      },
      deleteMemorialDay(id) {
        this.$confirm("删除之后将无法恢复，确定要删除吗？", "确认删除", {
          confirmButtonText: "确认",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          this.memorialDaysLoading = true;
          this.axios.delete("/api/memorialDay/" + id).then(res => {
            this.memorialDaysLoading = false;
            this.$message({
              type: "success",
              message: "纪念日删除成功"
            });
            this.load();
          }).catch(res => {
            this.memorialDaysLoading = false;
          })
        })
      }
    },
    created() {
      this.load();
    }
  }
</script>

<style>
  .el-step__head.is-finish {
    color: #de2070 !important;
    border-color: #de2070 !important;
  }

  .el-step__title.is-finish {
    color: #333;
  }

  .icon {
    font-size: 16px;
    cursor: pointer;
  }
</style>
