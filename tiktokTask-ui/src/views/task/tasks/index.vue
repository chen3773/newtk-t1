<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
<!--      <el-form-item label="任务标题" prop="title">-->
<!--        <el-input-->
<!--          v-model="queryParams.title"-->
<!--          placeholder="请输入任务标题"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="奖励金额" prop="rewardAmount">-->
<!--        <el-input-->
<!--          v-model="queryParams.rewardAmount"-->
<!--          placeholder="请输入奖励金额"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="任务链接" prop="link">-->
<!--        <el-input-->
<!--          v-model="queryParams.link"-->
<!--          placeholder="请输入任务链接"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="任务总数量" prop="totalQuantity">-->
<!--        <el-input-->
<!--          v-model="queryParams.totalQuantity"-->
<!--          placeholder="请输入任务总数量"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="剩余数量" prop="surplusquantity">-->
<!--        <el-input-->
<!--          v-model="queryParams.surplusquantity"-->
<!--          placeholder="请输入剩余数量"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="已完成数量" prop="completedQuantity">-->
<!--        <el-input-->
<!--          v-model="queryParams.completedQuantity"-->
<!--          placeholder="请输入已完成数量"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
      <el-form-item label="任务等级" prop="taskLevel">
        <el-select v-model="queryParams.taskLevel" placeholder="请选择任务等级" clearable>
          <el-option
            v-for="dict in dict.type.tk_task"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
<!--      <el-form-item label="任务创建时间" prop="createdAt">-->
<!--        <el-date-picker clearable-->
<!--                        v-model="queryParams.createdAt"-->
<!--                        type="date"-->
<!--                        value-format="yyyy-MM-dd"-->
<!--                        placeholder="请选择任务创建时间">-->
<!--        </el-date-picker>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="状态" prop="state">-->
<!--        <el-input-->
<!--          v-model="queryParams.state"-->
<!--          placeholder="请输入状态"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
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
          v-hasPermi="['task:tasks:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="multiple"
          @click="handleUpdateArr"
          v-hasPermi="['task:tasks:edit']"
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
          v-hasPermi="['task:tasks:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['task:tasks:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="tasksList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="任务id" align="center" prop="id" />
      <el-table-column label="任务标题" align="center" prop="title" />
      <el-table-column label="任务图片链接" align="center" prop="image" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.image" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="任务描述" align="center" prop="description" />
      <el-table-column label="任务的详细描述" align="center" prop="detailedDescription" >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            @click="
              smContent = scope.row.detailedDescription;
              open1 = true;
            "
            >查看详情</el-button
          >
        </template>
      </el-table-column>
      <el-table-column label="奖励金额" align="center" prop="rewardAmount" />
      <el-table-column label="任务链接" align="center" prop="link" />
      <el-table-column label="任务总数量" align="center" prop="totalQuantity" />
      <el-table-column label="剩余数量" align="center" prop="surplusquantity" />
      <!-- <el-table-column label="已完成数量" align="center" prop="completedQuantity" /> -->
      <el-table-column label="任务等级" align="center" prop="taskLevel">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.tk_task" :value="scope.row.taskLevel"/>
        </template>
      </el-table-column>
      <!-- <el-table-column label="任务创建时间" align="center" prop="createdAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column> -->
<!--      <el-table-column label="状态" align="center" prop="state" />-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['task:tasks:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['task:tasks:remove']"
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
      :page-sizes="[10, 20, 30, 50, 100, 200]"
    />

    <!-- 添加或修改任务列对话框 -->
    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
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
        <el-form-item label="任务标题" prop="title">
          <el-input v-model="titleObj[language]" placeholder="请输入任务标题" />
        </el-form-item>
        <el-form-item label="任务图片链接" prop="image">
          <image-upload v-model="form.image"/>
        </el-form-item>
        <el-form-item label="任务描述" prop="description">
          <el-input v-model="descriptionObj[language]" placeholder="请输入任务描述内容" />
        </el-form-item>
        <el-form-item label="任务的详细描述" prop="detailedDescription">
          <el-input v-model="detailedDescriptionObj[language]" type="textarea" placeholder="请输入任务的详细描述" />
        </el-form-item>
        <el-form-item label="奖励金额" prop="rewardAmount">
          <el-input v-model="form.rewardAmount" placeholder="请输入奖励金额" />
        </el-form-item>
        <el-form-item label="任务链接" prop="link">
          <el-input v-model="form.link" placeholder="请输入任务链接" />
        </el-form-item>
        <el-form-item label="任务总数量" prop="totalQuantity">
          <el-input v-model="form.totalQuantity" placeholder="请输入任务总数量" />
        </el-form-item>
        <el-form-item label="剩余数量" prop="surplusquantity">
          <el-input v-model="form.surplusquantity" placeholder="请输入剩余数量" />
        </el-form-item>
        <!-- <el-form-item label="已完成数量" prop="completedQuantity">
          <el-input v-model="form.completedQuantity" placeholder="请输入已完成数量" />
        </el-form-item> -->
        <el-form-item label="任务等级" prop="taskLevel">
          <el-select v-model="form.taskLevel" placeholder="请选择任务等级">
            <el-option
              v-for="dict in dict.type.tk_task"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <!-- <el-form-item label="任务创建时间" prop="createdAt">
          <el-date-picker clearable
                          v-model="form.createdAt"
                          type="datetime"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          placeholder="请选择任务创建时间">
          </el-date-picker>
        </el-form-item> -->
<!--        <el-form-item label="状态" prop="state">-->
<!--          <el-input v-model="form.state" placeholder="请输入状态" />-->
<!--        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog
      :close-on-click-modal="false"
      title="任务的详细描述"
      :visible.sync="open1"
      width="700px"
      append-to-body
    >
      <div class="smContent" v-html="smContent"></div>
    </el-dialog>
    <el-dialog :close-on-click-modal="false" title="批量修改任务" :visible.sync="open2" width="700px" append-to-body>
      <el-form ref="form2" :model="form2" label-width="120px">
        <el-form-item label="任务标题" prop="title">
          <el-input v-model="form2.title" placeholder="请输入任务标题" />
        </el-form-item>
        <el-form-item label="奖励金额" prop="rewardAmount">
          <el-input v-model="form2.rewardAmount" placeholder="请输入奖励金额" />
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm2">确 定</el-button>
        <el-button @click="cancel2">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listTasks, getTasks, delTasks, addTasks, updateTasks, BatchChanges } from "@/api/task/tasks";

export default {
  name: "Tasks",
  dicts: ['tk_task'],
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
      // 任务列表格数据
      tasksList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        image: null,
        description: null,
        detailedDescription: null,
        rewardAmount: null,
        link: null,
        totalQuantity: null,
        surplusquantity: null,
        completedQuantity: null,
        taskLevel: null,
        createdAt: null,
        state: null,
        language: "Chinese"
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        // title: [
        //   { required: true, message: "任务标题不能为空", trigger: "blur" }
        // ],
        // totalQuantity: [
        //   { required: true, message: "任务总数量不能为空", trigger: "blur" }
        // ],
      },
      smContent: "",
      open1: false,
      form2: {},
      open2: false,
      language: "Chinese",
      titleObj: {
        Chinese: "",
        English: "",
      },
      descriptionObj: {
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
      }]
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询任务列列表 */
    getList() {
      this.loading = true;
      listTasks(this.queryParams).then(response => {
        this.tasksList = response.rows;
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
        title: null,
        image: null,
        description: null,
        detailedDescription: null,
        rewardAmount: null,
        link: null,
        totalQuantity: null,
        surplusquantity: null,
        completedQuantity: null,
        taskLevel: null,
        createdAt: null,
        state: null
      };
      this.titleObj = {
        Chinese: "",
        English: "",
      };
      this.descriptionObj = {
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
      this.title = "添加任务列";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getTasks(id).then(response => {
        this.form = response.data;
        this.titleObj = JSON.parse(this.form.title);
        this.descriptionObj = JSON.parse(this.form.description);
        this.detailedDescriptionObj = JSON.parse(this.form.detailedDescription);
        this.open = true;
        this.title = "修改任务列";
      });
    },
    handleUpdateArr(row) {
      this.reset2();
      const id = row.id || this.ids;
      this.form2.idList = id;
      this.open2 = true;
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (!this.titleObj.Chinese) {
            return this.$modal.msgError("请输入任务标题");
          }
          this.form.title = JSON.stringify(this.titleObj);

          if (!this.descriptionObj.Chinese) {
            return this.$modal.msgError("请输入任务描述内容");
          }
          this.form.description = JSON.stringify(this.descriptionObj);

          if (!this.detailedDescriptionObj.Chinese) {
            return this.$modal.msgError("请输入任务的详细描述");
          }
          this.form.detailedDescription = JSON.stringify(this.detailedDescriptionObj);
          if (this.form.id != null) {
            updateTasks(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTasks(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除任务列编号为"' + ids + '"的数据项？').then(function() {
        return delTasks(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('task/tasks/export', {
        ...this.queryParams
      }, `tasks_${new Date().getTime()}.xlsx`)
    },
    submitForm2() {
      this.$refs["form2"].validate(valid => {
        if (valid) {
          BatchChanges(this.form2).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open2 = false;
              this.getList();
            });
        }
      });
    },
    cancel2() {
      this.open2 = false;
      this.reset2();
    },
    reset2() {
      this.form2 = {
        idList: null,
        title: null,
        rewardAmount: null
      };
      this.resetForm("form2");
    },
  }
};
</script>
