import request from '@/utils/request'

// 查询三级分佣百分比列表
export function getHomePage() {
  return request({
    url: '/admin/getHomePage',
    method: 'get'
  })
}

export function pendingTask() {
  return request({
    url: '/admin/pendingTask',
    method: 'get'
  })
}

export function LanguageSetting(query) {
  return request({
    url: '/task/users/LanguageSetting',
    method: 'get',
    params: query
  })
}

export function getLanguage() {
  return request({
    url: '/task/users/getLanguage',
    method: 'get'
  })
}
