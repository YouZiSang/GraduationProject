<script setup>
import {ref, onMounted, computed} from "vue";
import {Collection, Female, Iphone, Male, Message, Postcard, Sunset, User, UserFilled} from "@element-plus/icons-vue";
import {getConsultantsEvaluationService} from "@/api/consultants.js";

const consultantsDetails = ref({})
const evaluationListDate = ref({})
const evaluationConsultants = async (cid) => {
  let res = await getConsultantsEvaluationService(cid)
  evaluationListDate.value = res.data

}

const isImage = (url) => ['.png', '.jpg', '.jpeg', '.gif'].some(extension => url.includes(extension));
const isVideo = (url) => ['.mp4', '.webm', '.ogg'].some(extension => url.includes(extension));
const getImagesUrl = (name) => {
  return new URL(name, import.meta.url).href
}
const imageUrl = "../../assets/images/noData.png"

const iconStyle = computed(() => {
  return {
    fontSize: '20px',
    marginRight: '10px'
  }
})
onMounted(() => {
  const params = JSON.parse(sessionStorage.getItem('consultantsParams'));
  if (params) {
    const details = {
      id: params.consultants_id,
      cid: params.consultants_cid,
      name: params.consultants_name,
      avatarUrl: params.consultants_avatarUrl,
      sex: params.consultants_sex,
      age: params.consultants_age,
      email: params.consultants_email,
      phone: params.consultants_phone,
      certificate: params.consultants_certificate,
      biography: params.consultants_biography,
    }
    consultantsDetails.value = details
    evaluationConsultants(details.cid)
  }
})
</script>

<template>
  <el-tabs type="border-card" class="demo-tabs">
    <el-tab-pane label="咨询师"  style="background-color: #BEE7E9;height: 660px">
      <div class="consultants-info">
        <div class="consultants-avatar">
          <el-image :src="consultantsDetails.avatarUrl" />
        </div>
      </div>
      <el-descriptions
          class="descriptions-info"
          size="large"
          border
      >
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">
              <el-icon :style="iconStyle">
                <User/>
              </el-icon>
              姓名
            </div>
          </template>
          {{ consultantsDetails.name }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">
              <el-icon :style="iconStyle">
                <Male/>
              </el-icon>
              <el-icon :style="iconStyle">
                <Female/>
              </el-icon>
              性别
            </div>
          </template>
          {{ consultantsDetails.sex }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">
              <el-icon :style="iconStyle">
                <Sunset/>
              </el-icon>
              年龄
            </div>
          </template>
          {{ consultantsDetails.age }}
        </el-descriptions-item>
        <el-descriptions-item label="电子邮箱">
          <template #label>
            <div class="cell-item">
              <el-icon :style="iconStyle">
                <Message/>
              </el-icon>
              电子邮箱
            </div>
          </template>
          {{ consultantsDetails.email }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">
              <el-icon :style="iconStyle">
                <Iphone/>
              </el-icon>
              电话
            </div>
          </template>
          {{ consultantsDetails.phone }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">
              <el-icon :style="iconStyle">
                <Collection/>
              </el-icon>
              证书
            </div>
          </template>
          {{ consultantsDetails.certificate }}
        </el-descriptions-item>
        <el-descriptions-item :span="3">
          <template #label>
            <div class="cell-item">
              <el-icon :style="iconStyle">
                <Postcard/>
              </el-icon>
              简介
            </div>
          </template>
          {{ consultantsDetails.biography }}
        </el-descriptions-item>
      </el-descriptions>
      <div class="text-p">
        <p>专业心理咨询师{{consultantsDetails.name}}为你服务</p>
      </div>
    </el-tab-pane>
    <el-tab-pane label="评价" style="background-color: #BEE7E9">
      <div style="display: flex;padding: 20px 0">
        <div class="evaluation-page">
          <div style="flex-direction: column">
            <div v-for="item in evaluationListDate" :key="item.id" class="evaluation-list">
              <div class="evaluation-content">
                <el-avatar :src="item.avatar" size="large"/>
                <div style="display: flex;flex-direction: column;">
                  <div class="name">{{ item.name }}</div>
                  <div class="content">
                    <div style="display: flex">
                      <div class="evaluation-date">
                        {{ new Date(item.createdTime).toLocaleDateString('zh-CN', {month: 'numeric', day: 'numeric'}) }}
                      </div>
                    </div>
                    <div class="text-content">
                      {{ item.content }}
                    </div>
                    <div class="media-content">
                      <div v-for="media in item.media.split(',')" :key="media.trim()">
                        <!-- 检查媒体类型并渲染相应组件 -->
                        <template v-if="isImage(media.trim())">
                          <el-image :src="media.trim()"
                                    :preview-src-list="[media.trim()]">>
                          </el-image>
                        </template>
                        <template v-else-if="isVideo(media.trim())">
                          <video controls v-bind:src="media.trim()">
                          </video>
                        </template>
                      </div>
                    </div>
                    <div class="evaluation-score">
                      服务态度：
                      <el-rate v-model="item.attitude" disabled
                               show-score text-color="#ff9900"
                               score-template="{value} 分"></el-rate>
                    </div>
                    <div class="evaluation-score">
                      专业能力：
                      <el-rate v-model="item.professional" disabled
                               show-score text-color="#ff9900"
                               score-template="{value} 分"></el-rate>
                    </div>
                    <div class="evaluation-score">
                      稳定可靠：
                      <el-rate v-model="item.reliable" disabled
                               show-score text-color="#ff9900"
                               score-template="{value} 分"></el-rate>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="bottom-news">
              <div class="text-news">
                <span>暂时没有新的评价了</span>
              </div>
              <div>
                <el-image :src="getImagesUrl(imageUrl)" style="width: 100px;height: 100px"></el-image>
              </div>
            </div>
          </div>
        </div>
        <div class="ads-container">
          <div class="ads">
            <p>心灵疗愈</p>
            <p>专业心理咨询师助您</p>
            <p>探索内心深处</p>
            <p>释放压力</p>
            <p>重拾和谐生活</p>
          </div>
        </div>

      </div>
    </el-tab-pane>
  </el-tabs>
</template>

<style scoped lang="scss">
.consultants-info {
  margin-left: 40%;
  margin-bottom: 50px;
}

.consultants-avatar {
  max-width: 200px;
  max-height: 200px;
  margin-bottom: 20px;
  .el-image{
    margin-top: 20px;
    max-width: 200px;
    max-height: 200px;
  }
}

.descriptions-info {
  width: 1000px;
  margin: 0 auto;
}
.text-p{
  margin-left: 30%;
  margin-top: 80px;
  font-size: 40px;
}
.evaluation-page {
  display: flex;
  margin-left: 10%;
  background-color: white;
  width: 700px;
  box-shadow: #8c939d 1px 1px 5px;
  border-radius: 10px;
  padding: 20px 50px;

}

.evaluation-list {
  display: flex;
  flex-direction: column;
  margin-bottom: 30px;
}

.evaluation-content {
  display: flex;
  align-items: flex-start;
  margin-bottom: 10px;
}

.name {
  margin-top: 15px;
  margin-left: 20px;
  font-size: 18px;
}

.content {
  padding: 20px;
  display: flex;
  flex-direction: column;
  min-width: 600px;
  min-height: 200px;
  max-width: 700px;
  max-height: 500px;
  border-radius: 8px;
  box-shadow: #8c939d 1px 1px 5px;
  margin-left: 20px;
  margin-top: 20px;


  .evaluation-date {
    font-size: 13px;
    margin-bottom: 20px;
    color: #8c939d;
  }

  .text-content {
    min-width: 300px;
    margin-right: 30px;
  }

  .media-content {
    margin-top: 20px;
    display: flex;
    margin-bottom: 20px;
  }

  .el-image {
    min-width: 150px;
    min-height: 150px;
    max-width: 200px;
    max-height: 200px;
    margin-right: 15px;
    border-radius: 5px;
  }

  video {
    min-width: 150px;
    min-height: 150px;
    max-width: 200px;
    max-height: 200px;
    margin-right: 15px;
    border-radius: 5px;
  }
}

.evaluation-score {
  display: flex;
  align-items: center;
}

.bottom-news {
  display: flex;
  align-items: center;
  margin-left: 200px;
  font-size: 20px;

}

.text-news {
  span {
    font-size: 20px;
    color: white;
    filter: drop-shadow(1px 1px 10px #232323);;
  }
}
.demo-tabs{
  background-color: #fdebdb ;
  min-height: 670px;
}
.demo-tabs > .el-tabs__content {
  padding: 32px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}

.demo-tabs .custom-tabs-label .el-icon {
  vertical-align: middle;
}

.demo-tabs .custom-tabs-label span {
  vertical-align: middle;
  margin-left: 4px;
}

.el-descriptions {
  margin-top: 20px;
}

.cell-item {
  display: flex;
  align-items: center;
}

.margin-top {
  margin-top: 20px;
}
.ads-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  grid-gap: 20px; /* 设置列与列之间的间隔 */
}
.ads{
  margin-top: 0;
  margin-left: 40px;
  font-size: 40px;
  color: #F4606C;
  filter: drop-shadow(1px 1px 1px #232323);;
}

</style>