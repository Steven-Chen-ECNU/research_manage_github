<template>
    <div>
        <div class="card" style="margin-bottom: 5px">
            <el-select v-model="data.type" placeholder="请选择学术类型" style="width: 200px; margin-right: 10px">
                <el-option label="学术会议" value="学术会议"></el-option>
                <el-option label="学术讲座" value="学术讲座"></el-option>
                <el-option label="学术论坛" value="学术论坛"></el-option>
                <el-option label="学术竞赛" value="学术竞赛"></el-option>
                <el-option label="学术研讨会" value="学术研讨会"></el-option>
            </el-select>
            <el-input v-model="data.name" prefix-icon="Search" style="width: 240px; margin-right: 10px"
                placeholder="请输入活动名称查询"></el-input>
            <el-button type="success" plain @click="load">查询</el-button>
            <!-- <el-button type="warning" plain style="margin: 0 10px" @click="reset">重置</el-button> -->
            <el-button type="primary" round @click="handleAdd">新增</el-button>
            <el-button type="danger" round @click="delBatch">批量删除</el-button>
        </div>
        <!-- <div class="card" style="margin-bottom: 5px">
            <el-button type="primary" plain @click="handleAdd">新增</el-button>
            <el-button type="danger" plain @click="delBatch">批量删除</el-button>
        </div> -->

        <div class="card" style="margin-bottom: 5px">
            <el-table stripe :data="data.tableData" @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55" />
                <el-table-column prop="img" label="活动封面" width="100">
                    <template v-slot="scope">
                        <el-image style="width: 60px; height: 60px; border-radius: 5px; display: block"
                            v-if="scope.row.img" :src="scope.row.img" :preview-src-list="[scope.row.img]"
                            preview-teleported></el-image>
                    </template>
                </el-table-column>
                <el-table-column prop="type" label="学术类型" width="100" />
                <el-table-column prop="name" label="活动名称" show-overflow-tooltip />
                <el-table-column prop="content" label="活动描述" show-overflow-tooltip />
                <el-table-column prop="location" label="位置" />
                <el-table-column prop="start" label="开始时间" width="120" />
                <el-table-column prop="end" label="结束时间" width="120" />
                <el-table-column prop="status" label="活动状态" width="100">
                    <template v-slot="scope">
                        <el-tag v-if="scope.row.status === '进行中'" type="success">{{ scope.row.status }}</el-tag>
                        <el-tag v-if="scope.row.status === '未开始'" type="warning">{{ scope.row.status }}</el-tag>
                        <el-tag v-if="scope.row.status === '已结束'" type="danger">{{ scope.row.status }}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="100" fixed="right">
                    <template v-slot="scope">
                        <el-button type="primary" circle :icon="Edit" @click="handleEdit(scope.row)"
                            title="编辑"></el-button>
                        <el-button type="danger" circle :icon="Delete" @click="del(scope.row.id)"
                            title="删除"></el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div class="card" v-if="data.total">
            <el-pagination @current-change="load" background layout="prev, pager, next" :page-size="data.pageSize"
                v-model:current-page="data.pageNum" :total="data.total" />
        </div>

        <el-dialog title="学术活动" v-model="data.formVisible" width="40%" destroy-on-close>
            <el-form ref="formRef" :rules="rules" :model="data.form" label-width="80px" style="padding: 20px">
                <el-form-item prop="avatar" label="活动封面">
                    <el-upload :action="baseUrl + '/files/upload'" :on-success="handleFileUpload" list-type="picture">
                        <el-button type="primary">上传活动封面</el-button>
                    </el-upload>
                </el-form-item>
                <el-form-item prop="name" label="活动名称">
                    <el-input v-model="data.form.name" placeholder="请输入活动名称"></el-input>
                </el-form-item>
                <el-form-item prop="type" label="学术类型">
                    <el-select v-model="data.form.type" placeholder="请选择学术类型">
                        <el-option label="学术会议" value="学术会议"></el-option>
                        <el-option label="学术讲座" value="学术讲座"></el-option>
                        <el-option label="学术论坛" value="学术论坛"></el-option>
                        <el-option label="学术竞赛" value="学术竞赛"></el-option>
                        <el-option label="学术研讨会" value="学术研讨会"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="content" label="活动描述">
                    <el-input type="textarea" :rows="4" v-model="data.form.content" placeholder="请输入活动描述"></el-input>
                </el-form-item>
                <el-form-item prop="location" label="举办地点">
                    <el-select v-model="data.form.location" placeholder="请选择举办地点" style="width: 100%">
                        <el-option label="黄浦区" value="黄浦区"></el-option>
                        <el-option label="徐汇区" value="徐汇区"></el-option>
                        <el-option label="长宁区" value="长宁区"></el-option>
                        <el-option label="静安区" value="静安区"></el-option>
                        <el-option label="普陀区" value="普陀区"></el-option>
                        <el-option label="虹口区" value="虹口区"></el-option>
                        <el-option label="杨浦区" value="杨浦区"></el-option>
                        <el-option label="浦东新区" value="浦东新区"></el-option>
                        <el-option label="闵行区" value="闵行区"></el-option>
                        <el-option label="宝山区" value="宝山区"></el-option>
                        <el-option label="嘉定区" value="嘉定区"></el-option>
                        <el-option label="金山区" value="金山区"></el-option>
                        <el-option label="松江区" value="松江区"></el-option>
                        <el-option label="青浦区" value="青浦区"></el-option>
                        <el-option label="奉贤区" value="奉贤区"></el-option>
                        <el-option label="崇明区" value="崇明区"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="start" label="开始日期">
                    <el-date-picker value-format="YYYY-MM-DD" v-model="data.form.start" type="date" placeholder="请选择日期"
                        style="width: 100%"></el-date-picker>
                </el-form-item>
                <el-form-item prop="end" label="结束日期">
                    <el-date-picker value-format="YYYY-MM-DD" v-model="data.form.end" type="date" placeholder="请选择日期"
                        style="width: 100%"></el-date-picker>
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
const baseUrl = import.meta.env.VITE_BASE_URL

const data = reactive({
    formVisible: false,
    form: {},
    tableData: [],
    pageNum: 1,
    pageSize: 10,
    total: 0,
    type: null,
    name: null,
    ids: []
})

const formRef = ref()
const rules = reactive({
    name: [
        { required: true, message: '请输入活动名称', trigger: 'blur' },
    ],
    type: [
        { required: true, message: '请选择学术类型', trigger: 'blur' },
    ],
    content: [
        { required: true, message: '请输入活动描述', trigger: 'blur' },
    ],
})

const load = () => {
    request.get('/activity/selectPage', {
        params: {
            pageNum: data.pageNum,
            pageSize: data.pageSize,
            type: data.type,
            name: data.name
        }
    }).then(res => {
        if (res.code === '200') {
            data.tableData = res.data?.list || []
            data.total = res.data?.total
        } else {
            ElMessage.error(res.msg)
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
    request.post('/activity/add', data.form).then(res => {
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
    request.put('/activity/update', data.form).then(res => {
        if (res.code === '200') {
            ElMessage.success('操作成功')
            data.formVisible = false
            load()
        }
    })
}

const save = () => {
    formRef.value.validate(valid => {
        if (valid) {
            data.form.id ? update() : add()
        }
    })
}

const del = (id) => {
    ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
        request.delete('/activity/delete/' + id).then(res => {
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
        request.delete("/activity/delete/batch", { data: data.ids }).then(res => {
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
    data.type = null
    data.name = null
    load()
}

const handleFileUpload = (res) => {
    data.form.img = res.data
}

load()
</script>