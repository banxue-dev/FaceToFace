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
