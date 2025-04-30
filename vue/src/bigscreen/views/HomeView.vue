<template>
  <dv-full-screen-container> 
    <!-- 结构分配 -->
    <dv-loading v-if="loading">正在加载...</dv-loading>
    <div v-else>
      <!-- 标头 -->
      <TitleView />
      <!-- 中间区域 -->
      <MainView />
      <!-- 底部部分 -->
      <FooterView />
    </div>
  </dv-full-screen-container>
</template>

<script setup>
import {ref,reactive,onMounted} from 'vue';
import TitleView from './TitleView.vue'
import MainView from './MainView.vue'
import FooterView from './FooterView.vue'

let loading = ref(true);

onMounted(()=>{
  setTimeout(()=>{
    loading.value = false;
  },1000)
})


</script>


