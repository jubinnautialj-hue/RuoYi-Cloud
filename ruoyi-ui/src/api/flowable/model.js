import request from '@/utils/request'

export function listModel(query) {
  return request({
    url: '/flowable/model/list',
    method: 'get',
    params: query
  })
}

export function getModel(modelId) {
  return request({
    url: '/flowable/model/' + modelId,
    method: 'get'
  })
}

export function addModel(data) {
  return request({
    url: '/flowable/model',
    method: 'post',
    data: data
  })
}

export function updateModel(data) {
  return request({
    url: '/flowable/model',
    method: 'put',
    data: data
  })
}

export function delModel(modelId) {
  return request({
    url: '/flowable/model/' + modelId,
    method: 'delete'
  })
}

export function deployModel(modelId) {
  return request({
    url: '/flowable/model/deploy/' + modelId,
    method: 'post'
  })
}

export function saveEditor(modelId, jsonXml, svgXml) {
  return request({
    url: '/flowable/model/saveEditor',
    method: 'post',
    params: {
      modelId: modelId,
      json_xml: jsonXml,
      svg_xml: svgXml
    }
  })
}

export function getEditorSource(modelId) {
  return request({
    url: '/flowable/model/editor/' + modelId,
    method: 'get'
  })
}
