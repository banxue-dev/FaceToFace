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
          <el-select v-model="form.channels_mode" style="width: 370px" placeholder="请选择频道模式">
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
          <el-radio v-for="item in dictMap.channels_record_switch" :key="item.id" v-model="form.channels_record_switch" :label="item.value">{{ item.label }}</el-radio>
        </el-form-item>
        <el-form-item label="管理员" >
          <el-card class="box-card" style="width: 370px;">
            <el-tag
              :key="tag.key"
              v-for="tag in adminTags"
              closable
              color="#FFFFFF"
              @close="handleClose(tag)">
              {{ tag.username }}
            </el-tag>
            <el-tag color="#FFFFFF" class="add_btn" @click="selectUserDialog(0)">
              <i class="el-icon-plus"/>
            </el-tag>
          </el-card>
        </el-form-item>
        <el-form-item label="人员" >
          <el-card class="box-card" style="width: 370px;">
            <el-tag
              :key="tag.key"
              v-for="tag in userTags"
              closable
              color="#FFFFFF"
              @close="handleClose(tag)">
              {{ tag.username }}
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
    <el-dialog :append-to-body="true" :close-on-click-modal="false" :before-close="userDialogClose" :visible.sync="userDialog" :title="userTitle" width="500px">
      <el-select v-model="selectUsers" filterable placeholder="请选择用户">
        <el-option v-for="item in users" :key="item.id" :label="item.username" :value="item"/>
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
      loading: false, dialog: false, userDialog: false, depts: [], deptId: null, users: [], adminTags: [], userTags: [], userTitle: '', selectType: null,
      form: {
        id: '',
        attr: '',
        channelsName: '',
        createTime: '',
        createUser: '',
        maxPersonNumber: '',
        mode: '',
        recordSwitch: '',
        updateTime: '',
        updateUser: '',
        deptId: ''
      },
      selectUsers: [],
      rules: {
        channelsName: [
          { required: true, message: '请输入频道名称', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    handleClose(tag) {
      this.users.splice(this.users.indexOf(tag), 1)
    },
    cancel() {
      this.resetForm()
    },
    doSubmit() {
      this.loading = true
      if (this.isAdd) {
        this.doAdd()
      } else this.doEdit()
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
      this.form = {
        id: '',
        attr: '',
        channelsName: '',
        createTime: '',
        createUser: '',
        maxPersonNumber: '',
        mode: '',
        recordSwitch: '',
        updateTime: '',
        updateUser: '',
        deptId: ''
      }
    },
    getDepts() {
      getDepts({ enabled: true }).then(res => {
        this.depts = res.content
      })
    },
    selectDept(node, instanceId) {
      getUsers({ deptId: node.id }).then(res => {
        this.users = res
      })
    },
    selectUserDialog(type) {
      if (this.users.length === 0) {
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
        this.selectType = type
        this.userDialog = true
      }
    },
    userDialogClose() {
      this.userDialog = false
    },
    userDialogConfirm() {
      // var userArry = {}
      // if(this.selectType === 0) {
      //     userArry.key()
      //     userArry.name
      // } else {
      //
      // }
      console.log(this.selectUsers)
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
