<template>
  <!-- 中间区域 -->
  <div class="module-box" style="height: 100vh; overflow: hidden; display: flex;">
    <div style="flex:0 1 30%; display: flex; flex-direction: column; height: 100%;">
      <div style="margin-bottom: 8px; text-align: center; color: #00eaff; font-size: 18px;">某某高校科研活跃度</div>
      <dv-border-box12 style="width:100%;height:30%;" >
        <dv-percent-pond :config="config1" style="width:100%;height:100%;" />
      </dv-border-box12>
      <dv-border-box8 style="width:100%;height:60%;display:flex;align-items:center;justify-content:center;" >
        <myChart />
      </dv-border-box8>
    </div>

    <div style="flex:0 1 40%; height: 100%; display: flex; flex-direction: column;">
      <dv-border-box10 style="width:100%;height:100%;" >
        <FlylineChart height="100%" />
      </dv-border-box10>
    </div>

    <div style="flex:0 1 30%; display: flex; flex-direction: column; height: 100%;">
      <dv-border-box13 style="width:100%;height:250px;">
        <div style="text-align:center;color:#00eaff;font-size:18px;margin-bottom:4px;">上海各地区科研活力图</div>
        <ConicalChart height="160px" />
      </dv-border-box13>
      <div style="margin-bottom: 8px; text-align: center; color: #00eaff; font-size: 18px;">某某高校科研活跃度</div>
      <dv-border-box12 style="width:100%;height:60%;" >
        <dv-water-level-pond :config="config3" style="width:100%;height:100%;" />
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
  data: [0,100],
  shape: 'roundRect',
  colors: ['#00eaff', '#3DE7C9', '#00eaff'],
  waveNum: 5,
  waveHeight: 30,
  waveOpacity: 0.4,
  formatter: '{value}%'
})

onMounted(async () => {
  // 获取科研活跃度数据（用于左上角和右下角）
  const activeRes = await request.get('/dashboard/activeTeacherCount')
  const activityValue = activeRes.data * 100  // 转换为百分比
  const roundedValue = Math.round(activityValue)
  
  // 两个图表使用相同的值
  config1.value = roundedValue
  config3.data = [roundedValue,roundedValue/2]
})
</script>
