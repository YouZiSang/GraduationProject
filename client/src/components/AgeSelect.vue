<script setup>
import { ref, watch, defineProps, defineEmits } from 'vue';

const props = defineProps({
  value: {
    type: [Number, String],
    required: true,
  },
});

const selectedValue = ref(props.value);
const options = [];

for (let i = 8; i <= 100; i++) {
  const option = { value: i.toString(), label: i.toString() };
  options.push(option);
}

watch(() => props.value, (newValue) => {
  selectedValue.value = newValue;
});

const emit = defineEmits(['update:value']);

watch(selectedValue, (newValue) => {
  if (newValue >= 8 && newValue <= 100) {
    emit('update:value', newValue);
  } else {
    selectedValue.value = props.value;
  }
});
</script>

<template>
  <el-select v-model="selectedValue" @change="handleChange">
    <el-option
        v-for="item in options"
        :key="item.value"
        :label="item.label"
        :value="item.value"
    ></el-option>
  </el-select>
</template>