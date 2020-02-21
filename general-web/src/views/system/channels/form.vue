<template>
  <div>
    <el-dialog :append-to-body="true" :close-on-click-modal="false" :before-close="cancel" :visible.sync="dialog" :title="isAdd ? '新增' : '编辑'" width="500px">
      <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
        <el-form-item label="频道名称" prop="channelsName">
          <el-input v-model="form.channelsName" style="width: 370px;"/>
        </el-form-item>
        <el-form-item label="组织">
          <treeselect v-model="deptId" :options="depts" style="width: 370px" placeholder="选择组织" @select="selectDept"/>
        </el-form-item>
        <el-form-item label="频道属性">
          <el-select v-model="form.attr" style="width: 370px" placeholder="请选择频道属性">
            <el-option
              v-for="item in dictMap.channels_attr"
              :key="item.id"
              :label="item.label"
              :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="频道模式" >
          <el-select v-model="form.mode" style="width: 370px" placeholder="请选择频道模式">
            <el-option
              v-for="item in dictMap.channels_mode"
              :key="item.id"
              :label="item.label"
              :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="容量" >
          <el-input-number v-model.number="form.maxPersonNumber" :min="0" controls-position="right" style="width: 370px;"/>
        </el-form-item>
        <el-form-item label="录音开关" >
          <el-radio v-for="item in dictMap.channels_record_switch" :key="item.id" v-model="form.recordSwitch" :label="item.value">{{ item.label }}</el-radio>
        </el-form-item>
        <el-form-item label="管理员" >
          <el-card class="box-card" style="width: 370px;">
            <el-tag
              v-for="tag in adminTags"
              :key="tag.key"
              closable
              color="#FFFFFF"
              @close="handleClose(tag,0)">
              {{ tag.name }}
            </el-tag>
            <el-tag color="#FFFFFF" class="add_btn" @click="selectUserDialog(0)">
              <i class="el-icon-plus"/>
            </el-tag>
          </el-card>
        </el-form-item>
        <el-form-item label="人员" >
          <el-card class="box-card" style="width: 370px;">
            <el-tag
              v-for="tag in userTags"
              :key="tag.key"
              closable
              color="#FFFFFF"
              @close="handleClose(tag,1)">
              {{ tag.name }}
            </el-tag>
            <el-tag color="#FFFFFF" class="add_btn" @click="selectUserDialog(1)">
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
    <el-dialog :append-to-body="true" :close-on-click-modal="false" :before-close="userDialogClose" :visible.sync="userDialog" :title="userTitle" width="400px">
      <el-select v-model="selUsersIds" multiple placeholder="请选择用户">
        <el-option v-for="item in usersList" :key="item.id" :label="item.name" :value="item.id"/>
      </el-select>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="userDialogClose">取消</el-button>
        <el-button :loading="loading" type="primary" @click="userDialogConfirm">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { add, edit } from '@/api/channelsInfo'
import { getDepts } from '@/api/dept'
import { getUsers } from '@/api/user'
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
      loading: false, dialog: false, userDialog: false, depts: [], deptId: null, usersList: [], adminTags: [], userTags: [], userTitle: '',
      selUsersIds: [],
      form: {
        attr: '',
        channelsName: '',
        maxPersonNumber: '',
        mode: '',
        recordSwitch: '',
        dept: { id: '' },
        userSet: [],
        userAdmin: []
      },
      rules: {
        channelsName: [
          { required: true, message: '请输入频道名称', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    handleClose(tag, type) {
      if (type === 0) {
        this.adminTags.splice(this.adminTags.indexOf(tag), 1)
        this.form.userAdmin.splice(this.form.userAdmin.indexOf(tag.id), 1)
      } else {
        this.userTags.splice(this.userTags.indexOf(tag), 1)
        this.form.userSet.splice(this.form.userSet.indexOf(tag.id), 1)
      }
    },
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
          } else {
            this.loading = true
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
      this.depts = []
      this.usersList = []
      this.adminTags = []
      this.userTags = []
      this.form = {
        attr: '',
        channelsName: '',
        maxPersonNumber: '',
        mode: '',
        recordSwitch: '',
        dept: { id: '' },
        userSet: [],
        userAdmin: []
      }
    },
    getDepts() {
      getDepts({ enabled: true }).then(res => {
        this.depts = res.content
      })
    },
    selectDept(node, instanceId) {
      getUsers({ deptId: node.id }).then(res => {
        this.usersList = res
      })
    },
    selectUserByDeptId(deptId) {
      getUsers({ deptId: deptId }).then(res => {
        this.usersList = res
      })
    },
    selectUserDialog(type) {
      if (this.usersList.length === 0) {
        this.$notify({
          title: '请选择组织',
          type: 'warning',
          duration: 2500
        })
      } else {
        if (type === 0) {
          // 选择管理员
          this.userTitle = '请选择管理员'
        } else {
          // 选择人员
          this.userTitle = '请选择人员'
        }
        this.selUsersIds = []
        this.selectType = type
        this.userDialog = true
      }
    },
    userDialogClose() {
      this.userDialog = false
    },
    userDialogConfirm() {
      // 判断
      let existAdminUser = ''
      const adminTags = this.adminTags
      for (let j = 0; j < adminTags.length; j++) {
        for (let x = 0; x < this.selUsersIds.length; x++) {
          if (this.selUsersIds[x] === adminTags[j].id) {
            existAdminUser += adminTags[j].name + ','
          }
        }
      }
      if (existAdminUser.length > 0) {
        existAdminUser = existAdminUser.substring(0, existAdminUser.length - 1)
        this.$notify({
          title: '用户 ' + existAdminUser + ' 已经存在管理员列表中',
          type: 'warning',
          duration: 2500
        })
        return
      }
      let existUser = ''
      const userTags = this.userTags
      for (let j = 0; j < userTags.length; j++) {
        for (let x = 0; x < this.selUsersIds.length; x++) {
          if (this.selUsersIds[x] === userTags[j].id) {
            existUser += userTags[j].name + ','
          }
        }
      }
      if (existUser.length > 0) {
        existUser = existUser.substring(0, existUser.length - 1)
        this.$notify({
          title: '用户 ' + existUser + ' 已经存在管理员列表中',
          type: 'warning',
          duration: 2500
        })
        return
      }
      const arry = this.usersList
      for (let i = 0; i < arry.length; i++) {
        for (let x = 0; x < this.selUsersIds.length; x++) {
          if (arry[i].id === this.selUsersIds[x]) {
            const obj = {}
            obj.id = arry[i].id
            obj.name = arry[i].name
            if (this.selectType === 0) {
              // 管理员
              this.form.userAdmin.push({ id: arry[i].id })
              this.adminTags.push(obj)
            } else {
              this.form.userSet.push({ id: arry[i].id })
              this.userTags.push(obj)
            }
          }
        }
      }
      this.userDialog = false
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
