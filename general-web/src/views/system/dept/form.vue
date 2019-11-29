<template>
  <el-dialog :append-to-body="true" :close-on-click-modal="false" :before-close="cancel" :visible.sync="dialog" :title="isAdd ? '新增组织' : '编辑组织'" width="500px">
    <el-form ref="form" :model="form" :rules="rules" size="small" label-width="100px">
      <el-form-item label="名称" prop="name">
        <el-input v-model="form.name" style="width: 350px;"/>
      </el-form-item>
      <el-form-item v-if="form.pid !== 0" label="状态" prop="enabled">
        <el-radio v-for="item in dicts" :key="item.id" v-model="form.enabled" :label="item.value">{{ item.label }}</el-radio>
      </el-form-item>
      <el-form-item v-if="!isAdd && !form.ifTop" label="上级组织">
        <treeselect v-model="form.pid" :options="depts" style="width: 350px;" :disabled="isAdd ? false : true" placeholder="请选择上级组织" />
      </el-form-item>
	  <el-form-item v-if="isAdd " label="上级组织" >
        <treeselect v-model="form.pid"  :options="depts" style="width: 350px;" :disabled="isAdd ? false : true" placeholder="请选择上级组织(默认当前)" />
      </el-form-item>
      <el-form-item label="本级账号上限" prop="maxPersonNumber">
        <el-input-number v-model.number="form.maxPersonNumber" :min="0" controls-position="right" style="width: 350px;"/>
      </el-form-item>
	  <el-form-item label="子集账号上限" prop="maxPersonNumber">
        <el-input-number v-model.number="form.childMaxPersonNumber" :min="0" controls-position="right" style="width: 350px;"/>
      </el-form-item>
      <el-form-item label="企业识别码" prop="enterpriseCode">
        <el-input v-model="form.enterpriseCode" style="width: 350px;"/>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="text" @click="cancel">取消</el-button>
      <el-button :loading="loading" type="primary" @click="doSubmit">确认</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { add, edit, getDepts } from '@/api/dept'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
export default {
  components: { Treeselect },
  props: {
    isAdd: {
      type: Boolean,
      required: true
    },
    dicts: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      loading: false, dialog: false, depts: [],
      form: {
        id: '',
        name: '',
        pid: -1,
		ifTop:false,
        enabled: 'true',
        enterpriseCode: '',
        maxPersonNumber: '',
        childMaxPersonNumber: '',
        user: { id: null }
      },
      rules: {
        name: [
          { required: true, message: '请输入名称', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    cancel() {
      this.resetForm()
    },
    doSubmit() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (this.form.pid !== -1) {
            this.loading = true
            if (this.isAdd) {
              this.doAdd()
            } else this.doEdit()
          } else {
            this.$message({
              message: '上级组织不能为空',
              type: 'warning'
            })
          }
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
      this.form = {
        id: '',
        name: '',
        pid: 1,
        enabled: 'true'
      }
    },
    getDepts() {
      getDepts({ enabled: true }).then(res => {
		if(this.isAdd){
			this.form.pid=res.content[0].id;
		}else{
			if(this.form.isTop){
				this.form.pid=this.form.id
			}
		}
        this.depts = res.content
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  /deep/ .el-input-number .el-input__inner {
    text-align: left;
  }
</style>
