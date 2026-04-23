import request from '@/utils/request'

export function listDefinition(query) {
  return request({
    url: '/flowable/definition/list',
    method: 'get',
    params: query
  })
}

export function getDefinition(processDefinitionId) {
  return request({
    url: '/flowable/definition/' + processDefinitionId,
    method: 'get'
  })
}

export function deployDefinition(data) {
  return request({
    url: '/flowable/definition/deploy',
    method: 'post',
    data: data
  })
}

export function delDefinition(deploymentId) {
  return request({
    url: '/flowable/definition/' + deploymentId,
    method: 'delete'
  })
}

export function activateDefinition(processDefinitionId) {
  return request({
    url: '/flowable/definition/activate/' + processDefinitionId,
    method: 'put'
  })
}

export function suspendDefinition(processDefinitionId) {
  return request({
    url: '/flowable/definition/suspend/' + processDefinitionId,
    method: 'put'
  })
}
