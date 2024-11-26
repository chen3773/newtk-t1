import request from '@/utils/request'

// 查询订单信息列表
export function listOrders(query) {
  return request({
    url: '/task/orders/list',
    method: 'get',
    params: query
  })
}

// 查询订单信息详细
export function getOrders(orderId) {
  return request({
    url: '/task/orders/' + orderId,
    method: 'get'
  })
}

// 新增订单信息
export function addOrders(data) {
  return request({
    url: '/task/orders',
    method: 'post',
    data: data
  })
}

// 修改订单信息
export function updateOrders(data) {
  return request({
    url: '/task/orders',
    method: 'put',
    data: data
  })
}

// 删除订单信息
export function delOrders(orderId) {
  return request({
    url: '/task/orders/' + orderId,
    method: 'delete'
  })
}
