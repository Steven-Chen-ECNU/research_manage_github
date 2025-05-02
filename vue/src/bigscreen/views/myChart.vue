<template>
  <div style="width: 100%; height: 100%; display: flex; align-items: center; justify-content: center;">
    <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
    <div ref="chart" style="width: 100%; height: 100%; max-height: 100%; max-width: 100%;"></div>
  </div>
</template>
  
<script setup>
import { ref, reactive, onMounted } from "vue";
import * as echarts from "echarts"; //引入 echarts 核心模块
import 'echarts-gl';

const chart = ref();
const lists = ref([]);

onMounted(() => {
  chartInit();
});
const chartInit = () => {
  const myChart = echarts.init(chart.value);

  const option = {
    backgroundColor: '#000',
    globe: {
      baseTexture: '/src/bigscreen/assets/img/earth.jpg',
      heightTexture: '/src/bigscreen/assets/img/earth.jpg',
      displacementScale: 0.1,
      shading: 'realistic',
      environment: '/src/bigscreen/assets/img/starfield.jpg',
      realisticMaterial: {
        roughness: 0.2,
        metalness: 0
      },
      postEffect: {
        enable: true,
        SSAO: {
          enable: true,
          radius: 2,
          intensity: 1,
          quality: 'high'
        }
      },
      light: {
        ambient: {
          intensity: 0.1
        },
        main: {
          intensity: 1.5
        }
      },
      viewControl: {
        autoRotate: true,
        autoRotateSpeed: 10,
        distance: 150
      }
    }
  };
  myChart.setOption(option);
};
</script>
  
<style scoped>
</style>
