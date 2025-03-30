<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input v-model="data.code" prefix-icon="Search" style="width: 240px; margin-right: 10px"
        placeholder="请输入项目编号查询"></el-input>
      <el-input v-model="data.name" prefix-icon="Search" style="width: 240px; margin-right: 10px"
        placeholder="请输入项目名称查询"></el-input>
      <el-button type="info" plain @click="load">查询</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">重置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px" v-if="data.user.role === 'TEACHER'">
      <el-button type="primary" plain @click="handleAdd">新增</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table stripe :data="data.tableData">
        <el-table-column prop="code" label="项目编号" />
        <el-table-column prop="name" label="项目名称" />
        <el-table-column prop="source" label="项目来源" />
        <el-table-column prop="level" label="项目等级" />
        <el-table-column prop="subject" label="学科" />
        <el-table-column prop="price" label="项目预算" />
        <el-table-column prop="start" label="开始时间" />
        <el-table-column prop="end" label="结束时间" />
        <el-table-column prop="file" label="申请材料" width="120">
          <template v-slot="scope">
            <el-button type="primary" @click="down(scope.row.file)">下载材料</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="teacherName" label="申请教师" />
        <el-table-column prop="status" label="审核状态">
          <template v-slot="scope">
            <el-tag v-if="scope.row.status === '审核通过'" type="success">{{ scope.row.status }}</el-tag>
            <el-tag v-if="scope.row.status === '待审核'" type="warning">{{ scope.row.status }}</el-tag>
            <el-tag v-if="scope.row.status === '不通过'" type="danger">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reason" label="审核原因" />
        <el-table-column prop="time" label="审核时间" />
        <el-table-column label="操作" width="100" fixed="right">
          <template v-slot="scope">
            <el-button type="primary" circle :icon="Edit" @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" circle :icon="Delete" @click="del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="prev, pager, next" :page-size="data.pageSize"
        v-model:current-page="data.pageNum" :total="data.total" />
    </div>

    <el-dialog title="科研项目信息" v-model="data.formVisible" width="40%" destroy-on-close>
      <el-form ref="form" :model="data.form" label-width="70px" style="padding: 20px">
        <el-form-item prop="file" label="申请材料">
          <el-upload :action="baseUrl + '/files/upload'" :on-success="handleFileUpload">
            <el-button type="primary">点击上传</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="source" label="项目来源">
          <el-input v-model="data.form.source" placeholder="请输入项目来源"></el-input>
        </el-form-item>
        <el-form-item prop="name" label="项目名称">
          <el-input v-model="data.form.name" placeholder="请输入项目名称"></el-input>
        </el-form-item>
        <el-form-item prop="level" label="项目等级">
          <el-select v-model="data.form.level" placeholder="请选择项目等级">
            <el-option label="一级" value="一级"></el-option>
            <el-option label="二级" value="二级"></el-option>
            <el-option label="三级" value="三级"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="subject" label="申请学科">
          <el-input v-model="data.form.subject" placeholder="请输入申请学科"></el-input>
        </el-form-item>
        <el-form-item prop="start" label="开始日期">
          <el-date-picker style="width: 100%" v-model="data.form.start" value-format="YYYY-MM-DD" type="date"
            placeholder="请选择日期"></el-date-picker>
        </el-form-item>
        <el-form-item prop="end" label="结束日期">
          <el-date-picker style="width: 100%" v-model="data.form.end" value-format="YYYY-MM-DD" type="date"
            placeholder="请选择日期"></el-date-picker>
        </el-form-item>
        <el-form-item prop="price" label="项目预算">
          <el-input-number v-model="data.form.price" :min="1" :max="1000000" label="label"></el-input-number>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>

import { reactive } from "vue";
import request from "@/utils/request.js";
import { ElMessage, ElMessageBox } from "element-plus";
import { Delete, Edit } from "@element-plus/icons-vue";
const baseUrl = import.meta.env.VITE_BASE_URL

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  formVisible: false,
  form: {},
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  code: null,
  name: null,
})

const load = () => {
  const params = {
    pageNum: data.pageNum,
    pageSize: data.pageSize,
    code: data.code,
    name: data.name
  };
  console.log('load 方法请求参数:', params);
  request.get('/project/selectPage', { params }).then(res => {
    console.log('load 方法后端返回的数据:', res);
    if (res.code === '200') {
      data.tableData = res.data?.list || [];
      data.total = res.data?.total;
      console.log('更新后的表格数据:', data.tableData);
    }
  }).catch(error => {
    console.error('加载数据请求出错:', error);
    ElMessage.error('加载数据请求出错，请稍后重试');
  });
};

const handleAdd = () => {
  data.form = {}
  data.formVisible = true
}
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}
const add = () => {
  request.post('/project/add', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功');
      data.formVisible = false;
      console.log('准备调用 load 方法');
      load();
      console.log('load 方法调用完成');
    } else {
      ElMessage.error(res.msg);
    }
  }).catch(error => {
    console.error('新增请求出错:', error);
    ElMessage.error('新增请求出错，请稍后重试');
  });
};

const update = () => {
  request.put('/project/update', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      data.formVisible = false
      load()
    }
  })
}

const save = () => {
  console.log(data.form)
  data.form.id ? update() : add()
}

const del = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/project/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success("删除成功")
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {
    console.error(err)
  })
}

const reset = () => {
  data.code = null
  data.name = null
  load()
}

const handleFileUpload = (res) => {
  data.form.file = res.data
}
const down = (url) => {
  window.open(url)
}

load()
</script>