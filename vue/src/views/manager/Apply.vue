<template>
    <div>
        <div class="card" style="margin-bottom: 5px">
            <el-input v-if="data.user.role === 'ADMIN'" v-model="data.teacherName" prefix-icon="Search"
                style="width: 240px; margin-right: 10px" placeholder="请输入教师名称查询"></el-input>
            <el-input v-model="data.activityName" prefix-icon="Search" style="width: 240px; margin-right: 10px"
                placeholder="请输入活动名称查询"></el-input>
            <el-button type="info" plain @click="load">查询</el-button>
            <el-button type="danger" round @click="delBatch">批量删除</el-button>

            <!-- <el-button type="warning" plain style="margin: 0 10px" @click="reset">重置</el-button> -->
        </div>
        <!-- <div class="card" style="margin-bottom: 5px" v-if="data.user.role === 'ADMIN'">
            <el-button type="danger" plain @click="delBatch">批量删除</el-button>
        </div> -->

        <div class="card" style="margin-bottom: 5px">
            <el-table stripe :data="data.tableData" @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55" />
                <el-table-column prop="teacherName" label="教师姓名" />
                <el-table-column prop="activityName" label="活动名称" />
                <el-table-column prop="description" label="申请理由" />
                <el-table-column prop="time" label="申请时间" />
                <el-table-column prop="status" label="申请状态">
                    <template v-slot="scope">
                        <el-tag v-if="scope.row.status === '审核通过'" type="success">{{ scope.row.status }}</el-tag>
                        <el-tag v-if="scope.row.status === '待审核'" type="warning">{{ scope.row.status }}</el-tag>
                        <el-tag v-if="scope.row.status === '不通过'" type="danger">{{ scope.row.status }}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="reason" label="审核理由" />
                <el-table-column prop="checkTime" label="审核时间" />
                <el-table-column label="操作" width="100" fixed="right">
                    <template v-slot="scope">
                        <el-button v-if="scope.row.status === '待审核' && data.user.role === 'TEACHER'" type="primary"
                            circle :icon="Edit" @click="handleEdit(scope.row)" title="编辑内容"></el-button>
                        <el-button v-if="scope.row.status === '待审核' && data.user.role === 'ADMIN'" type="primary" circle
                            :icon="View" @click="handleCheck(scope.row)" title="查看详情"></el-button>
                        <el-button type="danger" circle :icon="Delete" @click="del(scope.row.id)"
                            title="删除记录"></el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div class="card" v-if="data.total">
            <el-pagination @current-change="load" background layout="prev, pager, next" :page-size="data.pageSize"
                v-model:current-page="data.pageNum" :total="data.total" />
        </div>

        <el-dialog title="申请理由" v-model="data.formVisible" width="40%" destroy-on-close>
            <el-form ref="form" :model="data.form" label-width="70px" style="padding: 20px">
                <el-form-item prop="description" label="申请理由">
                    <el-input type="textarea" :rows="4" v-model="data.form.description"
                        placeholder="请输入申请理由"></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="data.formVisible = false">取 消</el-button>
                    <el-button type="primary" @click="update">确 定</el-button>
                </span>
            </template>
        </el-dialog>
        <el-dialog title="学术活动审核" v-model="data.checkVisible" width="40%" destroy-on-close>
            <el-form :model="data.form" label-width="70px" style="padding: 20px">
                <el-form-item prop="status" label="申请状态">
                    <el-select v-model="data.form.status" placeholder="请选择审核结果">
                        <el-option label="待审核" value="待审核"></el-option>
                        <el-option label="审核通过" value="审核通过"></el-option>
                        <el-option label="不通过" value="不通过"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="reason" label="审核理由">
                    <el-input v-model="data.form.reason" placeholder="请输入审核理由"></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="data.checkVisible = false">取 消</el-button>
                    <el-button type="primary" @click="submit">提 交</el-button>
                </span>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>

import { reactive } from "vue";
import request from "@/utils/request.js";
import { ElMessage, ElMessageBox } from "element-plus";
import { Delete, Edit, View } from "@element-plus/icons-vue";


const data = reactive({
    user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
    formVisible: false,
    checkVisible: false,
    form: {},
    tableData: [],
    pageNum: 1,
    pageSize: 10,
    total: 0,
    teacherName: null,
    activityName: null,
    ids: []
})

const load = () => {
    request.get('/apply/selectPage', {
        params: {
            pageNum: data.pageNum,
            pageSize: data.pageSize,
            teacherName: data.teacherName,
            activityName: data.activityName,
        }
    }).then(res => {
        if (res.code === '200') {
            data.tableData = res.data?.list || []
            data.total = res.data?.total
        }
    })
}
const update = () => {
    request.put('/apply/update', data.form).then(res => {
        if (res.code === '200') {
            ElMessage.success('操作成功')
            data.formVisible = false
            load()
        }
    })
}
const handleEdit = (row) => {
    data.form = JSON.parse(JSON.stringify(row))
    data.formVisible = true
}

const handleCheck = (row) => {
    data.form = JSON.parse(JSON.stringify(row))
    data.checkVisible = true
}

const submit = () => {
    request.put('/apply/check', data.form).then(res => {
        if (res.code === '200') {
            ElMessage.success('操作成功')
            data.checkVisible = false
            load()
        } else {
            ElMessage.error(res.msg)
        }
    })
}

const del = (id) => {
    ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
        request.delete('/apply/delete/' + id).then(res => {
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
        request.delete("/apply/delete/batch", { data: data.ids }).then(res => {
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
    data.teacherName = null
    data.activityName = null
    load()
}

load()
</script>