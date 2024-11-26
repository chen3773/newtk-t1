<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="98px">
      <el-form-item label="产品标题" prop="productTitle">
        <el-input
          v-model="queryParams.productTitle"
          placeholder="请输入产品标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="单价" prop="totalAmount">
        <el-input
          v-model="queryParams.totalAmount"
          placeholder="请输入单价"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="最低等级限制" prop="level">
        <!-- <el-input
          v-model="queryParams.level"
          placeholder="请输入最低等级限制"
          clearable
          @keyup.enter.native="handleQuery"
        /> -->
        <el-select
          v-model="queryParams.level"
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
      </el-form-item>
      <!-- <el-form-item label="份额" prop="totalShares">
        <el-input
          v-model="queryParams.totalShares"
          placeholder="请输入份额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="已售出份额" prop="soldShares">
        <el-input
          v-model="queryParams.soldShares"
          placeholder="请输入已售出份额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="开始时间">
        <el-date-picker
          v-model="daterangeStartTime"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="分红开始时间" prop="dividendStartTime">
        <el-date-picker clearable
                        v-model="queryParams.dividendStartTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择分红开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="结束时间">
        <el-date-picker
          v-model="daterangeEndTime"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <!-- <el-form-item label="创建时间" prop="createTime">
        <el-date-picker clearable
                        v-model="queryParams.createTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择创建时间">
        </el-date-picker>
      </el-form-item> -->
      <!-- <el-form-item label="状态" prop="state">
        <el-select v-model="queryParams.state" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in dict.type.on_off"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item> -->
      <!-- <el-form-item label="" prop="deleted">
        <el-input
          v-model="queryParams.deleted"
          placeholder="请输入"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <!-- <el-form-item label="周期" prop="cycle">
        <el-input
          v-model="queryParams.cycle"
          placeholder="请输入周期"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="收益率" prop="returnRate">
        <el-input
          v-model="queryParams.returnRate"
          placeholder="请输入收益率"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <!-- <el-form-item label="分红周期" prop="dividendDays">
        <el-input
          v-model="queryParams.dividendDays"
          placeholder="请输入分红周期"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="类型" prop="type">
        <!-- <el-input
          v-model="queryParams.type"
          placeholder="请输入类型"
          clearable
          @keyup.enter.native="handleQuery"
        /> -->
        <el-select v-model="queryParams.type" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in dict.type.product_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
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
          v-hasPermi="['task:product:add']"
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
          v-hasPermi="['task:product:edit']"
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
          v-hasPermi="['task:product:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['task:product:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="productList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="45" align="center" />
      <el-table-column label="产品ID" align="center" prop="productId" />
      <el-table-column label="产品标题" align="center" prop="productTitle" />
      <el-table-column label="产品主图" align="center" prop="mainImageUrl" width="90">
        <template slot-scope="scope">
          <image-preview :src="scope.row.mainImageUrl" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="详情图" align="center" prop="detailImageUrl" width="90">
        <template slot-scope="scope">
          <image-preview :src="scope.row.detailImageUrl" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="产品描述" align="center" prop="productDescription">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            @click="
              smContent = scope.row.productDescription;
              open1 = true;
            "
            >查看详情</el-button
          >
        </template>
      </el-table-column>
      <el-table-column label="单价" align="center" prop="totalAmount" width="120">
        <template slot-scope="scope">
          <div style="text-align: left;">单价：{{ scope.row.totalAmount }}</div>
          <div style="text-align: left;">份额：{{ scope.row.totalShares }}</div>
          <div style="text-align: left;">已售：{{ scope.row.soldShares }}</div>
          <div style="text-align: left;">等级：SVIP{{ scope.row.level }}</div>
        </template>
      </el-table-column>
      <!-- <el-table-column label="最低等级限制" align="center" prop="level" >
        <template slot-scope="scope">
          <span>SVIP{{ scope.row.level }}</span>
        </template>
      </el-table-column> -->
      <!-- <el-table-column label="份额" align="center" prop="totalShares" />
      <el-table-column label="已售出份额" align="center" prop="soldShares" /> -->
      <el-table-column label="时间" align="center" prop="startTime" width="180">
        <template slot-scope="scope">
          <div style="text-align: left;">开始时间：{{ scope.row.startTime }}</div>
          <div style="text-align: left;">结束时间：{{ scope.row.endTime }}</div>
          <div style="text-align: left;">分红时间：{{ scope.row.dividendStartTime }}</div>
        </template>
      </el-table-column>
      <!-- <el-table-column label="分红开始时间" align="center" prop="dividendStartTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.dividendStartTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column> -->

      <!-- <el-table-column label="" align="center" prop="deleted" /> -->
      <el-table-column label="周期(天)" align="center" prop="cycle" width="130">
        <template slot-scope="scope">
          <div style="text-align: left;">周期(天)：{{ scope.row.cycle }}</div>
          <div style="text-align: left;">收益率(%)：{{ scope.row.returnRate }}</div>
          <div style="text-align: left;">分红周期(天)：{{ scope.row.dividendDays }}</div>
          <div style="text-align: left;">参与人数(人)：{{ scope.row.participants }}</div>
        </template>
      </el-table-column>
      <!-- <el-table-column label="收益率(%)" align="center" prop="returnRate" /> -->
      <el-table-column label="产品类型" align="center" prop="type" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.product_type" :value="scope.row.type"/>
        </template>
      </el-table-column>
      <!-- <el-table-column label="分红周期(天)" align="center" prop="dividendDays" /> -->
      <el-table-column label="状态" align="center" prop="state">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.on_off" :value="scope.row.state"/>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['task:product:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['task:product:remove']"
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

    <!-- 添加或修改众筹产品对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="产品标题" prop="productTitle">
          <el-input v-model="form.productTitle" placeholder="请输入产品标题" />
        </el-form-item>
        <el-form-item label="产品主图" prop="mainImageUrl">
          <image-upload v-model="form.mainImageUrl" :limit="1"/>
        </el-form-item>
        <el-form-item label="详情图" prop="detailImageUrl">
          <image-upload v-model="form.detailImageUrl" :limit="1"/>
        </el-form-item>
        <el-form-item label="产品描述" prop="productDescription">
          <el-input v-model="form.productDescription" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="单价" prop="totalAmount">
          <el-input v-model="form.totalAmount" placeholder="请输入单价" />
        </el-form-item>
        <el-form-item label="最低等级限制" prop="level">
          <el-select
            v-model="form.level"
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
          <!-- <el-input v-model="form.level" placeholder="请输入最低等级限制" /> -->
        </el-form-item>
        <el-form-item label="份额" prop="totalShares">
          <el-input v-model="form.totalShares" placeholder="请输入份额" />
        </el-form-item>
        <el-form-item label="已售出份额" prop="soldShares">
          <el-input v-model="form.soldShares" placeholder="请输入已售出份额" />
        </el-form-item>
        <el-form-item label="周期(天)" prop="cycle">
          <el-input v-model="form.cycle" placeholder="请输入周期天数" />
        </el-form-item>
        <el-form-item label="收益率(%)" prop="returnRate">
          <el-input v-model="form.returnRate" placeholder="请输入收益率" />
        </el-form-item>
        <el-form-item label="参与人数(人)" prop="participants">
          <el-input v-model="form.participants" placeholder="请输入参与人数" />
        </el-form-item>
        <el-form-item label="产品类型" prop="type">
          <el-radio-group v-model="form.type">
            <el-radio
              v-for="dict in dict.type.product_type"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="分红周期(天)" prop="dividendDays">
          <el-input v-model="form.dividendDays" placeholder="请输入分红周期" />
        </el-form-item>

        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker clearable
                          v-model="form.startTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="分红开始时间" prop="dividendStartTime">
          <el-date-picker clearable
                          v-model="form.dividendStartTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择分红开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker clearable
                          v-model="form.endTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="状态" prop="state">
          <el-radio-group v-model="form.state">
            <el-radio
              v-for="dict in dict.type.on_off"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <!-- <el-form-item label="" prop="deleted">
          <el-input v-model="form.deleted" placeholder="请输入" />
        </el-form-item> -->

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog
      :close-on-click-modal="false"
      title="产品描述"
      :visible.sync="open1"
      width="700px"
      append-to-body
    >
      <div class="smContent" v-html="smContent"></div>
    </el-dialog>
  </div>
</template>

<script>
import { listProduct, getProduct, delProduct, addProduct, updateProduct } from "@/api/task/product";
import { listSvipSetting } from "@/api/task/svipSetting";

export default {
  name: "Product",
  dicts: ['on_off', 'product_type'],
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
      // 众筹产品表格数据
      productList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 类型时间范围
      daterangeStartTime: [],
      // 类型时间范围
      daterangeEndTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        productTitle: null,
        mainImageUrl: null,
        detailImageUrl: null,
        productDescription: null,
        totalAmount: null,
        level: null,
        totalShares: null,
        soldShares: null,
        startTime: null,
        dividendStartTime: null,
        endTime: null,
        createTime: null,
        state: null,
        deleted: null,
        cycle: null,
        returnRate: null,
        dividendDays: null,
        type: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        productTitle: [
          { required: true, message: "产品标题不能为空", trigger: "blur" }
        ],
        mainImageUrl: [
          { required: true, message: "产品主图不能为空", trigger: "blur" }
        ],
        // detailImageUrl: [
        //   { required: true, message: "详情图不能为空", trigger: "blur" }
        // ],
        productDescription: [
          { required: true, message: "产品详情描述不能为空", trigger: "blur" }
        ],
        totalAmount: [
          { required: true, message: "单价不能为空", trigger: "blur" }
        ],
        totalShares: [
          { required: true, message: "份额不能为空", trigger: "blur" }
        ],
        soldShares: [
          { required: true, message: "已售出份额不能为空", trigger: "blur" }
        ],
        startTime: [
          { required: true, message: "开始时间不能为空", trigger: "blur" }
        ],
        dividendStartTime: [
          { required: true, message: "分红开始时间不能为空", trigger: "blur" }
        ],
        endTime: [
          { required: true, message: "结束时间不能为空", trigger: "blur" }
        ],
      },
      vipList: [],
      smContent: '',
      open1: false
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
    /** 查询众筹产品列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeStartTime && '' != this.daterangeStartTime) {
        this.queryParams.params["beginStartTime"] = this.daterangeStartTime[0];
        this.queryParams.params["endStartTime"] = this.daterangeStartTime[1];
      }
      if (null != this.daterangeEndTime && '' != this.daterangeEndTime) {
        this.queryParams.params["beginEndTime"] = this.daterangeEndTime[0];
        this.queryParams.params["endEndTime"] = this.daterangeEndTime[1];
      }
      listProduct(this.queryParams).then(response => {
        this.productList = response.rows;
        this.total = response.total;
        this.loading = false;
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
        productId: null,
        productTitle: null,
        mainImageUrl: null,
        detailImageUrl: null,
        productDescription: null,
        totalAmount: null,
        level: null,
        totalShares: null,
        soldShares: null,
        startTime: null,
        dividendStartTime: null,
        endTime: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        state: null,
        remark: null,
        deleted: null,
        cycle: null,
        returnRate: null,
        dividendDays: null,
        type: null,
        participants: null
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
      this.daterangeStartTime = [];
      this.daterangeEndTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.productId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加众筹产品";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const productId = row.productId || this.ids
      getProduct(productId).then(response => {
        this.form = response.data;
        this.form.level = Number(this.form.level)
        this.open = true;
        this.title = "修改众筹产品";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.productId != null) {
            updateProduct(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addProduct(this.form).then(response => {
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
      const productIds = row.productId || this.ids;
      this.$modal.confirm('是否确认删除众筹产品编号为"' + productIds + '"的数据项？').then(function() {
        return delProduct(productIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('task/product/export', {
        ...this.queryParams
      }, `product_${new Date().getTime()}.xlsx`)
    },
    getVipList() {
      listSvipSetting({
        pageNum: 1,
        pageSize: 20
      }).then(response => {
        this.vipList = response.rows;
      });
    },
  }
};
</script>
