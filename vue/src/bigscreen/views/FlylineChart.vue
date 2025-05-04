<template>
  <div class="flyline-container">
    <dv-flyline-chart :config="config" :style="{ width: '100%', height: prop.height }" />
    <div v-for="point in config.points" :key="point.name"
         class="point-hover-area"
         :style="getPointStyle(point)"
         @mouseenter="() => showPointTooltip(point)"
         @mouseleave="hideTooltip">
    </div>
    <div v-if="showTooltip" class="custom-tooltip">
      {{ tooltipContent }}
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '@/utils/request'

const prop = defineProps({
  height: {
    type: String
  }
})

const showTooltip = ref(false)
const tooltipContent = ref('')

function showPointTooltip(point) {
  showTooltip.value = true
  tooltipContent.value = `${point.name}科研活力值：${point.value}`
}
function hideTooltip() {
  showTooltip.value = false
}

function getPointStyle(point) {
  // 以父容器100%宽高为基准，定位到点的中心，宽高20px
  return {
    position: 'absolute',
    left: `calc(${point.position[0] * 100}% - 10px)`,
    top: `calc(${point.position[1] * 100}% - 10px)`,
    width: '20px',
    height: '20px',
    cursor: 'pointer',
    zIndex: 10,
    background: 'rgba(0,0,0,0)',
  }
}

const config = ref({  //飞线图配置
  centerPoint: [0.5, 0.5], //上海中心点坐标
  points: [
    { position: [0.635, 0.595], name: '浦东新区', value: 0 },
    { position: [0.445, 0.575], name: '徐汇区', value: 0 },
    { position: [0.483, 0.532], name: '黄浦区', value: 0 },
    { position: [0.418, 0.46], name: '静安区', value: 0 },
    { position: [0.508, 0.463], name: '杨浦区', value: 0 },
    { position: [0.438, 0.63], name: '闵行区', value: 0 },
    { position: [0.49, 0.485], name: '虹口区', value: 0 },
    { position: [0.374, 0.544], name: '长宁区', value: 0 },
    { position: [0.374, 0.5], name: '普陀区', value: 0 },
    { position: [0.421, 0.405], name: '宝山区', value: 0 },
    { position: [0.285, 0.42], name: '嘉定区', value: 0 },
    { position: [0.33, 0.808], name: '金山区', value: 0 },
    { position: [0.2835, 0.66], name: '松江区', value: 0 },
    { position: [0.1327, 0.615], name: '青浦区', value: 0 },
    { position: [0.512, 0.758], name: '奉贤区', value: 0 },
    { position: [0.586, 0.235], name: '崇明区', value: 0 }
  ],
  lineWidth: 1, //飞线宽度 默认 1
  bgImgUrl: '/src/bigscreen/assets/img/shmap.png',//背景图url（上海地图）
  centerPointImg: {
    url: '/src/bigscreen/assets/img/mapCenterPoint.png'
  },
  pointsImg: {
    url: '/src/bigscreen/assets/img/mapPoint.png'
  }
});

// 获取各区科研活力数据
const getResearchVitality = async () => {
  try {
    const response = await request.get('/dashboard/researchVitality')
    if (Array.isArray(response.data) && response.data.length > 0) {
      config.value.points = config.value.points.map(point => {
        const sectionData = response.data.find(item => {
          const sectionPositions = {
            '浦东新区': [0.635, 0.595],
            '徐汇区': [0.445, 0.575],
            '黄浦区': [0.483, 0.532],
            '静安区': [0.418, 0.46],
            '杨浦区': [0.508, 0.463],
            '闵行区': [0.438, 0.63],
            '虹口区': [0.49, 0.485],
            '长宁区': [0.374, 0.544],
            '普陀区': [0.374, 0.5],
            '宝山区': [0.421, 0.405],
            '嘉定区': [0.285, 0.42],
            '金山区': [0.33, 0.808],
            '松江区': [0.2835, 0.66],
            '青浦区': [0.1327, 0.615],
            '奉贤区': [0.512, 0.758],
            '崇明区': [0.586, 0.235]
          }
          return JSON.stringify(sectionPositions[item.section]) === JSON.stringify(point.position)
        })
        if (sectionData) {
          return {
            ...point,
            value: parseInt(sectionData.count)
          }
        }
        return point
      })
    }
  } catch (error) {
    console.error('获取科研活力数据失败:', error)
  }
}

onMounted(() => {
  getResearchVitality()
})
</script>

<style scoped>
.flyline-container {
  position: relative;
  width: 100%;
  height: 100%;
}

.custom-tooltip {
  position: absolute;
  left: 20px;
  top: 20px;
  background: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 5px 10px;
  border-radius: 4px;
  font-size: 14px;
  pointer-events: none;
  z-index: 1000;
}

.point-hover-area {
  position: absolute;
  width: 20px;
  height: 20px;
  background: rgba(0,0,0,0);
  z-index: 10;
  cursor: pointer;
}
</style>
