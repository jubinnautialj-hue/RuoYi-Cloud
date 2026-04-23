import request from '@/utils/request'

export function listTodoTask(query) {
  return request({
    url: '/flowable/task/todoList',
    method: 'get',
    params: query
  })
}

export function listDoneTask(query) {
  return request({
    url: '/flowable/task/doneList',
    method: 'get',
    params: query
  })
}

export function listMyProcess(query) {
  return request({
    url: '/flowable/task/myProcess',
    method: 'get',
    params: query
  })
}

export function getTask(taskId) {
  return request({
    url: '/flowable/task/' + taskId,
    method: 'get'
  })
}

export function claimTask(taskId) {
  return request({
    url: '/flowable/task/claim/' + taskId,
    method: 'post'
  })
}

export function unclaimTask(taskId) {
  return request({
    url: '/flowable/task/unclaim/' + taskId,
    method: 'post'
  })
}

export function completeTask(taskId, data) {
  return request({
    url: '/flowable/task/complete/' + taskId,
    method: 'post',
    data: data
  })
}

export function delegateTask(taskId, userId) {
  return request({
    url: '/flowable/task/delegate/' + taskId,
    method: 'post',
    params: { userId: userId }
  })
}

export function resolveTask(taskId, data) {
  return request({
    url: '/flowable/task/resolve/' + taskId,
    method: 'post',
    data: data
  })
}

export function assignTask(taskId, userId) {
  return request({
    url: '/flowable/task/assign/' + taskId,
    method: 'post',
    params: { userId: userId }
  })
}

export function addComment(taskId, processInstanceId, message) {
  return request({
    url: '/flowable/task/comment/' + taskId,
    method: 'post',
    params: {
      processInstanceId: processInstanceId,
      message: message
    }
  })
}
