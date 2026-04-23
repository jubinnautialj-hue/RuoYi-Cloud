<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="流程名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入流程名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="流程标识" prop="key">
        <el-input
          v-model="queryParams.key"
          placeholder="请输入流程标识"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="流程分类" prop="category">
        <el-input
          v-model="queryParams.category"
          placeholder="请输入流程分类"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-upload
          class="upload-demo"
          :action="uploadUrl"
          :headers="uploadHeaders"
          :before-upload="beforeUpload"
          :on-success="handleUploadSuccess"
          :on-error="handleUploadError"
          :limit="1"
          :show-file-list="false"
          accept=".bpmn,.bpmn20.xml,.xml"
          v-hasPermi="['flowable:definition:deploy']"
        >
          <el-button type="primary" plain icon="el-icon-upload2" size="mini">上传部署</el-button>
        </el-upload>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['flowable:definition:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="definitionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" type="index" width="50" align="center">
        <template slot-scope="scope">
          <span>{{(queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="流程名称" align="center" prop="name" :show-overflow-tooltip="true" />
      <el-table-column label="流程标识" align="center" prop="key" :show-overflow-tooltip="true" />
      <el-table-column label="流程分类" align="center" prop="category" :show-overflow-tooltip="true" />
      <el-table-column label="版本号" align="center" prop="version" width="80" />
      <el-table-column label="部署时间" align="center" prop="deploymentTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.deploymentTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="suspended" width="80">
        <template slot-scope="scope">
          <el-tag :type="scope.row.suspended ? 'danger' : 'success'">
            {{ scope.row.suspended ? '挂起' : '激活' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-picture"
            @click="handleImage(scope.row)"
          >流程图</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-document"
            @click="handleXml(scope.row)"
          >XML</el-button>
          <el-button
            v-if="!scope.row.suspended"
            size="mini"
            type="text"
            icon="el-icon-video-pause"
            @click="handleSuspend(scope.row)"
            v-hasPermi="['flowable:definition:edit']"
          >挂起</el-button>
          <el-button
            v-if="scope.row.suspended"
            size="mini"
            type="text"
            icon="el-icon-video-play"
            @click="handleActivate(scope.row)"
            v-hasPermi="['flowable:definition:edit']"
          >激活</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['flowable:definition:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog :title="imageTitle" :visible.sync="imageOpen" width="800px" append-to-body>
      <div style="text-align: center">
        <el-image :src="imageUrl" fit="contain" />
      </div>
    </el-dialog>

    <el-dialog :title="xmlTitle" :visible.sync="xmlOpen" width="800px" append-to-body>
      <div style="white-space: pre-wrap; word-wrap: break-word; background: #f4f4f5; padding: 10px; border-radius: 4px;">
        <pre>{{ xmlContent }}</pre>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDefinition, delDefinition, activateDefinition, suspendDefinition } from "@/api/flowable/definition"
import { getToken } from "@/utils/auth"

export default {
  name: "Definition",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      definitionList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
        key: undefined,
        category: undefined
      },
      uploadUrl: process.env.VUE_APP_BASE_API + "/flowable/definition/deploy",
      uploadHeaders: {
        Authorization: "Bearer " + getToken()
      },
      imageOpen: false,
      imageTitle: "",
      imageUrl: "",
      xmlOpen: false,
      xmlTitle: "",
      xmlContent: ""
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listDefinition(this.queryParams).then(response => {
        this.definitionList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.deploymentId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    beforeUpload(file) {
      const isBpmn = file.name.endsWith(".bpmn") || file.name.endsWith(".bpmn20.xml") || file.name.endsWith(".xml")
      if (!isBpmn) {
        this.$modal.msgError("请上传bpmn格式文件!")
        return false
      }
      return true
    },
    handleUploadSuccess(response) {
      if (response.code === 200) {
        this.$modal.msgSuccess("部署成功")
        this.getList()
      } else {
        this.$modal.msgError(response.msg)
      }
    },
    handleUploadError() {
      this.$modal.msgError("部署失败")
    },
    handleImage(row) {
      this.imageTitle = row.name + "流程图"
      this.imageUrl = process.env.VUE_APP_BASE_API + "/flowable/definition/diagram/" + row.id
      this.imageOpen = true
    },
    handleXml(row) {
      this.xmlTitle = row.name + "XML内容"
      this.xmlOpen = true
      this.xmlContent = "请查看资源文件: " + row.resourceName
    },
    handleSuspend(row) {
      this.$modal.confirm('是否确认挂起编号为"' + row.id + '"的流程定义？').then(function() {
        return suspendDefinition(row.id)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("挂起成功")
      }).catch(() => {})
    },
    handleActivate(row) {
      this.$modal.confirm('是否确认激活编号为"' + row.id + '"的流程定义？').then(function() {
        return activateDefinition(row.id)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("激活成功")
      }).catch(() => {})
    },
    handleDelete(row) {
      const ids = row.deploymentId || this.ids
      this.$modal.confirm('是否确认删除部署编号为"' + ids + '"的数据项？').then(function() {
        return delDefinition(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    }
  }
}
</script>
