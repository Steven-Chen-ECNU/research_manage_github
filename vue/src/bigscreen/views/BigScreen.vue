<template>
    <div class="bigscreen-wrapper">
      <div class="content bg">
        <MainView />
      </div>
    </div>
  </template>
  
  <script setup>
  import MainView from './MainView.vue'
  </script>
  
  <style scoped>
  .bigscreen-wrapper {
    width: 100%;
    height: 100vh;
    background: #000;
    color: #fff;
  }
  </style>