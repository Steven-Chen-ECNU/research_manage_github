<template>
    <dv-flyline-chart :config="config" :style="{width:'100%',height:prop.height}" />
</template>

<script setup>
import {ref} from 'vue'
const prop = defineProps({
  height:{
    type:String
  }
})
const config = ref({  //飞线图配置
        centerPoint: [0.48, 0.35], //中心点坐标
        points: [
          {
            position: [0.52, 0.235],
            text: '新乡'
          },
          {
            position: [0.43, 0.29],
            text: '焦作'
          },
          {
            position: [0.59, 0.35],
            text: '开封'
          },
          {
            position: [0.53, 0.47],
            text: '许昌'
          },
          {
            position: [0.45, 0.54],
            text: '平顶山'
          },
          {
            position: [0.36, 0.38],
            text: '洛阳'
          },
          {
            position: [0.62, 0.55],
            text: '周口'
          },
          {
            position: [0.56, 0.56],
            text: '漯河'
          },
          {
            position: [0.37, 0.66],
            text: '南阳'
          },
          {
            position: [0.55, 0.81],
            text: '信阳'
          },
          {
            position: [0.55, 0.67],
            text: '驻马店'
          },
          {
            position: [0.37, 0.29],
            text: '济源'
          },
          {
            position: [0.20, 0.36],
            text: '三门峡'
          },
          {
            position: [0.76, 0.41],
            text: '商丘'
          },
          {
            position: [0.59, 0.18],
            text: '鹤壁'
          },
          {
            position: [0.68, 0.17],
            text: '濮阳'
          },
          {
            position: [0.59, 0.10],
            text: '安阳'
          }
        ],
        lineWidth: 1, //飞线宽度 默认 1
        bgImgUrl: '/src/bigscreen/assets/img/map.jpg',//背景图url（河南省地图）
        centerPointImg: {
          url: '/src/bigscreen/assets/img/mapCenterPoint.png'
        },
        pointsImg: {
          url: '/src/bigscreen/assets/img/mapPoint.png'
        }
      });

</script>
