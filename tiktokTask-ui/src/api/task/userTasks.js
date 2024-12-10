import request from '@/utils/request'

// 查询用户指定对应等级任务列表
export function listUserTasks(query) {
  return request({
    url: '/task/userTasks/list',
    method: 'get',
    params: query
  })
}

// 查询用户指定对应等级任务详细
export function getUserTasks(id) {
  return request({
    url: '/task/userTasks/' + id,
    method: 'get'
  })
}

// 新增用户指定对应等级任务
export function addUserTasks(data) {
  return request({
    url: '/task/userTasks',
    method: 'post',
    data: data
  })
}

// 修改用户指定对应等级任务
export function updateUserTasks(data) {
  return request({
    url: '/task/userTasks',
    method: 'put',
    data: data
  })
}

// 删除用户指定对应等级任务
export function delUserTasks(id) {
  return request({
    url: '/task/userTasks/' + id,
    method: 'delete'
  })
}
