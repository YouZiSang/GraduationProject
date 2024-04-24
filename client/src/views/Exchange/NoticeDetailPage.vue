<script setup>
import { ref, onMounted ,nextTick} from 'vue'

const title = ref('')
const content = ref('')
const createdTime = ref('')
onMounted(() => {
  const params = JSON.parse(sessionStorage.getItem('noticeNew'));
  if (params) {
    title.value = params.title
    content.value = params.content
    createdTime.value = params.createdTime
  }
  // 在下一个 DOM 更新周期之后再去获取元素
   nextTick(() => {
    const elements = document.querySelectorAll('.notice-content');
    elements.forEach(element => {
      let images = element.getElementsByTagName('img');
      for (let i = 0; i < images.length; i++) {
        let img = images[i];
        img.style.width = '300px'; // 设置宽度
        img.style.height = 'auto'; // 设置高度
      }
    });
  });
})
</script>

<template>
  <div>
    <p>公告通知</p>
    <div style="text-align: center" class="notice">
      <div style="font-size: 24px;margin-bottom: 30px">
        {{title}}
      </div>
      <div style="text-align: left">
        <div v-html="content" class="notice-content">

        </div>
      </div>

    </div>
  </div>
</template>

<style scoped lang="scss">
.notice{
  width: 800px;
  padding: 20px;
  min-height: 600px;
  border: #A0EEE1 1px solid;
  box-shadow: #8c939d 0px 0px 10px;
  margin: 0 auto;
}
</style>