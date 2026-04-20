<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="客户姓名" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入客户姓名"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="手机号" prop="mobile">
        <el-input
          v-model="queryParams.mobile"
          placeholder="请输入手机号"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="性别" prop="sex">
        <el-select v-model="queryParams.sex" placeholder="请选择性别" clearable style="width: 240px">
          <el-option label="未知" :value="0" />
          <el-option label="男" :value="1" />
          <el-option label="女" :value="2" />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="dateRange"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['app:customer:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['app:customer:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['app:customer:del']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="customerList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="客户编号" align="center" prop="id" v-if="columns.id.visible" />
      <el-table-column label="客户姓名" align="center" prop="name" v-if="columns.name.visible" />
      <el-table-column label="手机号" align="center" prop="mobile" v-if="columns.mobile.visible" />
      <el-table-column label="性别" align="center" prop="sex" v-if="columns.sex.visible">
        <template slot-scope="scope">
          <span v-if="scope.row.sex === 0">未知</span>
          <span v-else-if="scope.row.sex === 1">男</span>
          <span v-else-if="scope.row.sex === 2">女</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="客户特征" align="center" prop="feature" v-if="columns.feature.visible">
        <template slot-scope="scope">
          <span v-if="scope.row.feature === 1">好沟通</span>
          <span v-else-if="scope.row.feature === 2">不好沟通</span>
          <span v-else-if="scope.row.feature === 3">爱投诉</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="是否可用" align="center" prop="publish" v-if="columns.publish.visible">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.publish"
            active-value="1"
            inactive-value="0"
            disabled
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" v-if="columns.createTime.visible">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="300">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
            v-hasPermi="['app:customer:query']"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['app:customer:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['app:customer:del']"
          >删除</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-order"
            @click="handleViewVisit(scope.row)"
            v-hasPermi="['app:visit:list']"
          >回访记录</el-button>
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

    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="基本信息" name="basic">
            <el-row>
              <el-col :span="12">
                <el-form-item label="客户姓名" prop="name">
                  <el-input v-model="form.name" placeholder="请输入客户姓名" maxlength="100" :disabled="isView" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="手机号" prop="mobile">
                  <el-input v-model="form.mobile" placeholder="请输入手机号" maxlength="100" :disabled="isView" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="性别" prop="sex">
                  <el-select v-model="form.sex" placeholder="请选择性别" style="width: 100%" :disabled="isView">
                    <el-option label="未知" :value="0" />
                    <el-option label="男" :value="1" />
                    <el-option label="女" :value="2" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="出生日期" prop="bornDate">
                  <el-date-picker
                    v-model="form.bornDate"
                    type="date"
                    placeholder="选择日期"
                    value-format="yyyy-MM-dd"
                    style="width: 100%"
                    :disabled="isView"
                  ></el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="客户特征" prop="feature">
                  <el-select v-model="form.feature" placeholder="请选择客户特征" style="width: 100%" :disabled="isView">
                    <el-option label="好沟通" :value="1" />
                    <el-option label="不好沟通" :value="2" />
                    <el-option label="爱投诉" :value="3" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="是否可用" prop="publish">
                  <el-radio-group v-model="form.publish" :disabled="isView">
                    <el-radio :value="1">可用</el-radio>
                    <el-radio :value="0">禁用</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="驾驶证号" prop="driverNum">
                  <el-input v-model="form.driverNum" placeholder="请输入驾驶证号" maxlength="100" :disabled="isView" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="驾驶证到期日" prop="driverVdate">
                  <el-date-picker
                    v-model="form.driverVdate"
                    type="date"
                    placeholder="选择日期"
                    value-format="yyyy-MM-dd"
                    style="width: 100%"
                    :disabled="isView"
                  ></el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="身份证号" prop="idcardNum">
                  <el-input v-model="form.idcardNum" placeholder="请输入身份证号" maxlength="100" :disabled="isView" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="客户地址" prop="address">
                  <el-input v-model="form.address" placeholder="请输入客户地址" maxlength="100" :disabled="isView" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="微信昵称" prop="wechatName">
                  <el-input v-model="form.wechatName" placeholder="请输入微信昵称" maxlength="100" :disabled="isView" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="微信appid" prop="wechatAppid">
                  <el-input v-model="form.wechatAppid" placeholder="请输入微信appid" maxlength="50" :disabled="isView" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="微信openid" prop="wechatOpenid">
                  <el-input v-model="form.wechatOpenid" placeholder="请输入微信openid" maxlength="50" :disabled="isView" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="备注" prop="remark">
                  <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" :disabled="isView"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
          <el-tab-pane label="车辆信息" name="car">
            <el-row :gutter="10" class="mb8" v-if="!isView">
              <el-col :span="1.5">
                <el-button
                  type="primary"
                  plain
                  icon="el-icon-plus"
                  size="mini"
                  @click="handleCarAdd"
                  v-hasPermi="['app:car:add']"
                >新增车辆</el-button>
              </el-col>
            </el-row>
            <el-table :data="form.cars" border size="small">
              <el-table-column label="车牌号" align="center" prop="carNum" />
              <el-table-column label="品牌名称" align="center" prop="brandName" />
              <el-table-column label="车系名称" align="center" prop="modelName" />
              <el-table-column label="年款" align="center" prop="yearName" />
              <el-table-column label="vin码" align="center" prop="vin" />
              <el-table-column label="发动机号" align="center" prop="engineNum" />
              <el-table-column label="操作" align="center" width="150" v-if="!isView">
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-edit"
                    @click="handleCarEdit(scope.$index, scope.row)"
                    v-hasPermi="['app:car:edit']"
                  >修改</el-button>
                  <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-delete"
                    @click="handleCarDelete(scope.$index)"
                    v-hasPermi="['app:car:del']"
                  >删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="回访记录" name="visit">
            <el-row :gutter="10" class="mb8" v-if="!isView">
              <el-col :span="1.5">
                <el-button
                  type="primary"
                  plain
                  icon="el-icon-plus"
                  size="mini"
                  @click="handleVisitAdd"
                  v-hasPermi="['app:visit:add']"
                >新增回访</el-button>
              </el-col>
            </el-row>
            <el-table :data="form.visits" border size="small">
              <el-table-column label="回访时间" align="center" prop="visitDate" width="120">
                <template slot-scope="scope">
                  <span>{{ parseTime(scope.row.visitDate, '{y}-{m}-{d}') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="回访内容" align="center" prop="visitContent" min-width="200" show-overflow-tooltip />
              <el-table-column label="客户反馈" align="center" prop="feedback" min-width="150" show-overflow-tooltip />
              <el-table-column label="回访人" align="center" prop="userName" width="100" />
              <el-table-column label="创建时间" align="center" prop="createTime" width="160">
                <template slot-scope="scope">
                  <span>{{ parseTime(scope.row.createTime) }}</span>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center" width="150" v-if="!isView">
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-edit"
                    @click="handleVisitEdit(scope.$index, scope.row)"
                    v-hasPermi="['app:visit:edit']"
                  >修改</el-button>
                  <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-delete"
                    @click="handleVisitDelete(scope.$index, scope.row)"
                    v-hasPermi="['app:visit:del']"
                  >删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" v-if="!isView">确 定</el-button>
        <el-button @click="cancel">关 闭</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="carTitle" :visible.sync="carOpen" width="700px" append-to-body>
      <el-form ref="carForm" :model="carForm" :rules="carRules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="车牌号" prop="carNum">
              <el-input v-model="carForm.carNum" placeholder="请输入车牌号" maxlength="100" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="品牌名称" prop="brandName">
              <el-input v-model="carForm.brandName" placeholder="请输入品牌名称" maxlength="50" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="车系名称" prop="modelName">
              <el-input v-model="carForm.modelName" placeholder="请输入车系名称" maxlength="100" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="年款名称" prop="yearName">
              <el-input v-model="carForm.yearName" placeholder="请输入年款名称" maxlength="50" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="配置名称" prop="styleName">
              <el-input v-model="carForm.styleName" placeholder="请输入配置名称" maxlength="255" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="颜色" prop="color">
              <el-input-number v-model="carForm.color" :min="0" :max="999" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="vin码" prop="vin">
              <el-input v-model="carForm.vin" placeholder="请输入vin码" maxlength="50" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发动机号" prop="engineNum">
              <el-input v-model="carForm.engineNum" placeholder="请输入发动机号" maxlength="50" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="行驶证注册日期" prop="driveRegDate">
              <el-date-picker
                v-model="carForm.driveRegDate"
                type="datetime"
                placeholder="选择日期时间"
                value-format="yyyy-MM-dd HH:mm:ss"
                style="width: 100%"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="行驶证发证日期" prop="driveIssueDate">
              <el-date-picker
                v-model="carForm.driveIssueDate"
                type="datetime"
                placeholder="选择日期时间"
                value-format="yyyy-MM-dd HH:mm:ss"
                style="width: 100%"
              ></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="行驶证照片" prop="drivingImg">
              <el-input v-model="carForm.drivingImg" placeholder="请输入行驶证照片路径" maxlength="100" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="carForm.remark" type="textarea" placeholder="请输入内容"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitCarForm">确 定</el-button>
        <el-button @click="cancelCar">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="visitTitle" :visible.sync="visitOpen" width="600px" append-to-body>
      <el-form ref="visitForm" :model="visitForm" :rules="visitRules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="回访时间" prop="visitDate">
              <el-date-picker
                v-model="visitForm.visitDate"
                type="date"
                placeholder="选择日期"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              ></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="回访内容" prop="visitContent">
              <el-input v-model="visitForm.visitContent" type="textarea" :rows="4" placeholder="请输入回访内容" maxlength="1000" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="客户反馈" prop="feedback">
              <el-input v-model="visitForm.feedback" type="textarea" :rows="3" placeholder="请输入客户反馈" maxlength="500" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="visitForm.remark" type="textarea" placeholder="请输入备注" maxlength="255" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitVisitForm">确 定</el-button>
        <el-button @click="cancelVisit">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="查看回访记录" :visible.sync="visitViewOpen" width="900px" append-to-body>
      <el-form :model="visitQueryParams" ref="visitQueryForm" size="small" :inline="true" label-width="80px">
        <el-form-item label="关键字" prop="keyword">
          <el-input
            v-model="visitQueryParams.keyword"
            placeholder="请输入关键字搜索"
            clearable
            style="width: 200px"
            @keyup.enter.native="handleVisitQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleVisitQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetVisitQuery">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="visitLoading" :data="visitList" border size="small">
        <el-table-column label="回访时间" align="center" prop="visitDate" width="120">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.visitDate, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="回访内容" align="center" prop="visitContent" min-width="200" show-overflow-tooltip />
        <el-table-column label="客户反馈" align="center" prop="feedback" min-width="150" show-overflow-tooltip />
        <el-table-column label="回访人" align="center" prop="userName" width="100" />
        <el-table-column label="创建时间" align="center" prop="createTime" width="160">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="visitTotal>0"
        :total="visitTotal"
        :page.sync="visitQueryParams.pageNum"
        :limit.sync="visitQueryParams.pageSize"
        @pagination="getVisitList"
      />
      <div slot="footer" class="dialog-footer">
        <el-button @click="visitViewOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCustomer, getCustomer, delCustomer, addCustomer, updateCustomer } from "@/api/application/customer"
import { listCustomerVisit, listCustomerVisitByCustomerId, addCustomerVisit, updateCustomerVisit, delCustomerVisit } from "@/api/application/customerVisit"

export default {
  name: "Customer",
  dicts: ['sys_normal_disable', 'sys_user_sex'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      customerList: [],
      title: "",
      open: false,
      activeTab: "basic",
      dateRange: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
        mobile: undefined,
        sex: undefined
      },
      columns: {
        id: { label: '客户编号', visible: true },
        name: { label: '客户姓名', visible: true },
        mobile: { label: '手机号', visible: true },
        sex: { label: '性别', visible: true },
        feature: { label: '客户特征', visible: true },
        publish: { label: '是否可用', visible: true },
        createTime: { label: '创建时间', visible: true }
      },
      form: {},
      rules: {
        name: [
          { required: true, message: "客户姓名不能为空", trigger: "blur" }
        ],
        mobile: [
          { required: true, message: "手机号不能为空", trigger: "blur" }
        ]
      },
      carTitle: "",
      carOpen: false,
      carIndex: -1,
      carForm: {},
      carRules: {
        carNum: [
          { required: true, message: "车牌号不能为空", trigger: "blur" }
        ],
        brandName: [
          { required: true, message: "品牌名称不能为空", trigger: "blur" }
        ],
        modelName: [
          { required: true, message: "车系名称不能为空", trigger: "blur" }
        ]
      },
      visitTitle: "",
      visitOpen: false,
      visitIndex: -1,
      visitForm: {},
      visitRules: {
        visitContent: [
          { required: true, message: "回访内容不能为空", trigger: "blur" }
        ]
      },
      isView: false,
      visitViewOpen: false,
      visitLoading: false,
      visitTotal: 0,
      visitList: [],
      currentCustomerId: undefined,
      visitQueryParams: {
        pageNum: 1,
        pageSize: 10,
        customerId: undefined,
        keyword: undefined
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listCustomer(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.customerList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        id: undefined,
        name: undefined,
        mobile: undefined,
        address: undefined,
        sex: 0,
        bornDate: undefined,
        feature: undefined,
        driverNum: undefined,
        driverVdate: undefined,
        idcardNum: undefined,
        wechatName: undefined,
        wechatAppid: undefined,
        wechatOpenid: undefined,
        publish: 1,
        remark: undefined,
        cars: [],
        visits: []
      }
      this.resetForm("form")
      this.activeTab = "basic"
      this.isView = false
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.dateRange = []
      this.resetForm("queryForm")
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加客户"
      this.isView = false
    },
    handleView(row) {
      this.reset()
      const id = row.id || this.ids
      getCustomer(id).then(response => {
        this.form = response.data
        if (!this.form.cars) {
          this.form.cars = []
        }
        if (!this.form.visits) {
          this.form.visits = []
        }
        this.loadCustomerVisits(id)
        this.open = true
        this.title = "查看客户"
        this.isView = true
      })
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getCustomer(id).then(response => {
        this.form = response.data
        if (!this.form.cars) {
          this.form.cars = []
        }
        if (!this.form.visits) {
          this.form.visits = []
        }
        this.loadCustomerVisits(id)
        this.open = true
        this.title = "修改客户"
        this.isView = false
      })
    },
    loadCustomerVisits(customerId) {
      if (customerId) {
        listCustomerVisitByCustomerId(customerId).then(response => {
          this.form.visits = response.data || []
        })
      }
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateCustomer(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addCustomer(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除客户编号为"' + ids + '"的数据项？').then(function() {
        return delCustomer(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleCarAdd() {
      this.carForm = {
        id: undefined,
        customerId: this.form.id,
        carNum: undefined,
        brandName: undefined,
        modelName: undefined,
        yearName: undefined,
        styleName: undefined,
        color: 0,
        vin: undefined,
        engineNum: undefined,
        drivingImg: undefined,
        driveRegDate: undefined,
        driveIssueDate: undefined,
        remark: undefined
      }
      this.carIndex = -1
      this.carOpen = true
      this.carTitle = "添加车辆"
    },
    handleCarEdit(index, row) {
      this.carForm = Object.assign({}, row)
      this.carIndex = index
      this.carOpen = true
      this.carTitle = "修改车辆"
    },
    handleCarDelete(index) {
      this.$modal.confirm('是否确认删除该车辆信息？').then(() => {
        this.form.cars.splice(index, 1)
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    submitCarForm() {
      this.$refs["carForm"].validate(valid => {
        if (valid) {
          if (this.carIndex >= 0) {
            this.form.cars.splice(this.carIndex, 1, this.carForm)
          } else {
            if (!this.form.cars) {
              this.form.cars = []
            }
            this.form.cars.push(this.carForm)
          }
          this.carOpen = false
        }
      })
    },
    cancelCar() {
      this.carOpen = false
      this.resetForm("carForm")
    },
    handleVisitAdd() {
      this.visitForm = {
        id: undefined,
        customerId: this.form.id,
        visitDate: this.parseTime(new Date(), '{y}-{m}-{d}'),
        visitContent: undefined,
        feedback: undefined,
        remark: undefined
      }
      this.visitIndex = -1
      this.visitOpen = true
      this.visitTitle = "添加回访记录"
    },
    handleVisitEdit(index, row) {
      this.visitForm = Object.assign({}, row)
      this.visitIndex = index
      this.visitOpen = true
      this.visitTitle = "修改回访记录"
    },
    handleVisitDelete(index, row) {
      if (row.id) {
        this.$modal.confirm('是否确认删除该回访记录？').then(() => {
          delCustomerVisit(row.id).then(() => {
            this.form.visits.splice(index, 1)
            this.$modal.msgSuccess("删除成功")
          })
        }).catch(() => {})
      } else {
        this.$modal.confirm('是否确认删除该回访记录？').then(() => {
          this.form.visits.splice(index, 1)
          this.$modal.msgSuccess("删除成功")
        }).catch(() => {})
      }
    },
    submitVisitForm() {
      this.$refs["visitForm"].validate(valid => {
        if (valid) {
          if (this.visitForm.id) {
            updateCustomerVisit(this.visitForm).then(response => {
              if (this.visitIndex >= 0) {
                this.form.visits.splice(this.visitIndex, 1, this.visitForm)
              }
              this.$modal.msgSuccess("修改成功")
              this.visitOpen = false
              this.loadCustomerVisits(this.form.id)
            })
          } else {
            if (this.form.id) {
              this.visitForm.customerId = this.form.id
              addCustomerVisit(this.visitForm).then(response => {
                this.$modal.msgSuccess("新增成功")
                this.visitOpen = false
                this.loadCustomerVisits(this.form.id)
              })
            } else {
              if (this.visitIndex >= 0) {
                this.form.visits.splice(this.visitIndex, 1, this.visitForm)
              } else {
                if (!this.form.visits) {
                  this.form.visits = []
                }
                this.form.visits.push(this.visitForm)
              }
              this.visitOpen = false
            }
          }
        }
      })
    },
    cancelVisit() {
      this.visitOpen = false
      this.resetForm("visitForm")
    },
    handleViewVisit(row) {
      this.currentCustomerId = row.id
      this.visitQueryParams.customerId = row.id
      this.visitQueryParams.keyword = undefined
      this.visitQueryParams.pageNum = 1
      this.visitViewOpen = true
      this.getVisitList()
    },
    getVisitList() {
      this.visitLoading = true
      listCustomerVisit(this.visitQueryParams).then(response => {
        this.visitList = response.rows
        this.visitTotal = response.total
        this.visitLoading = false
      })
    },
    handleVisitQuery() {
      this.visitQueryParams.pageNum = 1
      this.getVisitList()
    },
    resetVisitQuery() {
      this.visitQueryParams.keyword = undefined
      this.resetForm("visitQueryForm")
      this.handleVisitQuery()
    }
  }
}
</script>
