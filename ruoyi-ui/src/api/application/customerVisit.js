import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi"

export function listCustomerVisit(query) {
  return request({
    url: '/customer/visit/list',
    method: 'get',
    params: query
  })
}

export function listCustomerVisitByCustomerId(customerId) {
  return request({
    url: '/customer/visit/list/' + customerId,
    method: 'get'
  })
}

export function getCustomerVisit(id) {
  return request({
    url: '/customer/visit/' + parseStrEmpty(id),
    method: 'get'
  })
}

export function addCustomerVisit(data) {
  return request({
    url: '/customer/visit',
    method: 'post',
    data: data
  })
}

export function updateCustomerVisit(data) {
  return request({
    url: '/customer/visit',
    method: 'put',
    data: data
  })
}

export function delCustomerVisit(id) {
  return request({
    url: '/customer/visit/' + id,
    method: 'delete'
  })
}
