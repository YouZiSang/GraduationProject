<template>
  <el-config-provider :locale="locale">
    <router-view />
  </el-config-provider>
</template>
<script setup>
import { ref } from 'vue'
import { ElConfigProvider } from 'element-plus'
import zhCn from 'element-plus/dist/locale/zh-cn'

const locale = ref(zhCn)
</script>
<style>
body{
  background-color: #f0f2f5;
}
</style>