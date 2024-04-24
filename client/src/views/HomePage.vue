<script setup>
import {ref, onMounted} from 'vue'
import {getAllNoticeService, getCarouselImgService, getEvaluationHighService} from "@/api/notice.js";
import {Tickets} from "@element-plus/icons-vue";
import {useRouter} from "vue-router";

const noticeList = ref([])
const images = ref([])
const carouselImg = ref({
  firstImage: '',
  secondImage: '',
  thirdImage: '',
  fourthImage: ''
})

const highEvaluation = ref('')
const getEvaluationHigh = async () => {
  let res = await getEvaluationHighService()
  highEvaluation.value = res.data
}
const getAllNotice = async () => {
  let res = await getAllNoticeService()
  noticeList.value = res.data.slice(0, 5);
}
const getCarouselImg = async () => {
  let res = await getCarouselImgService()
  carouselImg.value = res.data
  images.value.push(carouselImg.value.firstImage)
  images.value.push(carouselImg.value.secondImage)
  images.value.push(carouselImg.value.thirdImage)
  images.value.push(carouselImg.value.fourthImage)
}
const router = useRouter();
const selectDetail = (item) => {
  sessionStorage.setItem('noticeNew', JSON.stringify(
      {
        title: item.title,
        content: item.content,
        createdTime: item.createdTime
      }
  ));
router.push('noticeDetail', {title: item.title})
}
onMounted(async () => {
  await getCarouselImg()
  await getAllNotice()
  await getEvaluationHigh()
})
</script>

<template>
  <div style="background-color: #dfe6f6;">
    <div style="display: flex">
      <div class="announcement">
        <div class="announcement-title">
          <el-icon>
            <Tickets/>
          </el-icon>
          通知公告
        </div>
        <div class="announcement-content" v-for="item in noticeList">
          <div class="content" @click="selectDetail(item)">
            <span>{{ item.title }}</span>
          </div>
          <div class="date-content">
            {{ new Date(item.createdTime).toLocaleDateString('default', {month: '2-digit', day: '2-digit'}) }}
          </div>
        </div>
      </div>
      <div>
        <el-carousel :interval="5000" arrow="always" style="width: 800px;margin: auto" height="400px">
          <el-carousel-item v-for="(index,item) in images" :key="index" class="carousel-img">
            <el-image :src="index" :fit="'contain'" @click="c(item)"></el-image>
          </el-carousel-item>
        </el-carousel>
      </div>
    </div>
    <div style="margin-top: 50px;margin-left: 100px">
    <span style="font-size: 20px">目前在该平台有<span style="font-size: 25px;margin:0 15px;color: coral">
      {{ highEvaluation }}
    </span>人心理问题得到解决</span>
    </div>
    <div class="wave-effect">
      <span style="font-family:STXingkai;color: orangered">开启心灵对话</span>
      <span style="font-family:STXingkai;color: #8CC7B5">拥抱健康生活</span>
      <span style="font-family:STXingkai;color: #E6CEAC">体验我们的心理咨询服务平台</span>
      <span style="font-family:STXingkai;color: #BEE7E9">让专业的心理支持触手可及</span>
    </div>
  </div>
</template>

<style scoped>
.announcement {
  background-color: #dfe6f6;
  width: 400px;
  margin-right: 30px;

}

.announcement-title {
//background-color: #fdebdb; text-align: center;
  font-size: 20px;
  padding: 10px;

}

.announcement-content {
  display: flex;
  justify-items: center;
}

.content {
  padding: 0 10px;
  width: 300px;

  height: 40px;
  display: flex;
  align-items: center;
  border-bottom: 2px solid white;

  span {
    white-space: nowrap; /* 保持文本在一行显示 */
    overflow: hidden; /* 隐藏超出容器的文本 */
    text-overflow: ellipsis;
  }
}

.date-content {
  display: flex;
  align-items: center;
  padding: 0 10px;
  background-color: #A0EEE1;
  margin-top: 10px;
  border-bottom: 2px solid white;
}

.el-carousel__item h3 {
  color: #475669;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
  text-align: center;
}

.carousel-img {
  .el-image {
    width: 100%;
    height: 100%;
  }
}

.el-carousel__item:nth-child(n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}

@keyframes wave-animation {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-30px);
  }
}

.wave-effect {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px;
  position: relative;
  overflow: hidden;
}

.wave-effect span {
  position: relative;
  animation: wave-animation 2s ease-in-out infinite;
  font-size: 35px;
  font-weight: bold;
  margin-right: 20px;
}

.wave-effect span:nth-child(odd) {
  animation-delay: -1s;
}
</style>