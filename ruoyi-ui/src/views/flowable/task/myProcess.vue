<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="流程标识" prop="processDefinitionKey">
        <el-input
          v-model="queryParams.processDefinitionKey"
          placeholder="请输入流程标识"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="queryParams.ended" placeholder="请选择状态" clearable>
          <el-option label="运行中" :value="false" />
          <el-option label="已结束" :value="true" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="processList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" type="index" width="50" align="center">
        <template slot-scope="scope">
          <span>{{(queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="流程定义名称" align="center" prop="processDefinitionName" :show-overflow-tooltip="true" />
      <el-table-column label="流程定义标识" align="center" prop="processDefinitionKey" :show-overflow-tooltip="true" />
      <el-table-column label="流程实例ID" align="center" prop="processInstanceId" :show-overflow-tooltip="true" width="200" />
      <el-table-column label="业务标识" align="center" prop="businessKey" :show-overflow-tooltip="true" />
      <el-table-column label="开始时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="ended" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.ended ? 'info' : 'success'">
            {{ scope.row.ended ? '已结束' : '运行中' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
          >详情</el-button>
          <el-button
            v-if="!scope.row.ended"
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
        <el-descriptions-item label="流程实例ID">{{ form.processInstanceId }}</el-descriptions-item>
        <el-descriptions-item label="业务标识">{{ form.businessKey || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="开始时间">{{ parseTime(form.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="结束时间">{{ parseTime(form.endTime) || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="form.ended ? 'info' : 'success'">
            {{ form.ended ? '已结束' : '运行中' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="删除原因">{{ form.deleteReason || '暂无' }}</el-descriptions-item>
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
import { listMyProcess } from "@/api/flowable/task"

export default {
  name: "MyProcess",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      processList: [],
      title: "",
      open: false,
      imageOpen: false,
      imageTitle: "",
      imageUrl: "",
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        processDefinitionKey: undefined,
        ended: undefined
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
      listMyProcess(this.queryParams).then(response => {
        this.processList = response.rows
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
      this.ids = selection.map(item => item.processInstanceId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleView(row) {
      this.form = Object.assign({}, row)
      this.open = true
      this.title = "流程详情"
    },
    handleImage(row) {
      this.imageTitle = row.processDefinitionName + "流程图"
      this.imageUrl = process.env.VUE_APP_BASE_API + "/flowable/instance/diagram/" + row.processInstanceId
      this.imageOpen = true
    }
  }
}
</script>
