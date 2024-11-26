import request from '@/utils/request'

// 查询产品列表
export function listSpproduct(query) {
  return request({
    url: '/task/spproduct/list',
    method: 'get',
    params: query
  })
}

// 查询产品详细
export function getSpproduct(id) {
  return request({
    url: '/task/spproduct/' + id,
    method: 'get'
  })
}

// 新增产品
export function addSpproduct(data) {
  return request({
    url: '/task/spproduct',
    method: 'post',
    data: data
  })
}

// 修改产品
export function updateSpproduct(data) {
  return request({
    url: '/task/spproduct',
    method: 'put',
    data: data
  })
}

// 删除产品
export function delSpproduct(id) {
  return request({
    url: '/task/spproduct/' + id,
    method: 'delete'
  })
}
