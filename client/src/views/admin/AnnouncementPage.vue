<script setup>
import {ref, onMounted, nextTick} from "vue"
import {Delete, Edit, Plus} from "@element-plus/icons-vue";
import Editor from "wangeditor";
import {
  addNoticeService,
  delNoticeService, editCarouselImgService,
  editNoticeService,
  getAllNoticeService,
  getCarouselImgService
} from "@/api/notice.js";
import {multiFileUpload, uploadSingleFile} from "@/api/upload.js";

const activeName = ref('noticeList')
const id = ref('')
const title = ref('')
const content = ref('')
let editor = null
const initWangEditor = (content) => {

  setTimeout(() => {
    if (!editor) {
      editor = new Editor('#editor')
      editor.config.placeholder = '请输入内容'
      editor.config.uploadFileName = 'file'
      editor.config.uploadVideoName = 'video'
      editor.config.uploadImgServer = 'http://localhost:8080/api/upload/wang/upload'
      editor.config.uploadVideoServer = 'http://localhost:8080/api/upload/wang/video'
      editor.config.height = 400;
      editor.create()

    }
    editor.txt.html(content)

  }, 0)
}

const noticeList = ref([])
const carouselImg = ref({
  firstImage: '',
  secondImage: '',
  thirdImage: '',
  fourthImage: ''
})

const getAllNotice = async () => {
  let res = await getAllNoticeService()
  noticeList.value = res.data
  console.log(res.data)
  // 在下一个 DOM 更新周期之后再去获取元素
  // await nextTick(() => {
  //   const elements = document.querySelectorAll('.collapse-content');
  //   elements.forEach(element => {
  //     let images = element.getElementsByTagName('img');
  //     for (let i = 0; i < images.length; i++) {
  //       let img = images[i];
  //       img.style.width = '300px'; // 设置宽度
  //       img.style.height = 'auto'; // 设置高度
  //     }
  //   });
  // });
}
const editorContent = ref('');
const add = async () => {
  operationType.value = 'add'
  title.value = ''; // 清空标题
  editorContent.value = ''; // 清空编辑器内容
  // 销毁旧的编辑器实例
  if (editor) {
    editor.destroy();
    editor = null; // 将编辑器实例置为null
  }
  initWangEditor('')
  dialogVisible.value = true
}

const publish = async () => {
  let params = {
    title: title.value,
    content: editor.txt.html()
  }
  dialogVisible.value = false
  await addNoticeService(params)
  await getAllNotice()
}
const dialogVisible = ref(false)
const operationType = ref(''); // 'edit' or 'add'
const edit = async (item) => {
  initWangEditor('')
  editorContent.value = ''; // 清空编辑器内容
  title.value = item.title; // 更新标题
  id.value = item.id
  // 销毁旧的编辑器实例
  if (editor) {
    editor.destroy();
    editor = null; // 将编辑器实例置为null
  }
  operationType.value = 'edit'; // 设置操作类型为编辑
  // 初始化编辑器并设置内容
  await initWangEditor(editorContent.value = item.content);
  dialogVisible.value = true

}
const editPublish = async () => {

  let params = {
    id: id.value,
    title: title.value,
    content: editor.txt.html()
  }
  await editNoticeService(params)
  await getAllNotice()
}
const del = async (id) => {
  await delNoticeService(id)
  await getAllNotice()
}
const  images = ref([])
const newImages = ref([])
const getCarouselImg = async () => {
  let res = await getCarouselImgService()
  carouselImg.value = res.data
  images.value.push(carouselImg.value.firstImage)
  images.value.push(carouselImg.value.secondImage)
  images.value.push(carouselImg.value.thirdImage)
  images.value.push(carouselImg.value.fourthImage)
}
const justSelect = (file, index) => {
  if (file.raw) {
    images.value[index] = URL.createObjectURL(file.raw);
    newImages.value[index] = file.raw;
  }
};
const imageProperties = ['firstImage', 'secondImage', 'thirdImage', 'fourthImage'];
const singleUpload = async (index) => {
  if (!newImages.value[index]) {
    ElMessage.error("请先选择图片")
    return;
  }
  let formData = new FormData();
  formData.append('file', newImages.value[index]);
  let res = await uploadSingleFile(formData)
  // 构建上传参数
  const propertyName = imageProperties[index];
  const params = {
    [propertyName]: res.data, // 使用计算属性名的语法
  };
  await editCarouselImgService(params)
}
const multipleSave = async () => {
  const uploadedImagesObj = {}; // 存储上传成功图片的对象
  if (newImages.value.length === 0) {
    ElMessage.error("请先选择图片")
    return;
  }
  // 遍历所有的新图片对象，依次上传它们
  for (let index = 0; index < newImages.value.length; index++) {
    const file = newImages.value[index];
    if (file) {
      let formData = new FormData();
      formData.append('file', file);
      let res = await multiFileUpload(formData);
      const imageUrl = res.data;

      // 去除首尾的方括号和引号
      const cleanedImageUrl = imageUrl.replace(/["\[]/g, '').replace(/["\]]/g, '');
      // 将图片URL和属性名存入uploadedImages对象
      const imageProperty = imageProperties[index];
      uploadedImagesObj[imageProperty] = cleanedImageUrl;
      await editCarouselImgService(uploadedImagesObj);
    }
  }

};
onMounted(async () => {
  await getAllNotice()
  await getCarouselImg()
})
</script>

<template>

  <el-tabs
      v-model="activeName"
      type="card"
      class="demo-tabs"
  >
    <el-tab-pane label="公告列表" name="noticeList">
      <div class="welcome-text">
        <span>Hi，</span><span
          style="font-size: 16px;color: orangered">admin</span><span>，欢迎来到发布通知公告页面</span>
      </div>
      <el-button type="primary" style="margin-bottom: 10px" @click="add">
        <el-icon style="color: white">
          <Plus/>
        </el-icon>
        添加
      </el-button>
      <el-collapse accordion style="width: 600px">
        <el-collapse-item :name="index" v-for="(item, index) in noticeList" :key="index" style="width: 900px">
          <template #title>
            <div style="width: 100%;">
              <p class="collapse-title">
                <i class="header-icon el-icon-question"></i>{{ item.title }}
              </p>
              <p class="collapse-date">
                {{ new Date(item.createdTime).toLocaleDateString('default', {month: '2-digit', day: '2-digit'}) }}
              </p>
            </div>
            <el-button type="primary" :icon="Edit" circle @click.stop="edit(item)"/>
            <el-button type="danger" :icon="Delete" circle @click.stop="del(item.id)" style="margin-right: 10px"/>
          </template>
          <div v-html="item.content" class="collapse-content">
          </div>
        </el-collapse-item>
      </el-collapse>
      <el-dialog v-model="dialogVisible" draggable>
        <slot name="title">
          <div style="font-size: 20px;text-align: center"> 发布通知公告</div>
        </slot>
        <div class="publish-notice-page">
          <div>
            <div class="content-notice">
              <span style="width: 110px">公告标题：</span>
              <el-input v-model="title" placeholder="请输入公告标题"></el-input>
            </div>
            <div class="content-notice">
              <span style="width: 150px">通知内容：</span>
              <div id="editor" v-html="editorContent">
              </div>
            </div>
            <div style="margin-left: 80px">
              <el-button v-if="operationType==='add'" @click="publish()">马上发布</el-button>
              <el-button v-if="operationType==='edit'" @click="editPublish">修改发布</el-button>
            </div>
          </div>
        </div>
      </el-dialog>
    </el-tab-pane>
    <el-tab-pane label="主页轮播图" name="tir">
      <div class="carousel-image">
        <div v-for="(image, index) in images" :key="index" style="border: #98d8cd 1px dashed;">
          <div >
            <div style="margin-left: 100px">
              <span style="text-align: center">{{ imageProperties[index] }}</span>
            </div>
            <el-upload
                :show-file-list="false"
                :on-change="(file) => justSelect(file, index)"
                :auto-upload="false">
              <el-image :src="image"></el-image>
            </el-upload>
          </div>
          <div style="text-align: center;margin-top: 20px;">
            <el-button @click="singleUpload(index)" style="width: 100px;font-size: 18px;margin-bottom: 20px">保存</el-button>
          </div>
        </div>
      </div>
      <el-button @click="multipleSave" style="margin-left: 40%;width: 100px;font-size: 18px;margin-top: 20px">全部保存</el-button>
    </el-tab-pane>
  </el-tabs>
</template>

<style scoped lang="scss">
.collapse-title {
  float: left;
  font-size: 17px;
  margin-left: 20px;
}

.collapse-date {
  text-align: right;
  font-size: 16px;
  padding: 0 10px;
}

.collapse-content {
  display: block;
  font-size: 18px;
  margin: 0 20px;
}

.publish-notice-page {
  padding: 0 20px;
}

.welcome-text {
  font-size: 13px;
  margin-bottom: 20px;
}

.content-notice {
  display: flex;
  margin: 20px 0;
}

.carousel-image {
  display: flex;

  .el-image {
    padding: 10px;
    width: 330px;
    height: auto;
  }
}

.image-view {
  display: block;
}
</style>