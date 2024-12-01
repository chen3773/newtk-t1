import request from '@/utils/request'

// 查询地址列表
export function listUserAddresses(query) {
  return request({
    url: '/task/userAddresses/list',
    method: 'get',
    params: query
  })
}

// 查询地址详细
export function getUserAddresses(addressId) {
  return request({
    url: '/task/userAddresses/' + addressId,
    method: 'get'
  })
}

// 新增地址
export function addUserAddresses(data) {
  return request({
    url: '/task/userAddresses',
    method: 'post',
    data: data
  })
}

// 修改地址
export function updateUserAddresses(data) {
  return request({
    url: '/task/userAddresses',
    method: 'put',
    data: data
  })
}

// 删除地址
export function delUserAddresses(addressId) {
  return request({
    url: '/task/userAddresses/' + addressId,
    method: 'delete'
  })
}
