import request from '@/utils/request'

export function listRunningInstance(query) {
  return request({
    url: '/flowable/instance/runningList',
    method: 'get',
    params: query
  })
}

export function listHistoryInstance(query) {
  return request({
    url: '/flowable/instance/historyList',
    method: 'get',
    params: query
  })
}

export function getInstance(processInstanceId) {
  return request({
    url: '/flowable/instance/' + processInstanceId,
    method: 'get'
  })
}

export function startInstanceById(processDefinitionId, businessKey, data) {
  return request({
    url: '/flowable/instance/startById',
    method: 'post',
    params: {
      processDefinitionId: processDefinitionId,
      businessKey: businessKey
    },
    data: data
  })
}

export function startInstanceByKey(processDefinitionKey, businessKey, data) {
  return request({
    url: '/flowable/instance/startByKey',
    method: 'post',
    params: {
      processDefinitionKey: processDefinitionKey,
      businessKey: businessKey
    },
    data: data
  })
}

export function suspendInstance(processInstanceId) {
  return request({
    url: '/flowable/instance/suspend/' + processInstanceId,
    method: 'put'
  })
}

export function activateInstance(processInstanceId) {
  return request({
    url: '/flowable/instance/activate/' + processInstanceId,
    method: 'put'
  })
}

export function delInstance(processInstanceId, deleteReason) {
  return request({
    url: '/flowable/instance/' + processInstanceId,
    method: 'delete',
    params: { deleteReason: deleteReason }
  })
}

export function getInstanceActivityList(processInstanceId) {
  return request({
    url: '/flowable/instance/activityList/' + processInstanceId,
    method: 'get'
  })
}
