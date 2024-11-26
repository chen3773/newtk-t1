import request from '@/utils/request'

// 查询商品分类列表
export function listGroups(query) {
  return request({
    url: '/task/groups/list',
    method: 'get',
    params: query
  })
}

// 查询商品分类详细
export function getGroups(id) {
  return request({
    url: '/task/groups/' + id,
    method: 'get'
  })
}

// 新增商品分类
export function addGroups(data) {
  return request({
    url: '/task/groups',
    method: 'post',
    data: data
  })
}

// 修改商品分类
export function updateGroups(data) {
  return request({
    url: '/task/groups',
    method: 'put',
    data: data
  })
}

// 删除商品分类
export function delGroups(id) {
  return request({
    url: '/task/groups/' + id,
    method: 'delete'
  })
}
