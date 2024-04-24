<template>
  <el-breadcrumb separator=">" style="margin-bottom: 10px">
    <el-breadcrumb-item v-for="(crumb, index) in breadcrumbs" :key="index" :to="crumb.to">
      {{ crumb.label }}
    </el-breadcrumb-item>
  </el-breadcrumb>
</template>

<script setup>
import { computed } from 'vue';
import { useRoute } from 'vue-router';

const breadcrumbs = computed(() => {
  const route = useRoute();

  // 检查是否存在路由信息
  if (!route || !route.matched) {
    return [];
  }

  const matchedRoutes = route.matched;
  const crumbs = [];

  // 添加首页
  crumbs.push({ label: '首页', to: '/' });

  // 遍历路由层级，构建面包屑导航
  for (const route of matchedRoutes) {
    if (route.name) {
      crumbs.push({ label: route.name, to: route.path });
    }
  }

  return crumbs;
});
</script>