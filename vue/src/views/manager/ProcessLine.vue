<template>
    <div style="margin-top: 20px">
        <el-timeline style="max-width: 600px">
            <el-timeline-item color="greenyellow" :timestamp="item.time" placement="top"
                v-for="item in data.processData">
                <el-card>
                    <div style="color: #666666; line-height: 25px"><span
                            style="font-size: 16px; font-weight: bold; color: #333333">工作内容：</span>{{ item.content }}
                    </div>
                    <div style="margin-top: 10px; color: red; line-height: 25px"><span
                            style="font-size: 16px; font-weight: bold; color: #333333">遇到的问题：</span>{{ item.question }}
                    </div>
                    <div style="margin-top: 10px; color: green; line-height: 25px"><span
                            style="font-size: 16px; font-weight: bold; color: #333333">解决方案：</span>{{ item.solution }}
                    </div>
                </el-card>
            </el-timeline-item>
        </el-timeline>
    </div>
</template>

<script setup>

import { reactive, ref } from "vue";
import request from "@/utils/request.js";
import { ElMessage, ElMessageBox } from "element-plus";
import { Delete, Edit } from "@element-plus/icons-vue";
import router from "@/router/index.js";


const data = reactive({
    projectId: router.currentRoute.value.query.id,
    processData: []
})

const loadProcess = () => {
    // 从路由中获取项目id
    data.projectId = router.currentRoute.value.query.id
    request.get('/process/selectAll', {
        // 条件查询
        params: {
            projectId: data.projectId
        }
    }).then(res => {
        if (res.code === '200') {
            data.processData = res.data
        } else {
            ElMessage.error(res.msg)
        }
    })
}
loadProcess()
</script>