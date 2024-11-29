<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="uid" prop="uid">
        <el-input
          type="number"
          v-model="queryParams.uid"
          placeholder="请输入uid"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="登录名" prop="username">
        <el-input
          v-model="queryParams.username"
          placeholder="请输入登录名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="svip等级" prop="svipLevel">
        <el-select
          v-model="queryParams.svipLevel"
          placeholder="请选择svip等级"
          clearable
        >
          <el-option
            v-for="dict in vipList"
            :key="dict.vipLevel"
            :label="'SVIP'+dict.vipLevel"
            :value="dict.vipLevel"
          />
        </el-select>
        <!-- <el-input
          v-model="queryParams.svipLevel"
          placeholder="请输入svip等级"
          clearable
          @keyup.enter.native="handleQuery"
        /> -->
      </el-form-item>
      <el-form-item label="注册时间">
        <el-date-picker
          v-model="daterangeRegistrationTime"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="邀请码" prop="referrerId">
        <el-input
          v-model="queryParams.invitationCode"
          placeholder="请输入邀请码"
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
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['task:users:add']"
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
          v-hasPermi="['task:users:edit']"
        >修改</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['task:users:remove']"
        >删除</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['task:users:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="multiple"
          @click="handleUpdate2"
          v-hasPermi="['task:users:edit']"
        >配置特殊任务</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="usersList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="45" align="center" />
      <el-table-column label="UID" align="center" prop="uid" width="140" >
        <template slot-scope="scope">
          <div><el-button type="text" style="padding: 0;" @click="toPage(scope)">UID：{{ scope.row.uid }}</el-button></div>
          <div :class="`tag-color${scope.row.svipLevel}`">等级：VIP{{ scope.row.svipLevel }}</div>
          <div>团队人数：{{ scope.row.teamsize }}</div>
          <div>邀请码：{{ scope.row.invitationCode }}</div>
          <div>上级id：{{ scope.row.referrerId }}</div>
        </template>
      </el-table-column>
      <el-table-column label="账户" align="center" prop="username" width="170" >
        <template slot-scope="scope">
          <div style="text-align: left;">登录名：{{ scope.row.username }}</div>
          <div style="text-align: left;">密码：{{ scope.row.password }}</div>
          <div style="text-align: left;">昵称：{{ scope.row.nickname }}</div>
          <div style="text-align: left;">交易密码：{{ scope.row.paymentPassword }}</div>
        </template>
      </el-table-column>
      <!-- <el-table-column label="密码" align="center" prop="password" /> -->
      <!-- <el-table-column label="昵称" align="center" prop="nickname" /> -->
      <!-- <el-table-column label="svip等级" align="center" prop="svipLevel" /> -->
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="余额" align="center" prop="balance" width="190" >
        <template slot-scope="scope">
          <div style="color: #1890ff;text-align: left;">余额：{{ scope.row.balance }}</div>
          <div style="color: #ff9292;text-align: left;">不可提现金额：{{ scope.row.nonWithdrawableBalance }}</div>
          <!-- <div style="color: #1890ff;text-align: left;">投资账户余额：{{ scope.row.investmentAmount }}</div>
          <div style="color: #ff9292;text-align: left;">投资账户冻结：{{ scope.row.frozenIvestmentAmount }}</div>
          <div style="color: #1890ff;text-align: left;">投资获利：{{ scope.row.invest.projectIncome }}</div> -->
          <div style="color: #1890ff;text-align: left;">参与项目数：{{ scope.row.invest.myProject }}</div>
          <div style="color: #1890ff;text-align: left;">质押冻结金额：{{ scope.row.invest.frozenAmount }}</div>
        </template>
      </el-table-column>
      <!-- <el-table-column label="不可提现余额" align="center" prop="nonWithdrawableBalance" /> -->
      <el-table-column label="钱包地址" align="center" prop="usdtAddress" />
      <el-table-column label="登录时间" align="center" prop="logindate" width="180">
        <template slot-scope="scope">
          <div style="text-align: left;">注册时间：{{ scope.row.registrationTime }}</div>
          <div style="text-align: left;">登录时间：{{ scope.row.logindate }}</div>
        </template>
      </el-table-column>
      <!-- <el-table-column label="邀请码" align="center" prop="invitationCode" /> -->
      <el-table-column label="状态" align="center" prop="userStatus" width="70">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.user_state" :value="scope.row.userStatus"/>
        </template>
      </el-table-column>

      <el-table-column label="提现" align="center" prop="userStatus" width="70">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.user_state" :value="scope.row.withdraw"/>
        </template>
      </el-table-column>
      <!-- <el-table-column label="最后登录时间" align="center" prop="logindate" width="160">
      </el-table-column> -->
      
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="120">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate2(scope.row)"
            v-hasPermi="['task:users:edit']"
          >配置特殊任务</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate1(scope.row)"
            v-hasPermi="['task:users:edit']"
          >提升等级</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate3(scope.row)"
            v-hasPermi="['task:users:edit']"
          >资金变动</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['task:users:edit']"
          >修改</el-button>
          <!-- <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['task:users:remove']"
          >删除</el-button> -->
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

    <!-- 添加或修改用户信息对话框 -->
    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="登录名" prop="username">
          <el-input v-model="form.username" placeholder="请输入登录名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="交易密码" prop="paymentPassword" v-if="title != '添加用户信息'">
          <el-input v-model="form.paymentPassword" placeholder="请输入交易密码" />
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="form.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <!-- <el-form-item label="svip等级" prop="svipLevel">
          <el-select
                v-model="form.svipLevel"
                placeholder="请选择svip等级"
                clearable
              >
                <el-option
                  v-for="dict in vipList"
                  :key="dict.vipLevel"
                  :label="'SVIP'+dict.vipLevel"
                  :value="dict.vipLevel"
                />
              </el-select>
        </el-form-item> -->
        <el-form-item label="余额" prop="balance" v-if="title != '添加用户信息'">
          <el-input v-model="form.balance" placeholder="请输入余额" />
        </el-form-item>
        <el-form-item label="不可提现余额" prop="nonWithdrawableBalance" v-if="title != '添加用户信息'">
          <el-input v-model="form.nonWithdrawableBalance" placeholder="请输入不可提现余额" />
        </el-form-item>
        <!-- <el-form-item label="投资账户余额" prop="investmentAmount" v-if="title != '添加用户信息'">
          <el-input v-model="form.investmentAmount" placeholder="请输入投资账户余额" />
        </el-form-item>
        <el-form-item label="投资账户冻结" prop="frozenIvestmentAmount" v-if="title != '添加用户信息'">
          <el-input v-model="form.frozenIvestmentAmount" placeholder="请输入投资账户冻结" />
        </el-form-item> -->
        <el-form-item label="钱包地址" prop="usdtAddress" v-if="title != '添加用户信息'">
          <el-input v-model="form.usdtAddress" placeholder="请输入钱包地址" />
        </el-form-item>
        <!-- <el-form-item label="注册时间" prop="registrationTime">
          <el-date-picker clearable
                          v-model="form.registrationTime"
                          type="datetime"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          placeholder="请选择注册时间">
          </el-date-picker>
        </el-form-item> -->
        <el-form-item label="邀请码" prop="invitationCode" v-if="title == '添加用户信息'">
          <el-input v-model="form.invitationCode" placeholder="请输入邀请码" />
        </el-form-item>
        <el-form-item label="用户状态" prop="userStatus" v-if="title != '添加用户信息'">
          <el-radio-group v-model="form.userStatus">
            <el-radio
              v-for="dict in dict.type.user_state"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="提现状态" prop="userStatus" v-if="title != '添加用户信息'">
          <el-radio-group v-model="form.withdraw">
            <el-radio
              v-for="dict in dict.type.user_state"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark" v-if="title != '添加用户信息'">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog :close-on-click-modal="false" title="修改vip等级" :visible.sync="open1" width="500px" append-to-body>
      <el-form ref="form1" :model="form1" label-width="80px">
        <el-form-item label="svip等级" prop="lv">
          <el-select
                v-model="form1.lv"
                placeholder="请选择svip等级"
                clearable
              >
                <el-option
                  v-for="dict in vipList"
                  :key="dict.vipLevel"
                  :label="'SVIP'+dict.vipLevel"
                  :value="dict.vipLevel"
                />
              </el-select>
          <!-- <el-input v-model="form1.lv" placeholder="请输入vip等级" /> -->
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-popconfirm
          title="升级vip1-5不可逆，请确认"
          @confirm="submitForm1"
        >
          <el-button type="primary" slot="reference">确 定</el-button>
        </el-popconfirm>
        <el-button @click="cancel1" style="margin-left: 10px;">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog :close-on-click-modal="false" title="配置特殊任务" :visible.sync="open2" width="600px" append-to-body>
      <div v-for="(data, index) in form2.taskList" :key="index">
        <div style="display: flex;align-items: center;justify-content: space-between; margin-bottom: 15px;">
          <p style="font-size: 16px; font-weight: bold; margin: 0;">特殊任务{{ index+1 }}</p>
          <i style="font-size: 16px; color: red; cursor: pointer;" class="el-icon-delete" @click="delTask(index)"></i>
        </div>
        <el-form ref="form2" label-width="130px">
          <el-form-item label="任务id" prop="taskId">
            <el-select v-model="data.taskId" placeholder="请选择任务" clearable style="width: 100%;">
              <el-option
                v-for="dict in tasksList"
                :key="dict.id"
                :label="dict.label"
                :value="dict.id"
              />
            </el-select>
            <!-- <el-input v-model="data.taskId" placeholder="请输入任务id" /> -->
          </el-form-item>
          <el-form-item label="在第几个任务触发" prop="count">
            <el-input v-model="data.count" placeholder="请输入在第几个任务触发" />
          </el-form-item>
        </el-form>
      </div>
      <el-button type="primary" @click="addTask">增加任务</el-button>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm2">确 定</el-button>
        <el-button @click="cancel2">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog :close-on-click-modal="false" title="资金变动" :visible.sync="open3" width="500px" append-to-body>
      <el-form ref="form3" :model="form3" label-width="100px">
        <!-- <el-form-item label="资金类型" prop="type">
          <el-radio-group v-model="form3.type">
            <el-radio
              v-for="dict in yesno2"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item> -->
        <el-form-item label="金额类型" prop="add">
          <el-radio-group v-model="form3.add">
            <el-radio
              v-for="dict in yesno1"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="是否可提现" prop="withdraw">
          <el-radio-group v-model="form3.withdraw">
            <el-radio
              v-for="dict in yesno"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="是否有提成" prop="rebate">
          <el-radio-group v-model="form3.rebate">
            <el-radio
              v-for="dict in yesno"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="金额" prop="amount">
          <el-input v-model="form3.amount" placeholder="请输入金额" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm3">确 定</el-button>
        <el-button @click="cancel3">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listUsers, getUsers, delUsers, addUsers, updateUsers, UpgradeSvip, addSpecialTask, getSpecialTask, addAndDeduct } from "@/api/task/users";
import { Sortinglist } from "@/api/task/tasks";
import { listSvipSetting } from "@/api/task/svipSetting";

export default {
  name: "Users",
  dicts: ['user_state'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 用户信息表格数据
      usersList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 备注时间范围
      daterangeRegistrationTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        username: null,
        svipLevel: null,
        registrationTime: null,
        referrerId: null,
        invitationCode:null,
        uid: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        username: [
          { required: true, message: "登录名不能为空", trigger: "blur" }
        ],
        password: [
          { required: true, message: "密码不能为空", trigger: "blur" }
        ],
        nickname: [
          { required: true, message: "昵称不能为空", trigger: "blur" }
        ],
        invitationCode: [
          { required: true, message: "邀请码不能为空", trigger: "blur" }
        ],
      },
      open1: false,
      form1: {},
      open2: false,
      form2: {
        uids: [],
        taskList: [{
          taskId: '',
          count: ''
        }]
      },
      open3: false,
      form3: {},
      yesno: [{
        value: 'yes',
        label: '是'
      },{
        value: 'no',
        label: '否'
      }],
      yesno1: [{
        value: 'yes',
        label: '增加'
      },{
        value: 'no',
        label: '扣除'
      }],
      yesno2: [{
        value: '0',
        label: '任务资金'
      },{
        value: '1',
        label: '投资资金'
      }],
      tasksList: [],
      vipList: []
    };
  },
  created() {
    this.getList();
    this.getVipList()
  },
  activated() {
    this.getList();
  },
  methods: {
    /** 查询用户信息列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeRegistrationTime && '' != this.daterangeRegistrationTime) {
        this.queryParams.params["beginRegistrationTime"] = this.daterangeRegistrationTime[0];
        this.queryParams.params["endRegistrationTime"] = this.daterangeRegistrationTime[1];
      }
      listUsers(this.queryParams).then(response => {
        this.usersList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
      Sortinglist({
        pageNum: 1,
        pageSize: 200,
        taskLevel: 6
      }).then(response => {
        this.tasksList = response.rows;
        this.tasksList.map(item => {
          item.id = item.id + '';
          item.label = `ID: ${item.id}; 名称: ${item.title}; 金额: ${item.rewardAmount}`;
        })
        this.tasksList = [...this.tasksList]
        console.log(this.tasksList, 'this.tasksList')
      });
    },
    getVipList() {
      listSvipSetting({
        pageNum: 1,
        pageSize: 20
      }).then(response => {
        this.vipList = response.rows;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        uid: null,
        username: null,
        password: null,
        nickname: null,
        svipLevel: null,
        balance: null,
        nonWithdrawableBalance: null,
        investmentAmount: null,
        frozenIvestmentAmount: null,
        usdtAddress: null,
        registrationTime: null,
        referrerId: null,
        invitationCode:null,
        withdraw:null,
        blockchainName:null,
        userStatus: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        paymentPassword: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.daterangeRegistrationTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.uid)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加用户信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const uid = row.uid || this.ids
      getUsers(uid).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改用户信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.uid != null) {
            updateUsers(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addUsers(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const uids = row.uid || this.ids;
      this.$modal.confirm('是否确认删除用户信息编号为"' + uids + '"的数据项？').then(function() {
        return delUsers(uids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('task/users/export', {
        ...this.queryParams
      }, `users_${new Date().getTime()}.xlsx`)
    },
    submitForm1() {
      this.$refs["form1"].validate(valid => {
        if (valid) {
          UpgradeSvip(this.form1).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open1 = false;
              this.getList();
            });
        }
      });
    },
    cancel1() {
      this.open1 = false;
      this.reset1();
    },
    // 表单重置
    reset1() {
      this.form1 = {
        uid: null,
        lv: null
      };
      this.resetForm("form1");
    },
     /** 修改按钮操作 */
    handleUpdate1(row) {
      this.reset1();
      const uid = row.uid || this.ids
      this.form1 = {
        uid: uid,
        lv: row.svipLevel
      };
      this.open1 = true;
    },
    submitForm2() {
      addSpecialTask(this.form2).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open2 = false;
              this.getList();
            });
    },
    cancel2() {
      this.open2 = false;
      this.reset2();
    },
    // 表单重置
    reset2() {
      this.form2 = {
        uids: [],
        taskList: [{
          taskId: '',
          count: ''
        }]
      };
    },
     /** 修改按钮操作 */
    handleUpdate2(row) {
      this.reset2();
      const uid = row.uid ? [row.uid] : this.ids;
      getSpecialTask({
        uid: uid[0]
      }).then(response => {
        console.log(response, 'response')
        this.form2 = {
          uids: uid,
          taskList: response.data.taskList
        };
        this.open2 = true;
      });

    },
    addTask() {
      this.form2.taskList.push({
        taskId: '',
        count: ''
      })
    },
    delTask(index) {
      this.form2.taskList.splice(index, 1)
      this.form2 = {...this.form2}
    },
    submitForm3() {
      this.$refs["form3"].validate(valid => {
        if (valid) {
          addAndDeduct(this.form3).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open3 = false;
              this.getList();
            });
        }
      });
    },
    cancel3() {
      this.open3 = false;
      this.reset3();
    },
    // 表单重置
    reset3() {
      this.form3 = {
        uid: '',
        amount: '',
        withdraw: 'yes',
        add: 'yes',
        rebate: 'yes',
        type: '0'
      };
      this.resetForm("form3");
    },
     /** 修改按钮操作 */
    handleUpdate3(row) {
      this.reset3();
      const uid = row.uid
      this.form3 = {
        uid: uid,
        amount: '',
        withdraw: 'yes',
        add: 'yes',
        rebate: 'yes',
        type: '0'
      };
      this.open3 = true;
    },
    toPage(scope) {
      if (scope.row.teamsize > 0) {
        this.$router.push({ path: '/tkuser/userstest', query: {
          uid: scope.row.uid
        }})
      }
    },
  }
};
</script>
