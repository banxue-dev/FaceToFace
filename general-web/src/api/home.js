import request from '@/utils/request'

export function getUserChartData() {
  return request({
    url: 'api/home/getUserChartData',
    method: 'get'
  })
}

export function getChannelsChartData() {
  return request({
    url: 'api/home/getChannelsChartData',
    method: 'get'
  })
}

export function getUserChannelsStatistics() {
  return request({
    url: 'api/home/getUserChannelsStatistics',
    method: 'get'
  })
}
