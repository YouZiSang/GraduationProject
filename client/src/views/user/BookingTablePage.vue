<script setup lang="ts">
import {getAppTableByUidService,updateAppStatusService} from "@/api/consultation/appoimtment.js";
import {evaluationImgService} from "@/api/orders.js";
import {updateConsultationStatusService,consultationEvaluationService} from "@/api/consultation/consultation.js";
import {computed, onMounted, ref} from "vue";
import {ElMessage, UploadFile} from "element-plus";
import {Camera, Delete, ZoomIn} from "@element-plus/icons-vue";
import {getId} from "@/utils/getCurrentUserInfo";
const today = new Date();
const year = today.getFullYear(); // 获取当前年份
const month = today.getMonth() + 1; // 获取当前月份，月份从0开始，所以加1
const date = today.getDate(); // 获取当前日
// 将月份和日期格式化为两位数，例如 '01', '21' 等
interface Appointment {
  cid: number;
  avatarUrl: string;
  name: string;
  sex: string;
  age: number;
  appointmentDate: string;
  status: string;
  email: string;
  phone: number
}


const appointmentInfo = ref()

const getAppTableByUid = async () => {
  let res = await getAppTableByUidService()
  appointmentInfo.value = res.data
}

const currentRow = ref(null)
const evaluation = async (row)=>{
  currentRow.value = row
  dialogVisible.value = true
}

let recognition = null;
const startRecognition = () => {
  if (!('webkitSpeechRecognition' in window)) {
    alert('您的浏览器不支持语音输入');
    return;
  }


  recognition.onerror = (event) => {
    console.error('语音识别发生错误：', event);
    stopRecognition();
  };

  recognition.start();
};

const stopRecognition = () => {
  if (recognition) {
    recognition.stop();
    recognition = null;
  }
};

let limit = 3
const fileList = ref([])
const hideBtn = ref(false)
const disabled = ref(false)
const content = ref('');
const attitude = ref(0);
const professional = ref(0)
const reliable = ref(0)
//评价图片
const evaluationMedia = ref([])
//
const mediaVisible = ref(false)
const dialogVideoUrl = ref('')
const dialogImageUrl = ref('')

const showBtn = computed(() => {
  return (
      content.value !== '' &&
      attitude.value !== 0 &&
      professional.value !== 0 &&
      reliable.value !== 0
  );
});
const handleFileChange = (file, fileList) => {
  file.progress = 0; // 初始化进度为0
  fileList.value = fileList

  // 更新fileList的引用
  hideBtn.value = fileList.value.length >= limit
  if (file.raw.type.startsWith('video/')) {
    // 如果选择的文件是视频文件，则显示视频预览
    file.url = URL.createObjectURL(file.raw);

    file.isVideo = true;
  } else {
    file.isVideo = false;

  }
};
import Breadcrumb from '@/components/Breadcrumb.vue';
const handlePictureCardPreview = (file) => {
  dialogImageUrl.value = file.isVideo ? '' : file.url
  dialogVideoUrl.value = file.isVideo ? file.url : ''
  mediaVisible.value = true
};

const handleRemove = (file: UploadFile) => {

  const index = fileList.value.findIndex((item) => item.uid === file.uid);
  if (index !== -1) {
    fileList.value.splice(index, 1);
  }

  //上传按钮
  hideBtn.value = fileList.value.length >= limit
}

const dialogVisible = ref(false)
const resetFormData = () => {
  fileList.value = [];
  content.value = '';
  reliable.value = 0;
  professional.value = 0;
  attitude.value = 0;
  // 其他需要重置的数据也可以在这里添加
  // ...
}

const publish = async () => {

  if (fileList.value.length > 0) {
    const formData = new FormData();
    fileList.value.forEach((file) => {
      formData.append('file', file.raw); // 将文件数据添加到 FormData 对象中
    });
    let res = await evaluationImgService(formData)
    evaluationMedia.value = res.data
  }

  let mediaValue = JSON.stringify(evaluationMedia.value);

  let params = {
    cid :currentRow.value.cid,
    media: mediaValue,
    consultationDate: currentRow.value.appointmentDate,
    content: content.value,
    professional: professional.value,
    reliable: reliable.value,
    attitude: attitude.value,
    evaId:getId()
  }
  //评价接口
  let res = await consultationEvaluationService(params)
  ElMessage.success(res.message)

  let consultationStatus = {
    uid :getId(),
    status: '已评价',
    cid: currentRow.value.cid,
    consultationDate: currentRow.value.appointmentDate,
    appointmentDate: currentRow.value.appointmentDate
  }

  await updateConsultationStatusService(consultationStatus)
  await updateAppStatusService(consultationStatus)
  await getAppTableByUid()
  resetFormData()
  dialogVisible.value = false
}

const cancel = () => {
  resetFormData()
  dialogVisible.value = false
}

const handleClose = () => {
  resetFormData()
  dialogVisible.value = false
}
onMounted(() => {
  getAppTableByUid()
  recognition = new webkitSpeechRecognition();
  recognition.continuous = true;
  recognition.interimResults = true; // 设置为false以避免中间结果触发onresult
  recognition.lang = 'zh-CN';
  recognition.onresult = (event) => {
    let result = '';
    for (let i = event.resultIndex; i < event.results.length; ++i) {
      if (event.results[i].isFinal) {
        result += event.results[i][0].transcript;
      }
    }
    if (result) {
      content.value += result;
    }
  };
})
</script>

<template>

  <div>预约表</div>
  <el-table :data="appointmentInfo" style="min-height: 700px">
    <el-table-column prop="appointmentType" label="预约咨询类型" width="120" align="center"></el-table-column>
    <el-table-column prop="avatarUrl" label="头像" width="120" align="center">
      <template #default="{row}">
        <el-avatar :src="row.avatarUrl" size="large"></el-avatar>
      </template>
    </el-table-column>
    <el-table-column prop="name" label="咨询师" width="160" align="center"></el-table-column>
    <el-table-column prop="sex" label="性别" width="120" align="center"></el-table-column>
    <el-table-column prop="age" label="年龄" width="120" align="center"></el-table-column>
    <el-table-column prop="appointmentDate" label="预约日期" width="160" align="center"></el-table-column>
    <el-table-column prop="email" label="电子邮箱" width="180" align="center"/>
    <el-table-column prop="status" label="状态" width="120" align="center">
      <template #default="{row}">
        <el-tag v-if="row.status !== '进行中'" type="warning" size="large">{{ row.status }}</el-tag>
        <el-tag v-else type="success" size="large">{{ row.status }}</el-tag>
      </template>
    </el-table-column>
    <el-table-column prop="" label="操作" width="200" align="center">
      <template #default="{row}">
        <el-button v-if="row.status === '咨询结束'" type="primary" @click="evaluation(row)">评价</el-button>
      </template>
    </el-table-column>
  </el-table>
  <el-dialog
      v-model="dialogVisible"
      title="评价"
      width="600"
      :before-close="handleClose"
  >

    <el-card class="card-appraise">
      <el-upload
          @update:fileList="fileList = $event"
          @remove="handleRemove"
          list-type="picture-card"
          :class="{uploadBtn:hideBtn}"
          :on-change="handleFileChange"
          :auto-upload="false"
          :limit
          :multiple="true"
      >
        <template #file="{file}">
          <div v-if="file.isVideo">
            <video controls width="120">
              <source :src="file.url" type="video/mp4">
            </video>
          </div>
          <div v-else>
            <img class="el-upload-list__item-thumbnail" :src="file.url" alt=""/>
          </div>
          <span class="el-upload-list__item-actions">
            <span
                v-if="!disabled"
                class="el-upload-list__item-preview"
                @click="handlePictureCardPreview(file)"
            >
              <el-icon><zoom-in/></el-icon>
            </span>
            <span
                v-if="!disabled"
                class="el-upload-list__item-delete"
                @click="handleRemove(<UploadFile>file)"
            >
              <el-icon><Delete/></el-icon>
            </span>
          </span>

        </template>
        <div v-if="fileList.length < limit">
          <el-icon size="80px">
            <!-- 使用你的Camera组件，或者替换为默认的icon -->
            <Camera/>
          </el-icon>
        </div>

      </el-upload>
    </el-card>
    <div class="evaluation-container">
      <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4 }" v-model="content"
                style="margin-bottom: 20px"></el-input>

      <div class="voice-container">
        <el-button @click="startRecognition" type="primary">语音输入</el-button>
        <el-button @click="stopRecognition" type="danger">结束</el-button>
      </div>

      <div class="rate-container">
        <div class="rate-item">
          <span>服务态度</span>
          <el-rate v-model="attitude" allow-half size="large" />
        </div>
        <div class="rate-item">
          <span>专业能力</span>
          <el-rate v-model="professional" allow-half size="large"/>
        </div>
        <div class="rate-item">
          <span>稳定可靠</span>
          <el-rate v-model="reliable" allow-half size="large"/>
        </div>
      </div>
      <div class="controller-container">
        <el-button v-if="showBtn" @click="publish" type="primary">发布</el-button>
        <el-button @click="cancel">取消</el-button>
      </div>
      <el-dialog v-model="mediaVisible">
        <div v-if="dialogImageUrl">
          <img :src="dialogImageUrl" alt="Preview Image"/>
        </div>
        <div v-if="dialogVideoUrl">
          <video controls>
            <source :src="dialogVideoUrl" type="video/mp4">
          </video>
        </div>
      </el-dialog>
    </div>
  </el-dialog>
</template>

<style scoped lang="scss">
.select-container {
  margin-bottom: 10px;
  display: flex;
  justify-content: flex-start;
}

.evaluation-container {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}

.voice-container {
  margin-bottom: 20px;

  .el-button {
    width: 80px;
  }
}

.rate-container {
  display: flex;
  flex-direction: column;
}

.rate-item {
  display: flex;
  align-items: center; /* 垂直居中对齐 */
  margin-bottom: 10px;
}

.rate-item:last-child {
  margin-bottom: 20px;
}

/* 可能需要额外的样式来调整文本和星星之间的间距 */
.rate-item span {
  margin-right: 10px; /* 根据需要调整 */
}

.card-appraise {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 20px;
  height: 200px; /* 或者一个具体的值，取决于你希望的高度 */
}
.controller-container {
  .el-button {
    width: 80px;
  }
}
</style>
<style>
.uploadBtn .el-upload--picture-card {
  display: none;
}
</style>