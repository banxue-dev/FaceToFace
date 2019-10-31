<template>
  <el-dialog :append-to-body="true" :close-on-click-modal="false" :before-close="cancel" :visible.sync="dialog" :title="isAdd ? '新增' : '编辑'" width="500px">
    <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
      <el-form-item label="频道属性（0音频，1音视频）" >
        <el-input v-model="form.attr" style="width: 370px;"/>
      </el-form-item>
      <el-form-item label="频道名" >
        <el-input v-model="form.channelsName" style="width: 370px;"/>
      </el-form-item>
      <el-form-item label="创建时间" >
        <el-input v-model="form.createTime" style="width: 370px;"/>
      </el-form-item>
      <el-form-item label="创建用户" >
        <el-input v-model="form.createUser" style="width: 370px;"/>
      </el-form-item>
      <el-form-item label="频道内最大人数" >
        <el-input v-model="form.maxPersonNumber" style="width: 370px;"/>
      </el-form-item>
      <el-form-item label="频道模式（0申请发言，1自由发言，2静默）" >
        <el-input v-model="form.mode" style="width: 370px;"/>
      </el-form-item>
      <el-form-item label="频道录音开关(0开，1关)" >
        <el-input v-model="form.recordSwitch" style="width: 370px;"/>
      </el-form-item>
      <el-form-item label="修改时间" >
        <el-input v-model="form.updateTime" style="width: 370px;"/>
      </el-form-item>
      <el-form-item label="修改用户" >
        <el-input v-model="form.updateUser" style="width: 370px;"/>
      </el-form-item>
      <el-form-item label="组织ID" >
        <el-input v-model="form.deptId" style="width: 370px;"/>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="text" @click="cancel">取消</el-button>
      <el-button :loading="loading" type="primary" @click="doSubmit">确认</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { add, edit } from '@/api/channelsInfo'
export default {
  props: {
    isAdd: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      loading: false, dialog: false,
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
      rules: {
      }
    }
  },
  methods: {
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
        console.log(err.response.data.message)
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
        console.log(err.response.data.message)
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
    }
  }
}
</script>

<style scoped>

</style>
