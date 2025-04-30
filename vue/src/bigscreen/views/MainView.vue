<template>
    <!-- 中间区域 -->
    <div class="module-box">
      <div style="flex:0 1 30%;">
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
        <dv-border-box13 style="width:100%;height:300px;" >
          <ConicalChart height="260px" />
        </dv-border-box13>
        <dv-border-box12 style="width:100%;height:300px;" >
          <dv-water-level-pond :config="config3" style="width:100%;height:300px;" />
        </dv-border-box12>
      </div>
    </div>

</template>

<script setup>
import {ref ,reactive} from 'vue'
import ConicalChart from './ConicalChart.vue'
import FlylineChart from './FlylineChart.vue'
import myChart from './myChart.vue'

const config1 = reactive({
  value: 66,
})

const config3 = reactive({
  data: [66, 45],
  shape: 'roundRect',
})
</script>


