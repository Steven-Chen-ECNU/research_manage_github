<template>
    <!-- 中间区域 -->
    <div class="module-box">
      <div style="flex:0 1 30%;">
        <div style="margin-bottom: 8px; text-align: center; color: #00eaff; font-size: 18px;">某某高校科研活跃度</div>
        <dv-border-box12 style="width:100%;height:200px;" >
          <dv-percent-pond :config="config1" style="width:100%;height:200px;" />
        </dv-border-box12>
        <dv-border-box8 style="width:100%;height:400px;" >
          <myChart />
        </dv-border-box8>
      </div>

      <div style="flex:0 1 40%;">
        <dv-border-box10 style="width:100%;height:600px;" >
          <FlylineChart height="560px" />
        </dv-border-box10>
      </div>

      <div style="flex:0 1 30%;">
        <dv-border-box13 style="width:100%;height:320px;" >
          <ConicalChart height="260px" />
        </dv-border-box13>
        <div style="margin-bottom: 8px; text-align: center; color: #00eaff; font-size: 18px;">某某高校科研活跃度</div>
        <dv-border-box12 style="width:100%;height:260px;" >
          <dv-water-level-pond :config="config3" style="width:100%;height:260px;" />
        </dv-border-box12>
      </div>
    </div>

</template>

<script setup>
import {ref ,reactive, onMounted} from 'vue'
import ConicalChart from './ConicalChart.vue'
import FlylineChart from './FlylineChart.vue'
import myChart from './myChart.vue'
import request from '@/utils/request'

const config1 = reactive({
  value: 0,
})

const config3 = reactive({
  data: [0, 100],
  shape: 'roundRect',
})

onMounted(async () => {
  // 获取教师总数
  const baseRes = await request.get('/dashboard/base')
  const totalTeacherCount = baseRes.data.teacher
  // 获取活跃教师数
  const activeRes = await request.get('/dashboard/activeTeacherCount')
  const activeTeacherCount = activeRes.data
  // 计算百分比
  const percent = totalTeacherCount === 0 ? 0 : Math.round((activeTeacherCount / totalTeacherCount) * 100)
  config1.value = percent
  config3.data = [percent, 100 - percent]
})
</script>


