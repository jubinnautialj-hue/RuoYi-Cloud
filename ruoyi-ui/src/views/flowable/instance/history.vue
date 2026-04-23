<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="流程名称" prop="processDefinitionName">
        <el-input
          v-model="queryParams.processDefinitionName"
          placeholder="请输入流程名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="流程标识" prop="processDefinitionKey">
        <el-input
          v-model="queryParams.processDefinitionKey"
          placeholder="请输入流程标识"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="业务标识" prop="businessKey">
        <el-input
          v-model="queryParams.businessKey"
          placeholder="请输入业务标识"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发起人" prop="startUserId">
        <el-input
          v-model="queryParams.startUserId"
          placeholder="请输入发起人"
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
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="instanceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" type="index" width="50" align="center">
        <template slot-scope="scope">
          <span>{{(queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="流程定义名称" align="center" prop="processDefinitionName" :show-overflow-tooltip="true" />
      <el-table-column label="流程定义标识" align="center" prop="processDefinitionKey" :show-overflow-tooltip="true" />
      <el-table-column label="流程实例ID" align="center" prop="id" :show-overflow-tooltip="true" width="200" />
      <el-table-column label="业务标识" align="center" prop="businessKey" :show-overflow-tooltip="true" />
      <el-table-column label="发起人" align="center" prop="startUserId" width="100" />
      <el-table-column label="开始时间" align="center" prop="startTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="执行时长" align="center" prop="duration" width="120">
        <template slot-scope="scope">
          <span v-if="scope.row.duration">{{ formatDuration(scope.row.duration) }}</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="删除原因" align="center" prop="deleteReason" :show-overflow-tooltip="true" width="150" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
          >详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-picture"
            @click="handleImage(scope.row)"
          >流程图</el-button>
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

    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="流程定义名称">{{ form.processDefinitionName }}</el-descriptions-item>
        <el-descriptions-item label="流程定义标识">{{ form.processDefinitionKey }}</el-descriptions-item>
        <el-descriptions-item label="流程实例ID">{{ form.id }}</el-descriptions-item>
        <el-descriptions-item label="业务标识">{{ form.businessKey || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="发起人">{{ form.startUserId || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="开始时间">{{ parseTime(form.startTime) }}</el-descriptions-item>
        <el-descriptions-item label="结束时间">{{ parseTime(form.endTime) || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="执行时长">{{ form.duration ? formatDuration(form.duration) : '-' }}</el-descriptions-item>
        <el-descriptions-item label="删除原因" :span="2">{{ form.deleteReason || '暂无' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <el-dialog :title="imageTitle" :visible.sync="imageOpen" width="800px" append-to-body>
      <div style="text-align: center">
        <el-image :src="imageUrl" fit="contain" />
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listHistoryInstance } from "@/api/flowable/instance"

export default {
  name: "HistoryInstance",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      instanceList: [],
      title: "",
      open: false,
      imageOpen: false,
      imageTitle: "",
      imageUrl: "",
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        processDefinitionName: undefined,
        processDefinitionKey: undefined,
        businessKey: undefined,
        startUserId: undefined,
        ended: true
      },
      form: {}
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listHistoryInstance(this.queryParams).then(response => {
        this.instanceList = response.rows
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleView(row) {
      this.form = Object.assign({}, row)
      this.open = true
      this.title = "历史流程实例详情"
    },
    handleImage(row) {
      this.imageTitle = row.processDefinitionName + "流程图"
      this.imageUrl = process.env.VUE_APP_BASE_API + "/flowable/definition/diagram/" + row.processDefinitionId
      this.imageOpen = true
    },
    formatDuration(milliseconds) {
      if (!milliseconds || milliseconds <= 0) {
        return '-'
      }
      const seconds = Math.floor(milliseconds / 1000)
      const minutes = Math.floor(seconds / 60)
      const hours = Math.floor(minutes / 60)
      const days = Math.floor(hours / 24)
      
      if (days > 0) {
        return `${days}天${hours % 24}小时`
      } else if (hours > 0) {
        return `${hours}小时${minutes % 60}分钟`
      } else if (minutes > 0) {
        return `${minutes}分钟${seconds % 60}秒`
      } else {
        return `${seconds}秒`
      }
    }
  }
}
</script>
