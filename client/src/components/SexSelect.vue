<script setup>
import { ref, watch, defineProps, defineEmits } from 'vue';

const props = defineProps({
  value: {
    type: String,
    required: true
  }
});

const emit = defineEmits(['update:value']);

const selectedValue = ref(props.value);
const options = [
  {
    value: '男',
    label: '男'
  },
  {
    value: '女',
    label: '女'
  },
  {
    value: '保密',
    label: '保密'
  }
];
// 监听value的变化，更新选中的值
watch(() => props.value, (newValue) => {
  selectedValue.value = newValue;
});
// 监听选中的值的变化，更新父组件的value
watch(selectedValue, (newValue) => {
  emit('update:value', newValue);
});
</script>

<template>
  <el-select v-model="selectedValue">
    <el-option
        v-for="item in options"
        :key="item.value"
        :label="item.label"
        :value="item.value"
    ></el-option>
  </el-select>
</template>

<style scoped>
/* 样式代码 */
</style>