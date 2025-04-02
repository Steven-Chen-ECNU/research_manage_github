<template>
    <div style="width: 80%; margin: 20px auto">
        <div class="card">
            <div style="font-weight: bold; font-size: 20px; margin-bottom: 10px">留下您的反馈信息：</div>
            <el-input type="textarea" :rows="5" v-model="data.content" placeholder="请输入反馈内容"></el-input>
            <div style="margin-top: 10px; text-align: right">
                <el-button type="primary" @click="submit">提交</el-button>
            </div>
        </div>
        <div class="card" style="margin-top: 20px; padding: 20px">
            <div style="font-size: 18px; color: #ce7b37; font-weight: bold; margin-bottom: 20px">看看其他教师提出的反馈内容 ({{
                data.feedBackData.length }})</div>
            <div v-for="item in data.feedBackData" style="margin-bottom: 20px">
                <div style="display: flex; align-items: center">
                    <div style="display: flex; align-items: center">
                        <img :src="item.teacherAvatar" alt="" style="width: 40px; height: 40px; border-radius: 50%">
                        <div style="margin-left: 10px">{{ item.teacherName }}</div>
                    </div>
                    <div style="flex: 1; margin: 0 40px; line-height: 25px; text-align: justify">{{ item.content }}
                    </div>
                    <div style="width: 200px; display: flex; align-items: center">
                        <div style="flex: 1">{{ item.time }}</div>
                        <el-icon style="width: 50px; color: red; cursor: pointer" v-if="data.user.id === item.teacherId"
                            @click="del(item.id)">
                            <Delete />
                        </el-icon>
                    </div>
                </div>
                <div style="display: flex; margin-top: 5px" v-if="item.replyContent">
                    <div style="width: 200px"></div>
                    <div style="flex: 1; color: #d48957">
                        {{ item.replyName }} 回复：{{ item.replyContent }}
                    </div>
                    <div style="width: 200px; color: #d48957">回复时间 {{ item.replyTime }}</div>
                </div>
            </div>

        </div>
    </div>
</template>

<script setup>

import { reactive, ref } from "vue";
import request from "@/utils/request.js";
import { ElMessage, ElMessageBox } from "element-plus";
import { Delete, Edit } from "@element-plus/icons-vue";
import router from "@/router/index.js";


const data = reactive({
    user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
    content: null,
    feedBackData: []
})

const load = () => {
    request.get('/feedback/selectAll').then(res => {
        if (res.code === '200') {
            data.feedBackData = res.data
        } else {
            ElMessage.error(res.msg)
        }
    })
}
const submit = () => {
    let submitData = {
        content: data.content
    }
    request.post('/feedback/add', submitData).then(res => {
        if (res.code === '200') {
            ElMessage.success('反馈成功')
            data.content = null
            load()
        } else {
            ElMessage.error(res.msg)
        }
    })
}
const del = (id) => {
    ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
        request.delete('/feedback/delete/' + id).then(res => {
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
load()

</script>