let isSelectingTextInDialog = false
let preventNextModalClick = false
let preventClickTimeout = null

function isEditableElement(target) {
  if (!target) return false
  const tagName = target.tagName && target.tagName.toLowerCase()
  if (tagName === 'input' || tagName === 'textarea') return true
  if (target.contentEditable === 'true' || target.isContentEditable) return true
  return false
}

function isInsideDialog(target) {
  if (!target) return false
  let current = target
  while (current) {
    if (current.classList && current.classList.contains('el-dialog')) return true
    current = current.parentElement
  }
  return false
}

function isModalOrWrapper(target) {
  if (!target) return false
  if (target.classList) {
    if (target.classList.contains('el-dialog__wrapper')) return true
    if (target.classList.contains('v-modal')) return true
  }
  return false
}

function initDialogFix() {
  document.addEventListener('mousedown', (e) => {
    if (preventClickTimeout) {
      clearTimeout(preventClickTimeout)
      preventClickTimeout = null
    }
    
    if (isEditableElement(e.target) && isInsideDialog(e.target)) {
      isSelectingTextInDialog = true
      preventNextModalClick = false
    } else {
      isSelectingTextInDialog = false
      preventNextModalClick = false
    }
  }, true)

  document.addEventListener('mousemove', (e) => {
    if (isSelectingTextInDialog) {
      if (!isInsideDialog(e.target)) {
        preventNextModalClick = true
      }
    }
  }, true)

  document.addEventListener('mouseup', (e) => {
    if (isSelectingTextInDialog && preventNextModalClick) {
      if (isModalOrWrapper(e.target) || !isInsideDialog(e.target)) {
        e.stopImmediatePropagation()
        e.stopPropagation()
        
        preventClickTimeout = setTimeout(() => {
          isSelectingTextInDialog = false
          preventNextModalClick = false
          preventClickTimeout = null
        }, 200)
        
        return
      }
    }
    
    isSelectingTextInDialog = false
    preventNextModalClick = false
  }, true)

  document.addEventListener('click', (e) => {
    if (preventNextModalClick && isModalOrWrapper(e.target)) {
      e.stopImmediatePropagation()
      e.stopPropagation()
      e.preventDefault()
      
      if (preventClickTimeout) {
        clearTimeout(preventClickTimeout)
      }
      
      preventClickTimeout = setTimeout(() => {
        preventNextModalClick = false
        isSelectingTextInDialog = false
        preventClickTimeout = null
      }, 0)
      
      return false
    }
  }, true)
}

export default {
  install(Vue) {
    initDialogFix()
  }
}

export { initDialogFix }
