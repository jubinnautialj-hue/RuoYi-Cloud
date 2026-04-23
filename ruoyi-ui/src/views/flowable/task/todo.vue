<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="任务名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入任务名称"
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
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="taskList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" type="index" width="50" align="center">
        <template slot-scope="scope">
          <span>{{(queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="任务名称" align="center" prop="name" :show-overflow-tooltip="true" />
      <el-table-column label="流程定义名称" align="center" prop="processDefinitionName" :show-overflow-tooltip="true" />
      <el-table-column label="流程定义标识" align="center" prop="processDefinitionKey" :show-overflow-tooltip="true" />
      <el-table-column label="流程实例ID" align="center" prop="processInstanceId" :show-overflow-tooltip="true" width="200" />
      <el-table-column label="任务负责人" align="center" prop="assignee" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.assignee" type="primary">
            {{ scope.row.assignee }}
          </el-tag>
          <el-tag v-else type="info">
            待认领
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="优先级" align="center" prop="priority" width="80" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="!scope.row.assignee"
            size="mini"
            type="text"
            icon="el-icon-s-claim"
            @click="handleClaim(scope.row)"
            v-hasPermi="['flowable:task:claim']"
          >认领</el-button>
          <el-button
            v-if="scope.row.assignee"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleComplete(scope.row)"
            v-hasPermi="['flowable:task:complete']"
          >办理</el-button>
          <el-button
            v-if="scope.row.assignee"
            size="mini"
            type="text"
            icon="el-icon-share"
            @click="handleDelegate(scope.row)"
            v-hasPermi="['flowable:task:delegate']"
          >委派</el-button>
          <el-button
            v-if="scope.row.assignee"
            size="mini"
            type="text"
            icon="el-icon-back"
            @click="handleUnclaim(scope.row)"
            v-hasPermi="['flowable:task:unclaim']"
          >取消认领</el-button>
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

    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" label-width="100px">
        <el-form-item label="任务名称">
          <el-input v-model="form.name" disabled />
        </el-form-item>
        <el-form-item label="任务描述">
          <el-input v-model="form.description" type="textarea" disabled />
        </el-form-item>
        <el-form-item label="流程变量">
          <el-input v-model="formVariables" type="textarea" placeholder="请输入流程变量(JSON格式，如：{\"key\":\"value\"})" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitComplete">提交</el-button>
        <el-button @click="cancel">取消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="delegateTitle" :visible.sync="delegateOpen" width="500px" append-to-body>
      <el-form ref="delegateForm" :model="delegateForm" :rules="delegateRules" label-width="100px">
        <el-form-item label="委派对象" prop="userId">
          <el-input v-model="delegateForm.userId" placeholder="请输入用户ID" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitDelegate">确定</el-button>
        <el-button @click="cancelDelegate">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listTodoTask, claimTask, unclaimTask, completeTask, delegateTask } from "@/api/flowable/task"

export default {
  name: "TodoTask",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      taskList: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
        processDefinitionKey: undefined
      },
      form: {},
      formVariables: "",
      delegateTitle: "",
      delegateOpen: false,
      delegateForm: {
        userId: undefined
      },
      delegateRules: {
        userId: [{ required: true, message: "请输入用户ID", trigger: "blur" }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listTodoTask(this.queryParams).then(response => {
        this.taskList = response.rows
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
    handleClaim(row) {
      this.$modal.confirm('是否确认认领编号为"' + row.id + '"的任务？').then(function() {
        return claimTask(row.id)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("认领成功")
      }).catch(() => {})
    },
    handleUnclaim(row) {
      this.$modal.confirm('是否确认取消认领编号为"' + row.id + '"的任务？').then(function() {
        return unclaimTask(row.id)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("取消认领成功")
      }).catch(() => {})
    },
    handleComplete(row) {
      this.reset()
      this.form = Object.assign({}, row)
      this.open = true
      this.title = "办理任务"
    },
    handleDelegate(row) {
      this.delegateForm = {
        taskId: row.id,
        userId: undefined
      }
      this.delegateTitle = "委派任务"
      this.delegateOpen = true
    },
    submitComplete() {
      let variables = {}
      if (this.formVariables) {
        try {
          variables = JSON.parse(this.formVariables)
        } catch (e) {
          this.$modal.msgError("流程变量格式错误，请输入正确的JSON格式")
          return
        }
      }
      completeTask(this.form.id, variables).then(response => {
        this.$modal.msgSuccess("办理成功")
        this.open = false
        this.getList()
      })
    },
    submitDelegate() {
      this.$refs["delegateForm"].validate(valid => {
        if (valid) {
          delegateTask(this.delegateForm.taskId, this.delegateForm.userId).then(response => {
            this.$modal.msgSuccess("委派成功")
            this.delegateOpen = false
            this.getList()
          })
        }
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    cancelDelegate() {
      this.delegateOpen = false
      this.delegateForm = {
        userId: undefined
      }
    },
    reset() {
      this.form = {
        id: undefined,
        name: undefined,
        description: undefined
      }
      this.formVariables = ""
      this.resetForm("form")
    }
  }
}
</script>
