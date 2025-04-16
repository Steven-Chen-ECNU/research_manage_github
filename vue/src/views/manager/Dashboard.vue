<template>
  <div>
    <div style="display: flex">
      <div style="flex: 1; margin-right: 5px; display: flex; height: 120px; align-items: center" class="card">
        <div style="flex: 1; text-align: center">
          <img src="@/assets/imgs/4.png" alt="" style="width: 70px; height: 60px">
        </div>
        <div style="flex: 1">
          <div style="font-size: 20px">科研项目数</div>
          <div style="font-size: 20px; margin-top: 10px; font-weight: bold">{{ data.baseData.project }}</div>
        </div>
      </div>
      <div style="flex: 1; margin: 0 5px; display: flex; height: 120px; align-items: center" class="card">
        <div style="flex: 1; text-align: center">
          <img src="@/assets/imgs/1.png" alt="" style="width: 70px; height: 60px">
        </div>
        <div style="flex: 1">
          <div style="font-size: 20px">反馈总数</div>
          <div style="font-size: 20px; margin-top: 10px; font-weight: bold">{{ data.baseData.feedback }}</div>
        </div>
      </div>
      <div style="flex: 1; margin: 0 5px; display: flex; height: 120px; align-items: center" class="card">
        <div style="flex: 1; text-align: center">
          <img src="@/assets/imgs/3.png" alt="" style="width: 70px; height: 60px">
        </div>
        <div style="flex: 1">
          <div style="font-size: 20px">活动总数</div>
          <div style="font-size: 20px; margin-top: 10px; font-weight: bold">{{ data.baseData.activity }}</div>
        </div>
      </div>
      <div style="flex: 1; margin-left: 5px; display: flex; height: 120px; align-items: center" class="card">
        <div style="flex: 1; text-align: center">
          <img src="@/assets/imgs/2.png" alt="" style="width: 70px; height: 60px">
        </div>
        <div style="flex: 1">
          <div style="font-size: 20px">教师总数</div>
          <div style="font-size: 20px; margin-top: 10px; font-weight: bold">{{ data.baseData.teacher }}</div>
        </div>
      </div>
    </div>
    <div style="margin-top: 10px; display: flex">
      <div id="pie" style="flex: 1; margin-right: 5px; height: 400px" class="card"></div>
      <div id="bar1" style="flex: 1; margin-left: 5px; height: 400px" class="card"></div>
    </div>
    <div style="margin-top: 10px; display: flex">
      <div id="bar2" style="flex: 1; margin-right: 5px; height: 400px" class="card"></div>
      <div id="line" style="flex: 1; margin-left: 5px; height: 400px" class="card"></div>
    </div>
  </div>
</template>

<script setup>

import { reactive, onMounted } from "vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";
import * as echarts from "echarts";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  baseData: {}
})

const loadBaseData = () => {
  request.get('/dashboard/base').then(res => {
    if (res.code === '200') {
      data.baseData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadBaseData()

let pieOptions = {
  title: {
    text: '系统中不同等级的科研项目数量',
    subtext: '统计维度：项目等级',
    left: 'center'
  },
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b} : {c} ({d}%)'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      name: '占比数据',
      type: 'pie',
      radius: '50%',
      center: ['50%', '60%'],
      data: [
        { value: 1048, name: 'Search Engine' },
        { value: 735, name: 'Direct' },
        { value: 580, name: 'Email' },
        { value: 484, name: 'Union Ads' },
        { value: 300, name: 'Video Ads' }
      ],
    }
  ]
}

const loadPie = () => {
  echarts.dispose(document.getElementById('pie'))
  request.get('/dashboard/pie').then(res => {
    if (res.code === '200') {
      let chartDom = document.getElementById('pie')
      let myChart = echarts.init(chartDom)
      pieOptions.series[0].data = res.data
      myChart.setOption(pieOptions)
    }
  })
}
onMounted(() => {
  loadPie()
})

// 柱状图1数据结构
let bar1Options = {
  title: {
    text: '系统中教师申请的学术活动数量', // 主标题
    subtext: '统计维度：活动名称', // 副标题
    left: 'center'
  },
  xAxis: {
    type: 'category',
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'], // 示例数据：统计的维度（横坐标）
    axisLabel: {    //重点在这一块，其余可以忽略
      interval: 0,   //这个一定要有，别忘记了
      rotate: 15,
      textStyle: {
        color: '#666666',
        fontSize: 10
      }
    },
  },
  yAxis: {
    type: 'value'
  },
  tooltip: {
    trigger: 'item'
  },
  series: [
    {
      data: [120, 200, 150, 80, 70, 110, 130], // 示例数据：横坐标维度对应的值（纵坐标）
      type: 'bar',
      itemStyle: {
        normal: {
          color: function () { return "#" + Math.floor(Math.random() * (256 * 256 * 256 - 1)).toString(16); }
        },
      },
    }
  ]
}
// 柱状图2数据结构
let bar2Options = {
  title: {
    text: '系统中不同类型的科研成果数量', // 主标题
    subtext: '统计维度：成果分类', // 副标题
    left: 'center'
  },
  xAxis: {
    type: 'category',
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'], // 示例数据：统计的维度（横坐标）
    axisLabel: {    //重点在这一块，其余可以忽略
      interval: 0,   //这个一定要有，别忘记了
      rotate: 15,
      textStyle: {
        color: '#666666',
        fontSize: 10
      }
    },
  },
  yAxis: {
    type: 'value'
  },
  tooltip: {
    trigger: 'item'
  },
  series: [
    {
      data: [120, 200, 150, 80, 70, 110, 130], // 示例数据：横坐标维度对应的值（纵坐标）
      type: 'bar',
      itemStyle: {
        normal: {
          color: function () { return "#" + Math.floor(Math.random() * (256 * 256 * 256 - 1)).toString(16); }
        },
      },
    }
  ]
}
// 平滑折线图数据结构
let lineOptions = {
  title: {
    text: '系统近期反馈数量', // 主标题
    left: 'center'
  },
  xAxis: {
    type: 'category',
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'] // 示例数据：统计的维度（横坐标）
  },
  yAxis: {
    type: 'value'
  },
  tooltip: {
    trigger: 'item'
  },
  series: [
    {
      data: [120, 200, 150, 80, 70, 110, 130], // 示例数据：横坐标维度对应的值（纵坐标）
      type: 'line',
      smooth: true
    }
  ]
}
const loadBar1 = () => {
  echarts.dispose(document.getElementById('bar1'))
  request.get('/dashboard/bar1').then(res => {
    if (res.code === '200') {
      let chartDom = document.getElementById('bar1')
      let myChart = echarts.init(chartDom)
      bar1Options.xAxis.data = res.data.xAxis
      bar1Options.series[0].data = res.data.yAxis
      myChart.setOption(bar1Options)
    }
  })
}
const loadBar2 = () => {
  echarts.dispose(document.getElementById('bar2'))
  request.get('/dashboard/bar2').then(res => {
    if (res.code === '200') {
      let chartDom = document.getElementById('bar2')
      let myChart = echarts.init(chartDom)
      bar2Options.xAxis.data = res.data.xAxis
      bar2Options.series[0].data = res.data.yAxis
      myChart.setOption(bar2Options)
    }
  })
}
const loadLine = () => {
  echarts.dispose(document.getElementById('line'))
  request.get('/dashboard/line').then(res => {
    if (res.code === '200') {
      let chartDom = document.getElementById('line')
      let myChart = echarts.init(chartDom)
      lineOptions.xAxis.data = res.data.xAxis
      lineOptions.series[0].data = res.data.yAxis
      myChart.setOption(lineOptions)
    }
  })
}
loadBaseData()
onMounted(() => {
  loadPie()
  loadBar1()
  loadBar2()
  loadLine()
})
</script>