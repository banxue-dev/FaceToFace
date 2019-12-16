<template>
  <div>
    <el-dialog :visible.sync="dialog" :close-on-click-modal="false" :before-close="cancel" :title="isAdd ? '新增用户' : '编辑用户'" append-to-body width="550px">
      <el-form ref="form" :inline="true" :model="form" :rules="rules" size="small" label-width="100px">
        <el-form-item label="登录名" prop="username">
          <el-input v-model="form.username" style="width: 350px;" placeholder="请输入登录名"/>
        </el-form-item>
        <el-form-item v-if="isAdd" label="用户密码" prop="password">
          <el-input v-model="form.password" style="width: 350px;" placeholder="不填默认123456"/>
        </el-form-item>
		<el-form-item label="用户密码" v-if="!isAdd" prop="password">
          <el-input v-model="form.password" style="width: 350px;" placeholder="不填表示不更改"/>
        </el-form-item>
		<el-form-item label="用户名" prop="name">
          <el-input v-model="form.name" style="width: 350px;" placeholder="请输入用户名"/>
        </el-form-item>
        <el-form-item label="状态" prop="enabled">
          <el-radio-group v-model="form.enabled">
            <el-radio v-for="item in dictMap.user_status" :key="item.id" :label="item.value">{{ item.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
		<el-form-item label="用户类型" prop="userType">
          <el-radio-group v-model="form.userType">
            <el-radio v-for="item in dictMap.user_types" :key="item.id" :label="item.value">{{ item.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="组织">
          <treeselect v-model="deptId" :options="depts" style="width: 350px" placeholder="请选择组织" @select="selectDept"/>
        </el-form-item>
        <el-form-item label="角色" v-if="false">
          <el-select v-model="roleIds" style="width: 350px;" multiple placeholder="请选择角色">
            <el-option
              v-for="(item, index) in roles"
              :disabled="level !== 1 && item.level < level"
              :key="item.name + index"
              :label="item.name"
              :value="item.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="等级"  v-if="false" prop="level">
          <el-input-number v-model.number="form.level" style="width: 350px;" controls-position="right" placeholder="请输入等级"/>
        </el-form-item>
        <el-form-item label="企业识别码" prop="enterpriseCode">
          <el-input v-model="form.enterpriseCode" style="width: 350px;" disabled placeholder="请输入企业识别码"/>
        </el-form-item>
        <el-form-item label="定位开关" prop="locationSwitch">
          <el-radio-group v-model="form.locationSwitch">
            <el-radio v-for="item in dictMap.user_location_switch" :key="item.id" :label="item.value">{{ item.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="定位间隔" prop="locationInterval">
          <el-input-number v-model.number="form.locationInterval" style="width: 350px;" controls-position="right" placeholder="请输入定位间隔"/>
        </el-form-item>
        <el-form-item label="视频开关" prop="videoSwitch">
          <el-radio-group v-model="form.videoSwitch">
            <el-radio v-for="item in dictMap.user_video_switch" :key="item.id" :label="item.value">{{ item.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="服务期限" prop="serviceTime">
		<el-date-picker
            v-model="form.serviceTime"
            type="datetime"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            placeholder="选择日期时间">
    </el-date-picker>
        </el-form-item>
        <el-form-item label="默认频道">
          <el-select v-model="form.channels.id" filterable style="width: 350px;" placeholder="请选择默认频道" @change="defaultChanneChange">
            <el-option
              v-for="item in channelsList"
              :key="item.id"
              :label="item.channelsName"
              :value="item.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="频道">
          <el-card class="box-card" style="width: 350px;">
            <el-tag
              v-for="tag in channelsTags"
              :key="tag.id"
              closable
              color="#FFFFFF"
              @close="handleClose(tag)">
              {{ tag.channelsName }}
            </el-tag>
            <el-tag color="#FFFFFF" class="add_btn" @click="selectChannelsDialog()">
              <i class="el-icon-plus"/>
            </el-tag>
          </el-card>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="cancel">取消</el-button>
        <el-button :loading="loading" type="primary" @click="doSubmit">确认</el-button>
      </div>
    </el-dialog>
    <el-dialog :append-to-body="true" :close-on-click-modal="false" :before-close="channelsDialogClose" :visible.sync="channelsDialog" title="选择频道" width="300px">
      <el-select v-model="channelsId" filterable placeholder="请选择频道">
        <el-option v-for="item in channelsList" :key="item.id" :label="item.channelsName" :value="item.id"/>
      </el-select>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="channelsDialogClose">取消</el-button>
        <el-button :loading="loading" type="primary" @click="channelsDialogConfirm">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

import { add, edit } from '@/api/user'
import { getDepts } from '@/api/dept'
import { listChannelsInfosByDeptId } from '@/api/channelsInfo'
import { getAll, getLevel } from '@/api/role'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
  components: { Treeselect },
  props: {
    isAdd: {
      type: Boolean,
      required: true
    },
    dictMap: {
      type: Object,
      required: true
    }
  },
  data() {
	
    return {
      dialog: false, loading: false,
      roleIds: [], roles: [], depts: [], deptId: null, level: 3, channelsDialog: false, channelsList: [], channelsTags: [], channelsId: null,
      form: {
        username: '',
        name: '',
		password:'',
        enterpriseCode: '',
        enabled: null,
        roles: [],
        dept: { id: '' },
        level: null,
        locationSwitch: null,
        locationInterval: null,
        serviceTime: '',
        channels: { id: '' },
        channelsSet: [],
        videoSwitch: null
      },
      rules: {
        username: [
          { required: true, message: '请输入登录名', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
		password: [
          { required: false, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        enabled: [
          { required: true, message: '状态不能为空' }
        ]
      }
    }
  },
  methods: {
    cancel() {
      this.resetForm()
    },
    doSubmit() {
      this.form.dept.id = this.deptId
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (this.deptId === null || this.deptId === undefined) {
            this.$message({
              message: '组织不能为空',
              type: 'warning'
            })
          } else if (this.roleIds.length === 0) {
            //this.$message({
            //  message: '角色不能为空',
            //  type: 'warning'
            //})
          } else {
            this.loading = true
            this.form.roles = []
            const _this = this
            this.roleIds.forEach(function(data, index) {
              const role = { id: data }
              _this.form.roles.push(role)
            })
            if (this.isAdd) {
              this.doAdd()
            } else this.doEdit()
          }
        } else {
          return false
        }
      })
    },
    doAdd() {
      add(this.form).then(res => {
        this.resetForm()
        this.$notify({
          title: '添加成功',
          message: '默认密码：123456',
          type: 'success',
          duration: 2500
        })
        this.loading = false
        this.$parent.init()
      }).catch(err => {
        this.loading = false
      })
    },
    doEdit() {
      edit(this.form).then(res => {
        this.resetForm()
        this.$notify({
          title: '修改成功',
          type: 'success',
          duration: 2500
        })
        this.loading = false
        this.$parent.init()
      }).catch(err => {
        this.loading = false
      })
    },
    resetForm() {
      this.dialog = false
      this.$refs['form'].resetFields()
      this.deptId = null
      this.roleIds = []
      this.channelsList = []
      this.channelsTags = []
      this.form = {
        username: '',
        name: '',
        password: '',
        enterpriseCode: '',
        enabled: null,
        roles: [],
        dept: { id: '' },
        level: null,
        locationSwitch: null,
        locationInterval: null,
        serviceTime: '',
        channels: { id: '' },
        channelsSet: [],
		userType:0,
        videoSwitch: null
      }
    },
    getRoles() {
      getAll().then(res => {
        this.roles = res
      }).catch(err => {
      })
    },
    getDepts() {
      getDepts({ enabled: true }).then(res => {
        this.depts = res.content
      })
    },
    getRoleLevel() {
      getLevel().then(res => {
        this.level = res.level
      }).catch(err => {
      })
    },
    selectDept(node, instanceId) {
      this.form.enterpriseCode = node.enterpriseCode
      listChannelsInfosByDeptId({ deptId: node.id }).then(res => {
        this.channelsList = res
      })
    },
    selectDeptByDeptId(deptId) {
      listChannelsInfosByDeptId({ deptId: deptId }).then(res => {
        this.channelsList = res
      })
    },
    selectChannelsDialog() {
      if (this.channelsList === null) {
        this.$notify({
          title: '请选择组织',
          type: 'warning',
          duration: 2500
        })
        return
      }
      this.channelsDialog = true
    },
    handleClose(tag) {
      this.channelsTags.splice(this.channelsTags.indexOf(tag), 1)
      this.form.channelsSet.splice(this.form.channelsSet.indexOf(tag.id), 1)
    },
    channelsDialogClose() {
      this.channelsDialog = false
    },
    channelsDialogConfirm() {
      if (this.form.channels.id === this.channelsId) {
        this.$notify({
          title: '该频道已经被选为默认频道',
          type: 'warning',
          duration: 2500
        })
        return
      }
      const arry = this.channelsList
      for (let i = 0; i < arry.length; i++) {
        if (arry[i].id === this.channelsId) {
          const tags = this.channelsTags
          for (let j = 0; j < tags.length; j++) {
            if (this.channelsId === tags[j].id) {
              this.$notify({
                title: '该频道已经存在列表中',
                type: 'warning',
                duration: 2500
              })
              return
            }
          }
          const obj = {}
          obj.id = arry[i].id
          obj.channelsName = arry[i].channelsName
          this.channelsTags.push(obj)
          this.form.channelsSet.push({ 'id': arry[i].id })
          this.channelsDialog = false
        }
      }
    },
    defaultChanneChange() {
      const tags = this.channelsTags
      for (let j = 0; j < tags.length; j++) {
        if (this.form.channels.id === tags[j].id) {
          this.handleClose(tags[j])
        }
      }
    }

  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  /deep/ .el-input-number .el-input__inner {
    text-align: left;
  }
  .el-tag {
    margin-left: 10px;
  }
  .add_btn {
    cursor:pointer;
  }
</style>
