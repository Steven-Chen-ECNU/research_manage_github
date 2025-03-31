<template>
    <div>
        <div class="card" style="margin-bottom: 5px">
            <el-input v-model="data.projectName" prefix-icon="Search" style="width: 240px; margin-right: 10px"
                placeholder="请输入项目名称查询"></el-input>
            <el-button type="info" plain @click="load">查询</el-button>
            <el-button type="warning" plain style="margin: 0 10px" @click="reset">重置</el-button>
        </div>
        <div class="card" style="margin-bottom: 5px">
            <el-button v-if="data.user.role === 'TEACHER'" type="primary" plain @click="handleAdd">新增</el-button>
            <el-button v-if="data.user.role === 'ADMIN'" type="danger" plain @click="delBatch">批量删除</el-button>
        </div>

        <div class="card" style="margin-bottom: 5px">
            <el-table stripe :data="data.tableData" @selection-change="handleSelectionChange">
                <el-table-column v-if="data.user.role === 'ADMIN'" type="selection" width="55" />
                <el-table-column prop="projectName" label="项目名称" />
                <el-table-column prop="projectCode" label="项目编号" />
                <el-table-column prop="teacherName" label="教师姓名" />
                <el-table-column prop="time" label="报告日期" />
                <el-table-column prop="content" label="工作内容" show-overflow-tooltip />
                <el-table-column prop="question" label="遇到的问题" show-overflow-tooltip />
                <el-table-column prop="solution" label="解决方案" show-overflow-tooltip />
                <el-table-column label="操作" width="100" fixed="right">
                    <template v-slot="scope">
                        <el-button v-if="data.user.role === 'TEACHER'" type="primary" circle :icon="Edit"
                            @click="handleEdit(scope.row)"></el-button>
                        <el-button type="danger" circle :icon="Delete" @click="del(scope.row.id)"></el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div class="card" v-if="data.total">
            <el-pagination @current-change="load" background layout="prev, pager, next" :page-size="data.pageSize"
                v-model:current-page="data.pageNum" :total="data.total" />
        </div>

        <el-dialog title="科研过程" v-model="data.formVisible" width="40%" destroy-on-close>
            <el-form ref="formRef" :rules="rules" :model="data.form" label-width="85px" style="padding: 20px">
                <el-form-item prop="projectId" label="科研项目">
                    <el-select v-model="data.form.projectId" placeholder="请选择科研项目">
                        <el-option v-for="item in data.projectData" :key="item.id" :label="item.name" :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="content" label="工作内容">
                    <el-input type="textarea" :rows="4" v-model="data.form.content" placeholder="请输入工作内容"></el-input>
                </el-form-item>
                <el-form-item prop="question" label="遇到的问题">
                    <el-input v-model="data.form.question" placeholder="请输入遇到的问题"></el-input>
                </el-form-item>
                <el-form-item prop="solution" label="解决方案">
                    <el-input type="textarea" :rows="4" v-model="data.form.solution" placeholder="请输入解决方案"></el-input>
                </el-form-item>
                <el-form-item prop="time" label="报告日期">
                    <el-date-picker style="width: 100%" v-model="data.form.time" value-format="YYYY-MM-DD" type="date"
                        placeholder="请选择日期"></el-date-picker>
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

import { reactive, ref } from "vue";
import request from "@/utils/request.js";
import { ElMessage, ElMessageBox } from "element-plus";
import { Delete, Edit } from "@element-plus/icons-vue";


const data = reactive({
    user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
    formVisible: false,
    form: {},
    tableData: [],
    pageNum: 1,
    pageSize: 10,
    total: 0,
    projectName: null,
    ids: [],
    projectData: []
})

const formRef = ref()
const rules = reactive({
    projectId: [
        { required: true, message: '请选择科研项目', trigger: 'blur' },
    ],
    content: [
        { required: true, message: '请输入工作内容', trigger: 'blur' },
    ],
    time: [
        { required: true, message: '请选择报告日期', trigger: 'blur' },
    ],
})

const loadProject = () => {
    request.get('/project/selectAll', {
        params: {
            teacherId: data.user.id,
            status: '审核通过'
        }
    }).then(res => {
        if (res.code === '200') {
            data.projectData = res.data
        } else {
            ElMessage.error(res.msg)
        }
    })
}
const load = () => {
    request.get('/process/selectPage', {
        params: {
            pageNum: data.pageNum,
            pageSize: data.pageSize,
            projectName: data.projectName
        }
    }).then(res => {
        if (res.code === '200') {
            data.tableData = res.data?.list || []
            data.total = res.data?.total
        }
    })
}
const handleAdd = () => {
    data.form = {}
    data.formVisible = true
}
const handleEdit = (row) => {
    data.form = JSON.parse(JSON.stringify(row))
    data.formVisible = true
}
const add = () => {
    request.post('/process/add', data.form).then(res => {
        if (res.code === '200') {
            ElMessage.success('操作成功')
            data.formVisible = false
            load()
        } else {
            ElMessage.error(res.msg)
        }
    })
}

const update = () => {
    request.put('/process/update', data.form).then(res => {
        if (res.code === '200') {
            ElMessage.success('操作成功')
            data.formVisible = false
            load()
        }
    })
}

const save = () => {
    data.form.id ? update() : add()
}

const del = (id) => {
    ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
        request.delete('/process/delete/' + id).then(res => {
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
const delBatch = () => {
    if (!data.ids.length) {
        ElMessage.warning("请选择数据")
        return
    }
    ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
        request.delete("/process/delete/batch", { data: data.ids }).then(res => {
            if (res.code === '200') {
                ElMessage.success('操作成功')
                load()
            } else {
                ElMessage.error(res.msg)
            }
        })
    }).catch(err => {
        console.error(err)
    })
}
const handleSelectionChange = (rows) => {
    data.ids = rows.map(v => v.id)
}

const reset = () => {
    data.projectName = null
    load()
}

load()
loadProject()
</script>