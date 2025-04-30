<template>
  <div class="panel-padding">
    <dv-conical-column-chart :config="config" :style="{width:'100%',height:prop.height}" />
  </div>
</template>

<script setup>
import {ref,defineProps} from 'vue'
const prop = defineProps({
  height:{
    type:String
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
</script>
