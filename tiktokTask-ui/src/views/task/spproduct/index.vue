<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <!-- <el-form-item label="" prop="id">
        <el-input
          v-model="queryParams.id"
          placeholder="请输入"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="名称" prop="productName">
        <el-input
          v-model="queryParams.productName"
          placeholder="请输入名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="价格" prop="productPrice">
        <el-input
          v-model="queryParams.productPrice"
          placeholder="请输入价格"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="库存" prop="productInventory">
        <el-input
          v-model="queryParams.productInventory"
          placeholder="请输入库存"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="daterangeCreateTime"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="是否上架" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择是否上架" clearable>
          <el-option
            v-for="dict in dict.type.on_off"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="商品分类" prop="groupId">
        <el-select v-model="queryParams.groupId" placeholder="请选择商品分类">
            <el-option
              v-for="dict in groupsList"
              :key="dict.id"
              :label="dict.groupName"
              :value="parseInt(dict.id)"
            ></el-option>
          </el-select>
      </el-form-item>
      <!-- <el-form-item label="语言" prop="language">
        <el-select
          v-model="queryParams.language"
          placeholder="请选择语言"
          clearable
        >
          <el-option
            v-for="dict in languageList"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item> -->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        <el-button type="primary" size="mini" @click="changeLange">切换语言</el-button>
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
          v-hasPermi="['task:spproduct:add']"
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
          v-hasPermi="['task:spproduct:edit']"
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
          v-hasPermi="['task:spproduct:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['task:spproduct:export']"
        >导出</el-button>
      </el-col>
      
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="spproductList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="" align="center" prop="id" /> -->
      <el-table-column label="商品分类" align="center" prop="groupName" />
      <el-table-column label="名称" align="center" prop="productName" />
      <el-table-column label="价格" align="center" prop="productPrice" />
      <el-table-column label="库存" align="center" prop="productInventory" />
      <el-table-column label="大图" align="center" prop="productImg" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.productImg" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="详细描述" align="center" prop="detailedDescription" />
      <el-table-column label="详情多图" align="center" prop="productDetails" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.productDetails" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="是否上架" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.on_off" :value="scope.row.status"/>
        </template>
      </el-table-column>
      
      <!-- <el-table-column label="" align="center" prop="deleted" /> -->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['task:spproduct:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['task:spproduct:remove']"
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

    <!-- 添加或修改产品对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="语言" prop="language">
          <el-radio-group v-model="language">
            <el-radio
              v-for="dict in languageList"
              :key="dict.value"
              :label="dict.value"
              >{{ dict.label }}</el-radio
            >
          </el-radio-group>
        </el-form-item>
        <el-form-item label="商品分类" prop="groupId">
          <el-select v-model="form.groupId" placeholder="请选择商品分类">
            <el-option
              v-for="dict in groupsList"
              :key="dict.id"
              :label="dict.groupName"
              :value="parseInt(dict.id)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="名称" prop="productName">
          <el-input v-model="productNameObj[language]" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="价格" prop="productPrice">
          <el-input v-model="form.productPrice" placeholder="请输入价格" />
        </el-form-item>
        <el-form-item label="库存" prop="productInventory">
          <el-input v-model="form.productInventory" placeholder="请输入库存" />
        </el-form-item>
        <el-form-item label="大图" prop="productImg">
          <image-upload v-model="form.productImg"/>
        </el-form-item>
        <el-form-item label="详细描述" prop="detailedDescription">
          <el-input v-model="detailedDescriptionObj[language]" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="详情多图" prop="productDetails">
          <image-upload v-model="form.productDetails"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="是否上架" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.on_off"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
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
  </div>
</template>

<script>
import { listSpproduct, getSpproduct, delSpproduct, addSpproduct, updateSpproduct } from "@/api/task/spproduct";
import { listGroups } from "@/api/task/groups";
import { LanguageSetting } from "@/api/index";

export default {
  name: "Spproduct",
  dicts: ['on_off'],
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
      // 产品表格数据
      spproductList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 时间范围
      daterangeCreateTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: null,
        productName: null,
        productPrice: null,
        productInventory: null,
        productImg: null,
        detailedDescription: null,
        productDetails: null,
        createTime: null,
        status: null,
        groupId: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      language: "Chinese",
      productNameObj: {
        Chinese: "",
        English: "",
      },
      detailedDescriptionObj: {
        Chinese: "",
        English: "",
      },
      languageList: [{
        label: '繁体',
        value: 'Chinese'
      },{
        label: '英文',
        value: 'English'
      }],
      groupsList: []
    };
  },
  created() {
    this.getListGroups();
    this.getList();
  },
  activated() {
    this.getList();
  },
  methods: {
    changeLange() {
      let language = localStorage.getItem('Language') == 'English' ? 'Chinese' : 'English';
      LanguageSetting({
        Language: language
      }).then(response => {
        localStorage.setItem('Language', language)
        this.getList();
      });
    },
    getListGroups() {
      listGroups({
        pageNum: 1,
        pageSize: 20,
      }).then(response => {
        this.groupsList = response.rows;
      });
    },
    /** 查询产品列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeCreateTime && '' != this.daterangeCreateTime) {
        this.queryParams.params["beginCreateTime"] = this.daterangeCreateTime[0];
        this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
      }
      listSpproduct(this.queryParams).then(response => {
        this.spproductList = response.rows;
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
        id: null,
        productName: null,
        productPrice: null,
        productInventory: null,
        productImg: null,
        detailedDescription: null,
        productDetails: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        status: null,
        groupId: null,
        deleted: null
      };
      this.productNameObj = {
        Chinese: "",
        English: "",
      };
      this.detailedDescriptionObj = {
        Chinese: "",
        English: "",
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
      this.daterangeCreateTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加产品";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getSpproduct(id).then(response => {
        this.form = response.data;
        this.productNameObj = JSON.parse(this.form.productName);
        this.detailedDescriptionObj = JSON.parse(this.form.detailedDescription);
        this.open = true;
        this.title = "修改产品";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (!this.productNameObj.Chinese) {
            return this.$modal.msgError("请输入商品描述");
          }
          this.form.productName = JSON.stringify(this.productNameObj);

          if (!this.detailedDescriptionObj.Chinese) {
            return this.$modal.msgError("请输入商品描述");
          }
          this.form.detailedDescription = JSON.stringify(this.detailedDescriptionObj);
          if (this.form.id != null) {
            updateSpproduct(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addSpproduct(this.form).then(response => {
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
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除产品编号为"' + ids + '"的数据项？').then(function() {
        return delSpproduct(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('task/spproduct/export', {
        ...this.queryParams
      }, `spproduct_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
