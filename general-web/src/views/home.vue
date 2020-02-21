<template>
  <div class="dashboard-container">
    <div class="dashboard-editor-container">
      <el-row :gutter="32">
        <el-col :xs="24" :sm="24" :lg="12">
          <div class="chart-wrapper">
            <user-line-chart/>
          </div>
        </el-col>
        <el-col :xs="24" :sm="24" :lg="12">
          <div class="chart-wrapper">
            <channels-line-chart/>
          </div>
        </el-col>
      </el-row>
      <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
        <!--表格渲染-->
        <el-card class="box-card" shadow="never">
          <div slot="header" class="clearfix">
            <span class="role-span">七天详细数据</span>
          </div>
          <el-table v-loading="loading" :data="listData" size="small" style="width: 100%;" border>
            <el-table-column prop="time" label="日期" align="center"/>
            <el-table-column prop="addUserCount" label="新增用户" align="center"/>
            <el-table-column prop="userCount" label="用户总数" align="center"/>
            <el-table-column prop="addchannelsCount" label="新增频道" align="center"/>
            <el-table-column prop="channelsCount" label="频道总数" align="center"/>
          </el-table>
        </el-card>
      </el-row>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import UserLineChart from './dashboard/UserLineChart'
import ChannelsLineChart from './dashboard/ChannelsLineChart'
import { getUserChannelsStatistics } from '@/api/home'
// import { count } from '@/api/visits'

/**
     * 记录访问，只有页面刷新或者第一次加载才会记录
     */
// count().then(res => {
// })

export default {
  name: 'Dashboard',
  components: {
    UserLineChart,
    ChannelsLineChart
  },
  data: function() {
    return {
      listData: []
    }
  },
  computed: {
    ...mapGetters([
      'roles'
    ])
  },
  mounted() {
    getUserChannelsStatistics().then(res => {
      this.listData = res
    })
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  .role-span {
    font-weight: bold;
    color: #303133;
    font-size: 15px;
  }

  .dashboard-editor-container {
    padding: 18px 22px 22px 22px;
  }
</style>
