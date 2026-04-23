export default {
  data() {
    return {
      _isMousedownOnEditable: false,
      _originalCloseOnClickModal: null,
      _restoreCloseOnClickModalTimer: null,
      _dialogMixinInited: false
    }
  },
  mounted() {
    this._initDialogPreventCloseMixin()
  },
  beforeDestroy() {
    this._destroyDialogPreventCloseMixin()
  },
  methods: {
    _isEditableElement(target) {
      if (!target) return false
      
      const tagName = target.tagName && target.tagName.toLowerCase()
      
      if (tagName === 'input' || tagName === 'textarea') {
        return true
      }
      
      if (target.contentEditable === 'true' || target.isContentEditable === true) {
        return true
      }
      
      return false
    },
    _isDialogComponent() {
      if (this.$options.name === 'ElDialog') {
        return true
      }
      
      if (this.$el && this.$el.classList) {
        if (this.$el.classList.contains('el-dialog__wrapper')) {
          return true
        }
      }
      
      if (typeof this.closeOnClickModal !== 'undefined') {
        return true
      }
      
      return false
    },
    _initDialogPreventCloseMixin() {
      if (this._dialogMixinInited) return
      
      if (!this._isDialogComponent()) return
      
      this._dialogMixinInited = true

      this._handleDialogMousedown = (e) => {
        if (this._isEditableElement(e.target)) {
          this._isMousedownOnEditable = true
          this._originalCloseOnClickModal = this.closeOnClickModal
        }
      }

      this._handleDocumentMouseup = (e) => {
        if (!this._isMousedownOnEditable) return

        const dialogEl = this.$el
        if (dialogEl && !dialogEl.contains(e.target)) {
          if (this.closeOnClickModal) {
            this.closeOnClickModal = false

            if (this._restoreCloseOnClickModalTimer) {
              clearTimeout(this._restoreCloseOnClickModalTimer)
            }
            
            this._restoreCloseOnClickModalTimer = setTimeout(() => {
              this.closeOnClickModal = this._originalCloseOnClickModal
              this._restoreCloseOnClickModalTimer = null
            }, 0)
          }
        }

        this._isMousedownOnEditable = false
      }

      this.$el.addEventListener('mousedown', this._handleDialogMousedown, true)
      document.addEventListener('mouseup', this._handleDocumentMouseup, true)
    },
    _destroyDialogPreventCloseMixin() {
      if (!this._dialogMixinInited) return
      
      if (this._handleDialogMousedown && this.$el) {
        this.$el.removeEventListener('mousedown', this._handleDialogMousedown, true)
      }
      if (this._handleDocumentMouseup) {
        document.removeEventListener('mouseup', this._handleDocumentMouseup, true)
      }
      if (this._restoreCloseOnClickModalTimer) {
        clearTimeout(this._restoreCloseOnClickModalTimer)
        this._restoreCloseOnClickModalTimer = null
      }
      
      this._dialogMixinInited = false
    }
  }
}
