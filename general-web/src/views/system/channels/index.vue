<template>
  <div class="app-container">
    <!--表单组件-->
    <eForm ref="form" :is-add="isAdd" :dict-map="dictMap"/>
    <el-row :gutter="20">
      <!--组织数据-->
      <el-col :xs="9" :sm="6" :md="4" :lg="4" :xl="4">
        <div class="head-container">
          <el-input v-model="deptName" clearable placeholder="输入组织名称搜索" prefix-icon="el-icon-search" style="width: 100%;" class="filter-item" @input="getDeptDatas"/>
        </div>
        <el-tree :data="depts" :props="defaultProps" :expand-on-click-node="false" default-expand-all @node-click="handleNodeClick"/>
      </el-col>
      <!--用户数据-->
      <el-col :xs="15" :sm="18" :md="20" :lg="20" :xl="20">
        <!--工具栏-->
        <div class="head-container">
          <!-- 搜索 -->
          <el-input v-model="query.blurry" clearable placeholder="输入名称或者邮箱搜索" style="width: 200px;" class="filter-item" @keyup.enter.native="toQuery"/>
          <el-button class="filter-item" size="mini" type="success" icon="el-icon-search" @click="toQuery">搜索
          </el-button>
          <!-- 新增 -->
          <div v-permission="['ADMIN','CHANNELSINFO_ALL','CHANNELSINFO_CREATE']" style="display: inline-block;margin: 0px 2px;">
            <el-button
              v-permission="['ADMIN','CHANNELSINFO_ALL','CHANNELSINFO_CREATE']"
              class="filter-item"
              size="mini"
              type="primary"
              icon="el-icon-plus"
              @click="add">新增
            </el-button>
          </div>
        </div>
        <!--表格渲染-->
        <el-table v-loading="loading" :data="data" size="small" style="width: 100%;">
          <el-table-column prop="channelsName" label="频道名"/>
          <el-table-column prop="createTime" label="创建时间">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="组织">
            <template slot-scope="scope">
              <div>{{ scope.row.dept.name }}</div>
            </template>
          </el-table-column>
          <el-table-column prop="maxPersonNumber" label="频道内最大人数"/>
          <el-table-column
            v-if="checkPermission(['ADMIN','CHANNELSINFO_ALL','CHANNELSINFO_EDIT','CHANNELSINFO_DELETE'])"
            label="操作"
            width="150px"
            align="center">
            <template slot-scope="scope">
              <el-button
                v-permission="['ADMIN','CHANNELSINFO_ALL','CHANNELSINFO_EDIT']"
                size="mini"
                type="primary"
                icon="el-icon-edit"
                @click="edit(scope.row)"/>
              <el-popover
                v-permission="['ADMIN','CHANNELSINFO_ALL','CHANNELSINFO_DELETE']"
                :ref="scope.row.id"
                placement="top"
                width="180">
                <p>确定删除本条数据吗？</p>
                <div style="text-align: right; margin: 0">
                  <el-button size="mini" type="text" @click="$refs[scope.row.id].doClose()">取消</el-button>
                  <el-button :loading="delLoading" type="primary" size="mini" @click="subDelete(scope.row.id)">确定
                  </el-button>
                </div>
                <el-button slot="reference" type="danger" icon="el-icon-delete" size="mini"/>
              </el-popover>
            </template>
          </el-table-column>
        </el-table>
        <!--分页组件-->
        <el-pagination
          :total="total"
          :current-page="page + 1"
          style="margin-top: 8px;"
          layout="total, prev, pager, next, sizes"
          @size-change="sizeChange"
          @current-change="pageChange"/>
    </el-col></el-row>
  </div>
</template>

<script>
import checkPermission from '@/utils/permission'
import initData from '@/mixins/initData'
import initDict from '@/mixins/initDict'
import { del } from '@/api/channelsInfo'
import { getDepts } from '@/api/dept'
import { parseTime } from '@/utils/index'
import eForm from './form'

export default {
  components: { eForm },
  mixins: [initData, initDict],
  data() {
    return {
      delLoading: false, deptName: '', depts: [], deptId: null,
      defaultProps: {
        children: 'children',
        label: 'name'
      }
    }
  },
  created() {
    this.getDeptDatas()
    this.$nextTick(() => {
      this.init()
      // 加载数据字典
      this.getDictMap('channels_record_switch,channels_attr,channels_mode')
    })
  },
  methods: {
    parseTime,
    checkPermission,
    beforeInit() {
      this.url = 'api/channelsInfo'
      const sort = 'id,desc'
      const query = this.query
      const blurry = query.blurry
      this.params = { page: this.page, size: this.size, sort: sort, deptId: this.deptId }
      if (blurry) { this.params['blurry'] = blurry }
      return true
    },
    subDelete(id) {
      this.delLoading = true
      del(id).then(res => {
        this.delLoading = false
        this.$refs[id].doClose()
        this.dleChangePage()
        this.init()
        this.$notify({
          title: '删除成功',
          type: 'success',
          duration: 2500
        })
      }).catch(err => {
        this.delLoading = false
        this.$refs[id].doClose()
      })
    },
    add() {
      this.isAdd = true
      this.$refs.form.dialog = true
      this.$refs.form.getDepts()
    },
    edit(data) {
      this.isAdd = false
      this.$refs.form.getDepts()
      this.$refs.form.selectUserByDeptId(data.dept.id)
      const _this = this.$refs.form
      _this.form = {
        id: data.id,
        attr: data.attr.toString(),
        channelsName: data.channelsName,
        maxPersonNumber: data.maxPersonNumber,
        mode: data.mode.toString(),
        recordSwitch: data.recordSwitch.toString(),
        dept: { id: data.dept.id },
        userSet: data.userSet,
        userAdmin: data.userAdmin,
        deptId: data.deptId
      }
      if (data.userSet != null) {
        data.userSet.forEach(function(data, index) {
          const obj = {}
          obj.id = data.id
          obj.name = data.name
          _this.userTags.push(obj)
        })
      }
      if (data.userAdmin != null) {
        data.userAdmin.forEach(function(data, index) {
          const obj = {}
          obj.id = data.id
          obj.name = data.name
          _this.adminTags.push(obj)
        })
      }
      _this.deptId = data.dept.id
      _this.dialog = true
    },
    handleNodeClick(data) {
      if (data.pid === 0) {
        this.deptId = null
      } else {
        this.deptId = data.id
      }
      this.init()
    },
    getDeptDatas() {
      const sort = 'id,desc'
      const params = { sort: sort }
      if (this.deptName) { params['name'] = this.deptName }
      getDepts(params).then(res => {
        this.depts = res.content
      })
    }
  }
}
</script>

<style scoped>

</style>
