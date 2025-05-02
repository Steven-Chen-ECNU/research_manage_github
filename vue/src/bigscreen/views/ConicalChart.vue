<template>
  <div class="panel-padding">
    <dv-conical-column-chart :config="config" :style="{ width: '100%', height: prop.height }" />
  </div>
</template>

<script setup>
import { ref, defineProps, onMounted } from 'vue'
import axios from 'axios'
import request from '@/utils/request'

const prop = defineProps({
  height: {
    type: String
  }
})

const config = ref({
  // data: [
  //   {
  //     name: '徐汇区',
  //     value: 3
  //   },
  //   {
  //     name: '宝山区',
  //     value: 1
  //   },
  //   {
  //     name: '嘉定区',
  //     value: 1
  //   },
  //   {
  //     name: '奉贤区',
  //     value: 1
  //   }
  // ],
  data: [],
  img: [
    '/src/bigscreen/assets/img/1st.png',
    '/src/bigscreen/assets/img/2st.png',
    '/src/bigscreen/assets/img/3st.png',
    '/src/bigscreen/assets/img/4st.png',
    '/src/bigscreen/assets/img/5st.png',
    '/src/bigscreen/assets/img/6st.png',
    '/src/bigscreen/assets/img/7st.png'
  ],
})

// 获取各区科研活力数据
const getResearchVitality = async () => {
  try {
    const response = await request.get('/dashboard/researchVitality')
    console.log('原始数据:', response.data)

    // if (response.data && response.data.code === 200) {
    //   const rawData = response.data.data
    //   console.log('后端返回的数据:', rawData)

    //   // 保持现有数据作为默认值
    //   const currentData = config.value.data

    //   if (Array.isArray(rawData) && rawData.length > 0) {
    //     // 按活动数量排序，取前7名
    //     const sortedData = rawData.sort((a, b) => b.count - a.count).slice(0, 7)
    //     config.value.data = sortedData.map(item => ({
    //       name: item.section,
    //       value: parseInt(item.count)
    //     }))
    //   }
    // }

    const rawData = response.data

    if (Array.isArray(rawData) && rawData.length > 0) {
      // 按活动数量排序，取前7名
      const sortedData = rawData.sort((a, b) => b.count - a.count).slice(0, 7)
      config.value.data = sortedData.map(item => ({
        name: item.section,
        value: parseInt(item.count)
      }))
    }

  } catch (error) {
    console.error('获取科研活力数据失败:', error)
  }
}

onMounted(() => {
  getResearchVitality()
})
</script>
