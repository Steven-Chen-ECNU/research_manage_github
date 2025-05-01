<template>
  <div class="panel-padding">
    <dv-conical-column-chart :config="config" :style="{ width: '100%', height: prop.height }" />
  </div>
</template>

<script setup>
import { ref, defineProps, onMounted } from 'vue'
import axios from 'axios'

const prop = defineProps({
  height: {
    type: String
  }
})

const config = ref({
  data: [
    {
      name: '周口',
      value: 55,
    },
    {
      name: '南阳',
      value: 120,
    },
    {
      name: '西峡',
      value: 71,
    },
    {
      name: '驻马店',
      value: 66,
    },
    {
      name: '新乡',
      value: 80,
    },
    {
      name: '信阳',
      value: 35,
    },
    {
      name: '漯河',
      value: 15,
    },
  ],
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

// // 获取各区科研活力数据
// const getResearchVitality = async () => {
//   try {
//     const response = await axios.get('/dashboard/research-vitality')
//     const data = response.data
//     // 按活动数量排序，取前7名
//     const sortedData = data.sort((a, b) => b.count - a.count).slice(0, 7)
//     config.value.data = sortedData.map(item => ({
//       name: item.section,
//       value: item.count
//     }))
//   } catch (error) {
//     console.error('获取科研活力数据失败:', error)
//   }
// }

// onMounted(() => {
//   getResearchVitality()
// })
</script>
