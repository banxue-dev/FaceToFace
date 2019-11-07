import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/channelsInfo',
    method: 'post',
    data
  })
}

export function del(id) {
  return request({
    url: 'api/channelsInfo/' + id,
    method: 'delete'
  })
}

export function edit(data) {
  return request({
    url: 'api/channelsInfo',
    method: 'put',
    data
  })
}

// 根据组织ID集合查询频道
export function listChannelsInfosByDeptId(params) {
  return request({
    url: 'api/listChannelsInfosByDeptId',
    method: 'get',
    params
  })
}
