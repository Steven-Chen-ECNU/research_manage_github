<template>
    <div>
        <el-row :gutter="10">
            <el-col :span="6" v-for="item in data.tableData" style="margin-bottom: 10px">
                <div class="card">
                    <img :src="item.img" alt="" style="width: 100%; height: 150px; border-radius: 5px">
                    <div style="margin-top: 5px">活动类型：<span style="color: #666666">{{ item.type }}</span></div>
                    <div class="overflow" style="margin-top: 5px">活动名称：<span style="color: #666666">{{ item.name
                            }}</span></div>
                    <div class="overflows" style="margin-top: 5px">活动详情：
                        <el-popover placement="top-start" :width="250" trigger="hover" :content="item.content">
                            <template #reference>
                                <span style="color: #666666">{{ item.content }}</span>
                            </template>
                        </el-popover>
                    </div>
                    <div style="margin-top: 5px">活动地点：<span style="color: #666666">{{ item.location }}</span></div>
                    <div style="margin-top: 5px">活动日期：<span style="color: #666666">{{ item.start }} ~ {{ item.end
                            }}</span></div>
                    <div style="margin-top: 5px">活动状态：
                        <el-tag v-if="item.status === '进行中'" type="success">{{ item.status }}</el-tag>
                        <el-tag v-if="item.status === '未开始'" type="warning">{{ item.status }}</el-tag>
                        <el-tag v-if="item.status === '已结束'" type="danger">{{ item.status }}</el-tag>
                    </div>
                    <div style="margin-top: 15px; text-align: center">
                        <el-button type="primary" style="padding: 18px 30px" :disabled="item.status !== '进行中'"
                            @click="submitInit(item)">申请</el-button>
                    </div>
                </div>
            </el-col>
        </el-row>

        <el-dialog title="申请理由" v-model="data.formVisible" width="40%" destroy-on-close>
            <el-form ref="form" :model="data.form" label-width="70px" style="padding: 20px">
                <el-form-item prop="content" label="申请理由">
                    <el-input type="textarea" :rows="4" v-model="data.description" placeholder="请输入申请理由"></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="cancel">取 消</el-button>
                    <el-button type="primary" @click="submit">提 交</el-button>
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
    tableData: [],
    pageNum: 1,
    pageSize: 10,
    total: 0,
    form: {},
    formVisible: false,
    description: null
})


const load = () => {
    request.get('/activity/selectPage', {
        params: {
            pageNum: data.pageNum,
            pageSize: data.pageSize,
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

const submitInit = (item) => {
    data.form = JSON.parse(JSON.stringify(item))
    data.formVisible = true
}
const submit = () => {
    let submitData = {
        activityId: data.form.id,
        description: data.description,
    }
    request.post('/apply/add', submitData).then(res => {
        if (res.code === '200') {
            ElMessage.success('申请成功，请耐心等待审核')
            data.formVisible = false
            data.description = null
            data.form = {}
            load()
        } else {
            ElMessage.error(res.msg)
        }
    })
}
const cancel = () => {
    data.formVisible = false
    data.description = null
    data.form = {}
    load()
}

load()
</script>
<style scope>
.overflow {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.overflows {
    word-break: break-all;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 2;
    /* 超出几行省略 */
    overflow: hidden;
}
</style>